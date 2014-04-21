package cz.xsendl00.synccontact;

import com.xsendl00.synccontact.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class Pokus extends Activity {
  // Declare Tab Variable
  ActionBar.Tab Tab1, Tab2, Tab3;
  Fragment fragmentTab1 = new FragmentTab1();
  Fragment fragmentTab2 = new FragmentTab2();
  Fragment fragmentTab3 = new FragmentTab3();

  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      ActionBar actionBar = getActionBar();

      // Hide Actionbar Icon
      actionBar.setDisplayShowHomeEnabled(false);

      // Hide Actionbar Title
      actionBar.setDisplayShowTitleEnabled(false);

      // Create Actionbar Tabs
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

      // Set Tab Icon and Titles
      Tab1 = actionBar.newTab().setText("Tab1");
      Tab2 = actionBar.newTab().setText("Tab2");
      Tab3 = actionBar.newTab().setText("Tab3");

      // Set Tab Listeners
      Tab1.setTabListener(new MyTabListener(fragmentTab1));
      Tab2.setTabListener(new MyTabListener(fragmentTab2));
      Tab3.setTabListener(new MyTabListener(fragmentTab3));

      // Add tabs to actionbar
      actionBar.addTab(Tab1);
      actionBar.addTab(Tab2);
      actionBar.addTab(Tab3);
  }
}
