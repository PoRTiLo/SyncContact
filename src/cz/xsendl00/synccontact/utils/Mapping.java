package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.ReadOnlyEntry;
import com.unboundid.ldap.sdk.SearchResultEntry;

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
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.HelperSQL;

public class Mapping {

  private static final String TAG = "Mapping";

  public static List<String> fetchDirtyContacts(Context context) {
    HelperSQL db = new HelperSQL(context);
    List<String> list = db.getSyncContactsId();
    List<String> dirtyContactsId = new ArrayList<String>();
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
  
  public static Map<String, GoogleContact> fetchDirtyContacts(Context context, List<ContactRow> list) {
    Map<String, GoogleContact> dirtyContacts = new HashMap<String, GoogleContact>();
    for (ContactRow con : list) {
      //Log.i(TAG, con.getId());
      Cursor c = context.getContentResolver().query(
        RawContacts.CONTENT_URI,
        new String[]{RawContacts._ID},
        RawContacts._ID + "=? AND " + RawContacts.DIRTY + "=?",
        new String[]{con.getId(), "1"}, null);
      while (c.moveToNext()) {
        //Log.i(TAG, c.getString(c.getColumnIndex(RawContacts._ID)));
        dirtyContacts.put(con.getUuid(), mappingContactFromDB(context.getContentResolver(), c.getString(c.getColumnIndex(RawContacts._ID))));
      }
      c.close();
    }
    return dirtyContacts;
  }
  
  public static final String[] createAttributes() {
    ArrayList<String> ldapAttributes = new ArrayList<String>();
    ldapAttributes.add(Constants.WORK_SIP);
    ldapAttributes.add(Constants.HOME_SIP);
    ldapAttributes.add(Constants.OTHER_SIP);
    ldapAttributes.add(Constants.EVENT_OTHER);
    ldapAttributes.add(Constants.EVENT_BIRTHDAY);
    ldapAttributes.add(Constants.EVENT_ANNIVERSARY);
    ldapAttributes.add(Constants.NICKNAME_DEFAULT);
    ldapAttributes.add(Constants.NICKNAME_OTHER);
    ldapAttributes.add(Constants.NICKNAME_MAIDEN);
    ldapAttributes.add(Constants.NICKNAME_SHORT);
    ldapAttributes.add(Constants.NICKNAME_INITIALS);
    ldapAttributes.add(Constants.PHONE_ASSISTANT);
    ldapAttributes.add(Constants.PHONE_CALLBACK);
    ldapAttributes.add(Constants.PHONE_CAR);
    ldapAttributes.add(Constants.PHONE_COMPANY);
    ldapAttributes.add(Constants.PHONE_FAX_HOME);
    ldapAttributes.add(Constants.PHONE_FAX_WORK);
    ldapAttributes.add(Constants.PHONE_HOME);
    ldapAttributes.add(Constants.PHONE_ISDN);
    ldapAttributes.add(Constants.PHONE_MAIN);
    ldapAttributes.add(Constants.PHONE_MMS);
    ldapAttributes.add(Constants.PHONE_MOBILE);
    ldapAttributes.add(Constants.PHONE_OTHER);
    ldapAttributes.add(Constants.PHONE_OTHER_FAX);
    ldapAttributes.add(Constants.PHONE_PAGER);
    ldapAttributes.add(Constants.PHONE_RADIO);
    ldapAttributes.add(Constants.PHONE_TELEX);
    ldapAttributes.add(Constants.PHONE_TTY_TDD);
    ldapAttributes.add(Constants.PHONE_WORK);
    ldapAttributes.add(Constants.PHONE_WORK_MOBILE);
    ldapAttributes.add(Constants.PHONE_WORK_PAGER);
    ldapAttributes.add(Constants.IM_HOME_AIM );
    ldapAttributes.add(Constants.IM_HOME_GOOGLE_TALK );
    ldapAttributes.add(Constants.IM_HOME_ICQ );
    ldapAttributes.add(Constants.IM_HOME_JABBER);
    ldapAttributes.add(Constants.IM_HOME_MSN );
    ldapAttributes.add(Constants.IM_HOME_NETMEETING );
    ldapAttributes.add(Constants.IM_HOME_QQ );
    ldapAttributes.add(Constants.IM_HOME_SKYPE);
    ldapAttributes.add(Constants.IM_HOME_YAHOO);
    ldapAttributes.add(Constants.IM_WORK_AIM);
    ldapAttributes.add(Constants.IM_WORK_GOOGLE_TALK );
    ldapAttributes.add(Constants.IM_WORK_ICQ);
    ldapAttributes.add(Constants.IM_WORK_JABBER);
    ldapAttributes.add(Constants.IM_WORK_MSN);
    ldapAttributes.add(Constants.IM_WORK_NETMEETING);
    ldapAttributes.add(Constants.IM_WORK_QQ);
    ldapAttributes.add(Constants.IM_WORK_SKYPE);
    ldapAttributes.add(Constants.IM_WORK_YAHOO);
    ldapAttributes.add(Constants.IM_OTHER_AIM);
    ldapAttributes.add(Constants.IM_OTHER_GOOGLE_TALK);
    ldapAttributes.add(Constants.IM_OTHER_ICQ);
    ldapAttributes.add(Constants.IM_OTHER_JABBER);
    ldapAttributes.add(Constants.IM_OTHER_MSN );
    ldapAttributes.add(Constants.IM_OTHER_NETMEETING);
    ldapAttributes.add(Constants.IM_OTHER_QQ);
    ldapAttributes.add(Constants.IM_OTHER_SKYPE);
    ldapAttributes.add(Constants.IM_OTHER_YAHOO);
    /*ldapAttributes.add(Constants.IM_NULL_AIM);
    ldapAttributes.add(Constants.IM_NULL_GOOGLE_TALK);
    ldapAttributes.add(Constants.IM_NULL_ICQ);
    ldapAttributes.add(Constants.IM_NULL_JABBER);
    ldapAttributes.add(Constants.IM_NULL_MSN);
    ldapAttributes.add(Constants.IM_NULL_NETMEETING);
    ldapAttributes.add(Constants.IM_NULL_QQ);
    ldapAttributes.add(Constants.IM_NULL_SKYPE);
    ldapAttributes.add(Constants.IM_NULL_YAHOO);*/
    ldapAttributes.add(Constants.PHONETIC_MIDDLE_NAME);
    ldapAttributes.add(Constants.PHONETIC_GIVEN_NAME);
    ldapAttributes.add(Constants.PHONETIC_FAMILY_NAME);
    ldapAttributes.add(Constants.DISPLAY_NAME);
    ldapAttributes.add(Constants.GIVEN_NAME);
    ldapAttributes.add(Constants.NAME_PREFIX);
    ldapAttributes.add(Constants.NAME_SUFFIX);
    ldapAttributes.add(Constants.FAMILY_NAME);
    ldapAttributes.add(Constants.MIDDLE_NAME);
    ldapAttributes.add(Constants.ORGANIZATION_WORK_COMPANY);
    ldapAttributes.add(Constants.ORGANIZATION_WORK_TITLE);
    ldapAttributes.add(Constants.ORGANIZATION_WORK_DEPARTMENT );
    ldapAttributes.add(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION );
    ldapAttributes.add(Constants.ORGANIZATION_WORK_SYMBOL );
    ldapAttributes.add(Constants.ORGANIZATION_WORK_PHONETIC_NAME );
    ldapAttributes.add(Constants.ORGANIZATION_WORK_OFFICE_LOCATION );
    ldapAttributes.add(Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE);
    ldapAttributes.add(Constants.ORGANIZATION_OTHER_COMPANY);
    ldapAttributes.add(Constants.ORGANIZATION_OTHER_TITLE);
    ldapAttributes.add(Constants.ORGANIZATION_OTHER_DEPARTMENT);
    ldapAttributes.add(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION);
    ldapAttributes.add(Constants.ORGANIZATION_OTHER_SYMBOL);
    ldapAttributes.add(Constants.ORGANIZATION_OTHER_PHONETIC_NAME);
    ldapAttributes.add(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION);
    ldapAttributes.add(Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE);
    ldapAttributes.add(Constants.IDENTITY_TEXT);
    ldapAttributes.add(Constants.IDENTITY_NAMESPACE );
    ldapAttributes.add(Constants.RELATION_ASSISTANT);
    ldapAttributes.add(Constants.RELATION_BROTHER);
    ldapAttributes.add(Constants.RELATION_CHILD);
    ldapAttributes.add(Constants.RELATION_DOMESTIC_PARTNER);
    ldapAttributes.add(Constants.RELATION_FATHER);
    ldapAttributes.add(Constants.RELATION_FRIEND);
    ldapAttributes.add(Constants.RELATION_MANAGER);
    ldapAttributes.add(Constants.RELATION_MOTHER );
    ldapAttributes.add(Constants.RELATION_PARENT);
    ldapAttributes.add(Constants.RELATION_PARTNER);
    ldapAttributes.add(Constants.RELATION_REFFERED_BY);
    ldapAttributes.add(Constants.RELATION_RELATIVE);
    ldapAttributes.add(Constants.RELATION_SISTER);
    ldapAttributes.add(Constants.RELATION_SPOUSE);
    ldapAttributes.add(Constants.WEBSITE_HOMEPAGE);
    ldapAttributes.add(Constants.WEBSITE_BLOG);
    ldapAttributes.add(Constants.WEBSITE_PROFILE);
    ldapAttributes.add(Constants.WEBSITE_HOME);
    ldapAttributes.add(Constants.WEBSITE_WORK);
    ldapAttributes.add(Constants.WEBSITE_FTP);
    ldapAttributes.add(Constants.WEBSITE_OTHER);
    ldapAttributes.add(Constants.HOME_MAIL);
    ldapAttributes.add(Constants.WORK_MAIL);
    ldapAttributes.add(Constants.OTHER_MAIL);
    ldapAttributes.add(Constants.MOBILE_MAIL);
    ldapAttributes.add(Constants.NOTES);
    ldapAttributes.add(Constants.HOME_STREET);
    ldapAttributes.add(Constants.HOME_POBOX);
    ldapAttributes.add(Constants.HOME_CITY);
    ldapAttributes.add(Constants.HOME_REGION);
    ldapAttributes.add(Constants.HOME_POSTAL_CODE);
    ldapAttributes.add(Constants.HOME_COUNTRY);
    ldapAttributes.add(Constants.WORK_STREET);
    ldapAttributes.add(Constants.WORK_POBOX);
    ldapAttributes.add(Constants.WORK_CITY);
    ldapAttributes.add(Constants.WORK_REGION);
    ldapAttributes.add(Constants.WORK_POSTAL_CODE);
    ldapAttributes.add(Constants.WORK_COUNTRY);
    ldapAttributes.add(Constants.WORK_FORMATTED_ADDRESS);
    ldapAttributes.add(Constants.HOME_FORMATTED_ADDRESS );
    ldapAttributes.add(Constants.WORK_NEIGHBORHOOD);
    ldapAttributes.add(Constants.HOME_NEIGHBORHOOD);
    ldapAttributes.add(Constants.OTHER_NEIGHBORHOOD);
    ldapAttributes.add(Constants.OTHER_STREET );
    ldapAttributes.add(Constants.OTHER_CITY );
    ldapAttributes.add(Constants.OTHER_POBOX );
    ldapAttributes.add(Constants.OTHER_REGION );
    ldapAttributes.add(Constants.OTHER_POSTAL_CODE );
    ldapAttributes.add(Constants.OTHER_COUNTRY);
    ldapAttributes.add(Constants.OTHER_FORMATTED_ADDRESS);
    
    String[] ldapArray = new String[ldapAttributes.size()];
    ldapArray = ldapAttributes.toArray(ldapArray);
    return ldapArray;
  }
  
  public static AddRequest mappingRequest(ContentResolver cr, String id, String baseDn, String rdn) {
    Cursor cursor = ContactDetail.fetchAllDataOfContact(cr, id);
    ArrayList<Attribute> attributes = new ArrayList<Attribute>();
    
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_GOOGLE));
    //attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_INET));
    //attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_ORG));
    //attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_PERSON));
    //attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_TOP));
    attributes.add(new Attribute("uuid", rdn));
    
    
    while (cursor.moveToNext()) {
      attributes.addAll(fill(cursor));
    }
    cursor.close();
    AddRequest addRequest = new AddRequest("uuid="+rdn+",ou=users,"+baseDn, attributes);
    
    Log.i(TAG, addRequest.toLDIFString());
    return addRequest;
  }
  
  public static GoogleContact mappingContactFromDB(ContentResolver cr, String id) {
    Cursor cursor = ContactDetail.fetchAllDataOfContact(cr, id);
    GoogleContact contact = new GoogleContact();
    while (cursor.moveToNext()) {
      fillContact(cursor, contact);
    }
    cursor.close();
    
    //Log.i(TAG, contact.toString());
    return contact;
  }
  
  public static GoogleContact mappingContactFromLDAP(ReadOnlyEntry user) {
    GoogleContact c = GoogleContact.defaultValue();
    // email
    c.getEmail().setHomeMail(user.hasAttribute(c.getEmail().getHomeMail()) ? user.getAttributeValue(c.getEmail().getHomeMail()) : null);
    c.getEmail().setMobileMail(user.hasAttribute(c.getEmail().getMobileMail()) ? user.getAttributeValue(c.getEmail().getMobileMail()) : null);
    c.getEmail().setOtherMail(user.hasAttribute(c.getEmail().getOtherMail()) ? user.getAttributeValue(c.getEmail().getOtherMail()) : null);
    c.getEmail().setWorkMail(user.hasAttribute(c.getEmail().getWorkMail()) ? user.getAttributeValue(c.getEmail().getWorkMail()) : null);
    // event
    c.getEvent().setEventAnniversary(user.hasAttribute(c.getEvent().getEventAnniversary()) ? user.getAttributeValue(c.getEvent().getEventAnniversary()) : null);
    c.getEvent().setEventBirthday(user.hasAttribute(c.getEvent().getEventBirthday()) ? user.getAttributeValue(c.getEvent().getEventBirthday()) : null);
    c.getEvent().setEventOther(user.hasAttribute(c.getEvent().getEventOther()) ? user.getAttributeValue(c.getEvent().getEventOther()) : null);
    // identity
    c.getIdentity().setIdentityNamespace(user.hasAttribute(c.getIdentity().getIdentityNamespace()) ? user.getAttributeValue(c.getIdentity().getIdentityNamespace()) : null);
    c.getIdentity().setIdentityText(user.hasAttribute(c.getIdentity().getIdentityText()) ? user.getAttributeValue(c.getIdentity().getIdentityText()) : null);
    // im
    c.getIm().setImHomeAim(user.hasAttribute(c.getIm().getImHomeAim()) ? user.getAttributeValue(c.getIm().getImHomeAim()) : null);
    c.getIm().setImHomeGoogleTalk(user.hasAttribute(c.getIm().getImHomeGoogleTalk()) ? user.getAttributeValue(c.getIm().getImHomeGoogleTalk()) : null);
    c.getIm().setImHomeIcq(user.hasAttribute(c.getIm().getImHomeIcq()) ? user.getAttributeValue(c.getIm().getImHomeIcq()) : null);
    c.getIm().setImHomeJabber(user.hasAttribute(c.getIm().getImHomeJabber()) ? user.getAttributeValue(c.getIm().getImHomeJabber()) : null);
    c.getIm().setImHomeMsn(user.hasAttribute(c.getIm().getImHomeMsn()) ? user.getAttributeValue(c.getIm().getImHomeMsn()) : null);
    c.getIm().setImHomeNetmeeting(user.hasAttribute(c.getIm().getImHomeNetmeeting()) ? user.getAttributeValue(c.getIm().getImHomeNetmeeting()) : null);
    c.getIm().setImHomeQq(user.hasAttribute(c.getIm().getImHomeQq()) ? user.getAttributeValue(c.getIm().getImHomeQq()) : null);
    c.getIm().setImHomeSkype(user.hasAttribute(c.getIm().getImHomeSkype()) ? user.getAttributeValue(c.getIm().getImHomeSkype()) : null);
    c.getIm().setImHomeYahoo(user.hasAttribute(c.getIm().getImHomeYahoo()) ? user.getAttributeValue(c.getIm().getImHomeYahoo()) : null);
    c.getIm().setImOtherAim(user.hasAttribute(c.getIm().getImOtherAim()) ? user.getAttributeValue(c.getIm().getImOtherAim()) : null);
    c.getIm().setImOtherGoogleTalk(user.hasAttribute(c.getIm().getImOtherGoogleTalk()) ? user.getAttributeValue(c.getIm().getImOtherGoogleTalk()) : null);
    c.getIm().setImOtherIcq(user.hasAttribute(c.getIm().getImOtherIcq()) ? user.getAttributeValue(c.getIm().getImOtherIcq()) : null);
    c.getIm().setImOtherJabber(user.hasAttribute(c.getIm().getImOtherJabber()) ? user.getAttributeValue(c.getIm().getImOtherJabber()) : null);
    c.getIm().setImOtherMsn(user.hasAttribute(c.getIm().getImOtherMsn()) ? user.getAttributeValue(c.getIm().getImOtherMsn()) : null);
    c.getIm().setImOtherNetmeeting(user.hasAttribute(c.getIm().getImOtherNetmeeting()) ? user.getAttributeValue(c.getIm().getImOtherNetmeeting()) : null);
    c.getIm().setImOtherQq(user.hasAttribute(c.getIm().getImOtherQq()) ? user.getAttributeValue(c.getIm().getImOtherQq()) : null);
    c.getIm().setImOtherSkype(user.hasAttribute(c.getIm().getImOtherSkype()) ? user.getAttributeValue(c.getIm().getImOtherSkype()) : null);
    c.getIm().setImOtherYahoo(user.hasAttribute(c.getIm().getImOtherYahoo()) ? user.getAttributeValue(c.getIm().getImOtherYahoo()) : null);
    c.getIm().setImWorkAim(user.hasAttribute(c.getIm().getImWorkAim()) ? user.getAttributeValue(c.getIm().getImWorkAim()) : null);
    c.getIm().setImWorkGoogleTalk(user.hasAttribute(c.getIm().getImWorkGoogleTalk()) ? user.getAttributeValue(c.getIm().getImWorkGoogleTalk()) : null);
    c.getIm().setImWorkIcq(user.hasAttribute(c.getIm().getImWorkIcq()) ? user.getAttributeValue(c.getIm().getImWorkIcq()) : null);
    c.getIm().setImWorkJabber(user.hasAttribute(c.getIm().getImWorkJabber()) ? user.getAttributeValue(c.getIm().getImWorkJabber()) : null);
    c.getIm().setImWorkMsn(user.hasAttribute(c.getIm().getImWorkMsn()) ? user.getAttributeValue(c.getIm().getImWorkMsn()) : null);
    c.getIm().setImWorkNetmeeting(user.hasAttribute(c.getIm().getImWorkNetmeeting()) ? user.getAttributeValue(c.getIm().getImWorkNetmeeting()) : null);
    c.getIm().setImWorkQq(user.hasAttribute(c.getIm().getImWorkQq()) ? user.getAttributeValue(c.getIm().getImWorkQq()) : null);
    c.getIm().setImWorkSkype(user.hasAttribute(c.getIm().getImWorkSkype()) ? user.getAttributeValue(c.getIm().getImWorkSkype()) : null);
    c.getIm().setImWorkYahoo(user.hasAttribute(c.getIm().getImWorkYahoo()) ? user.getAttributeValue(c.getIm().getImWorkYahoo()) : null);
    // nickname
    c.getNickname().setNicknameDefault(user.hasAttribute(c.getNickname().getNicknameDefault()) ? user.getAttributeValue(c.getNickname().getNicknameDefault()) : null);
    c.getNickname().setNicknameInitials(user.hasAttribute(c.getNickname().getNicknameInitials()) ? user.getAttributeValue(c.getNickname().getNicknameInitials()) : null);
    c.getNickname().setNicknameMaiden(user.hasAttribute(c.getNickname().getNicknameMaiden()) ? user.getAttributeValue(c.getNickname().getNicknameMaiden()) : null);
    c.getNickname().setNicknameOther(user.hasAttribute(c.getNickname().getNicknameOther()) ? user.getAttributeValue(c.getNickname().getNicknameOther()) : null);
    c.getNickname().setNicknameShort(user.hasAttribute(c.getNickname().getNicknameShort()) ? user.getAttributeValue(c.getNickname().getNicknameShort()) : null);
    // notes
    c.getNote().setNotes(user.hasAttribute(c.getNote().getNotes()) ? user.getAttributeValue(c.getNote().getNotes()) : null);
    // organization
    c.getOrganization().setOrganizationOtherCompany(user.hasAttribute(c.getOrganization().getOrganizationOtherCompany()) ? user.getAttributeValue(c.getOrganization().getOrganizationOtherCompany()) : null);
    c.getOrganization().setOrganizationOtherDepartment(user.hasAttribute(c.getOrganization().getOrganizationOtherDepartment()) ? user.getAttributeValue(c.getOrganization().getOrganizationOtherDepartment()) : null);
    c.getOrganization().setOrganizationOtherJobDescription(user.hasAttribute(c.getOrganization().getOrganizationOtherJobDescription()) ? user.getAttributeValue(c.getOrganization().getOrganizationOtherJobDescription()) : null);
    c.getOrganization().setOrganizationOtherOfficeLocation(user.hasAttribute(c.getOrganization().getOrganizationOtherOfficeLocation()) ? user.getAttributeValue(c.getOrganization().getOrganizationOtherOfficeLocation()) : null);
    c.getOrganization().setOrganizationOtherPhoneticName(user.hasAttribute(c.getOrganization().getOrganizationOtherPhoneticName()) ? user.getAttributeValue(c.getOrganization().getOrganizationOtherPhoneticName()) : null);
    c.getOrganization().setOrganizationOtherPhoneticNameStyle(user.hasAttribute(c.getOrganization().getOrganizationOtherPhoneticNameStyle()) ? user.getAttributeValue(c.getOrganization().getOrganizationOtherPhoneticNameStyle()) : null);
    c.getOrganization().setOrganizationOtherSymbol(user.hasAttribute(c.getOrganization().getOrganizationOtherSymbol()) ? user.getAttributeValue(c.getOrganization().getOrganizationOtherSymbol()) : null);
    c.getOrganization().setOrganizationOtherTitle(user.hasAttribute(c.getOrganization().getOrganizationOtherTitle()) ? user.getAttributeValue(c.getOrganization().getOrganizationOtherTitle()) : null);
    c.getOrganization().setOrganizationWorkCompany(user.hasAttribute(c.getOrganization().getOrganizationWorkCompany()) ? user.getAttributeValue(c.getOrganization().getOrganizationWorkCompany()) : null);
    c.getOrganization().setOrganizationWorkDepartment(user.hasAttribute(c.getOrganization().getOrganizationWorkDepartment()) ? user.getAttributeValue(c.getOrganization().getOrganizationWorkDepartment()) : null);
    c.getOrganization().setOrganizationWorkJobDescription(user.hasAttribute(c.getOrganization().getOrganizationWorkJobDescription()) ? user.getAttributeValue(c.getOrganization().getOrganizationWorkJobDescription()) : null);
    c.getOrganization().setOrganizationWorkOfficeLocation(user.hasAttribute(c.getOrganization().getOrganizationWorkOfficeLocation()) ? user.getAttributeValue(c.getOrganization().getOrganizationWorkOfficeLocation()) : null);
    c.getOrganization().setOrganizationWorkPhoneticName(user.hasAttribute(c.getOrganization().getOrganizationWorkPhoneticName()) ? user.getAttributeValue(c.getOrganization().getOrganizationWorkPhoneticName()) : null);
    c.getOrganization().setOrganizationWorkPhoneticNameStyle(user.hasAttribute(c.getOrganization().getOrganizationWorkPhoneticNameStyle()) ? user.getAttributeValue(c.getOrganization().getOrganizationWorkPhoneticNameStyle()) : null);
    c.getOrganization().setOrganizationWorkSymbol(user.hasAttribute(c.getOrganization().getOrganizationWorkSymbol()) ? user.getAttributeValue(c.getOrganization().getOrganizationWorkSymbol()) : null);
    c.getOrganization().setOrganizationWorkTitle(user.hasAttribute(c.getOrganization().getOrganizationWorkTitle()) ? user.getAttributeValue(c.getOrganization().getOrganizationWorkTitle()) : null);
    // phone
    c.getPhone().setPhoneAssistant(user.hasAttribute(c.getPhone().getPhoneAssistant()) ? user.getAttributeValue(c.getPhone().getPhoneAssistant()) : null);
    c.getPhone().setPhoneCallback(user.hasAttribute(c.getPhone().getPhoneCallback()) ? user.getAttributeValue(c.getPhone().getPhoneCallback()) : null);
    c.getPhone().setPhoneCar(user.hasAttribute(c.getPhone().getPhoneCar()) ? user.getAttributeValue(c.getPhone().getPhoneCar()) : null);
    c.getPhone().setPhoneCompany(user.hasAttribute(c.getPhone().getPhoneCompany()) ? user.getAttributeValue(c.getPhone().getPhoneCompany()) : null);
    c.getPhone().setPhoneFaxHome(user.hasAttribute(c.getPhone().getPhoneFaxHome()) ? user.getAttributeValue(c.getPhone().getPhoneFaxHome()) : null);
    c.getPhone().setPhoneFaxWork(user.hasAttribute(c.getPhone().getPhoneFaxWork()) ? user.getAttributeValue(c.getPhone().getPhoneFaxWork()) : null);
    c.getPhone().setPhoneHome(user.hasAttribute(c.getPhone().getPhoneHome()) ? user.getAttributeValue(c.getPhone().getPhoneHome()) : null);
    c.getPhone().setPhoneISDN(user.hasAttribute(c.getPhone().getPhoneISDN()) ? user.getAttributeValue(c.getPhone().getPhoneISDN()) : null);
    c.getPhone().setPhoneMain(user.hasAttribute(c.getPhone().getPhoneMain()) ? user.getAttributeValue(c.getPhone().getPhoneMain()) : null);
    c.getPhone().setPhoneMMS(user.hasAttribute(c.getPhone().getPhoneMMS()) ? user.getAttributeValue(c.getPhone().getPhoneMMS()) : null);
    c.getPhone().setPhoneMobile(user.hasAttribute(c.getPhone().getPhoneMobile()) ? user.getAttributeValue(c.getPhone().getPhoneMobile()) : null);
    c.getPhone().setPhoneOther(user.hasAttribute(c.getPhone().getPhoneOther()) ? user.getAttributeValue(c.getPhone().getPhoneOther()) : null);
    c.getPhone().setPhoneOtherFax(user.hasAttribute(c.getPhone().getPhoneOtherFax()) ? user.getAttributeValue(c.getPhone().getPhoneOtherFax()) : null);
    c.getPhone().setPhonePager(user.hasAttribute(c.getPhone().getPhonePager()) ? user.getAttributeValue(c.getPhone().getPhonePager()) : null);
    c.getPhone().setPhoneRadio(user.hasAttribute(c.getPhone().getPhoneRadio()) ? user.getAttributeValue(c.getPhone().getPhoneRadio()) : null);
    c.getPhone().setPhoneTelex(user.hasAttribute(c.getPhone().getPhoneTelex()) ? user.getAttributeValue(c.getPhone().getPhoneTelex()) : null);
    c.getPhone().setPhoneTTYTDD(user.hasAttribute(c.getPhone().getPhoneTTYTDD()) ? user.getAttributeValue(c.getPhone().getPhoneTTYTDD()) : null);
    c.getPhone().setPhoneWork(user.hasAttribute(c.getPhone().getPhoneWork()) ? user.getAttributeValue(c.getPhone().getPhoneWork()) : null);
    c.getPhone().setPhoneWorkMobile(user.hasAttribute(c.getPhone().getPhoneWorkMobile()) ? user.getAttributeValue(c.getPhone().getPhoneWorkMobile()) : null);
    c.getPhone().setPhoneWorkPager(user.hasAttribute(c.getPhone().getPhoneWorkPager()) ? user.getAttributeValue(c.getPhone().getPhoneWorkPager()) : null);
    // relation
    c.getRelation().setRelationAssistant(user.hasAttribute(c.getRelation().getRelationAssistant()) ? user.getAttributeValue(c.getRelation().getRelationAssistant()) : null);
    c.getRelation().setRelationBrother(user.hasAttribute(c.getRelation().getRelationBrother()) ? user.getAttributeValue(c.getRelation().getRelationBrother()) : null);
    c.getRelation().setRelationChild(user.hasAttribute(c.getRelation().getRelationChild()) ? user.getAttributeValue(c.getRelation().getRelationChild()) : null);
    c.getRelation().setRelationDomesticPartner(user.hasAttribute(c.getRelation().getRelationDomesticPartner()) ? user.getAttributeValue(c.getRelation().getRelationDomesticPartner()) : null);
    c.getRelation().setRelationFather(user.hasAttribute(c.getRelation().getRelationFather()) ? user.getAttributeValue(c.getRelation().getRelationFather()) : null);
    c.getRelation().setRelationFriend(user.hasAttribute(c.getRelation().getRelationFriend()) ? user.getAttributeValue(c.getRelation().getRelationFriend()) : null);
    c.getRelation().setRelationManager(user.hasAttribute(c.getRelation().getRelationManager()) ? user.getAttributeValue(c.getRelation().getRelationManager()) : null);
    c.getRelation().setRelationMother(user.hasAttribute(c.getRelation().getRelationMother()) ? user.getAttributeValue(c.getRelation().getRelationMother()) : null);
    c.getRelation().setRelationParent(user.hasAttribute(c.getRelation().getRelationParent()) ? user.getAttributeValue(c.getRelation().getRelationParent()) : null);
    c.getRelation().setRelationPartner(user.hasAttribute(c.getRelation().getRelationPartner()) ? user.getAttributeValue(c.getRelation().getRelationPartner()) : null);
    c.getRelation().setRelationRefferedBy(user.hasAttribute(c.getRelation().getRelationRefferedBy()) ? user.getAttributeValue(c.getRelation().getRelationRefferedBy()) : null);
    c.getRelation().setRelationRelative(user.hasAttribute(c.getRelation().getRelationRelative()) ? user.getAttributeValue(c.getRelation().getRelationRelative()) : null);
    c.getRelation().setRelationSister(user.hasAttribute(c.getRelation().getRelationSister()) ? user.getAttributeValue(c.getRelation().getRelationSister()) : null);
    c.getRelation().setRelationSpouse(user.hasAttribute(c.getRelation().getRelationSpouse()) ? user.getAttributeValue(c.getRelation().getRelationSpouse()) : null);
    // sipaddress
    c.getSipAddress().setHomeSip(user.hasAttribute(c.getSipAddress().getHomeSip()) ? user.getAttributeValue(c.getSipAddress().getHomeSip()) : null);
    c.getSipAddress().setOtherSip(user.hasAttribute(c.getSipAddress().getOtherSip()) ? user.getAttributeValue(c.getSipAddress().getOtherSip()) : null);
    c.getSipAddress().setWorkSip(user.hasAttribute(c.getSipAddress().getWorkSip()) ? user.getAttributeValue(c.getSipAddress().getWorkSip()) : null);
    // structuredname
    c.getStructuredName().setDisplayName(user.hasAttribute(c.getStructuredName().getDisplayName()) ? user.getAttributeValue(c.getStructuredName().getDisplayName()) : null);
    c.getStructuredName().setFamilyName(user.hasAttribute(c.getStructuredName().getFamilyName()) ? user.getAttributeValue(c.getStructuredName().getFamilyName()) : null);
    c.getStructuredName().setGivenName(user.hasAttribute(c.getStructuredName().getGivenName()) ? user.getAttributeValue(c.getStructuredName().getGivenName()) : null);
    c.getStructuredName().setMiddleName(user.hasAttribute(c.getStructuredName().getMiddleName()) ? user.getAttributeValue(c.getStructuredName().getMiddleName()) : null);
    c.getStructuredName().setNamePrefix(user.hasAttribute(c.getStructuredName().getNamePrefix()) ? user.getAttributeValue(c.getStructuredName().getNamePrefix()) : null);
    c.getStructuredName().setNameSuffix(user.hasAttribute(c.getStructuredName().getNameSuffix()) ? user.getAttributeValue(c.getStructuredName().getNameSuffix()) : null);
    c.getStructuredName().setPhoneticFamilyName(user.hasAttribute(c.getStructuredName().getPhoneticFamilyName()) ? user.getAttributeValue(c.getStructuredName().getPhoneticFamilyName()) : null);
    c.getStructuredName().setPhoneticGivenName(user.hasAttribute(c.getStructuredName().getPhoneticGivenName()) ? user.getAttributeValue(c.getStructuredName().getPhoneticGivenName()) : null);
    c.getStructuredName().setPhoneticMiddleName(user.hasAttribute(c.getStructuredName().getPhoneticMiddleName()) ? user.getAttributeValue(c.getStructuredName().getPhoneticMiddleName()) : null);
    // structured postal 
    c.getStructuredPostal().setHomeCity(user.hasAttribute(c.getStructuredPostal().getHomeCity()) ? user.getAttributeValue(c.getStructuredPostal().getHomeCity()) : null);
    c.getStructuredPostal().setHomeCountry(user.hasAttribute(c.getStructuredPostal().getHomeCountry()) ? user.getAttributeValue(c.getStructuredPostal().getHomeCountry()) : null);
    c.getStructuredPostal().setHomeFormattedAddress(user.hasAttribute(c.getStructuredPostal().getHomeFormattedAddress()) ? user.getAttributeValue(c.getStructuredPostal().getHomeFormattedAddress()) : null);
    c.getStructuredPostal().setHomeNeighborhood(user.hasAttribute(c.getStructuredPostal().getHomeNeighborhood()) ? user.getAttributeValue(c.getStructuredPostal().getHomeNeighborhood()) : null);
    c.getStructuredPostal().setHomePOBox(user.hasAttribute(c.getStructuredPostal().getHomePOBox()) ? user.getAttributeValue(c.getStructuredPostal().getHomePOBox()) : null);
    c.getStructuredPostal().setHomePostalCode(user.hasAttribute(c.getStructuredPostal().getHomePostalCode()) ? user.getAttributeValue(c.getStructuredPostal().getHomePostalCode()) : null);
    c.getStructuredPostal().setHomeRegion(user.hasAttribute(c.getStructuredPostal().getHomeRegion()) ? user.getAttributeValue(c.getStructuredPostal().getHomeRegion()) : null);
    c.getStructuredPostal().setHomeStreet(user.hasAttribute(c.getStructuredPostal().getHomeStreet()) ? user.getAttributeValue(c.getStructuredPostal().getHomeStreet()) : null);
    c.getStructuredPostal().setWorkCity(user.hasAttribute(c.getStructuredPostal().getWorkCity()) ? user.getAttributeValue(c.getStructuredPostal().getWorkCity()) : null);
    c.getStructuredPostal().setWorkCountry(user.hasAttribute(c.getStructuredPostal().getWorkCountry()) ? user.getAttributeValue(c.getStructuredPostal().getWorkCountry()) : null);
    c.getStructuredPostal().setWorkFormattedAddress(user.hasAttribute(c.getStructuredPostal().getWorkFormattedAddress()) ? user.getAttributeValue(c.getStructuredPostal().getWorkFormattedAddress()) : null);
    c.getStructuredPostal().setWorkNeighborhood(user.hasAttribute(c.getStructuredPostal().getWorkNeighborhood()) ? user.getAttributeValue(c.getStructuredPostal().getWorkNeighborhood()) : null);
    c.getStructuredPostal().setWorkPOBox(user.hasAttribute(c.getStructuredPostal().getWorkPOBox()) ? user.getAttributeValue(c.getStructuredPostal().getWorkPOBox()) : null);
    c.getStructuredPostal().setWorkPostalCode(user.hasAttribute(c.getStructuredPostal().getWorkPostalCode()) ? user.getAttributeValue(c.getStructuredPostal().getWorkPostalCode()) : null);
    c.getStructuredPostal().setWorkRegion(user.hasAttribute(c.getStructuredPostal().getWorkRegion()) ? user.getAttributeValue(c.getStructuredPostal().getWorkRegion()) : null);
    c.getStructuredPostal().setWorkStreet(user.hasAttribute(c.getStructuredPostal().getWorkStreet()) ? user.getAttributeValue(c.getStructuredPostal().getWorkStreet()) : null);
    c.getStructuredPostal().setOtherCity(user.hasAttribute(c.getStructuredPostal().getOtherCity()) ? user.getAttributeValue(c.getStructuredPostal().getOtherCity()) : null);
    c.getStructuredPostal().setOtherCountry(user.hasAttribute(c.getStructuredPostal().getOtherCountry()) ? user.getAttributeValue(c.getStructuredPostal().getOtherCountry()) : null);
    c.getStructuredPostal().setOtherFormattedAddress(user.hasAttribute(c.getStructuredPostal().getOtherFormattedAddress()) ? user.getAttributeValue(c.getStructuredPostal().getOtherFormattedAddress()) : null);
    c.getStructuredPostal().setOtherNeighborhood(user.hasAttribute(c.getStructuredPostal().getOtherNeighborhood()) ? user.getAttributeValue(c.getStructuredPostal().getOtherNeighborhood()) : null);
    c.getStructuredPostal().setOtherPOBox(user.hasAttribute(c.getStructuredPostal().getOtherPOBox()) ? user.getAttributeValue(c.getStructuredPostal().getOtherPOBox()) : null);
    c.getStructuredPostal().setOtherPostalCode(user.hasAttribute(c.getStructuredPostal().getOtherPostalCode()) ? user.getAttributeValue(c.getStructuredPostal().getOtherPostalCode()) : null);
    c.getStructuredPostal().setOtherRegion(user.hasAttribute(c.getStructuredPostal().getOtherRegion()) ? user.getAttributeValue(c.getStructuredPostal().getOtherRegion()) : null);
    c.getStructuredPostal().setOtherStreet(user.hasAttribute(c.getStructuredPostal().getOtherStreet()) ? user.getAttributeValue(c.getStructuredPostal().getOtherStreet()) : null);
    // website
    c.getWebsite().setWebsiteBlog(user.hasAttribute(c.getWebsite().getWebsiteBlog()) ? user.getAttributeValue(c.getWebsite().getWebsiteBlog()) : null);
    c.getWebsite().setWebsiteFtp(user.hasAttribute(c.getWebsite().getWebsiteFtp()) ? user.getAttributeValue(c.getWebsite().getWebsiteFtp()) : null);
    c.getWebsite().setWebsiteHome(user.hasAttribute(c.getWebsite().getWebsiteHome()) ? user.getAttributeValue(c.getWebsite().getWebsiteHome()) : null);
    c.getWebsite().setWebsiteHomepage(user.hasAttribute(c.getWebsite().getWebsiteHomepage()) ? user.getAttributeValue(c.getWebsite().getWebsiteHomepage()) : null);
    c.getWebsite().setWebsiteOther(user.hasAttribute(c.getWebsite().getWebsiteOther()) ? user.getAttributeValue(c.getWebsite().getWebsiteOther()) : null);
    c.getWebsite().setWebsiteProfile(user.hasAttribute(c.getWebsite().getWebsiteProfile()) ? user.getAttributeValue(c.getWebsite().getWebsiteProfile()) : null);
    c.getWebsite().setWebsiteWork(user.hasAttribute(c.getWebsite().getWebsiteWork()) ? user.getAttributeValue(c.getWebsite().getWebsiteWork()) : null);
    
    return c;
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
       /*} else if (protocol == Im.PROTOCOL_AIM) {
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
         attributes.add(new Attribute(Constants.IM_NULL_YAHOO, cursor.getString(cursor.getColumnIndex(Data.DATA1))));*/
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
       //attributes.add(new Attribute(Constants.NOTES, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
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
     Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
     if (type == Website.TYPE_CUSTOM) {
       //TODO:String  LABEL DATA3,TYPE_CUSTOM. Put the actual type in LABEL. 
     } else if (type == Website.TYPE_BLOG) {
       attributes.add(new Attribute(Constants.WEBSITE_BLOG, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Website.TYPE_FTP) {
       attributes.add(new Attribute(Constants.WEBSITE_FTP, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Website.TYPE_HOME) {
       attributes.add(new Attribute(Constants.WEBSITE_HOME, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Website.TYPE_HOMEPAGE) {
       attributes.add(new Attribute(Constants.WEBSITE_HOMEPAGE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Website.TYPE_OTHER) {
       //attributes.add(new Attribute(Constants.WEBSITE_OTHER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Website.TYPE_PROFILE) {
       attributes.add(new Attribute(Constants.WEBSITE_PROFILE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Website.TYPE_WORK) {
       attributes.add(new Attribute(Constants.WEBSITE_WORK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else {
       Log.i("NOT SUPPORTED TYPE WEBSITE", "NOT SUPPORTED TYPE WEBSITE");
     }
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
     Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
     if (type == Relation.TYPE_CUSTOM) {
       // TODO:
       //attributes.add(new Attribute(Constants.EVENT_ANNIVERSARY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
       //String  LABEL DATA3
     } else if (type == Relation.TYPE_ASSISTANT) {
       attributes.add(new Attribute(Constants.RELATION_ASSISTANT, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_BROTHER) {
       attributes.add(new Attribute(Constants.RELATION_BROTHER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_CHILD) {
       attributes.add(new Attribute(Constants.RELATION_CHILD, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_DOMESTIC_PARTNER) {
       attributes.add(new Attribute(Constants.RELATION_DOMESTIC_PARTNER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_FATHER) {
       attributes.add(new Attribute(Constants.RELATION_FATHER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_FRIEND) {
       attributes.add(new Attribute(Constants.RELATION_FRIEND, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_MANAGER) {
       attributes.add(new Attribute(Constants.RELATION_MANAGER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_MOTHER) {
       attributes.add(new Attribute(Constants.RELATION_MOTHER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_PARENT) {
       attributes.add(new Attribute(Constants.RELATION_PARENT, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_PARTNER) {
       attributes.add(new Attribute(Constants.RELATION_PARTNER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_REFERRED_BY) {
       attributes.add(new Attribute(Constants.RELATION_REFFERED_BY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_RELATIVE) {
       attributes.add(new Attribute(Constants.RELATION_RELATIVE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_SISTER) {
       attributes.add(new Attribute(Constants.RELATION_SISTER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else if (type == Relation.TYPE_SPOUSE) {
       attributes.add(new Attribute(Constants.RELATION_SPOUSE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     } else {
       Log.i("NOT SUPPORTED TYPE Relation", "NOT SUPPORTED TYPE Relation");
     }
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
     //attributes.add(new Attribute(Constants.IDENTITY_TEXT, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
     //attributes.add(new Attribute(Constants.IDENTITY_NAMESPACE, cursor.getString(cursor.getColumnIndex(Data.DATA2))));
   } else {
     Log.i("NOT SUPPORTED TYPE MIME", "NOT SUPPORTED TYPE MIME");
   }
   
   return attributes;
  }
  
  private static void fillContact(Cursor cursor, GoogleContact contact) {
    
    String str = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE));
    
    if (str.equals(StructuredName.CONTENT_ITEM_TYPE)) {
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
        contact.getStructuredName().setDisplayName(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA2))) {
        contact.getStructuredName().setGivenName(cursor.getString(cursor.getColumnIndex(Data.DATA2)));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA3))) {
        contact.getStructuredName().setFamilyName(cursor.getString(cursor.getColumnIndex(Data.DATA3)));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
        contact.getStructuredName().setNamePrefix(cursor.getString(cursor.getColumnIndex(Data.DATA4)));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
        contact.getStructuredName().setMiddleName(cursor.getString(cursor.getColumnIndex(Data.DATA5)));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
        contact.getStructuredName().setNameSuffix(cursor.getString(cursor.getColumnIndex(Data.DATA6)));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
        contact.getStructuredName().setPhoneticGivenName(cursor.getString(cursor.getColumnIndex(Data.DATA7)));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
        contact.getStructuredName().setPhoneticMiddleName(cursor.getString(cursor.getColumnIndex(Data.DATA8)));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
        contact.getStructuredName().setPhoneticFamilyName(cursor.getString(cursor.getColumnIndex(Data.DATA9)));
      }
    } else if (str.equals(Phone.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Phone.TYPE_CUSTOM) {
        //TYPE_CUSTOM. Put the actual type in LABEL.
        //String  LABEL DATA3
      } else if (type == Phone.TYPE_ASSISTANT) {
        contact.getPhone().setPhoneAssistant(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_CALLBACK) {
        contact.getPhone().setPhoneCallback(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_CAR) {
        contact.getPhone().setPhoneCar(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_COMPANY_MAIN) {
        contact.getPhone().setPhoneCompany(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_FAX_HOME) {
        contact.getPhone().setPhoneFaxHome(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_FAX_WORK) {
        contact.getPhone().setPhoneFaxWork(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_HOME) {
        contact.getPhone().setPhoneHome(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_ISDN) {
        contact.getPhone().setPhoneISDN(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_MAIN) {
        contact.getPhone().setPhoneMain(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_MMS) {
        contact.getPhone().setPhoneMMS(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_MOBILE) {
        contact.getPhone().setPhoneMobile(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_OTHER) {
        contact.getPhone().setPhoneOther(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_OTHER_FAX) {
        contact.getPhone().setPhoneOtherFax(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_PAGER) {
        contact.getPhone().setPhonePager(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_RADIO) {
        contact.getPhone().setPhoneRadio(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_TELEX) {
        contact.getPhone().setPhoneTelex(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_TTY_TDD) {
        contact.getPhone().setPhoneTTYTDD(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_WORK) {
        contact.getPhone().setPhoneWork(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_WORK_MOBILE) {
        contact.getPhone().setPhoneWorkMobile(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Phone.TYPE_WORK_PAGER) {
        contact.getPhone().setPhoneWorkPager(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else {
        Log.i("NOT SUPPORTED TYPE PHONE", "NOT SUPPORTED TYPE PHONE");
      }
    } else if (str.equals(Email.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Email.TYPE_HOME) {
        contact.getEmail().setHomeMail(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Email.TYPE_WORK) {
        contact.getEmail().setWorkMail(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Email.TYPE_OTHER) {
        contact.getEmail().setOtherMail(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Email.TYPE_MOBILE) {
        contact.getEmail().setMobileMail(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
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
          contact.getOrganization().setOrganizationWorkCompany(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
          contact.getOrganization().setOrganizationWorkTitle(cursor.getString(cursor.getColumnIndex(Data.DATA4)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
          contact.getOrganization().setOrganizationWorkDepartment(cursor.getString(cursor.getColumnIndex(Data.DATA5)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
          contact.getOrganization().setOrganizationWorkJobDescription(cursor.getString(cursor.getColumnIndex(Data.DATA6)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
          contact.getOrganization().setOrganizationWorkSymbol(cursor.getString(cursor.getColumnIndex(Data.DATA7)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
          contact.getOrganization().setOrganizationWorkPhoneticName(cursor.getString(cursor.getColumnIndex(Data.DATA8)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
          contact.getOrganization().setOrganizationWorkOfficeLocation(cursor.getString(cursor.getColumnIndex(Data.DATA9)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
          contact.getOrganization().setOrganizationWorkPhoneticNameStyle(cursor.getString(cursor.getColumnIndex(Data.DATA10)));
        }
      } else if (type == Organization.TYPE_OTHER) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
          contact.getOrganization().setOrganizationOtherCompany(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
          contact.getOrganization().setOrganizationOtherTitle(cursor.getString(cursor.getColumnIndex(Data.DATA4)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
          contact.getOrganization().setOrganizationOtherDepartment(cursor.getString(cursor.getColumnIndex(Data.DATA5)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
          contact.getOrganization().setOrganizationOtherJobDescription(cursor.getString(cursor.getColumnIndex(Data.DATA6)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
          contact.getOrganization().setOrganizationOtherSymbol(cursor.getString(cursor.getColumnIndex(Data.DATA7)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
          contact.getOrganization().setOrganizationOtherPhoneticName(cursor.getString(cursor.getColumnIndex(Data.DATA8)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
          contact.getOrganization().setOrganizationOtherOfficeLocation(cursor.getString(cursor.getColumnIndex(Data.DATA9)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
          contact.getOrganization().setOrganizationOtherPhoneticNameStyle(cursor.getString(cursor.getColumnIndex(Data.DATA10)));
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
          contact.getIm().setImHomeAim(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
          contact.getIm().setImHomeGoogleTalk(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_ICQ) {
          contact.getIm().setImHomeIcq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_JABBER) {
          contact.getIm().setImHomeJabber(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_MSN) {
          contact.getIm().setImHomeMsn(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_NETMEETING) {
          contact.getIm().setImHomeNetmeeting(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_QQ) {
          contact.getIm().setImHomeQq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_SKYPE) {
          contact.getIm().setImHomeSkype(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_YAHOO) {
          contact.getIm().setImHomeYahoo(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else if (type == Im.TYPE_WORK) {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String  CUSTOM_PROTOCOL DATA6
       // TODO:
        } else if (protocol == Im.PROTOCOL_AIM) {
          contact.getIm().setImWorkAim(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
          contact.getIm().setImWorkGoogleTalk(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_ICQ) {
          contact.getIm().setImWorkIcq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_JABBER) {
          contact.getIm().setImWorkJabber(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_MSN) {
          contact.getIm().setImWorkMsn(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_NETMEETING) {
          contact.getIm().setImWorkNetmeeting(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_QQ) {
          contact.getIm().setImWorkQq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_SKYPE) {
          contact.getIm().setImWorkSkype(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_YAHOO) {
          contact.getIm().setImWorkYahoo(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else if (type == Im.TYPE_OTHER) {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String  CUSTOM_PROTOCOL DATA6
       // TODO:
        } else if (protocol == Im.PROTOCOL_AIM) {
          contact.getIm().setImOtherAim(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
          contact.getIm().setImOtherGoogleTalk(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_ICQ) {
          contact.getIm().setImOtherIcq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_JABBER) {
          contact.getIm().setImOtherJabber(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_MSN) {
          contact.getIm().setImOtherMsn(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_NETMEETING) {
          contact.getIm().setImOtherNetmeeting(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_QQ) {
          contact.getIm().setImOtherQq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_SKYPE) {
          contact.getIm().setImOtherSkype(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_YAHOO) {
          contact.getIm().setImOtherYahoo(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String  CUSTOM_PROTOCOL DATA6
       // TODO:
        /*} else if (protocol == Im.PROTOCOL_AIM) {
          contact.getIm().setImNullAim(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
          contact.getIm().setImNullGoogleTalk(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_ICQ) {
          contact.getIm().setImNullIcq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_JABBER) {
          contact.getIm().setImNullJabber(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_MSN) {
          contact.getIm().setImNullMsn(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_NETMEETING) {
          contact.getIm().setImNullNetmeeting(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_QQ) {
          contact.getIm().setImNullQq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_SKYPE) {
          contact.getIm().setImNullSkype(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_YAHOO) {
          contact.getIm().setImNullYahoo(cursor.getString(cursor.getColumnIndex(Data.DATA1)));*/
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      }
    } else if (str.equals(Nickname.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Nickname.TYPE_DEFAULT) {
        contact.getNickname().setNicknameDefault(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Nickname.TYPE_OTHER_NAME) {
        contact.getNickname().setNicknameOther(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Nickname.TYPE_MAIDEN_NAME) {
        contact.getNickname().setNicknameMaiden(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Nickname.TYPE_SHORT_NAME) {
        contact.getNickname().setNicknameShort(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Nickname.TYPE_INITIALS) {
        contact.getNickname().setNicknameInitials(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Nickname.TYPE_CUSTOM) {
        // TODO: String  LABEL DATA3
      } else {
        Log.i("NOT SUPPORTED TYPE NICKNAME", "NOT SUPPORTED TYPE NICKANEME");
      }
    } else if (str.equals(Note.CONTENT_ITEM_TYPE)) {
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
        contact.getNote().setNotes(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      }
    } else if (str.equals(StructuredPostal.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == StructuredPostal.TYPE_CUSTOM) {
        // TODO:
        //TYPE_CUSTOM. Put the actual type in LABEL.
        // String  LABEL DATA3
      } else if (type == StructuredPostal.TYPE_HOME) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
          contact.getStructuredPostal().setHomeFormattedAddress(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
          contact.getStructuredPostal().setHomeStreet(cursor.getString(cursor.getColumnIndex(Data.DATA4)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
          contact.getStructuredPostal().setHomePOBox(cursor.getString(cursor.getColumnIndex(Data.DATA5)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
          contact.getStructuredPostal().setHomeNeighborhood(cursor.getString(cursor.getColumnIndex(Data.DATA6)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
          contact.getStructuredPostal().setHomeCity(cursor.getString(cursor.getColumnIndex(Data.DATA7)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
          contact.getStructuredPostal().setHomeRegion(cursor.getString(cursor.getColumnIndex(Data.DATA8)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
          contact.getStructuredPostal().setHomePostalCode(cursor.getString(cursor.getColumnIndex(Data.DATA9)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
          contact.getStructuredPostal().setHomeCountry(cursor.getString(cursor.getColumnIndex(Data.DATA10)));
        }
      } else if (type == StructuredPostal.TYPE_WORK) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
          contact.getStructuredPostal().setWorkFormattedAddress(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
          contact.getStructuredPostal().setWorkStreet(cursor.getString(cursor.getColumnIndex(Data.DATA4)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
          contact.getStructuredPostal().setWorkPOBox(cursor.getString(cursor.getColumnIndex(Data.DATA5)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
          contact.getStructuredPostal().setWorkNeighborhood(cursor.getString(cursor.getColumnIndex(Data.DATA6)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
          contact.getStructuredPostal().setWorkCity(cursor.getString(cursor.getColumnIndex(Data.DATA7)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
          contact.getStructuredPostal().setWorkRegion(cursor.getString(cursor.getColumnIndex(Data.DATA8)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
          contact.getStructuredPostal().setWorkPostalCode(cursor.getString(cursor.getColumnIndex(Data.DATA9)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
          contact.getStructuredPostal().setWorkCountry(cursor.getString(cursor.getColumnIndex(Data.DATA10)));
        }
      } else if (type == StructuredPostal.TYPE_OTHER) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
          contact.getStructuredPostal().setOtherFormattedAddress(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
          contact.getStructuredPostal().setOtherStreet(cursor.getString(cursor.getColumnIndex(Data.DATA4)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
          contact.getStructuredPostal().setOtherPOBox(cursor.getString(cursor.getColumnIndex(Data.DATA5)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
          contact.getStructuredPostal().setOtherNeighborhood(cursor.getString(cursor.getColumnIndex(Data.DATA6)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
          contact.getStructuredPostal().setOtherCity(cursor.getString(cursor.getColumnIndex(Data.DATA7)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
          contact.getStructuredPostal().setOtherRegion(cursor.getString(cursor.getColumnIndex(Data.DATA8)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
          contact.getStructuredPostal().setOtherPostalCode(cursor.getString(cursor.getColumnIndex(Data.DATA9)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
          contact.getStructuredPostal().setOtherCountry(cursor.getString(cursor.getColumnIndex(Data.DATA10)));
        }
      } else {
        
      }
       
    } else if (str.equals(GroupMembership.CONTENT_ITEM_TYPE)) {
      // TODO:
      //long  GROUP_ROW_ID  DATA1
      //attributes.add(new Attribute(Constants., cursor.getString(cursor.getColumnIndex(Data.DATA1))));
    } else if (str.equals(Website.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Website.TYPE_CUSTOM) {
        //TODO:String  LABEL DATA3,TYPE_CUSTOM. Put the actual type in LABEL. 
      } else if (type == Website.TYPE_BLOG) {
        contact.getWebsite().setWebsiteBlog(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_FTP) {
        contact.getWebsite().setWebsiteFtp(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_HOME) {
        contact.getWebsite().setWebsiteHome(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_HOMEPAGE) {
        contact.getWebsite().setWebsiteHomepage(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_OTHER) {
        contact.getWebsite().setWebsiteOther(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_PROFILE) {
        contact.getWebsite().setWebsiteProfile(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_WORK) {
        contact.getWebsite().setWebsiteWork(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else {
        Log.i("NOT SUPPORTED TYPE WEBSITE", "NOT SUPPORTED TYPE WEBSITE");
      }
    } else if (str.equals(Event.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Event.TYPE_CUSTOM) {
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String  LABEL DATA3
      } else if (type == Event.TYPE_ANNIVERSARY) {
        contact.getEvent().setEventAnniversary(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Event.TYPE_BIRTHDAY) {
        contact.getEvent().setEventBirthday(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Event.TYPE_OTHER) {
        contact.getEvent().setEventOther(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else {
        Log.i("NOT SUPPORTED TYPE Event", "NOT SUPPORTED TYPE EVENT");
      }
    } else if (str.equals(Relation.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Relation.TYPE_CUSTOM) {
        // TODO:
        //attributes.add(new Attribute(Constants.EVENT_ANNIVERSARY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        //String  LABEL DATA3
      } else if (type == Relation.TYPE_ASSISTANT) {
        contact.getRelation().setRelationAssistant(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_BROTHER) {
        contact.getRelation().setRelationBrother(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_CHILD) {
        contact.getRelation().setRelationChild(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_DOMESTIC_PARTNER) {
        contact.getRelation().setRelationDomesticPartner(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_FATHER) {
        contact.getRelation().setRelationFather(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_FRIEND) {
        contact.getRelation().setRelationFriend(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_MANAGER) {
        contact.getRelation().setRelationManager(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_MOTHER) {
        contact.getRelation().setRelationMother(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_PARENT) {
        contact.getRelation().setRelationParent(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_PARTNER) {
        contact.getRelation().setRelationPartner(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_REFERRED_BY) {
        contact.getRelation().setRelationRefferedBy(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_RELATIVE) {
        contact.getRelation().setRelationRelative(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_SISTER) {
        contact.getRelation().setRelationSister(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_SPOUSE) {
        contact.getRelation().setRelationSpouse(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else {
        Log.i("NOT SUPPORTED TYPE Relation", "NOT SUPPORTED TYPE Relation");
      }
    } else if (str.equals(SipAddress.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == SipAddress.TYPE_CUSTOM) {
        // TODO:
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String  LABEL DATA3
      } else if (type == SipAddress.TYPE_HOME) {
        contact.getSipAddress().setHomeSip(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == SipAddress.TYPE_WORK) {
        contact.getSipAddress().setWorkSip(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == SipAddress.TYPE_OTHER) {
        contact.getSipAddress().setOtherSip(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else {
        Log.i("NOT SUPPORTED TYPE SIP", "NOT SUPPORTED TYPE SIP");
      }
    } else if (str.equals(Identity.CONTENT_ITEM_TYPE)) {
      contact.getIdentity().setIdentityText(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      contact.getIdentity().setIdentityNamespace(cursor.getString(cursor.getColumnIndex(Data.DATA2)));
    } else {
      Log.i("NOT SUPPORTED TYPE MIME", "NOT SUPPORTED TYPE MIME: " + str);
    }
  }
}
