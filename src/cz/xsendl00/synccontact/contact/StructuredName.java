package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

public class StructuredName implements ContactInterface {

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
}
