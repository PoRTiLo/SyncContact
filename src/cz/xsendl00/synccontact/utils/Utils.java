package cz.xsendl00.synccontact.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.EBean;

import android.annotation.SuppressLint;
import android.text.format.Time;
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
    Log.i(tag, "start -- " + text + " --- time : 0.0");
  }

  /**
   * Stop running timer. Print time between start and stop.
   * @param tag Tag
   * @param text text in log
   */
  public void stopTime(String tag, String text) {
    now = System.currentTimeMillis();
    String str = String.valueOf(now - last);
    Log.i(tag, "end -- " + text + " --- time : " + str);
  }

  /**
   * Get new timestamp "yyyyMMddHHmmss".
   *
   * @return The {@link String} representation timestamp.
   */
  public String createTimestamp() {
    Time now = new Time(Time.getCurrentTimezone());
    now.setToNow();
    StringBuilder builder = new StringBuilder();
    builder.append(now.year);
    int month = now.month + 1;
    builder.append(month > 9 ? month : "0" + month);
    builder.append(now.monthDay > 9 ? now.monthDay : "0" + now.monthDay);
    builder.append(now.hour > 9 ? now.hour : "0" + now.hour);
    builder.append(now.minute > 9 ? now.minute : "0" + now.minute);
    builder.append(now.second > 9 ? now.second : "0" + now.second);
    builder.append("Z");
    return builder.toString();
  }

  @SuppressLint("SimpleDateFormat")
  public String timestamptoDate(String str) {
    SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
    String out = null;
    try {
      out = df1.parse(str).toString();
    } catch (ParseException e) {
      Log.e(TAG, "Can not formated timestamp from db to readable string :" + out);
      out = "No synchronization.";
    }
    return out;
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

    for (int pozicion = 0; pozicion < list1.size();) {
      ContactRow contactRow = map2.get(list1.get(pozicion).getUuid());
      if (contactRow != null) {
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

  /**
  *
  * @param list1
  * @param map2
  * @return
  */
 public List<GroupRow> intersection(final List<GroupRow> list1, final List<GroupRow> list2) {
   List<GroupRow> list = new ArrayList<GroupRow>();
   for (GroupRow groupRow : list1) {
     boolean found = false;
     for (AbstractRow abstractRow : list2) {
       if (groupRow.getName().equals(abstractRow.getName())) {
         found = true;
         break;
       }
     }
     if (!found) {
       list.add(groupRow);
     }
   }
   return list;
 }
}
