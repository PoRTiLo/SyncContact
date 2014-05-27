package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Data;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing an email address.
 *
 * @author xsendl00
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
    return "Email [homeMail=" + homeMail + ", workMail=" + workMail + ", otherMail=" + otherMail
        + ", mobileMail=" + mobileMail + "]";
  }

  @Override
  public void defaultValue() {
    this.homeMail = Constants.HOME_MAIL;
    this.workMail = Constants.WORK_MAIL;
    this.otherMail = Constants.OTHER_MAIL;
    this.mobileMail = Constants.MOBILE_MAIL;
  }

  /**
   * Check if Email s value are NULL.
   * @return true is null, false not null
   */
  public boolean isNull() {
    return homeMail == null && mobileMail == null && otherMail == null && workMail == null;
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

  public static ContentProviderOperation add(int id, String email, int type, boolean create) {
    if (create) {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValueBackReference(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
          .withValue(Email.ADDRESS, email)
          .withValue(Email.TYPE, type)
          .build();
    } else {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValue(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
          .withValue(Email.ADDRESS, email)
          .withValue(Email.TYPE, type)
          .build();
    }
  }

  public static ContentProviderOperation update(String id, String email, int type) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{id})
        .withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
        .withValue(Email.ADDRESS, email)
        .withValue(Email.TYPE, type)
        .build();
  }

  public static ArrayList<ContentProviderOperation> operation(int id,
      EmailSync em1,
      EmailSync em2,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db

      if (em2.getHomeMail() != null) {
        ops.add(add(id, em2.getHomeMail(), Email.TYPE_HOME, create));
      }
      if (em2.getMobileMail() != null) {
        ops.add(add(id, em2.getMobileMail(), Email.TYPE_MOBILE, create));
      }
      if (em2.getOtherMail() != null) {
        ops.add(add(id, em2.getOtherMail(), Email.TYPE_OTHER, create));
      }
      if (em2.getWorkMail() != null) {
        ops.add(add(id, em2.getWorkMail(), Email.TYPE_WORK, create));
      }

    } else if (em1 == null && em2 == null) { // nothing
    } else if (em1 != null && em2 == null) { // clear or update data in db
      if (em1.getHomeMail() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_HOME),
            null)));
      }
      if (em1.getMobileMail() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Email.TYPE_MOBILE), null)));
      }
      if (em1.getOtherMail() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_OTHER),
            null)));
      }
      if (em1.getWorkMail() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_WORK),
            null)));
      }
    } else if (em1 != null && em2 != null) { // merge

      String i = ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_HOME), null);
      if (i == null && em2.getHomeMail() != null) {
        ops.add(add(id, em2.getHomeMail(), Email.TYPE_HOME, create));
      } else if (i != null && em2.getHomeMail() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getHomeMail() != null
          && !em2.getHomeMail().equals(em1.getHomeMail())) {
        ops.add(update(i, em2.getHomeMail(), Email.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_MOBILE), null);
      if (i == null && em2.getMobileMail() != null) {
        ops.add(add(id, em2.getMobileMail(), Email.TYPE_HOME, create));
      } else if (i != null && em2.getMobileMail() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getMobileMail() != null
          && !em2.getMobileMail().equals(em1.getMobileMail())) {
        ops.add(update(i, em2.getMobileMail(), Email.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_OTHER), null);
      if (i == null && em2.getOtherMail() != null) {
        ops.add(add(id, em2.getOtherMail(), Email.TYPE_HOME, create));
      } else if (i != null && em2.getOtherMail() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getOtherMail() != null
          && !em2.getOtherMail().equals(em1.getOtherMail())) {
        ops.add(update(i, em2.getOtherMail(), Email.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Email.TYPE_WORK), null);
      if (i == null && em2.getWorkMail() != null) {
        ops.add(add(id, em2.getWorkMail(), Email.TYPE_HOME, create));
      } else if (i != null && em2.getWorkMail() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getWorkMail() != null
          && !em2.getWorkMail().equals(em1.getWorkMail())) {
        ops.add(update(i, em2.getWorkMail(), Email.TYPE_HOME));
      }
    }
    return ops.size() > 0 ? ops : null;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((homeMail == null) ? 0 : homeMail.hashCode());
    result = prime * result + ((mobileMail == null) ? 0 : mobileMail.hashCode());
    result = prime * result + ((otherMail == null) ? 0 : otherMail.hashCode());
    result = prime * result + ((workMail == null) ? 0 : workMail.hashCode());
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    EmailSync other = (EmailSync) obj;
    if (homeMail == null) {
      if (other.homeMail != null) {
        return false;
      }
    } else if (!homeMail.equals(other.homeMail)) {
      return false;
    }
    if (mobileMail == null) {
      if (other.mobileMail != null) {
        return false;
      }
    } else if (!mobileMail.equals(other.mobileMail)) {
      return false;
    }
    if (otherMail == null) {
      if (other.otherMail != null) {
        return false;
      }
    } else if (!otherMail.equals(other.otherMail)) {
      return false;
    }
    if (workMail == null) {
      if (other.workMail != null) {
        return false;
      }
    } else if (!workMail.equals(other.workMail)) {
      return false;
    }
    return true;
  }
}
