package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing a SIP address for the contact.
 * @author portilo
 *
 */
public class SipAddress extends AbstractType implements ContactInterface {

  private String workSip;
  private String homeSip;
  private String otherSip;
  
  public String getWorkSip() {
    return workSip;
  }
  public void setWorkSip(String workSip) {
    this.workSip = workSip;
  }
  public String getHomeSip() {
    return homeSip;
  }
  public void setHomeSip(String homeSip) {
    this.homeSip = homeSip;
  }
  public String getOtherSip() {
    return otherSip;
  }
  public void setOtherSip(String otherSip) {
    this.otherSip = otherSip;
  }  
  @Override
  public String toString() {
    return "SipAddress [workSip=" + workSip + ", homeSip=" + homeSip
        + ", otherSip=" + otherSip + "]";
  }
  @Override
  public void defaultValue() {
    workSip = Constants.WORK_SIP;
    homeSip = Constants.HOME_SIP;
    otherSip = Constants.OTHER_SIP;
  }
  
  public static ContentValues compare(SipAddress obj1, SipAddress obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getHomeSip() != null) {
        values.put(Constants.HOME_SIP, obj2.getHomeSip());
      }
      if (obj2.getWorkSip() != null) {
        values.put(Constants.WORK_SIP, obj2.getWorkSip());
      }
      if (obj2.getOtherSip() != null) {
        values.put(Constants.OTHER_SIP, obj2.getOtherSip());
      }
    } else if (obj1 == null && obj2 == null) { // nothing
      
    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getHomeSip() != null) {
        values.putNull(Constants.HOME_SIP);
      }
      if (obj1.getWorkSip() != null) {
        values.putNull(Constants.WORK_SIP);
      }
      if (obj1.getOtherSip() != null) {
        values.putNull(Constants.OTHER_SIP);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getHomeSip() != null) {
        values.put(Constants.HOME_SIP, obj2.getHomeSip());
      } else {
        values.putNull(Constants.HOME_SIP);
      }
      if (obj2.getWorkSip() != null) {
        values.put(Constants.WORK_SIP, obj2.getWorkSip());
      } else {
        values.putNull(Constants.WORK_SIP);
      }
      if (obj2.getOtherSip() != null) {
        values.put(Constants.OTHER_SIP, obj2.getOtherSip());
      } else {
        values.putNull(Constants.OTHER_SIP);
      }
    }
    return values;
  }
}
