package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A object of contact.
 *
 * @author xsendl00
 */
public class GoogleContact {

  private static final String TAG = "GoogleContact";

  private EmailSync email;
  private EventSync event;
  private IdentitySync identity;
  private ImSync im;
  private NicknameSync nickname;
  private NoteSync note;
  private OrganizationSync organization;
  private PhoneSync phone;
  private RelationSync relation;
  private SipAddressSync sipAddressSync;
  private StructuredNameSync structuredNameSync;
  private StructuredPostalSync structuredPostalSync;
  private WebsiteSync website;
  private String timestamp;
  private String uuid;
  private Integer id;
  private boolean synchronize = false;
  private String accountNamePrevious;
  private String accountTypePrevious;
  private boolean deleted = false;

  /**
   * Initialize attributes.
   */
  public void init() {
    this.email = new EmailSync();
    this.event = new EventSync();
    this.identity = new IdentitySync();
    this.im = new ImSync();
    this.nickname = new NicknameSync();
    this.note = new NoteSync();
    this.organization = new OrganizationSync();
    this.phone = new PhoneSync();
    this.relation = new RelationSync();
    this.sipAddressSync = new SipAddressSync();
    this.structuredNameSync = new StructuredNameSync();
    this.structuredPostalSync = new StructuredPostalSync();
    this.website = new WebsiteSync();
  }

  public void initStructuredPostalSync() {
    this.structuredPostalSync = new StructuredPostalSync();
  }

  public void initSipAddressSync() {
    this.sipAddressSync = new SipAddressSync();
  }

  public void initStructuredName() {
    this.structuredNameSync = new StructuredNameSync();
  }

  public void initRelation() {
    this.relation = new RelationSync();
  }

  public void initPhone() {
    this.phone = new PhoneSync();
  }

  public void initOrganization() {
    this.organization = new OrganizationSync();
  }

  public void initNote() {
    this.note = new NoteSync();
  }

  public void initNickname() {
    this.nickname = new NicknameSync();
  }

  public void initIm() {
    this.im = new ImSync();
  }

  public void initIdentity() {
    this.identity = new IdentitySync();
  }

  public void initEvent() {
    this.event = new EventSync();
  }

  public void initWebsite() {
    this.website = new WebsiteSync();
  }

  public void initEmail() {
    this.email = new EmailSync();
  }

  public static GoogleContact defaultValue() {
    GoogleContact contact = new GoogleContact();
    contact.init();
    contact.email.defaultValue();
    contact.event.defaultValue();
    contact.identity.defaultValue();
    contact.im.defaultValue();
    contact.nickname.defaultValue();
    contact.note.defaultValue();
    contact.organization.defaultValue();
    contact.phone.defaultValue();
    contact.relation.defaultValue();
    contact.sipAddressSync.defaultValue();
    contact.structuredNameSync.defaultValue();
    contact.structuredPostalSync.defaultValue();
    contact.website.defaultValue();
    return contact;
  }

  public EmailSync getEmail() {
    return email;
  }
  public void setEmail(EmailSync email) {
    this.email = email;
  }
  public EventSync getEvent() {
    return event;
  }
  public void setEvent(EventSync event) {
    this.event = event;
  }
  public IdentitySync getIdentity() {
    return identity;
  }
  public void setIdentity(IdentitySync identity) {
    this.identity = identity;
  }
  public ImSync getImSync() {
    return im;
  }
  public void setImSync(ImSync im) {
    this.im = im;
  }
  public NicknameSync getNickname() {
    return nickname;
  }
  public void setNickname(NicknameSync nickname) {
    this.nickname = nickname;
  }
  public NoteSync getNote() {
    return note;
  }
  public void setNote(NoteSync note) {
    this.note = note;
  }
  public OrganizationSync getOrganization() {
    return organization;
  }
  public void setOrganization(OrganizationSync organization) {
    this.organization = organization;
  }
  public PhoneSync getPhone() {
    return phone;
  }
  public void setPhone(PhoneSync phone) {
    this.phone = phone;
  }
  public RelationSync getRelation() {
    return relation;
  }
  public void setRelation(RelationSync relation) {
    this.relation = relation;
  }
  public SipAddressSync getSipAddress() {
    return sipAddressSync;
  }
  public void setSipAddress(SipAddressSync sipAddressSync) {
    this.sipAddressSync = sipAddressSync;
  }
  public StructuredNameSync getStructuredName() {
    return structuredNameSync;
  }
  public void setStructuredName(StructuredNameSync structuredNameSync) {
    this.structuredNameSync = structuredNameSync;
  }
  public StructuredPostalSync getStructuredPostal() {
    return structuredPostalSync;
  }
  public void setStructuredPostal(StructuredPostalSync structuredPostalSync) {
    this.structuredPostalSync = structuredPostalSync;
  }
  public WebsiteSync getWebsite() {
    return website;
  }
  public void setWebsite(WebsiteSync website) {
    this.website = website;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public static ContentProviderOperation delete(String id) {
    return ContentProviderOperation.newDelete(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{String.valueOf(id)})
        .build();
  }

  public static ContentValues compare(GoogleContact con1, GoogleContact con2) {
    ContentValues values = new ContentValues();

    values.putAll(EmailSync.compare(con1.getEmail(), con2.getEmail()));
    values.putAll(EventSync.compare(con1.getEvent(), con2.getEvent()));
    values.putAll(IdentitySync.compare(con1.getIdentity(), con2.getIdentity()));
    values.putAll(ImSync.compare(con1.getImSync(), con2.getImSync()));
    values.putAll(NicknameSync.compare(con1.getNickname(), con2.getNickname()));
    values.putAll(NoteSync.compare(con1.getNote(), con2.getNote()));
    values.putAll(OrganizationSync.compare(con1.getOrganization(), con2.getOrganization()));
    values.putAll(PhoneSync.compare(con1.getPhone(), con2.getPhone()));
    values.putAll(RelationSync.compare(con1.getRelation(), con2.getRelation()));
    values.putAll(SipAddressSync.compare(con1.getSipAddress(), con2.getSipAddress()));
    values.putAll(StructuredNameSync.compare(con1.getStructuredName(), con2.getStructuredName()));
    values.putAll(StructuredPostalSync.compare(con1.getStructuredPostal(), con2.getStructuredPostal()));
    values.putAll(WebsiteSync.compare(con1.getWebsite(), con2.getWebsite()));

    return values;
  }

  /**
   * Create list of {@link ContentProviderOperation}, which added new contact to contact provider database.
   * @param oldGoogleContact The {@link GoogleContact}
   * @param newGoogleContact The {@link GoogleContact}
   * @return list of ContentProviderOperation for adding new contact on based newGoogleContact.
   */
  public static ArrayList<ContentProviderOperation> createOperationNew(
      final GoogleContact oldGoogleContact, final GoogleContact newGoogleContact, final int size) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    try {
      int rawContactInsertIndex = size;
      ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
               .withValue(RawContacts.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE)
               .withValue(RawContacts.ACCOUNT_NAME, Constants.ACCOUNT_NAME)
               .withValue(RawContacts.SYNC1, "1")
               //.withValue(RawContacts.SYNC2, new Utils().createTimestamp())
               .withValue(RawContacts.SYNC3, AndroidDB.SET_IMPORT)
               .withValue(RawContacts.SYNC4, newGoogleContact.getUuid())
               .build());
      ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
          .withValue(Data.MIMETYPE, AndroidDB.MIMETYPE)
          //.withYieldAllowed(true)
          .build());
      ops.addAll(createOperation(rawContactInsertIndex, oldGoogleContact, newGoogleContact, true));

    } catch (NumberFormatException ex) {
      ex.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    Log.i(TAG, "Add new contact:" + ops.toString());

    return ops;
  }

  public static ArrayList<ContentProviderOperation> createOperationUpdate(
      final GoogleContact oldGoogleContact, final GoogleContact newGoogleContact) {
    ArrayList<ContentProviderOperation> ops = null;
    try {
      ops = createOperation(oldGoogleContact.getId(), oldGoogleContact, newGoogleContact, false);
    } catch (NumberFormatException ex) {
      ex.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    Log.i(TAG, "Update contact:" + ops.toString());
    return ops;
  }

  private static ArrayList<ContentProviderOperation> createOperation(int id, GoogleContact con1, GoogleContact con2, boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

    ArrayList<ContentProviderOperation> emailOps;
    emailOps = EmailSync.operation(id, con1.getEmail(), con2.getEmail(), create);
    if (emailOps != null) {
      ops.addAll(emailOps);
    }

    ArrayList<ContentProviderOperation> eventOps;
    eventOps = EventSync.operation(id, con1.getEvent(), con2.getEvent(), create);
    if (eventOps != null) {
      ops.addAll(eventOps);
    }

    ArrayList<ContentProviderOperation> identityOps;
    identityOps = IdentitySync.operation(id, con1.getIdentity(), con2.getIdentity(), create);
    if (identityOps != null) {
      ops.addAll(identityOps);
    }

    ArrayList<ContentProviderOperation> imOps;
    imOps = ImSync.operation(id, con1.getImSync(), con2.getImSync(), create);
    if (imOps != null) {
      ops.addAll(imOps);
    }

    ArrayList<ContentProviderOperation> nicknameOps;
    nicknameOps = NicknameSync.operation(id, con1.getNickname(), con2.getNickname(), create);
    if (nicknameOps != null) {
      ops.addAll(nicknameOps);
    }

    ArrayList<ContentProviderOperation> noteOps;
    noteOps = NoteSync.operation(id, con1.getNote(), con2.getNote(), create);
    if (noteOps != null) {
      ops.addAll(noteOps);
    }

    ArrayList<ContentProviderOperation> orgOps;
    orgOps = OrganizationSync.operation(id, con1.getOrganization(), con2.getOrganization(), create);
    if (orgOps != null) {
      ops.addAll(orgOps);
    }

    ArrayList<ContentProviderOperation> phoneOps;
    phoneOps = PhoneSync.operation(id, con1.getPhone(), con2.getPhone(), create);
    if (phoneOps != null) {
      ops.addAll(phoneOps);
    }

    ArrayList<ContentProviderOperation> relationOps;
    relationOps = RelationSync.operation(id, con1.getRelation(), con2.getRelation(), create);
    if (relationOps != null) {
      ops.addAll(relationOps);
    }

    ArrayList<ContentProviderOperation> sipOps;
    sipOps = SipAddressSync.operation(id, con1.getSipAddress(), con2.getSipAddress(), create);
    if (sipOps != null) {
      ops.addAll(sipOps);
    }

    ArrayList<ContentProviderOperation> nameOps;
    nameOps = StructuredNameSync.operation(id, con1.getStructuredName(), con2.getStructuredName(), create);
    if (nameOps != null) {
      ops.addAll(nameOps);
    }

    ArrayList<ContentProviderOperation> addressOps;
    addressOps = StructuredPostalSync.operation(id, con1.getStructuredPostal(), con2.getStructuredPostal(), create);
    if (addressOps != null) {
      ops.addAll(addressOps);
    }

    ArrayList<ContentProviderOperation> webOps;
    webOps = WebsiteSync.operation(id, con1.getWebsite(), con2.getWebsite(), create);
    if (webOps != null) {
      ops.addAll(webOps);
    }

    return ops;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return Returns the accountNamePrevious.
   */
  public String getAccountNamePrevious() {
    return accountNamePrevious;
  }

  /**
   * @param accountNamePrevious The accountNamePrevious to set.
   */
  public void setAccountNamePrevious(String accountNamePrevious) {
    this.accountNamePrevious = accountNamePrevious;
  }

  public String getDeleted() {
    return deleted ? "TRUE" : "FALSE";
  }

  public String getSynchronize() {
    return synchronize ? "TRUE" : "FALSE";
  }

  /**
   * @return Returns the synchronize.
   */
  public boolean isSynchronize() {
    return synchronize;
  }

  /**
   * @param synchronize The synchronize to set.
   */
  public void setSynchronize(boolean synchronize) {
    this.synchronize = synchronize;
  }

  /**
   * @return Returns the accountTypePrevious.
   */
  public String getAccountTypePrevious() {
    return accountTypePrevious;
  }

  /**
   * @param accountTypePrevious The accountTypePrevious to set.
   */
  public void setAccountTypePrevious(String accountTypePrevious) {
    this.accountTypePrevious = accountTypePrevious;
  }

  /**
   * @return Returns the deleted.
   */
  public boolean isDeleted() {
    return deleted;
  }

  /**
   * @param deleted The deleted to set.
   */
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "GoogleContact [email=" + email + ",\n event=" + event + ",\n identity=" + identity
        + ",\n im=" + im + ",\n nickname=" + nickname + ",\n note=" + note + ",\n organization="
        + organization + ",\n phone=" + phone + ",\n relation=" + relation + ",\n sipAddressSync="
        + sipAddressSync + ",\n structuredNameSync=" + structuredNameSync + ",\n structuredPostalSync="
        + structuredPostalSync + ",\n website=" + website + ", timestamp=" + timestamp + ", uuid="
        + uuid + ", id=" + id + ", synchronize=" + synchronize + ", accountNamePrevious="
        + accountNamePrevious + ", accountTypePrevious=" + accountTypePrevious + ", deleted="
        + deleted + "]";
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accountNamePrevious == null) ? 0 : accountNamePrevious.hashCode());
    result = prime * result + ((accountTypePrevious == null) ? 0 : accountTypePrevious.hashCode());
    result = prime * result + (deleted ? 1231 : 1237);
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((event == null) ? 0 : event.hashCode());
    result = prime * result + ((identity == null) ? 0 : identity.hashCode());
    result = prime * result + ((im == null) ? 0 : im.hashCode());
    result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
    result = prime * result + ((note == null) ? 0 : note.hashCode());
    result = prime * result + ((organization == null) ? 0 : organization.hashCode());
    result = prime * result + ((phone == null) ? 0 : phone.hashCode());
    result = prime * result + ((relation == null) ? 0 : relation.hashCode());
    result = prime * result + ((sipAddressSync == null) ? 0 : sipAddressSync.hashCode());
    result = prime * result + ((structuredNameSync == null) ? 0 : structuredNameSync.hashCode());
    result = prime * result
        + ((structuredPostalSync == null) ? 0 : structuredPostalSync.hashCode());
    result = prime * result + (synchronize ? 1231 : 1237);
    result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    result = prime * result + ((website == null) ? 0 : website.hashCode());
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
    GoogleContact other = (GoogleContact) obj;
//    if (accountNamePrevious == null) {
//      if (other.accountNamePrevious != null) {
//        return false;
//      }
//    } else if (!accountNamePrevious.equals(other.accountNamePrevious)) {
//      return false;
//    }
//    if (accountTypePrevious == null) {
//      if (other.accountTypePrevious != null) {
//        return false;
//      }
//    } else if (!accountTypePrevious.equals(other.accountTypePrevious)) {
//      return false;
//    }
    if (deleted != other.deleted) {
      return false;
    }
    if (email == null) {
      if (other.email != null) {
        return false;
      }
    } else if (!email.equals(other.email)) {
      return false;
    }
    if (event == null) {
      if (other.event != null) {
        return false;
      }
    } else if (!event.equals(other.event)) {
      return false;
    }
    if (identity == null) {
      if (other.identity != null) {
        return false;
      }
    } else if (!identity.equals(other.identity)) {
      return false;
    }
    if (im == null) {
      if (other.im != null) {
        return false;
      }
    } else if (!im.equals(other.im)) {
      return false;
    }
    if (nickname == null) {
      if (other.nickname != null) {
        return false;
      }
    } else if (!nickname.equals(other.nickname)) {
      return false;
    }
    if (note == null) {
      if (other.note != null) {
        return false;
      }
    } else if (!note.equals(other.note)) {
      return false;
    }
    if (organization == null) {
      if (other.organization != null) {
        return false;
      }
    } else if (!organization.equals(other.organization)) {
      return false;
    }
    if (phone == null) {
      if (other.phone != null) {
        return false;
      }
    } else if (!phone.equals(other.phone)) {
      return false;
    }
    if (relation == null) {
      if (other.relation != null) {
        return false;
      }
    } else if (!relation.equals(other.relation)) {
      return false;
    }
    if (sipAddressSync == null) {
      if (other.sipAddressSync != null) {
        return false;
      }
    } else if (!sipAddressSync.equals(other.sipAddressSync)) {
      return false;
    }
    if (structuredNameSync == null) {
      if (other.structuredNameSync != null) {
        return false;
      }
    } else if (!structuredNameSync.equals(other.structuredNameSync)) {
      return false;
    }
    if (structuredPostalSync == null) {
      if (other.structuredPostalSync != null) {
        return false;
      }
    } else if (!structuredPostalSync.equals(other.structuredPostalSync)) {
      return false;
    }
    if (synchronize != other.synchronize) {
      return false;
    }
//    if (timestamp == null) {
//      if (other.timestamp != null) {
//        return false;
//      }
//    } else if (!timestamp.equals(other.timestamp)) {
//      return false;
//    }
    if (uuid == null) {
      if (other.uuid != null) {
        return false;
      }
    } else if (!uuid.equals(other.uuid)) {
      return false;
    }
    if (website == null) {
      if (other.website != null) {
        return false;
      }
    } else if (!website.equals(other.website)) {
      return false;
    }
    return true;
  }


}

