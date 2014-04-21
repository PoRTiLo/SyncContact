package cz.xsendl00.synccontact;

import com.xsendl00.synccontact.R;

import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class MyTabListener implements ActionBar.TabListener {
  Fragment fragment;
  
  public MyTabListener(Fragment fragment) {
      this.fragment = fragment;
  }

  @Override
  public void onTabSelected(Tab tab, FragmentTransaction ft) {
      ft.replace(R.id.fragment_container, fragment);
  }

  @Override
  public void onTabUnselected(Tab tab, FragmentTransaction ft) {
      ft.remove(fragment);
  }

  @Override
  public void onTabReselected(Tab tab, FragmentTransaction ft) {
      // TODO Auto-generated method stub
  }
}
