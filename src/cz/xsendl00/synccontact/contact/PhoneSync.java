package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import cz.xsendl00.synccontact.utils.Constants;

public class PhoneSync extends AbstractType implements ContactInterface{

  private String phoneAssistant;
  private String phoneCallback;
  private String phoneCar;
  private String phoneCompany;
  private String phoneFaxHome;
  private String phoneFaxWork;
  private String phoneHome;
  private String phoneISDN;
  private String phoneMain;
  private String phoneMMS;
  private String phoneMobile;
  private String phoneOther;
  private String phoneOtherFax;
  private String phonePager;
  private String phoneRadio;
  private String phoneTelex;
  private String phoneTTYTDD;
  private String phoneWork;
  private String phoneWorkMobile;
  private String phoneWorkPager;

  public String getPhoneAssistant() {
    return phoneAssistant;
  }
  public void setPhoneAssistant(String phoneAssistant) {
    this.phoneAssistant = phoneAssistant;
  }
  public String getPhoneCallback() {
    return phoneCallback;
  }
  public void setPhoneCallback(String phoneCallback) {
    this.phoneCallback = phoneCallback;
  }
  public String getPhoneCar() {
    return phoneCar;
  }
  public void setPhoneCar(String phoneCar) {
    this.phoneCar = phoneCar;
  }
  public String getPhoneCompany() {
    return phoneCompany;
  }
  public void setPhoneCompany(String phoneCompany) {
    this.phoneCompany = phoneCompany;
  }
  public String getPhoneFaxHome() {
    return phoneFaxHome;
  }
  public void setPhoneFaxHome(String phoneFaxHome) {
    this.phoneFaxHome = phoneFaxHome;
  }
  public String getPhoneFaxWork() {
    return phoneFaxWork;
  }
  public void setPhoneFaxWork(String phoneFaxWork) {
    this.phoneFaxWork = phoneFaxWork;
  }
  public String getPhoneHome() {
    return phoneHome;
  }
  public void setPhoneHome(String phoneHome) {
    this.phoneHome = phoneHome;
  }
  public String getPhoneISDN() {
    return phoneISDN;
  }
  public void setPhoneISDN(String phoneISDN) {
    this.phoneISDN = phoneISDN;
  }
  public String getPhoneMain() {
    return phoneMain;
  }
  public void setPhoneMain(String phoneMain) {
    this.phoneMain = phoneMain;
  }
  public String getPhoneMMS() {
    return phoneMMS;
  }
  public void setPhoneMMS(String phoneMMS) {
    this.phoneMMS = phoneMMS;
  }
  public String getPhoneMobile() {
    return phoneMobile;
  }
  public void setPhoneMobile(String phoneMobile) {
    this.phoneMobile = phoneMobile;
  }
  public String getPhoneOther() {
    return phoneOther;
  }
  public void setPhoneOther(String phoneOther) {
    this.phoneOther = phoneOther;
  }
  public String getPhoneOtherFax() {
    return phoneOtherFax;
  }
  public void setPhoneOtherFax(String phoneOtherFax) {
    this.phoneOtherFax = phoneOtherFax;
  }
  public String getPhonePager() {
    return phonePager;
  }
  public void setPhonePager(String phonePager) {
    this.phonePager = phonePager;
  }
  public String getPhoneRadio() {
    return phoneRadio;
  }
  public void setPhoneRadio(String phoneRadio) {
    this.phoneRadio = phoneRadio;
  }
  public String getPhoneTelex() {
    return phoneTelex;
  }
  public void setPhoneTelex(String phoneTelex) {
    this.phoneTelex = phoneTelex;
  }
  public String getPhoneTTYTDD() {
    return phoneTTYTDD;
  }
  public void setPhoneTTYTDD(String phoneTTYTDD) {
    this.phoneTTYTDD = phoneTTYTDD;
  }
  public String getPhoneWork() {
    return phoneWork;
  }
  public void setPhoneWork(String phoneWork) {
    this.phoneWork = phoneWork;
  }
  public String getPhoneWorkMobile() {
    return phoneWorkMobile;
  }
  public void setPhoneWorkMobile(String phoneWorkMobile) {
    this.phoneWorkMobile = phoneWorkMobile;
  }
  public String getPhoneWorkPager() {
    return phoneWorkPager;
  }
  public void setPhoneWorkPager(String phoneWorkPager) {
    this.phoneWorkPager = phoneWorkPager;
  }
  @Override
  public String toString() {
    return "Phone [phoneAssistant=" + phoneAssistant + ", phoneCallback="
        + phoneCallback + ", phoneCar=" + phoneCar + ", phoneCompany="
        + phoneCompany + ", phoneFaxHome=" + phoneFaxHome + ", phoneFaxWork="
        + phoneFaxWork + ", phoneHome=" + phoneHome + ", phoneISDN="
        + phoneISDN + ", phoneMain=" + phoneMain + ", phoneMMS=" + phoneMMS
        + ", phoneMobile=" + phoneMobile + ", phoneOther=" + phoneOther
        + ", phoneOtherFax=" + phoneOtherFax + ", phonePager=" + phonePager
        + ", phoneRadio=" + phoneRadio + ", phoneTelex=" + phoneTelex
        + ", phoneTTYTDD=" + phoneTTYTDD + ", phoneWork=" + phoneWork
        + ", phoneWorkMobile=" + phoneWorkMobile + ", phoneWorkPager="
        + phoneWorkPager + "]";
  }
  @Override
  public void defaultValue() {
    phoneAssistant = Constants.PHONE_ASSISTANT;
    phoneCallback = Constants.PHONE_CALLBACK;
    phoneCar = Constants.PHONE_CAR;
    phoneCompany = Constants.PHONE_COMPANY;
    phoneFaxHome = Constants.PHONE_FAX_HOME;
    phoneFaxWork = Constants.PHONE_FAX_WORK;;
    phoneHome = Constants.PHONE_HOME;
    phoneISDN = Constants.PHONE_ISDN;
    phoneMain = Constants.PHONE_MAIN;
    phoneMMS = Constants.PHONE_MMS;
    phoneMobile = Constants.PHONE_MOBILE;
    phoneOther = Constants.PHONE_OTHER ;
    phoneOtherFax = Constants.PHONE_OTHER_FAX;
    phonePager = Constants.PHONE_PAGER;
    phoneRadio = Constants.PHONE_RADIO;
    phoneTelex = Constants.PHONE_TELEX;
    phoneTTYTDD = Constants.PHONE_TTY_TDD;
    phoneWork = Constants.PHONE_WORK;
    phoneWorkMobile = Constants.PHONE_WORK_MOBILE;
    phoneWorkPager = Constants.PHONE_WORK_PAGER;
    
  }
  
  public static ContentValues compare(PhoneSync obj1, PhoneSync obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getPhoneAssistant() != null) {
        values.put(Constants.PHONE_ASSISTANT, obj2.getPhoneAssistant());
      }
      if (obj2.getPhoneCallback() != null) {
        values.put(Constants.PHONE_CALLBACK, obj2.getPhoneCallback());
      }
      if (obj2.getPhoneCar() != null) {
        values.put(Constants.PHONE_CAR, obj2.getPhoneCar());
      }
      if (obj2.getPhoneCompany() != null) {
        values.put(Constants.PHONE_COMPANY, obj2.getPhoneCompany());
      }
      if (obj2.getPhoneFaxHome() != null) {
        values.put(Constants.PHONE_FAX_HOME, obj2.getPhoneFaxHome());
      }
      if (obj2.getPhoneFaxWork() != null) {
        values.put(Constants.PHONE_FAX_WORK, obj2.getPhoneFaxWork());
      }
      if (obj2.getPhoneHome() != null) {
        values.put(Constants.PHONE_HOME, obj2.getPhoneHome());
      }
      if (obj2.getPhoneISDN() != null) {
        values.put(Constants.PHONE_ISDN, obj2.getPhoneISDN());
      }
      if (obj2.getPhoneMain() != null) {
        values.put(Constants.PHONE_MAIN, obj2.getPhoneMain());
      }
      if (obj2.getPhoneMMS() != null) {
        values.put(Constants.PHONE_MMS, obj2.getPhoneMMS());
      }
      if (obj2.getPhoneMobile() != null) {
        values.put(Constants.PHONE_MOBILE, obj2.getPhoneMobile());
      }
      if (obj2.getPhoneOther() != null) {
        values.put(Constants.PHONE_OTHER, obj2.getPhoneOther());
      }
      if (obj2.getPhoneOtherFax() != null) {
        values.put(Constants.PHONE_OTHER_FAX, obj2.getPhoneOtherFax());
      }
      if (obj2.getPhonePager() != null) {
        values.put(Constants.PHONE_PAGER, obj2.getPhonePager());
      }
      if (obj2.getPhoneRadio() != null) {
        values.put(Constants.PHONE_RADIO, obj2.getPhoneRadio());
      }
      if (obj2.getPhoneTelex() != null) {
        values.put(Constants.PHONE_TELEX, obj2.getPhoneTelex());
      }
      if (obj2.getPhoneTTYTDD() != null) {
        values.put(Constants.PHONE_TTY_TDD, obj2.getPhoneTTYTDD());
      }
      if (obj2.getPhoneWork() != null) {
        values.put(Constants.PHONE_WORK, obj2.getPhoneWork());
      }
      if (obj2.getPhoneWorkMobile() != null) {
        values.put(Constants.PHONE_WORK_MOBILE, obj2.getPhoneWorkMobile());
      }
      if (obj2.getPhoneWorkPager() != null) {
        values.put(Constants.PHONE_WORK_PAGER, obj2.getPhonePager());
      }
    } else if (obj1 == null && obj2 == null) { // nothing
      
    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getPhoneAssistant() != null) {
        values.putNull(Constants.PHONE_ASSISTANT);
      }
      if (obj1.getPhoneCallback() != null) {
        values.putNull(Constants.PHONE_CALLBACK);
      }
      if (obj1.getPhoneCar() != null) {
        values.putNull(Constants.PHONE_CAR);
      }
      if (obj1.getPhoneCompany() != null) {
        values.putNull(Constants.PHONE_COMPANY);
      }
      if (obj1.getPhoneFaxHome() != null) {
        values.putNull(Constants.PHONE_FAX_HOME);
      }
      if (obj1.getPhoneFaxWork() != null) {
        values.putNull(Constants.PHONE_FAX_WORK);
      }
      if (obj1.getPhoneHome() != null) {
        values.putNull(Constants.PHONE_HOME);
      }
      if (obj1.getPhoneISDN() != null) {
        values.putNull(Constants.PHONE_ISDN);
      }
      if (obj1.getPhoneMain() != null) {
        values.putNull(Constants.PHONE_MAIN);
      }
      if (obj1.getPhoneMMS() != null) {
        values.putNull(Constants.PHONE_MMS);
      }
      if (obj1.getPhoneMobile() != null) {
        values.putNull(Constants.PHONE_MOBILE);
      }
      if (obj1.getPhoneOther() != null) {
        values.putNull(Constants.PHONE_OTHER);
      }
      if (obj1.getPhoneOtherFax() != null) {
        values.putNull(Constants.PHONE_OTHER_FAX);
      }
      if (obj1.getPhonePager() != null) {
        values.putNull(Constants.PHONE_PAGER);
      }
      if (obj1.getPhoneRadio() != null) {
        values.putNull(Constants.PHONE_RADIO);
      }
      if (obj1.getPhoneTelex() != null) {
        values.putNull(Constants.PHONE_TELEX);
      }
      if (obj1.getPhoneTTYTDD() != null) {
        values.putNull(Constants.PHONE_TTY_TDD);
      }
      if (obj1.getPhoneWork() != null) {
        values.putNull(Constants.PHONE_WORK);
      }
      if (obj1.getPhoneWorkMobile() != null) {
        values.putNull(Constants.PHONE_WORK_MOBILE);
      }
      if (obj1.getPhoneWorkPager() != null) {
        values.putNull(Constants.PHONE_WORK_PAGER);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getPhoneAssistant() != null) {
        values.put(Constants.PHONE_ASSISTANT, obj2.getPhoneAssistant());
      } else {
        values.putNull(Constants.PHONE_ASSISTANT);
      }
      if (obj2.getPhoneCallback() != null) {
        values.put(Constants.PHONE_CALLBACK, obj2.getPhoneCallback());
      } else {
        values.putNull(Constants.PHONE_CALLBACK);
      }
      if (obj2.getPhoneCar() != null) {
        values.put(Constants.PHONE_CAR, obj2.getPhoneCar());
      } else {
        values.putNull(Constants.PHONE_CAR);
      }
      if (obj2.getPhoneCompany() != null) {
        values.put(Constants.PHONE_COMPANY, obj2.getPhoneCompany());
      } else {
        values.putNull(Constants.PHONE_COMPANY);
      }
      if (obj2.getPhoneFaxHome() != null) {
        values.put(Constants.PHONE_FAX_HOME, obj2.getPhoneFaxHome());
      } else {
        values.putNull(Constants.PHONE_FAX_HOME);
      }
      if (obj2.getPhoneFaxWork() != null) {
        values.put(Constants.PHONE_FAX_WORK, obj2.getPhoneFaxWork());
      } else {
        values.putNull(Constants.PHONE_FAX_WORK);
      }
      if (obj2.getPhoneHome() != null) {
        values.put(Constants.PHONE_HOME, obj2.getPhoneHome());
      } else {
        values.putNull(Constants.PHONE_HOME);
      }
      if (obj2.getPhoneISDN() != null) {
        values.put(Constants.PHONE_ISDN, obj2.getPhoneISDN());
      } else {
        values.putNull(Constants.PHONE_ISDN);
      }
      if (obj2.getPhoneMain() != null) {
        values.put(Constants.PHONE_MAIN, obj2.getPhoneMain());
      } else {
        values.putNull(Constants.PHONE_MAIN);
      }
      if (obj2.getPhoneMMS() != null) {
        values.put(Constants.PHONE_MMS, obj2.getPhoneMMS());
      } else {
        values.putNull(Constants.PHONE_MMS);
      }
      if (obj2.getPhoneMobile() != null) {
        values.put(Constants.PHONE_MOBILE, obj2.getPhoneMobile());
      } else {
        values.putNull(Constants.PHONE_MOBILE);
      }
      if (obj2.getPhoneOther() != null) {
        values.put(Constants.PHONE_OTHER, obj2.getPhoneOther());
      } else {
        values.putNull(Constants.PHONE_OTHER);
      }
      if (obj2.getPhoneOtherFax() != null) {
        values.put(Constants.PHONE_OTHER_FAX, obj2.getPhoneOtherFax());
      } else {
        values.putNull(Constants.PHONE_OTHER_FAX);
      }
      if (obj2.getPhonePager() != null) {
        values.put(Constants.PHONE_PAGER, obj2.getPhonePager());
      } else {
        values.putNull(Constants.PHONE_PAGER);
      }
      if (obj2.getPhoneRadio() != null) {
        values.put(Constants.PHONE_RADIO, obj2.getPhoneRadio());
      } else {
        values.putNull(Constants.PHONE_RADIO);
      }
      if (obj2.getPhoneTelex() != null) {
        values.put(Constants.PHONE_TELEX, obj2.getPhoneTelex());
      } else {
        values.putNull(Constants.PHONE_TELEX);
      }
      if (obj2.getPhoneTTYTDD() != null) {
        values.put(Constants.PHONE_TTY_TDD, obj2.getPhoneTTYTDD());
      } else {
        values.putNull(Constants.PHONE_TTY_TDD);
      }
      if (obj2.getPhoneWork() != null) {
        values.put(Constants.PHONE_WORK, obj2.getPhoneWork());
      } else {
        values.putNull(Constants.PHONE_WORK);
      }
      if (obj2.getPhoneWorkMobile() != null) {
        values.put(Constants.PHONE_WORK_MOBILE, obj2.getPhoneWorkMobile());
      } else {
        values.putNull(Constants.PHONE_WORK_MOBILE);
      }
      if (obj2.getPhoneWorkPager() != null) {
        values.put(Constants.PHONE_WORK_PAGER, obj2.getPhonePager());
      } else {
        values.putNull(Constants.PHONE_WORK_PAGER);
      }
    }
    return values;
  }
  
  /**
   * 
   * @param id        raw_contact id
   * @param value     name of protocol
   * @param protocol  like {@link Phone.PROTOCOl_AIM}
   * @param type      like Phone.TYPE_HOME
   * @return
   */
  public static ContentProviderOperation add(String id, String value, int type) {
    return ContentProviderOperation.newInsert(Data.CONTENT_URI)
    .withValue(Data.RAW_CONTACT_ID, id)
    .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
    .withValue(Phone.TYPE, type)
    .withValue(Phone.DATA, value)
    .build();
  }
  
  public static ContentProviderOperation update(String id, String value, int type) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{String.valueOf(id)})
        .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
        .withValue(Phone.DATA, value)
        .build();
  }
  

  public static ArrayList<ContentProviderOperation> operation(String id, PhoneSync em1, PhoneSync em2) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      if (em2.getPhoneAssistant() != null) {
        ops.add(add(id, em2.getPhoneAssistant(), Phone.TYPE_ASSISTANT));
      }
      if (em2.getPhoneCallback() != null) {
        ops.add(add(id, em2.getPhoneCallback(), Phone.TYPE_CALLBACK));
      }
      if (em2.getPhoneCar() != null) {
        ops.add(add(id, em2.getPhoneCar(), Phone.TYPE_CAR));
      }
      if (em2.getPhoneCompany() != null) {
        ops.add(add(id, em2.getPhoneCompany(), Phone.TYPE_COMPANY_MAIN));
      }
      if (em2.getPhoneFaxHome() != null) {
        ops.add(add(id, em2.getPhoneFaxHome(), Phone.TYPE_FAX_HOME));
      }
      if (em2.getPhoneFaxWork() != null) {
        ops.add(add(id, em2.getPhoneFaxWork(), Phone.TYPE_FAX_WORK));
      }
      if (em2.getPhoneHome()  != null) {
        ops.add(add(id, em2.getPhoneHome(), Phone.TYPE_HOME));
      }
      if (em2.getPhoneISDN() != null) {
        ops.add(add(id, em2.getPhoneISDN(), Phone.TYPE_ISDN));
      }
      if (em2.getPhoneMain() != null) {
        ops.add(add(id, em2.getPhoneMain(), Phone.TYPE_MAIN));
      }
      if (em2.getPhoneMMS() != null) {
        ops.add(add(id, em2.getPhoneMMS(), Phone.TYPE_MMS));
      }
      if (em2.getPhoneMobile() != null) {
        ops.add(add(id, em2.getPhoneMobile(), Phone.TYPE_MOBILE));
      }
      if (em2.getPhoneOther() != null) {
        ops.add(add(id, em2.getPhoneOther(), Phone.TYPE_OTHER));
      }
      if (em2.getPhoneOtherFax() != null) {
        ops.add(add(id, em2.getPhoneOtherFax(), Phone.TYPE_OTHER_FAX));
      }
      if (em2.getPhonePager() != null) {
        ops.add(add(id, em2.getPhonePager(), Phone.TYPE_PAGER));
      }
      if (em2.getPhoneRadio() != null) {
        ops.add(add(id, em2.getPhoneRadio(), Phone.TYPE_RADIO));
      }
      if (em2.getPhoneTelex() != null) {
        ops.add(add(id, em2.getPhoneTelex(), Phone.TYPE_TELEX));
      }
      if (em2.getPhoneTTYTDD() != null) {
        ops.add(add(id, em2.getPhoneTTYTDD(), Phone.TYPE_TTY_TDD));
      }
      if (em2.getPhoneWork() != null) {
        ops.add(add(id, em2.getPhoneWork(), Phone.TYPE_WORK));
      }
      if (em2.getPhoneWorkMobile() != null) {
        ops.add(add(id, em2.getPhoneWorkMobile(), Phone.TYPE_WORK_MOBILE));
      }
      if (em2.getPhoneWorkPager() != null) {
        ops.add(add(id, em2.getPhoneWorkPager(), Phone.TYPE_WORK_PAGER));
      }
    } else if (em1 == null && em2 == null) { // nothing
      
    } else if (em1 != null && em2 == null) { // delete
      if (em1.getPhoneAssistant() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_ASSISTANT), null)));
      }
      if (em1.getPhoneCallback() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_CALLBACK), null)));
      }
      if (em1.getPhoneCar() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_CAR), null)));
      }
      if (em1.getPhoneCompany() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_COMPANY_MAIN), null)));
      }
      if (em1.getPhoneFaxHome() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_FAX_HOME), null)));
      }
      if (em1.getPhoneFaxWork() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_FAX_WORK), null)));
      }
      if (em1.getPhoneHome()  != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_HOME), null)));
      }
      if (em1.getPhoneISDN() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_ISDN), null)));
      }
      if (em1.getPhoneMain() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_MAIN), null)));
      }
      if (em1.getPhoneMMS() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_MMS), null)));
      }
      if (em1.getPhoneMobile() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_MOBILE), null)));
      }
      if (em1.getPhoneOther() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_OTHER), null)));
      }
      if (em1.getPhoneOtherFax() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_OTHER_FAX), null)));
      }
      if (em1.getPhonePager() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_PAGER), null)));
      }
      if (em1.getPhoneRadio() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_RADIO), null)));
      }
      if (em1.getPhoneTelex() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_TELEX), null)));
      }
      if (em1.getPhoneTTYTDD() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_TTY_TDD), null)));
      }
      if (em1.getPhoneWork() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_WORK), null)));
      }
      if (em1.getPhoneWorkMobile() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_WORK_MOBILE), null)));
      }
      if (em1.getPhoneWorkPager() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Phone.TYPE_WORK_PAGER), null)));
      }
    } else if (em1 != null && em2 != null) { // clear or update data in db
      ArrayList<ContentProviderOperation> op;
      op = createOps(em1.getPhoneAssistant(), em2.getPhoneAssistant(), em1.getID(), Phone.TYPE_ASSISTANT, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      op = createOps(em1.getPhoneCallback(), em2.getPhoneCallback(), em1.getID(), Phone.TYPE_CALLBACK, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneCar(), em2.getPhoneCar(), em1.getID(), Phone.TYPE_CAR, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneCompany(), em2.getPhoneCompany(), em1.getID(), Phone.TYPE_COMPANY_MAIN, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneFaxHome(), em2.getPhoneFaxHome(), em1.getID(), Phone.TYPE_FAX_HOME, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneFaxWork(), em2.getPhoneFaxWork(), em1.getID(), Phone.TYPE_FAX_WORK, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneHome(), em2.getPhoneHome(), em1.getID(), Phone.TYPE_HOME, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      op = createOps(em1.getPhoneISDN(), em2.getPhoneISDN(), em1.getID(), Phone.TYPE_ISDN, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneMain(), em2.getPhoneMain(), em1.getID(), Phone.TYPE_MAIN, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneMMS(), em2.getPhoneMMS(), em1.getID(), Phone.TYPE_MMS, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneMobile(), em2.getPhoneMobile(), em1.getID(), Phone.TYPE_MOBILE, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneOther(), em2.getPhoneOther(), em1.getID(), Phone.TYPE_OTHER, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneOtherFax(), em2.getPhoneOtherFax(), em1.getID(), Phone.TYPE_OTHER_FAX, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      op = createOps(em1.getPhonePager(), em2.getPhonePager(), em1.getID(), Phone.TYPE_PAGER, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneRadio(), em2.getPhoneRadio(), em1.getID(), Phone.TYPE_RADIO, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneTelex(), em2.getPhoneTelex(), em1.getID(), Phone.TYPE_TELEX, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneTTYTDD(), em2.getPhoneTTYTDD(), em1.getID(), Phone.TYPE_TTY_TDD, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneWork(), em2.getPhoneWork(), em1.getID(), Phone.TYPE_WORK, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      op = createOps(em1.getPhoneWorkMobile(), em2.getPhoneWorkMobile(), em1.getID(), Phone.TYPE_WORK_MOBILE, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getPhoneWorkPager(), em2.getPhoneWorkPager(), em1.getID(), Phone.TYPE_WORK_PAGER, id);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
    }
    return ops.size() > 0 ? ops : null;
  }
  
  /**
   * 
   * @param value1
   * @param value2
   * @param ids
   * @param type
   * @param id
   * @return
   */
  private static ArrayList<ContentProviderOperation> createOps(String value1, String value2, List<ID> ids, int type, String id) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if(value2 != null || value2 != null) {
      String i = ID.getIdByValue(ids, String.valueOf(type), null);
      if (i == null && value2 != null) {
        ops.add(add(id, value2, type));
      } else if ( i != null && value2 == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && value2 != null && !value2.equals(value1)) { 
        ops.add(update(id, value2, type));
      }
    }
    return ops.size() > 0 ? ops : null;
  }
  
}
