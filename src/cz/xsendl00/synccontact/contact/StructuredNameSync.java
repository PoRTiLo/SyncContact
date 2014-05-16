package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import cz.xsendl00.synccontact.utils.Constants;

public class StructuredNameSync extends AbstractType implements ContactInterface {

  private String phoneticMiddleName;
  private String phoneticGivenName;
  private String phoneticFamilyName;
  private String familyName;
  private String middleName;
  private String displayName;
  private String givenName;
  private String namePrefix;
  private String nameSuffix;

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getNamePrefix() {
    return namePrefix;
  }

  public void setNamePrefix(String namePrefix) {
    this.namePrefix = namePrefix;
  }

  public String getNameSuffix() {
    return nameSuffix;
  }

  public void setNameSuffix(String nameSuffix) {
    this.nameSuffix = nameSuffix;
  }

  public String getPhoneticMiddleName() {
    return phoneticMiddleName;
  }

  public void setPhoneticMiddleName(String phoneticMiddleName) {
    this.phoneticMiddleName = phoneticMiddleName;
  }

  public String getPhoneticGivenName() {
    return phoneticGivenName;
  }

  public void setPhoneticGivenName(String phoneticGivenName) {
    this.phoneticGivenName = phoneticGivenName;
  }

  public String getPhoneticFamilyName() {
    return phoneticFamilyName;
  }

  public void setPhoneticFamilyName(String phoneticFamilyName) {
    this.phoneticFamilyName = phoneticFamilyName;
  }

  @Override
  public String toString() {
    return "StructuredNameSync [phoneticMiddleName=" + phoneticMiddleName + ", phoneticGivenName="
        + phoneticGivenName + ", phoneticFamilyName=" + phoneticFamilyName + ", familyName="
        + familyName + ", middleName=" + middleName + ", displayName=" + displayName
        + ", givenName=" + givenName + ", namePrefix=" + namePrefix + ", nameSuffix=" + nameSuffix
        + "]";
  }

  @Override
  public void defaultValue() {
    phoneticMiddleName = Constants.PHONETIC_MIDDLE_NAME;
    phoneticGivenName = Constants.PHONETIC_GIVEN_NAME;
    phoneticFamilyName = Constants.PHONETIC_FAMILY_NAME;
    familyName = Constants.FAMILY_NAME;
    middleName = Constants.MIDDLE_NAME;
    displayName = Constants.DISPLAY_NAME;
    givenName = Constants.GIVEN_NAME;
    namePrefix = Constants.NAME_PREFIX;
    nameSuffix = Constants.NAME_SUFFIX;

  }

  public static ContentValues compare(StructuredNameSync obj1, StructuredNameSync obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getDisplayName() != null) {
        values.put(Constants.DISPLAY_NAME, obj2.getDisplayName());
      }
      if (obj2.getFamilyName() != null) {
        values.put(Constants.FAMILY_NAME, obj2.getFamilyName());
      }
      if (obj2.getGivenName() != null) {
        values.put(Constants.GIVEN_NAME, obj2.getGivenName());
      }
      if (obj2.getMiddleName() != null) {
        values.put(Constants.MIDDLE_NAME, obj2.getMiddleName());
      }
      if (obj2.getNamePrefix() != null) {
        values.put(Constants.NAME_PREFIX, obj2.getNamePrefix());
      }
      if (obj2.getNameSuffix() != null) {
        values.put(Constants.NAME_SUFFIX, obj2.getNameSuffix());
      }
      if (obj2.getPhoneticFamilyName() != null) {
        values.put(Constants.PHONETIC_FAMILY_NAME, obj2.getPhoneticFamilyName());
      }
      if (obj2.getPhoneticGivenName() != null) {
        values.put(Constants.PHONETIC_GIVEN_NAME, obj2.getPhoneticGivenName());
      }
      if (obj2.getPhoneticMiddleName() != null) {
        values.put(Constants.PHONETIC_MIDDLE_NAME, obj2.getPhoneticMiddleName());
      }
    } else if (obj1 == null && obj2 == null) { // nothing

    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getDisplayName() != null) {
        values.putNull(Constants.DISPLAY_NAME);
      }
      if (obj1.getFamilyName() != null) {
        values.putNull(Constants.FAMILY_NAME);
      }
      if (obj1.getGivenName() != null) {
        values.putNull(Constants.GIVEN_NAME);
      }
      if (obj1.getMiddleName() != null) {
        values.putNull(Constants.MIDDLE_NAME);
      }
      if (obj1.getNamePrefix() != null) {
        values.putNull(Constants.NAME_PREFIX);
      }
      if (obj1.getNameSuffix() != null) {
        values.putNull(Constants.NAME_SUFFIX);
      }
      if (obj1.getPhoneticFamilyName() != null) {
        values.putNull(Constants.PHONETIC_FAMILY_NAME);
      }
      if (obj1.getPhoneticGivenName() != null) {
        values.putNull(Constants.PHONETIC_GIVEN_NAME);
      }
      if (obj1.getPhoneticMiddleName() != null) {
        values.putNull(Constants.PHONETIC_MIDDLE_NAME);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getDisplayName() != null) {
        values.put(Constants.DISPLAY_NAME, obj2.getDisplayName());
      } else {
        values.putNull(Constants.DISPLAY_NAME);
      }
      if (obj2.getFamilyName() != null) {
        values.put(Constants.FAMILY_NAME, obj2.getFamilyName());
      } else {
        values.putNull(Constants.FAMILY_NAME);
      }
      if (obj2.getGivenName() != null) {
        values.put(Constants.GIVEN_NAME, obj2.getGivenName());
      } else {
        values.putNull(Constants.GIVEN_NAME);
      }
      if (obj2.getMiddleName() != null) {
        values.put(Constants.MIDDLE_NAME, obj2.getMiddleName());
      } else {
        values.putNull(Constants.MIDDLE_NAME);
      }
      if (obj2.getNamePrefix() != null) {
        values.put(Constants.NAME_PREFIX, obj2.getNamePrefix());
      } else {
        values.putNull(Constants.NAME_PREFIX);
      }
      if (obj2.getNameSuffix() != null) {
        values.put(Constants.NAME_SUFFIX, obj2.getNameSuffix());
      } else {
        values.putNull(Constants.NAME_SUFFIX);
      }
      if (obj2.getPhoneticFamilyName() != null) {
        values.put(Constants.PHONETIC_FAMILY_NAME, obj2.getPhoneticFamilyName());
      } else {
        values.putNull(Constants.PHONETIC_FAMILY_NAME);
      }
      if (obj2.getPhoneticGivenName() != null) {
        values.put(Constants.PHONETIC_GIVEN_NAME, obj2.getPhoneticGivenName());
      } else {
        values.putNull(Constants.PHONETIC_GIVEN_NAME);
      }
      if (obj2.getPhoneticMiddleName() != null) {
        values.put(Constants.PHONETIC_MIDDLE_NAME, obj2.getPhoneticMiddleName());
      } else {
        values.putNull(Constants.PHONETIC_MIDDLE_NAME);
      }
    }
    return values;
  }


  public static ContentProviderOperation add(int id, Map<String, String> values, boolean create) {
    ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
    if (create) {
      operationBuilder.withValueBackReference(Data.RAW_CONTACT_ID, id).withValue(Data.MIMETYPE,
          StructuredName.CONTENT_ITEM_TYPE);
    } else {
      operationBuilder.withValue(Data.RAW_CONTACT_ID, id).withValue(Data.MIMETYPE,
          StructuredName.CONTENT_ITEM_TYPE);
    }
    Iterator<String> iter = values.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      String val = values.get(key);
      operationBuilder.withValue(key, val);
    }
    return operationBuilder.build();
  }

  public static ContentProviderOperation update(String id, Map<String, String> values) {
    ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(Data.CONTENT_URI);
    operationBuilder.withSelection(Data._ID + "=?", new String[]{id}).withValue(Data.MIMETYPE,
        StructuredName.CONTENT_ITEM_TYPE);

    Iterator<String> iter = values.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      String val = values.get(key);
      operationBuilder.withValue(key, val);
    }
    return operationBuilder.build();
  }


  public static ArrayList<ContentProviderOperation> operation(int id,
      StructuredNameSync em1,
      StructuredNameSync em2,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      Map<String, String> value = op(em2);
      if (value.size() > 0) {
        ops.add(add(id, value, create));
      }
    } else if (em1 == null && em2 == null) { // nothing

    } else if (em1 != null && em2 == null) { // clear or update data in db
      if (em1.getDisplayName() != null || em1.getFamilyName() != null || em1.getGivenName() != null
          || em1.getMiddleName() != null || em1.getNamePrefix() != null
          || em1.getNameSuffix() != null || em1.getPhoneticFamilyName() != null
          || em1.getPhoneticGivenName() != null || em1.getPhoneticMiddleName() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(StructuredName.CONTENT_ITEM_TYPE), null)));
      }
    } else if (em1 != null && em2 != null) { // merge
      Map<String, String> value = op(em2);

      if (value.size() > 0) {
        ops.add(update(
            ID.getIdByValue(em1.getID(), String.valueOf(StructuredName.CONTENT_ITEM_TYPE), null),
            value));
      }
    }
    return ops.size() > 0 ? ops : null;
  }

  private static Map<String, String> op(StructuredNameSync em2) {
    Map<String, String> value = new HashMap<String, String>();
    if (em2.getDisplayName() != null) {
      value.put(StructuredName.DISPLAY_NAME, em2.getDisplayName());
    }
    if (em2.getFamilyName() != null) {
      value.put(StructuredName.FAMILY_NAME, em2.getFamilyName());
    }
    if (em2.getGivenName() != null) {
      value.put(StructuredName.GIVEN_NAME, em2.getGivenName());
    }
    if (em2.getMiddleName() != null) {
      value.put(StructuredName.MIDDLE_NAME, em2.getMiddleName());
    }
    if (em2.getNamePrefix() != null) {
      value.put(StructuredName.PREFIX, em2.getNamePrefix());
    }
    if (em2.getNameSuffix() != null) {
      value.put(StructuredName.SUFFIX, em2.getNameSuffix());
    }
    if (em2.getPhoneticFamilyName() != null) {
      value.put(StructuredName.PHONETIC_FAMILY_NAME, em2.getPhoneticFamilyName());
    }
    if (em2.getPhoneticGivenName() != null) {
      value.put(StructuredName.PHONETIC_GIVEN_NAME, em2.getPhoneticGivenName());
    }
    if (em2.getPhoneticMiddleName() != null) {
      value.put(StructuredName.PHONETIC_MIDDLE_NAME, em2.getPhoneticMiddleName());
    }
    return value;
  }
}
