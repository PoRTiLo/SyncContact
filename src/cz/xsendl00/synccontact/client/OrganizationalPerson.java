package cz.xsendl00.synccontact.client;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * The 'organizationalPerson' object class is the basis of an entry that
   represents a person in relation to an organization.
 * 
 * olcObjectClasses: {5}( 2.5.6.7 NAME 'organizationalPerson' DESC 'RFC2256: an o
 rganizational person' SUP person STRUCTURAL MAY ( title $ x121Address $ regis
 teredAddress $ destinationIndicator $ preferredDeliveryMethod $ telexNumber $
  teletexTerminalIdentifier $ telephoneNumber $ internationaliSDNNumber $ facs
 imileTelephoneNumber $ street $ postOfficeBox $ postalCode $ postalAddress $ 
 physicalDeliveryOfficeName $ ou $ st $ l ) )
 * 
 * @author portilo
 *
 */

public class OrganizationalPerson extends Person {

  //MAY
  private String title = Constants.TITLE;
  private String x121Address = Constants.X12_ADDRESS;
  private String registeredAddress = Constants.REGISTRED_ADDRESS;
  private String destinationIndicator = Constants.DESTINATION_INDICATOR;
  private String internationaliSDNNumber = Constants.INTERNATIONAL_SDN_NUMBER;
  private String facsimileTelephoneNumber = Constants.FASCIMILE_TELEPHONE_NUMBER;
  private String preferredDeliveryMethod = Constants.PREFERRED_DELIVERY_METHOD;
  
  private String telexNumber = Constants.TELEX_NUMBER;
  private String postOfficeBox = Constants.POST_OFFICE_BOX;
  private String postalCode = Constants.POSTAL_CODE;
  private String postalAddress = Constants.POSTAL_ADDRESS;
  private String physicalDeliveryOfficeName = Constants.PHYSICAL_DELIVERY_OFFICE_NAME;
  private String ou = Constants.OU;
  private String st = Constants.ST;
  private String l = Constants.L;
  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }
  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }
  public String getX121Address() {
    return x121Address;
  }
  public void setX121Address(String x121Address) {
    this.x121Address = x121Address;
  }
  public String getRegisteredAddress() {
    return registeredAddress;
  }
  public void setRegisteredAddress(String registeredAddress) {
    this.registeredAddress = registeredAddress;
  }
  public String getDestinationIndicator() {
    return destinationIndicator;
  }
  public void setDestinationIndicator(String destinationIndicator) {
    this.destinationIndicator = destinationIndicator;
  }
  public String getInternationaliSDNNumber() {
    return internationaliSDNNumber;
  }
  public void setInternationaliSDNNumber(String internationaliSDNNumber) {
    this.internationaliSDNNumber = internationaliSDNNumber;
  }
  public String getFacsimileTelephoneNumber() {
    return facsimileTelephoneNumber;
  }
  public void setFacsimileTelephoneNumber(String facsimileTelephoneNumber) {
    this.facsimileTelephoneNumber = facsimileTelephoneNumber;
  }
  public String getPreferredDeliveryMethod() {
    return preferredDeliveryMethod;
  }
  public void setPreferredDeliveryMethod(String preferredDeliveryMethod) {
    this.preferredDeliveryMethod = preferredDeliveryMethod;
  }
  public String getTelexNumber() {
    return telexNumber;
  }
  public void setTelexNumber(String telexNumber) {
    this.telexNumber = telexNumber;
  }
  public String getPostalCode() {
    return postalCode;
  }
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
  public String getPostOfficeBox() {
    return postOfficeBox;
  }
  public void setPostOfficeBox(String postOfficeBox) {
    this.postOfficeBox = postOfficeBox;
  }
  public String getPostalAddress() {
    return postalAddress;
  }
  public void setPostalAddress(String postalAddress) {
    this.postalAddress = postalAddress;
  }
  public String getPhysicalDeliveryOfficeName() {
    return physicalDeliveryOfficeName;
  }
  public void setPhysicalDeliveryOfficeName(String physicalDeliveryOfficeName) {
    this.physicalDeliveryOfficeName = physicalDeliveryOfficeName;
  }
  public String getOu() {
    return ou;
  }
  public void setOu(String ou) {
    this.ou = ou;
  }
  public String getSt() {
    return st;
  }
  public void setSt(String st) {
    this.st = st;
  }
  public String getL() {
    return l;
  }
  public void setL(String l) {
    this.l = l;
  }
}
