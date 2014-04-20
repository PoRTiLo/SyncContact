package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing an email address.
 * @author portilo
 *
 */
public class EmailSync extends AbstractType implements ContactInterface {
  
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

  public static ContentValues compare(EmailSync em1, EmailSync em2) {
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
  
  public static ContentProviderOperation add(String id, String email, int type) {
    return ContentProviderOperation.newInsert(Data.CONTENT_URI)
    .withValue(Data.RAW_CONTACT_ID, id)
    .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
    .withValue(Email.ADDRESS, email)
    .withValue(Email.TYPE, type)
    .build();
  }
  
  public static ContentProviderOperation update(String id, String email, int type) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{id})
        .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
        .withValue(Email.ADDRESS, email)
        .withValue(Email.TYPE, type)
        .build();
  }
  
  public static ArrayList<ContentProviderOperation> operation(String id, EmailSync em1, EmailSync em2) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      
      if (em2.getHomeMail() != null) {
        ops.add(add(id, em2.getHomeMail(), Email.TYPE_HOME));
      }
      if (em2.getMobileMail() != null) {
        ops.add(add(id, em2.getMobileMail(), Email.TYPE_MOBILE));
      }
      if (em2.getOtherMail() != null) {
        ops.add(add(id, em2.getOtherMail(), Email.TYPE_OTHER));
      }
      if (em2.getWorkMail() != null) {
        ops.add(add(id, em2.getWorkMail(), Email.TYPE_WORK));
      }
      
    } else if (em1 == null && em2 == null) { // nothing
    } else if (em1 != null && em2 == null) { // clear or update data in db
      if (em1.getHomeMail() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_HOME), null)));
      }
      if (em1.getMobileMail() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_MOBILE), null)));
      }
      if (em1.getOtherMail() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_OTHER), null)));
      }
      if (em1.getWorkMail() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_WORK), null)));
      }
    } else if (em1 != null && em2 != null) { // merge
      
      String i = ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_HOME), null);
      if (i == null && em2.getHomeMail() != null) {
        ops.add(add(id, em2.getHomeMail(), Email.TYPE_HOME));
      } else if ( i != null && em2.getHomeMail() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getHomeMail() != null && !em2.getHomeMail().equals(em1.getHomeMail())) { 
        ops.add(update(i, em2.getHomeMail(), Email.TYPE_HOME));
      }
      
      i = ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_MOBILE), null);
      if (i == null && em2.getMobileMail() != null) {
        ops.add(add(id, em2.getMobileMail(), Email.TYPE_HOME));
      } else if ( i != null && em2.getMobileMail() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getMobileMail() != null && !em2.getMobileMail().equals(em1.getMobileMail())) { 
        ops.add(update(i, em2.getMobileMail(), Email.TYPE_HOME));
      }
     
      i = ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_OTHER), null);
      if (i == null && em2.getOtherMail() != null) {
        ops.add(add(id, em2.getOtherMail(), Email.TYPE_HOME));
      } else if ( i != null && em2.getOtherMail() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getOtherMail() != null && !em2.getOtherMail().equals(em1.getOtherMail())) { 
        ops.add(update(i, em2.getOtherMail(), Email.TYPE_HOME));
      }
      
      i = ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_WORK), null);
      if (i == null && em2.getWorkMail() != null) {
        ops.add(add(id, em2.getWorkMail(), Email.TYPE_HOME));
      } else if ( i != null && em2.getWorkMail() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getWorkMail() != null && !em2.getWorkMail().equals(em1.getWorkMail())) { 
        ops.add(update(i, em2.getWorkMail(), Email.TYPE_HOME));
      }
    }
    return ops.size() > 0 ? ops : null;
  }
}
