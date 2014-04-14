package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
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
  
  public static ContentValues compare(Im obj1, Im obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getImHomeAim() != null) {
        values.put(Constants.IM_HOME_AIM, obj2.getImHomeAim());
      }
      if (obj2.getImHomeGoogleTalk() != null) {
        values.put(Constants.IM_HOME_GOOGLE_TALK, obj2.getImHomeGoogleTalk());
      }
      if (obj2.getImHomeIcq() != null) {
        values.put(Constants.IM_HOME_ICQ, obj2.getImHomeIcq());
      }
      if (obj2.getImHomeJabber() != null) {
        values.put(Constants.IM_HOME_JABBER, obj2.getImHomeJabber());
      }
      if (obj2.getImHomeMsn() != null) {
        values.put(Constants.IM_HOME_MSN, obj2.getImHomeMsn());
      }
      if (obj2.getImHomeNetmeeting() != null) {
        values.put(Constants.IM_HOME_NETMEETING, obj2.getImHomeNetmeeting());
      }
      if (obj2.getImHomeQq() != null) {
        values.put(Constants.IM_HOME_QQ, obj2.getImHomeQq());
      }
      if (obj2.getImHomeSkype() != null) {
        values.put(Constants.IM_HOME_SKYPE, obj2.getImHomeSkype());
      }
      if (obj2.getImHomeYahoo() != null) {
        values.put(Constants.IM_HOME_YAHOO, obj2.getImHomeYahoo());
      }
      if (obj2.getImOtherAim() != null) {
        values.put(Constants.IM_OTHER_AIM, obj2.getImOtherAim());
      }
      if (obj2.getImOtherGoogleTalk() != null) {
        values.put(Constants.IM_OTHER_GOOGLE_TALK, obj2.getImOtherGoogleTalk());
      }
      if (obj2.getImOtherIcq() != null) {
        values.put(Constants.IM_OTHER_ICQ, obj2.getImOtherIcq());
      }
      if (obj2.getImOtherJabber() != null) {
        values.put(Constants.IM_OTHER_JABBER, obj2.getImOtherJabber());
      }
      if (obj2.getImOtherMsn() != null) {
        values.put(Constants.IM_OTHER_MSN, obj2.getImOtherMsn());
      }
      if (obj2.getImOtherNetmeeting() != null) {
        values.put(Constants.IM_OTHER_NETMEETING, obj2.getImOtherNetmeeting());
      }
      if (obj2.getImOtherQq() != null) {
        values.put(Constants.IM_OTHER_QQ, obj2.getImOtherQq());
      }
      if (obj2.getImOtherSkype() != null) {
        values.put(Constants.IM_OTHER_SKYPE, obj2.getImOtherSkype());
      }
      if (obj2.getImOtherYahoo() != null) {
        values.put(Constants.IM_OTHER_YAHOO, obj2.getImOtherYahoo());
      }
      if (obj2.getImWorkAim() != null) {
        values.put(Constants.IM_WORK_AIM, obj2.getImWorkAim());
      }
      if (obj2.getImWorkGoogleTalk() != null) {
        values.put(Constants.IM_WORK_GOOGLE_TALK, obj2.getImWorkGoogleTalk());
      }
      if (obj2.getImWorkIcq() != null) {
        values.put(Constants.IM_WORK_ICQ, obj2.getImWorkIcq());
      }
      if (obj2.getImWorkJabber() != null) {
        values.put(Constants.IM_WORK_JABBER, obj2.getImWorkJabber());
      }
      if (obj2.getImWorkMsn() != null) {
        values.put(Constants.IM_WORK_MSN, obj2.getImWorkMsn());
      }
      if (obj2.getImWorkNetmeeting() != null) {
        values.put(Constants.IM_WORK_NETMEETING, obj2.getImWorkNetmeeting());
      }
      if (obj2.getImWorkQq() != null) {
        values.put(Constants.IM_WORK_QQ, obj2.getImWorkQq());
      }
      if (obj2.getImWorkSkype() != null) {
        values.put(Constants.IM_WORK_SKYPE, obj2.getImWorkSkype());
      }
      if (obj2.getImWorkYahoo() != null) {
        values.put(Constants.IM_WORK_YAHOO, obj2.getImWorkYahoo());
      }
    } else if (obj1 == null && obj2 == null) { // nothing
      
    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getImHomeAim() != null) {
        values.putNull(Constants.IM_WORK_AIM);
      }
      if (obj1.getImHomeGoogleTalk() != null) {
        values.putNull(Constants.IM_HOME_GOOGLE_TALK);
      }
      if (obj1.getImHomeIcq() != null) {
        values.putNull(Constants.IM_HOME_ICQ);
      }
      if (obj1.getImHomeJabber() != null) {
        values.putNull(Constants.IM_HOME_JABBER);
      }
      if (obj1.getImHomeMsn() != null) {
        values.putNull(Constants.IM_HOME_MSN);
      }
      if (obj1.getImHomeNetmeeting() != null) {
        values.putNull(Constants.IM_HOME_NETMEETING);
      }
      if (obj1.getImHomeQq() != null) {
        values.putNull(Constants.IM_HOME_QQ);
      }
      if (obj1.getImHomeSkype() != null) {
        values.putNull(Constants.IM_HOME_SKYPE);
      }
      if (obj1.getImHomeYahoo() != null) {
        values.putNull(Constants.IM_WORK_YAHOO);
      }
      if (obj1.getImWorkAim() != null) {
        values.putNull(Constants.IM_WORK_AIM);
      }
      if (obj1.getImWorkGoogleTalk() != null) {
        values.putNull(Constants.IM_WORK_GOOGLE_TALK);
      }
      if (obj1.getImWorkIcq() != null) {
        values.putNull(Constants.IM_WORK_ICQ);
      }
      if (obj1.getImWorkJabber() != null) {
        values.putNull(Constants.IM_WORK_JABBER);
      }
      if (obj1.getImWorkMsn() != null) {
        values.putNull(Constants.IM_WORK_MSN);
      }
      if (obj1.getImWorkNetmeeting() != null) {
        values.putNull(Constants.IM_WORK_NETMEETING);
      }
      if (obj1.getImWorkQq() != null) {
        values.putNull(Constants.IM_WORK_QQ);
      }
      if (obj1.getImWorkSkype() != null) {
        values.putNull(Constants.IM_WORK_SKYPE);
      }
      if (obj1.getImWorkYahoo() != null) {
        values.putNull(Constants.IM_WORK_YAHOO);
      }
      if (obj1.getImOtherAim() != null) {
        values.putNull(Constants.IM_OTHER_AIM);
      }
      if (obj1.getImOtherGoogleTalk() != null) {
        values.putNull(Constants.IM_OTHER_GOOGLE_TALK);
      }
      if (obj1.getImOtherIcq() != null) {
        values.putNull(Constants.IM_OTHER_ICQ);
      }
      if (obj1.getImOtherJabber() != null) {
        values.putNull(Constants.IM_OTHER_JABBER);
      }
      if (obj1.getImOtherMsn() != null) {
        values.putNull(Constants.IM_OTHER_MSN);
      }
      if (obj1.getImOtherNetmeeting() != null) {
        values.putNull(Constants.IM_OTHER_NETMEETING);
      }
      if (obj1.getImOtherQq() != null) {
        values.putNull(Constants.IM_OTHER_QQ);
      }
      if (obj1.getImOtherSkype() != null) {
        values.putNull(Constants.IM_OTHER_SKYPE);
      }
      if (obj1.getImOtherYahoo() != null) {
        values.putNull(Constants.IM_OTHER_YAHOO);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getImHomeAim() != null) {
        values.put(Constants.IM_HOME_AIM, obj2.getImHomeAim());
      } else {
        values.putNull(Constants.IM_HOME_AIM);
      }
      if (obj2.getImHomeGoogleTalk() != null) {
        values.put(Constants.IM_HOME_GOOGLE_TALK, obj2.getImHomeGoogleTalk());
      } else {
        values.putNull(Constants.IM_HOME_GOOGLE_TALK);
      }
      if (obj2.getImHomeIcq() != null) {
        values.put(Constants.IM_HOME_ICQ, obj2.getImHomeIcq());
      } else {
        values.putNull(Constants.IM_HOME_ICQ);
      }
      if (obj2.getImHomeJabber() != null) {
        values.put(Constants.IM_HOME_JABBER, obj2.getImHomeJabber());
      } else {
        values.putNull(Constants.IM_HOME_JABBER);
      }
      if (obj2.getImHomeMsn() != null) {
        values.put(Constants.IM_HOME_MSN, obj2.getImHomeMsn());
      } else {
        values.putNull(Constants.IM_HOME_MSN);
      }
      if (obj2.getImHomeNetmeeting() != null) {
        values.put(Constants.IM_HOME_NETMEETING, obj2.getImHomeNetmeeting());
      } else {
        values.putNull(Constants.IM_HOME_NETMEETING);
      }
      if (obj2.getImHomeQq() != null) {
        values.put(Constants.IM_HOME_QQ, obj2.getImHomeQq());
      } else {
        values.putNull(Constants.IM_HOME_QQ);
      }
      if (obj2.getImHomeSkype() != null) {
        values.put(Constants.IM_HOME_SKYPE, obj2.getImHomeSkype());
      } else {
        values.putNull(Constants.IM_HOME_SKYPE);
      }
      if (obj2.getImHomeYahoo() != null) {
        values.put(Constants.IM_HOME_YAHOO, obj2.getImHomeYahoo());
      } else {
        values.putNull(Constants.IM_HOME_YAHOO);
      }
      if (obj2.getImOtherAim() != null) {
        values.put(Constants.IM_OTHER_AIM, obj2.getImOtherAim());
      } else {
        values.putNull(Constants.IM_OTHER_AIM);
      }
      if (obj2.getImOtherGoogleTalk() != null) {
        values.put(Constants.IM_OTHER_GOOGLE_TALK, obj2.getImOtherGoogleTalk());
      } else {
        values.putNull(Constants.IM_OTHER_GOOGLE_TALK);
      }
      if (obj2.getImOtherIcq() != null) {
        values.put(Constants.IM_OTHER_ICQ, obj2.getImOtherIcq());
      } else {
        values.putNull(Constants.IM_OTHER_ICQ);
      }
      if (obj2.getImOtherJabber() != null) {
        values.put(Constants.IM_OTHER_JABBER, obj2.getImOtherJabber());
      } else {
        values.putNull(Constants.IM_OTHER_JABBER);
      }
      if (obj2.getImOtherMsn() != null) {
        values.put(Constants.IM_OTHER_MSN, obj2.getImOtherMsn());
      } else {
        values.putNull(Constants.IM_OTHER_MSN);
      }
      if (obj2.getImOtherNetmeeting() != null) {
        values.put(Constants.IM_OTHER_NETMEETING, obj2.getImOtherNetmeeting());
      } else {
        values.putNull(Constants.IM_OTHER_NETMEETING);
      }
      if (obj2.getImOtherQq() != null) {
        values.put(Constants.IM_OTHER_QQ, obj2.getImOtherQq());
      } else {
        values.putNull(Constants.IM_OTHER_QQ);
      }
      if (obj2.getImOtherSkype() != null) {
        values.put(Constants.IM_OTHER_SKYPE, obj2.getImOtherSkype());
      } else {
        values.putNull(Constants.IM_OTHER_SKYPE);
      }
      if (obj2.getImOtherYahoo() != null) {
        values.put(Constants.IM_OTHER_YAHOO, obj2.getImOtherYahoo());
      } else {
        values.putNull(Constants.IM_OTHER_YAHOO);
      }
      if (obj2.getImWorkAim() != null) {
        values.put(Constants.IM_WORK_AIM, obj2.getImWorkAim());
      } else {
        values.putNull(Constants.IM_WORK_AIM);
      }
      if (obj2.getImWorkGoogleTalk() != null) {
        values.put(Constants.IM_WORK_GOOGLE_TALK, obj2.getImWorkGoogleTalk());
      } else {
        values.putNull(Constants.IM_WORK_GOOGLE_TALK);
      }
      if (obj2.getImWorkIcq() != null) {
        values.put(Constants.IM_WORK_ICQ, obj2.getImWorkIcq());
      } else {
        values.putNull(Constants.IM_WORK_ICQ);
      }
      if (obj2.getImWorkJabber() != null) {
        values.put(Constants.IM_WORK_JABBER, obj2.getImWorkJabber());
      } else {
        values.putNull(Constants.IM_WORK_JABBER);
      }
      if (obj2.getImWorkMsn() != null) {
        values.put(Constants.IM_WORK_MSN, obj2.getImWorkMsn());
      } else {
        values.putNull(Constants.IM_WORK_MSN);
      }
      if (obj2.getImWorkNetmeeting() != null) {
        values.put(Constants.IM_WORK_NETMEETING, obj2.getImWorkNetmeeting());
      } else {
        values.putNull(Constants.IM_WORK_NETMEETING);
      }
      if (obj2.getImWorkQq() != null) {
        values.put(Constants.IM_WORK_QQ, obj2.getImWorkQq());
      } else {
        values.putNull(Constants.IM_WORK_QQ);
      }
      if (obj2.getImWorkSkype() != null) {
        values.put(Constants.IM_WORK_SKYPE, obj2.getImWorkSkype());
      } else {
        values.putNull(Constants.IM_WORK_SKYPE);
      }
      if (obj2.getImWorkYahoo() != null) {
        values.put(Constants.IM_WORK_YAHOO, obj2.getImWorkYahoo());
      } else {
        values.putNull(Constants.IM_WORK_YAHOO);
      }
    }
    return values;
  }
}
