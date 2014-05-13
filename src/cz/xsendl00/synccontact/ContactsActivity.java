package cz.xsendl00.synccontact;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.activity.first.InfoMergeActivity;
import cz.xsendl00.synccontact.activity.fragment.ContactFragment;
import cz.xsendl00.synccontact.activity.fragment.GroupFragment;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.Synchronization;
import cz.xsendl00.synccontact.utils.Constants;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.Menu;
import android.view.View;

/**
 * Contact activity.
 * @author portilo
 *
 */
public class ContactsActivity extends Activity {
  private static final String TAG = "ContactsActivity";

  private final Handler handler = new Handler();
  private ProgressDialog progressDialog;
  private ContactManager contactManager;
  private boolean first = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_contact);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    Intent intent = getIntent();
    first = intent.getBooleanExtra("FIRST", false);
    contactManager = ContactManager.getInstance(getApplicationContext());
    if (!contactManager.isContactListInit() || !contactManager.isGroupListInit()) {
      progressDialog = ProgressDialog.show(ContactsActivity.this,
          Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);
      if (first) {
        new LoadTask(ContactsActivity.this).execute();
      } else {
        new LoadTask(ContactsActivity.this).execute();
        //new LoadTaskSimply(ContactsActivity.this).execute();
      }
    } else {
      init();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    if (first) {
      // TODO; 
      getMenuInflater().inflate(R.menu.contacts_menu_simply, menu);
    } else {
      getMenuInflater().inflate(R.menu.contacts_menu, menu);
    }

    return super.onCreateOptionsMenu(menu);
  }

  private void init() {
    ActionBar actionBar = getActionBar();
    actionBar.removeAllTabs();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    actionBar.addTab(actionBar
        .newTab()
        .setText("GROUP")
        .setIcon(R.drawable.ic_action_group)
        .setTabListener(new MyTabListener<GroupFragment>(this, "GROUP", GroupFragment.class))
        );
    actionBar.addTab(actionBar
        .newTab()
        .setText("CONTACT")
        .setIcon(R.drawable.ic_action_person)
        .setTabListener(new MyTabListener<ContactFragment>(this, "CONTACT", ContactFragment.class))
        );
  }
  
  /**
   * Do after data from database is loaded.
   */
  public void onLoadCompleted() {
    init();
  }

  /**
   * Update data, call by icon in menu.
   */
  public void update() {
    progressDialog = ProgressDialog.show(ContactsActivity.this,
        Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);
    new  LoadTask(ContactsActivity.this).execute();
  }
  
  private class LoadTask extends AsyncTask<Void, Void, Boolean> {
    private Activity activity;

    public LoadTask(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      contactManager.initGroup();
      contactManager.initContact();
      return null;
    }

    protected void onPostExecute(Boolean bool) {
      if (ContactsActivity.this.progressDialog != null) {
        ContactsActivity.this.progressDialog.dismiss();
      }
      ((ContactsActivity) activity).onLoadCompleted();
    }

    protected void onPreExecute() {
      super.onPreExecute();

    }
  }
/*
  private class LoadTaskSimply extends AsyncTask<Void, Void, Pair> {
    private Activity activity;

    public LoadTaskSimply(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Pair doInBackground(Void... params) {
      Pair p = new Pair();
      HelperSQL db = new HelperSQL(ContactsActivity.this);
      // load group
      p.setGroupsList(db.getAllGroups());
      // load user
      p.setContactList(db.getAllContacts());
      
      return p;
    }

    protected void onPostExecute(Pair p) {
      if (ContactsActivity.this.progressDialog != null) {
        ContactsActivity.this.progressDialog.dismiss();
      }
      ((ContactsActivity) activity).onTaskCompleted(p);
    }

    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
*/
  
  /**
   * Call after clock on button
   * @param view
   */
  public void mainActivity(View view) {
    Editor editor = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE).edit();
    editor.putBoolean(Constants.PREFS_START_FIRST, false);
    editor.commit();
    
    // Selected contatc should be import to server
    new SyncTask(ContactsActivity.this).execute();
    
    
  }
  
  /**
   * Task for synchronization contact.
   * @author portilo
   *
   */
  private class SyncTask extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog progressDialog;
    
    private Activity activity;

    public SyncTask(Activity activity) {
      this.activity = activity;
    }
    
    @Override
    protected Boolean doInBackground(Void...params) {
      try {
        Synchronization synchronization = new Synchronization();
        synchronization.synchronizeFirst(new ServerInstance(AccountData.getAccountData(getApplicationContext())), getApplicationContext());
      } catch (RemoteException e) {
        e.printStackTrace();
      } catch (OperationApplicationException e) {
        e.printStackTrace();
      }
      return true;
    }
    protected void onPostExecute(Boolean bool) {
      progressDialog.dismiss();
      ((ContactsActivity) activity).onTaskCompleted();
    }
    
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = ProgressDialog.show(ContactsActivity.this, "Downloading...","Downloading data from server", true);
    }
  }
  
  /**
   * 
   */
  public void onTaskCompleted() {
    Intent intent = new Intent(this, InfoMergeActivity.class);
    startActivity(intent);
  }
  

  /**
   * First run this application.
   * @return true first run, false not first run.
   */
  public boolean isFirst() {
    return first;
  }

  /**
   * 
   * @param first
   */
  public void setFirst(boolean first) {
    this.first = first;
  }
}
