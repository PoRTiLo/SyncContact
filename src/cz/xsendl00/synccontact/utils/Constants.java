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
}
