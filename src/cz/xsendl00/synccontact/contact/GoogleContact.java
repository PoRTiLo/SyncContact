package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import cz.xsendl00.synccontact.utils.Mapping;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;

public class GoogleContact {

  private EmailSync email;
  private EventSync event;
  private IdentitySync identity;
  private ImSync im;
  private NicknameSync nickname;
  private NoteSync note;
  private Organization organization;
  private PhoneSync phone;
  private Relation relation;
  private SipAddress sipAddress;
  private StructuredName structuredName;
  private StructuredPostal structuredPostal;
  private WebsiteSync website;
  private String timestamp;
  private UUID uuid;
  private String id;
  
  public GoogleContact() {
    this.email = new EmailSync();
    this.event = new EventSync();
    this.identity = new IdentitySync();
    this.im = new ImSync();
    this.nickname = new NicknameSync();
    this.note = new NoteSync();
    this.organization = new Organization();
    this.phone = new PhoneSync();
    this.relation = new Relation();
    this.sipAddress = new SipAddress();
    this.structuredName = new StructuredName();
    this.structuredPostal = new StructuredPostal();
    this.website = new WebsiteSync();
  }
  
  public static GoogleContact defaultValue() {
    GoogleContact contact = new GoogleContact();
    contact.email.defaultValue();
    contact.event.defaultValue();
    contact.identity.defaultValue();
    contact.im.defaultValue();
    contact.nickname.defaultValue();
    contact.note.defaultValue();
    contact.organization.defaultValue();
    contact.phone.defaultValue();
    contact.relation.defaultValue();
    contact.sipAddress.defaultValue();
    contact.structuredName.defaultValue();
    contact.structuredPostal.defaultValue();
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
  public ImSync getIm() {
    return im;
  }
  public void setIm(ImSync im) {
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
  public Organization getOrganization() {
    return organization;
  }
  public void setOrganization(Organization organization) {
    this.organization = organization;
  }
  public PhoneSync getPhone() {
    return phone;
  }
  public void setPhone(PhoneSync phone) {
    this.phone = phone;
  }
  public Relation getRelation() {
    return relation;
  }
  public void setRelation(Relation relation) {
    this.relation = relation;
  }
  public SipAddress getSipAddress() {
    return sipAddress;
  }
  public void setSipAddress(SipAddress sipAddress) {
    this.sipAddress = sipAddress;
  }
  public StructuredName getStructuredName() {
    return structuredName;
  }
  public void setStructuredName(StructuredName structuredName) {
    this.structuredName = structuredName;
  }
  public StructuredPostal getStructuredPostal() {
    return structuredPostal;
  }
  public void setStructuredPostal(StructuredPostal structuredPostal) {
    this.structuredPostal = structuredPostal;
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
        + relation + ", sipAddress=" + sipAddress + ", structuredName="
        + structuredName + ", structuredPostal=" + structuredPostal
        + ", website=" + website + "]";
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
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
    values.putAll(ImSync.compare(con1.getIm(), con2.getIm()));
    values.putAll(NicknameSync.compare(con1.getNickname(), con2.getNickname()));
    values.putAll(NoteSync.compare(con1.getNote(), con2.getNote()));
    values.putAll(Organization.compare(con1.getOrganization(), con2.getOrganization()));
    values.putAll(PhoneSync.compare(con1.getPhone(), con2.getPhone()));
    values.putAll(Relation.compare(con1.getRelation(), con2.getRelation()));
    values.putAll(SipAddress.compare(con1.getSipAddress(), con2.getSipAddress()));
    values.putAll(StructuredName.compare(con1.getStructuredName(), con2.getStructuredName()));
    values.putAll(StructuredPostal.compare(con1.getStructuredPostal(), con2.getStructuredPostal()));
    values.putAll(WebsiteSync.compare(con1.getWebsite(), con2.getWebsite()));
    
    return values;
  }
  
  public static ArrayList<ContentProviderOperation> createOperationUpdate(GoogleContact con1, GoogleContact con2) {
    
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    
    ops.addAll(EmailSync.operation(con1.getId(), con1.getEmail(), con2.getEmail()));
    ops.addAll(EventSync.operation(con1.getId(), con1.getEvent(), con2.getEvent()));
    ops.addAll(IdentitySync.operation(con1.getId(), con1.getIdentity(), con2.getIdentity()));
    ops.addAll(ImSync.operation(con1.getId(), con1.getIm(), con2.getIm()));
    ops.addAll(NicknameSync.operation(con1.getId(), con1.getNickname(), con2.getNickname()));
    ops.addAll(NoteSync.operation(con1.getId(), con1.getNote(), con2.getNote()));
    
    return ops;
  }

  
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}

