package cz.xsendl00.synccontact.client;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * Definition of the inetOrgPerson LDAP Object Class.
 * 
 * Request for Comments: 2798 
 * 
 * While the X.500 standards define many useful attribute types [X520]
   and object classes [X521], they do not define a person object class
   that meets the requirements found in today's Internet and Intranet
   directory service deployments.  We define a new object class called
   inetOrgPerson for use in LDAP and X.500 directory services that
   extends the X.521 standard organizationalPerson class to meet these
   needs.
 * 
 * @author portilo
 *
 */
public class InetOrgPerson extends OrganizationalPerson {
  /*
  ( 2.16.840.1.113730.3.1.1 NAME 'carLicense'
      DESC 'vehicle license or registration plate'
      EQUALITY caseIgnoreMatch
      SUBSTR caseIgnoreSubstringsMatch
      SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )
  */
  private String carLicense = Constants.CAR_LICENCE;
  
  /*
  ( 2.16.840.1.113730.3.1.2
    NAME 'departmentNumber'
    DESC 'identifies a department within an organization'
    EQUALITY caseIgnoreMatch
    SUBSTR caseIgnoreSubstringsMatch
    SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )
   */
  private String departmentNumber = Constants.DEPARTMENT_NUMBER;
  
  /*
   * ( 2.16.840.1.113730.3.1.241
    NAME 'displayName'
    DESC 'preferred name of a person to be used when displaying entries'
    EQUALITY caseIgnoreMatch
    SUBSTR caseIgnoreSubstringsMatch
    SYNTAX 1.3.6.1.4.1.1466.115.121.1.15
    SINGLE-VALUE )
   */
  private String displayName = Constants.DISPLAY_NAME;
  
  /*
   * ( 2.16.840.1.113730.3.1.3
    NAME 'employeeNumber'
    DESC 'numerically identifies an employee within an organization'
    EQUALITY caseIgnoreMatch
    SUBSTR caseIgnoreSubstringsMatch
    SYNTAX 1.3.6.1.4.1.1466.115.121.1.15
    SINGLE-VALUE )
   */
  private String employeeNumber = Constants.EMPLOYEE_NUMBER;
  
  /*
   * ( 2.16.840.1.113730.3.1.4
    NAME 'employeeType'
    DESC 'type of employment for a person'
    EQUALITY caseIgnoreMatch
    SUBSTR caseIgnoreSubstringsMatch
    SYNTAX 1.3.6.1.4.1.1466.115.121.1.15 )
   */
  private String employeeType = Constants.EMPLOYEE_NUMBER;
  
  /*
   * ( 0.9.2342.19200300.100.1.60
    NAME 'jpegPhoto'
    DESC 'a JPEG image'
    SYNTAX 1.3.6.1.4.1.1466.115.121.1.28 )
   */
  private String jpegPhoto = Constants.JPEG_PHOTO;
  
  /*
   * ( 2.16.840.1.113730.3.1.39
    NAME 'preferredLanguage'
    DESC 'preferred written or spoken language for a person'
    EQUALITY caseIgnoreMatch
    SUBSTR caseIgnoreSubstringsMatch
    SYNTAX 1.3.6.1.4.1.1466.115.121.1.15
    SINGLE-VALUE )
   */
  private String preferredLanguage = Constants.PREFERRED_LANGUAGE;
  
  /*
   * ( 2.16.840.1.113730.3.1.40
    NAME 'userSMIMECertificate'
    DESC 'signed message used to support S/MIME'
    SYNTAX 1.3.6.1.4.1.1466.115.121.1.5 )
   */
  private String userSMIMECertificate = Constants.USER_SMIME_CERTIFICATE;
  
  /*
   * ( 2.16.840.1.113730.3.1.216
    NAME 'userPKCS12'
    DESC 'PKCS #12 PFX PDU for exchange of personal identity information'
    SYNTAX 1.3.6.1.4.1.1466.115.121.1.5 )
   */
  private String userPKCS12 = Constants.USER_PKCS12;

  public String getCarLicense() {
    return carLicense;
  }

  public void setCarLicense(String carLicense) {
    this.carLicense = carLicense;
  }

  public String getDepartmentNumber() {
    return departmentNumber;
  }

  public void setDepartmentNumber(String departmentNumber) {
    this.departmentNumber = departmentNumber;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getEmployeeNumber() {
    return employeeNumber;
  }

  public void setEmployeeNumber(String employeeNumber) {
    this.employeeNumber = employeeNumber;
  }

  public String getEmployeeType() {
    return employeeType;
  }

  public void setEmployeeType(String employeeType) {
    this.employeeType = employeeType;
  }

  public String getJpegPhoto() {
    return jpegPhoto;
  }

  public void setJpegPhoto(String jpegPhoto) {
    this.jpegPhoto = jpegPhoto;
  }

  public String getPreferredLanguage() {
    return preferredLanguage;
  }

  public void setPreferredLanguage(String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
  }

  public String getUserSMIMECertificate() {
    return userSMIMECertificate;
  }

  public void setUserSMIMECertificate(String userSMIMECertificate) {
    this.userSMIMECertificate = userSMIMECertificate;
  }

  public String getUserPKCS12() {
    return userPKCS12;
  }

  public void setUserPKCS12(String userPKCS12) {
    this.userPKCS12 = userPKCS12;
  }
}
