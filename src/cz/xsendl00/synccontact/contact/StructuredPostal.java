package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing a postal addresses.
 * @author portilo
 *
 */
public class StructuredPostal implements ContactInterface {

  private String homeStreet;
  private String homePOBox;
  private String homeCity;
  private String homeRegion;
  private String homePostalCode;
  private String homeCountry;
  private String workStreet;
  private String workPOBox;
  private String workCity;
  private String workRegion;
  private String workPostalCode;
  private String workCountry;
  private String workFormattedAddress;
  private String homeFormattedAddress;
  private String workNeighborhood;
  private String homeNeighborhood;
  private String otherNeighborhood;
  private String otherStreet;
  private String otherCity;
  private String otherPOBox;
  private String otherRegion;
  private String otherPostalCode;
  private String otherCountry;
  private String otherFormattedAddress;
  
  public String getHomeStreet() {
    return homeStreet;
  }
  public void setHomeStreet(String homeStreet) {
    this.homeStreet = homeStreet;
  }
  public String getHomePOBox() {
    return homePOBox;
  }
  public void setHomePOBox(String homePOBox) {
    this.homePOBox = homePOBox;
  }
  public String getHomeCity() {
    return homeCity;
  }
  public void setHomeCity(String homeCity) {
    this.homeCity = homeCity;
  }
  public String getHomeRegion() {
    return homeRegion;
  }
  public void setHomeRegion(String homeRegion) {
    this.homeRegion = homeRegion;
  }
  public String getHomePostalCode() {
    return homePostalCode;
  }
  public void setHomePostalCode(String homePostalCode) {
    this.homePostalCode = homePostalCode;
  }
  public String getHomeCountry() {
    return homeCountry;
  }
  public void setHomeCountry(String homeCountry) {
    this.homeCountry = homeCountry;
  }
  public String getWorkStreet() {
    return workStreet;
  }
  public void setWorkStreet(String workStreet) {
    this.workStreet = workStreet;
  }
  public String getWorkPOBox() {
    return workPOBox;
  }
  public void setWorkPOBox(String workPOBox) {
    this.workPOBox = workPOBox;
  }
  public String getWorkCity() {
    return workCity;
  }
  public void setWorkCity(String workCity) {
    this.workCity = workCity;
  }
  public String getWorkRegion() {
    return workRegion;
  }
  public void setWorkRegion(String workRegion) {
    this.workRegion = workRegion;
  }
  public String getWorkPostalCode() {
    return workPostalCode;
  }
  public void setWorkPostalCode(String workPostalCode) {
    this.workPostalCode = workPostalCode;
  }
  public String getWorkCountry() {
    return workCountry;
  }
  public void setWorkCountry(String workCountry) {
    this.workCountry = workCountry;
  }
  public String getWorkFormattedAddress() {
    return workFormattedAddress;
  }
  public void setWorkFormattedAddress(String workFormattedAddress) {
    this.workFormattedAddress = workFormattedAddress;
  }
  public String getHomeFormattedAddress() {
    return homeFormattedAddress;
  }
  public void setHomeFormattedAddress(String homeFormattedAddress) {
    this.homeFormattedAddress = homeFormattedAddress;
  }
  public String getWorkNeighborhood() {
    return workNeighborhood;
  }
  public void setWorkNeighborhood(String workNeighborhood) {
    this.workNeighborhood = workNeighborhood;
  }
  public String getHomeNeighborhood() {
    return homeNeighborhood;
  }
  public void setHomeNeighborhood(String homeNeighborhood) {
    this.homeNeighborhood = homeNeighborhood;
  }
  public String getOtherNeighborhood() {
    return otherNeighborhood;
  }
  public void setOtherNeighborhood(String otherNeighborhood) {
    this.otherNeighborhood = otherNeighborhood;
  }
  public String getOtherStreet() {
    return otherStreet;
  }
  public void setOtherStreet(String otherStreet) {
    this.otherStreet = otherStreet;
  }
  public String getOtherCity() {
    return otherCity;
  }
  public void setOtherCity(String otherCity) {
    this.otherCity = otherCity;
  }
  public String getOtherPOBox() {
    return otherPOBox;
  }
  public void setOtherPOBox(String otherPOBox) {
    this.otherPOBox = otherPOBox;
  }
  public String getOtherRegion() {
    return otherRegion;
  }
  public void setOtherRegion(String otherRegion) {
    this.otherRegion = otherRegion;
  }
  public String getOtherPostalCode() {
    return otherPostalCode;
  }
  public void setOtherPostalCode(String otherPostalCode) {
    this.otherPostalCode = otherPostalCode;
  }
  public String getOtherCountry() {
    return otherCountry;
  }
  public void setOtherCountry(String otherCountry) {
    this.otherCountry = otherCountry;
  }
  public String getOtherFormattedAddress() {
    return otherFormattedAddress;
  }
  public void setOtherFormattedAddress(String otherFormattedAddress) {
    this.otherFormattedAddress = otherFormattedAddress;
  }

  
  @Override
  public String toString() {
    return "StructuredPostal [homeStreet=" + homeStreet + ", homePOBox="
        + homePOBox + ", homeCity=" + homeCity + ", homeRegion=" + homeRegion
        + ", homePostalCode=" + homePostalCode + ", homeCountry=" + homeCountry
        + ", workStreet=" + workStreet + ", workPOBox=" + workPOBox
        + ", workCity=" + workCity + ", workRegion=" + workRegion
        + ", workPostalCode=" + workPostalCode + ", workCountry=" + workCountry
        + ", workFormattedAddress=" + workFormattedAddress
        + ", homeFormattedAddress=" + homeFormattedAddress
        + ", workNeighborhood=" + workNeighborhood + ", homeNeighborhood="
        + homeNeighborhood + ", otherNeighborhood=" + otherNeighborhood
        + ", otherStreet=" + otherStreet + ", otherCity=" + otherCity
        + ", otherPOBox=" + otherPOBox + ", otherRegion=" + otherRegion
        + ", otherPostalCode=" + otherPostalCode + ", otherCountry="
        + otherCountry + ", otherFormattedAddress=" + otherFormattedAddress
        + "]";
  }
  @Override
  public void defaultValue() {
    homeStreet = Constants.HOME_STREET;
    homePOBox = Constants.HOME_POBOX;
    homeCity = Constants.HOME_CITY;
    homeRegion = Constants.HOME_REGION;
    homePostalCode = Constants.HOME_POSTAL_CODE;
    homeCountry = Constants.HOME_COUNTRY;
    workStreet = Constants.WORK_STREET;
    workPOBox = Constants.WORK_POBOX;
    workCity = Constants.WORK_CITY;
    workRegion = Constants.WORK_REGION;
    workPostalCode = Constants.WORK_POSTAL_CODE;
    workCountry = Constants.WORK_COUNTRY;
    workFormattedAddress = Constants.WORK_FORMATTED_ADDRESS;
    homeFormattedAddress = Constants.HOME_FORMATTED_ADDRESS;
    workNeighborhood = Constants.WORK_NEIGHBORHOOD;
    homeNeighborhood = Constants.HOME_NEIGHBORHOOD;
    otherNeighborhood = Constants.OTHER_NEIGHBORHOOD;
    otherStreet = Constants.OTHER_STREET;
    otherCity = Constants.OTHER_CITY;
    otherPOBox = Constants.OTHER_POBOX;
    otherRegion = Constants.OTHER_REGION;
    otherPostalCode = Constants.OTHER_POSTAL_CODE;
    otherCountry = Constants.OTHER_COUNTRY;
    otherFormattedAddress = Constants.OTHER_FORMATTED_ADDRESS;
    
    
  }
}
