package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class ContactRow {
  
  private static final String TAG = "ContactShow";
  
  private static ArrayList<ContactRow> contacts = null;
  private String name;
  private String id;
  private Boolean sync;
  private String[] groups;
  private Integer idTable;
  private String accouNamePrevious;
  private String accouTypePrevious;

  public ContactRow(String id, String name) {
    this(id, name, false, null, null, null, null);
  }
  
  public ContactRow(String id, String name, String accouNamePrevious, String accouTypePrevious) {
    this(id, name, false, null, null, accouNamePrevious, accouTypePrevious);
  }
  
  public ContactRow(String id, String name, Boolean sync, Integer idTable, String accouNamePrevious, String accouTypePrevious) {
    this(id, name, sync, null, idTable, accouNamePrevious, accouTypePrevious);
  }
  
  public ContactRow(String id, String name, Boolean sync, String[] groups, Integer idTable, String accouNamePrevious, String accouTypePrevious) {
    this.id = id;
    this.name = name;
    this.sync = sync;
    this.groups = groups;
    this.idTable = idTable;
    this.accouNamePrevious = accouNamePrevious;
    this.accouTypePrevious = accouTypePrevious;
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
        ", accouNamePrevious: " + accouNamePrevious + ", accouTypePrevious: " + accouTypePrevious + "\n";
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
}
