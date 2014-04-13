package cz.xsendl00.synccontact.contact;

import java.util.UUID;

public class GoogleContact {

  private Email email;
  private Event event;
  private Identity identity;
  private Im im;
  private Nickname nickname;
  private Note note;
  private Organization organization;
  private Phone phone;
  private Relation relation;
  private SipAddress sipAddress;
  private StructuredName structuredName;
  private StructuredPostal structuredPostal;
  private Website website;
  private String timestamp;
  private UUID uuid;
  
  public GoogleContact() {
    this.email = new Email();
    this.event = new Event();
    this.identity = new Identity();
    this.im = new Im();
    this.nickname = new Nickname();
    this.note = new Note();
    this.organization = new Organization();
    this.phone = new Phone();
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
  
  public Email getEmail() {
    return email;
  }
  public void setEmail(Email email) {
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
  public Phone getPhone() {
    return phone;
  }
  public void setPhone(Phone phone) {
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
}
