package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.EBean;

import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Modification;
import com.unboundid.ldap.sdk.ModificationType;
import com.unboundid.ldap.sdk.ModifyRequest;
import com.unboundid.ldap.sdk.ReadOnlyEntry;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
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
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.contact.EmailSync;
import cz.xsendl00.synccontact.contact.EventSync;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.contact.ID;
import cz.xsendl00.synccontact.contact.IdentitySync;
import cz.xsendl00.synccontact.contact.ImSync;
import cz.xsendl00.synccontact.contact.NicknameSync;
import cz.xsendl00.synccontact.contact.NoteSync;
import cz.xsendl00.synccontact.contact.OrganizationSync;
import cz.xsendl00.synccontact.contact.PhoneSync;
import cz.xsendl00.synccontact.contact.RelationSync;
import cz.xsendl00.synccontact.contact.SipAddressSync;
import cz.xsendl00.synccontact.contact.StructuredNameSync;
import cz.xsendl00.synccontact.contact.StructuredPostalSync;
import cz.xsendl00.synccontact.contact.WebsiteSync;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.database.HelperSQL;

@EBean
public class Mapping {

  private static final String TAG = "Mapping";

  // TODO: nemam tam byt RawContacts._ID + "=? AND " + RawContacts.DIRTY + "=?",
  public static List<String> fetchDirtyContacts(Context context) {
    HelperSQL db = new HelperSQL(context);
    List<String> list = db.getSyncContactsId();
    List<String> dirtyContactsId = new ArrayList<String>();
    for (String id : list) {
      // Log.i(TAG, id);
      Cursor c = context.getContentResolver().query(RawContacts.CONTENT_URI,
          new String[]{RawContacts._ID},
          RawContacts.CONTACT_ID + "=? AND " + RawContacts.DIRTY + "=?",
          new String[]{id.toString(), "1"}, null);
      while (c.moveToNext()) {
        // Log.i(TAG, c.getString(c.getColumnIndex(RawContacts._ID)));
        dirtyContactsId.add(c.getString(c.getColumnIndex(RawContacts._ID)));
      }
      c.close();
    }
    return dirtyContactsId;
  }

  /**
   * Fetch contact selected like modified. In database SYNC = 1;
   *
   * @param contentResolver contentResolver
   * @param list list of synchronization contacts.
   * @return map of UUID and googleContact.
   */
  public Map<String, GoogleContact> fetchDirtyContacts(ContentResolver contentResolver,
      List<ContactRow> list) {
    Map<String, GoogleContact> dirtyContacts = new HashMap<String, GoogleContact>();
    Cursor cursor = null;
    try {
      // get list id_raw of changed contacts
      cursor = contentResolver.query(RawContacts.CONTENT_URI, new String[]{RawContacts._ID},
          RawContacts.DIRTY + "=?", new String[]{"1"}, null);
      List<String> dirtys = new ArrayList<String>();
      while (cursor.moveToNext()) {
        dirtys.add(cursor.getString(cursor.getColumnIndex(RawContacts._ID)));
      }
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }

      for (ContactRow contactRow : list) {
        if (dirtys.contains(contactRow.getId())) {
          GoogleContact googleContact = mappingContactFromDB(contentResolver, contactRow.getId(),
              contactRow.getUuid());
          dirtyContacts.put(contactRow.getUuid(), googleContact);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }
    return dirtyContacts;
  }

  /**
   * Create attribute for mapping data {@link ContactRow} from LDAP server.
   *
   * @return array of name LDAP attributes.
   */
  public static final String[] createAttributesContactSimply() {
    ArrayList<String> ldapAttributes = new ArrayList<String>();
    ldapAttributes.add(Constants.DISPLAY_NAME);
    ldapAttributes.add(Constants.UUID);

    String[] ldapArray = new String[ldapAttributes.size()];
    ldapArray = ldapAttributes.toArray(ldapArray);
    return ldapArray;
  }

  /**
   * Create attribute for mapping data {@link GroupRow} from LDAP server.
   *
   * @return array of name LDAP attributes.
   */
  public static final String[] createAttributesGroup() {
    ArrayList<String> ldapAttributes = new ArrayList<String>();
    ldapAttributes.add(Constants.DESCRIPTION);
    ldapAttributes.add(Constants.CN);
    ldapAttributes.add(Constants.OBJECT_ATTRIBUTE_MEMBER);

    String[] ldapArray = new String[ldapAttributes.size()];
    ldapArray = ldapAttributes.toArray(ldapArray);
    return ldapArray;
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
    ldapAttributes.add(Constants.IM_HOME_AIM);
    ldapAttributes.add(Constants.IM_HOME_GOOGLE_TALK);
    ldapAttributes.add(Constants.IM_HOME_ICQ);
    ldapAttributes.add(Constants.IM_HOME_JABBER);
    ldapAttributes.add(Constants.IM_HOME_MSN);
    ldapAttributes.add(Constants.IM_HOME_NETMEETING);
    ldapAttributes.add(Constants.IM_HOME_QQ);
    ldapAttributes.add(Constants.IM_HOME_SKYPE);
    ldapAttributes.add(Constants.IM_HOME_YAHOO);
    ldapAttributes.add(Constants.IM_WORK_AIM);
    ldapAttributes.add(Constants.IM_WORK_GOOGLE_TALK);
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
    ldapAttributes.add(Constants.IM_OTHER_MSN);
    ldapAttributes.add(Constants.IM_OTHER_NETMEETING);
    ldapAttributes.add(Constants.IM_OTHER_QQ);
    ldapAttributes.add(Constants.IM_OTHER_SKYPE);
    ldapAttributes.add(Constants.IM_OTHER_YAHOO);
    /*
     * ldapAttributes.add(Constants.IM_NULL_AIM); ldapAttributes.add(Constants.IM_NULL_GOOGLE_TALK);
     * ldapAttributes.add(Constants.IM_NULL_ICQ); ldapAttributes.add(Constants.IM_NULL_JABBER);
     * ldapAttributes.add(Constants.IM_NULL_MSN); ldapAttributes.add(Constants.IM_NULL_NETMEETING);
     * ldapAttributes.add(Constants.IM_NULL_QQ); ldapAttributes.add(Constants.IM_NULL_SKYPE);
     * ldapAttributes.add(Constants.IM_NULL_YAHOO);
     */
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
    ldapAttributes.add(Constants.ORGANIZATION_WORK_DEPARTMENT);
    ldapAttributes.add(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION);
    ldapAttributes.add(Constants.ORGANIZATION_WORK_SYMBOL);
    ldapAttributes.add(Constants.ORGANIZATION_WORK_PHONETIC_NAME);
    ldapAttributes.add(Constants.ORGANIZATION_WORK_OFFICE_LOCATION);
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
    ldapAttributes.add(Constants.IDENTITY_NAMESPACE);
    ldapAttributes.add(Constants.RELATION_ASSISTANT);
    ldapAttributes.add(Constants.RELATION_BROTHER);
    ldapAttributes.add(Constants.RELATION_CHILD);
    ldapAttributes.add(Constants.RELATION_DOMESTIC_PARTNER);
    ldapAttributes.add(Constants.RELATION_FATHER);
    ldapAttributes.add(Constants.RELATION_FRIEND);
    ldapAttributes.add(Constants.RELATION_MANAGER);
    ldapAttributes.add(Constants.RELATION_MOTHER);
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
    ldapAttributes.add(Constants.HOME_FORMATTED_ADDRESS);
    ldapAttributes.add(Constants.WORK_NEIGHBORHOOD);
    ldapAttributes.add(Constants.HOME_NEIGHBORHOOD);
    ldapAttributes.add(Constants.OTHER_NEIGHBORHOOD);
    ldapAttributes.add(Constants.OTHER_STREET);
    ldapAttributes.add(Constants.OTHER_CITY);
    ldapAttributes.add(Constants.OTHER_POBOX);
    ldapAttributes.add(Constants.OTHER_REGION);
    ldapAttributes.add(Constants.OTHER_POSTAL_CODE);
    ldapAttributes.add(Constants.OTHER_COUNTRY);
    ldapAttributes.add(Constants.OTHER_FORMATTED_ADDRESS);
    ldapAttributes.add(Constants.UUID);

    String[] ldapArray = new String[ldapAttributes.size()];
    ldapArray = ldapAttributes.toArray(ldapArray);
    return ldapArray;
  }

  public static AddRequest mappingRequest(ContentResolver cr, String id, String baseDn, String rdn) {
    Cursor cursor = new ContactDetail().fetchAllDataOfContact(cr, id);
    ArrayList<Attribute> attributes = new ArrayList<Attribute>();

    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_GOOGLE));
    attributes.add(new Attribute(Constants.UUID, rdn));


    while (cursor.moveToNext()) {
      attributes.addAll(fill(cursor));
    }
    cursor.close();
    AddRequest addRequest = new AddRequest(Constants.UUID + "=" + rdn + ","
        + Constants.ACCOUNT_OU_PEOPLE + baseDn, attributes);

    // (final String dn, final List<Modification> mods)
    Log.i(TAG, addRequest.toLDIFString());
    return addRequest;
  }

  /**
   * @param gc
   * @param baseDn
   * @return
   */
  public AddRequest mappingAddRequest(GoogleContact gc, String baseDn) {

    ArrayList<Attribute> attributes = new ArrayList<Attribute>();

    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_GOOGLE));
    attributes.add(new Attribute(Constants.UUID, gc.getUuid()));

    ArrayList<Attribute> attributesTemp = fillAttribute(gc);
    if (attributesTemp != null && !attributesTemp.isEmpty()) {
      attributes.addAll(attributesTemp);
    }
    // TODO:predtim 2
    if (attributes != null && attributes.size() > 1) {
      AddRequest addRequest = new AddRequest(Constants.UUID + "=" + gc.getUuid().toString() + ","
          + Constants.ACCOUNT_OU_PEOPLE + baseDn, attributes);
      // Log.i(TAG, "AddRequest : " + addRequest.toLDIFString());
      return addRequest;
    } else {
      return null;
    }
  }

  /**
   * Create Add request for group.
   *
   * @param groupRow group
   * @param baseDn base DN
   * @param context context
   * @return list of add request.
   */
  public AddRequest createGroupAddRequest(final GroupRow groupRow,
      final String baseDn,
      final Context context,
      List<Modification> mod) {
    Log.i(TAG, "createGroupAddRequest START");
    ArrayList<Attribute> attributes = new ArrayList<Attribute>();

    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_GROUP_OF_NAME));
    attributes.add(new Attribute(Constants.CN, groupRow.getUuid()));


    ArrayList<Attribute> attributesTemp = fillAttribute(groupRow, baseDn, context, mod);


    if (attributesTemp != null && !attributesTemp.isEmpty()) {
      attributes.addAll(attributesTemp);
    }
    if (attributes != null && attributes.size() > 0 && attributesTemp != null) {
      AddRequest addRequest = new AddRequest(Constants.CN + "=" + groupRow.getUuid() + ","
          + Constants.ACCOUNT_OU_GROUPS + baseDn, attributes);
      Log.i(TAG, "AddRequest : " + addRequest.toLDIFString());
      return addRequest;
    } else {
      return null;
    }
  }

  public static ModifyRequest mappingRequest(GoogleContact gc, String baseDn) {
    List<Modification> mod;
    mod = fillModification(gc);
    if (mod != null && mod.size() > 0) {
      ModifyRequest modifyRequest = new ModifyRequest(Constants.UUID + "="
          + gc.getUuid().toString() + "," + Constants.ACCOUNT_OU_PEOPLE + baseDn, mod);
      // Log.i(TAG, "ModifyRequest : " + modifyRequest.toLDIFString());
      return modifyRequest;
    } else {
      return null;
    }
  }

  /**
   * Create group LDAP request. Get all member of group and create attributes member.
   *
   * @param groupRow group
   * @return list of request
   */
  private static ArrayList<Attribute> fillAttribute(final GroupRow groupRow,
      final String baseDn,
      Context context,
      List<Modification> mod) {
    ArrayList<Attribute> attributes = new ArrayList<Attribute>();

    ContactManager contactManager = ContactManager.getInstance(context);
    Map<String, List<ContactRow>> map = contactManager.getGroupsContacts();
    List<ContactRow> list = map.get(groupRow.getId());
    if (list.isEmpty()) {
      return null;
    }
    boolean first = true;
    for (ContactRow contactRow : list) {
      if (first) {
        attributes.add(new Attribute(Constants.GROUP_MEMBER, Constants.UUID + "="
            + contactRow.getUuid() + "," + Constants.ACCOUNT_OU_PEOPLE + baseDn));
        first = false;
      } else {
        mod.add(new Modification(ModificationType.ADD, Constants.GROUP_MEMBER, Constants.UUID + "="
            + contactRow.getUuid() + "," + Constants.ACCOUNT_OU_PEOPLE + baseDn));
      }
    }
    attributes.add(new Attribute(Constants.GROUP_DESCRIPTION, groupRow.getName()));
    return attributes;
  }

  private static ArrayList<Attribute> fillAttribute(GoogleContact gc) {

    ArrayList<Attribute> attributes = new ArrayList<Attribute>();

    // Log.i(TAG, "fillAttribute : " + gc.toString());

    EmailSync e = gc.getEmail();
    if (e != null) {
      if (e.getHomeMail() != null) {
        attributes.add(new Attribute(Constants.HOME_MAIL, e.getHomeMail()));
      }
      if (e.getMobileMail() != null) {
        attributes.add(new Attribute(Constants.MOBILE_MAIL, e.getMobileMail()));
      }
      if (e.getOtherMail() != null) {
        attributes.add(new Attribute(Constants.OTHER_MAIL, e.getOtherMail()));
      }
      if (e.getWorkMail() != null) {
        attributes.add(new Attribute(Constants.WORK_MAIL, e.getWorkMail()));
      }
    }

    EventSync ev = gc.getEvent();
    if (ev != null) {
      if (ev.getEventAnniversary() != null) {
        attributes.add(new Attribute(Constants.EVENT_ANNIVERSARY, ev.getEventAnniversary()));
      }
      if (ev.getEventBirthday() != null) {
        attributes.add(new Attribute(Constants.EVENT_BIRTHDAY, ev.getEventBirthday()));
      }
      if (ev.getEventOther() != null) {
        attributes.add(new Attribute(Constants.EVENT_OTHER, ev.getEventOther()));
      }
    }

    IdentitySync id = gc.getIdentity();
    if (id != null) {
      if (id.getIdentityNamespace() != null) {
        attributes.add(new Attribute(Constants.IDENTITY_NAMESPACE, id.getIdentityNamespace()));
      }
      if (id.getIdentityText() != null) {
        attributes.add(new Attribute(Constants.IDENTITY_TEXT, id.getIdentityText()));
      }
    }

    ImSync im = gc.getImSync();
    if (im != null) {
      if (im.getImHomeAim() != null) {
        attributes.add(new Attribute(Constants.IM_HOME_AIM, im.getImHomeAim()));
      }
      if (im.getImHomeGoogleTalk() != null) {
        attributes.add(new Attribute(Constants.IM_HOME_GOOGLE_TALK, im.getImHomeGoogleTalk()));
      }
      if (im.getImHomeIcq() != null) {
        attributes.add(new Attribute(Constants.IM_HOME_ICQ, im.getImHomeIcq()));
      }
      if (im.getImHomeJabber() != null) {
        attributes.add(new Attribute(Constants.IM_HOME_JABBER, im.getImHomeJabber()));
      }
      if (im.getImHomeMsn() != null) {
        attributes.add(new Attribute(Constants.IM_HOME_MSN, im.getImHomeMsn()));
      }
      if (im.getImHomeNetmeeting() != null) {
        attributes.add(new Attribute(Constants.IM_HOME_NETMEETING, im.getImHomeNetmeeting()));
      }
      if (im.getImHomeQq() != null) {
        attributes.add(new Attribute(Constants.IM_HOME_QQ, im.getImHomeQq()));
      }
      if (im.getImHomeSkype() != null) {
        attributes.add(new Attribute(Constants.IM_HOME_SKYPE, im.getImHomeSkype()));
      }
      if (im.getImHomeYahoo() != null) {
        attributes.add(new Attribute(Constants.IM_HOME_YAHOO, im.getImHomeYahoo()));
      }

      if (im.getImWorkAim() != null) {
        attributes.add(new Attribute(Constants.IM_WORK_AIM, im.getImWorkAim()));
      }
      if (im.getImWorkGoogleTalk() != null) {
        attributes.add(new Attribute(Constants.IM_WORK_GOOGLE_TALK, im.getImWorkGoogleTalk()));
      }
      if (im.getImWorkIcq() != null) {
        attributes.add(new Attribute(Constants.IM_WORK_ICQ, im.getImWorkIcq()));
      }
      if (im.getImWorkJabber() != null) {
        attributes.add(new Attribute(Constants.IM_WORK_JABBER, im.getImWorkJabber()));
      }
      if (im.getImWorkMsn() != null) {
        attributes.add(new Attribute(Constants.IM_WORK_MSN, im.getImWorkMsn()));
      }
      if (im.getImWorkNetmeeting() != null) {
        attributes.add(new Attribute(Constants.IM_WORK_NETMEETING, im.getImWorkNetmeeting()));
      }
      if (im.getImWorkQq() != null) {
        attributes.add(new Attribute(Constants.IM_WORK_QQ, im.getImWorkQq()));
      }
      if (im.getImWorkSkype() != null) {
        attributes.add(new Attribute(Constants.IM_WORK_SKYPE, im.getImWorkSkype()));
      }
      if (im.getImWorkYahoo() != null) {
        attributes.add(new Attribute(Constants.IM_WORK_YAHOO, im.getImWorkYahoo()));
      }

      if (im.getImOtherAim() != null) {
        attributes.add(new Attribute(Constants.IM_OTHER_AIM, im.getImOtherAim()));
      }
      if (im.getImOtherGoogleTalk() != null) {
        attributes.add(new Attribute(Constants.IM_OTHER_GOOGLE_TALK, im.getImOtherGoogleTalk()));
      }
      if (im.getImOtherIcq() != null) {
        attributes.add(new Attribute(Constants.IM_OTHER_ICQ, im.getImOtherIcq()));
      }
      if (im.getImOtherJabber() != null) {
        attributes.add(new Attribute(Constants.IM_OTHER_JABBER, im.getImOtherJabber()));
      }
      if (im.getImOtherMsn() != null) {
        attributes.add(new Attribute(Constants.IM_OTHER_MSN, im.getImOtherMsn()));
      }
      if (im.getImOtherNetmeeting() != null) {
        attributes.add(new Attribute(Constants.IM_OTHER_NETMEETING, im.getImOtherNetmeeting()));
      }
      if (im.getImOtherQq() != null) {
        attributes.add(new Attribute(Constants.IM_OTHER_QQ, im.getImOtherQq()));
      }
      if (im.getImOtherSkype() != null) {
        attributes.add(new Attribute(Constants.IM_OTHER_SKYPE, im.getImOtherSkype()));
      }
      if (im.getImOtherYahoo() != null) {
        attributes.add(new Attribute(Constants.IM_OTHER_YAHOO, im.getImOtherYahoo()));
      }
    }

    NicknameSync ni = gc.getNickname();
    if (ni != null) {
      if (ni.getNicknameDefault() != null) {
        attributes.add(new Attribute(Constants.NICKNAME_DEFAULT, ni.getNicknameDefault()));
      }
      if (ni.getNicknameInitials() != null) {
        attributes.add(new Attribute(Constants.NICKNAME_INITIALS, ni.getNicknameInitials()));
      }
      if (ni.getNicknameMaiden() != null) {
        attributes.add(new Attribute(Constants.NICKNAME_MAIDEN, ni.getNicknameMaiden()));
      }
      if (ni.getNicknameOther() != null) {
        attributes.add(new Attribute(Constants.NICKNAME_OTHER, ni.getNicknameOther()));
      }
      if (ni.getNicknameShort() != null) {
        attributes.add(new Attribute(Constants.NICKNAME_SHORT, ni.getNicknameShort()));
      }
    }

    NoteSync noteSync = gc.getNote();
    if (noteSync != null) {
      if (noteSync.getNotes() != null) {
        attributes.add(new Attribute(Constants.NOTES, noteSync.getNotes()));
      }
    }

    OrganizationSync or = gc.getOrganization();
    if (or != null) {
      if (or.getOrganizationOtherCompany() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_COMPANY,
            or.getOrganizationOtherCompany()));
      }
      if (or.getOrganizationOtherDepartment() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_DEPARTMENT,
            or.getOrganizationOtherDepartment()));
      }
      if (or.getOrganizationOtherJobDescription() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION,
            or.getOrganizationOtherJobDescription()));
      }
      if (or.getOrganizationOtherOfficeLocation() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION,
            or.getOrganizationOtherOfficeLocation()));
      }
      if (or.getOrganizationOtherPhoneticName() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_PHONETIC_NAME,
            or.getOrganizationOtherPhoneticName()));
      }
      if (or.getOrganizationOtherPhoneticNameStyle() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE,
            or.getOrganizationOtherPhoneticNameStyle()));
      }
      if (or.getOrganizationOtherSymbol() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_SYMBOL,
            or.getOrganizationOtherSymbol()));
      }
      if (or.getOrganizationOtherTitle() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_TITLE,
            or.getOrganizationOtherTitle()));
      }

      if (or.getOrganizationWorkCompany() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_WORK_COMPANY,
            or.getOrganizationWorkCompany()));
      }
      if (or.getOrganizationWorkDepartment() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_WORK_DEPARTMENT,
            or.getOrganizationWorkDepartment()));
      }
      if (or.getOrganizationWorkJobDescription() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION,
            or.getOrganizationWorkJobDescription()));
      }
      if (or.getOrganizationWorkOfficeLocation() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_WORK_OFFICE_LOCATION,
            or.getOrganizationWorkOfficeLocation()));
      }
      if (or.getOrganizationWorkPhoneticName() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_WORK_PHONETIC_NAME,
            or.getOrganizationWorkPhoneticName()));
      }
      if (or.getOrganizationWorkPhoneticNameStyle() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE,
            or.getOrganizationWorkPhoneticNameStyle()));
      }
      if (or.getOrganizationWorkSymbol() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_WORK_SYMBOL,
            or.getOrganizationWorkSymbol()));
      }
      if (or.getOrganizationWorkTitle() != null) {
        attributes.add(new Attribute(Constants.ORGANIZATION_WORK_TITLE,
            or.getOrganizationWorkTitle()));
      }
    }

    PhoneSync ph = gc.getPhone();
    if (ph != null) {
      if (ph.getPhoneAssistant() != null) {
        attributes.add(new Attribute(Constants.PHONE_ASSISTANT, ph.getPhoneAssistant()));
      }
      if (ph.getPhoneCallback() != null) {
        attributes.add(new Attribute(Constants.PHONE_CALLBACK, ph.getPhoneCallback()));
      }
      if (ph.getPhoneCar() != null) {
        attributes.add(new Attribute(Constants.PHONE_CAR, ph.getPhoneCar()));
      }
      if (ph.getPhoneCompany() != null) {
        attributes.add(new Attribute(Constants.PHONE_COMPANY, ph.getPhoneCompany()));
      }
      if (ph.getPhoneFaxHome() != null) {
        attributes.add(new Attribute(Constants.PHONE_FAX_HOME, ph.getPhoneFaxHome()));
      }
      if (ph.getPhoneFaxWork() != null) {
        attributes.add(new Attribute(Constants.PHONE_FAX_WORK, ph.getPhoneFaxWork()));
      }
      if (ph.getPhoneHome() != null) {
        attributes.add(new Attribute(Constants.PHONE_HOME, ph.getPhoneHome()));
      }
      if (ph.getPhoneISDN() != null) {
        attributes.add(new Attribute(Constants.PHONE_ISDN, ph.getPhoneISDN()));
      }
      if (ph.getPhoneMain() != null) {
        attributes.add(new Attribute(Constants.PHONE_MAIN, ph.getPhoneMain()));
      }
      if (ph.getPhoneMMS() != null) {
        attributes.add(new Attribute(Constants.PHONE_MMS, ph.getPhoneMMS()));
      }
      if (ph.getPhoneMobile() != null) {
        attributes.add(new Attribute(Constants.PHONE_MOBILE, ph.getPhoneMobile()));
      }
      if (ph.getPhoneOther() != null) {
        attributes.add(new Attribute(Constants.PHONE_OTHER, ph.getPhoneOther()));
      }
      if (ph.getPhoneOtherFax() != null) {
        attributes.add(new Attribute(Constants.PHONE_OTHER_FAX, ph.getPhoneOtherFax()));
      }
      if (ph.getPhonePager() != null) {
        attributes.add(new Attribute(Constants.PHONE_PAGER, ph.getPhonePager()));
      }
      if (ph.getPhoneRadio() != null) {
        attributes.add(new Attribute(Constants.PHONE_RADIO, ph.getPhoneRadio()));
      }
      if (ph.getPhoneTelex() != null) {
        attributes.add(new Attribute(Constants.PHONE_TELEX, ph.getPhoneTelex()));
      }
      if (ph.getPhoneTTYTDD() != null) {
        attributes.add(new Attribute(Constants.PHONE_TTY_TDD, ph.getPhoneTTYTDD()));
      }
      if (ph.getPhoneWork() != null) {
        attributes.add(new Attribute(Constants.PHONE_WORK, ph.getPhoneWork()));
      }
      if (ph.getPhoneWorkMobile() != null) {
        attributes.add(new Attribute(Constants.PHONE_WORK_MOBILE, ph.getPhoneWorkMobile()));
      }
      if (ph.getPhoneWorkPager() != null) {
        attributes.add(new Attribute(Constants.PHONE_WORK_PAGER, ph.getPhoneWorkPager()));
      }
    }

    RelationSync re = gc.getRelation();
    if (re != null) {
      if (re.getRelationAssistant() != null) {
        attributes.add(new Attribute(Constants.RELATION_ASSISTANT, re.getRelationAssistant()));
      }
      if (re.getRelationBrother() != null) {
        attributes.add(new Attribute(Constants.RELATION_BROTHER, re.getRelationBrother()));
      }
      if (re.getRelationChild() != null) {
        attributes.add(new Attribute(Constants.RELATION_CHILD, re.getRelationChild()));
      }
      if (re.getRelationDomesticPartner() != null) {
        attributes.add(new Attribute(Constants.RELATION_DOMESTIC_PARTNER,
            re.getRelationDomesticPartner()));
      }
      if (re.getRelationFather() != null) {
        attributes.add(new Attribute(Constants.RELATION_FATHER, re.getRelationFather()));
      }
      if (re.getRelationFriend() != null) {
        attributes.add(new Attribute(Constants.RELATION_FRIEND, re.getRelationFriend()));
      }
      if (re.getRelationManager() != null) {
        attributes.add(new Attribute(Constants.RELATION_MANAGER, re.getRelationManager()));
      }
      if (re.getRelationMother() != null) {
        attributes.add(new Attribute(Constants.RELATION_MOTHER, re.getRelationMother()));
      }
      if (re.getRelationParent() != null) {
        attributes.add(new Attribute(Constants.RELATION_PARENT, re.getRelationParent()));
      }
      if (re.getRelationPartner() != null) {
        attributes.add(new Attribute(Constants.RELATION_PARTNER, re.getRelationPartner()));
      }
      if (re.getRelationRefferedBy() != null) {
        attributes.add(new Attribute(Constants.RELATION_REFFERED_BY, re.getRelationRefferedBy()));
      }
      if (re.getRelationRelative() != null) {
        attributes.add(new Attribute(Constants.RELATION_RELATIVE, re.getRelationRelative()));
      }
      if (re.getRelationSister() != null) {
        attributes.add(new Attribute(Constants.RELATION_SISTER, re.getRelationSister()));
      }
      if (re.getRelationSpouse() != null) {
        attributes.add(new Attribute(Constants.RELATION_SPOUSE, re.getRelationSpouse()));
      }
    }

    SipAddressSync si = gc.getSipAddress();
    if (si != null) {
      if (si.getHomeSip() != null) {
        attributes.add(new Attribute(Constants.HOME_SIP, si.getHomeSip()));
      }
      if (si.getOtherSip() != null) {
        attributes.add(new Attribute(Constants.OTHER_SIP, si.getOtherSip()));
      }
      if (si.getWorkSip() != null) {
        attributes.add(new Attribute(Constants.WORK_SIP, si.getWorkSip()));
      }
    }

    StructuredNameSync sn = gc.getStructuredName();
    if (sn != null) {
      if (sn.getDisplayName() != null) {
        attributes.add(new Attribute(Constants.DISPLAY_NAME, sn.getDisplayName()));
      }
      if (sn.getFamilyName() != null) {
        attributes.add(new Attribute(Constants.FAMILY_NAME, sn.getFamilyName()));
      }
      if (sn.getGivenName() != null) {
        attributes.add(new Attribute(Constants.GIVEN_NAME, sn.getGivenName()));
      }
      if (sn.getMiddleName() != null) {
        attributes.add(new Attribute(Constants.MIDDLE_NAME, sn.getMiddleName()));
      }
      if (sn.getNamePrefix() != null) {
        attributes.add(new Attribute(Constants.NAME_PREFIX, sn.getNamePrefix()));
      }
      if (sn.getNameSuffix() != null) {
        attributes.add(new Attribute(Constants.NAME_SUFFIX, sn.getNameSuffix()));
      }
      if (sn.getPhoneticFamilyName() != null) {
        attributes.add(new Attribute(Constants.PHONETIC_FAMILY_NAME, sn.getPhoneticFamilyName()));
      }
      if (sn.getPhoneticGivenName() != null) {
        attributes.add(new Attribute(Constants.PHONETIC_GIVEN_NAME, sn.getPhoneticGivenName()));
      }
      if (sn.getPhoneticMiddleName() != null) {
        attributes.add(new Attribute(Constants.PHONETIC_MIDDLE_NAME, sn.getPhoneticFamilyName()));
      }
    }

    StructuredPostalSync sp = gc.getStructuredPostal();
    if (sp != null) {
      if (sp.getHomeCity() != null) {
        attributes.add(new Attribute(Constants.HOME_CITY, sp.getHomeCity()));
      }
      if (sp.getHomeCountry() != null) {
        attributes.add(new Attribute(Constants.HOME_COUNTRY, sp.getHomeCountry()));
      }
      if (sp.getHomeFormattedAddress() != null) {
        attributes.add(new Attribute(Constants.HOME_FORMATTED_ADDRESS, sp.getHomeFormattedAddress()));
      }
      if (sp.getHomeNeighborhood() != null) {
        attributes.add(new Attribute(Constants.HOME_NEIGHBORHOOD, sp.getHomeNeighborhood()));
      }
      if (sp.getHomePOBox() != null) {
        attributes.add(new Attribute(Constants.HOME_POBOX, sp.getHomePOBox()));
      }
      if (sp.getHomePostalCode() != null) {
        attributes.add(new Attribute(Constants.HOME_POSTAL_CODE, sp.getHomePostalCode()));
      }
      if (sp.getHomeRegion() != null) {
        attributes.add(new Attribute(Constants.HOME_REGION, sp.getHomeRegion()));
      }
      if (sp.getHomeStreet() != null) {
        attributes.add(new Attribute(Constants.HOME_STREET, sp.getHomeStreet()));
      }
      if (sp.getWorkCity() != null) {
        attributes.add(new Attribute(Constants.WORK_CITY, sp.getWorkCity()));
      }
      if (sp.getWorkCountry() != null) {
        attributes.add(new Attribute(Constants.WORK_COUNTRY, sp.getWorkCountry()));
      }
      if (sp.getWorkFormattedAddress() != null) {
        attributes.add(new Attribute(Constants.WORK_FORMATTED_ADDRESS, sp.getWorkFormattedAddress()));
      }
      if (sp.getWorkNeighborhood() != null) {
        attributes.add(new Attribute(Constants.WORK_NEIGHBORHOOD, sp.getWorkNeighborhood()));
      }
      if (sp.getWorkPOBox() != null) {
        attributes.add(new Attribute(Constants.WORK_POBOX, sp.getWorkPOBox()));
      }
      if (sp.getWorkPostalCode() != null) {
        attributes.add(new Attribute(Constants.WORK_POSTAL_CODE, sp.getWorkPostalCode()));
      }
      if (sp.getWorkRegion() != null) {
        attributes.add(new Attribute(Constants.WORK_REGION, sp.getWorkRegion()));
      }
      if (sp.getWorkStreet() != null) {
        attributes.add(new Attribute(Constants.WORK_STREET, sp.getWorkStreet()));
      }
      if (sp.getOtherCity() != null) {
        attributes.add(new Attribute(Constants.OTHER_CITY, sp.getOtherCity()));
      }
      if (sp.getOtherCountry() != null) {
        attributes.add(new Attribute(Constants.OTHER_COUNTRY, sp.getOtherCountry()));
      }
      if (sp.getOtherFormattedAddress() != null) {
        attributes.add(new Attribute(Constants.OTHER_FORMATTED_ADDRESS,
            sp.getOtherFormattedAddress()));
      }
      if (sp.getOtherNeighborhood() != null) {
        attributes.add(new Attribute(Constants.OTHER_NEIGHBORHOOD, sp.getOtherNeighborhood()));
      }
      if (sp.getOtherPOBox() != null) {
        attributes.add(new Attribute(Constants.OTHER_POBOX, sp.getOtherPOBox()));
      }
      if (sp.getOtherPostalCode() != null) {
        attributes.add(new Attribute(Constants.OTHER_POSTAL_CODE, sp.getOtherPostalCode()));
      }
      if (sp.getOtherRegion() != null) {
        attributes.add(new Attribute(Constants.OTHER_REGION, sp.getOtherRegion()));
      }
      if (sp.getOtherStreet() != null) {
        attributes.add(new Attribute(Constants.OTHER_STREET, sp.getOtherStreet()));
      }
    }

    WebsiteSync we = gc.getWebsite();
    if (we != null) {
      if (we.getWebsiteBlog() != null) {
        attributes.add(new Attribute(Constants.WEBSITE_BLOG, we.getWebsiteBlog()));
      }
      if (we.getWebsiteFtp() != null) {
        attributes.add(new Attribute(Constants.WEBSITE_FTP, we.getWebsiteFtp()));
      }
      if (we.getWebsiteHome() != null) {
        attributes.add(new Attribute(Constants.WEBSITE_HOME, we.getWebsiteHome()));
      }
      if (we.getWebsiteHomepage() != null) {
        attributes.add(new Attribute(Constants.WEBSITE_HOMEPAGE, we.getWebsiteHomepage()));
      }
      if (we.getWebsiteOther() != null) {
        attributes.add(new Attribute(Constants.WEBSITE_OTHER, we.getWebsiteOther()));
      }
      if (we.getWebsiteProfile() != null) {
        attributes.add(new Attribute(Constants.WEBSITE_PROFILE, we.getWebsiteProfile()));
      }
      if (we.getWebsiteWork() != null) {
        attributes.add(new Attribute(Constants.WEBSITE_WORK, we.getWebsiteWork()));
      }
    }
    return attributes;
  }

  /**
   * Mapping data from contact provider database to {@link GoogleContact}.
   *
   * @param contentResolver contentResolver
   * @param id raw_ID contacts
   * @param uuid UUID
   * @return GoogleContact
   */
  public GoogleContact mappingContactFromDB(ContentResolver contentResolver, String id, String uuid) {
    Cursor cursor = null;
    GoogleContact contact = null;
    try {
      cursor = new ContactDetail().fetchAllDataOfContact(contentResolver, id);
      contact = new GoogleContact();
      contact.setId(id);
      contact.setUuid(uuid);
      while (cursor.moveToNext()) {
        fillContact(cursor, contact);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }

    if (contact.getStructuredName() == null || contact.getStructuredName().getDisplayName() == null) {
      if (contact.getStructuredName() == null) {
        contact.initStructuredName();
      }
      contact.getStructuredName().setDisplayName(
          new AndroidDB().fetchContactName(contentResolver, id));
    }

    //Log.i(TAG, contact.toString());
    return contact;
  }

  /**
   * Create {@link GoogleContact} from data from server.
   * @param user data from server
   * @return new {@link GoogleContact}
   */
  public static GoogleContact mappingContactFromLDAP(ReadOnlyEntry user) {
    GoogleContact contact = GoogleContact.defaultValue();
    contact.setUuid(user.hasAttribute(Constants.UUID) ? user.getAttributeValue(Constants.UUID) : null);
    // email
    contact.getEmail().setHomeMail(
        user.hasAttribute(contact.getEmail().getHomeMail()) ? user.getAttributeValue(contact.getEmail()
            .getHomeMail()) : null);
    contact.getEmail().setMobileMail(
        user.hasAttribute(contact.getEmail().getMobileMail()) ? user.getAttributeValue(contact.getEmail()
            .getMobileMail()) : null);
    contact.getEmail().setOtherMail(
        user.hasAttribute(contact.getEmail().getOtherMail()) ? user.getAttributeValue(contact.getEmail()
            .getOtherMail()) : null);
    contact.getEmail().setWorkMail(
        user.hasAttribute(contact.getEmail().getWorkMail()) ? user.getAttributeValue(contact.getEmail()
            .getWorkMail()) : null);
    if (contact.getEmail().isNull()) {
      contact.setEmail(null);
    }
    // event
    contact.getEvent().setEventAnniversary(
        user.hasAttribute(contact.getEvent().getEventAnniversary()) ? user.getAttributeValue(contact.getEvent()
            .getEventAnniversary()) : null);
    contact.getEvent().setEventBirthday(
        user.hasAttribute(contact.getEvent().getEventBirthday()) ? user.getAttributeValue(contact.getEvent()
            .getEventBirthday()) : null);
    contact.getEvent().setEventOther(
        user.hasAttribute(contact.getEvent().getEventOther()) ? user.getAttributeValue(contact.getEvent()
            .getEventOther()) : null);
    if (contact.getEvent().isNull()) {
      contact.setEvent(null);
    }

    // identity
    contact.getIdentity().setIdentityNamespace(
        user.hasAttribute(contact.getIdentity().getIdentityNamespace())
            ? user.getAttributeValue(contact.getIdentity().getIdentityNamespace())
            : null);
    contact.getIdentity().setIdentityText(
        user.hasAttribute(contact.getIdentity().getIdentityText())
            ? user.getAttributeValue(contact.getIdentity().getIdentityText())
            : null);
    if (contact.getIdentity().isNull()) {
      contact.setIdentity(null);
    }

    // im
    contact.getImSync().setImHomeAim(
        user.hasAttribute(contact.getImSync().getImHomeAim()) ? user.getAttributeValue(contact.getImSync()
            .getImHomeAim()) : null);
    contact.getImSync().setImHomeGoogleTalk(
        user.hasAttribute(contact.getImSync().getImHomeGoogleTalk())
            ? user.getAttributeValue(contact.getImSync().getImHomeGoogleTalk())
            : null);
    contact.getImSync().setImHomeIcq(
        user.hasAttribute(contact.getImSync().getImHomeIcq()) ? user.getAttributeValue(contact.getImSync()
            .getImHomeIcq()) : null);
    contact.getImSync().setImHomeJabber(
        user.hasAttribute(contact.getImSync().getImHomeJabber()) ? user.getAttributeValue(contact.getImSync()
            .getImHomeJabber()) : null);
    contact.getImSync().setImHomeMsn(
        user.hasAttribute(contact.getImSync().getImHomeMsn()) ? user.getAttributeValue(contact.getImSync()
            .getImHomeMsn()) : null);
    contact.getImSync().setImHomeNetmeeting(
        user.hasAttribute(contact.getImSync().getImHomeNetmeeting())
            ? user.getAttributeValue(contact.getImSync().getImHomeNetmeeting())
            : null);
    contact.getImSync().setImHomeQq(
        user.hasAttribute(contact.getImSync().getImHomeQq()) ? user.getAttributeValue(contact.getImSync()
            .getImHomeQq()) : null);
    contact.getImSync().setImHomeSkype(
        user.hasAttribute(contact.getImSync().getImHomeSkype()) ? user.getAttributeValue(contact.getImSync()
            .getImHomeSkype()) : null);
    contact.getImSync().setImHomeYahoo(
        user.hasAttribute(contact.getImSync().getImHomeYahoo()) ? user.getAttributeValue(contact.getImSync()
            .getImHomeYahoo()) : null);
    contact.getImSync().setImOtherAim(
        user.hasAttribute(contact.getImSync().getImOtherAim()) ? user.getAttributeValue(contact.getImSync()
            .getImOtherAim()) : null);
    contact.getImSync().setImOtherGoogleTalk(
        user.hasAttribute(contact.getImSync().getImOtherGoogleTalk())
            ? user.getAttributeValue(contact.getImSync().getImOtherGoogleTalk())
            : null);
    contact.getImSync().setImOtherIcq(
        user.hasAttribute(contact.getImSync().getImOtherIcq()) ? user.getAttributeValue(contact.getImSync()
            .getImOtherIcq()) : null);
    contact.getImSync().setImOtherJabber(
        user.hasAttribute(contact.getImSync().getImOtherJabber()) ? user.getAttributeValue(contact.getImSync()
            .getImOtherJabber()) : null);
    contact.getImSync().setImOtherMsn(
        user.hasAttribute(contact.getImSync().getImOtherMsn()) ? user.getAttributeValue(contact.getImSync()
            .getImOtherMsn()) : null);
    contact.getImSync().setImOtherNetmeeting(
        user.hasAttribute(contact.getImSync().getImOtherNetmeeting())
            ? user.getAttributeValue(contact.getImSync().getImOtherNetmeeting())
            : null);
    contact.getImSync().setImOtherQq(
        user.hasAttribute(contact.getImSync().getImOtherQq()) ? user.getAttributeValue(contact.getImSync()
            .getImOtherQq()) : null);
    contact.getImSync().setImOtherSkype(
        user.hasAttribute(contact.getImSync().getImOtherSkype()) ? user.getAttributeValue(contact.getImSync()
            .getImOtherSkype()) : null);
    contact.getImSync().setImOtherYahoo(
        user.hasAttribute(contact.getImSync().getImOtherYahoo()) ? user.getAttributeValue(contact.getImSync()
            .getImOtherYahoo()) : null);
    contact.getImSync().setImWorkAim(
        user.hasAttribute(contact.getImSync().getImWorkAim()) ? user.getAttributeValue(contact.getImSync()
            .getImWorkAim()) : null);
    contact.getImSync().setImWorkGoogleTalk(
        user.hasAttribute(contact.getImSync().getImWorkGoogleTalk())
            ? user.getAttributeValue(contact.getImSync().getImWorkGoogleTalk())
            : null);
    contact.getImSync().setImWorkIcq(
        user.hasAttribute(contact.getImSync().getImWorkIcq()) ? user.getAttributeValue(contact.getImSync()
            .getImWorkIcq()) : null);
    contact.getImSync().setImWorkJabber(
        user.hasAttribute(contact.getImSync().getImWorkJabber()) ? user.getAttributeValue(contact.getImSync()
            .getImWorkJabber()) : null);
    contact.getImSync().setImWorkMsn(
        user.hasAttribute(contact.getImSync().getImWorkMsn()) ? user.getAttributeValue(contact.getImSync()
            .getImWorkMsn()) : null);
    contact.getImSync().setImWorkNetmeeting(
        user.hasAttribute(contact.getImSync().getImWorkNetmeeting())
            ? user.getAttributeValue(contact.getImSync().getImWorkNetmeeting())
            : null);
    contact.getImSync().setImWorkQq(
        user.hasAttribute(contact.getImSync().getImWorkQq()) ? user.getAttributeValue(contact.getImSync()
            .getImWorkQq()) : null);
    contact.getImSync().setImWorkSkype(
        user.hasAttribute(contact.getImSync().getImWorkSkype()) ? user.getAttributeValue(contact.getImSync()
            .getImWorkSkype()) : null);
    contact.getImSync().setImWorkYahoo(
        user.hasAttribute(contact.getImSync().getImWorkYahoo()) ? user.getAttributeValue(contact.getImSync()
            .getImWorkYahoo()) : null);
    if (contact.getImSync().isNull()) {
      contact.setImSync(null);
    }

    // nickname
    contact.getNickname().setNicknameDefault(
        user.hasAttribute(contact.getNickname().getNicknameDefault())
            ? user.getAttributeValue(contact.getNickname().getNicknameDefault())
            : null);
    contact.getNickname().setNicknameInitials(
        user.hasAttribute(contact.getNickname().getNicknameInitials())
            ? user.getAttributeValue(contact.getNickname().getNicknameInitials())
            : null);
    contact.getNickname().setNicknameMaiden(
        user.hasAttribute(contact.getNickname().getNicknameMaiden())
            ? user.getAttributeValue(contact.getNickname().getNicknameMaiden())
            : null);
    contact.getNickname().setNicknameOther(
        user.hasAttribute(contact.getNickname().getNicknameOther())
            ? user.getAttributeValue(contact.getNickname().getNicknameOther())
            : null);
    contact.getNickname().setNicknameShort(
        user.hasAttribute(contact.getNickname().getNicknameShort())
            ? user.getAttributeValue(contact.getNickname().getNicknameShort())
            : null);
    if (contact.getNickname().isNull()) {
      contact.setNickname(null);
    }

    // notes
    contact.getNote().setNotes(
        user.hasAttribute(contact.getNote().getNotes())
            ? user.getAttributeValue(contact.getNote().getNotes())
            : null);
    if (contact.getNote().isNull()) {
      contact.setNote(null);

    }
    // organization
    contact.getOrganization().setOrganizationOtherCompany(
        user.hasAttribute(contact.getOrganization().getOrganizationOtherCompany())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationOtherCompany())
            : null);
    contact.getOrganization().setOrganizationOtherDepartment(
        user.hasAttribute(contact.getOrganization().getOrganizationOtherDepartment())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationOtherDepartment())
            : null);
    contact.getOrganization().setOrganizationOtherJobDescription(
        user.hasAttribute(contact.getOrganization().getOrganizationOtherJobDescription())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationOtherJobDescription())
            : null);
    contact.getOrganization().setOrganizationOtherOfficeLocation(
        user.hasAttribute(contact.getOrganization().getOrganizationOtherOfficeLocation())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationOtherOfficeLocation())
            : null);
    contact.getOrganization().setOrganizationOtherPhoneticName(
        user.hasAttribute(contact.getOrganization().getOrganizationOtherPhoneticName())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationOtherPhoneticName())
            : null);
    contact.getOrganization().setOrganizationOtherPhoneticNameStyle(
        user.hasAttribute(contact.getOrganization().getOrganizationOtherPhoneticNameStyle())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationOtherPhoneticNameStyle())
            : null);
    contact.getOrganization().setOrganizationOtherSymbol(
        user.hasAttribute(contact.getOrganization().getOrganizationOtherSymbol())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationOtherSymbol())
            : null);
    contact.getOrganization().setOrganizationOtherTitle(
        user.hasAttribute(contact.getOrganization().getOrganizationOtherTitle())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationOtherTitle())
            : null);
    contact.getOrganization().setOrganizationWorkCompany(
        user.hasAttribute(contact.getOrganization().getOrganizationWorkCompany())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationWorkCompany())
            : null);
    contact.getOrganization().setOrganizationWorkDepartment(
        user.hasAttribute(contact.getOrganization().getOrganizationWorkDepartment())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationWorkDepartment())
            : null);
    contact.getOrganization().setOrganizationWorkJobDescription(
        user.hasAttribute(contact.getOrganization().getOrganizationWorkJobDescription())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationWorkJobDescription())
            : null);
    contact.getOrganization().setOrganizationWorkOfficeLocation(
        user.hasAttribute(contact.getOrganization().getOrganizationWorkOfficeLocation())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationWorkOfficeLocation())
            : null);
    contact.getOrganization().setOrganizationWorkPhoneticName(
        user.hasAttribute(contact.getOrganization().getOrganizationWorkPhoneticName())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationWorkPhoneticName())
            : null);
    contact.getOrganization().setOrganizationWorkPhoneticNameStyle(
        user.hasAttribute(contact.getOrganization().getOrganizationWorkPhoneticNameStyle())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationWorkPhoneticNameStyle())
            : null);
    contact.getOrganization().setOrganizationWorkSymbol(
        user.hasAttribute(contact.getOrganization().getOrganizationWorkSymbol())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationWorkSymbol())
            : null);
    contact.getOrganization().setOrganizationWorkTitle(
        user.hasAttribute(contact.getOrganization().getOrganizationWorkTitle())
            ? user.getAttributeValue(contact.getOrganization().getOrganizationWorkTitle())
            : null);
    if (contact.getOrganization().isNull()) {
      contact.setOrganization(null);
    }

    // phone
    contact.getPhone().setPhoneAssistant(
        user.hasAttribute(contact.getPhone().getPhoneAssistant()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneAssistant()) : null);
    contact.getPhone().setPhoneCallback(
        user.hasAttribute(contact.getPhone().getPhoneCallback()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneCallback()) : null);
    contact.getPhone().setPhoneCar(
        user.hasAttribute(contact.getPhone().getPhoneCar()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneCar()) : null);
    contact.getPhone().setPhoneCompany(
        user.hasAttribute(contact.getPhone().getPhoneCompany()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneCompany()) : null);
    contact.getPhone().setPhoneFaxHome(
        user.hasAttribute(contact.getPhone().getPhoneFaxHome()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneFaxHome()) : null);
    contact.getPhone().setPhoneFaxWork(
        user.hasAttribute(contact.getPhone().getPhoneFaxWork()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneFaxWork()) : null);
    contact.getPhone().setPhoneHome(
        user.hasAttribute(contact.getPhone().getPhoneHome()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneHome()) : null);
    contact.getPhone().setPhoneISDN(
        user.hasAttribute(contact.getPhone().getPhoneISDN()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneISDN()) : null);
    contact.getPhone().setPhoneMain(
        user.hasAttribute(contact.getPhone().getPhoneMain()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneMain()) : null);
    contact.getPhone().setPhoneMMS(
        user.hasAttribute(contact.getPhone().getPhoneMMS()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneMMS()) : null);
    contact.getPhone().setPhoneMobile(
        user.hasAttribute(contact.getPhone().getPhoneMobile()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneMobile()) : null);
    contact.getPhone().setPhoneOther(
        user.hasAttribute(contact.getPhone().getPhoneOther()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneOther()) : null);
    contact.getPhone().setPhoneOtherFax(
        user.hasAttribute(contact.getPhone().getPhoneOtherFax()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneOtherFax()) : null);
    contact.getPhone().setPhonePager(
        user.hasAttribute(contact.getPhone().getPhonePager()) ? user.getAttributeValue(contact.getPhone()
            .getPhonePager()) : null);
    contact.getPhone().setPhoneRadio(
        user.hasAttribute(contact.getPhone().getPhoneRadio()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneRadio()) : null);
    contact.getPhone().setPhoneTelex(
        user.hasAttribute(contact.getPhone().getPhoneTelex()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneTelex()) : null);
    contact.getPhone().setPhoneTTYTDD(
        user.hasAttribute(contact.getPhone().getPhoneTTYTDD()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneTTYTDD()) : null);
    contact.getPhone().setPhoneWork(
        user.hasAttribute(contact.getPhone().getPhoneWork()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneWork()) : null);
    contact.getPhone().setPhoneWorkMobile(
        user.hasAttribute(contact.getPhone().getPhoneWorkMobile()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneWorkMobile()) : null);
    contact.getPhone().setPhoneWorkPager(
        user.hasAttribute(contact.getPhone().getPhoneWorkPager()) ? user.getAttributeValue(contact.getPhone()
            .getPhoneWorkPager()) : null);
    if (contact.getPhone().isNull()) {
      contact.setPhone(null);
    }

    // relation
    contact.getRelation().setRelationAssistant(
        user.hasAttribute(contact.getRelation().getRelationAssistant())
            ? user.getAttributeValue(contact.getRelation().getRelationAssistant())
            : null);
    contact.getRelation().setRelationBrother(
        user.hasAttribute(contact.getRelation().getRelationBrother())
            ? user.getAttributeValue(contact.getRelation().getRelationBrother())
            : null);
    contact.getRelation().setRelationChild(
        user.hasAttribute(contact.getRelation().getRelationChild())
            ? user.getAttributeValue(contact.getRelation().getRelationChild())
            : null);
    contact.getRelation().setRelationDomesticPartner(
        user.hasAttribute(contact.getRelation().getRelationDomesticPartner())
            ? user.getAttributeValue(contact.getRelation().getRelationDomesticPartner())
            : null);
    contact.getRelation().setRelationFather(
        user.hasAttribute(contact.getRelation().getRelationFather())
            ? user.getAttributeValue(contact.getRelation().getRelationFather())
            : null);
    contact.getRelation().setRelationFriend(
        user.hasAttribute(contact.getRelation().getRelationFriend())
            ? user.getAttributeValue(contact.getRelation().getRelationFriend())
            : null);
    contact.getRelation().setRelationManager(
        user.hasAttribute(contact.getRelation().getRelationManager())
            ? user.getAttributeValue(contact.getRelation().getRelationManager())
            : null);
    contact.getRelation().setRelationMother(
        user.hasAttribute(contact.getRelation().getRelationMother())
            ? user.getAttributeValue(contact.getRelation().getRelationMother())
            : null);
    contact.getRelation().setRelationParent(
        user.hasAttribute(contact.getRelation().getRelationParent())
            ? user.getAttributeValue(contact.getRelation().getRelationParent())
            : null);
    contact.getRelation().setRelationPartner(
        user.hasAttribute(contact.getRelation().getRelationPartner())
            ? user.getAttributeValue(contact.getRelation().getRelationPartner())
            : null);
    contact.getRelation().setRelationRefferedBy(
        user.hasAttribute(contact.getRelation().getRelationRefferedBy())
            ? user.getAttributeValue(contact.getRelation().getRelationRefferedBy())
            : null);
    contact.getRelation().setRelationRelative(
        user.hasAttribute(contact.getRelation().getRelationRelative())
            ? user.getAttributeValue(contact.getRelation().getRelationRelative())
            : null);
    contact.getRelation().setRelationSister(
        user.hasAttribute(contact.getRelation().getRelationSister())
            ? user.getAttributeValue(contact.getRelation().getRelationSister())
            : null);
    contact.getRelation().setRelationSpouse(
        user.hasAttribute(contact.getRelation().getRelationSpouse())
            ? user.getAttributeValue(contact.getRelation().getRelationSpouse())
            : null);
    if (contact.getRelation().isNull()) {
      contact.setRelation(null);
    }

    // sipaddress
    contact.getSipAddress().setHomeSip(
        user.hasAttribute(contact.getSipAddress().getHomeSip())
            ? user.getAttributeValue(contact.getSipAddress().getHomeSip())
            : null);
    contact.getSipAddress().setOtherSip(
        user.hasAttribute(contact.getSipAddress().getOtherSip())
            ? user.getAttributeValue(contact.getSipAddress().getOtherSip())
            : null);
    contact.getSipAddress().setWorkSip(
        user.hasAttribute(contact.getSipAddress().getWorkSip())
            ? user.getAttributeValue(contact.getSipAddress().getWorkSip())
            : null);
    if (contact.getSipAddress().isNull()) {
      contact.setSipAddress(null);
    }

    // structuredname
    contact.getStructuredName().setDisplayName(
        user.hasAttribute(contact.getStructuredName().getDisplayName())
            ? user.getAttributeValue(contact.getStructuredName().getDisplayName())
            : null);
    contact.getStructuredName().setFamilyName(
        user.hasAttribute(contact.getStructuredName().getFamilyName())
            ? user.getAttributeValue(contact.getStructuredName().getFamilyName())
            : null);
    contact.getStructuredName().setGivenName(
        user.hasAttribute(contact.getStructuredName().getGivenName())
            ? user.getAttributeValue(contact.getStructuredName().getGivenName())
            : null);
    contact.getStructuredName().setMiddleName(
        user.hasAttribute(contact.getStructuredName().getMiddleName())
            ? user.getAttributeValue(contact.getStructuredName().getMiddleName())
            : null);
    contact.getStructuredName().setNamePrefix(
        user.hasAttribute(contact.getStructuredName().getNamePrefix())
            ? user.getAttributeValue(contact.getStructuredName().getNamePrefix())
            : null);
    contact.getStructuredName().setNameSuffix(
        user.hasAttribute(contact.getStructuredName().getNameSuffix())
            ? user.getAttributeValue(contact.getStructuredName().getNameSuffix())
            : null);
    contact.getStructuredName().setPhoneticFamilyName(
        user.hasAttribute(contact.getStructuredName().getPhoneticFamilyName())
            ? user.getAttributeValue(contact.getStructuredName().getPhoneticFamilyName())
            : null);
    contact.getStructuredName().setPhoneticGivenName(
        user.hasAttribute(contact.getStructuredName().getPhoneticGivenName())
            ? user.getAttributeValue(contact.getStructuredName().getPhoneticGivenName())
            : null);
    contact.getStructuredName().setPhoneticMiddleName(
        user.hasAttribute(contact.getStructuredName().getPhoneticMiddleName())
            ? user.getAttributeValue(contact.getStructuredName().getPhoneticMiddleName())
            : null);
    if (contact.getStructuredName().isNull()) {
      contact.setStructuredName(null);
    }

    // structured postal
    contact.getStructuredPostal().setHomeCity(
        user.hasAttribute(contact.getStructuredPostal().getHomeCity())
            ? user.getAttributeValue(contact.getStructuredPostal().getHomeCity())
            : null);
    contact.getStructuredPostal().setHomeCountry(
        user.hasAttribute(contact.getStructuredPostal().getHomeCountry())
            ? user.getAttributeValue(contact.getStructuredPostal().getHomeCountry())
            : null);
    contact.getStructuredPostal().setHomeFormattedAddress(
        user.hasAttribute(contact.getStructuredPostal().getHomeFormattedAddress())
            ? user.getAttributeValue(contact.getStructuredPostal().getHomeFormattedAddress())
            : null);
    contact.getStructuredPostal().setHomeNeighborhood(
        user.hasAttribute(contact.getStructuredPostal().getHomeNeighborhood())
            ? user.getAttributeValue(contact.getStructuredPostal().getHomeNeighborhood())
            : null);
    contact.getStructuredPostal().setHomePOBox(
        user.hasAttribute(contact.getStructuredPostal().getHomePOBox())
            ? user.getAttributeValue(contact.getStructuredPostal().getHomePOBox())
            : null);
    contact.getStructuredPostal().setHomePostalCode(
        user.hasAttribute(contact.getStructuredPostal().getHomePostalCode())
            ? user.getAttributeValue(contact.getStructuredPostal().getHomePostalCode())
            : null);
    contact.getStructuredPostal().setHomeRegion(
        user.hasAttribute(contact.getStructuredPostal().getHomeRegion())
            ? user.getAttributeValue(contact.getStructuredPostal().getHomeRegion())
            : null);
    contact.getStructuredPostal().setHomeStreet(
        user.hasAttribute(contact.getStructuredPostal().getHomeStreet())
            ? user.getAttributeValue(contact.getStructuredPostal().getHomeStreet())
            : null);
    contact.getStructuredPostal().setWorkCity(
        user.hasAttribute(contact.getStructuredPostal().getWorkCity())
            ? user.getAttributeValue(contact.getStructuredPostal().getWorkCity())
            : null);
    contact.getStructuredPostal().setWorkCountry(
        user.hasAttribute(contact.getStructuredPostal().getWorkCountry())
            ? user.getAttributeValue(contact.getStructuredPostal().getWorkCountry())
            : null);
    contact.getStructuredPostal().setWorkFormattedAddress(
        user.hasAttribute(contact.getStructuredPostal().getWorkFormattedAddress())
            ? user.getAttributeValue(contact.getStructuredPostal().getWorkFormattedAddress())
            : null);
    contact.getStructuredPostal().setWorkNeighborhood(
        user.hasAttribute(contact.getStructuredPostal().getWorkNeighborhood())
            ? user.getAttributeValue(contact.getStructuredPostal().getWorkNeighborhood())
            : null);
    contact.getStructuredPostal().setWorkPOBox(
        user.hasAttribute(contact.getStructuredPostal().getWorkPOBox())
            ? user.getAttributeValue(contact.getStructuredPostal().getWorkPOBox())
            : null);
    contact.getStructuredPostal().setWorkPostalCode(
        user.hasAttribute(contact.getStructuredPostal().getWorkPostalCode())
            ? user.getAttributeValue(contact.getStructuredPostal().getWorkPostalCode())
            : null);
    contact.getStructuredPostal().setWorkRegion(
        user.hasAttribute(contact.getStructuredPostal().getWorkRegion())
            ? user.getAttributeValue(contact.getStructuredPostal().getWorkRegion())
            : null);
    contact.getStructuredPostal().setWorkStreet(
        user.hasAttribute(contact.getStructuredPostal().getWorkStreet())
            ? user.getAttributeValue(contact.getStructuredPostal().getWorkStreet())
            : null);
    contact.getStructuredPostal().setOtherCity(
        user.hasAttribute(contact.getStructuredPostal().getOtherCity())
            ? user.getAttributeValue(contact.getStructuredPostal().getOtherCity())
            : null);
    contact.getStructuredPostal().setOtherCountry(
        user.hasAttribute(contact.getStructuredPostal().getOtherCountry())
            ? user.getAttributeValue(contact.getStructuredPostal().getOtherCountry())
            : null);
    contact.getStructuredPostal().setOtherFormattedAddress(
        user.hasAttribute(contact.getStructuredPostal().getOtherFormattedAddress())
            ? user.getAttributeValue(contact.getStructuredPostal().getOtherFormattedAddress())
            : null);
    contact.getStructuredPostal().setOtherNeighborhood(
        user.hasAttribute(contact.getStructuredPostal().getOtherNeighborhood())
            ? user.getAttributeValue(contact.getStructuredPostal().getOtherNeighborhood())
            : null);
    contact.getStructuredPostal().setOtherPOBox(
        user.hasAttribute(contact.getStructuredPostal().getOtherPOBox())
            ? user.getAttributeValue(contact.getStructuredPostal().getOtherPOBox())
            : null);
    contact.getStructuredPostal().setOtherPostalCode(
        user.hasAttribute(contact.getStructuredPostal().getOtherPostalCode())
            ? user.getAttributeValue(contact.getStructuredPostal().getOtherPostalCode())
            : null);
    contact.getStructuredPostal().setOtherRegion(
        user.hasAttribute(contact.getStructuredPostal().getOtherRegion())
            ? user.getAttributeValue(contact.getStructuredPostal().getOtherRegion())
            : null);
    contact.getStructuredPostal().setOtherStreet(
        user.hasAttribute(contact.getStructuredPostal().getOtherStreet())
            ? user.getAttributeValue(contact.getStructuredPostal().getOtherStreet())
            : null);
    if (contact.getStructuredPostal().isNull()) {
      contact.setStructuredPostal(null);
    }

    // website
    contact.getWebsite().setWebsiteBlog(
        user.hasAttribute(contact.getWebsite().getWebsiteBlog()) ? user.getAttributeValue(contact.getWebsite()
            .getWebsiteBlog()) : null);
    contact.getWebsite().setWebsiteFtp(
        user.hasAttribute(contact.getWebsite().getWebsiteFtp()) ? user.getAttributeValue(contact.getWebsite()
            .getWebsiteFtp()) : null);
    contact.getWebsite().setWebsiteHome(
        user.hasAttribute(contact.getWebsite().getWebsiteHome()) ? user.getAttributeValue(contact.getWebsite()
            .getWebsiteHome()) : null);
    contact.getWebsite().setWebsiteHomepage(
        user.hasAttribute(contact.getWebsite().getWebsiteHomepage())
            ? user.getAttributeValue(contact.getWebsite().getWebsiteHomepage())
            : null);
    contact.getWebsite().setWebsiteOther(
        user.hasAttribute(contact.getWebsite().getWebsiteOther()) ? user.getAttributeValue(contact.getWebsite()
            .getWebsiteOther()) : null);
    contact.getWebsite().setWebsiteProfile(
        user.hasAttribute(contact.getWebsite().getWebsiteProfile())
            ? user.getAttributeValue(contact.getWebsite().getWebsiteProfile())
            : null);
    contact.getWebsite().setWebsiteWork(
        user.hasAttribute(contact.getWebsite().getWebsiteWork()) ? user.getAttributeValue(contact.getWebsite()
            .getWebsiteWork()) : null);
    if (contact.getWebsite().isNull()) {
      contact.setWebsite(null);
    }

    return contact;
  }

  private static List<Modification> fillModification(GoogleContact gc) {
    List<Modification> mod = new ArrayList<Modification>();

    EmailSync e = gc.getEmail();
    if (e != null) {
      if (e.getHomeMail() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.HOME_MAIL, e.getHomeMail()));
      }
      if (e.getMobileMail() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.MOBILE_MAIL, e.getMobileMail()));
      }
      if (e.getOtherMail() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.OTHER_MAIL, e.getOtherMail()));
      }
      if (e.getWorkMail() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WORK_MAIL, e.getWorkMail()));
      }
    }

    EventSync ev = gc.getEvent();
    if (ev != null) {
      if (ev.getEventAnniversary() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.EVENT_ANNIVERSARY,
            ev.getEventAnniversary()));
      }
      if (ev.getEventBirthday() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.EVENT_BIRTHDAY,
            ev.getEventBirthday()));
      }
      if (ev.getEventOther() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.EVENT_OTHER,
            ev.getEventOther()));
      }
    }

    IdentitySync id = gc.getIdentity();
    if (id != null) {
      if (id.getIdentityNamespace() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IDENTITY_NAMESPACE,
            id.getIdentityNamespace()));
      }
      if (id.getIdentityText() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IDENTITY_TEXT,
            id.getIdentityText()));
      }
    }

    ImSync im = gc.getImSync();
    if (im != null) {
      if (im.getImHomeAim() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_HOME_AIM, im.getImHomeAim()));
      }
      if (im.getImHomeGoogleTalk() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_HOME_GOOGLE_TALK,
            im.getImHomeGoogleTalk()));
      }
      if (im.getImHomeIcq() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_HOME_ICQ, im.getImHomeIcq()));
      }
      if (im.getImHomeJabber() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_HOME_JABBER,
            im.getImHomeJabber()));
      }
      if (im.getImHomeMsn() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_HOME_MSN, im.getImHomeMsn()));
      }
      if (im.getImHomeNetmeeting() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_HOME_NETMEETING,
            im.getImHomeNetmeeting()));
      }
      if (im.getImHomeQq() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_HOME_QQ, im.getImHomeQq()));
      }
      if (im.getImHomeSkype() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_HOME_SKYPE,
            im.getImHomeSkype()));
      }
      if (im.getImHomeYahoo() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_HOME_YAHOO,
            im.getImHomeYahoo()));
      }

      if (im.getImWorkAim() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_WORK_AIM, im.getImWorkAim()));
      }
      if (im.getImWorkGoogleTalk() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_WORK_GOOGLE_TALK,
            im.getImWorkGoogleTalk()));
      }
      if (im.getImWorkIcq() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_WORK_ICQ, im.getImWorkIcq()));
      }
      if (im.getImWorkJabber() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_WORK_JABBER,
            im.getImWorkJabber()));
      }
      if (im.getImWorkMsn() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_WORK_MSN, im.getImWorkMsn()));
      }
      if (im.getImWorkNetmeeting() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_WORK_NETMEETING,
            im.getImWorkNetmeeting()));
      }
      if (im.getImWorkQq() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_WORK_QQ, im.getImWorkQq()));
      }
      if (im.getImWorkSkype() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_WORK_SKYPE,
            im.getImWorkSkype()));
      }
      if (im.getImWorkYahoo() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_WORK_YAHOO,
            im.getImWorkYahoo()));
      }

      if (im.getImOtherAim() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_OTHER_AIM,
            im.getImOtherAim()));
      }
      if (im.getImOtherGoogleTalk() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_OTHER_GOOGLE_TALK,
            im.getImOtherGoogleTalk()));
      }
      if (im.getImOtherIcq() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_OTHER_ICQ,
            im.getImOtherIcq()));
      }
      if (im.getImOtherJabber() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_OTHER_JABBER,
            im.getImOtherJabber()));
      }
      if (im.getImOtherMsn() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_OTHER_MSN,
            im.getImOtherMsn()));
      }
      if (im.getImOtherNetmeeting() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_OTHER_NETMEETING,
            im.getImOtherNetmeeting()));
      }
      if (im.getImOtherQq() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_OTHER_QQ, im.getImOtherQq()));
      }
      if (im.getImOtherSkype() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_OTHER_SKYPE,
            im.getImOtherSkype()));
      }
      if (im.getImOtherYahoo() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.IM_OTHER_YAHOO,
            im.getImOtherYahoo()));
      }
    }

    NicknameSync ni = gc.getNickname();
    if (ni != null) {
      if (ni.getNicknameDefault() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.NICKNAME_DEFAULT,
            ni.getNicknameDefault()));
      }
      if (ni.getNicknameInitials() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.NICKNAME_INITIALS,
            ni.getNicknameInitials()));
      }
      if (ni.getNicknameMaiden() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.NICKNAME_MAIDEN,
            ni.getNicknameMaiden()));
      }
      if (ni.getNicknameOther() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.NICKNAME_OTHER,
            ni.getNicknameOther()));
      }
      if (ni.getNicknameShort() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.NICKNAME_SHORT,
            ni.getNicknameShort()));
      }
    }

    NoteSync noteSync = gc.getNote();
    if (noteSync != null) {
      if (noteSync.getNotes() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.NOTES, gc.getNote().getNotes()));
      }
    }

    OrganizationSync or = gc.getOrganization();
    if (or != null) {
      if (or.getOrganizationOtherCompany() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.ORGANIZATION_OTHER_COMPANY,
            or.getOrganizationOtherCompany()));
      }
      if (or.getOrganizationOtherDepartment() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.ORGANIZATION_OTHER_DEPARTMENT,
            or.getOrganizationOtherDepartment()));
      }
      if (or.getOrganizationOtherJobDescription() != null) {
        mod.add(new Modification(ModificationType.REPLACE,
            Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION, or.getOrganizationOtherJobDescription()));
      }
      if (or.getOrganizationOtherOfficeLocation() != null) {
        mod.add(new Modification(ModificationType.REPLACE,
            Constants.ORGANIZATION_OTHER_OFFICE_LOCATION, or.getOrganizationOtherOfficeLocation()));
      }
      if (or.getOrganizationOtherPhoneticName() != null) {
        mod.add(new Modification(ModificationType.REPLACE,
            Constants.ORGANIZATION_OTHER_PHONETIC_NAME, or.getOrganizationOtherPhoneticName()));
      }
      if (or.getOrganizationOtherPhoneticNameStyle() != null) {
        mod.add(new Modification(ModificationType.REPLACE,
            Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE,
            or.getOrganizationOtherPhoneticNameStyle()));
      }
      if (or.getOrganizationOtherSymbol() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.ORGANIZATION_OTHER_SYMBOL,
            or.getOrganizationOtherSymbol()));
      }
      if (or.getOrganizationOtherTitle() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.ORGANIZATION_OTHER_TITLE,
            or.getOrganizationOtherTitle()));
      }

      if (or.getOrganizationWorkCompany() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.ORGANIZATION_WORK_COMPANY,
            or.getOrganizationWorkCompany()));
      }
      if (or.getOrganizationWorkDepartment() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.ORGANIZATION_WORK_DEPARTMENT,
            or.getOrganizationWorkDepartment()));
      }
      if (or.getOrganizationWorkJobDescription() != null) {
        mod.add(new Modification(ModificationType.REPLACE,
            Constants.ORGANIZATION_WORK_JOB_DESCRIPTION, or.getOrganizationWorkJobDescription()));
      }
      if (or.getOrganizationWorkOfficeLocation() != null) {
        mod.add(new Modification(ModificationType.REPLACE,
            Constants.ORGANIZATION_WORK_OFFICE_LOCATION, or.getOrganizationWorkOfficeLocation()));
      }
      if (or.getOrganizationWorkPhoneticName() != null) {
        mod.add(new Modification(ModificationType.REPLACE,
            Constants.ORGANIZATION_WORK_PHONETIC_NAME, or.getOrganizationWorkPhoneticName()));
      }
      if (or.getOrganizationWorkPhoneticNameStyle() != null) {
        mod.add(new Modification(ModificationType.REPLACE,
            Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE,
            or.getOrganizationWorkPhoneticNameStyle()));
      }
      if (or.getOrganizationWorkSymbol() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.ORGANIZATION_WORK_SYMBOL,
            or.getOrganizationWorkSymbol()));
      }
      if (or.getOrganizationWorkTitle() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.ORGANIZATION_WORK_TITLE,
            or.getOrganizationWorkTitle()));
      }
    }

    PhoneSync ph = gc.getPhone();
    if (ph != null) {
      if (ph.getPhoneAssistant() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_ASSISTANT,
            ph.getPhoneAssistant()));
      }
      if (ph.getPhoneCallback() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_CALLBACK,
            ph.getPhoneCallback()));
      }
      if (ph.getPhoneCar() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_CAR, ph.getPhoneCar()));
      }
      if (ph.getPhoneCompany() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_COMPANY,
            ph.getPhoneCompany()));
      }
      if (ph.getPhoneFaxHome() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_FAX_HOME,
            ph.getPhoneFaxHome()));
      }
      if (ph.getPhoneFaxWork() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_FAX_WORK,
            ph.getPhoneFaxWork()));
      }
      if (ph.getPhoneHome() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_HOME, ph.getPhoneHome()));
      }
      if (ph.getPhoneISDN() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_ISDN, ph.getPhoneISDN()));
      }
      if (ph.getPhoneMain() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_MAIN, ph.getPhoneMain()));
      }
      if (ph.getPhoneMMS() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_MMS, ph.getPhoneMMS()));
      }
      if (ph.getPhoneMobile() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_MOBILE,
            ph.getPhoneMobile()));
      }
      if (ph.getPhoneOther() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_OTHER,
            ph.getPhoneOther()));
      }
      if (ph.getPhoneOtherFax() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_OTHER_FAX,
            ph.getPhoneOtherFax()));
      }
      if (ph.getPhonePager() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_PAGER,
            ph.getPhonePager()));
      }
      if (ph.getPhoneRadio() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_RADIO,
            ph.getPhoneRadio()));
      }
      if (ph.getPhoneTelex() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_TELEX,
            ph.getPhoneTelex()));
      }
      if (ph.getPhoneTTYTDD() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_TTY_TDD,
            ph.getPhoneTTYTDD()));
      }
      if (ph.getPhoneWork() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_WORK, ph.getPhoneWork()));
      }
      if (ph.getPhoneWorkMobile() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_WORK_MOBILE,
            ph.getPhoneWorkMobile()));
      }
      if (ph.getPhoneWorkPager() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONE_WORK_PAGER,
            ph.getPhoneWorkPager()));
      }
    }

    RelationSync re = gc.getRelation();
    if (re != null) {
      if (re.getRelationAssistant() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_ASSISTANT,
            re.getRelationAssistant()));
      }
      if (re.getRelationBrother() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_BROTHER,
            re.getRelationBrother()));
      }
      if (re.getRelationChild() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_CHILD,
            re.getRelationChild()));
      }
      if (re.getRelationDomesticPartner() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_DOMESTIC_PARTNER,
            re.getRelationDomesticPartner()));
      }
      if (re.getRelationFather() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_FATHER,
            re.getRelationFather()));
      }
      if (re.getRelationFriend() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_FRIEND,
            re.getRelationFriend()));
      }
      if (re.getRelationManager() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_MANAGER,
            re.getRelationManager()));
      }
      if (re.getRelationMother() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_MOTHER,
            re.getRelationMother()));
      }
      if (re.getRelationParent() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_PARENT,
            re.getRelationParent()));
      }
      if (re.getRelationPartner() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_PARTNER,
            re.getRelationPartner()));
      }
      if (re.getRelationRefferedBy() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_REFFERED_BY,
            re.getRelationRefferedBy()));
      }
      if (re.getRelationRelative() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_RELATIVE,
            re.getRelationRelative()));
      }
      if (re.getRelationSister() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_SISTER,
            re.getRelationSister()));
      }
      if (re.getRelationSpouse() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.RELATION_SPOUSE,
            re.getRelationSpouse()));
      }
    }

    SipAddressSync si = gc.getSipAddress();
    if (si != null) {
      if (si.getHomeSip() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.HOME_SIP, si.getHomeSip()));
      }
      if (si.getOtherSip() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.OTHER_SIP, si.getOtherSip()));
      }
      if (si.getWorkSip() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WORK_SIP, si.getWorkSip()));
      }
    }

    StructuredNameSync sn = gc.getStructuredName();
    if (sn != null) {
      if (sn.getDisplayName() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.DISPLAY_NAME,
            sn.getDisplayName()));
      }
      if (sn.getFamilyName() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.FAMILY_NAME,
            sn.getFamilyName()));
      }
      if (sn.getGivenName() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.GIVEN_NAME, sn.getGivenName()));
      }
      if (sn.getMiddleName() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.MIDDLE_NAME,
            sn.getMiddleName()));
      }
      if (sn.getNamePrefix() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.NAME_PREFIX,
            sn.getNamePrefix()));
      }
      if (sn.getNameSuffix() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.NAME_SUFFIX,
            sn.getNameSuffix()));
      }
      if (sn.getPhoneticFamilyName() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONETIC_FAMILY_NAME,
            sn.getPhoneticFamilyName()));
      }
      if (sn.getPhoneticGivenName() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONETIC_GIVEN_NAME,
            sn.getPhoneticGivenName()));
      }
      if (sn.getPhoneticMiddleName() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.PHONETIC_MIDDLE_NAME,
            sn.getPhoneticFamilyName()));
      }
    }

    StructuredPostalSync sp = gc.getStructuredPostal();
    if (sp != null) {
      if (sp.getHomeCity() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.HOME_CITY, sp.getHomeCity()));
      }
      if (sp.getHomeCountry() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.HOME_COUNTRY,
            sp.getHomeCountry()));
      }
      if (sp.getHomeFormattedAddress() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.HOME_FORMATTED_ADDRESS,
            sp.getHomeFormattedAddress()));
      }
      if (sp.getHomeNeighborhood() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.HOME_NEIGHBORHOOD,
            sp.getHomeNeighborhood()));
      }
      if (sp.getHomePOBox() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.HOME_POBOX, sp.getHomePOBox()));
      }
      if (sp.getHomePostalCode() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.HOME_POSTAL_CODE,
            sp.getHomePostalCode()));
      }
      if (sp.getHomeRegion() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.HOME_REGION,
            sp.getHomeRegion()));
      }
      if (sp.getHomeStreet() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.HOME_STREET,
            sp.getHomeStreet()));
      }
      if (sp.getWorkCity() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WORK_CITY, sp.getWorkCity()));
      }
      if (sp.getWorkCountry() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WORK_COUNTRY,
            sp.getWorkCountry()));
      }
      if (sp.getWorkFormattedAddress() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WORK_FORMATTED_ADDRESS,
            sp.getWorkFormattedAddress()));
      }
      if (sp.getWorkNeighborhood() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WORK_NEIGHBORHOOD,
            sp.getWorkNeighborhood()));
      }
      if (sp.getWorkPOBox() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WORK_POBOX, sp.getWorkPOBox()));
      }
      if (sp.getWorkPostalCode() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WORK_POSTAL_CODE,
            sp.getWorkPostalCode()));
      }
      if (sp.getWorkRegion() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WORK_REGION,
            sp.getWorkRegion()));
      }
      if (sp.getWorkStreet() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WORK_STREET,
            sp.getWorkStreet()));
      }
      if (sp.getOtherCity() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.OTHER_CITY, sp.getOtherCity()));
      }
      if (sp.getOtherCountry() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.OTHER_COUNTRY,
            sp.getOtherCountry()));
      }
      if (sp.getOtherFormattedAddress() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.OTHER_FORMATTED_ADDRESS,
            sp.getOtherFormattedAddress()));
      }
      if (sp.getOtherNeighborhood() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.OTHER_NEIGHBORHOOD,
            sp.getOtherNeighborhood()));
      }
      if (sp.getOtherPOBox() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.OTHER_POBOX,
            sp.getOtherPOBox()));
      }
      if (sp.getOtherPostalCode() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.OTHER_POSTAL_CODE,
            sp.getOtherPostalCode()));
      }
      if (sp.getOtherRegion() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.OTHER_REGION,
            sp.getOtherRegion()));
      }
      if (sp.getOtherStreet() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.OTHER_STREET,
            sp.getOtherStreet()));
      }
    }

    WebsiteSync we = gc.getWebsite();
    if (we != null) {
      if (we.getWebsiteBlog() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WEBSITE_BLOG,
            we.getWebsiteBlog()));
      }
      if (we.getWebsiteFtp() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WEBSITE_FTP,
            we.getWebsiteFtp()));
      }
      if (we.getWebsiteHome() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WEBSITE_HOME,
            we.getWebsiteHome()));
      }
      if (we.getWebsiteHomepage() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WEBSITE_HOMEPAGE,
            we.getWebsiteHomepage()));
      }
      if (we.getWebsiteOther() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WEBSITE_OTHER,
            we.getWebsiteOther()));
      }
      if (we.getWebsiteProfile() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WEBSITE_PROFILE,
            we.getWebsiteProfile()));
      }
      if (we.getWebsiteWork() != null) {
        mod.add(new Modification(ModificationType.REPLACE, Constants.WEBSITE_WORK,
            we.getWebsiteWork()));
      }
    }
    return mod;
  }

  // TODO: vnd.com.google.cursor.item/contact_misc add by google?
  // todo groupmempber
  private static ArrayList<Attribute> fill(Cursor cursor) {

    ArrayList<Attribute> attributes = new ArrayList<Attribute>();

    String str = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE));

    if (str.equals(StructuredName.CONTENT_ITEM_TYPE)) {
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
        attributes.add(new Attribute(Constants.DISPLAY_NAME,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA2))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA2)).isEmpty()) {
        attributes.add(new Attribute(Constants.GIVEN_NAME,
            cursor.getString(cursor.getColumnIndex(Data.DATA2))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA3))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA3)).isEmpty()) {
        attributes.add(new Attribute(Constants.FAMILY_NAME,
            cursor.getString(cursor.getColumnIndex(Data.DATA3))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
        attributes.add(new Attribute(Constants.NAME_PREFIX,
            cursor.getString(cursor.getColumnIndex(Data.DATA4))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
        attributes.add(new Attribute(Constants.MIDDLE_NAME,
            cursor.getString(cursor.getColumnIndex(Data.DATA5))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
        attributes.add(new Attribute(Constants.NAME_SUFFIX,
            cursor.getString(cursor.getColumnIndex(Data.DATA6))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
        attributes.add(new Attribute(Constants.PHONETIC_GIVEN_NAME,
            cursor.getString(cursor.getColumnIndex(Data.DATA7))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
        attributes.add(new Attribute(Constants.PHONETIC_MIDDLE_NAME,
            cursor.getString(cursor.getColumnIndex(Data.DATA8))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
        attributes.add(new Attribute(Constants.PHONETIC_FAMILY_NAME,
            cursor.getString(cursor.getColumnIndex(Data.DATA9))));
      }
    } else if (str.equals(Phone.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Phone.TYPE_CUSTOM) {
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String LABEL DATA3
      } else if (type == Phone.TYPE_ASSISTANT) {
        attributes.add(new Attribute(Constants.PHONE_ASSISTANT,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_CALLBACK) {
        attributes.add(new Attribute(Constants.PHONE_CALLBACK,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_CAR) {
        attributes.add(new Attribute(Constants.PHONE_CAR,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_COMPANY_MAIN) {
        attributes.add(new Attribute(Constants.PHONE_COMPANY,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_FAX_HOME) {
        attributes.add(new Attribute(Constants.PHONE_FAX_HOME,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_FAX_WORK) {
        attributes.add(new Attribute(Constants.PHONE_FAX_WORK,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_HOME) {
        attributes.add(new Attribute(Constants.PHONE_HOME,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_ISDN) {
        attributes.add(new Attribute(Constants.PHONE_ISDN,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_MAIN) {
        attributes.add(new Attribute(Constants.PHONE_MAIN,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_MMS) {
        attributes.add(new Attribute(Constants.PHONE_MMS,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_MOBILE) {
        attributes.add(new Attribute(Constants.PHONE_MOBILE,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_OTHER) {
        attributes.add(new Attribute(Constants.PHONE_OTHER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_OTHER_FAX) {
        attributes.add(new Attribute(Constants.PHONE_OTHER_FAX,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_PAGER) {
        attributes.add(new Attribute(Constants.PHONE_PAGER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_RADIO) {
        attributes.add(new Attribute(Constants.PHONE_RADIO,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_TELEX) {
        attributes.add(new Attribute(Constants.PHONE_TELEX,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_TTY_TDD) {
        attributes.add(new Attribute(Constants.PHONE_TTY_TDD,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_WORK) {
        attributes.add(new Attribute(Constants.PHONE_WORK,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_WORK_MOBILE) {
        attributes.add(new Attribute(Constants.PHONE_WORK_MOBILE,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_WORK_PAGER) {
        attributes.add(new Attribute(Constants.PHONE_WORK_PAGER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else {
        Log.i("NOT SUPPORTED TYPE PHONE", "NOT SUPPORTED TYPE PHONE");
      }
    } else if (str.equals(Email.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Email.TYPE_HOME) {
        attributes.add(new Attribute(Constants.HOME_MAIL,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Email.TYPE_WORK) {
        attributes.add(new Attribute(Constants.WORK_MAIL,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Email.TYPE_OTHER) {
        attributes.add(new Attribute(Constants.OTHER_MAIL,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Email.TYPE_MOBILE) {
        attributes.add(new Attribute(Constants.MOBILE_MAIL,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Email.TYPE_CUSTOM) {
        // TODO:
      } else {
        Log.i("NOT SUPPORTED TYPE EMAIL", "NOT SUPPORTED TYPE EMAIL");
      }
    } else if (str.equals(Photo.CONTENT_ITEM_TYPE)) {

    } else if (str.equals(Organization.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Organization.TYPE_WORK) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_COMPANY,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_TITLE,
              cursor.getString(cursor.getColumnIndex(Data.DATA4))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_DEPARTMENT,
              cursor.getString(cursor.getColumnIndex(Data.DATA5))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION,
              cursor.getString(cursor.getColumnIndex(Data.DATA6))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_SYMBOL,
              cursor.getString(cursor.getColumnIndex(Data.DATA7))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_PHONETIC_NAME,
              cursor.getString(cursor.getColumnIndex(Data.DATA8))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_OFFICE_LOCATION,
              cursor.getString(cursor.getColumnIndex(Data.DATA9))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA10)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE,
              cursor.getString(cursor.getColumnIndex(Data.DATA10))));
        }
      } else if (type == Organization.TYPE_OTHER) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_COMPANY,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_TITLE,
              cursor.getString(cursor.getColumnIndex(Data.DATA4))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_DEPARTMENT,
              cursor.getString(cursor.getColumnIndex(Data.DATA5))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION,
              cursor.getString(cursor.getColumnIndex(Data.DATA6))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_SYMBOL,
              cursor.getString(cursor.getColumnIndex(Data.DATA7))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_PHONETIC_NAME,
              cursor.getString(cursor.getColumnIndex(Data.DATA8))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION,
              cursor.getString(cursor.getColumnIndex(Data.DATA9))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA10)).isEmpty()) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE,
              cursor.getString(cursor.getColumnIndex(Data.DATA10))));
        }
      } else if (type == Organization.TYPE_CUSTOM) {
        // TODO: String LABEL DATA3
      } else {

      }
    } else if (str.equals(Im.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      Integer protocol = cursor.getInt(cursor.getColumnIndex(Data.DATA5));
      if (type == Im.TYPE_CUSTOM) {
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String LABEL DATA3
      } else if (type == Im.TYPE_HOME) {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String CUSTOM_PROTOCOL DATA6
          // TODO:
        } else if (protocol == Im.PROTOCOL_AIM) {
          attributes.add(new Attribute(Constants.IM_HOME_AIM,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
          attributes.add(new Attribute(Constants.IM_HOME_GOOGLE_TALK,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_ICQ) {
          attributes.add(new Attribute(Constants.IM_HOME_ICQ,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_JABBER) {
          attributes.add(new Attribute(Constants.IM_HOME_JABBER,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_MSN) {
          attributes.add(new Attribute(Constants.IM_HOME_MSN,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_NETMEETING) {
          attributes.add(new Attribute(Constants.IM_HOME_NETMEETING,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_QQ) {
          attributes.add(new Attribute(Constants.IM_HOME_QQ,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_SKYPE) {
          attributes.add(new Attribute(Constants.IM_HOME_SKYPE,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_YAHOO) {
          attributes.add(new Attribute(Constants.IM_HOME_YAHOO,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else if (type == Im.TYPE_WORK) {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String CUSTOM_PROTOCOL DATA6
          // TODO:
        } else if (protocol == Im.PROTOCOL_AIM) {
          attributes.add(new Attribute(Constants.IM_WORK_AIM,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
          attributes.add(new Attribute(Constants.IM_WORK_GOOGLE_TALK,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_ICQ) {
          attributes.add(new Attribute(Constants.IM_WORK_ICQ,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_JABBER) {
          attributes.add(new Attribute(Constants.IM_WORK_JABBER,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_MSN) {
          attributes.add(new Attribute(Constants.IM_WORK_MSN,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_NETMEETING) {
          attributes.add(new Attribute(Constants.IM_WORK_NETMEETING,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_QQ) {
          attributes.add(new Attribute(Constants.IM_WORK_QQ,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_SKYPE) {
          attributes.add(new Attribute(Constants.IM_WORK_SKYPE,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_YAHOO) {
          attributes.add(new Attribute(Constants.IM_WORK_YAHOO,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else if (type == Im.TYPE_OTHER) {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String CUSTOM_PROTOCOL DATA6
          // TODO:
        } else if (protocol == Im.PROTOCOL_AIM) {
          attributes.add(new Attribute(Constants.IM_OTHER_AIM,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
          attributes.add(new Attribute(Constants.IM_OTHER_GOOGLE_TALK,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_ICQ) {
          attributes.add(new Attribute(Constants.IM_OTHER_ICQ,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_JABBER) {
          attributes.add(new Attribute(Constants.IM_OTHER_JABBER,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_MSN) {
          attributes.add(new Attribute(Constants.IM_OTHER_MSN,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_NETMEETING) {
          attributes.add(new Attribute(Constants.IM_OTHER_NETMEETING,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_QQ) {
          attributes.add(new Attribute(Constants.IM_OTHER_QQ,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_SKYPE) {
          attributes.add(new Attribute(Constants.IM_OTHER_SKYPE,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol == Im.PROTOCOL_YAHOO) {
          attributes.add(new Attribute(Constants.IM_OTHER_YAHOO,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String CUSTOM_PROTOCOL DATA6
          // TODO:
          /*
           * } else if (protocol == Im.PROTOCOL_AIM) { attributes.add(new
           * Attribute(Constants.IM_NULL_AIM, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
           * } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) { attributes.add(new
           * Attribute(Constants.IM_NULL_GOOGLE_TALK,
           * cursor.getString(cursor.getColumnIndex(Data.DATA1)))); } else if (protocol ==
           * Im.PROTOCOL_ICQ) { attributes.add(new Attribute(Constants.IM_NULL_ICQ,
           * cursor.getString(cursor.getColumnIndex(Data.DATA1)))); } else if (protocol ==
           * Im.PROTOCOL_JABBER) { attributes.add(new Attribute(Constants.IM_NULL_JABBER,
           * cursor.getString(cursor.getColumnIndex(Data.DATA1)))); } else if (protocol ==
           * Im.PROTOCOL_MSN) { attributes.add(new Attribute(Constants.IM_NULL_MSN,
           * cursor.getString(cursor.getColumnIndex(Data.DATA1)))); } else if (protocol ==
           * Im.PROTOCOL_NETMEETING) { attributes.add(new Attribute(Constants.IM_NULL_NETMEETING,
           * cursor.getString(cursor.getColumnIndex(Data.DATA1)))); } else if (protocol ==
           * Im.PROTOCOL_QQ) { attributes.add(new Attribute(Constants.IM_NULL_QQ,
           * cursor.getString(cursor.getColumnIndex(Data.DATA1)))); } else if (protocol ==
           * Im.PROTOCOL_SKYPE) { attributes.add(new Attribute(Constants.IM_NULL_SKYPE,
           * cursor.getString(cursor.getColumnIndex(Data.DATA1)))); } else if (protocol ==
           * Im.PROTOCOL_YAHOO) { attributes.add(new Attribute(Constants.IM_NULL_YAHOO,
           * cursor.getString(cursor.getColumnIndex(Data.DATA1))));
           */
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      }
    } else if (str.equals(Nickname.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Nickname.TYPE_DEFAULT) {
        attributes.add(new Attribute(Constants.NICKNAME_DEFAULT,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Nickname.TYPE_OTHER_NAME) {
        attributes.add(new Attribute(Constants.NICKNAME_OTHER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Nickname.TYPE_MAIDEN_NAME) {
        attributes.add(new Attribute(Constants.NICKNAME_MAIDEN,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Nickname.TYPE_SHORT_NAME) {
        attributes.add(new Attribute(Constants.NICKNAME_SHORT,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Nickname.TYPE_INITIALS) {
        attributes.add(new Attribute(Constants.NICKNAME_INITIALS,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Nickname.TYPE_CUSTOM) {
        // TODO: String LABEL DATA3
      } else {
        Log.i("NOT SUPPORTED TYPE NICKNAME", "NOT SUPPORTED TYPE NICKANEME");
      }
    } else if (str.equals(Note.CONTENT_ITEM_TYPE)) {
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
        // attributes.add(new Attribute(Constants.NOTES,
        // cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      }
    } else if (str.equals(StructuredPostal.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == StructuredPostal.TYPE_CUSTOM) {
        // TODO:
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String LABEL DATA3
      } else if (type == StructuredPostal.TYPE_HOME) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
          attributes.add(new Attribute(Constants.HOME_FORMATTED_ADDRESS,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
          attributes.add(new Attribute(Constants.HOME_STREET,
              cursor.getString(cursor.getColumnIndex(Data.DATA4))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
          attributes.add(new Attribute(Constants.HOME_POBOX,
              cursor.getString(cursor.getColumnIndex(Data.DATA5))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
          attributes.add(new Attribute(Constants.HOME_NEIGHBORHOOD,
              cursor.getString(cursor.getColumnIndex(Data.DATA6))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
          attributes.add(new Attribute(Constants.HOME_CITY,
              cursor.getString(cursor.getColumnIndex(Data.DATA7))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
          attributes.add(new Attribute(Constants.HOME_REGION,
              cursor.getString(cursor.getColumnIndex(Data.DATA8))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
          attributes.add(new Attribute(Constants.HOME_POSTAL_CODE,
              cursor.getString(cursor.getColumnIndex(Data.DATA9))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA10)).isEmpty()) {
          attributes.add(new Attribute(Constants.HOME_COUNTRY,
              cursor.getString(cursor.getColumnIndex(Data.DATA10))));
        }
      } else if (type == StructuredPostal.TYPE_WORK) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
          attributes.add(new Attribute(Constants.WORK_FORMATTED_ADDRESS,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
          attributes.add(new Attribute(Constants.WORK_STREET,
              cursor.getString(cursor.getColumnIndex(Data.DATA4))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
          attributes.add(new Attribute(Constants.WORK_POBOX,
              cursor.getString(cursor.getColumnIndex(Data.DATA5))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
          attributes.add(new Attribute(Constants.WORK_NEIGHBORHOOD,
              cursor.getString(cursor.getColumnIndex(Data.DATA6))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
          attributes.add(new Attribute(Constants.WORK_CITY,
              cursor.getString(cursor.getColumnIndex(Data.DATA7))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
          attributes.add(new Attribute(Constants.WORK_REGION,
              cursor.getString(cursor.getColumnIndex(Data.DATA8))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
          attributes.add(new Attribute(Constants.WORK_POSTAL_CODE,
              cursor.getString(cursor.getColumnIndex(Data.DATA9))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA10)).isEmpty()) {
          attributes.add(new Attribute(Constants.WORK_COUNTRY,
              cursor.getString(cursor.getColumnIndex(Data.DATA10))));
        }
      } else if (type == StructuredPostal.TYPE_OTHER) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
          attributes.add(new Attribute(Constants.OTHER_FORMATTED_ADDRESS,
              cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
          attributes.add(new Attribute(Constants.OTHER_STREET,
              cursor.getString(cursor.getColumnIndex(Data.DATA4))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
          attributes.add(new Attribute(Constants.OTHER_POBOX,
              cursor.getString(cursor.getColumnIndex(Data.DATA5))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
          attributes.add(new Attribute(Constants.OTHER_NEIGHBORHOOD,
              cursor.getString(cursor.getColumnIndex(Data.DATA6))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
          attributes.add(new Attribute(Constants.OTHER_CITY,
              cursor.getString(cursor.getColumnIndex(Data.DATA7))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
          attributes.add(new Attribute(Constants.OTHER_REGION,
              cursor.getString(cursor.getColumnIndex(Data.DATA8))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
          attributes.add(new Attribute(Constants.OTHER_POSTAL_CODE,
              cursor.getString(cursor.getColumnIndex(Data.DATA9))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA10)).isEmpty()) {
          attributes.add(new Attribute(Constants.OTHER_COUNTRY,
              cursor.getString(cursor.getColumnIndex(Data.DATA10))));
        }
      } else {

      }

    } else if (str.equals(GroupMembership.CONTENT_ITEM_TYPE)) {
      // TODO:
      // long GROUP_ROW_ID DATA1
      // attributes.add(new Attribute(Constants.,
      // cursor.getString(cursor.getColumnIndex(Data.DATA1))));
    } else if (str.equals(Website.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Website.TYPE_CUSTOM) {
        // TODO:String LABEL DATA3,TYPE_CUSTOM. Put the actual type in LABEL.
      } else if (type == Website.TYPE_BLOG) {
        attributes.add(new Attribute(Constants.WEBSITE_BLOG,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Website.TYPE_FTP) {
        attributes.add(new Attribute(Constants.WEBSITE_FTP,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Website.TYPE_HOME) {
        attributes.add(new Attribute(Constants.WEBSITE_HOME,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Website.TYPE_HOMEPAGE) {
        attributes.add(new Attribute(Constants.WEBSITE_HOMEPAGE,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Website.TYPE_OTHER) {
        // attributes.add(new Attribute(Constants.WEBSITE_OTHER,
        // cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Website.TYPE_PROFILE) {
        attributes.add(new Attribute(Constants.WEBSITE_PROFILE,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Website.TYPE_WORK) {
        attributes.add(new Attribute(Constants.WEBSITE_WORK,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else {
        Log.i("NOT SUPPORTED TYPE WEBSITE", "NOT SUPPORTED TYPE WEBSITE");
      }
    } else if (str.equals(Event.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Event.TYPE_CUSTOM) {
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String LABEL DATA3
      } else if (type == Event.TYPE_ANNIVERSARY) {
        attributes.add(new Attribute(Constants.EVENT_ANNIVERSARY,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Event.TYPE_BIRTHDAY) {
        attributes.add(new Attribute(Constants.EVENT_BIRTHDAY,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Event.TYPE_OTHER) {
        attributes.add(new Attribute(Constants.EVENT_OTHER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else {
        Log.i("NOT SUPPORTED TYPE Event", "NOT SUPPORTED TYPE EVENT");
      }
    } else if (str.equals(Relation.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Relation.TYPE_CUSTOM) {
        // TODO:
        // attributes.add(new Attribute(Constants.EVENT_ANNIVERSARY,
        // cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        // String LABEL DATA3
      } else if (type == Relation.TYPE_ASSISTANT) {
        attributes.add(new Attribute(Constants.RELATION_ASSISTANT,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_BROTHER) {
        attributes.add(new Attribute(Constants.RELATION_BROTHER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_CHILD) {
        attributes.add(new Attribute(Constants.RELATION_CHILD,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_DOMESTIC_PARTNER) {
        attributes.add(new Attribute(Constants.RELATION_DOMESTIC_PARTNER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_FATHER) {
        attributes.add(new Attribute(Constants.RELATION_FATHER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_FRIEND) {
        attributes.add(new Attribute(Constants.RELATION_FRIEND,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_MANAGER) {
        attributes.add(new Attribute(Constants.RELATION_MANAGER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_MOTHER) {
        attributes.add(new Attribute(Constants.RELATION_MOTHER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_PARENT) {
        attributes.add(new Attribute(Constants.RELATION_PARENT,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_PARTNER) {
        attributes.add(new Attribute(Constants.RELATION_PARTNER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_REFERRED_BY) {
        attributes.add(new Attribute(Constants.RELATION_REFFERED_BY,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_RELATIVE) {
        attributes.add(new Attribute(Constants.RELATION_RELATIVE,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_SISTER) {
        attributes.add(new Attribute(Constants.RELATION_SISTER,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Relation.TYPE_SPOUSE) {
        attributes.add(new Attribute(Constants.RELATION_SPOUSE,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else {
        Log.i("NOT SUPPORTED TYPE Relation", "NOT SUPPORTED TYPE Relation");
      }
    } else if (str.equals(SipAddress.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == SipAddress.TYPE_CUSTOM) {
        // TODO:
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String LABEL DATA3
      } else if (type == SipAddress.TYPE_HOME) {
        attributes.add(new Attribute(Constants.HOME_SIP,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == SipAddress.TYPE_WORK) {
        attributes.add(new Attribute(Constants.WORK_SIP,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == SipAddress.TYPE_OTHER) {
        attributes.add(new Attribute(Constants.OTHER_SIP,
            cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else {
        Log.i("NOT SUPPORTED TYPE SIP", "NOT SUPPORTED TYPE SIP");
      }
    } else if (str.equals(Identity.CONTENT_ITEM_TYPE)) {
      // attributes.add(new Attribute(Constants.IDENTITY_TEXT,
      // cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      // attributes.add(new Attribute(Constants.IDENTITY_NAMESPACE,
      // cursor.getString(cursor.getColumnIndex(Data.DATA2))));
    } else {
      Log.i("NOT SUPPORTED TYPE MIME", "NOT SUPPORTED TYPE MIME");
    }

    return attributes;
  }

  private StructuredNameSync fillStructuredName(final Cursor cursor) {
    StructuredNameSync structuredNameSync = new StructuredNameSync();
    structuredNameSync.getID().add(new ID(StructuredName.CONTENT_ITEM_TYPE, null,
        cursor.getString(cursor.getColumnIndex(Data._ID))));
    if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
        && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
      structuredNameSync.setDisplayName(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
    }
    if (!cursor.isNull(cursor.getColumnIndex(Data.DATA2))
        && !cursor.getString(cursor.getColumnIndex(Data.DATA2)).isEmpty()) {
      structuredNameSync.setGivenName(cursor.getString(cursor.getColumnIndex(Data.DATA2)));
    }
    if (!cursor.isNull(cursor.getColumnIndex(Data.DATA3))
        && !cursor.getString(cursor.getColumnIndex(Data.DATA3)).isEmpty()) {
      structuredNameSync.setFamilyName(cursor.getString(cursor.getColumnIndex(Data.DATA3)));
    }
    if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
        && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
      structuredNameSync.setNamePrefix(cursor.getString(cursor.getColumnIndex(Data.DATA4)));
    }
    if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
        && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
      structuredNameSync.setMiddleName(cursor.getString(cursor.getColumnIndex(Data.DATA5)));
    }
    if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
        && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
      structuredNameSync.setNameSuffix(cursor.getString(cursor.getColumnIndex(Data.DATA6)));
    }
    if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
        && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
      structuredNameSync.setPhoneticGivenName(cursor.getString(cursor.getColumnIndex(Data.DATA7)));
    }
    if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
        && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
      structuredNameSync.setPhoneticMiddleName(cursor.getString(cursor.getColumnIndex(Data.DATA8)));
    }
    if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
        && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
      structuredNameSync.setPhoneticFamilyName(cursor.getString(cursor.getColumnIndex(Data.DATA9)));
    }

    return structuredNameSync;
  }

  private void fillContact(final Cursor cursor, GoogleContact contact) {

    String mimeType = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE));


    if (mimeType.equals(StructuredName.CONTENT_ITEM_TYPE)) {
      contact.setStructuredName(fillStructuredName(cursor));
    } else if (mimeType.equals(Phone.CONTENT_ITEM_TYPE)) {
      contact.initPhone();
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      contact.getPhone()
          .getID()
          .add(new ID(type.toString(), null, cursor.getString(cursor.getColumnIndex(Data._ID))));
      if (type == Phone.TYPE_CUSTOM) {
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String LABEL DATA3
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
        Log.d("NOT SUPPORTED TYPE PHONE", "NOT SUPPORTED TYPE PHONE");
      }
    } else if (mimeType.equals(Email.CONTENT_ITEM_TYPE)) {
      contact.initEmail();
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      contact.getEmail()
          .getID()
          .add(new ID(type.toString(), null, cursor.getString(cursor.getColumnIndex(Data._ID))));
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
        Log.d("NOT SUPPORTED TYPE EMAIL", "NOT SUPPORTED TYPE EMAIL");
      }
    } else if (mimeType.equals(Photo.CONTENT_ITEM_TYPE)) {
      Log.d("NOT SUPPORTED TYPE Photo", "NOT SUPPORTED TYPE Photo");
    } else if (mimeType.equals(Organization.CONTENT_ITEM_TYPE)) {
      contact.initOrganization();
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      contact.getOrganization()
          .getID()
          .add(new ID(type.toString(), null, cursor.getString(cursor.getColumnIndex(Data._ID))));
      if (type == Organization.TYPE_WORK) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
          contact.getOrganization().setOrganizationWorkCompany(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
          contact.getOrganization().setOrganizationWorkTitle(
              cursor.getString(cursor.getColumnIndex(Data.DATA4)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
          contact.getOrganization().setOrganizationWorkDepartment(
              cursor.getString(cursor.getColumnIndex(Data.DATA5)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
          contact.getOrganization().setOrganizationWorkJobDescription(
              cursor.getString(cursor.getColumnIndex(Data.DATA6)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
          contact.getOrganization().setOrganizationWorkSymbol(
              cursor.getString(cursor.getColumnIndex(Data.DATA7)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
          contact.getOrganization().setOrganizationWorkPhoneticName(
              cursor.getString(cursor.getColumnIndex(Data.DATA8)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
          contact.getOrganization().setOrganizationWorkOfficeLocation(
              cursor.getString(cursor.getColumnIndex(Data.DATA9)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA10)).isEmpty()) {
          contact.getOrganization().setOrganizationWorkPhoneticNameStyle(
              cursor.getString(cursor.getColumnIndex(Data.DATA10)));
        }
      } else if (type == Organization.TYPE_OTHER) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
          contact.getOrganization().setOrganizationOtherCompany(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
          contact.getOrganization().setOrganizationOtherTitle(
              cursor.getString(cursor.getColumnIndex(Data.DATA4)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
          contact.getOrganization().setOrganizationOtherDepartment(
              cursor.getString(cursor.getColumnIndex(Data.DATA5)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
          contact.getOrganization().setOrganizationOtherJobDescription(
              cursor.getString(cursor.getColumnIndex(Data.DATA6)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
          contact.getOrganization().setOrganizationOtherSymbol(
              cursor.getString(cursor.getColumnIndex(Data.DATA7)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
          contact.getOrganization().setOrganizationOtherPhoneticName(
              cursor.getString(cursor.getColumnIndex(Data.DATA8)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
          contact.getOrganization().setOrganizationOtherOfficeLocation(
              cursor.getString(cursor.getColumnIndex(Data.DATA9)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA10)).isEmpty()) {
          contact.getOrganization().setOrganizationOtherPhoneticNameStyle(
              cursor.getString(cursor.getColumnIndex(Data.DATA10)));
        }
      } else if (type == Organization.TYPE_CUSTOM) {
        Log.d("NOT SUPPORTED TYPE Organization", "NOT SUPPORTED TYPE TYPE_CUSTOM");
      } else {
        Log.d("NOT SUPPORTED TYPE Organization", "NOT SUPPORTED TYPE else");
      }
    } else if (mimeType.equals(Im.CONTENT_ITEM_TYPE)) {
      contact.initIm();
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      Integer protocol = cursor.getInt(cursor.getColumnIndex(Data.DATA5));
      contact.getImSync()
          .getID()
          .add(
              new ID(type.toString(), protocol.toString(),
                  cursor.getString(cursor.getColumnIndex(Data._ID))));
      if (type == Im.TYPE_CUSTOM) {
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String LABEL DATA3
        Log.d("NOT SUPPORTED TYPE Im", "NOT SUPPORTED TYPE_CUSTOM");
      } else if (type == Im.TYPE_HOME) {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String CUSTOM_PROTOCOL DATA6
          Log.d("NOT SUPPORTED TYPE Im", "NOT SUPPORTED PROTOCOL_CUSTOM");
          // TODO:
        } else if (protocol == Im.PROTOCOL_AIM) {
          contact.getImSync().setImHomeAim(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
          contact.getImSync().setImHomeGoogleTalk(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_ICQ) {
          contact.getImSync().setImHomeIcq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_JABBER) {
          contact.getImSync().setImHomeJabber(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_MSN) {
          contact.getImSync().setImHomeMsn(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_NETMEETING) {
          contact.getImSync().setImHomeNetmeeting(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_QQ) {
          contact.getImSync().setImHomeQq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_SKYPE) {
          contact.getImSync().setImHomeSkype(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_YAHOO) {
          contact.getImSync().setImHomeYahoo(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else {
          Log.d("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else if (type == Im.TYPE_WORK) {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String CUSTOM_PROTOCOL DATA6
          Log.d("NOT SUPPORTED TYPE Im TYPE_WORK", "NOT SUPPORTED PROTOCOL_CUSTOM");
          // TODO:
        } else if (protocol == Im.PROTOCOL_AIM) {
          contact.getImSync().setImWorkAim(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
          contact.getImSync().setImWorkGoogleTalk(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_ICQ) {
          contact.getImSync().setImWorkIcq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_JABBER) {
          contact.getImSync().setImWorkJabber(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_MSN) {
          contact.getImSync().setImWorkMsn(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_NETMEETING) {
          contact.getImSync().setImWorkNetmeeting(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_QQ) {
          contact.getImSync().setImWorkQq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_SKYPE) {
          contact.getImSync().setImWorkSkype(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_YAHOO) {
          contact.getImSync().setImWorkYahoo(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else {
          Log.d("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else if (type == Im.TYPE_OTHER) {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String CUSTOM_PROTOCOL DATA6
          Log.d("NOT SUPPORTED TYPE Im TYPE_OTHER", "NOT SUPPORTED PROTOCOL_CUSTOM");
          // TODO:
        } else if (protocol == Im.PROTOCOL_AIM) {
          contact.getImSync().setImOtherAim(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
          contact.getImSync().setImOtherGoogleTalk(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_ICQ) {
          contact.getImSync().setImOtherIcq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_JABBER) {
          contact.getImSync().setImOtherJabber(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_MSN) {
          contact.getImSync().setImOtherMsn(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_NETMEETING) {
          contact.getImSync().setImOtherNetmeeting(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_QQ) {
          contact.getImSync().setImOtherQq(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_SKYPE) {
          contact.getImSync().setImOtherSkype(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else if (protocol == Im.PROTOCOL_YAHOO) {
          contact.getImSync().setImOtherYahoo(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        } else {
          Log.d("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else {
        if (protocol == Im.PROTOCOL_CUSTOM) {
          Log.d("NOT SUPPORTED TYPE Im ELSE", "NOT SUPPORTED PROTOCOL_CUSTOM");
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String CUSTOM_PROTOCOL DATA6
          // TODO:
          /*
           * } else if (protocol == Im.PROTOCOL_AIM) {
           * contact.getIm().setImNullAim(cursor.getString(cursor.getColumnIndex(Data.DATA1))); }
           * else if (protocol == Im.PROTOCOL_GOOGLE_TALK) {
           * contact.getIm().setImNullGoogleTalk(cursor
           * .getString(cursor.getColumnIndex(Data.DATA1))); } else if (protocol == Im.PROTOCOL_ICQ)
           * { contact.getIm().setImNullIcq(cursor.getString(cursor.getColumnIndex(Data.DATA1))); }
           * else if (protocol == Im.PROTOCOL_JABBER) {
           * contact.getIm().setImNullJabber(cursor.getString(cursor.getColumnIndex(Data.DATA1))); }
           * else if (protocol == Im.PROTOCOL_MSN) {
           * contact.getIm().setImNullMsn(cursor.getString(cursor.getColumnIndex(Data.DATA1))); }
           * else if (protocol == Im.PROTOCOL_NETMEETING) {
           * contact.getIm().setImNullNetmeeting(cursor
           * .getString(cursor.getColumnIndex(Data.DATA1))); } else if (protocol == Im.PROTOCOL_QQ)
           * { contact.getIm().setImNullQq(cursor.getString(cursor.getColumnIndex(Data.DATA1))); }
           * else if (protocol == Im.PROTOCOL_SKYPE) {
           * contact.getIm().setImNullSkype(cursor.getString(cursor.getColumnIndex(Data.DATA1))); }
           * else if (protocol == Im.PROTOCOL_YAHOO) {
           * contact.getIm().setImNullYahoo(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
           */
        } else {
          Log.d("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      }
    } else if (mimeType.equals(Nickname.CONTENT_ITEM_TYPE)) {
      contact.initNickname();
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      contact.getNickname()
          .getID()
          .add(new ID(type.toString(), null, cursor.getString(cursor.getColumnIndex(Data._ID))));
      if (type == Nickname.TYPE_DEFAULT) {
        contact.getNickname().setNicknameDefault(
            cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Nickname.TYPE_OTHER_NAME) {
        contact.getNickname().setNicknameOther(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Nickname.TYPE_MAIDEN_NAME) {
        contact.getNickname()
            .setNicknameMaiden(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Nickname.TYPE_SHORT_NAME) {
        contact.getNickname().setNicknameShort(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Nickname.TYPE_INITIALS) {
        contact.getNickname().setNicknameInitials(
            cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Nickname.TYPE_CUSTOM) {
        // TODO: String LABEL DATA3
        Log.d("NOT SUPPORTED TYPE NICKNAME", "NOT SUPPORTED TYPE TYPE_CUSTOM");
      } else {
        Log.d("NOT SUPPORTED TYPE NICKNAME", "NOT SUPPORTED TYPE NICKANEME");
      }
    } else if (mimeType.equals(Note.CONTENT_ITEM_TYPE)) {
      contact.initNote();
      contact.getNote()
          .getID()
          .add(
              new ID(Note.CONTENT_ITEM_TYPE, null,
                  cursor.getString(cursor.getColumnIndex(Data._ID))));
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
          && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
        contact.getNote().setNotes(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      }
    } else if (mimeType.equals(StructuredPostal.CONTENT_ITEM_TYPE)) {
      contact.initStructuredPostalSync();
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      contact.getStructuredPostal()
          .getID()
          .add(new ID(type.toString(), null, cursor.getString(cursor.getColumnIndex(Data._ID))));
      if (type == StructuredPostal.TYPE_CUSTOM) {
        // TODO:
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String LABEL DATA3
        Log.d("NOT SUPPORTED TYPE StructuredPostal", "NOT SUPPORTED TYPE TYPE_CUSTOM");
      } else if (type == StructuredPostal.TYPE_HOME) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
          contact.getStructuredPostal().setHomeFormattedAddress(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
          contact.getStructuredPostal().setHomeStreet(
              cursor.getString(cursor.getColumnIndex(Data.DATA4)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
          contact.getStructuredPostal().setHomePOBox(
              cursor.getString(cursor.getColumnIndex(Data.DATA5)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
          contact.getStructuredPostal().setHomeNeighborhood(
              cursor.getString(cursor.getColumnIndex(Data.DATA6)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
          contact.getStructuredPostal().setHomeCity(
              cursor.getString(cursor.getColumnIndex(Data.DATA7)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
          contact.getStructuredPostal().setHomeRegion(
              cursor.getString(cursor.getColumnIndex(Data.DATA8)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
          contact.getStructuredPostal().setHomePostalCode(
              cursor.getString(cursor.getColumnIndex(Data.DATA9)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA10)).isEmpty()) {
          contact.getStructuredPostal().setHomeCountry(
              cursor.getString(cursor.getColumnIndex(Data.DATA10)));
          Log.d("MAPPING home jeempty:", cursor.getString(cursor.getColumnIndex(Data.DATA10)) + ":");
        }
      } else if (type == StructuredPostal.TYPE_WORK) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
          contact.getStructuredPostal().setWorkFormattedAddress(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
          contact.getStructuredPostal().setWorkStreet(
              cursor.getString(cursor.getColumnIndex(Data.DATA4)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
          contact.getStructuredPostal().setWorkPOBox(
              cursor.getString(cursor.getColumnIndex(Data.DATA5)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
          contact.getStructuredPostal().setWorkNeighborhood(
              cursor.getString(cursor.getColumnIndex(Data.DATA6)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
          contact.getStructuredPostal().setWorkCity(
              cursor.getString(cursor.getColumnIndex(Data.DATA7)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
          contact.getStructuredPostal().setWorkRegion(
              cursor.getString(cursor.getColumnIndex(Data.DATA8)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
          contact.getStructuredPostal().setWorkPostalCode(
              cursor.getString(cursor.getColumnIndex(Data.DATA9)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA10)).isEmpty()) {
          contact.getStructuredPostal().setWorkCountry(
              cursor.getString(cursor.getColumnIndex(Data.DATA10)));
        }
      } else if (type == StructuredPostal.TYPE_OTHER) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA1)).isEmpty()) {
          contact.getStructuredPostal().setOtherFormattedAddress(
              cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA4)).isEmpty()) {
          contact.getStructuredPostal().setOtherStreet(
              cursor.getString(cursor.getColumnIndex(Data.DATA4)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA5)).isEmpty()) {
          contact.getStructuredPostal().setOtherPOBox(
              cursor.getString(cursor.getColumnIndex(Data.DATA5)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA6)).isEmpty()) {
          contact.getStructuredPostal().setOtherNeighborhood(
              cursor.getString(cursor.getColumnIndex(Data.DATA6)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA7)).isEmpty()) {
          contact.getStructuredPostal().setOtherCity(
              cursor.getString(cursor.getColumnIndex(Data.DATA7)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA8)).isEmpty()) {
          contact.getStructuredPostal().setOtherRegion(
              cursor.getString(cursor.getColumnIndex(Data.DATA8)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA9)).isEmpty()) {
          contact.getStructuredPostal().setOtherPostalCode(
              cursor.getString(cursor.getColumnIndex(Data.DATA9)));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))
            && !cursor.getString(cursor.getColumnIndex(Data.DATA10)).isEmpty()) {
          contact.getStructuredPostal().setOtherCountry(
              cursor.getString(cursor.getColumnIndex(Data.DATA10)));
        }
      } else {
        Log.d("NOT SUPPORTED TYPE StructuredPostal", "NOT SUPPORTED ELSE");
      }

    } else if (mimeType.equals(GroupMembership.CONTENT_ITEM_TYPE)) {
      // TODO:
      // long GROUP_ROW_ID DATA1
      // attributes.add(new Attribute(Constants.,
      // cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      Log.d("NOT SUPPORTED TYPE GroupMembership", "NOT SUPPORTED TYPE ");
    } else if (mimeType.equals(Website.CONTENT_ITEM_TYPE)) {
      contact.initWebsite();
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      contact.getWebsite()
          .getID()
          .add(new ID(type.toString(), null, cursor.getString(cursor.getColumnIndex(Data._ID))));
      if (type == Website.TYPE_CUSTOM) {
        // TODO:String LABEL DATA3,TYPE_CUSTOM. Put the actual type in LABEL.
        Log.d("NOT SUPPORTED TYPE Website", "NOT SUPPORTED TYPE_CUSTOM ");
      } else if (type == Website.TYPE_BLOG) {
        contact.getWebsite().setWebsiteBlog(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_FTP) {
        contact.getWebsite().setWebsiteFtp(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_HOME) {
        contact.getWebsite().setWebsiteHome(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_HOMEPAGE) {
        contact.getWebsite()
            .setWebsiteHomepage(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_OTHER) {
        contact.getWebsite().setWebsiteOther(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_PROFILE) {
        contact.getWebsite().setWebsiteProfile(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Website.TYPE_WORK) {
        contact.getWebsite().setWebsiteWork(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else {
        Log.d("NOT SUPPORTED TYPE WEBSITE", "NOT SUPPORTED TYPE WEBSITE");
      }
    } else if (mimeType.equals(Event.CONTENT_ITEM_TYPE)) {
      contact.initEvent();
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      contact.getEvent()
          .getID()
          .add(new ID(type.toString(), null, cursor.getString(cursor.getColumnIndex(Data._ID))));
      if (type == Event.TYPE_CUSTOM) {
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String LABEL DATA3
        Log.d("NOT SUPPORTED TYPE Event", "NOT SUPPORTED TYPE_CUSTOM ");
      } else if (type == Event.TYPE_ANNIVERSARY) {
        contact.getEvent().setEventAnniversary(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Event.TYPE_BIRTHDAY) {
        contact.getEvent().setEventBirthday(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Event.TYPE_OTHER) {
        contact.getEvent().setEventOther(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else {
        Log.d("NOT SUPPORTED TYPE Event", "NOT SUPPORTED TYPE EVENT");
      }
    } else if (mimeType.equals(Relation.CONTENT_ITEM_TYPE)) {
      contact.initRelation();
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      contact.getRelation()
          .getID()
          .add(new ID(type.toString(), null, cursor.getString(cursor.getColumnIndex(Data._ID))));
      if (type == Relation.TYPE_CUSTOM) {
        // TODO:
        // attributes.add(new Attribute(Constants.EVENT_ANNIVERSARY,
        // cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        // String LABEL DATA3
        Log.d("NOT SUPPORTED TYPE Relation", "NOT SUPPORTED TYPE_CUSTOM ");
      } else if (type == Relation.TYPE_ASSISTANT) {
        contact.getRelation().setRelationAssistant(
            cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_BROTHER) {
        contact.getRelation().setRelationBrother(
            cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_CHILD) {
        contact.getRelation().setRelationChild(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_DOMESTIC_PARTNER) {
        contact.getRelation().setRelationDomesticPartner(
            cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_FATHER) {
        contact.getRelation()
            .setRelationFather(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_FRIEND) {
        contact.getRelation()
            .setRelationFriend(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_MANAGER) {
        contact.getRelation().setRelationManager(
            cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_MOTHER) {
        contact.getRelation()
            .setRelationMother(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_PARENT) {
        contact.getRelation()
            .setRelationParent(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_PARTNER) {
        contact.getRelation().setRelationPartner(
            cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_REFERRED_BY) {
        contact.getRelation().setRelationRefferedBy(
            cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_RELATIVE) {
        contact.getRelation().setRelationRelative(
            cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_SISTER) {
        contact.getRelation()
            .setRelationSister(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == Relation.TYPE_SPOUSE) {
        contact.getRelation()
            .setRelationSpouse(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else {
        Log.d("NOT SUPPORTED TYPE Relation", "NOT SUPPORTED TYPE Relation");
      }
    } else if (mimeType.equals(SipAddress.CONTENT_ITEM_TYPE)) {
      contact.initSipAddressSync();
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      contact.getSipAddress()
          .getID()
          .add(new ID(type.toString(), null, cursor.getString(cursor.getColumnIndex(Data._ID))));
      if (type == SipAddress.TYPE_CUSTOM) {
        // TODO:
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String LABEL DATA3
        Log.d("NOT SUPPORTED TYPE SipAddress", "NOT SUPPORTED TYPE_CUSTOM ");
      } else if (type == SipAddress.TYPE_HOME) {
        contact.getSipAddress().setHomeSip(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == SipAddress.TYPE_WORK) {
        contact.getSipAddress().setWorkSip(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else if (type == SipAddress.TYPE_OTHER) {
        contact.getSipAddress().setOtherSip(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      } else {
        Log.d("NOT SUPPORTED TYPE SIP", "NOT SUPPORTED TYPE SIP");
      }
    } else if (mimeType.equals(Identity.CONTENT_ITEM_TYPE)) {
      contact.initIdentity();
      contact.getIdentity()
          .getID()
          .add(
              new ID(Identity.CONTENT_ITEM_TYPE, null,
                  cursor.getString(cursor.getColumnIndex(Data._ID))));
      contact.getIdentity().setIdentityText(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      contact.getIdentity().setIdentityNamespace(
          cursor.getString(cursor.getColumnIndex(Data.DATA2)));
    } else {
      Log.d("NOT SUPPORTED TYPE MIME", "NOT SUPPORTED TYPE MIME: " + mimeType);
    }
  }
}
