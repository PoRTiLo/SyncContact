package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;
import cz.xsendl00.synccontact.RowContactAdapter;
import cz.xsendl00.synccontact.contact.GoogleContact;

/**
 * Utilities for all.
 * @author portilo
 *
 */
public class Utils {
  
  private static final String TAG = "Utils";
  
  private static Long last = System.currentTimeMillis();
  private static Long now  = last;
  private static Integer i = 0;
  
  public static String getTime() {
    now = System.currentTimeMillis();
    String str = i.toString() + String.valueOf(now - last);
    i++;
    last = now;
    
    return str;
  }
  
  /**
   * 
   * @param list1
   * @param map2
   * @return
   */
  public List<ContactRow> intersectionDifference(List<ContactRow> list1, final Map<String, ContactRow> map2) {
    Log.i(TAG, "intersection: " + list1.size() + " to " + map2.size());
    List<ContactRow> list = new ArrayList<ContactRow>();
    
    for (int pozicion = 0; pozicion < list1.size(); ) {
      if(map2.get(list1.get(pozicion).getUuid()) != null) {
        if (list1.get(pozicion).isSync()) {
          list.add(list1.get(pozicion));
        }
        list1.remove(pozicion);
      } else {
        pozicion++;
      }
    }
    return list;
  }
}
