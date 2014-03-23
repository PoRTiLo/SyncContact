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
  public static final String HOME_REGION = "homeRegion";
  public static final String WORK_POSTAL_ADDRESS = "workPostalAddress";
  public static final String WORK_STREET = "workStreet";
  public static final String WORK_CITY = "workCity";
  public static final String WORK_POBOX = "workPOBox";
  public static final String WORK_REGION = "workRegion";
  public static final String WORK_POSTAL_CODE = "workPostalCode";
  public static final String WORK_COUNTRY = "workCountry";
  public static final String WORK_EXTENDED_ADDRESS = "workExtendeAddress";
  public static final String WEBSITE = "website";
  
  /* Person */
  public static final String SN = "sn";
  public static final String CN = "cn";
  public static final String USER_PASSWORD = "userPassword";
  public static final String TELEPHONE_NUMBER = "telephoneNumber";
  public static final String SEE_ALSO = "seeAlso";
  public static final String DESCRIPTION = "description";
  
  /* OrganizationalPerson */
  public static final String TITLE = "title";
  public static final String X12_ADDRESS = "x121Address";
  public static final String REGISTRED_ADDRESS = "registeredAddress";
  public static final String DESTINATION_INDICATOR = "destinationIndicator";
  public static final String INTERNATIONAL_SDN_NUMBER = "internationaliSDNNumber";
  public static final String FASCIMILE_TELEPHONE_NUMBER = "facsimileTelephoneNumber";
  public static final String PREFERRED_DELIVERY_METHOD = "preferredDeliveryMethod";
  public static final String TELEX_NUMBER = "telexNumber";
  
  public static final String POST_OFFICE_BOX = "postOfficeBox";
  public static final String POSTAL_CODE = "postalCode";
  public static final String POSTAL_ADDRESS = "postalAddress";
  public static final String PHYSICAL_DELIVERY_OFFICE_NAME = "physicalDeliveryOfficeName";
  public static final String OU = "ou";
  public static final String ST = "st";
  public static final String L = "l";
  
  /* InetOrgPerson */
  public static final String AUDIO = "audio";
  public static final String BUSSINES_CATEGORY = "businessCategory";
  public static final String INITIALS = "initials";
  public static final String GIVEN_NAME = "givenName";
  public static final String CAR_LICENCE = "carLicense";
  public static final String DEPARTMENT_NUMBER = "departmentNumber";
  public static final String DISPLAY_NAME = "displayName";
  public static final String EMPLOYEE_NUMBER = "employeeNumber";
  public static final String EMPLOYEE_TYPE = "employeeType";
  public static final String JPEG_PHOTO = "jpegPhoto";
  public static final String PREFERRED_LANGUAGE = "preferredLanguage";
  public static final String USER_SMIME_CERTIFICATE = "userSMIMECertificate";
  public static final String USER_PKCS12 = "userPKCS12";
  public static final String HOME_PHONE = "homePhone";
  public static final String HOME_POSTAL_ADDRESS = "homePostalAddress";
  public static final String LABELED_URI = "labeledURI";
  public static final String MAIL = "mail";
  public static final String MANAGER = "manager";
  public static final String MOBILE = "mobile";
  public static final String O = "o";
  public static final String PAGER = "pager";
  public static final String PHOTO = "photo";
  public static final String ROOM_NUMBER = "roomNumber";
  public static final String SECRETARY = "secretary";
  public static final String UID = "uid";
  public static final String USER_CERTIFICATE = "userCertificate";
  public static final String X500_UNIQUE_IDENTIFIER = "x500uniqueIdentifier";
  
  
  /* AddserverActivity */
  public static final String PAR_CONFIRMCREDENTIALS = "confirmCredentials";
  public static final String PAR_USERNAME = "username";
  public static final String PAR_PASSWORD = "password";
  public static final String PAR_HOST = "host";
  public static final String PAR_PORT = "port";
  public static final String PAR_ENCRYPTION = "encryption";
  public static final String PAR_AUTHTOKEN_TYPE = "authtokenType";
  public static final String PAR_SEARCHFILTER = "searchFilter";
  public static final String PAR_BASEDN = "baseDN";
  public static final String PAR_MAPPING = "map_";
}
