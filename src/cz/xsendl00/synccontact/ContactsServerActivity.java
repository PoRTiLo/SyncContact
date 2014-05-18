package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import cz.xsendl00.synccontact.activity.fragment.ContactServerFragment;
import cz.xsendl00.synccontact.activity.fragment.GroupServerFragment;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Activity for data from server.
 *
 * @author portilo
 *
 */
@EActivity(R.layout.activity_contacts_server)
public class ContactsServerActivity extends Activity {

  @Bean
  protected Utils util;
  private static final String TAG = "ContactsServerActivity";

  /**
   * ProgressBar show by loading data.
   */
  @ViewById(R.id.activit_contacts_server_layout)
  protected LinearLayout linearLayoutProgress;
  private final Handler handler = new Handler();
  private ProgressDialog progressDialog;
  private ContactManager contactManager;
  private boolean first = false;
  private Menu mMenu;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    Intent intent = getIntent();
    first = intent.getBooleanExtra(Constants.INTENT_FIRST, false);

    contactManager = ContactManager.getInstance(getApplicationContext());
    if (!contactManager.isContactsServerInit() || !contactManager.isGroupsServerInit()) {
      loadData();
    } else {
      init();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.mMenu = menu;
    if (first) {
      getMenuInflater().inflate(R.menu.contacts_menu_server_simply, menu);
    } else {
      getMenuInflater().inflate(R.menu.contacts_menu_server, menu);
    }
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Initialize GUI.
   */
  @UiThread
  protected void init() {
    ActionBar actionBar = getActionBar();
    actionBar.removeAllTabs();
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    actionBar.addTab(actionBar
        .newTab()
        .setText(getString(R.string.def_group))
        .setIcon(R.drawable.ic_action_group)
        .setTabListener(
            new MyTabListener<GroupServerFragment>(this, "GROUP_LDAP",
                GroupServerFragment.class))
         );
    actionBar.addTab(actionBar
        .newTab()
        .setText(getString(R.string.def_contact))
        .setIcon(R.drawable.ic_action_person)
        .setTabListener(
            new MyTabListener<ContactServerFragment>(this, "CONTACT_LDAP",
                ContactServerFragment.class))
        );
    linearLayoutProgress.setVisibility(View.GONE);
  }

  /**
   * Download data from server.
   */
  @Background(id = "loadData")
  protected void loadData() {
    contactManager.initContactsServer(handler);
    contactManager.initGroupsServer(handler);
    init();
  }

  /**
   * Update data, call by icon (refresh) in menu.
   */
  @Background(id  = "loadData")
  public void reinitData() {
    setRefreshActionButtonState(true);
    contactManager.initContactsServer(handler);
    contactManager.initGroupsServer(handler);
    setRefreshActionButtonState(false);
  }

  /**
   * Show ProgressBar instead of button for refresh (R.id.action_add_group).
   *
   * @param refreshing true/false - show/hide
   */
  @UiThread
  protected void setRefreshActionButtonState(final boolean refreshing) {
    if (mMenu != null) {
      final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);
      if (refreshItem != null) {
        if (refreshing) {
          refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
        } else {
          refreshItem.setActionView(null);
        }
      }
    }
  }

  public void onImportCompleted() {
    Editor editor = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE).edit();
    editor.putBoolean(Constants.PREFS_START_FIRST, false);
    editor.commit();
    Intent intent = new Intent(this, MainActivity_.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
        | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  /**
   * Import selected contact form server to android.
   * @param view unused
   */
  public void go2InfoMerge(@SuppressWarnings("unused") View view) {
    if (contactManager.getContactsServer().isEmpty()) {
      onImportCompleted();
    } else {
      progressDialog = ProgressDialog.show(ContactsServerActivity.this,
          Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);
      // import pair.getContactList() form server do db -> must be created new contact and data
      new ImportTask(ContactsServerActivity.this).execute();
    }

  }

  private class ImportTask extends AsyncTask<Void, Void, Boolean> {
    private Activity activity;

    public ImportTask(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      HelperSQL db = new HelperSQL(activity);
      Map<String, ContactRow> dbContact =  db.getAllContactsMap();
      ArrayList<ContactRow> contactRows = new ArrayList<ContactRow>();
      contactRows.addAll(contactManager.getContactsServer());
      final List<ContactRow> intersection = util.intersectionDifference(contactRows, dbContact);
      Log.i(TAG, "intersection:" + intersection.size() + ", contactRows:" + contactRows.size()
          + ", dbContact:" + dbContact.size());

      // intersection-> must be sign as sync
      new Thread(new Runnable() {
        @Override
        public void run() {
          HelperSQL database = new HelperSQL(activity);
          database.updateContactsSync(intersection, true);
        }
      }).start();

      for (ContactRow contactRow : contactRows) {
        if (contactRow.isSync()) {
          GoogleContact googleContact =  new ServerUtilities().fetchLDAPContact(
              new ServerInstance(AccountData.getAccountData(getApplicationContext())),
              getApplicationContext(), handler, contactRow.getUuid());
          Log.i(TAG, googleContact.getUuid());
        }
      }
      return null;
    }

    @Override
    protected void onPostExecute(Boolean p) {
      if (ContactsServerActivity.this.progressDialog != null) {
        ContactsServerActivity.this.progressDialog.dismiss();
      }
      Log.i(TAG, "Data from server are downloaded.");
      if (first) {
        ((ContactsServerActivity) activity).onImportCompleted();
      } else {
        //((ContactsServerActivity) activity).onTaskCompleted();
      }
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }


  /**
   * First run this application.
   *
   * @return true first run, false not first run.
   */
  public boolean isFirst() {
    return first;
  }

  /**
   * If pressed back, background threads will be stopped.
   */
  @Override
  public void onPause() {
    super.onPause();
    Log.i(TAG, "back press");
    boolean mayInterruptIfRunning = true;
    BackgroundExecutor.cancelAll("loadData", mayInterruptIfRunning);
    Log.i(TAG, "back press - delete");
    setRefreshActionButtonState(false);
  }

  @Override
  protected void onStop() {
    super.onStop();
    boolean mayInterruptIfRunning = true;
    BackgroundExecutor.cancelAll("loadData", mayInterruptIfRunning);
  }
}
