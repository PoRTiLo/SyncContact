package cz.xsendl00.synccontact.utils;

/**
 * Constants.
 */
public final class Constants {

  private Constants() {

  }

  /* Name of setting file */
  public static final String PREFS_NAME = "SyncContactSetting";
  public static final String PREFS_SET_SYNC_CONTACT = "SetSyncContact";
  public static final String PREFS_SHOW_INFO = "ShowInfo";
  public static final String PREFS_START_FIRST = "StartFirst";
  public static final String PREFS_WIFI = "wifi";

  public static final String ACCOUNT_TYPE = "cz.xsendl00.synccontact";
  public static final String ACCOUNT_NAME = "syncContact@synccontact.xsendl00.cz";
  public static final String ACCOUNT_FILTER_LDAP = "(objectClass=googleContact)";
  public static final String ACCOUNT_FILTER_LDAP_GROUP = "(objectClass=groupOfNames)";
  public static final String ACCOUNT_OU_PEOPLE = "ou=people,";
  public static final String ACCOUNT_OU_GROUPS = "ou=groups,";

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

  /** Activity text **/

  public static final String INTENT_FIRST = "FIRST";
  public static final String INTENT_ID = "ID";
  public static final String INTENT_NAME = "NAME";
  public static final String INTENT_SYNC = "SYNC";
  public static final String INTENT_SELECTED = "SELECTED";

  /**
   * LDAP port to server.
   */
  public static final String SSL_TLS = "636";
  public static final String STARTTLS = "389";

  public static final Integer SSL_TLS_INT = 2;
  public static final Integer STARTTLS_INT = 3;

  public static final Integer TRUST_YES = 1;
  public static final Integer TRUST_NO = 2;


  /** ---------->USED---------------------- **/
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
  /*
   * public static final String IM_NULL_AIM = "imNullAim"; public static final String
   * IM_NULL_GOOGLE_TALK = "imNullGoogleTalk"; public static final String IM_NULL_ICQ = "imNullIcq";
   * public static final String IM_NULL_JABBER = "imNullJabber"; public static final String
   * IM_NULL_MSN = "imNullMsn"; public static final String IM_NULL_NETMEETING = "imNullNetmeeting";
   * public static final String IM_NULL_QQ = "imNullQq"; public static final String IM_NULL_SKYPE =
   * "imNullSkype"; public static final String IM_NULL_YAHOO = "imNullYahoo";
   */
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
  public static final String UUID = "uuid";

  public static final String CN = "cn";
  public static final String DESCRIPTION = "description";
  /* GROUP_OF_NAME --- ldap class */
  public static final String GROUP_DESCRIPTION = "description";
  public static final String GROUP_MEMBER = "member";

  /* AddserverActivity */
  public static final String PAR_CONFIRMCREDENTIALS = "confirmCredentials";
  public static final String PAR_USERNAME = "username";
  public static final String PAR_PASSWORD = "password";
  public static final String PAR_HOST = "host";
  public static final String PAR_PORT = "port";
  public static final String PAR_ENCRYPTION = "encryption";
  public static final String PAR_AUTHTOKEN_TYPE = "authtokenType";
  public static final String PAR_ACCOUNT_TYPE = "accountType";
  public static final String PAR_IS_ADDING_NEW_ACCOUNT = "newContact";
  public static final String PAR_SEARCHFILTER = "searchFilter";
  public static final String PAR_BASEDN = "baseDN";
  public static final String PAR_MAPPING = "map_";

  /**/
  public static final String DN = "dn";
  public static final String OBJECT_CLASS = "objectClass";
  public static final String OBJECT_CLASS_GOOGLE = "googleContact";
  public static final String OBJECT_CLASS_GROUP_OF_NAME = "groupOfNames";
  public static final String OBJECT_CLASS_INET = "inetOrgPerson";
  public static final String OBJECT_CLASS_ORG = "organizationalPerson";
  public static final String OBJECT_CLASS_PERSON = "person";
  public static final String OBJECT_CLASS_TOP = "top";

  public static final String OBJECT_ATTRIBUTE_MEMBER = "member";

  /** LDAP serach **/
  public static final String LDAP_MODIFY_TIME_STAMP = "modifyTimestamp";
  public static final String LDAP_DELETED = "deleted";
  public static final String LDAP_SYNCHRONIZE = "synchonize";
  public static final String LDAP_ACCOUNT_PREVIOUS_TYPE = "accountTypePrevious";
  public static final String LDAP_ACCOUNT_PREVIOUS_NAME = "accountNamePrevious";
}
