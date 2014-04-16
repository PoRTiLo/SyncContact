package cz.xsendl00.synccontact.contact;

import java.util.UUID;

import android.content.ContentProviderOperation;
import android.content.ContentValues;

public class GoogleContact {

  private EmailSync email;
  private Event event;
  private Identity identity;
  private Im im;
  private Nickname nickname;
  private Note note;
  private Organization organization;
  private PhoneSync phone;
  private Relation relation;
  private SipAddress sipAddress;
  private StructuredName structuredName;
  private StructuredPostal structuredPostal;
  private Website website;
  private String timestamp;
  private UUID uuid;
  
  public GoogleContact() {
    this.email = new EmailSync();
    this.event = new Event();
    this.identity = new Identity();
    this.im = new Im();
    this.nickname = new Nickname();
    this.note = new Note();
    this.organization = new Organization();
    this.phone = new PhoneSync();
    this.relation = new Relation();
    this.sipAddress = new SipAddress();
    this.structuredName = new StructuredName();
    this.structuredPostal = new StructuredPostal();
    this.website = new Website();
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
  public Event getEvent() {
    return event;
  }
  public void setEvent(Event event) {
    this.event = event;
  }
  public Identity getIdentity() {
    return identity;
  }
  public void setIdentity(Identity identity) {
    this.identity = identity;
  }
  public Im getIm() {
    return im;
  }
  public void setIm(Im im) {
    this.im = im;
  }
  public Nickname getNickname() {
    return nickname;
  }
  public void setNickname(Nickname nickname) {
    this.nickname = nickname;
  }
  public Note getNote() {
    return note;
  }
  public void setNote(Note note) {
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
  public Website getWebsite() {
    return website;
  }
  public void setWebsite(Website website) {
    this.website = website;
  }

  @Override
  public String toString() {
    return "GoogleContact [email=" + email + ", event=" + event + ", identity="
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
  
  public static ContentValues compare(GoogleContact con1, GoogleContact con2) {
    ContentValues values = new ContentValues();
    
    values.putAll(EmailSync.compare(con1.getEmail(), con2.getEmail()));
    values.putAll(Event.compare(con1.getEvent(), con2.getEvent()));
    values.putAll(Identity.compare(con1.getIdentity(), con2.getIdentity()));
    values.putAll(Im.compare(con1.getIm(), con2.getIm()));
    values.putAll(Nickname.compare(con1.getNickname(), con2.getNickname()));
    values.putAll(Note.compare(con1.getNote(), con2.getNote()));
    values.putAll(Organization.compare(con1.getOrganization(), con2.getOrganization()));
    values.putAll(PhoneSync.compare(con1.getPhone(), con2.getPhone()));
    values.putAll(Relation.compare(con1.getRelation(), con2.getRelation()));
    values.putAll(SipAddress.compare(con1.getSipAddress(), con2.getSipAddress()));
    values.putAll(StructuredName.compare(con1.getStructuredName(), con2.getStructuredName()));
    values.putAll(StructuredPostal.compare(con1.getStructuredPostal(), con2.getStructuredPostal()));
    values.putAll(Website.compare(con1.getWebsite(), con2.getWebsite()));
    
    return values;
  }
  
  public static ContentProviderOperation createUpdate(GoogleContact con1, GoogleContact con2) {
    //ContentProviderOperation
    return null;
  }
}

