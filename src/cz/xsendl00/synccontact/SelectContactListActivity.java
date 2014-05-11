package cz.xsendl00.synccontact;

import java.util.ArrayList;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.GroupFragment.OnHeadlineSelectedListener;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.ldap.Synchronization;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SelectContactListActivity extends Activity implements
    OnTaskCompleted, OnHeadlineSelectedListener {

  private static final String TAG = "SelectContactListActivity";

  private final Handler handler = new Handler();
  private ProgressDialog progressDialog;
  private Pair pair;
  private boolean first = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_contact);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    Intent intent = getIntent();
    first = intent.getBooleanExtra("FIRST", false);
    progressDialog = ProgressDialog.show(SelectContactListActivity.this,
        Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);
    if (first) {
      // TODO; pro prvni zmeninut menu
     
      new LoadTask(SelectContactListActivity.this).execute();
    } else {
     
      new LoadTaskSimply(SelectContactListActivity.this).execute();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    if (first) {
      // TODO; pro prvni zmeninut menu
      getMenuInflater().inflate(R.menu.contacts_menu, menu);
     // new LoadTask(SelectContactListActivity.this).execute();
    } else {
      getMenuInflater().inflate(R.menu.contacts_menu, menu);
      //new LoadTaskSimply(SelectContactListActivity.this).execute();
    }

    return super.onCreateOptionsMenu(menu);
  }

  public void onTaskCompleted(Pair p) {
    pair = p;
    ActionBar actionBar = getActionBar();
    actionBar.removeAllTabs();
    actionBar.setHomeButtonEnabled(false);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    actionBar.addTab(actionBar
        .newTab()
        .setText("GROUP")
        .setTabListener(
            new MyTabListener(GroupFragment.newInstance(pair, first))));
    actionBar.addTab(actionBar
        .newTab()
        .setText("CONTACT")
        .setTabListener(
            new MyTabListener(ContactFragment.newInstance(pair, first))));

  }
  public void onBackPressed() {
    super.onBackPressed();
    
  }
  

  // sync all contact
  // upload all contact to server
  private void sync() {
    UpdateTask rt = new UpdateTask();
    rt.execute();
    Log.i(TAG, "sync");
  }

  

  private class ChangeAccountTask extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog progressDialog;

    @Override
    protected Boolean doInBackground(Void... params) {
      ServerUtilities.addContactsToServer(
          new ServerInstance(AccountData
              .getAccountData(getApplicationContext())), handler,
          SelectContactListActivity.this);
      return true;
    }

    protected void onPostExecute(Boolean bool) {
      progressDialog.dismiss();
    }

    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = ProgressDialog.show(SelectContactListActivity.this,
          "Downloading...", "Downloading data from server", true);
    }
  }

  private class UpdateTask extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog progressDialog;

    @Override
    protected Boolean doInBackground(Void... params) {
      ServerUtilities.addContactsToServer(
          new ServerInstance(AccountData
              .getAccountData(getApplicationContext())), handler,
          SelectContactListActivity.this);
      return true;
    }

    protected void onPostExecute(Boolean bool) {
      progressDialog.dismiss();
    }

    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = ProgressDialog.show(SelectContactListActivity.this,
          "Downloading...", "Downloading data from server", true);
    }
  }

  public void update() {
    progressDialog = ProgressDialog.show(SelectContactListActivity.this,
        Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);

    new  LoadTask(SelectContactListActivity.this).execute();
  }
  
  private class LoadTask extends AsyncTask<Void, Void, Pair> {
    private Activity activity;

    public LoadTask(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Pair doInBackground(Void... params) {

      Pair p = new Pair();
      
      Log.i(TAG, "getGroupsList");
      p.getGroupsList().clear();
      p.getGroupsList().addAll(GroupRow.fetchGroups(SelectContactListActivity.this.getContentResolver()));
      Log.i(TAG, "getGroupsList size:" + p.getGroupsList().size());
      for (GroupRow group : p.getGroupsList()) {
        ArrayList<ContactRow> groupMembers = new ArrayList<ContactRow>();
        groupMembers.addAll(ContactRow.fetchGroupMembers(SelectContactListActivity.this.getContentResolver(), group.getId()));
        group.setSize(groupMembers.size());
        Log.i(TAG, "id:" + group.getId() + "group size:" + group.getSize());
        // p.getGroupMemberList().put(group, groupMembers);
      }
      HelperSQL db = new HelperSQL(SelectContactListActivity.this);
      Log.i(TAG, "fillGroups");
      db.fillGroups(p.getGroupsList());
      
      Log.i(TAG, "getContactList");
      p.getContactList().addAll(
          ContactRow.fetchAllContact(SelectContactListActivity.this
              .getContentResolver()));
      Log.i(TAG, "getContactList size:" + p.getContactList().size());
      Log.i(TAG, "fillContacts");
      db.fillContacts(p.getContactList());
      Log.i(TAG, "fillContacts END");
      return p;
    }

    protected void onPostExecute(Pair p) {
      if (SelectContactListActivity.this.progressDialog != null) {
        SelectContactListActivity.this.progressDialog.dismiss();
      }
      ((SelectContactListActivity) activity).onTaskCompleted(p);
    }

    protected void onPreExecute() {
      super.onPreExecute();

    }
  }

  private class LoadTaskSimply extends AsyncTask<Void, Void, Pair> {
    private Activity activity;

    public LoadTaskSimply(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Pair doInBackground(Void... params) {

      Pair p = new Pair();
      HelperSQL db = new HelperSQL(SelectContactListActivity.this);
      // load group
      p.setGroupsList(db.getAllGroups());
      // load user
      p.setContactList(db.getAllContacts());
      
      return p;
    }

    protected void onPostExecute(Pair p) {
      if (SelectContactListActivity.this.progressDialog != null) {
        SelectContactListActivity.this.progressDialog.dismiss();
      }
      ((SelectContactListActivity) activity).onTaskCompleted(p);
    }

    protected void onPreExecute() {
      super.onPreExecute();
    }
  }

  @Override
  public void onArticleSelected(Pair p) {
    // this.pair = p;
    // pair.getContactList().get(1).setSync(true);
  }

  public void mainActivity(View view) {
    Editor editor = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        .edit();
    editor.putBoolean(Constants.PREFS_START_FIRST, false);
    editor.commit();
    
    // Selected contatc should be import to server
    new SyncTask(SelectContactListActivity.this).execute();
    
    
  }
  
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
        synchronization.synchronization(new ServerInstance(AccountData.getAccountData(getApplicationContext())), getApplicationContext());
      } catch (RemoteException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (OperationApplicationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return true;
    }
    protected void onPostExecute(Boolean bool) {
      progressDialog.dismiss();
      ((SelectContactListActivity) activity).onTaskCompleted();
    }
    
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = ProgressDialog.show(SelectContactListActivity.this, "Downloading...","Downloading data from server", true);
    }
  }
  
  public void onTaskCompleted() {
    Intent intent = new Intent(this, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
        | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);

  }
}
