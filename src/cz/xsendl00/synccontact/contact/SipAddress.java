package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing a SIP address for the contact.
 * @author portilo
 *
 */
public class SipAddress implements ContactInterface {

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
}
