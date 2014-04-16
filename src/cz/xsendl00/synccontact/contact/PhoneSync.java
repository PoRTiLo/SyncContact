package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
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
}
