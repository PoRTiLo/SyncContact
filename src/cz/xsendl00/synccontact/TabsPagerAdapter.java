package cz.xsendl00.synccontact;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class TabsPagerAdapter extends FragmentPagerAdapter {
  Pair p;
  public TabsPagerAdapter(FragmentManager fm, Pair p) {
    
    super(fm);
    this.p = p;
  }

  @Override
  public Fragment getItem(int index) {
    Log.i("Fragment", "getItem:"+index);

    switch (index) {
      //case 0: return GroupFragment.newInstance(p);
     // case 1: return ContactFragment.newInstance(p);
      default: return null;
    }
  }
  
  /*
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }
*/
  @Override
  public int getCount() {
    return 2;
  }
}