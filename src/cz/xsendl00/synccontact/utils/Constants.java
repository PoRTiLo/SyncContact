package cz.xsendl00.synccontact.utils;


public class Constants {
  
  /* Name of setting file */
  public static final String PREFS_NAME = "SyncContactSetting";
  public static final String SET_SYNC_CONTACT = "SetSyncContact";
  
  public static final String ACCOUNT_TYPE = "cz.xsendl00.synccontact";
  public static final String ACCOUNT_NAME = "syncContact@synccontact.xsendl00.cz";

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
  public static final String NICKNAME = "nickname";
  public static final String SHORT_NAME = "shortName";
  public static final String MAIDEN_NAME = "maidenName";
  public static final String GENDER = "gender";  
  public static final String WORK_PHONE = "workPhone";
  public static final String HOME_EXTENDED_ADDRESS = "homeExtendedAddress";
  public static final String WORK_POSTAL_ADDRESS = "workPostalAddress";
  public static final String WORK_EXTENDED_ADDRESS = "workExtendeAddress";
  public static final String WEBSITE = "website";
  
  /** ---------->USED----------------------**/
  public static final String WORK_SIP = "workSip";
  public static final String HOME_SIP = "homeSip";
  public static final String OTHER_SIP = "otherSip";
  public static final String EVENT_OTHER = "eventOther";
  public static final String EVENT_BIRTHDAY = "eventBirthday";
  public static final String EVENT_ANNIVERSARY = "eventAnniversary";
  public static final String NICKNAME_DEFAULT = "nicknameDefault";
  public static final String NICKNAME_OTHER = "nicknameOther";
  public static final String NICKNAME_MAIDEN = "nicknameMaiden";
  public static final String NICKNAME_SHORT = "nicknameShort";
  public static final String NICKNAME_INITIALS = "nicknameInitials";
  public static final String PHONE_ASSISTANT = "phoneAssistant";
  public static final String PHONE_CALLBACK = "phoneCallback";
  public static final String PHONE_CAR = "phoneCar";
  public static final String PHONE_COMPANY = "phoneCompany";
  public static final String PHONE_FAX_HOME = "phoneFaxHome";
  public static final String PHONE_FAX_WORK = "phoneFaxWork";
  public static final String PHONE_HOME = "phoneHome";
  public static final String PHONE_ISDN = "phoneISDN";
  public static final String PHONE_MAIN = "phoneMain";
  public static final String PHONE_MMS = "phoneMMS";
  public static final String PHONE_MOBILE = "phoneMobile";
  public static final String PHONE_OTHER = "phoneOther";
  public static final String PHONE_OTHER_FAX = "phoneOtherFax";
  public static final String PHONE_PAGER = "phonePager";
  public static final String PHONE_RADIO = "phoneRadio";
  public static final String PHONE_TELEX = "phoneTelex";
  public static final String PHONE_TTY_TDD = "phoneTTYTDD";
  public static final String PHONE_WORK = "phoneWork";
  public static final String PHONE_WORK_MOBILE = "phoneWorkMobile";
  public static final String PHONE_WORK_PAGER = "phoneWorkPager";
  public static final String IM_HOME_AIM = "imHomeAim";
  public static final String IM_HOME_GOOGLE_TALK = "imHomeGoogleTalk";
  public static final String IM_HOME_ICQ = "imHomeIcq";
  public static final String IM_HOME_JABBER = "imHomeJabber";
  public static final String IM_HOME_MSN = "imHomeMsn";
  public static final String IM_HOME_NETMEETING = "imHomeNetmeeting";
  public static final String IM_HOME_QQ = "imHomeQq";
  public static final String IM_HOME_SKYPE = "imHomeSkype";
  public static final String IM_HOME_YAHOO = "imHomeYahoo";
  public static final String IM_WORK_AIM = "imWorkAim";
  public static final String IM_WORK_GOOGLE_TALK = "imWorkGoogleTalk";
  public static final String IM_WORK_ICQ = "imWorkIcq";
  public static final String IM_WORK_JABBER = "imWorkJabber";
  public static final String IM_WORK_MSN = "imWorkMsn";
  public static final String IM_WORK_NETMEETING = "imWorkNetmeeting";
  public static final String IM_WORK_QQ = "imWorkQq";
  public static final String IM_WORK_SKYPE = "imWorkSkype";
  public static final String IM_WORK_YAHOO = "imWorkYahoo";
  public static final String IM_OTHER_AIM = "imOtherAim";
  public static final String IM_OTHER_GOOGLE_TALK = "imOtherGoogleTalk";
  public static final String IM_OTHER_ICQ = "imOtherIcq";
  public static final String IM_OTHER_JABBER = "imOtherJabber";
  public static final String IM_OTHER_MSN = "imOtherMsn";
  public static final String IM_OTHER_NETMEETING = "imOtherNetmeeting";
  public static final String IM_OTHER_QQ = "imOtherQq";
  public static final String IM_OTHER_SKYPE = "imOtherSkype";
  public static final String IM_OTHER_YAHOO = "imOtherYahoo";
  /*public static final String IM_NULL_AIM = "imNullAim";
  public static final String IM_NULL_GOOGLE_TALK = "imNullGoogleTalk";
  public static final String IM_NULL_ICQ = "imNullIcq";
  public static final String IM_NULL_JABBER = "imNullJabber";
  public static final String IM_NULL_MSN = "imNullMsn";
  public static final String IM_NULL_NETMEETING = "imNullNetmeeting";
  public static final String IM_NULL_QQ = "imNullQq";
  public static final String IM_NULL_SKYPE = "imNullSkype";
  public static final String IM_NULL_YAHOO = "imNullYahoo";*/
  public static final String PHONETIC_MIDDLE_NAME = "phoneticMiddleName";
  public static final String PHONETIC_GIVEN_NAME = "phoneticGivenName";
  public static final String PHONETIC_FAMILY_NAME = "phoneticFamilyName";
  public static final String DISPLAY_NAME = "displayName";
  public static final String GIVEN_NAME = "givenName";
  public static final String NAME_PREFIX = "namePrefix";
  public static final String NAME_SUFFIX = "nameSuffix";
  public static final String FAMILY_NAME = "familyName";
  public static final String MIDDLE_NAME = "middleName";
  public static final String ORGANIZATION_WORK_COMPANY = "organizationWorkCompany";
  public static final String ORGANIZATION_WORK_TITLE = "organizationWorkTitle";
  public static final String ORGANIZATION_WORK_DEPARTMENT = "organizationWorkDepartment";
  public static final String ORGANIZATION_WORK_JOB_DESCRIPTION = "organizationWorkJobDescription";
  public static final String ORGANIZATION_WORK_SYMBOL = "organizationWorkSymbol";
  public static final String ORGANIZATION_WORK_PHONETIC_NAME = "organizationWorkPhoneticName";
  public static final String ORGANIZATION_WORK_OFFICE_LOCATION = "organizationWorkOfficeLocation";
  public static final String ORGANIZATION_WORK_PHONETIC_NAME_STYLE = "organizationWorkPhoneticNameStyle";
  public static final String ORGANIZATION_OTHER_COMPANY = "organizationOtherCompany";
  public static final String ORGANIZATION_OTHER_TITLE = "organizationOtherTitle";
  public static final String ORGANIZATION_OTHER_DEPARTMENT = "organizationOtherDepartment";
  public static final String ORGANIZATION_OTHER_JOB_DESCRIPTION = "organizationOtherJobDescription";
  public static final String ORGANIZATION_OTHER_SYMBOL = "organizationOtherSymbol";
  public static final String ORGANIZATION_OTHER_PHONETIC_NAME = "organizationOtherPhoneticName";
  public static final String ORGANIZATION_OTHER_OFFICE_LOCATION = "organizationOtherOfficeLocation";
  public static final String ORGANIZATION_OTHER_PHONETIC_NAME_STYLE = "organizationOtherPhoneticNameStyle";
  public static final String IDENTITY_TEXT = "identityText";
  public static final String IDENTITY_NAMESPACE = "identityNamespace";
  public static final String RELATION_ASSISTANT = "relationAssistant";
  public static final String RELATION_BROTHER = "relationBrother";
  public static final String RELATION_CHILD = "relationChild";
  public static final String RELATION_DOMESTIC_PARTNER = "relationDomesticPartner";
  public static final String RELATION_FATHER = "relationFather";
  public static final String RELATION_FRIEND = "relationFriend";
  public static final String RELATION_MANAGER = "relationManager";
  public static final String RELATION_MOTHER = "relationMother";
  public static final String RELATION_PARENT = "relationParent";
  public static final String RELATION_PARTNER = "relationPartner";
  public static final String RELATION_REFFERED_BY = "relationRefferedBy";
  public static final String RELATION_RELATIVE = "relationRelative";
  public static final String RELATION_SISTER = "relationSister";
  public static final String RELATION_SPOUSE = "relationSpouse";
  public static final String WEBSITE_HOMEPAGE = "websiteHomepage";
  public static final String WEBSITE_BLOG = "websiteBlog";
  public static final String WEBSITE_PROFILE = "websiteProfile";
  public static final String WEBSITE_HOME = "websiteHome";
  public static final String WEBSITE_WORK = "websiteWork";
  public static final String WEBSITE_FTP = "websiteFtp";
  public static final String WEBSITE_OTHER = "websiteOther";
  public static final String HOME_MAIL = "homeMail";
  public static final String WORK_MAIL = "workMail";
  public static final String OTHER_MAIL = "otherMail";
  public static final String MOBILE_MAIL = "mobileMail";
  public static final String NOTES = "notes";
  public static final String HOME_STREET = "homeStreet";
  public static final String HOME_POBOX = "homePOBox";
  public static final String HOME_CITY = "homeCity";
  public static final String HOME_REGION = "homeRegion";
  public static final String HOME_POSTAL_CODE = "homePostalCode";
  public static final String HOME_COUNTRY = "homeCountry";
  public static final String WORK_STREET = "workStreet";
  public static final String WORK_POBOX = "workPOBox";
  public static final String WORK_CITY = "workCity";
  public static final String WORK_REGION = "workRegion";
  public static final String WORK_POSTAL_CODE = "workPostalCode";
  public static final String WORK_COUNTRY = "workCountry";
  public static final String WORK_FORMATTED_ADDRESS = "workFormattedAddress";
  public static final String HOME_FORMATTED_ADDRESS = "homeFormattedAddress";
  public static final String WORK_NEIGHBORHOOD = "workNeighborhood";
  public static final String HOME_NEIGHBORHOOD = "homeNeighborhood";
  public static final String OTHER_NEIGHBORHOOD = "otherNeighborhood";
  public static final String OTHER_STREET = "otherStreet";
  public static final String OTHER_CITY = "otherCity";
  public static final String OTHER_POBOX = "otherPOBox";
  public static final String OTHER_REGION = "otherRegion";
  public static final String OTHER_POSTAL_CODE = "otherPostalCode";
  public static final String OTHER_COUNTRY = "otherCountry";
  public static final String OTHER_FORMATTED_ADDRESS = "otherFormattedAddress";
  
  
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
  
  public static final String CAR_LICENCE = "carLicense";
  public static final String DEPARTMENT_NUMBER = "departmentNumber";
  
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
  public static final String PAR_ACCOUNT_TYPE = "accountType";
  public static final String PAR_IS_ADDING_NEW_ACCOUNT= "newContact";
  public static final String PAR_SEARCHFILTER = "searchFilter";
  public static final String PAR_BASEDN = "baseDN";
  public static final String PAR_MAPPING = "map_";
  
  /**/
  public static final String DN = "dn";
  public static final String OBJECT_CLASS = "objectClass";
  public static final String OBJECT_CLASS_GOOGLE = "googleContact";
  public static final String OBJECT_CLASS_INET = "inetOrgPerson";
  public static final String OBJECT_CLASS_ORG ="organizationalPerson";
  public static final String OBJECT_CLASS_PERSON ="person";
  public static final String OBJECT_CLASS_TOP = "top";
  
  /** LDAP serach **/
  public static final String LDAP_MODIFY_TIME_STAMP = "modifyTimestamp";
}
