package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.androidannotations.annotations.EBean;

import android.util.Log;

/**
 * Utilities for all.
 * @author portilo
 *
 */
@EBean
public class Utils {

  private static final String TAG = "Utils";

  private Long last;
  private Long now;

  /**
   * Set time to 0, start run timer.
   * @param tag TAG
   * @param text text in log
   */
  public void startTime(String tag, String text) {
    last = System.currentTimeMillis();
    Log.i(tag, "start --" + text + ": 0.0");
  }

  /**
   * Stop running timer. Print time between start and stop.
   * @param tag Tag
   * @param text text in log
   */
  public void stopTime(String tag, String text) {
    now = System.currentTimeMillis();
    String str = String.valueOf(now - last);
    Log.i(tag, "end --" + text + ": " + str);
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
      ContactRow contactRow= map2.get(list1.get(pozicion).getUuid());
      if(contactRow != null) {
        if (list1.get(pozicion).isSync() && !contactRow.isSync()) {
          list.add(contactRow);
        }
        list1.remove(pozicion);
      } else {
        pozicion++;
      }
    }
    return list;
  }
}
