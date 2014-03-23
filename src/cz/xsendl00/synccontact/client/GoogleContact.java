package cz.xsendl00.synccontact.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cz.xsendl00.synccontact.utils.Constants;

public class GoogleContact extends InetOrgPerson {
  private String additionalName = Constants.ADDITIONAL_NAME;
  private String namePrefix = Constants.NAME_PREFIX;
  private String nameSuffix = Constants.NAME_SUFFIX;
  private String nickname = Constants.NICKNAME;
  private String shortName = Constants.SHORT_NAME;
  private String maidenName = Constants.MAIDEN_NAME;
  private String gender = Constants.GENDER;
  private String notes = Constants.NOTES;
  private String homeMail = Constants.HOME_MAIL;
  private String workMail = Constants.WORK_MAIL;
  private String workPhone = Constants.WORK_PHONE;
  private String website = Constants.WEBSITE;
  
  
  private LinkedHashMap<AddressType, Address> address;
  
  
  
  public static String FIRSTNAME = "FIRSTNAME";
  public static String LASTNAME = "LASTNAME";
  public static String TELEPHONE = "TELEPHONE";
  public static String MOBILE = "MOBILE";
  public static String HOMEPHONE = "HOMEPHONE";
  public static String MAIL = "MAIL";
  public static String PHOTO = "PHOTO";
  public static String STREET = "STREET";
  public static String CITY = "CITY";
  public static String STATE = "STATE";
  public static String ZIP = "ZIP";
  public static String COUNTRY = "COUNTRY";

  public void init() {
    address = new LinkedHashMap<AddressType, Address>();
    Address addressHome = new Address();
    addressHome.setCity(Constants.HOME_CITY);
    addressHome.setType(AddressType.HOME);
    addressHome.setCountry(Constants.HOME_COUNTRY);
    addressHome.setExtendedAddress(Constants.HOME_EXTENDED_ADDRESS);
    addressHome.setPobox(Constants.HOME_POBOX);
    addressHome.setRegion(Constants.HOME_REGION);
    addressHome.setStreet(Constants.HOME_STREET);
    addressHome.setZip(Constants.HOME_POSTAL_CODE);
    address.put(AddressType.HOME, addressHome);
    
    Address addressWork = new Address();
    addressWork.setCity(Constants.WORK_CITY);
    addressWork.setType(AddressType.WORK);
    addressWork.setCountry(Constants.WORK_COUNTRY);
    addressWork.setExtendedAddress(Constants.WORK_EXTENDED_ADDRESS);
    addressWork.setPobox(Constants.WORK_POBOX);
    addressWork.setRegion(Constants.WORK_REGION);
    addressWork.setStreet(Constants.WORK_STREET);
    addressWork.setZip(Constants.WORK_POSTAL_CODE);
    address.put(AddressType.WORK, addressWork);
    
    Address addressPostal = new Address();
    addressWork.setType(AddressType.POSTAL);
    addressWork.setPobox(Constants.POST_OFFICE_BOX);
    addressWork.setZip(Constants.POSTAL_CODE);
    address.put(AddressType.POSTAL, addressPostal);
  }
  

  /**
   * Creates and returns an instance of the user from the provided LDAP data.
   * 
   * @param user
   *            The LDAPObject containing user data
   * @param mB
   *            Mapping bundle for the LDAP attribute names.
   * @return user The new instance of LDAP user created from the LDAP data.
   */
  /*
  public static Contact valueOf(ReadOnlyEntry user, Bundle mB) {
    Contact c = new Contact();
    try {
      c.setDn(user.getDN());
      c.setFirstName(user.hasAttribute(mB.getString(FIRSTNAME)) ? user.getAttributeValue(mB.getString(FIRSTNAME)) : null);
      c.setLastName(user.hasAttribute(mB.getString(LASTNAME)) ? user.getAttributeValue(mB.getString(LASTNAME)) : null);
      if ((user.hasAttribute(mB.getString(FIRSTNAME)) ? user.getAttributeValue(mB.getString(FIRSTNAME)) : null) == null
          || (user.hasAttribute(mB.getString(LASTNAME)) ? user.getAttributeValue(mB.getString(LASTNAME)) : null) == null) {
        return null;
      }
      c.setWorkPhone(user.hasAttribute(mB.getString(TELEPHONE)) ? user.getAttributeValue(mB.getString(TELEPHONE)) : null);
      c.setCellWorkPhone(user.hasAttribute(mB.getString(MOBILE)) ? user.getAttributeValue(mB.getString(MOBILE)) : null);
      c.setHomePhone(user.hasAttribute(mB.getString(HOMEPHONE)) ? user.getAttributeValue(mB.getString(HOMEPHONE)) : null);
      c.setEmails(user.hasAttribute(mB.getString(MAIL)) ? user.getAttributeValues(mB.getString(MAIL)) : null);
      byte[] image = null;
      if (user.hasAttribute(mB.getString(PHOTO))) {
        byte[] array = user.getAttributeValueBytes(mB.getString(PHOTO));

        try {
          Bitmap myBitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
          if (myBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            image = baos.toByteArray();
          }
        } catch (OutOfMemoryError e) {
          // Do not set an image, when an OutOfMemoryError occurs
          image = null;
          array = null;
        }
      }
      c.setImage(image);

      // Get address
      if (user.hasAttribute(mB.getString(STREET)) || user.hasAttribute(mB.getString(CITY)) || user.hasAttribute(mB.getString(STATE))
          || user.hasAttribute(mB.getString(ZIP)) || user.hasAttribute(mB.getString(COUNTRY))) {
        Address a = new Address();
        a.setStreet(user.hasAttribute(mB.getString(STREET)) ? user.getAttributeValue(mB.getString(STREET)) : null);
        a.setCity(user.hasAttribute(mB.getString(CITY)) ? user.getAttributeValue(mB.getString(CITY)) : null);
        a.setState(user.hasAttribute(mB.getString(STATE)) ? user.getAttributeValue(mB.getString(STATE)) : null);
        a.setZip(user.hasAttribute(mB.getString(ZIP)) ? user.getAttributeValue(mB.getString(ZIP)) : null);
        a.setCountry(user.hasAttribute(mB.getString(COUNTRY)) ? user.getAttributeValue(mB.getString(COUNTRY)) : null);
        c.setAddress(a);
      }
    } catch (final Exception ex) {
      Log.i("User", "Error parsing LDAP user object" + ex.toString());
    }
    return c;
  }
*/

  public String getAdditionalName() {
    return additionalName;
  }


  public void setAdditionalName(String additionalName) {
    this.additionalName = additionalName;
  }


  public String getNameSuffix() {
    return nameSuffix;
  }


  public void setNameSuffix(String nameSuffix) {
    this.nameSuffix = nameSuffix;
  }


  public String getNickname() {
    return nickname;
  }


  public void setNickname(String nickname) {
    this.nickname = nickname;
  }


  public String getShortName() {
    return shortName;
  }


  public void setShortName(String shortName) {
    this.shortName = shortName;
  }


  public String getMaidenName() {
    return maidenName;
  }


  public void setMaidenName(String maidenName) {
    this.maidenName = maidenName;
  }


  public String getNamePrefix() {
    return namePrefix;
  }


  public void setNamePrefix(String namePrefix) {
    this.namePrefix = namePrefix;
  }


  public String getGender() {
    return gender;
  }


  public void setGender(String gender) {
    this.gender = gender;
  }


  public String getNotes() {
    return notes;
  }


  public void setNotes(String notes) {
    this.notes = notes;
  }


  public String getHomeMail() {
    return homeMail;
  }


  public void setHomeMail(String homeMail) {
    this.homeMail = homeMail;
  }


  public String getWorkMail() {
    return workMail;
  }


  public void setWorkMail(String workMail) {
    this.workMail = workMail;
  }


  public String getWorkPhone() {
    return workPhone;
  }


  public void setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
  }


  public String getWebsite() {
    return website;
  }


  public void setWebsite(String website) {
    this.website = website;
  }
  
  public LinkedHashMap<AddressType, Address> getAddress() {
    return this.address;
  }
}
