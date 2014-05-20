package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.Data;
import cz.xsendl00.synccontact.utils.Constants;

public class ImSync extends AbstractType implements ContactInterface {


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
    return "Im [imHomeAim=" + imHomeAim + ", imHomeGoogleTalk=" + imHomeGoogleTalk + ", imHomeIcq="
        + imHomeIcq + ", imHomeJabber=" + imHomeJabber + ", imHomeMsn=" + imHomeMsn
        + ", imHomeNetmeeting=" + imHomeNetmeeting + ", imHomeQq=" + imHomeQq + ", imHomeSkype="
        + imHomeSkype + ", imHomeYahoo=" + imHomeYahoo + ", imWorkAim=" + imWorkAim
        + ", imWorkGoogleTalk=" + imWorkGoogleTalk + ", imWorkIcq=" + imWorkIcq + ", imWorkJabber="
        + imWorkJabber + ", imWorkMsn=" + imWorkMsn + ", imWorkNetmeeting=" + imWorkNetmeeting
        + ", imWorkQq=" + imWorkQq + ", imWorkSkype=" + imWorkSkype + ", imWorkYahoo="
        + imWorkYahoo + ", imOtherAim=" + imOtherAim + ", imOtherGoogleTalk=" + imOtherGoogleTalk
        + ", imOtherIcq=" + imOtherIcq + ", imOtherJabber=" + imOtherJabber + ", imOtherMsn="
        + imOtherMsn + ", imOtherNetmeeting=" + imOtherNetmeeting + ", imOtherQq=" + imOtherQq
        + ", imOtherSkype=" + imOtherSkype + ", imOtherYahoo=" + imOtherYahoo + "]";
  }

  @Override
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

  public static ContentValues compare(ImSync obj1, ImSync obj2) {
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

  /**
   * @param id raw_contact id
   * @param value name of protocol
   * @param protocol like {@link Im.PROTOCOl_AIM}
   * @param type like Im.TYPE_HOME
   * @return
   */
  public static ContentProviderOperation add(int id,
      String value,
      int protocol,
      int type,
      boolean create) {
    if (create) {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValueBackReference(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE)
          .withValue(Im.PROTOCOL, protocol)
          .withValue(Im.TYPE, type)
          .withValue(Im.DATA, value)
          .build();
    } else {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValue(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE)
          .withValue(Im.PROTOCOL, protocol)
          .withValue(Im.TYPE, type)
          .withValue(Im.DATA, value)
          .build();
    }
  }

  /**
   * @param id
   * @param value
   * @param protocol
   * @param type
   * @return
   */
  public static ContentProviderOperation update(String id, String value, int protocol, int type) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{id})
        .withValue(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE)
        .withValue(Im.PROTOCOL, protocol)
        .withValue(Im.TYPE, type)
        .withValue(Im.DATA, value)
        .build();
  }


  public static ArrayList<ContentProviderOperation> operation(int id,
      ImSync em1,
      ImSync em2,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      if (em2.getImHomeAim() != null) {
        ops.add(add(id, em2.getImHomeAim(), Im.PROTOCOL_AIM, Im.TYPE_HOME, create));
      }
      if (em2.getImHomeGoogleTalk() != null) {
        ops.add(add(id, em2.getImHomeGoogleTalk(), Im.PROTOCOL_GOOGLE_TALK, Im.TYPE_HOME, create));
      }
      if (em2.getImHomeIcq() != null) {
        ops.add(add(id, em2.getImHomeIcq(), Im.PROTOCOL_ICQ, Im.TYPE_HOME, create));
      }
      if (em2.getImHomeJabber() != null) {
        ops.add(add(id, em2.getImHomeJabber(), Im.PROTOCOL_JABBER, Im.TYPE_HOME, create));
      }
      if (em2.getImHomeMsn() != null) {
        ops.add(add(id, em2.getImHomeMsn(), Im.PROTOCOL_MSN, Im.TYPE_HOME, create));
      }
      if (em2.getImHomeNetmeeting() != null) {
        ops.add(add(id, em2.getImHomeNetmeeting(), Im.PROTOCOL_NETMEETING, Im.TYPE_HOME, create));
      }
      if (em2.getImHomeQq() != null) {
        ops.add(add(id, em2.getImHomeQq(), Im.PROTOCOL_QQ, Im.TYPE_HOME, create));
      }
      if (em2.getImHomeSkype() != null) {
        ops.add(add(id, em2.getImHomeSkype(), Im.PROTOCOL_SKYPE, Im.TYPE_HOME, create));
      }
      if (em2.getImHomeYahoo() != null) {
        ops.add(add(id, em2.getImHomeYahoo(), Im.PROTOCOL_YAHOO, Im.TYPE_HOME, create));
      }

      if (em2.getImWorkAim() != null) {
        ops.add(add(id, em2.getImWorkAim(), Im.PROTOCOL_AIM, Im.TYPE_WORK, create));
      }
      if (em2.getImWorkGoogleTalk() != null) {
        ops.add(add(id, em2.getImWorkGoogleTalk(), Im.PROTOCOL_GOOGLE_TALK, Im.TYPE_WORK, create));
      }
      if (em2.getImWorkIcq() != null) {
        ops.add(add(id, em2.getImWorkIcq(), Im.PROTOCOL_ICQ, Im.TYPE_WORK, create));
      }
      if (em2.getImWorkJabber() != null) {
        ops.add(add(id, em2.getImWorkJabber(), Im.PROTOCOL_JABBER, Im.TYPE_WORK, create));
      }
      if (em2.getImWorkMsn() != null) {
        ops.add(add(id, em2.getImWorkMsn(), Im.PROTOCOL_MSN, Im.TYPE_WORK, create));
      }
      if (em2.getImWorkNetmeeting() != null) {
        ops.add(add(id, em2.getImWorkNetmeeting(), Im.PROTOCOL_NETMEETING, Im.TYPE_WORK, create));
      }
      if (em2.getImWorkQq() != null) {
        ops.add(add(id, em2.getImWorkQq(), Im.PROTOCOL_QQ, Im.TYPE_WORK, create));
      }
      if (em2.getImWorkSkype() != null) {
        ops.add(add(id, em2.getImWorkSkype(), Im.PROTOCOL_SKYPE, Im.TYPE_WORK, create));
      }
      if (em2.getImWorkYahoo() != null) {
        ops.add(add(id, em2.getImHomeYahoo(), Im.PROTOCOL_YAHOO, Im.TYPE_WORK, create));
      }

      if (em2.getImOtherAim() != null) {
        ops.add(add(id, em2.getImOtherAim(), Im.PROTOCOL_AIM, Im.TYPE_OTHER, create));
      }
      if (em2.getImOtherGoogleTalk() != null) {
        ops.add(add(id, em2.getImOtherGoogleTalk(), Im.PROTOCOL_GOOGLE_TALK, Im.TYPE_OTHER, create));
      }
      if (em2.getImOtherIcq() != null) {
        ops.add(add(id, em2.getImOtherIcq(), Im.PROTOCOL_ICQ, Im.TYPE_OTHER, create));
      }
      if (em2.getImOtherJabber() != null) {
        ops.add(add(id, em2.getImOtherJabber(), Im.PROTOCOL_JABBER, Im.TYPE_OTHER, create));
      }
      if (em2.getImOtherMsn() != null) {
        ops.add(add(id, em2.getImOtherMsn(), Im.PROTOCOL_MSN, Im.TYPE_OTHER, create));
      }
      if (em2.getImOtherNetmeeting() != null) {
        ops.add(add(id, em2.getImOtherNetmeeting(), Im.PROTOCOL_NETMEETING, Im.TYPE_OTHER, create));
      }
      if (em2.getImOtherQq() != null) {
        ops.add(add(id, em2.getImOtherQq(), Im.PROTOCOL_QQ, Im.TYPE_OTHER, create));
      }
      if (em2.getImOtherSkype() != null) {
        ops.add(add(id, em2.getImOtherSkype(), Im.PROTOCOL_SKYPE, Im.TYPE_OTHER, create));
      }
      if (em2.getImOtherYahoo() != null) {
        ops.add(add(id, em2.getImOtherYahoo(), Im.PROTOCOL_YAHOO, Im.TYPE_OTHER, create));
      }
    } else if (em1 == null && em2 == null) { // nothing

    } else if (em1 != null && em2 == null) { // delete
      if (em1.getImHomeAim() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
            String.valueOf(Im.PROTOCOL_AIM))));
      }
      if (em1.getImHomeGoogleTalk() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
            String.valueOf(Im.PROTOCOL_GOOGLE_TALK))));
      }
      if (em1.getImHomeIcq() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
            String.valueOf(Im.PROTOCOL_ICQ))));
      }
      if (em1.getImHomeJabber() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
            String.valueOf(Im.PROTOCOL_JABBER))));
      }
      if (em1.getImHomeMsn() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
            String.valueOf(Im.PROTOCOL_MSN))));
      }
      if (em1.getImHomeNetmeeting() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
            String.valueOf(Im.PROTOCOL_NETMEETING))));
      }
      if (em1.getImHomeQq() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
            String.valueOf(Im.PROTOCOL_QQ))));
      }
      if (em1.getImHomeSkype() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
            String.valueOf(Im.PROTOCOL_SKYPE))));
      }
      if (em1.getImHomeYahoo() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
            String.valueOf(Im.PROTOCOL_YAHOO))));
      }

      if (em1.getImWorkAim() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
            String.valueOf(Im.PROTOCOL_AIM))));
      }
      if (em1.getImWorkGoogleTalk() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
            String.valueOf(Im.PROTOCOL_GOOGLE_TALK))));
      }
      if (em1.getImWorkIcq() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
            String.valueOf(Im.PROTOCOL_ICQ))));
      }
      if (em1.getImWorkJabber() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
            String.valueOf(Im.PROTOCOL_JABBER))));
      }
      if (em1.getImWorkMsn() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
            String.valueOf(Im.PROTOCOL_MSN))));
      }
      if (em1.getImWorkNetmeeting() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
            String.valueOf(Im.PROTOCOL_NETMEETING))));
      }
      if (em1.getImWorkQq() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
            String.valueOf(Im.PROTOCOL_QQ))));
      }
      if (em1.getImWorkSkype() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
            String.valueOf(Im.PROTOCOL_SKYPE))));
      }
      if (em1.getImWorkYahoo() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
            String.valueOf(Im.PROTOCOL_YAHOO))));
      }

      if (em1.getImOtherAim() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
            String.valueOf(Im.PROTOCOL_AIM))));
      }
      if (em1.getImOtherGoogleTalk() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
            String.valueOf(Im.PROTOCOL_GOOGLE_TALK))));
      }
      if (em1.getImOtherIcq() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
            String.valueOf(Im.PROTOCOL_ICQ))));
      }
      if (em1.getImOtherJabber() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
            String.valueOf(Im.PROTOCOL_JABBER))));
      }
      if (em1.getImOtherMsn() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
            String.valueOf(Im.PROTOCOL_MSN))));
      }
      if (em1.getImOtherNetmeeting() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
            String.valueOf(Im.PROTOCOL_NETMEETING))));
      }
      if (em1.getImOtherQq() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
            String.valueOf(Im.PROTOCOL_QQ))));
      }
      if (em1.getImOtherSkype() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
            String.valueOf(Im.PROTOCOL_SKYPE))));
      }
      if (em1.getImOtherYahoo() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
            String.valueOf(Im.PROTOCOL_YAHOO))));
      }
    } else if (em1 != null && em2 != null) { // clear or update data in db
      String i = null;
      if (em2.getImHomeAim() != null || em1.getImHomeAim() != null) {
        i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
            String.valueOf(Im.PROTOCOL_AIM));
        if (i == null && em2.getImHomeAim() != null) {
          ops.add(add(id, em2.getImHomeAim(), Im.PROTOCOL_AIM, Im.TYPE_HOME, create));
        } else if (i != null && em2.getImHomeAim() == null) {
          ops.add(GoogleContact.delete(i));
        } else if (i != null && em2.getImHomeAim() != null
            && !em2.getImHomeAim().equals(em1.getImHomeAim())) {
          ops.add(update(i, em2.getImHomeAim(), Im.PROTOCOL_AIM, Im.TYPE_HOME));
        }
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
          String.valueOf(Im.PROTOCOL_GOOGLE_TALK));
      if (i == null && em2.getImHomeGoogleTalk() != null) {
        ops.add(add(id, em2.getImHomeGoogleTalk(), Im.PROTOCOL_GOOGLE_TALK, Im.TYPE_HOME, create));
      } else if (i != null && em2.getImHomeGoogleTalk() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImHomeGoogleTalk() != null
          && !em2.getImHomeGoogleTalk().equals(em1.getImHomeGoogleTalk())) {
        ops.add(update(i, em2.getImHomeGoogleTalk(), Im.PROTOCOL_GOOGLE_TALK, Im.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
          String.valueOf(Im.PROTOCOL_ICQ));
      if (i == null && em2.getImHomeIcq() != null) {
        ops.add(add(id, em2.getImHomeIcq(), Im.PROTOCOL_ICQ, Im.TYPE_HOME, create));
      } else if (i != null && em2.getImHomeIcq() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImHomeIcq() != null
          && !em2.getImHomeIcq().equals(em1.getImHomeIcq())) {
        ops.add(update(i, em2.getImHomeIcq(), Im.PROTOCOL_ICQ, Im.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
          String.valueOf(Im.PROTOCOL_JABBER));
      if (i == null && em2.getImHomeJabber() != null) {
        ops.add(add(id, em2.getImHomeJabber(), Im.PROTOCOL_JABBER, Im.TYPE_HOME, create));
      } else if (i != null && em2.getImHomeJabber() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImHomeJabber() != null
          && !em2.getImHomeJabber().equals(em1.getImHomeJabber())) {
        ops.add(update(i, em2.getImHomeJabber(), Im.PROTOCOL_JABBER, Im.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
          String.valueOf(Im.PROTOCOL_MSN));
      if (i == null && em2.getImHomeMsn() != null) {
        ops.add(add(id, em2.getImHomeMsn(), Im.PROTOCOL_MSN, Im.TYPE_HOME, create));
      } else if (i != null && em2.getImHomeMsn() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImHomeMsn() != null
          && !em2.getImHomeMsn().equals(em1.getImHomeMsn())) {
        ops.add(update(i, em2.getImHomeMsn(), Im.PROTOCOL_MSN, Im.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
          String.valueOf(Im.PROTOCOL_NETMEETING));
      if (i == null && em2.getImHomeNetmeeting() != null) {
        ops.add(add(id, em2.getImHomeNetmeeting(), Im.PROTOCOL_NETMEETING, Im.TYPE_HOME, create));
      } else if (i != null && em2.getImHomeNetmeeting() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImHomeNetmeeting() != null
          && !em2.getImHomeNetmeeting().equals(em1.getImHomeNetmeeting())) {
        ops.add(update(i, em2.getImHomeNetmeeting(), Im.PROTOCOL_NETMEETING, Im.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME), String.valueOf(Im.PROTOCOL_QQ));
      if (i == null && em2.getImHomeQq() != null) {
        ops.add(add(id, em2.getImHomeQq(), Im.PROTOCOL_QQ, Im.TYPE_HOME, create));
      } else if (i != null && em2.getImHomeQq() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImHomeQq() != null
          && !em2.getImHomeQq().equals(em1.getImHomeQq())) {
        ops.add(update(i, em2.getImHomeQq(), Im.PROTOCOL_QQ, Im.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
          String.valueOf(Im.PROTOCOL_SKYPE));
      if (i == null && em2.getImHomeSkype() != null) {
        ops.add(add(id, em2.getImHomeSkype(), Im.PROTOCOL_SKYPE, Im.TYPE_HOME, create));
      } else if (i != null && em2.getImHomeSkype() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImHomeSkype() != null
          && !em2.getImHomeSkype().equals(em1.getImHomeSkype())) {
        ops.add(update(i, em2.getImHomeSkype(), Im.PROTOCOL_SKYPE, Im.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_HOME),
          String.valueOf(Im.PROTOCOL_YAHOO));
      if (i == null && em2.getImHomeYahoo() != null) {
        ops.add(add(id, em2.getImHomeYahoo(), Im.PROTOCOL_YAHOO, Im.TYPE_HOME, create));
      } else if (i != null && em2.getImHomeYahoo() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImHomeYahoo() != null
          && !em2.getImHomeSkype().equals(em1.getImHomeYahoo())) {
        ops.add(update(i, em2.getImHomeYahoo(), Im.PROTOCOL_YAHOO, Im.TYPE_HOME));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
          String.valueOf(Im.PROTOCOL_AIM));
      if (i == null && em2.getImWorkAim() != null) {
        ops.add(add(id, em2.getImWorkAim(), Im.PROTOCOL_AIM, Im.TYPE_WORK, create));
      } else if (i != null && em2.getImWorkAim() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImWorkAim() != null
          && !em2.getImWorkAim().equals(em1.getImWorkAim())) {
        ops.add(update(i, em2.getImWorkAim(), Im.PROTOCOL_AIM, Im.TYPE_WORK));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
          String.valueOf(Im.PROTOCOL_GOOGLE_TALK));
      if (i == null && em2.getImWorkGoogleTalk() != null) {
        ops.add(add(id, em2.getImWorkGoogleTalk(), Im.PROTOCOL_GOOGLE_TALK, Im.TYPE_WORK, create));
      } else if (i != null && em2.getImWorkGoogleTalk() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImWorkGoogleTalk() != null
          && !em2.getImWorkGoogleTalk().equals(em1.getImWorkGoogleTalk())) {
        ops.add(update(i, em2.getImWorkGoogleTalk(), Im.PROTOCOL_GOOGLE_TALK, Im.TYPE_WORK));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
          String.valueOf(Im.PROTOCOL_ICQ));
      if (i == null && em2.getImWorkIcq() != null) {
        ops.add(add(id, em2.getImWorkIcq(), Im.PROTOCOL_ICQ, Im.TYPE_WORK, create));
      } else if (i != null && em2.getImWorkIcq() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImWorkIcq() != null
          && !em2.getImWorkIcq().equals(em1.getImWorkIcq())) {
        ops.add(update(i, em2.getImWorkIcq(), Im.PROTOCOL_ICQ, Im.TYPE_WORK));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
          String.valueOf(Im.PROTOCOL_JABBER));
      if (i == null && em2.getImWorkJabber() != null) {
        ops.add(add(id, em2.getImWorkJabber(), Im.PROTOCOL_JABBER, Im.TYPE_WORK, create));
      } else if (i != null && em2.getImWorkJabber() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImWorkJabber() != null
          && !em2.getImWorkJabber().equals(em1.getImWorkJabber())) {
        ops.add(update(i, em2.getImWorkJabber(), Im.PROTOCOL_JABBER, Im.TYPE_WORK));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
          String.valueOf(Im.PROTOCOL_MSN));
      if (i == null && em2.getImWorkMsn() != null) {
        ops.add(add(id, em2.getImWorkMsn(), Im.PROTOCOL_MSN, Im.TYPE_WORK, create));
      } else if (i != null && em2.getImWorkMsn() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImWorkMsn() != null
          && !em2.getImWorkMsn().equals(em1.getImWorkMsn())) {
        ops.add(update(i, em2.getImWorkMsn(), Im.PROTOCOL_MSN, Im.TYPE_WORK));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
          String.valueOf(Im.PROTOCOL_NETMEETING));
      if (i == null && em2.getImWorkNetmeeting() != null) {
        ops.add(add(id, em2.getImWorkNetmeeting(), Im.PROTOCOL_NETMEETING, Im.TYPE_WORK, create));
      } else if (i != null && em2.getImWorkNetmeeting() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImWorkNetmeeting() != null
          && !em2.getImWorkNetmeeting().equals(em1.getImWorkNetmeeting())) {
        ops.add(update(i, em2.getImWorkNetmeeting(), Im.PROTOCOL_NETMEETING, Im.TYPE_WORK));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK), String.valueOf(Im.PROTOCOL_QQ));
      if (i == null && em2.getImWorkQq() != null) {
        ops.add(add(id, em2.getImWorkQq(), Im.PROTOCOL_QQ, Im.TYPE_WORK, create));
      } else if (i != null && em2.getImWorkQq() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImWorkQq() != null
          && !em2.getImWorkQq().equals(em1.getImWorkQq())) {
        ops.add(update(i, em2.getImWorkQq(), Im.PROTOCOL_QQ, Im.TYPE_WORK));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
          String.valueOf(Im.PROTOCOL_SKYPE));
      if (i == null && em2.getImWorkSkype() != null) {
        ops.add(add(id, em2.getImWorkSkype(), Im.PROTOCOL_SKYPE, Im.TYPE_WORK, create));
      } else if (i != null && em2.getImWorkSkype() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImWorkSkype() != null
          && !em2.getImWorkSkype().equals(em1.getImWorkSkype())) {
        ops.add(update(i, em2.getImWorkSkype(), Im.PROTOCOL_SKYPE, Im.TYPE_WORK));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_WORK),
          String.valueOf(Im.PROTOCOL_YAHOO));
      if (i == null && em2.getImWorkYahoo() != null) {
        ops.add(add(id, em2.getImWorkYahoo(), Im.PROTOCOL_YAHOO, Im.TYPE_WORK, create));
      } else if (i != null && em2.getImWorkYahoo() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImWorkYahoo() != null
          && !em2.getImWorkSkype().equals(em1.getImWorkYahoo())) {
        ops.add(update(i, em2.getImWorkYahoo(), Im.PROTOCOL_YAHOO, Im.TYPE_WORK));
      }


      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
          String.valueOf(Im.PROTOCOL_AIM));
      if (i == null && em2.getImOtherAim() != null) {
        ops.add(add(id, em2.getImOtherAim(), Im.PROTOCOL_AIM, Im.TYPE_OTHER, create));
      } else if (i != null && em2.getImOtherAim() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImOtherAim() != null
          && !em2.getImOtherAim().equals(em1.getImOtherAim())) {
        ops.add(update(i, em2.getImOtherAim(), Im.PROTOCOL_AIM, Im.TYPE_OTHER));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
          String.valueOf(Im.PROTOCOL_GOOGLE_TALK));
      if (i == null && em2.getImOtherGoogleTalk() != null) {
        ops.add(add(id, em2.getImOtherGoogleTalk(), Im.PROTOCOL_GOOGLE_TALK, Im.TYPE_OTHER, create));
      } else if (i != null && em2.getImOtherGoogleTalk() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImOtherGoogleTalk() != null
          && !em2.getImOtherGoogleTalk().equals(em1.getImOtherGoogleTalk())) {
        ops.add(update(i, em2.getImOtherGoogleTalk(), Im.PROTOCOL_GOOGLE_TALK, Im.TYPE_OTHER));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
          String.valueOf(Im.PROTOCOL_ICQ));
      if (i == null && em2.getImOtherIcq() != null) {
        ops.add(add(id, em2.getImOtherIcq(), Im.PROTOCOL_ICQ, Im.TYPE_OTHER, create));
      } else if (i != null && em2.getImOtherIcq() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImOtherIcq() != null
          && !em2.getImOtherIcq().equals(em1.getImOtherIcq())) {
        ops.add(update(i, em2.getImOtherIcq(), Im.PROTOCOL_ICQ, Im.TYPE_OTHER));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
          String.valueOf(Im.PROTOCOL_JABBER));
      if (i == null && em2.getImOtherJabber() != null) {
        ops.add(add(id, em2.getImOtherJabber(), Im.PROTOCOL_JABBER, Im.TYPE_OTHER, create));
      } else if (i != null && em2.getImOtherJabber() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImOtherJabber() != null
          && !em2.getImOtherJabber().equals(em1.getImOtherJabber())) {
        ops.add(update(i, em2.getImOtherJabber(), Im.PROTOCOL_JABBER, Im.TYPE_OTHER));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
          String.valueOf(Im.PROTOCOL_MSN));
      if (i == null && em2.getImOtherMsn() != null) {
        ops.add(add(id, em2.getImOtherMsn(), Im.PROTOCOL_MSN, Im.TYPE_OTHER, create));
      } else if (i != null && em2.getImOtherMsn() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImOtherMsn() != null
          && !em2.getImOtherMsn().equals(em1.getImOtherMsn())) {
        ops.add(update(i, em2.getImOtherMsn(), Im.PROTOCOL_MSN, Im.TYPE_OTHER));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
          String.valueOf(Im.PROTOCOL_NETMEETING));
      if (i == null && em2.getImOtherNetmeeting() != null) {
        ops.add(add(id, em2.getImOtherNetmeeting(), Im.PROTOCOL_NETMEETING, Im.TYPE_OTHER, create));
      } else if (i != null && em2.getImOtherNetmeeting() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImOtherNetmeeting() != null
          && !em2.getImOtherNetmeeting().equals(em1.getImOtherNetmeeting())) {
        ops.add(update(i, em2.getImOtherNetmeeting(), Im.PROTOCOL_NETMEETING, Im.TYPE_OTHER));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
          String.valueOf(Im.PROTOCOL_QQ));
      if (i == null && em2.getImOtherQq() != null) {
        ops.add(add(id, em2.getImOtherQq(), Im.PROTOCOL_QQ, Im.TYPE_OTHER, create));
      } else if (i != null && em2.getImOtherQq() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImOtherQq() != null
          && !em2.getImOtherQq().equals(em1.getImOtherQq())) {
        ops.add(update(i, em2.getImOtherQq(), Im.PROTOCOL_QQ, Im.TYPE_OTHER));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
          String.valueOf(Im.PROTOCOL_SKYPE));
      if (i == null && em2.getImOtherSkype() != null) {
        ops.add(add(id, em2.getImOtherSkype(), Im.PROTOCOL_SKYPE, Im.TYPE_OTHER, create));
      } else if (i != null && em2.getImOtherSkype() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImOtherSkype() != null
          && !em2.getImOtherSkype().equals(em1.getImOtherSkype())) {
        ops.add(update(i, em2.getImOtherSkype(), Im.PROTOCOL_SKYPE, Im.TYPE_OTHER));
      }

      i = ID.getIdByValue(em1.getID(), String.valueOf(Im.TYPE_OTHER),
          String.valueOf(Im.PROTOCOL_YAHOO));
      if (i == null && em2.getImOtherYahoo() != null) {
        ops.add(add(id, em2.getImOtherYahoo(), Im.PROTOCOL_YAHOO, Im.TYPE_OTHER, create));
      } else if (i != null && em2.getImOtherYahoo() == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && em2.getImOtherYahoo() != null
          && !em2.getImOtherSkype().equals(em1.getImOtherYahoo())) {
        ops.add(update(i, em2.getImOtherYahoo(), Im.PROTOCOL_YAHOO, Im.TYPE_OTHER));
      }
    }
    return ops.size() > 0 ? ops : null;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((imHomeAim == null) ? 0 : imHomeAim.hashCode());
    result = prime * result + ((imHomeGoogleTalk == null) ? 0 : imHomeGoogleTalk.hashCode());
    result = prime * result + ((imHomeIcq == null) ? 0 : imHomeIcq.hashCode());
    result = prime * result + ((imHomeJabber == null) ? 0 : imHomeJabber.hashCode());
    result = prime * result + ((imHomeMsn == null) ? 0 : imHomeMsn.hashCode());
    result = prime * result + ((imHomeNetmeeting == null) ? 0 : imHomeNetmeeting.hashCode());
    result = prime * result + ((imHomeQq == null) ? 0 : imHomeQq.hashCode());
    result = prime * result + ((imHomeSkype == null) ? 0 : imHomeSkype.hashCode());
    result = prime * result + ((imHomeYahoo == null) ? 0 : imHomeYahoo.hashCode());
    result = prime * result + ((imOtherAim == null) ? 0 : imOtherAim.hashCode());
    result = prime * result + ((imOtherGoogleTalk == null) ? 0 : imOtherGoogleTalk.hashCode());
    result = prime * result + ((imOtherIcq == null) ? 0 : imOtherIcq.hashCode());
    result = prime * result + ((imOtherJabber == null) ? 0 : imOtherJabber.hashCode());
    result = prime * result + ((imOtherMsn == null) ? 0 : imOtherMsn.hashCode());
    result = prime * result + ((imOtherNetmeeting == null) ? 0 : imOtherNetmeeting.hashCode());
    result = prime * result + ((imOtherQq == null) ? 0 : imOtherQq.hashCode());
    result = prime * result + ((imOtherSkype == null) ? 0 : imOtherSkype.hashCode());
    result = prime * result + ((imOtherYahoo == null) ? 0 : imOtherYahoo.hashCode());
    result = prime * result + ((imWorkAim == null) ? 0 : imWorkAim.hashCode());
    result = prime * result + ((imWorkGoogleTalk == null) ? 0 : imWorkGoogleTalk.hashCode());
    result = prime * result + ((imWorkIcq == null) ? 0 : imWorkIcq.hashCode());
    result = prime * result + ((imWorkJabber == null) ? 0 : imWorkJabber.hashCode());
    result = prime * result + ((imWorkMsn == null) ? 0 : imWorkMsn.hashCode());
    result = prime * result + ((imWorkNetmeeting == null) ? 0 : imWorkNetmeeting.hashCode());
    result = prime * result + ((imWorkQq == null) ? 0 : imWorkQq.hashCode());
    result = prime * result + ((imWorkSkype == null) ? 0 : imWorkSkype.hashCode());
    result = prime * result + ((imWorkYahoo == null) ? 0 : imWorkYahoo.hashCode());
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
    ImSync other = (ImSync) obj;
    if (imHomeAim == null) {
      if (other.imHomeAim != null) {
        return false;
      }
    } else if (!imHomeAim.equals(other.imHomeAim)) {
      return false;
    }
    if (imHomeGoogleTalk == null) {
      if (other.imHomeGoogleTalk != null) {
        return false;
      }
    } else if (!imHomeGoogleTalk.equals(other.imHomeGoogleTalk)) {
      return false;
    }
    if (imHomeIcq == null) {
      if (other.imHomeIcq != null) {
        return false;
      }
    } else if (!imHomeIcq.equals(other.imHomeIcq)) {
      return false;
    }
    if (imHomeJabber == null) {
      if (other.imHomeJabber != null) {
        return false;
      }
    } else if (!imHomeJabber.equals(other.imHomeJabber)) {
      return false;
    }
    if (imHomeMsn == null) {
      if (other.imHomeMsn != null) {
        return false;
      }
    } else if (!imHomeMsn.equals(other.imHomeMsn)) {
      return false;
    }
    if (imHomeNetmeeting == null) {
      if (other.imHomeNetmeeting != null) {
        return false;
      }
    } else if (!imHomeNetmeeting.equals(other.imHomeNetmeeting)) {
      return false;
    }
    if (imHomeQq == null) {
      if (other.imHomeQq != null) {
        return false;
      }
    } else if (!imHomeQq.equals(other.imHomeQq)) {
      return false;
    }
    if (imHomeSkype == null) {
      if (other.imHomeSkype != null) {
        return false;
      }
    } else if (!imHomeSkype.equals(other.imHomeSkype)) {
      return false;
    }
    if (imHomeYahoo == null) {
      if (other.imHomeYahoo != null) {
        return false;
      }
    } else if (!imHomeYahoo.equals(other.imHomeYahoo)) {
      return false;
    }
    if (imOtherAim == null) {
      if (other.imOtherAim != null) {
        return false;
      }
    } else if (!imOtherAim.equals(other.imOtherAim)) {
      return false;
    }
    if (imOtherGoogleTalk == null) {
      if (other.imOtherGoogleTalk != null) {
        return false;
      }
    } else if (!imOtherGoogleTalk.equals(other.imOtherGoogleTalk)) {
      return false;
    }
    if (imOtherIcq == null) {
      if (other.imOtherIcq != null) {
        return false;
      }
    } else if (!imOtherIcq.equals(other.imOtherIcq)) {
      return false;
    }
    if (imOtherJabber == null) {
      if (other.imOtherJabber != null) {
        return false;
      }
    } else if (!imOtherJabber.equals(other.imOtherJabber)) {
      return false;
    }
    if (imOtherMsn == null) {
      if (other.imOtherMsn != null) {
        return false;
      }
    } else if (!imOtherMsn.equals(other.imOtherMsn)) {
      return false;
    }
    if (imOtherNetmeeting == null) {
      if (other.imOtherNetmeeting != null) {
        return false;
      }
    } else if (!imOtherNetmeeting.equals(other.imOtherNetmeeting)) {
      return false;
    }
    if (imOtherQq == null) {
      if (other.imOtherQq != null) {
        return false;
      }
    } else if (!imOtherQq.equals(other.imOtherQq)) {
      return false;
    }
    if (imOtherSkype == null) {
      if (other.imOtherSkype != null) {
        return false;
      }
    } else if (!imOtherSkype.equals(other.imOtherSkype)) {
      return false;
    }
    if (imOtherYahoo == null) {
      if (other.imOtherYahoo != null) {
        return false;
      }
    } else if (!imOtherYahoo.equals(other.imOtherYahoo)) {
      return false;
    }
    if (imWorkAim == null) {
      if (other.imWorkAim != null) {
        return false;
      }
    } else if (!imWorkAim.equals(other.imWorkAim)) {
      return false;
    }
    if (imWorkGoogleTalk == null) {
      if (other.imWorkGoogleTalk != null) {
        return false;
      }
    } else if (!imWorkGoogleTalk.equals(other.imWorkGoogleTalk)) {
      return false;
    }
    if (imWorkIcq == null) {
      if (other.imWorkIcq != null) {
        return false;
      }
    } else if (!imWorkIcq.equals(other.imWorkIcq)) {
      return false;
    }
    if (imWorkJabber == null) {
      if (other.imWorkJabber != null) {
        return false;
      }
    } else if (!imWorkJabber.equals(other.imWorkJabber)) {
      return false;
    }
    if (imWorkMsn == null) {
      if (other.imWorkMsn != null) {
        return false;
      }
    } else if (!imWorkMsn.equals(other.imWorkMsn)) {
      return false;
    }
    if (imWorkNetmeeting == null) {
      if (other.imWorkNetmeeting != null) {
        return false;
      }
    } else if (!imWorkNetmeeting.equals(other.imWorkNetmeeting)) {
      return false;
    }
    if (imWorkQq == null) {
      if (other.imWorkQq != null) {
        return false;
      }
    } else if (!imWorkQq.equals(other.imWorkQq)) {
      return false;
    }
    if (imWorkSkype == null) {
      if (other.imWorkSkype != null) {
        return false;
      }
    } else if (!imWorkSkype.equals(other.imWorkSkype)) {
      return false;
    }
    if (imWorkYahoo == null) {
      if (other.imWorkYahoo != null) {
        return false;
      }
    } else if (!imWorkYahoo.equals(other.imWorkYahoo)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean isNull() {
    return imHomeAim == null && imHomeGoogleTalk == null && imHomeIcq == null && imHomeJabber == null
        && imHomeMsn == null && imHomeNetmeeting == null && imHomeQq == null && imHomeSkype == null
        && imHomeYahoo == null && imWorkAim == null && imWorkGoogleTalk == null && imWorkIcq == null
        && imWorkJabber == null && imWorkMsn == null && imWorkNetmeeting == null && imWorkQq == null
        && imWorkSkype == null && imWorkYahoo == null && imOtherAim == null && imOtherGoogleTalk == null
        && imOtherIcq == null && imOtherJabber == null && imOtherMsn == null && imOtherNetmeeting == null
        && imOtherQq == null && imOtherSkype == null && imOtherYahoo == null;
  }


}
