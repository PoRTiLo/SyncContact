package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
import cz.xsendl00.synccontact.utils.Constants;

public class Event extends AbstractType implements ContactInterface {


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
  
  public static ContentValues compare(Event obj1, Event obj2) {
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
}
