package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

public class Event {
  private String eventOther = Constants.EVENT_OTHER;
  private String eventBirthday = Constants.EVENT_BIRTHDAY;
  private String eventAnniversary = Constants.EVENT_ANNIVERSARY;
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
}
