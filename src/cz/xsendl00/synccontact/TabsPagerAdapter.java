package cz.xsendl00.synccontact;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

  public TabsPagerAdapter(FragmentManager fm) {
      super(fm);
  }

  @Override
  public Fragment getItem(int index) {
    switch (index) {
      case 0: return new GroupFragment();
      case 1: return new ContactFragmnet();
      default: return null;
    }
  }

  @Override
  public int getCount() {
    return 2;
  }
}