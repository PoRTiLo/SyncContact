package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

public class Event implements ContactInterface {


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
}
