package cz.xsendl00.synccontact;

import java.util.ArrayList;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.GroupFragment.OnHeadlineSelectedListener;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.Utils;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v4.view.ViewPager;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class SelectContactListActivity extends Activity implements OnTaskCompleted, OnHeadlineSelectedListener {
  
  private static final String TAG = "SelectContactListActivity";

  private final Handler handler = new Handler();
  private ProgressDialog progressDialog;
  private Pair pair;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_contact);
    progressDialog = ProgressDialog.show(SelectContactListActivity.this,
        "Loading...", "Loading data from database.", true);
    
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.activity_main_actions, menu);
    new LoadTask(SelectContactListActivity.this).execute();
    return super.onCreateOptionsMenu(menu);
  }

  public void onTaskCompleted(Pair p) {
    pair = p;
    ActionBar actionBar = getActionBar();
    actionBar.setHomeButtonEnabled(false);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    actionBar.addTab(actionBar.newTab().setText("GROUP").setTabListener(new MyTabListener(GroupFragment.newInstance(pair))));
    actionBar.addTab(actionBar.newTab().setText("CONTACT").setTabListener(new MyTabListener(ContactFragment.newInstance(pair))));

  }

  /**
   * On selecting action bar icons
   * */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Take appropriate action for each action item click
    switch (item.getItemId()) {
    case R.id.action_search:
      sync();
      return true;
    case R.id.action_location_found:
      // location found
      LocationFound();
      return true;
    case R.id.action_refresh:
      // refresh
      return true;
    case R.id.action_help:
      help();
      return true;
    case R.id.action_check_updates:
      // check for updates action
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  private void help() {
    ChangeAccountTask rt = new ChangeAccountTask();
    rt.execute();
  }

  // sync all contact
  // upload all contact to server
  private void sync() {
    UpdateTask rt = new UpdateTask();
    rt.execute();
    Log.i(TAG, "sync");
  }

  private class ExportAccountTask extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog progressDialog;

    @Override
    protected Boolean doInBackground(Void... params) {
      HelperSQL db = new HelperSQL(getApplicationContext());

      // AndroidDB.exportContactFromSyncAccount(Context context, Integer id,
      // String accountName, String accountType);
      // ServerUtilities.addContactsToServer(new
      // ServerInstance(AccountData.getAccountData(getApplicationContext())),
      // handler, SelectContactListActivity.this);
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


  // show anly sync contact
  private void LocationFound() {

  }

  private class LoadTask extends AsyncTask<Void, Void, Pair> {
    private Activity activity;

    public LoadTask(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Pair doInBackground(Void... params) {

      Pair p = new Pair();
      p.getGroupsList().addAll(GroupRow.fetchGroups(SelectContactListActivity.this.getContentResolver()));
      for (GroupRow group : p.getGroupsList()) {
        ArrayList<ContactRow> groupMembers = new ArrayList<ContactRow>();
        groupMembers.addAll(ContactRow.fetchGroupMembers(SelectContactListActivity.this.getContentResolver(), group.getId()));
        group.setSize(groupMembers.size());
       // p.getGroupMemberList().put(group, groupMembers);
      }
      HelperSQL db = new HelperSQL(SelectContactListActivity.this);
      db.fillGroups(p.getGroupsList());
      p.getContactList().addAll(ContactRow.fetchAllContact(SelectContactListActivity.this.getContentResolver()));
      db.fillContacts(p.getContactList());
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
  public void onArticleSelected(Pair p ) {
    //this.pair = p;
    //pair.getContactList().get(1).setSync(true);
  }
  
  public void mainActivity(View view) {
    Editor editor = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE).edit();
    editor.putBoolean(Constants.PREFS_START_FIRST, false);
    editor.commit();
    
    Intent intent = new Intent(this, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    
  }
}
