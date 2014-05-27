package cz.xsendl00.synccontact.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.androidannotations.annotations.EBean;

import android.annotation.SuppressLint;
import android.text.format.Time;
import android.util.Log;

/**
 * Utilities for all.
 * @author xsendl00
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
    Time time = new Time("GMT");
    time.setToNow();
    StringBuilder builder = new StringBuilder();
    builder.append(time.year);
    int month = time.month + 1;
    builder.append(month > 9 ? month : "0" + month);
    builder.append(time.monthDay > 9 ? time.monthDay : "0" + time.monthDay);
    builder.append(time.hour > 9 ? time.hour : "0" + time.hour);
    builder.append(time.minute > 9 ? time.minute : "0" + time.minute);
    builder.append(time.second > 9 ? time.second : "0" + time.second);
    builder.append("Z");
    return builder.toString();
  }

  /**
   * Timestamp string format to readable string.
   * @param timestamp timestamp
   * @return String readable format timesatmp
   */
  @SuppressLint("SimpleDateFormat")
  public String timestamptoDate(String timestamp) {
    String out = null;
    if (timestamp == null) {
      out = "No synchronization.";
    } else {

    }


    try {

      SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
      df1.setTimeZone(TimeZone.getTimeZone("UTC"));
      Date date = df1.parse(timestamp);

      TimeZone tz = TimeZone.getTimeZone("Europe/Prague");
      SimpleDateFormat destFormat = new SimpleDateFormat("HH:mm:ss: dd.MM.yyyy");
      destFormat.setTimeZone(tz);


      out = destFormat.format(date);








//
//      out = df1.parse(timestamp).toString();
//
//      Calendar cal = Calendar.getInstance();
//      cal.setTime(date);
//      Log.i(TAG, cal.getTime().toString());
//      cal.add(Calendar.HOUR_OF_DAY, 2);
//      Log.i(TAG, cal.getTime().toString());
    } catch (ParseException e) {
      Log.e(TAG, "Can not formated timestamp from db to readable string :" + out);
      out = "No synchronization.";
    } catch (NullPointerException e) {
      e.printStackTrace();
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
  public List<ContactRow> intersectionDifference(List<ContactRow> list1, final List<ContactRow> list2) {
    List<ContactRow> list = new ArrayList<ContactRow>();
    for (ContactRow contactRow1 : list1) {
      boolean found = false;
      for (ContactRow contactRow2 : list2) {
        if (contactRow1.getUuidFirst().equals(contactRow2.getUuidFirst())) {
          found = true;
          break;
        }
      }
      if (!found && contactRow1.isSync()) {
        list.add(contactRow1);
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

 public static Thread performOnBackgroundThread(final Runnable runnable) {
   final Thread t = new Thread() {

     @Override
     public void run() {
       runnable.run();
     }
   };
   t.start();
   return t;
 }
}
