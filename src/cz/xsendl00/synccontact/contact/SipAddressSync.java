package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.CommonDataKinds.SipAddress;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing a SIP address for the contact.
 * @author portilo
 *
 */
public class SipAddressSync extends AbstractType implements ContactInterface {

  private String workSip;
  private String homeSip;
  private String otherSip;
  
  public String getWorkSip() {
    return workSip;
  }
  public void setWorkSip(String workSip) {
    this.workSip = workSip;
  }
  public String getHomeSip() {
    return homeSip;
  }
  public void setHomeSip(String homeSip) {
    this.homeSip = homeSip;
  }
  public String getOtherSip() {
    return otherSip;
  }
  public void setOtherSip(String otherSip) {
    this.otherSip = otherSip;
  }  
  @Override
  public String toString() {
    return "SipAddressSync [workSip=" + workSip + ", homeSip=" + homeSip
        + ", otherSip=" + otherSip + "]";
  }
  @Override
  public void defaultValue() {
    workSip = Constants.WORK_SIP;
    homeSip = Constants.HOME_SIP;
    otherSip = Constants.OTHER_SIP;
  }
  
  public static ContentValues compare(SipAddressSync obj1, SipAddressSync obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getHomeSip() != null) {
        values.put(Constants.HOME_SIP, obj2.getHomeSip());
      }
      if (obj2.getWorkSip() != null) {
        values.put(Constants.WORK_SIP, obj2.getWorkSip());
      }
      if (obj2.getOtherSip() != null) {
        values.put(Constants.OTHER_SIP, obj2.getOtherSip());
      }
    } else if (obj1 == null && obj2 == null) { // nothing
      
    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getHomeSip() != null) {
        values.putNull(Constants.HOME_SIP);
      }
      if (obj1.getWorkSip() != null) {
        values.putNull(Constants.WORK_SIP);
      }
      if (obj1.getOtherSip() != null) {
        values.putNull(Constants.OTHER_SIP);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getHomeSip() != null) {
        values.put(Constants.HOME_SIP, obj2.getHomeSip());
      } else {
        values.putNull(Constants.HOME_SIP);
      }
      if (obj2.getWorkSip() != null) {
        values.put(Constants.WORK_SIP, obj2.getWorkSip());
      } else {
        values.putNull(Constants.WORK_SIP);
      }
      if (obj2.getOtherSip() != null) {
        values.put(Constants.OTHER_SIP, obj2.getOtherSip());
      } else {
        values.putNull(Constants.OTHER_SIP);
      }
    }
    return values;
  }

  /**
   * 
   * @param id        raw_contact id
   * @param value     name of protocol
   * @param protocol  like {@link SipAddress.PROTOCOl_AIM}
   * @param type      like SipAddress.TYPE_HOME
   * @return
   */
  public static ContentProviderOperation add(int id, String value, int type, boolean create) {
    if (create) {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValueBackReference(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, SipAddress.CONTENT_ITEM_TYPE)
    .withValue(SipAddress.TYPE, type)
    .withValue(SipAddress.DATA, value)
          .build();
    } else {
    return ContentProviderOperation.newInsert(Data.CONTENT_URI)
    .withValue(Data.RAW_CONTACT_ID, id)
    .withValue(Data.MIMETYPE, SipAddress.CONTENT_ITEM_TYPE)
    .withValue(SipAddress.TYPE, type)
    .withValue(SipAddress.DATA, value)
    .build();
    }
  }
  
  public static ContentProviderOperation update(int id, String value, int type) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{String.valueOf(id)})
        .withValue(Data.MIMETYPE, SipAddress.CONTENT_ITEM_TYPE)
        .withValue(SipAddress.DATA, value)
        .build();
  }
  

  public static ArrayList<ContentProviderOperation> operation(int id, SipAddressSync em1, SipAddressSync em2, boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      if (em2.getHomeSip() != null) {
        ops.add(add(id, em2.getHomeSip(), SipAddress.TYPE_HOME, create));
      }
      if (em2.getOtherSip()!= null) {
        ops.add(add(id, em2.getOtherSip(), SipAddress.TYPE_OTHER, create));
      }
      if (em2.getWorkSip() != null) {
        ops.add(add(id, em2.getWorkSip(), SipAddress.TYPE_WORK, create));
      }
    } else if (em1 == null && em2 == null) { // nothing
      
    } else if (em1 != null && em2 == null) { // delete
      if (em1.getHomeSip() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(SipAddress.TYPE_HOME), null)));
      }
      if (em1.getOtherSip() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(SipAddress.TYPE_OTHER), null)));
      }
      if (em1.getWorkSip() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(SipAddress.TYPE_WORK), null)));
      }
      
    } else if (em1 != null && em2 != null) { // clear or update data in db
      ArrayList<ContentProviderOperation> op;
      op = createOps(em1.getHomeSip(), em2.getHomeSip(), em1.getID(), SipAddress.TYPE_HOME, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      op = createOps(em1.getOtherSip(), em2.getOtherSip(), em1.getID(), SipAddress.TYPE_OTHER, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      
      op = createOps(em1.getWorkSip(), em2.getWorkSip(), em1.getID(), SipAddress.TYPE_WORK, id, create);
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
  private static ArrayList<ContentProviderOperation> createOps(String value1, String value2, List<ID> ids, int type, int id, boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if(value2 != null || value2 != null) {
      String i = ID.getIdByValue(ids, String.valueOf(type), null);
      if (i == null && value2 != null) {
        ops.add(add(id, value2, type, create));
      } else if ( i != null && value2 == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && value2 != null && !value2.equals(value1)) { 
        ops.add(update(id, value2, type));
      }
    }
    return ops.size() > 0 ? ops : null;
  }
}
