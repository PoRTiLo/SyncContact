package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
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

  public static ContentValues compare(Email em1, Email em2) {
    ContentValues values = new ContentValues();
    if (em1 == null && em2 != null) { // update from LDAP
      if (em2.getHomeMail() != null) {
        values.put(Constants.HOME_MAIL, em2.getHomeMail());
      }
      if (em2.getMobileMail() != null) {
        values.put(Constants.MOBILE_MAIL, em2.getMobileMail());
      }
      if (em2.getOtherMail() != null) {
        values.put(Constants.OTHER_MAIL, em2.getOtherMail());
      }
      if (em2.getWorkMail() != null) {
        values.put(Constants.WORK_MAIL, em2.getWorkMail());
      }
    } else if (em1 == null && em2 == null) { // nothing
      
    } else if (em1 != null && em2 == null) { // clear data in db
      if (em1.getHomeMail() != null) {
        values.putNull(Constants.HOME_MAIL);
      }
      if (em1.getMobileMail() != null) {
        values.putNull(Constants.MOBILE_MAIL);
      }
      if (em1.getOtherMail() != null) {
        values.putNull(Constants.OTHER_MAIL);
      }
      if (em1.getWorkMail() != null) {
        values.putNull(Constants.WORK_MAIL);
      }
    } else if (em1 != null && em2 != null) { // merge
      if (em2.getHomeMail() != null) {
        values.put(Constants.HOME_MAIL, em2.getHomeMail());
      } else {
        values.putNull(Constants.HOME_MAIL);
      } 
      if (em2.getMobileMail() != null) {
        values.put(Constants.MOBILE_MAIL, em2.getMobileMail());
      } else {
        values.putNull(Constants.MOBILE_MAIL);
      }
      if (em2.getOtherMail() != null) {
        values.put(Constants.OTHER_MAIL, em2.getOtherMail());
      } else {
        values.putNull(Constants.OTHER_MAIL);
      }
      if (em2.getWorkMail() != null) {
        values.put(Constants.WORK_MAIL, em2.getWorkMail());
      } else {
        values.putNull(Constants.WORK_MAIL);
      }
    }
    return values;
  }
}
