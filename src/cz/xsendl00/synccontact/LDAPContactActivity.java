package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.ContactRowComparator;
import cz.xsendl00.synccontact.utils.Utils;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;

/**
 * Activity for data from server.
 * 
 * @author portilo
 * 
 */
public class LDAPContactActivity extends Activity {

  private static final String TAG = "LDAPContactActivity";

  private final Handler handler = new Handler();
  private ProgressDialog progressDialog;
  private static Pair pair;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ldap_contact);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    if (pair == null) {
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
    actionBar.setHomeButtonEnabled(false);
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

  /**
   * Data from sever are loaded. Background task finished.
   * 
   * @param pair
   *          data of contact and group from server
   */
  public void onTaskCompleted(Pair pair) {
    
    this.setPair(pair);
    Log.i(TAG, "pai size:"+pair.getContactList().size());
    init();
   // ContactLDAPFragment fragment = (ContactLDAPFragment) getFragmentManager().findFragmentByTag("CONTACT_LDAP");
   // fragment.getAdapter().notifyDataSetChanged();
  }

  /**
   * Contact and group data.
   * 
   * @return data.
   */
  public Pair getPair() {
    return pair;
  }

  /**
   * Set contact data and group data.
   * 
   * @param pair
   *          New data.
   */
  public void setPair(Pair pair) {
    LDAPContactActivity.pair = pair;
  }

  private class DownloadTask extends AsyncTask<Void, Void, Pair> {
    private Activity activity;

    public DownloadTask(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Pair doInBackground(Void... params) {
      Pair p = new Pair();
      p.setContactList(ServerUtilities.fetchLDAPContacts(new ServerInstance(
          AccountData.getAccountData(getApplicationContext())),
          getApplicationContext(), handler));
      Collections.sort(p.getContactList(), new ContactRowComparator());
      return p;
    }

    protected void onPostExecute(Pair p) {
      if (LDAPContactActivity.this.progressDialog != null) {
        LDAPContactActivity.this.progressDialog.dismiss();
      }
      Log.i(TAG, "Data from server are downloaded.");
      ((LDAPContactActivity) activity).onTaskCompleted(p);
    }

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
    
    progressDialog = ProgressDialog.show(LDAPContactActivity.this,
        Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);
    // import pair.getContactList() form server do db -> must be created new contact and data
    new ImportTask(LDAPContactActivity.this).execute();
    
  }
  
  private class ImportTask extends AsyncTask<Void, Void, Pair> {
    private Activity activity;

    public ImportTask(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Pair doInBackground(Void... params) {
      HelperSQL db = new HelperSQL(activity);
      Utils util = new Utils();
      Map<String, ContactRow> dbContact =  db.getAllContactsMap();
      ArrayList<ContactRow> contactRows = new ArrayList<ContactRow>();
      contactRows.addAll(pair.getContactList());
      final List<ContactRow> intersection = util.intersectionDifference(contactRows, dbContact);
      Log.i(TAG, "intersection:" + intersection.size() + ", contactRows:" + contactRows.size() + ", dbContact:" + dbContact.size());
      
      // intersection-> must be sign as sync
      new Thread(new Runnable() {
        public void run() {
          HelperSQL db = new HelperSQL(activity);
          db.updateContactsSync(intersection, true);
        }
      }).start();
      
      for (ContactRow contactRow : contactRows) {
        GoogleContact googleContact =  new ServerUtilities().fetchLDAPContact(new ServerInstance(AccountData.getAccountData(getApplicationContext())), getApplicationContext(), handler, contactRow.getUuid());
        Log.i(TAG, googleContact.getUuid());
      }
      return pair;
    }

    protected void onPostExecute(Pair p) {
      if (LDAPContactActivity.this.progressDialog != null) {
        LDAPContactActivity.this.progressDialog.dismiss();
      }
      Log.i(TAG, "Data from server are downloaded.");
      ((LDAPContactActivity) activity).onTaskCompleted(p);
    }

    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
}
