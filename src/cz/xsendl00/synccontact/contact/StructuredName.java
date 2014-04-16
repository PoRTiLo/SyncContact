package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
import cz.xsendl00.synccontact.utils.Constants;

public class StructuredName extends AbstractType implements ContactInterface {

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
    return "StructuredName [phoneticMiddleName=" + phoneticMiddleName
        + ", phoneticGivenName=" + phoneticGivenName + ", phoneticFamilyName="
        + phoneticFamilyName + ", familyName=" + familyName + ", middleName="
        + middleName + ", displayName=" + displayName + ", givenName="
        + givenName + ", namePrefix=" + namePrefix + ", nameSuffix="
        + nameSuffix + "]";
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
  
  public static ContentValues compare(StructuredName obj1, StructuredName obj2) {
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
}
