package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Data;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing a postal addresses.
 *
 * @author xsendl00
 */
public class StructuredPostalSync extends AbstractType implements ContactInterface {

  @Override
  public String toString() {
    return "StructuredPostalSync [homeStreet=" + homeStreet + ", homePOBox=" + homePOBox
        + ", homeCity=" + homeCity + ", homeRegion=" + homeRegion + ", homePostalCode="
        + homePostalCode + ", homeCountry=" + homeCountry + ", workStreet=" + workStreet
        + ", workPOBox=" + workPOBox + ", workCity=" + workCity + ", workRegion=" + workRegion
        + ", workPostalCode=" + workPostalCode + ", workCountry=" + workCountry
        + ", workFormattedAddress=" + workFormattedAddress + ", homeFormattedAddress="
        + homeFormattedAddress + ", workNeighborhood=" + workNeighborhood + ", homeNeighborhood="
        + homeNeighborhood + ", otherNeighborhood=" + otherNeighborhood + ", otherStreet="
        + otherStreet + ", otherCity=" + otherCity + ", otherPOBox=" + otherPOBox
        + ", otherRegion=" + otherRegion + ", otherPostalCode=" + otherPostalCode
        + ", otherCountry=" + otherCountry + ", otherFormattedAddress=" + otherFormattedAddress
        + "]";
  }

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

  public static ContentValues compare(StructuredPostalSync obj1, StructuredPostalSync obj2) {
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

  public static ContentProviderOperation add(int id,
      Map<String, String> values,
      int type,
      boolean create) {
    ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
    if (create) {
      operationBuilder.withValueBackReference(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE)
          .withValue(StructuredPostal.TYPE, type);
    } else {
      operationBuilder.withValue(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE)
          .withValue(StructuredPostal.TYPE, type);
    }


    Iterator<String> iter = values.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      String val = values.get(key);
      operationBuilder.withValue(key, val);
    }
    return operationBuilder.build();
  }

  public static ContentProviderOperation update(String id, Map<String, String> values) {
    ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(Data.CONTENT_URI);
    operationBuilder.withSelection(Data._ID + "=?", new String[]{id}).withValue(Data.MIMETYPE,
        StructuredPostal.CONTENT_ITEM_TYPE);

    Iterator<String> iter = values.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      String val = values.get(key);
      operationBuilder.withValue(key, val);
    }
    return operationBuilder.build();
  }


  public static ArrayList<ContentProviderOperation> operation(int id,
      StructuredPostalSync em1,
      StructuredPostalSync em2,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      Map<String, String> value = op(em2.getHomeStreet(), em2.getHomePOBox(), em2.getHomeCity(),
          em2.getHomeRegion(), em2.getHomePostalCode(), em2.getHomeCountry(),
          em2.getHomeFormattedAddress());

      if (value.size() > 0) {
        ops.add(add(id, value, StructuredPostal.TYPE_HOME, create));
      }

      value = op(em2.getWorkStreet(), em2.getWorkPOBox(), em2.getWorkCity(), em2.getWorkRegion(),
          em2.getWorkPostalCode(), em2.getWorkCountry(), em2.getWorkFormattedAddress());

      if (value.size() > 0) {
        ops.add(add(id, value, StructuredPostal.TYPE_WORK, create));
      }

      value = op(em2.getOtherStreet(), em2.getOtherPOBox(), em2.getOtherCity(),
          em2.getOtherRegion(), em2.getOtherPostalCode(), em2.getOtherCountry(),
          em2.getOtherFormattedAddress());

      if (value.size() > 0) {
        ops.add(add(id, value, StructuredPostal.TYPE_OTHER, create));
      }

    } else if (em1 == null && em2 == null) { // nothing

    } else if (em1 != null && em2 == null) { // clear or update data in db
      if (em1.getHomeCity() != null || em1.getHomeCountry() != null
          || em1.getHomeFormattedAddress() != null || em1.getHomeNeighborhood() != null
          || em1.getHomePOBox() != null || em1.getHomePostalCode() != null
          || em1.getHomeRegion() != null || em1.getHomeStreet() != null) {

        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(StructuredPostal.TYPE_HOME), null)));
      }

      if (em1.getWorkCity() != null || em1.getWorkCountry() != null
          || em1.getWorkFormattedAddress() != null || em1.getWorkNeighborhood() != null
          || em1.getWorkPOBox() != null || em1.getWorkPostalCode() != null
          || em1.getWorkRegion() != null || em1.getWorkStreet() != null) {

        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(StructuredPostal.TYPE_WORK), null)));
      }

      if (em1.getOtherCity() != null || em1.getOtherCountry() != null
          || em1.getOtherFormattedAddress() != null || em1.getOtherNeighborhood() != null
          || em1.getOtherPOBox() != null || em1.getOtherPostalCode() != null
          || em1.getOtherRegion() != null || em1.getOtherStreet() != null) {

        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(StructuredPostal.TYPE_OTHER), null)));
      }
    } else if (em1 != null && em2 != null) { // merge
      Map<String, String> value2 = op(em2.getHomeStreet(), em2.getHomePOBox(), em2.getHomeCity(),
          em2.getHomeRegion(), em2.getHomePostalCode(), em2.getHomeCountry(),
          em2.getHomeFormattedAddress());
      Map<String, String> value1 = op(em1.getHomeStreet(), em1.getHomePOBox(), em1.getHomeCity(),
          em1.getHomeRegion(), em1.getHomePostalCode(), em1.getHomeCountry(),
          em1.getHomeFormattedAddress());
      if (value1 != null && (value1.size() > 0) && (value2 == null || !(value2.size() > 0))) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(StructuredPostal.TYPE_HOME), null)));
      } else if ((value1 == null || !(value1.size() > 0)) && value2 != null && (value2.size() > 0)) {
        ops.add(add(id, value2, StructuredPostal.TYPE_HOME, create));
      } else if (value1 != null && (value1.size() > 0) && value2 != null && (value2.size() > 0)) {
        ops.add(update(
            ID.getIdByValue(em1.getID(), String.valueOf(StructuredPostal.TYPE_HOME), null), value2));
      }

      value2 = op(em2.getWorkStreet(), em2.getWorkPOBox(), em2.getWorkCity(), em2.getWorkRegion(),
          em2.getWorkPostalCode(), em2.getWorkCountry(), em2.getWorkFormattedAddress());
      value1 = op(em1.getWorkStreet(), em1.getWorkPOBox(), em1.getWorkCity(), em1.getWorkRegion(),
          em1.getWorkPostalCode(), em1.getWorkCountry(), em1.getHomeFormattedAddress());
      if (value1 != null && (value1.size() > 0) && (value2 == null || !(value2.size() > 0))) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(StructuredPostal.TYPE_WORK), null)));
      } else if ((value1 == null || !(value1.size() > 0)) && value2 != null && (value2.size() > 0)) {
        ops.add(add(id, value2, StructuredPostal.TYPE_WORK, create));
      } else if (value1 != null && (value1.size() > 0) && value2 != null && (value2.size() > 0)) {
        ops.add(update(
            ID.getIdByValue(em1.getID(), String.valueOf(StructuredPostal.TYPE_WORK), null), value2));
      }

      value2 = op(em2.getOtherStreet(), em2.getOtherPOBox(), em2.getOtherCity(),
          em2.getOtherRegion(), em2.getOtherPostalCode(), em2.getOtherCountry(),
          em2.getOtherFormattedAddress());
      value1 = op(em1.getOtherStreet(), em1.getOtherPOBox(), em1.getOtherCity(),
          em1.getOtherRegion(), em1.getOtherPostalCode(), em1.getOtherCountry(),
          em1.getHomeFormattedAddress());
      if (value1 != null && (value1.size() > 0) && (value2 == null || !(value2.size() > 0))) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(StructuredPostal.TYPE_OTHER), null)));
      } else if ((value1 == null || !(value1.size() > 0)) && value2 != null && (value2.size() > 0)) {
        ops.add(add(id, value2, StructuredPostal.TYPE_OTHER, create));
      } else if (value1 != null && (value1.size() > 0) && value2 != null && (value2.size() > 0)) {
        ops.add(update(
            ID.getIdByValue(em1.getID(), String.valueOf(StructuredPostal.TYPE_OTHER), null), value2));
      }
    }
    return ops.size() > 0 ? ops : null;
  }

  private static Map<String, String> op(String street,
      String pOBox,
      String city,
      String region,
      String postalCode,
      String country,
      String formattedAddress) {
    Map<String, String> value = new HashMap<String, String>();
    if (street != null) {
      value.put(StructuredPostal.STREET, street);
    }
    if (pOBox != null) {
      value.put(StructuredPostal.POBOX, pOBox);
    }
    if (city != null) {
      value.put(StructuredPostal.CITY, city);
    }
    if (region != null) {
      value.put(StructuredPostal.REGION, region);
    }
    if (postalCode != null) {
      value.put(StructuredPostal.POSTCODE, postalCode);
    }
    if (country != null) {
      value.put(StructuredPostal.COUNTRY, country);
    }
    if (formattedAddress != null) {
      value.put(StructuredPostal.FORMATTED_ADDRESS, formattedAddress);
    }
    return value;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((homeCity == null) ? 0 : homeCity.hashCode());
    result = prime * result + ((homeCountry == null) ? 0 : homeCountry.hashCode());
    result = prime * result
        + ((homeFormattedAddress == null) ? 0 : homeFormattedAddress.hashCode());
    result = prime * result + ((homeNeighborhood == null) ? 0 : homeNeighborhood.hashCode());
    result = prime * result + ((homePOBox == null) ? 0 : homePOBox.hashCode());
    result = prime * result + ((homePostalCode == null) ? 0 : homePostalCode.hashCode());
    result = prime * result + ((homeRegion == null) ? 0 : homeRegion.hashCode());
    result = prime * result + ((homeStreet == null) ? 0 : homeStreet.hashCode());
    result = prime * result + ((otherCity == null) ? 0 : otherCity.hashCode());
    result = prime * result + ((otherCountry == null) ? 0 : otherCountry.hashCode());
    result = prime * result
        + ((otherFormattedAddress == null) ? 0 : otherFormattedAddress.hashCode());
    result = prime * result + ((otherNeighborhood == null) ? 0 : otherNeighborhood.hashCode());
    result = prime * result + ((otherPOBox == null) ? 0 : otherPOBox.hashCode());
    result = prime * result + ((otherPostalCode == null) ? 0 : otherPostalCode.hashCode());
    result = prime * result + ((otherRegion == null) ? 0 : otherRegion.hashCode());
    result = prime * result + ((otherStreet == null) ? 0 : otherStreet.hashCode());
    result = prime * result + ((workCity == null) ? 0 : workCity.hashCode());
    result = prime * result + ((workCountry == null) ? 0 : workCountry.hashCode());
    result = prime * result
        + ((workFormattedAddress == null) ? 0 : workFormattedAddress.hashCode());
    result = prime * result + ((workNeighborhood == null) ? 0 : workNeighborhood.hashCode());
    result = prime * result + ((workPOBox == null) ? 0 : workPOBox.hashCode());
    result = prime * result + ((workPostalCode == null) ? 0 : workPostalCode.hashCode());
    result = prime * result + ((workRegion == null) ? 0 : workRegion.hashCode());
    result = prime * result + ((workStreet == null) ? 0 : workStreet.hashCode());
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    StructuredPostalSync other = (StructuredPostalSync) obj;
    if (homeCity == null) {
      if (other.homeCity != null) {
        return false;
      }
    } else if (!homeCity.equals(other.homeCity)) {
      return false;
    }
    if (homeCountry == null) {
      if (other.homeCountry != null) {
        return false;
      }
    } else if (!homeCountry.equals(other.homeCountry)) {
      return false;
    }
    if (homeFormattedAddress == null) {
      if (other.homeFormattedAddress != null) {
        return false;
      }
    } else if (!homeFormattedAddress.equals(other.homeFormattedAddress)) {
      return false;
    }
    if (homeNeighborhood == null) {
      if (other.homeNeighborhood != null) {
        return false;
      }
    } else if (!homeNeighborhood.equals(other.homeNeighborhood)) {
      return false;
    }
    if (homePOBox == null) {
      if (other.homePOBox != null) {
        return false;
      }
    } else if (!homePOBox.equals(other.homePOBox)) {
      return false;
    }
    if (homePostalCode == null) {
      if (other.homePostalCode != null) {
        return false;
      }
    } else if (!homePostalCode.equals(other.homePostalCode)) {
      return false;
    }
    if (homeRegion == null) {
      if (other.homeRegion != null) {
        return false;
      }
    } else if (!homeRegion.equals(other.homeRegion)) {
      return false;
    }
    if (homeStreet == null) {
      if (other.homeStreet != null) {
        return false;
      }
    } else if (!homeStreet.equals(other.homeStreet)) {
      return false;
    }
    if (otherCity == null) {
      if (other.otherCity != null) {
        return false;
      }
    } else if (!otherCity.equals(other.otherCity)) {
      return false;
    }
    if (otherCountry == null) {
      if (other.otherCountry != null) {
        return false;
      }
    } else if (!otherCountry.equals(other.otherCountry)) {
      return false;
    }
    if (otherFormattedAddress == null) {
      if (other.otherFormattedAddress != null) {
        return false;
      }
    } else if (!otherFormattedAddress.equals(other.otherFormattedAddress)) {
      return false;
    }
    if (otherNeighborhood == null) {
      if (other.otherNeighborhood != null) {
        return false;
      }
    } else if (!otherNeighborhood.equals(other.otherNeighborhood)) {
      return false;
    }
    if (otherPOBox == null) {
      if (other.otherPOBox != null) {
        return false;
      }
    } else if (!otherPOBox.equals(other.otherPOBox)) {
      return false;
    }
    if (otherPostalCode == null) {
      if (other.otherPostalCode != null) {
        return false;
      }
    } else if (!otherPostalCode.equals(other.otherPostalCode)) {
      return false;
    }
    if (otherRegion == null) {
      if (other.otherRegion != null) {
        return false;
      }
    } else if (!otherRegion.equals(other.otherRegion)) {
      return false;
    }
    if (otherStreet == null) {
      if (other.otherStreet != null) {
        return false;
      }
    } else if (!otherStreet.equals(other.otherStreet)) {
      return false;
    }
    if (workCity == null) {
      if (other.workCity != null) {
        return false;
      }
    } else if (!workCity.equals(other.workCity)) {
      return false;
    }
    if (workCountry == null) {
      if (other.workCountry != null) {
        return false;
      }
    } else if (!workCountry.equals(other.workCountry)) {
      return false;
    }
    if (workFormattedAddress == null) {
      if (other.workFormattedAddress != null) {
        return false;
      }
    } else if (!workFormattedAddress.equals(other.workFormattedAddress)) {
      return false;
    }
    if (workNeighborhood == null) {
      if (other.workNeighborhood != null) {
        return false;
      }
    } else if (!workNeighborhood.equals(other.workNeighborhood)) {
      return false;
    }
    if (workPOBox == null) {
      if (other.workPOBox != null) {
        return false;
      }
    } else if (!workPOBox.equals(other.workPOBox)) {
      return false;
    }
    if (workPostalCode == null) {
      if (other.workPostalCode != null) {
        return false;
      }
    } else if (!workPostalCode.equals(other.workPostalCode)) {
      return false;
    }
    if (workRegion == null) {
      if (other.workRegion != null) {
        return false;
      }
    } else if (!workRegion.equals(other.workRegion)) {
      return false;
    }
    if (workStreet == null) {
      if (other.workStreet != null) {
        return false;
      }
    } else if (!workStreet.equals(other.workStreet)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean isNull() {
    return homeStreet == null && homePOBox == null && homeCity == null && homeRegion == null
        && homePostalCode == null && homeCountry == null && workStreet == null && workPOBox == null
        && workCity == null && workRegion == null && workPostalCode == null && workCountry == null
        && workFormattedAddress == null && homeFormattedAddress == null && workNeighborhood == null
        && homeNeighborhood == null && otherNeighborhood == null && otherStreet == null
        && otherCity == null && otherPOBox == null && otherRegion == null && otherPostalCode == null
        && otherCountry == null && otherFormattedAddress == null;
  }


}
