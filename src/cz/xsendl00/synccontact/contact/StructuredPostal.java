package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing a postal addresses.
 * @author portilo
 *
 */
public class StructuredPostal extends AbstractType implements ContactInterface {

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
  
  public static ContentValues compare(StructuredPostal obj1, StructuredPostal obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getHomeCity() != null) {
        values.put(Constants.HOME_CITY, obj2.getHomeCity());
      }
      if (obj2.getHomeCountry() != null) {
        values.put(Constants.HOME_COUNTRY, obj2.getHomeCountry());
      }
      if (obj2.getHomeFormattedAddress() != null) {
        values.put(Constants.HOME_FORMATTED_ADDRESS, obj2.getHomeFormattedAddress());
      }
      if (obj2.getHomeNeighborhood() != null) {
        values.put(Constants.HOME_NEIGHBORHOOD, obj2.getHomeNeighborhood());
      }
      if (obj2.getHomePOBox() != null) {
        values.put(Constants.HOME_POBOX, obj2.getHomePOBox());
      }
      if (obj2.getHomePostalCode() != null) {
        values.put(Constants.HOME_POSTAL_CODE, obj2.getHomePostalCode());
      }
      if (obj2.getHomeRegion() != null) {
        values.put(Constants.HOME_REGION, obj2.getHomeRegion());
      }
      if (obj2.getHomeStreet() != null) {
        values.put(Constants.HOME_STREET, obj2.getHomeStreet());
      }
      if (obj2.getWorkCity() != null) {
        values.put(Constants.WORK_CITY, obj2.getWorkCity());
      }
      if (obj2.getWorkCountry() != null) {
        values.put(Constants.WORK_COUNTRY, obj2.getWorkCountry());
      }
      if (obj2.getWorkFormattedAddress() != null) {
        values.put(Constants.WORK_FORMATTED_ADDRESS, obj2.getWorkFormattedAddress());
      }
      if (obj2.getWorkNeighborhood() != null) {
        values.put(Constants.WORK_NEIGHBORHOOD, obj2.getWorkNeighborhood());
      }
      if (obj2.getWorkPOBox() != null) {
        values.put(Constants.WORK_POBOX, obj2.getWorkPOBox());
      }
      if (obj2.getWorkPostalCode() != null) {
        values.put(Constants.WORK_POSTAL_CODE, obj2.getWorkPostalCode());
      }
      if (obj2.getWorkRegion() != null) {
        values.put(Constants.WORK_REGION, obj2.getWorkRegion());
      }
      if (obj2.getWorkStreet() != null) {
        values.put(Constants.WORK_STREET, obj2.getWorkStreet());
      }
      if (obj2.getOtherCity() != null) {
        values.put(Constants.OTHER_CITY, obj2.getOtherCity());
      }
      if (obj2.getOtherCountry() != null) {
        values.put(Constants.OTHER_COUNTRY, obj2.getOtherCountry());
      }
      if (obj2.getOtherFormattedAddress() != null) {
        values.put(Constants.OTHER_FORMATTED_ADDRESS, obj2.getOtherFormattedAddress());
      }
      if (obj2.getOtherNeighborhood() != null) {
        values.put(Constants.OTHER_NEIGHBORHOOD, obj2.getOtherNeighborhood());
      }
      if (obj2.getOtherPOBox() != null) {
        values.put(Constants.OTHER_POBOX, obj2.getOtherPOBox());
      }
      if (obj2.getOtherPostalCode() != null) {
        values.put(Constants.OTHER_POSTAL_CODE, obj2.getOtherPostalCode());
      }
      if (obj2.getOtherRegion() != null) {
        values.put(Constants.OTHER_REGION, obj2.getOtherRegion());
      }
      if (obj2.getOtherStreet() != null) {
        values.put(Constants.OTHER_STREET, obj2.getOtherStreet());
      }
    } else if (obj1 == null && obj2 == null) { // nothing
      
    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getHomeCity() != null) {
        values.putNull(Constants.HOME_CITY);
      }
      if (obj1.getHomeCountry() != null) {
        values.putNull(Constants.HOME_COUNTRY);
      }
      if (obj1.getHomeFormattedAddress() != null) {
        values.putNull(Constants.HOME_FORMATTED_ADDRESS);
      }
      if (obj1.getHomeNeighborhood() != null) {
        values.putNull(Constants.HOME_NEIGHBORHOOD);
      }
      if (obj1.getHomePOBox() != null) {
        values.putNull(Constants.HOME_POBOX);
      }
      if (obj1.getHomePostalCode() != null) {
        values.putNull(Constants.HOME_POSTAL_CODE);
      }
      if (obj1.getHomeRegion() != null) {
        values.putNull(Constants.HOME_REGION);
      }
      if (obj1.getHomeStreet() != null) {
        values.putNull(Constants.HOME_STREET);
      }
      if (obj1.getWorkCity() != null) {
        values.putNull(Constants.WORK_CITY);
      }
      if (obj1.getWorkCountry() != null) {
        values.putNull(Constants.WORK_COUNTRY);
      }
      if (obj1.getWorkFormattedAddress() != null) {
        values.putNull(Constants.WORK_FORMATTED_ADDRESS);
      }
      if (obj1.getWorkNeighborhood() != null) {
        values.putNull(Constants.WORK_NEIGHBORHOOD);
      }
      if (obj1.getWorkPOBox() != null) {
        values.putNull(Constants.WORK_POBOX);
      }
      if (obj1.getWorkPostalCode() != null) {
        values.putNull(Constants.WORK_POSTAL_CODE);
      }
      if (obj1.getWorkRegion() != null) {
        values.putNull(Constants.WORK_REGION);
      }
      if (obj1.getWorkStreet() != null) {
        values.putNull(Constants.WORK_STREET);
      }
      if (obj1.getOtherCity() != null) {
        values.putNull(Constants.OTHER_CITY);
      }
      if (obj1.getOtherCountry() != null) {
        values.putNull(Constants.OTHER_COUNTRY);
      }
      if (obj1.getOtherFormattedAddress() != null) {
        values.putNull(Constants.OTHER_FORMATTED_ADDRESS);
      }
      if (obj1.getOtherNeighborhood() != null) {
        values.putNull(Constants.OTHER_NEIGHBORHOOD);
      }
      if (obj1.getOtherPOBox() != null) {
        values.putNull(Constants.OTHER_POBOX);
      }
      if (obj1.getOtherPostalCode() != null) {
        values.putNull(Constants.OTHER_POSTAL_CODE);
      }
      if (obj1.getOtherRegion() != null) {
        values.putNull(Constants.OTHER_REGION);
      }
      if (obj1.getOtherStreet() != null) {
        values.putNull(Constants.OTHER_STREET);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getHomeCity() != null) {
        values.put(Constants.HOME_CITY, obj2.getHomeCity());
      } else {
        values.putNull(Constants.HOME_CITY);
      }
      if (obj2.getHomeCountry() != null) {
        values.put(Constants.HOME_COUNTRY, obj2.getHomeCountry());
      } else {
        values.putNull(Constants.HOME_COUNTRY);
      }
      if (obj2.getHomeFormattedAddress() != null) {
        values.put(Constants.HOME_FORMATTED_ADDRESS, obj2.getHomeFormattedAddress());
      } else {
        values.putNull(Constants.HOME_FORMATTED_ADDRESS);
      }
      if (obj2.getHomeNeighborhood() != null) {
        values.put(Constants.HOME_NEIGHBORHOOD, obj2.getHomeNeighborhood());
      } else {
        values.putNull(Constants.HOME_NEIGHBORHOOD);
      }
      if (obj2.getHomePOBox() != null) {
        values.put(Constants.HOME_POBOX, obj2.getHomePOBox());
      } else {
        values.putNull(Constants.HOME_POBOX);
      }
      if (obj2.getHomePostalCode() != null) {
        values.put(Constants.HOME_POSTAL_CODE, obj2.getHomePostalCode());
      } else {
        values.putNull(Constants.HOME_POSTAL_CODE);
      }
      if (obj2.getHomeRegion() != null) {
        values.put(Constants.HOME_REGION, obj2.getHomeRegion());
      } else {
        values.putNull(Constants.HOME_REGION);
      }
      if (obj2.getHomeStreet() != null) {
        values.put(Constants.HOME_STREET, obj2.getHomeStreet());
      } else {
        values.putNull(Constants.HOME_STREET);
      }
      if (obj2.getWorkCity() != null) {
        values.put(Constants.WORK_CITY, obj2.getWorkCity());
      } else {
        values.putNull(Constants.WORK_CITY);
      }
      if (obj2.getWorkCountry() != null) {
        values.put(Constants.WORK_COUNTRY, obj2.getWorkCountry());
      } else {
        values.putNull(Constants.WORK_COUNTRY);
      }
      if (obj2.getWorkFormattedAddress() != null) {
        values.put(Constants.WORK_FORMATTED_ADDRESS, obj2.getWorkFormattedAddress());
      } else {
        values.putNull(Constants.WORK_FORMATTED_ADDRESS);
      }
      if (obj2.getWorkNeighborhood() != null) {
        values.put(Constants.WORK_NEIGHBORHOOD, obj2.getWorkNeighborhood());
      } else {
        values.putNull(Constants.WORK_NEIGHBORHOOD);
      }
      if (obj2.getWorkPOBox() != null) {
        values.put(Constants.WORK_POBOX, obj2.getWorkPOBox());
      } else {
        values.putNull(Constants.WORK_POBOX);
      }
      if (obj2.getWorkPostalCode() != null) {
        values.put(Constants.WORK_POSTAL_CODE, obj2.getWorkPostalCode());
      } else {
        values.putNull(Constants.WORK_POSTAL_CODE);
      }
      if (obj2.getWorkRegion() != null) {
        values.put(Constants.WORK_REGION, obj2.getWorkRegion());
      } else {
        values.putNull(Constants.WORK_REGION);
      }
      if (obj2.getWorkStreet() != null) {
        values.put(Constants.WORK_STREET, obj2.getWorkStreet());
      } else {
        values.putNull(Constants.WORK_STREET);
      }
      if (obj2.getOtherCity() != null) {
        values.put(Constants.OTHER_CITY, obj2.getOtherCity());
      } else {
        values.putNull(Constants.OTHER_CITY);
      }
      if (obj2.getOtherCountry() != null) {
        values.put(Constants.OTHER_COUNTRY, obj2.getOtherCountry());
      } else {
        values.putNull(Constants.OTHER_COUNTRY);
      }
      if (obj2.getOtherFormattedAddress() != null) {
        values.put(Constants.OTHER_FORMATTED_ADDRESS, obj2.getOtherFormattedAddress());
      } else {
        values.putNull(Constants.OTHER_FORMATTED_ADDRESS);
      }
      if (obj2.getOtherNeighborhood() != null) {
        values.put(Constants.OTHER_NEIGHBORHOOD, obj2.getOtherNeighborhood());
      } else {
        values.putNull(Constants.OTHER_NEIGHBORHOOD);
      }
      if (obj2.getOtherPOBox() != null) {
        values.put(Constants.OTHER_POBOX, obj2.getOtherPOBox());
      } else {
        values.putNull(Constants.OTHER_POBOX);
      }
      if (obj2.getOtherPostalCode() != null) {
        values.put(Constants.OTHER_POSTAL_CODE, obj2.getOtherPostalCode());
      } else {
        values.putNull(Constants.OTHER_POSTAL_CODE);
      }
      if (obj2.getOtherRegion() != null) {
        values.put(Constants.OTHER_REGION, obj2.getOtherRegion());
      } else {
        values.putNull(Constants.OTHER_REGION);
      }
      if (obj2.getOtherStreet() != null) {
        values.put(Constants.OTHER_STREET, obj2.getOtherStreet());
      } else {
        values.putNull(Constants.OTHER_STREET);
      }
    }
    return values;
  }
}
