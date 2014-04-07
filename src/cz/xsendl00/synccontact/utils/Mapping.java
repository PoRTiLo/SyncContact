package cz.xsendl00.synccontact.utils;

import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldif.LDIFException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.Address;
import cz.xsendl00.synccontact.client.AddressType;
import cz.xsendl00.synccontact.client.GoogleContact;

public class Mapping {

  private static final String TAG = "Mapping";
  
  public static Bundle mapingTo(GoogleContact gcMapping, AccountData accountData) {
    Bundle userData = new Bundle();
    userData.putString(Constants.PAR_USERNAME, accountData.getName());
    userData.putString(Constants.PAR_PORT, accountData.getPort().toString());
    userData.putString(Constants.PAR_HOST, accountData.getHost());
    userData.putString(Constants.PAR_ENCRYPTION, accountData.getEncryption().toString());
    userData.putString(Constants.PAR_SEARCHFILTER, accountData.getSearchFilter());
    userData.putString(Constants.PAR_BASEDN, accountData.getBaseDn());
    // Mappings for LDAP data
    // person 
    userData.putString(Constants.PAR_MAPPING + Constants.CN, gcMapping.getCn());
    userData.putString(Constants.PAR_MAPPING + Constants.SN, gcMapping.getSn());
    userData.putString(Constants.PAR_MAPPING + Constants.USER_PASSWORD, gcMapping.getUserPassword());
    userData.putString(Constants.PAR_MAPPING + Constants.TELEPHONE_NUMBER, gcMapping.getTelephoneNumber());
    userData.putString(Constants.PAR_MAPPING + Constants.SEE_ALSO, gcMapping.getSeeAlso());
    userData.putString(Constants.PAR_MAPPING + Constants.DESCRIPTION, gcMapping.getDescription());
    
    // OrganizationalPerson
    userData.putString(Constants.PAR_MAPPING + Constants.TITLE, gcMapping.getTitle());
    userData.putString(Constants.PAR_MAPPING + Constants.X12_ADDRESS, gcMapping.getX121Address());
    userData.putString(Constants.PAR_MAPPING + Constants.REGISTRED_ADDRESS, gcMapping.getRegisteredAddress());
    userData.putString(Constants.PAR_MAPPING + Constants.DESTINATION_INDICATOR, gcMapping.getDestinationIndicator());
    userData.putString(Constants.PAR_MAPPING + Constants.INTERNATIONAL_SDN_NUMBER, gcMapping.getInternationaliSDNNumber());
    userData.putString(Constants.PAR_MAPPING + Constants.FASCIMILE_TELEPHONE_NUMBER, gcMapping.getFacsimileTelephoneNumber());
    userData.putString(Constants.PAR_MAPPING + Constants.PREFERRED_DELIVERY_METHOD, gcMapping.getPreferredDeliveryMethod());
    userData.putString(Constants.PAR_MAPPING + Constants.TELEX_NUMBER, gcMapping.getTelexNumber());
    userData.putString(Constants.PAR_MAPPING + Constants.PHYSICAL_DELIVERY_OFFICE_NAME, gcMapping.getPhysicalDeliveryOfficeName());
    userData.putString(Constants.PAR_MAPPING + Constants.OU, gcMapping.getOu());
    userData.putString(Constants.PAR_MAPPING + Constants.ST, gcMapping.getSt());
    userData.putString(Constants.PAR_MAPPING + Constants.L, gcMapping.getL());
    
    // InetOrgPerson
    userData.putString(Constants.PAR_MAPPING + Constants.AUDIO, gcMapping.getAudio());
    userData.putString(Constants.PAR_MAPPING + Constants.BUSSINES_CATEGORY, gcMapping.getBusinessCategory());
    userData.putString(Constants.PAR_MAPPING + Constants.CAR_LICENCE, gcMapping.getCarLicense());
    userData.putString(Constants.PAR_MAPPING + Constants.DEPARTMENT_NUMBER, gcMapping.getDepartmentNumber());
    userData.putString(Constants.PAR_MAPPING + Constants.DISPLAY_NAME, gcMapping.getDisplayName());
    userData.putString(Constants.PAR_MAPPING + Constants.EMPLOYEE_NUMBER, gcMapping.getEmployeeNumber());
    userData.putString(Constants.PAR_MAPPING + Constants.EMPLOYEE_TYPE, gcMapping.getEmployeeType());
    userData.putString(Constants.PAR_MAPPING + Constants.GIVEN_NAME, gcMapping.getGivenName());
    userData.putString(Constants.PAR_MAPPING + Constants.HOME_PHONE, gcMapping.getHomePhone());
    userData.putString(Constants.PAR_MAPPING + Constants.HOME_POSTAL_ADDRESS, gcMapping.getHomePostalAddress());
    userData.putString(Constants.PAR_MAPPING + Constants.INITIALS, gcMapping.getInitials());
    userData.putString(Constants.PAR_MAPPING + Constants.JPEG_PHOTO, gcMapping.getJpegPhoto());
    userData.putString(Constants.PAR_MAPPING + Constants.LABELED_URI, gcMapping.getLabeledURI());
    userData.putString(Constants.PAR_MAPPING + Constants.MAIL, gcMapping.getMail());
    userData.putString(Constants.PAR_MAPPING + Constants.MANAGER, gcMapping.getManager());
    userData.putString(Constants.PAR_MAPPING + Constants.MOBILE, gcMapping.getMobile());
    userData.putString(Constants.PAR_MAPPING + Constants.O, gcMapping.getO());
    userData.putString(Constants.PAR_MAPPING + Constants.PAGER, gcMapping.getPager());
    userData.putString(Constants.PAR_MAPPING + Constants.PHOTO, gcMapping.getPhoto());
    userData.putString(Constants.PAR_MAPPING + Constants.ROOM_NUMBER, gcMapping.getRoomNumber());
    userData.putString(Constants.PAR_MAPPING + Constants.SECRETARY, gcMapping.getSecretary());
    userData.putString(Constants.PAR_MAPPING + Constants.UID, gcMapping.getUid());
    userData.putString(Constants.PAR_MAPPING + Constants.USER_CERTIFICATE, gcMapping.getUserCertificate());
    userData.putString(Constants.PAR_MAPPING + Constants.X500_UNIQUE_IDENTIFIER, gcMapping.getX500uniqueIdentifier());
    userData.putString(Constants.PAR_MAPPING + Constants.PREFERRED_LANGUAGE, gcMapping.getPreferredLanguage());
    userData.putString(Constants.PAR_MAPPING + Constants.USER_SMIME_CERTIFICATE, gcMapping.getUserSMIMECertificate());
    userData.putString(Constants.PAR_MAPPING + Constants.USER_PKCS12, gcMapping.getUserPKCS12());
    userData.putString(Constants.PAR_MAPPING + Constants.GIVEN_NAME, gcMapping.getGivenName());
    
    //GoogleContatc
    userData.putString(Constants.PAR_MAPPING + Constants.ADDITIONAL_NAME, gcMapping.getAdditionalName());
    userData.putString(Constants.PAR_MAPPING + Constants.NAME_PREFIX, gcMapping.getNamePrefix());
    userData.putString(Constants.PAR_MAPPING + Constants.NAME_SUFFIX, gcMapping.getNameSuffix());
    userData.putString(Constants.PAR_MAPPING + Constants.NICKNAME, gcMapping.getNickname());
    userData.putString(Constants.PAR_MAPPING + Constants.SHORT_NAME, gcMapping.getShortName());
    userData.putString(Constants.PAR_MAPPING + Constants.MAIDEN_NAME, gcMapping.getMaidenName());
    userData.putString(Constants.PAR_MAPPING + Constants.GENDER, gcMapping.getGender());
    userData.putString(Constants.PAR_MAPPING + Constants.NOTES, gcMapping.getNotes());
    userData.putString(Constants.PAR_MAPPING + Constants.HOME_MAIL, gcMapping.getHomeMail());
    userData.putString(Constants.PAR_MAPPING + Constants.WORK_MAIL, gcMapping.getWorkMail());
    userData.putString(Constants.PAR_MAPPING + Constants.WORK_PHONE, gcMapping.getWorkPhone());
    userData.putString(Constants.PAR_MAPPING + Constants.WEBSITE, gcMapping.getWebsite());
    //GoogleContact - Home address
    Address home = gcMapping.getAddress().get(AddressType.HOME);
    userData.putString(Constants.PAR_MAPPING + Constants.HOME_CITY, home.getCity());
    userData.putString(Constants.PAR_MAPPING + Constants.HOME_COUNTRY, home.getCountry());
    userData.putString(Constants.PAR_MAPPING + Constants.HOME_EXTENDED_ADDRESS, home.getExtendedAddress());
    userData.putString(Constants.PAR_MAPPING + Constants.HOME_POBOX, home.getPobox());
    userData.putString(Constants.PAR_MAPPING + Constants.HOME_REGION, home.getRegion());
    userData.putString(Constants.PAR_MAPPING + Constants.HOME_STREET, home.getStreet());
    userData.putString(Constants.PAR_MAPPING + Constants.HOME_POSTAL_CODE, home.getZip());
    //GoogleContact - Work address
    Address work = gcMapping.getAddress().get(AddressType.WORK);
    userData.putString(Constants.PAR_MAPPING + Constants.WORK_CITY, work.getCity());
    userData.putString(Constants.PAR_MAPPING + Constants.WORK_COUNTRY, work.getCountry());
    userData.putString(Constants.PAR_MAPPING + Constants.WORK_EXTENDED_ADDRESS, work.getExtendedAddress());
    userData.putString(Constants.PAR_MAPPING + Constants.WORK_POBOX, work.getPobox());
    userData.putString(Constants.PAR_MAPPING + Constants.WORK_REGION, work.getRegion());
    userData.putString(Constants.PAR_MAPPING + Constants.WORK_STREET, work.getStreet());
    userData.putString(Constants.PAR_MAPPING + Constants.WORK_POSTAL_CODE, work.getZip());
    //GoogleContact - postal address
    Address postal = gcMapping.getAddress().get(AddressType.POSTAL);
    userData.putString(Constants.PAR_MAPPING + Constants.POST_OFFICE_BOX, gcMapping.getPostOfficeBox());
    userData.putString(Constants.PAR_MAPPING + Constants.POSTAL_CODE, postal.getZip());
    userData.putString(Constants.PAR_MAPPING + Constants.POSTAL_ADDRESS, postal.getExtendedAddress());
    
    return userData;
  }
  
  public static Bundle mappingFrom(AccountManager accountManager, Account account) {
    // LDAP name mappings
    final Bundle userMapping = new Bundle();
  // person 
    userMapping.putString(Constants.CN, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.CN));
    userMapping.putString(Constants.SN, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.SN));
    userMapping.putString(Constants.USER_PASSWORD, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.USER_PASSWORD));
    userMapping.putString(Constants.TELEPHONE_NUMBER, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.TELEPHONE_NUMBER));
    userMapping.putString(Constants.SEE_ALSO, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.SEE_ALSO));
    userMapping.putString(Constants.DESCRIPTION, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.DESCRIPTION));
 // OrganizationalPerson
    userMapping.putString(Constants.TITLE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.TITLE));
    userMapping.putString(Constants.X12_ADDRESS, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.X12_ADDRESS));
    userMapping.putString(Constants.REGISTRED_ADDRESS, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.REGISTRED_ADDRESS));
    userMapping.putString(Constants.DESTINATION_INDICATOR, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.DESTINATION_INDICATOR));
    userMapping.putString(Constants.INTERNATIONAL_SDN_NUMBER, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.INTERNATIONAL_SDN_NUMBER));
    userMapping.putString(Constants.FASCIMILE_TELEPHONE_NUMBER, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.FASCIMILE_TELEPHONE_NUMBER));
    userMapping.putString(Constants.PREFERRED_DELIVERY_METHOD, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.PREFERRED_DELIVERY_METHOD));
    userMapping.putString(Constants.TELEX_NUMBER, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.TELEX_NUMBER));
    userMapping.putString(Constants.PHYSICAL_DELIVERY_OFFICE_NAME, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.PHYSICAL_DELIVERY_OFFICE_NAME));
    userMapping.putString(Constants.OU, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.OU));
    userMapping.putString(Constants.ST, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.ST));
    userMapping.putString(Constants.L, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.L));
 // InetOrgPerson
    userMapping.putString(Constants.AUDIO, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.AUDIO));
    userMapping.putString(Constants.BUSSINES_CATEGORY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.BUSSINES_CATEGORY));
    userMapping.putString(Constants.CAR_LICENCE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.CAR_LICENCE));
    userMapping.putString(Constants.DEPARTMENT_NUMBER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.DEPARTMENT_NUMBER));
    userMapping.putString(Constants.DISPLAY_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.DISPLAY_NAME));
    userMapping.putString(Constants.EMPLOYEE_NUMBER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.EMPLOYEE_NUMBER));
    userMapping.putString(Constants.EMPLOYEE_TYPE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.EMPLOYEE_TYPE));
    userMapping.putString(Constants.GIVEN_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.GIVEN_NAME));
    userMapping.putString(Constants.HOME_PHONE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_PHONE));
    userMapping.putString(Constants.HOME_POSTAL_ADDRESS, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_POSTAL_ADDRESS));
    userMapping.putString(Constants.INITIALS, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.INITIALS));
    userMapping.putString(Constants.JPEG_PHOTO, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.JPEG_PHOTO));
    userMapping.putString(Constants.LABELED_URI, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.LABELED_URI));
    userMapping.putString(Constants.MAIL, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.MAIL));
    userMapping.putString(Constants.MANAGER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.MANAGER));
    userMapping.putString(Constants.MOBILE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.MOBILE));
    userMapping.putString(Constants.O, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.O));
    userMapping.putString(Constants.PAGER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.PAGER));
    userMapping.putString(Constants.PHOTO, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.PHOTO));
    userMapping.putString(Constants.ROOM_NUMBER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.ROOM_NUMBER));
    userMapping.putString(Constants.SECRETARY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.SECRETARY));
    userMapping.putString(Constants.UID, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.UID));
    userMapping.putString(Constants.USER_CERTIFICATE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.USER_CERTIFICATE));
    userMapping.putString(Constants.X500_UNIQUE_IDENTIFIER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.X500_UNIQUE_IDENTIFIER));
    userMapping.putString(Constants.PREFERRED_LANGUAGE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.PREFERRED_LANGUAGE));
    userMapping.putString(Constants.USER_SMIME_CERTIFICATE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.USER_SMIME_CERTIFICATE));
    userMapping.putString(Constants.USER_PKCS12, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.USER_PKCS12));
    userMapping.putString(Constants.GIVEN_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.GIVEN_NAME));
  //GoogleContatc
    userMapping.putString(Constants.ADDITIONAL_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.ADDITIONAL_NAME));
    userMapping.putString(Constants.NAME_PREFIX, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.NAME_PREFIX));
    userMapping.putString(Constants.NAME_SUFFIX, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.NAME_SUFFIX));
    userMapping.putString(Constants.NICKNAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.NICKNAME));
    userMapping.putString(Constants.SHORT_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.SHORT_NAME));
    userMapping.putString(Constants.MAIDEN_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.MAIDEN_NAME));
    userMapping.putString(Constants.GENDER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.GENDER));
    userMapping.putString(Constants.NOTES, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.NOTES));
    userMapping.putString(Constants.HOME_MAIL, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_MAIL));
    userMapping.putString(Constants.WORK_MAIL, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_MAIL));
    userMapping.putString(Constants.WORK_PHONE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_PHONE));
    userMapping.putString(Constants.WEBSITE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WEBSITE));
    //GoogleContact - Home address
    userMapping.putString(Constants.HOME_CITY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_CITY));
    userMapping.putString(Constants.HOME_COUNTRY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_COUNTRY));
    userMapping.putString(Constants.HOME_EXTENDED_ADDRESS, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_EXTENDED_ADDRESS));
    userMapping.putString(Constants.HOME_POBOX, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_POBOX));
    userMapping.putString(Constants.HOME_REGION, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_REGION));
    userMapping.putString(Constants.HOME_STREET, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_STREET));
    userMapping.putString(Constants.HOME_POSTAL_CODE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_POSTAL_CODE));
    //GoogleContact - Work address
    userMapping.putString(Constants.WORK_CITY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_CITY));
    userMapping.putString(Constants.WORK_COUNTRY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_COUNTRY));
    userMapping.putString(Constants.WORK_EXTENDED_ADDRESS, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_EXTENDED_ADDRESS));
    userMapping.putString(Constants.WORK_POBOX, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_POBOX));
    userMapping.putString(Constants.WORK_REGION, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_REGION));
    userMapping.putString(Constants.WORK_STREET, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_STREET));
    userMapping.putString(Constants.WORK_POSTAL_CODE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_POSTAL_CODE));
    //GoogleContact - postal address
    userMapping.putString(Constants.POST_OFFICE_BOX, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.POST_OFFICE_BOX));
    userMapping.putString(Constants.POSTAL_CODE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.POSTAL_CODE));
    userMapping.putString(Constants.POSTAL_ADDRESS, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.POSTAL_ADDRESS));
    
    return userMapping; 
  }
  
  public static AddRequest mappingRequest(ContentResolver cr, String id, String baseDn) {
    //Cursor cursor = ContactDetail.fetchAllDataOfContact(cr);
    //Log.i("aaa", cursor.toString());
    /*
    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null);
    if (cur.getCount() > 0) {
  while (cur.moveToNext()) {
      String idC = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID));
String name = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
    //Query phone here.  Covered next
      }
        }
}
    */
    
    
    try {
      AddRequest addRequest = new AddRequest(
        Constants.DN + ": " + "cn=pokus,ou=users," + baseDn,
          Constants.OBJECT_CLASS_GOOGLE,
          Constants.OBJECT_CLASS_INET,
          Constants.OBJECT_CLASS_ORG,
          Constants.OBJECT_CLASS_PERSON,
          Constants.OBJECT_CLASS_TOP,
          "cn: aaa",
          "sn: bbb");
      
      Log.i(TAG, addRequest.toLDIFString());
      return addRequest;
    } catch (LDIFException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
}





















