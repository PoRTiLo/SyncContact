package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

public class Im  implements ContactInterface {

 
  private String imHomeAim;
  private String imHomeGoogleTalk;
  private String imHomeIcq;
  private String imHomeJabber;
  private String imHomeMsn;
  private String imHomeNetmeeting;
  private String imHomeQq;
  private String imHomeSkype;
  private String imHomeYahoo;
  private String imWorkAim;
  private String imWorkGoogleTalk;
  private String imWorkIcq;
  private String imWorkJabber;
  private String imWorkMsn;
  private String imWorkNetmeeting;
  private String imWorkQq;
  private String imWorkSkype;
  private String imWorkYahoo;
  private String imOtherAim;
  private String imOtherGoogleTalk;
  private String imOtherIcq;
  private String imOtherJabber;
  private String imOtherMsn;
  private String imOtherNetmeeting;
  private String imOtherQq;
  private String imOtherSkype;
  private String imOtherYahoo;
 /* private String imNullAim;
  private String imNullGoogleTalk;
  private String imNullIcq;
  private String imNullJabber;
  private String imNullMsn;
  private String imNullNetmeeting;
  private String imNullQq;
  private String imNullSkype;
  private String imNullYahoo;
 */ 
  public String getImHomeAim() {
    return imHomeAim;
  }
  public void setImHomeAim(String imHomeAim) {
    this.imHomeAim = imHomeAim;
  }
  public String getImHomeGoogleTalk() {
    return imHomeGoogleTalk;
  }
  public void setImHomeGoogleTalk(String imHomeGoogleTalk) {
    this.imHomeGoogleTalk = imHomeGoogleTalk;
  }
  public String getImHomeIcq() {
    return imHomeIcq;
  }
  public void setImHomeIcq(String imHomeIcq) {
    this.imHomeIcq = imHomeIcq;
  }
  public String getImHomeJabber() {
    return imHomeJabber;
  }
  public void setImHomeJabber(String imHomeJabber) {
    this.imHomeJabber = imHomeJabber;
  }
  public String getImHomeMsn() {
    return imHomeMsn;
  }
  public void setImHomeMsn(String imHomeMsn) {
    this.imHomeMsn = imHomeMsn;
  }
  public String getImHomeNetmeeting() {
    return imHomeNetmeeting;
  }
  public void setImHomeNetmeeting(String imHomeNetmeeting) {
    this.imHomeNetmeeting = imHomeNetmeeting;
  }
  public String getImHomeQq() {
    return imHomeQq;
  }
  public void setImHomeQq(String imHomeQq) {
    this.imHomeQq = imHomeQq;
  }
  public String getImHomeSkype() {
    return imHomeSkype;
  }
  public void setImHomeSkype(String imHomeSkype) {
    this.imHomeSkype = imHomeSkype;
  }
  public String getImHomeYahoo() {
    return imHomeYahoo;
  }
  public void setImHomeYahoo(String imHomeYahoo) {
    this.imHomeYahoo = imHomeYahoo;
  }
  public String getImWorkAim() {
    return imWorkAim;
  }
  public void setImWorkAim(String imWorkAim) {
    this.imWorkAim = imWorkAim;
  }
  public String getImWorkGoogleTalk() {
    return imWorkGoogleTalk;
  }
  public void setImWorkGoogleTalk(String imWorkGoogleTalk) {
    this.imWorkGoogleTalk = imWorkGoogleTalk;
  }
  public String getImWorkIcq() {
    return imWorkIcq;
  }
  public void setImWorkIcq(String imWorkIcq) {
    this.imWorkIcq = imWorkIcq;
  }
  public String getImWorkJabber() {
    return imWorkJabber;
  }
  public void setImWorkJabber(String imWorkJabber) {
    this.imWorkJabber = imWorkJabber;
  }
  public String getImWorkMsn() {
    return imWorkMsn;
  }
  public void setImWorkMsn(String imWorkMsn) {
    this.imWorkMsn = imWorkMsn;
  }
  public String getImWorkNetmeeting() {
    return imWorkNetmeeting;
  }
  public void setImWorkNetmeeting(String imWorkNetmeeting) {
    this.imWorkNetmeeting = imWorkNetmeeting;
  }
  public String getImWorkQq() {
    return imWorkQq;
  }
  public void setImWorkQq(String imWorkQq) {
    this.imWorkQq = imWorkQq;
  }
  public String getImWorkSkype() {
    return imWorkSkype;
  }
  public void setImWorkSkype(String imWorkSkype) {
    this.imWorkSkype = imWorkSkype;
  }
  public String getImWorkYahoo() {
    return imWorkYahoo;
  }
  public void setImWorkYahoo(String imWorkYahoo) {
    this.imWorkYahoo = imWorkYahoo;
  }
  public String getImOtherAim() {
    return imOtherAim;
  }
  public void setImOtherAim(String imOtherAim) {
    this.imOtherAim = imOtherAim;
  }
  public String getImOtherGoogleTalk() {
    return imOtherGoogleTalk;
  }
  public void setImOtherGoogleTalk(String imOtherGoogleTalk) {
    this.imOtherGoogleTalk = imOtherGoogleTalk;
  }
  public String getImOtherIcq() {
    return imOtherIcq;
  }
  public void setImOtherIcq(String imOtherIcq) {
    this.imOtherIcq = imOtherIcq;
  }
  public String getImOtherJabber() {
    return imOtherJabber;
  }
  public void setImOtherJabber(String imOtherJabber) {
    this.imOtherJabber = imOtherJabber;
  }
  public String getImOtherMsn() {
    return imOtherMsn;
  }
  public void setImOtherMsn(String imOtherMsn) {
    this.imOtherMsn = imOtherMsn;
  }
  public String getImOtherNetmeeting() {
    return imOtherNetmeeting;
  }
  public void setImOtherNetmeeting(String imOtherNetmeeting) {
    this.imOtherNetmeeting = imOtherNetmeeting;
  }
  public String getImOtherQq() {
    return imOtherQq;
  }
  public void setImOtherQq(String imOtherQq) {
    this.imOtherQq = imOtherQq;
  }
  public String getImOtherSkype() {
    return imOtherSkype;
  }
  public void setImOtherSkype(String imOtherSkype) {
    this.imOtherSkype = imOtherSkype;
  }
  public String getImOtherYahoo() {
    return imOtherYahoo;
  }
  public void setImOtherYahoo(String imOtherYahoo) {
    this.imOtherYahoo = imOtherYahoo;
  }

  @Override
  public String toString() {
    return "Im [imHomeAim=" + imHomeAim + ", imHomeGoogleTalk="
        + imHomeGoogleTalk + ", imHomeIcq=" + imHomeIcq + ", imHomeJabber="
        + imHomeJabber + ", imHomeMsn=" + imHomeMsn + ", imHomeNetmeeting="
        + imHomeNetmeeting + ", imHomeQq=" + imHomeQq + ", imHomeSkype="
        + imHomeSkype + ", imHomeYahoo=" + imHomeYahoo + ", imWorkAim="
        + imWorkAim + ", imWorkGoogleTalk=" + imWorkGoogleTalk + ", imWorkIcq="
        + imWorkIcq + ", imWorkJabber=" + imWorkJabber + ", imWorkMsn="
        + imWorkMsn + ", imWorkNetmeeting=" + imWorkNetmeeting + ", imWorkQq="
        + imWorkQq + ", imWorkSkype=" + imWorkSkype + ", imWorkYahoo="
        + imWorkYahoo + ", imOtherAim=" + imOtherAim + ", imOtherGoogleTalk="
        + imOtherGoogleTalk + ", imOtherIcq=" + imOtherIcq + ", imOtherJabber="
        + imOtherJabber + ", imOtherMsn=" + imOtherMsn + ", imOtherNetmeeting="
        + imOtherNetmeeting + ", imOtherQq=" + imOtherQq + ", imOtherSkype="
        + imOtherSkype + ", imOtherYahoo=" + imOtherYahoo + "]";
  }

  public void defaultValue() {
    imHomeAim = Constants.IM_HOME_AIM;
    imHomeGoogleTalk = Constants.IM_HOME_GOOGLE_TALK;
    imHomeIcq = Constants.IM_HOME_ICQ;
    imHomeJabber = Constants.IM_HOME_JABBER;
    imHomeMsn = Constants.IM_HOME_MSN;
    imHomeNetmeeting = Constants.IM_HOME_NETMEETING;
    imHomeQq = Constants.IM_HOME_QQ;
    imHomeSkype = Constants.IM_HOME_SKYPE;
    imHomeYahoo = Constants.IM_HOME_YAHOO;
    imWorkAim = Constants.IM_WORK_AIM;
    imWorkGoogleTalk = Constants.IM_WORK_GOOGLE_TALK;
    imWorkIcq = Constants.IM_WORK_ICQ;
    imWorkJabber = Constants.IM_WORK_JABBER;
    imWorkMsn = Constants.IM_WORK_MSN;
    imWorkNetmeeting = Constants.IM_WORK_NETMEETING;
    imWorkQq = Constants.IM_WORK_QQ;
    imWorkSkype = Constants.IM_WORK_SKYPE;
    imWorkYahoo = Constants.IM_WORK_YAHOO;
    imOtherAim = Constants.IM_OTHER_AIM;
    imOtherGoogleTalk = Constants.IM_OTHER_GOOGLE_TALK;
    imOtherIcq = Constants.IM_OTHER_ICQ;
    imOtherJabber = Constants.IM_OTHER_JABBER;
    imOtherMsn = Constants.IM_OTHER_MSN;
    imOtherNetmeeting = Constants.IM_OTHER_NETMEETING;
    imOtherQq = Constants.IM_OTHER_QQ;
    imOtherSkype = Constants.IM_OTHER_SKYPE;
    imOtherYahoo = Constants.IM_OTHER_YAHOO;
  }
}
