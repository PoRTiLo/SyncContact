package cz.xsendl00.synccontact.client;

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
  private String title;
  private String x121Address;
  private String registeredAddress;
  private String destinationIndicator;
  private String telephoneNumber;
  private String internationaliSDNNumber;
  private String facsimileTelephoneNumber;
  private String preferredDeliveryMethod;
  private String telexNumber;
  private String street;
  private String postOfficeBox;
  private String postalCode;
  private String postalAddress;
  private String physicalDeliveryOfficeName;
  private String ou;
  private String st;
  private String l;
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
  public String getTelephoneNumber() {
    return telephoneNumber;
  }
  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
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
  public String getStreet() {
    return street;
  }
  public void setStreet(String street) {
    this.street = street;
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
