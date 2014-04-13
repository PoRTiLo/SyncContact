package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

public class Phone implements ContactInterface{

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
}
