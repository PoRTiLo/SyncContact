package cz.xsendl00.synccontact;

import com.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.ServerInstance;
import cz.xsendl00.synccontact.client.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SelectContactListActivity extends FragmentActivity implements ActionBar.TabListener {

  private static final String TAG = "SelectContactListActivity"; 
  
  private final Handler handler = new Handler();
  private ActionBar actionBar;
  private ViewPager viewPager;
  private TabsPagerAdapter mAdapter;
  private String[] tabs = { "Group", "Contact"};
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_contact);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.activity_main_actions, menu);

    
 // Initilization
    viewPager = (ViewPager) findViewById(R.id.pager);
    actionBar = getActionBar();
    mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

    viewPager.setAdapter(mAdapter);
    viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
      }
    });
    actionBar = getActionBar();
    actionBar.setHomeButtonEnabled(false);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    for (String tab_name : tabs) {
      actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
    }
    
    return super.onCreateOptionsMenu(menu);
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
        // help action
        return true;
      case R.id.action_check_updates:
        // check for updates action
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  // sync all contact
  // upload all contact to server
  private void sync() {
    UpdateTask rt = new UpdateTask();
    rt.execute();
    Log.i(TAG, "sync");
  }

  private class UpdateTask extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog progressDialog;
    @Override
    protected Boolean doInBackground(Void...params) {
      ServerUtilities.addContactsToServer(new ServerInstance(AccountData.getAccountData(getApplicationContext())), handler, SelectContactListActivity.this);
      return true;
    }
    protected void onPostExecute(Boolean bool) {
      progressDialog.dismiss();
    }
    
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = ProgressDialog.show(SelectContactListActivity.this, "Downloading...","Downloading data from server", true);
    }
  }
  
  @Override
  public void onTabReselected(Tab tab, FragmentTransaction ft) {}

  @Override
  public void onTabSelected(Tab tab, FragmentTransaction ft) {
    viewPager.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
  
  /**
   * Launching new activity
   * */
  private void LocationFound() {
    //Intent i = new Intent(SelectContactListActivity.this, LocationFound.class);
    //startActivity(i);
  }
}
