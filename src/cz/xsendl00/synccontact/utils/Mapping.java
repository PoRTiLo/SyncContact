package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;
import java.util.List;

import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.Attribute;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Identity;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.CommonDataKinds.SipAddress;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.Address;
import cz.xsendl00.synccontact.client.AddressType;
import cz.xsendl00.synccontact.client.GoogleContact;
import cz.xsendl00.synccontact.database.HelperSQL;

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
  
  public static List<String> fetchDirtyContacts(Context context) {
    HelperSQL db = new HelperSQL(context);
    List<String> list = db.getSyncContactsId();
    List<String> dirtyContactsId = new ArrayList<String>();;
    for (String id : list) {
      //Log.i(TAG, id);
      Cursor c = context.getContentResolver().query(
        RawContacts.CONTENT_URI,
        new String[]{RawContacts._ID},
        RawContacts.CONTACT_ID + "=? AND " + RawContacts.DIRTY + "=?",
        new String[]{id.toString(), "1"}, null);
      while (c.moveToNext()) {
        //Log.i(TAG, c.getString(c.getColumnIndex(RawContacts._ID)));
        dirtyContactsId.add(c.getString(c.getColumnIndex(RawContacts._ID)));
      }
      c.close();
    }
    return dirtyContactsId;
  }
  
  public static AddRequest mappingRequest(ContentResolver cr, String id, String baseDn) {
    Cursor cursor = ContactDetail.fetchAllDataOfContact(cr, id);
    ArrayList<Attribute> attributes = new ArrayList<Attribute>();
    
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_GOOGLE));
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_INET));
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_ORG));
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_PERSON));
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_TOP));
    
    
    while (cursor.moveToNext()) {
      attributes.addAll(fill(cursor));
    }
    cursor.close();
    AddRequest addRequest = new AddRequest(baseDn, attributes);
    
    Log.i(TAG, addRequest.toLDIFString());
    return addRequest;
  }
  
//TODO: vnd.com.google.cursor.item/contact_misc add by google?
  private static ArrayList<Attribute> fill(Cursor cursor) {
   
   ArrayList<Attribute> attributes = new ArrayList<Attribute>();
   
   String str = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE));
   
   if (str.equals(StructuredName.CONTENT_ITEM_TYPE)) {
     if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
       attributes.add(new Attribute(Constants.DISPLAY_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     }
     if (!cursor.isNull(cursor.getColumnIndex(Data.DATA2))) {
       attributes.add(new Attribute(Constants.GIVEN_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA2))));
     }
     if (!cursor.isNull(cursor.getColumnIndex(Data.DATA3))) {
       attributes.add(new Attribute(Constants.FAMILY_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA3))));
     }
     if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
       attributes.add(new Attribute(Constants.NAME_PREFIX, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
     }
     if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
       attributes.add(new Attribute(Constants.MIDDLE_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
     }
     if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
       attributes.add(new Attribute(Constants.NAME_SUFFIX, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
     }
     if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
       attributes.add(new Attribute(Constants.PHONETIC_GIVEN_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
     }
     if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
       attributes.add(new Attribute(Constants.PHONETIC_MIDDLE_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
     }
     if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
       attributes.add(new Attribute(Constants.PHONETIC_FAMILY_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
     }
   } else if (str.equals(Phone.CONTENT_ITEM_TYPE)) {
     Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
     if (type == Phone.TYPE_CUSTOM) {
       //TYPE_CUSTOM. Put the actual type in LABEL.
       //String  LABEL DATA3
     } else if (type == Phone.TYPE_ASSISTANT) {
       attributes.add(new Attribute(Constants.PHONE_ASSISTANT, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_CALLBACK) {
       attributes.add(new Attribute(Constants.PHONE_CALLBACK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_CAR) {
       attributes.add(new Attribute(Constants.PHONE_CAR, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_COMPANY_MAIN) {
       attributes.add(new Attribute(Constants.PHONE_COMPANY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_FAX_HOME) {
       attributes.add(new Attribute(Constants.PHONE_FAX_HOME, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_FAX_WORK) {
       attributes.add(new Attribute(Constants.PHONE_FAX_WORK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_HOME) {
       attributes.add(new Attribute(Constants.PHONE_HOME, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_ISDN) {
       attributes.add(new Attribute(Constants.PHONE_ISDN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_MAIN) {
       attributes.add(new Attribute(Constants.PHONE_MAIN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_MMS) {
       attributes.add(new Attribute(Constants.PHONE_MMS, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_MOBILE) {
       attributes.add(new Attribute(Constants.PHONE_MOBILE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_OTHER) {
       attributes.add(new Attribute(Constants.PHONE_OTHER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_OTHER_FAX) {
       attributes.add(new Attribute(Constants.PHONE_OTHER_FAX, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_PAGER) {
       attributes.add(new Attribute(Constants.PHONE_PAGER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_RADIO) {
       attributes.add(new Attribute(Constants.PHONE_RADIO, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_TELEX) {
       attributes.add(new Attribute(Constants.PHONE_TELEX, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_TTY_TDD) {
       attributes.add(new Attribute(Constants.PHONE_TTY_TDD, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_WORK) {
       attributes.add(new Attribute(Constants.PHONE_WORK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_WORK_MOBILE) {
       attributes.add(new Attribute(Constants.PHONE_WORK_MOBILE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Phone.TYPE_WORK_PAGER) {
       attributes.add(new Attribute(Constants.PHONE_WORK_PAGER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else {
       Log.i("NOT SUPPORTED TYPE PHONE", "NOT SUPPORTED TYPE PHONE");
     }
   } else if (str.equals(Email.CONTENT_ITEM_TYPE)) {
     Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
     if (type == Email.TYPE_HOME) {
       attributes.add(new Attribute(Constants.HOME_MAIL, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Email.TYPE_WORK) {
       attributes.add(new Attribute(Constants.WORK_MAIL, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Email.TYPE_OTHER) {
       attributes.add(new Attribute(Constants.OTHER_MAIL, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Email.TYPE_MOBILE) {
       attributes.add(new Attribute(Constants.MOBILE_MAIL, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Email.TYPE_CUSTOM) {
       // TODO:
     } else {
       Log.i("NOT SUPPORTED TYPE EMAIL", "NOT SUPPORTED TYPE EMAIL");
     }
   } else if (str.equals(Photo.CONTENT_ITEM_TYPE)) {
     
   } else if (str.equals(Organization.CONTENT_ITEM_TYPE)) {
     Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
     if (type == Organization.TYPE_WORK) {
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_WORK_COMPANY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_WORK_TITLE, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_WORK_DEPARTMENT, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_WORK_SYMBOL, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_WORK_PHONETIC_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_WORK_OFFICE_LOCATION, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE, cursor.getString(cursor.getColumnIndex(Data.DATA10))));
       }
     } else if (type == Organization.TYPE_OTHER) {
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_COMPANY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_TITLE, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_DEPARTMENT, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_SYMBOL, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_PHONETIC_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
         attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE, cursor.getString(cursor.getColumnIndex(Data.DATA10))));
       }
     } else if (type == Organization.TYPE_CUSTOM) {
       // TODO:      String  LABEL DATA3 
     } else {
       
     }
   } else if (str.equals(Im.CONTENT_ITEM_TYPE)) {
     Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
     Integer protocol = cursor.getInt(cursor.getColumnIndex(Data.DATA5));
     if (type == Im.TYPE_CUSTOM) {
       // TYPE_CUSTOM. Put the actual type in LABEL.
       // String  LABEL DATA3
     } else if (type == Im.TYPE_HOME) {
       if (protocol == Im.PROTOCOL_CUSTOM) {
         // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
         // String  CUSTOM_PROTOCOL DATA6
      // TODO:
       } else if (protocol == Im.PROTOCOL_AIM) {
         attributes.add(new Attribute(Constants.IM_HOME_AIM, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
         attributes.add(new Attribute(Constants.IM_HOME_GOOGLE_TALK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_ICQ) {
         attributes.add(new Attribute(Constants.IM_HOME_ICQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_JABBER) {
         attributes.add(new Attribute(Constants.IM_HOME_JABBER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_MSN) {
         attributes.add(new Attribute(Constants.IM_HOME_MSN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_NETMEETING) {
         attributes.add(new Attribute(Constants.IM_HOME_NETMEETING, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_QQ) {
         attributes.add(new Attribute(Constants.IM_HOME_QQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_SKYPE) {
         attributes.add(new Attribute(Constants.IM_HOME_SKYPE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_YAHOO) {
         attributes.add(new Attribute(Constants.IM_HOME_YAHOO, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else {
         Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
       }
     } else if (type == Im.TYPE_WORK) {
       if (protocol == Im.PROTOCOL_CUSTOM) {
         // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
         // String  CUSTOM_PROTOCOL DATA6
      // TODO:
       } else if (protocol == Im.PROTOCOL_AIM) {
         attributes.add(new Attribute(Constants.IM_WORK_AIM, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
         attributes.add(new Attribute(Constants.IM_WORK_GOOGLE_TALK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_ICQ) {
         attributes.add(new Attribute(Constants.IM_WORK_ICQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_JABBER) {
         attributes.add(new Attribute(Constants.IM_WORK_JABBER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_MSN) {
         attributes.add(new Attribute(Constants.IM_WORK_MSN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_NETMEETING) {
         attributes.add(new Attribute(Constants.IM_WORK_NETMEETING, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_QQ) {
         attributes.add(new Attribute(Constants.IM_WORK_QQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_SKYPE) {
         attributes.add(new Attribute(Constants.IM_WORK_SKYPE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_YAHOO) {
         attributes.add(new Attribute(Constants.IM_WORK_YAHOO, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else {
         Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
       }
     } else if (type == Im.TYPE_OTHER) {
       if (protocol == Im.PROTOCOL_CUSTOM) {
         // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
         // String  CUSTOM_PROTOCOL DATA6
      // TODO:
       } else if (protocol == Im.PROTOCOL_AIM) {
         attributes.add(new Attribute(Constants.IM_OTHER_AIM, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
         attributes.add(new Attribute(Constants.IM_OTHER_GOOGLE_TALK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_ICQ) {
         attributes.add(new Attribute(Constants.IM_OTHER_ICQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_JABBER) {
         attributes.add(new Attribute(Constants.IM_OTHER_JABBER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_MSN) {
         attributes.add(new Attribute(Constants.IM_OTHER_MSN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_NETMEETING) {
         attributes.add(new Attribute(Constants.IM_OTHER_NETMEETING, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_QQ) {
         attributes.add(new Attribute(Constants.IM_OTHER_QQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_SKYPE) {
         attributes.add(new Attribute(Constants.IM_OTHER_SKYPE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_YAHOO) {
         attributes.add(new Attribute(Constants.IM_OTHER_YAHOO, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else {
         Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
       }
     } else {
       if (protocol == Im.PROTOCOL_CUSTOM) {
         // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
         // String  CUSTOM_PROTOCOL DATA6
      // TODO:
       } else if (protocol == Im.PROTOCOL_AIM) {
         attributes.add(new Attribute(Constants.IM_NULL_AIM, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
         attributes.add(new Attribute(Constants.IM_NULL_GOOGLE_TALK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_ICQ) {
         attributes.add(new Attribute(Constants.IM_NULL_ICQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_JABBER) {
         attributes.add(new Attribute(Constants.IM_NULL_JABBER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_MSN) {
         attributes.add(new Attribute(Constants.IM_NULL_MSN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_NETMEETING) {
         attributes.add(new Attribute(Constants.IM_NULL_NETMEETING, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_QQ) {
         attributes.add(new Attribute(Constants.IM_NULL_QQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_SKYPE) {
         attributes.add(new Attribute(Constants.IM_NULL_SKYPE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else if (protocol == Im.PROTOCOL_YAHOO) {
         attributes.add(new Attribute(Constants.IM_NULL_YAHOO, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       } else {
         Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
       }
     }
   } else if (str.equals(Nickname.CONTENT_ITEM_TYPE)) {
     Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
     if (type == Nickname.TYPE_DEFAULT) {
       attributes.add(new Attribute(Constants.NICKNAME_DEFAULT, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Nickname.TYPE_OTHER_NAME) {
       attributes.add(new Attribute(Constants.NICKNAME_OTHER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Nickname.TYPE_MAIDEN_NAME) {
       attributes.add(new Attribute(Constants.NICKNAME_MAIDEN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Nickname.TYPE_SHORT_NAME) {
       attributes.add(new Attribute(Constants.NICKNAME_SHORT, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Nickname.TYPE_INITIALS) {
       attributes.add(new Attribute(Constants.NICKNAME_INITIALS, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Nickname.TYPE_CUSTOM) {
       // TODO: String  LABEL DATA3
     } else {
       Log.i("NOT SUPPORTED TYPE NICKNAME", "NOT SUPPORTED TYPE NICKANEME");
     }
   } else if (str.equals(Note.CONTENT_ITEM_TYPE)) {
     if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
       attributes.add(new Attribute(Constants.NOTES, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     }
   } else if (str.equals(StructuredPostal.CONTENT_ITEM_TYPE)) {
     Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
     if (type == StructuredPostal.TYPE_CUSTOM) {
       // TODO:
       //TYPE_CUSTOM. Put the actual type in LABEL.
       // String  LABEL DATA3
     } else if (type == StructuredPostal.TYPE_HOME) {
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
         attributes.add(new Attribute(Constants.HOME_FORMATTED_ADDRESS, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
         attributes.add(new Attribute(Constants.HOME_STREET, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
         attributes.add(new Attribute(Constants.HOME_POBOX, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
         attributes.add(new Attribute(Constants.HOME_NEIGHBORHOOD, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
         attributes.add(new Attribute(Constants.HOME_CITY, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
         attributes.add(new Attribute(Constants.HOME_REGION, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
         attributes.add(new Attribute(Constants.HOME_POSTAL_CODE, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
         attributes.add(new Attribute(Constants.HOME_COUNTRY, cursor.getString(cursor.getColumnIndex(Data.DATA10))));
       }
     } else if (type == StructuredPostal.TYPE_WORK) {
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
         attributes.add(new Attribute(Constants.WORK_FORMATTED_ADDRESS, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
         attributes.add(new Attribute(Constants.WORK_STREET, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
         attributes.add(new Attribute(Constants.WORK_POBOX, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
         attributes.add(new Attribute(Constants.WORK_NEIGHBORHOOD, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
         attributes.add(new Attribute(Constants.WORK_CITY, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
         attributes.add(new Attribute(Constants.WORK_REGION, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
         attributes.add(new Attribute(Constants.WORK_POSTAL_CODE, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
         attributes.add(new Attribute(Constants.WORK_COUNTRY, cursor.getString(cursor.getColumnIndex(Data.DATA10))));
       }
     } else if (type == StructuredPostal.TYPE_OTHER) {
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
         attributes.add(new Attribute(Constants.OTHER_FORMATTED_ADDRESS, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
         attributes.add(new Attribute(Constants.OTHER_STREET, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
         attributes.add(new Attribute(Constants.OTHER_POBOX, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
         attributes.add(new Attribute(Constants.OTHER_NEIGHBORHOOD, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
         attributes.add(new Attribute(Constants.OTHER_CITY, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
         attributes.add(new Attribute(Constants.OTHER_REGION, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
         attributes.add(new Attribute(Constants.OTHER_POSTAL_CODE, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
       }
       if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
         attributes.add(new Attribute(Constants.OTHER_COUNTRY, cursor.getString(cursor.getColumnIndex(Data.DATA10))));
       }
     } else {
       
     }
      
   } else if (str.equals(GroupMembership.CONTENT_ITEM_TYPE)) {
     // TODO:
     //long  GROUP_ROW_ID  DATA1
     //attributes.add(new Attribute(Constants., cursor.getString(cursor.getColumnIndex(Data.DATA1))));
   } else if (str.equals(Website.CONTENT_ITEM_TYPE)) {
     
   } else if (str.equals(Event.CONTENT_ITEM_TYPE)) {
     Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
     if (type == Event.TYPE_CUSTOM) {
       // TYPE_CUSTOM. Put the actual type in LABEL.
       // String  LABEL DATA3
     } else if (type == Event.TYPE_ANNIVERSARY) {
       attributes.add(new Attribute(Constants.EVENT_ANNIVERSARY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Event.TYPE_BIRTHDAY) {
       attributes.add(new Attribute(Constants.EVENT_BIRTHDAY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Event.TYPE_OTHER) {
       attributes.add(new Attribute(Constants.EVENT_OTHER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else {
       Log.i("NOT SUPPORTED TYPE Event", "NOT SUPPORTED TYPE EVENT");
     }
   } else if (str.equals(Relation.CONTENT_ITEM_TYPE)) {
     
   } else if (str.equals(SipAddress.CONTENT_ITEM_TYPE)) {
     Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
     if (type == SipAddress.TYPE_CUSTOM) {
       // TODO:
       // TYPE_CUSTOM. Put the actual type in LABEL.
       // String  LABEL DATA3
     } else if (type == SipAddress.TYPE_HOME) {
       attributes.add(new Attribute(Constants.HOME_SIP, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == SipAddress.TYPE_WORK) {
       attributes.add(new Attribute(Constants.WORK_SIP, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == SipAddress.TYPE_OTHER) {
       attributes.add(new Attribute(Constants.OTHER_SIP, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else {
       Log.i("NOT SUPPORTED TYPE SIP", "NOT SUPPORTED TYPE SIP");
     }
   } else if (str.equals(Identity.CONTENT_ITEM_TYPE)) {
     // A data kind representing an Identity related to the contact. 
     // This can be used as a signal by the aggregator to combine raw contacts into contacts, e.g. if two contacts have Identity rows with the same NAMESPACE and IDENTITY values the aggregator can know that they refer to the same person.
     Log.i("  IDENTITY", cursor.getString(cursor.getColumnIndex(Data.DATA1)));
     Log.i("  NAMESPACE", cursor.getString(cursor.getColumnIndex(Data.DATA2)));
   } else {
     Log.i("NOT SUPPORTED TYPE MIME", "NOT SUPPORTED TYPE MIME");
   }
   
   return attributes;
 }
}
