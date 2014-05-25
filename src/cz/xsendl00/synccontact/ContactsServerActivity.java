package cz.xsendl00.synccontact;

import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.api.BackgroundExecutor;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import cz.xsendl00.synccontact.activity.fragment.ContactServerFragment;
import cz.xsendl00.synccontact.activity.fragment.GroupServerFragment;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Activity for data from server.
 *
 * @author portilo
 */
@EActivity(R.layout.activity_contacts_server)
public class ContactsServerActivity extends Activity {

  @Bean
  protected Utils util;
  private static final String TAG = "ContactsServerActivity";

  private final Handler handler = new Handler();
  private ContactManager contactManager;
  private boolean first = false;
  private Menu mMenu;
  private ProgressDialog progressDialog;
  public boolean importAll = false;

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
    actionBar.addTab(actionBar.newTab()
        .setText(getString(R.string.def_group))
        .setIcon(R.drawable.ic_action_group)
        .setTabListener(
            new MyTabListener<GroupServerFragment>(this, "GROUP_LDAP", GroupServerFragment.class)));
    actionBar.addTab(actionBar.newTab()
        .setText(getString(R.string.def_contact))
        .setIcon(R.drawable.ic_action_person)
        .setTabListener(
            new MyTabListener<ContactServerFragment>(this, "CONTACT_LDAP",
                ContactServerFragment.class)));
  }

  /**
   * Download data from server.
   */
  @Background(id = "loadData")
  protected void loadData() {
    contactManager.initContactsServer(handler);
    contactManager.initGroupsServer(handler);
    contactManager.getServerContact2Import();
    init();
  }

  /**
   * Update data, call by icon (refresh) in menu.
   */
  @Background(id = "loadData")
  public void reinitData() {
    setRefreshActionButtonState(true);
    contactManager.initContactsServer(handler);
    contactManager.initGroupsServer(handler);
    contactManager.getServerContact2Import();
    fragmnetCall();
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

  @UiThread
  public void onImportCompleted() {
    progressDialog.dismiss();
    contactManager.setLocalContactsInit(false);
    contactManager.getLocalContacts();
    if (first) {
      Editor editor = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE).edit();
      editor.putBoolean(Constants.PREFS_START_FIRST, false);
      editor.commit();
      Intent intent = new Intent(this, MainActivity_.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }
  }

  /**
   * Import selected contact form server to android.
   *
   * @param view unused
   */
  public void go2InfoMerge(@SuppressWarnings("unused") View view) {
    if (contactManager.getContactsServer().isEmpty()) {
      onImportCompleted();
    } else {
      progressDialog = new ProgressDialog(ContactsServerActivity.this);
      progressDialog.setTitle(getText(R.string.progress_importing));
      progressDialog.setMessage(getText(R.string.progress_importing_text));
      progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      progressDialog.setProgress(0);
      progressDialog.setCanceledOnTouchOutside(false);
      progressDialog.show();
      doInBackground();
    }

  }

  Handler handler1 = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      progressDialog.incrementProgressBy(1);
      // if (progressDialog.getProgress() >= progressDialog.getMax()) {
      // //msgWorking.setText("Done");
      // progressDialog.dismiss();
      // } else {
      // //msgWorking.setText("Working..." +
      // // progressDialog.getProgress());
    }
  };

  @Background
  protected void doInBackground() {
    final List<ContactRow> intersection = util.intersectionDifference(
        contactManager.getContactsServer(), contactManager.getLocalContacts());
    Log.i(TAG, "intersection:" + intersection.size() + ", contactlocal:"
        + contactManager.getLocalContacts().size() + ", dbContact:"
        + contactManager.getContactsServer().size());
    final Map<String, GoogleContact> contacts = new ServerUtilities().fetchServerContacts(
        new ServerInstance(AccountData.getAccountData(getApplicationContext())),
        getApplicationContext(), handler, intersection);
    progressDialog.setMax(contacts.size());
    Thread thread = new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          new AndroidDB().addContacts2Database(getApplicationContext(), contacts, handler1);
        } catch (RemoteException e) {
          e.printStackTrace();
        } catch (OperationApplicationException e) {
          e.printStackTrace();
        }
      }
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    onImportCompleted();
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
    // setRefreshActionButtonState(false);
  }

  @Override
  protected void onStop() {
    super.onStop();
    boolean mayInterruptIfRunning = true;
    BackgroundExecutor.cancelAll("loadData", mayInterruptIfRunning);
  }

  @UiThread
  public void fragmnetCall() {
    GroupServerFragment f2 = (GroupServerFragment) getFragmentManager().findFragmentByTag(
        "GROUP_LDAP");
    if (f2 != null) {
      // f2.updateAdapter();
    }
    ContactServerFragment f1 = (ContactServerFragment) getFragmentManager().findFragmentByTag(
        "CONTACT_LDAP");
    if (f1 != null) {
      f1.updateAdapter(); // Your method of the fragment
    }
    Log.i(TAG, "fragmnetCall");
  }
}
