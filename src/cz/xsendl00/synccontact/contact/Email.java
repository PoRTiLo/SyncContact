package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing an email address.
 * @author portilo
 *
 */
public class Email {
  
  private String homeMail = Constants.HOME_MAIL;
  private String workMail = Constants.WORK_MAIL;
  private String otherMail = Constants.OTHER_MAIL;
  private String mobileMail = Constants.MOBILE_MAIL;
  
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

}
