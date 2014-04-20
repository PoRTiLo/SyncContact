package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.Data;

public class GoogleContact {

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
  private String id;
  
  public GoogleContact() {
    /*this.email = new EmailSync();
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
    this.website = new WebsiteSync();*/
  }
  
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

  @Override
  public String toString() {
    return "GoogleContact [id=" + id + ", email=" + email + ", event=" + event + ", identity="
        + identity + ", im=" + im + ", nickname=" + nickname + ", note=" + note
        + ", organization=" + organization + ", phone=" + phone + ", relation="
        + relation + ", sipAddressSync=" + sipAddressSync + ", structuredNameSync="
        + structuredNameSync + ", structuredPostalSync=" + structuredPostalSync
        + ", website=" + website + "]";
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
  
  public static ArrayList<ContentProviderOperation> createOperationUpdate(GoogleContact con1, GoogleContact con2) {
    
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    
    ArrayList<ContentProviderOperation> emailOps;
    emailOps = EmailSync.operation(con1.getId(), con1.getEmail(), con2.getEmail());
    if (emailOps != null) {
      ops.addAll(emailOps);
    }
    
    ArrayList<ContentProviderOperation> eventOps;
    eventOps = EventSync.operation(con1.getId(), con1.getEvent(), con2.getEvent());
    if (eventOps != null) {
      ops.addAll(eventOps);
    }
    
    ArrayList<ContentProviderOperation> identityOps;
    identityOps = IdentitySync.operation(con1.getId(), con1.getIdentity(), con2.getIdentity());
    if (identityOps != null) {
      ops.addAll(identityOps);
    }
    
    ArrayList<ContentProviderOperation> imOps;
    imOps = ImSync.operation(con1.getId(), con1.getImSync(), con2.getImSync());
    if (imOps != null) {
      ops.addAll(imOps);
    }
    
    ArrayList<ContentProviderOperation> nicknameOps;
    nicknameOps = NicknameSync.operation(con1.getId(), con1.getNickname(), con2.getNickname());
    if (nicknameOps != null) {
      ops.addAll(nicknameOps);
    }
    
    ArrayList<ContentProviderOperation> noteOps;
    noteOps = NoteSync.operation(con1.getId(), con1.getNote(), con2.getNote());
    if (noteOps != null) {
      ops.addAll(noteOps);
    }
    
    ArrayList<ContentProviderOperation> orgOps;
    orgOps = OrganizationSync.operation(con1.getId(), con1.getOrganization(), con2.getOrganization());
    if (orgOps != null) {
      ops.addAll(orgOps);
    }
    
    ArrayList<ContentProviderOperation> phoneOps;
    phoneOps = PhoneSync.operation(con1.getId(), con1.getPhone(), con2.getPhone());
    if (phoneOps != null) {
      ops.addAll(phoneOps);
    }
    
    ArrayList<ContentProviderOperation> relationOps;
    relationOps = RelationSync.operation(con1.getId(), con1.getRelation(), con2.getRelation());
    if (relationOps != null) {
      ops.addAll(relationOps);
    }
    
    return ops;
  }

  
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}

