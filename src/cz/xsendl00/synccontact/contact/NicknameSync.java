package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.Data;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing the contact's nickname. For example, for Bob Parr ("Mr. Incredible"):
 *
 * @author xsendl00
 */
public class NicknameSync extends AbstractType implements ContactInterface {

  private String nicknameDefault;
  private String nicknameOther;
  private String nicknameMaiden;
  private String nicknameShort;
  private String nicknameInitials;

  public NicknameSync() {
  }

  public NicknameSync(String nicknameDefault,
      String nicknameOther,
      String nicknameMaiden,
      String nicknameShort,
      String nicknameInitials) {
    this.nicknameDefault = nicknameDefault;
    this.nicknameInitials = nicknameInitials;
    this.nicknameMaiden = nicknameMaiden;
    this.nicknameOther = nicknameOther;
    this.nicknameShort = nicknameShort;
  }

  public String getNicknameDefault() {
    return nicknameDefault;
  }

  public void setNicknameDefault(String nicknameDefault) {
    this.nicknameDefault = nicknameDefault;
  }

  public String getNicknameOther() {
    return nicknameOther;
  }

  public void setNicknameOther(String nicknameOther) {
    this.nicknameOther = nicknameOther;
  }

  public String getNicknameMaiden() {
    return nicknameMaiden;
  }

  public void setNicknameMaiden(String nicknameMaiden) {
    this.nicknameMaiden = nicknameMaiden;
  }

  public String getNicknameShort() {
    return nicknameShort;
  }

  public void setNicknameShort(String nicknameShort) {
    this.nicknameShort = nicknameShort;
  }

  public String getNicknameInitials() {
    return nicknameInitials;
  }

  public void setNicknameInitials(String nicknameInitials) {
    this.nicknameInitials = nicknameInitials;
  }

  @Override
  public String toString() {
    return "Nickname [nicknameDefault=" + nicknameDefault + ", nicknameOther=" + nicknameOther
        + ", nicknameMaiden=" + nicknameMaiden + ", nicknameShort=" + nicknameShort
        + ", nicknameInitials=" + nicknameInitials + "]";
  }

  @Override
  public void defaultValue() {
    nicknameDefault = Constants.NICKNAME_DEFAULT;
    nicknameOther = Constants.NICKNAME_OTHER;
    nicknameMaiden = Constants.NICKNAME_MAIDEN;
    nicknameShort = Constants.NICKNAME_SHORT;
    nicknameInitials = Constants.NICKNAME_INITIALS;
  }

  public static ContentValues compare(NicknameSync obj1, NicknameSync obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getNicknameDefault() != null) {
        values.put(Constants.NICKNAME_DEFAULT, obj2.getNicknameDefault());
      }
      if (obj2.getNicknameInitials() != null) {
        values.put(Constants.NICKNAME_INITIALS, obj2.getNicknameInitials());
      }
      if (obj2.getNicknameMaiden() != null) {
        values.put(Constants.NICKNAME_MAIDEN, obj2.getNicknameMaiden());
      }
      if (obj2.getNicknameOther() != null) {
        values.put(Constants.NICKNAME_OTHER, obj2.getNicknameOther());
      }
      if (obj2.getNicknameShort() != null) {
        values.put(Constants.NICKNAME_SHORT, obj2.getNicknameShort());
      }
    } else if (obj1 == null && obj2 == null) { // nothing

    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getNicknameDefault() != null) {
        values.putNull(Constants.NICKNAME_DEFAULT);
      }
      if (obj1.getNicknameInitials() != null) {
        values.putNull(Constants.NICKNAME_INITIALS);
      }
      if (obj1.getNicknameMaiden() != null) {
        values.putNull(Constants.NICKNAME_MAIDEN);
      }
      if (obj1.getNicknameOther() != null) {
        values.putNull(Constants.NICKNAME_OTHER);
      }
      if (obj1.getNicknameShort() != null) {
        values.putNull(Constants.NICKNAME_SHORT);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getNicknameDefault() != null) {
        values.put(Constants.NICKNAME_DEFAULT, obj2.getNicknameDefault());
      } else {
        values.putNull(Constants.NICKNAME_DEFAULT);
      }
      if (obj2.getNicknameInitials() != null) {
        values.put(Constants.NICKNAME_INITIALS, obj2.getNicknameInitials());
      } else {
        values.putNull(Constants.NICKNAME_INITIALS);
      }
      if (obj2.getNicknameMaiden() != null) {
        values.put(Constants.NICKNAME_MAIDEN, obj2.getNicknameMaiden());
      } else {
        values.putNull(Constants.NICKNAME_MAIDEN);
      }
      if (obj2.getNicknameOther() != null) {
        values.put(Constants.NICKNAME_OTHER, obj2.getNicknameOther());
      } else {
        values.putNull(Constants.NICKNAME_OTHER);
      }
      if (obj2.getNicknameShort() != null) {
        values.put(Constants.NICKNAME_SHORT, obj2.getNicknameShort());
      } else {
        values.putNull(Constants.NICKNAME_SHORT);
      }
    }
    return values;
  }

  /**
   * @param id raw_contact id
   * @param value name of protocol
   * @param type like Nickname.TYPE_HOME
   * @param create true/false
   * @return ContentProviderOperation
   */
  public static ContentProviderOperation add(int id, String value, int type, boolean create) {
    if (create) {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValueBackReference(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Nickname.CONTENT_ITEM_TYPE)
          .withValue(Nickname.TYPE, type)
          .withValue(Nickname.DATA, value)
          .build();
    } else {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValue(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Nickname.CONTENT_ITEM_TYPE)
          .withValue(Nickname.TYPE, type)
          .withValue(Nickname.DATA, value)
          .build();
    }
  }

  public static ContentProviderOperation update(int id, String value, int type) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{String.valueOf(id)})
        .withValue(Data.MIMETYPE, Nickname.CONTENT_ITEM_TYPE)
        .withValue(Nickname.DATA, value)
        .build();
  }


  public static ArrayList<ContentProviderOperation> operation(int id,
      NicknameSync em1,
      NicknameSync em2,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      if (em2.getNicknameDefault() != null) {
        ops.add(add(id, em2.getNicknameDefault(), Nickname.TYPE_OTHER_NAME, create));
      }
      if (em2.getNicknameInitials() != null) {
        ops.add(add(id, em2.getNicknameInitials(), Nickname.TYPE_INITIALS, create));
      }
      if (em2.getNicknameMaiden() != null) {
        ops.add(add(id, em2.getNicknameMaiden(), Nickname.TYPE_MAIDEN_NAME, create));
      }
      if (em2.getNicknameOther() != null) {
        ops.add(add(id, em2.getNicknameOther(), Nickname.TYPE_OTHER_NAME, create));
      }
      if (em2.getNicknameShort() != null) {
        ops.add(add(id, em2.getNicknameShort(), Nickname.TYPE_SHORT_NAME, create));
      }
    } else if (em1 == null && em2 == null) { // nothing

    } else if (em1 != null && em2 == null) { // delete
      if (em1.getNicknameDefault() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Nickname.TYPE_DEFAULT), null)));
      }
      if (em1.getNicknameInitials() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Nickname.TYPE_INITIALS), null)));
      }
      if (em1.getNicknameMaiden() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Nickname.TYPE_MAIDEN_NAME), null)));
      }
      if (em1.getNicknameOther() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Nickname.TYPE_OTHER_NAME), null)));
      }
      if (em1.getNicknameShort() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Nickname.TYPE_SHORT_NAME), null)));
      }

    } else if (em1 != null && em2 != null) { // clear or update data in db
      ArrayList<ContentProviderOperation> op;
      op = createOps(em1.getNicknameDefault(), em2.getNicknameDefault(), em1.getID(),
          Nickname.TYPE_DEFAULT, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      op = createOps(em1.getNicknameInitials(), em2.getNicknameInitials(), em1.getID(),
          Nickname.TYPE_INITIALS, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getNicknameMaiden(), em2.getNicknameMaiden(), em1.getID(),
          Nickname.TYPE_MAIDEN_NAME, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getNicknameOther(), em2.getNicknameOther(), em1.getID(),
          Nickname.TYPE_OTHER_NAME, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getNicknameShort(), em2.getNicknameShort(), em1.getID(),
          Nickname.TYPE_SHORT_NAME, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
    }
    return ops.size() > 0 ? ops : null;
  }

  /**
   * @param value1
   * @param value2
   * @param ids
   * @param type
   * @param id
   * @return
   */
  private static ArrayList<ContentProviderOperation> createOps(String value1,
      String value2,
      List<ID> ids,
      int type,
      int id,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (value2 != null || value2 != null) {
      String i = ID.getIdByValue(ids, String.valueOf(type), null);
      if (i == null && value2 != null) {
        ops.add(add(id, value2, type, create));
      } else if (i != null && value2 == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && value2 != null && !value2.equals(value1)) {
        ops.add(update(id, value2, type));
      }
    }
    return ops.size() > 0 ? ops : null;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((nicknameDefault == null) ? 0 : nicknameDefault.hashCode());
    result = prime * result + ((nicknameInitials == null) ? 0 : nicknameInitials.hashCode());
    result = prime * result + ((nicknameMaiden == null) ? 0 : nicknameMaiden.hashCode());
    result = prime * result + ((nicknameOther == null) ? 0 : nicknameOther.hashCode());
    result = prime * result + ((nicknameShort == null) ? 0 : nicknameShort.hashCode());
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
    NicknameSync other = (NicknameSync) obj;
    if (nicknameDefault == null) {
      if (other.nicknameDefault != null) {
        return false;
      }
    } else if (!nicknameDefault.equals(other.nicknameDefault)) {
      return false;
    }
    if (nicknameInitials == null) {
      if (other.nicknameInitials != null) {
        return false;
      }
    } else if (!nicknameInitials.equals(other.nicknameInitials)) {
      return false;
    }
    if (nicknameMaiden == null) {
      if (other.nicknameMaiden != null) {
        return false;
      }
    } else if (!nicknameMaiden.equals(other.nicknameMaiden)) {
      return false;
    }
    if (nicknameOther == null) {
      if (other.nicknameOther != null) {
        return false;
      }
    } else if (!nicknameOther.equals(other.nicknameOther)) {
      return false;
    }
    if (nicknameShort == null) {
      if (other.nicknameShort != null) {
        return false;
      }
    } else if (!nicknameShort.equals(other.nicknameShort)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean isNull() {
    return nicknameDefault == null && nicknameOther == null && nicknameMaiden == null
        && nicknameShort == null && nicknameInitials == null;
  }

}
