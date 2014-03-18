package cz.xsendl00.synccontact.utils;

public class Constants {
  
  public static final String ACCOUNT_TYPE = "cz.xsendl00.synccontact";

  /**
   * Authtoken type string.
   */
  public static final String AUTHTOKEN_TYPE = "cz.xsendl00.synccontact";

  /**
   * SD card LDAPSync folder.
   */
  public static final String SDCARD_FOLDER = "/SyncContact";
  
  /**
   * SD card LDAPSync folder.
   */
  public static final String CERT_NAME = "/LDAP_CERT";
  
  /**
   * LDAP port to server.
   */
  public static final String SSL_TLS = "636";
  public static final String STARTTLS = "389";

  public static final Integer SSL_TLS_INT = 2;
  public static final Integer STARTTLS_INT = 3;
  
  public static final Integer TRUST_YES = 1;
  public static final Integer TRUST_NO = 2;
  
  
  /** ldap mapping **/
  public static final String ADDITIONAL_NAME = "additionalName";
  public static final String NAME_PREFIX = "namePrefix";
  public static final String NAME_SUFFIX = "nameSuffix";
  public static final String NICKNAME = "nickname";
  public static final String SHORT_NAME = "shortName";
  public static final String MAIDEN_NAME = "maidenName";
  public static final String GENDER = "gender";
  public static final String NOTES = "notes";
  public static final String HOME_MAIL = "homeMail";
  public static final String WORK_MAIL = "workMail";
  public static final String WORK_PHONE = "workPhone";
  public static final String HOME_STREET = "homeStreet";
  public static final String HOME_CITY = "homeCity";
  public static final String HOME_POBOX = "homePOBox";
  public static final String HOME_POSTAL_CODE = "homePostalCode";
  public static final String HOME_COUNTRY = "homeCountry";
  public static final String HOME_EXTENDED_ADDRESS = "homeExtendedAddress";
  public static final String WORK_POSTAL_ADDRESS = "workPostalAddress";
  public static final String WORK_STREET = "workStreet";
  public static final String WORK_CITY = "workCity";
  public static final String WORK_POBOX = "workPOBox";
  public static final String WORK_REGION = "workRegion";
  public static final String WORK_POSTAL_CODE = "workPostalCode";
  public static final String WORK_COUNTRY = "workCountry";
  public static final String WORK_EXTENDED_ADDRESS = "workExtendeAddress";
  public static final String WEBSITE = "website";
}
