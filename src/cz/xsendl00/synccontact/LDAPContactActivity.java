package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.EActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import cz.xsendl00.synccontact.activity.fragment.ContactLDAPFragment;
import cz.xsendl00.synccontact.activity.fragment.GroupLDAPFragment;
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
@EActivity(R.layout.activity_ldap_contact)
public class LDAPContactActivity extends Activity {

  private static final String TAG = "LDAPContactActivity";

  private final Handler handler = new Handler();
  private ProgressDialog progressDialog;
  private ContactManager contactManager;
  private boolean first = false;

  public boolean isFirst() {
    return first;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    Intent intent = getIntent();
    first = intent.getBooleanExtra(Constants.INTENT_FIRST, false);

    contactManager = ContactManager.getInstance(getApplicationContext());
    if (!contactManager.isContactsServerInit()) {
      progressDialog = ProgressDialog.show(LDAPContactActivity.this,
          Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);
      new DownloadTask(LDAPContactActivity.this).execute();
    } else {
      init();
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.contacts_menu_ldap, menu);
    return super.onCreateOptionsMenu(menu);
  }

  private void init() {
    ActionBar actionBar = getActionBar();
    actionBar.removeAllTabs();
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    actionBar.addTab(actionBar
        .newTab()
        .setText("GROUP")
        .setTabListener(
            new MyTabListener<GroupLDAPFragment>(this, "GROUP_LDAP",
                GroupLDAPFragment.class)).setIcon(R.drawable.ic_action_group));
    actionBar.addTab(actionBar
        .newTab()
        .setText("CONTACT")
        .setTabListener(
            new MyTabListener<ContactLDAPFragment>(this, "CONTACT_LDAP",
                ContactLDAPFragment.class))
        .setIcon(R.drawable.ic_action_person));
  }

  public void onImportCompleted() {
    Intent intent = new Intent(this, MainActivity_.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
        | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  /**
   * Data from sever are loaded. Background task finished.
   */
  public void onTaskCompleted() {
    init();
  }


  private class DownloadTask extends AsyncTask<Void, Void, Boolean> {
    private Activity activity;

    public DownloadTask(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      contactManager.initContactsServer(handler);
      return null;
    }

    @Override
    protected void onPostExecute(Boolean p) {
      if (LDAPContactActivity.this.progressDialog != null) {
        LDAPContactActivity.this.progressDialog.dismiss();
      }
      Log.i(TAG, "Data from server are downloaded.");
      ((LDAPContactActivity) activity).onTaskCompleted();
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }

  /**
   * Re-download data from LDAP server.
   */
  public void update() {
    new DownloadTask(LDAPContactActivity.this).execute();
  }

  /**
   * Import selected contact form server to android.
   * @param view
   */
  public void mainActivity(View view) {
    if (contactManager.getContactsServer().isEmpty()) {
      onImportCompleted();
    } else {
      progressDialog = ProgressDialog.show(LDAPContactActivity.this,
          Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);
      // import pair.getContactList() form server do db -> must be created new contact and data
      new ImportTask(LDAPContactActivity.this).execute();
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
      Utils util = new Utils();
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
      if (LDAPContactActivity.this.progressDialog != null) {
        LDAPContactActivity.this.progressDialog.dismiss();
      }
      Log.i(TAG, "Data from server are downloaded.");
      if (first) {
        ((LDAPContactActivity) activity).onImportCompleted();
      } else {
        ((LDAPContactActivity) activity).onTaskCompleted();
      }
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
}
