package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing a SIP address for the contact.
 * @author portilo
 *
 */
public class SipAddress {
  
  private String workSip = Constants.WORK_SIP;
  private String homeSip = Constants.HOME_SIP;
  private String otherSip = Constants.OTHER_SIP;
  
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
}
