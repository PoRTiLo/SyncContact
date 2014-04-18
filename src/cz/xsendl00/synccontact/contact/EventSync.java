package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Event;
import cz.xsendl00.synccontact.utils.Constants;

public class EventSync extends AbstractType implements ContactInterface {


  private String eventOther;
  private String eventBirthday;
  private String eventAnniversary;
  
  public String getEventOther() {
    return eventOther;
  }
  public void setEventOther(String eventOther) {
    this.eventOther = eventOther;
  }
  public String getEventBirthday() {
    return eventBirthday;
  }
  public void setEventBirthday(String eventBirthday) {
    this.eventBirthday = eventBirthday;
  }
  public String getEventAnniversary() {
    return eventAnniversary;
  }
  public void setEventAnniversary(String eventAnniversary) {
    this.eventAnniversary = eventAnniversary;
  }
  
  @Override
  public String toString() {
    return "Event [eventOther=" + eventOther + ", eventBirthday="
        + eventBirthday + ", eventAnniversary=" + eventAnniversary + "]";
  }
  
  public void defaultValue() {
    this.eventOther = Constants.EVENT_OTHER;
    this.eventBirthday = Constants.EVENT_BIRTHDAY;
    this.eventAnniversary = Constants.EVENT_ANNIVERSARY;
  }
  
  public static ContentValues compare(EventSync obj1, EventSync obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getEventAnniversary() != null) {
        values.put(Constants.EVENT_ANNIVERSARY, obj2.getEventAnniversary());
      }
      if (obj2.getEventBirthday() != null) {
        values.put(Constants.EVENT_BIRTHDAY, obj2.getEventBirthday());
      }
      if (obj2.getEventOther() != null) {
        values.put(Constants.EVENT_OTHER, obj2.getEventOther());
      }
    } else if (obj1 == null && obj2 == null) { // nothing
      
    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getEventAnniversary() != null) {
        values.putNull(Constants.EVENT_ANNIVERSARY);
      }
      if (obj1.getEventBirthday() != null) {
        values.putNull(Constants.EVENT_BIRTHDAY);
      }
      if (obj1.getEventOther() != null) {
        values.putNull(Constants.EVENT_OTHER);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getEventAnniversary() != null) {
        values.put(Constants.EVENT_ANNIVERSARY, obj2.getEventAnniversary());
      } else {
        values.putNull(Constants.EVENT_ANNIVERSARY);
      } 
      if (obj2.getEventBirthday() != null) {
        values.put(Constants.EVENT_BIRTHDAY, obj2.getEventBirthday());
      } else {
        values.putNull(Constants.EVENT_BIRTHDAY);
      }
      if (obj2.getEventOther() != null) {
        values.put(Constants.EVENT_OTHER, obj2.getEventOther());
      } else {
        values.putNull(Constants.EVENT_OTHER);
      }
    }
    return values;
  }
  
  public static ContentProviderOperation add(String id, String event, int type) {
    return ContentProviderOperation.newInsert(Data.CONTENT_URI)
    .withValue(Data.RAW_CONTACT_ID, id)
    .withValue(Data.MIMETYPE, Event.CONTENT_ITEM_TYPE)
    .withValue(Event.START_DATE, event)
    .withValue(Event.TYPE, type)
    .build();
  }
  
  public static ContentProviderOperation update(String id, String event, int type) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{String.valueOf(id)})
        .withValue(Data.MIMETYPE, Event.CONTENT_ITEM_TYPE)
        .withValue(Event.START_DATE, event)
        .withValue(Event.TYPE, type)
        .build();
  }
  
  public static ArrayList<ContentProviderOperation> operation(String id, EventSync em1, EventSync em2) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      
      if (em2.getEventAnniversary() != null) {
        ops.add(add(id, em2.getEventAnniversary(), Event.TYPE_ANNIVERSARY));
      }
      if (em2.getEventBirthday() != null) {
        ops.add(add(id, em2.getEventBirthday(), Event.TYPE_BIRTHDAY));
      }
      if (em2.getEventOther() != null) {
        ops.add(add(id, em2.getEventOther(), Event.TYPE_OTHER));
      }
    } else if (em1 == null && em2 == null) { // nothing
      
    } else if (em1 != null && em2 == null) { // clear or update data in db
      if (em1.getEventAnniversary() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Event.TYPE_ANNIVERSARY), null)));
      }
      if (em1.getEventBirthday() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Event.TYPE_BIRTHDAY), null)));
      }
      if (em1.getEventOther() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Event.TYPE_OTHER), null)));
      }
    } else if (em1 != null && em2 != null) { // merge
      String i = ID.getIdByValue(em1.getID(), String.valueOf(Event.TYPE_ANNIVERSARY), null);
      if (em2.getEventAnniversary() != null) {
        ops.add(update(i, em2.getEventAnniversary(), Event.TYPE_ANNIVERSARY));
      } else {
        ops.add(GoogleContact.delete(i));
      }
      i = ID.getIdByValue(em1.getID(), String.valueOf(Event.TYPE_BIRTHDAY), null);
      if (em2.getEventBirthday() != null) {
        ops.add(update(i, em2.getEventBirthday(), Event.TYPE_BIRTHDAY));
      } else {
        ops.add(GoogleContact.delete(i));
      }
      i = ID.getIdByValue(em1.getID(), String.valueOf(Event.TYPE_OTHER), null);
      if (em2.getEventOther() != null) {
        ops.add(update(i, em2.getEventOther(), Event.TYPE_OTHER));
      } else {
        ops.add(GoogleContact.delete(i));
      }
    }
    return ops.size() > 0 ? ops : null;
  }
}
