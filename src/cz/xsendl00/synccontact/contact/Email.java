package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing an email address.
 * @author portilo
 *
 */
public class Email implements ContactInterface {
  
  private String homeMail;
  private String workMail;;
  private String otherMail;;
  private String mobileMail;;
  
  public String getHomeMail() {
    return homeMail;
  }
  public void setHomeMail(String homeMail) {
    this.homeMail = homeMail;
  }
  public String getWorkMail() {
    return workMail;
  }
  public void setWorkMail(String workMail) {
    this.workMail = workMail;
  }
  public String getOtherMail() {
    return otherMail;
  }
  public void setOtherMail(String otherMail) {
    this.otherMail = otherMail;
  }
  public String getMobileMail() {
    return mobileMail;
  }
  public void setMobileMail(String mobileMail) {
    this.mobileMail = mobileMail;
  }
  
  @Override
  public String toString() {
    return "Email [homeMail=" + homeMail + ", workMail=" + workMail
        + ", otherMail=" + otherMail + ", mobileMail=" + mobileMail + "]";
  }
  
  public void defaultValue() {
    this.homeMail = Constants.HOME_MAIL;
    this.workMail = Constants.WORK_MAIL;
    this.otherMail = Constants.OTHER_MAIL;
    this.mobileMail = Constants.MOBILE_MAIL;
  }

}
