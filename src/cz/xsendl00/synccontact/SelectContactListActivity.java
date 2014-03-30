package cz.xsendl00.synccontact;

import com.xsendl00.synccontact.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SelectContactListActivity extends FragmentActivity implements ActionBar.TabListener {
  
  private static final String TAG = "SelectContactListActivity";
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
          // search action
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

  
  
  @Override
  public void onTabReselected(Tab tab, FragmentTransaction ft) {
  }

  @Override
  public void onTabSelected(Tab tab, FragmentTransaction ft) {
      // on tab selected
      // show respected fragment view
      viewPager.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(Tab tab, FragmentTransaction ft) {
  }
  
  /**
   * Launching new activity
   * */
  private void LocationFound() {
      //Intent i = new Intent(SelectContactListActivity.this, LocationFound.class);
      //startActivity(i);
  }
 
}
