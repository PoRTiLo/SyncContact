package cz.xsendl00.synccontact.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class ContactRow implements Parcelable {
  
  private static final String TAG = "ContactShow";
  
  private static ArrayList<ContactRow> contacts = null;
  private String name;
  private String id;
  private Boolean sync;
  private String[] groups;
  private Integer idTable;
  private String accouNamePrevious;
  private String accouTypePrevious;
  private String timestamp;
  private String uuid;

  public ContactRow() {
    this(null, null, true, null, null, null, null, null, null);
  }
  
  public ContactRow(String id, String name) {
    this(id, name, false, null, null, null, null, null, null);
  }
  
  public ContactRow(String id, String name, String accouNamePrevious, String accouTypePrevious) {
    this(id, name, false, null, null, accouNamePrevious, accouTypePrevious, null, null);
  }
  
  public ContactRow(String id, String name, Boolean sync, Integer idTable, String accouNamePrevious, String accouTypePrevious, String timestamp, String uuid) {
    this(id, name, sync, null, idTable, accouNamePrevious, accouTypePrevious, timestamp, uuid);
  }
  
  public ContactRow(String id, String name, Boolean sync, String[] groups, Integer idTable, String accouNamePrevious, String accouTypePrevious, 
      String timestamp, String uuid) {
    this.id = id;
    this.name = name;
    this.sync = sync;
    this.groups = groups;
    this.idTable = idTable;
    this.accouNamePrevious = accouNamePrevious;
    this.accouTypePrevious = accouTypePrevious;
    this.timestamp = timestamp;
    this.setUuid(uuid);
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Id: "+id + ", name: " + name + ", sync: " + sync + ", groups: " + groups + ", idTable: " + idTable + 
        ", accouNamePrevious: " + accouNamePrevious + ", accouTypePrevious: " + accouTypePrevious + 
        ", timestamp: " + timestamp + "\n";
  }
  
  public static ArrayList<String> fetchGroupMembersId(ContentResolver contentResolver, String groupId) {
    ArrayList<String> groupMembers = new ArrayList<String>();
    String where = CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" +groupId + " AND " + 
        CommonDataKinds.GroupMembership.MIMETYPE + "='" + CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'";
    String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID};
    Cursor cursor = contentResolver.query(Data.CONTENT_URI, projection, where, null, null);
    while (cursor.moveToNext()) {
      groupMembers.add(cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID)));
    } 
    cursor.close();
    return groupMembers;
  }
  
  public static ArrayList<ContactRow> fetchGroupMembers(ContentResolver contentResolver, String groupId) {
    ArrayList<ContactRow> groupMembers = new ArrayList<ContactRow>();
    String where = CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" +groupId + " AND " + 
        CommonDataKinds.GroupMembership.MIMETYPE + "='" + CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'";
    String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID, Data.DISPLAY_NAME, RawContacts.ACCOUNT_NAME, RawContacts.ACCOUNT_TYPE};
    Cursor cursor = contentResolver.query(Data.CONTENT_URI, projection, where, null, Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    while (cursor.moveToNext()) {
      ContactRow contactShow = new ContactRow(cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID)),
          cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME)),
          cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_NAME)),
          cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_TYPE))
          );
      groupMembers.add(contactShow);
    } 
    cursor.close();
    return groupMembers;
  }
  
  public static ArrayList<ContactRow> fetchAllContact(ContentResolver contentResolver) {
    if (contacts == null) {
      contacts = new ArrayList<ContactRow>(); 
      String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID, Data.DISPLAY_NAME, 
          CommonDataKinds.GroupMembership.GROUP_ROW_ID, RawContacts.ACCOUNT_NAME, RawContacts.ACCOUNT_TYPE};
      Cursor cursor = contentResolver.query(Data.CONTENT_URI, projection, null, null, Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
      while (cursor.moveToNext()) {
        ContactRow contactShow = new ContactRow(cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID)),
            cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME)),
            cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_NAME)),
          cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_TYPE))
            );
        boolean found = false;
        for (ContactRow co : contacts) {
          if ( co.getId().equals(contactShow.getId())) {
            found = true;
          } else {
            
          }
        }
        if( !found) {
          contacts.add(contactShow);
        }
      }
    }
    Log.i(TAG, "all user:" + contacts.size());
    return contacts;
  }

  public Boolean isSync() {
    return sync;
  }

  public void setSync(Boolean sync) {
    this.sync = sync;
  }

  public String[] getGroupNames() {
    return groups;
  }

  public void setGroupNames(String[] groupNames) {
    this.groups = groupNames;
  }

  public Integer getIdTable() {
    return idTable;
  }

  public void setIdTable(Integer idTable) {
    this.idTable = idTable;
  }

  public String getAccouNamePrevious() {
    return accouNamePrevious;
  }

  public void setAccouNamePrevious(String accouNamePrevious) {
    this.accouNamePrevious = accouNamePrevious;
  }

  public String getAccouTypePrevious() {
    return accouTypePrevious;
  }

  public void setAccouTypePrevious(String accouTypePrevious) {
    this.accouTypePrevious = accouTypePrevious;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
  
  @SuppressLint("SimpleDateFormat")
  public static String createTimestamp() {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
    df1.setTimeZone(TimeZone.getTimeZone("GMT"));
    return df1.format(c.getTime()) +"Z";
  }

  public static String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }
  
  public String getUuid() {
    if (this.uuid == null) {
      uuid = generateUUID();
    }
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public int describeContents() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(uuid);
    dest.writeString(this.accouNamePrevious);
    dest.writeString(this.accouTypePrevious);
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeString(this.timestamp);
    dest.writeInt(this.idTable);
    dest.writeValue(this.sync);
    dest.writeStringArray(this.groups);
  }
  
  public static final Parcelable.Creator<ContactRow> CREATOR = new Parcelable.Creator<ContactRow>() {
    public ContactRow createFromParcel(Parcel in) {
      return new ContactRow(in);
    }

    public ContactRow[] newArray(int size) {
      return new ContactRow[size];
    }
  };

  private ContactRow(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.sync = in.readInt() == 1;
    in.readStringArray(groups);
    this.idTable = in.readInt();
    this.accouNamePrevious = in.readString();
    this.accouTypePrevious = in.readString();
    this.timestamp = in.readString();
    this.uuid = in.readString();
  }
}
