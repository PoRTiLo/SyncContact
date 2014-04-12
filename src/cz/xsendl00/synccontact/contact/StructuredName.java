package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

public class StructuredName {

  private String phoneticMiddleName = Constants.PHONETIC_MIDDLE_NAME;
  private String phoneticGivenName = Constants.PHONETIC_GIVEN_NAME;
  private String phoneticFamilyName = Constants.PHONETIC_FAMILY_NAME;
  private String familyName = Constants.FAMILY_NAME;
  private String middleName = Constants.MIDDLE_NAME;
  private String displayName = Constants.DISPLAY_NAME;
  private String givenName = Constants.GIVEN_NAME;
  private String namePrefix = Constants.NAME_PREFIX;
  private String nameSuffix = Constants.NAME_SUFFIX;
  
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

}
