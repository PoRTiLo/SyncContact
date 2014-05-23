package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import cz.xsendl00.synccontact.database.AndroidDB;

public class ContactRow extends AbstractRow {

  private static final String TAG = "ContactShow";
  private List<String> groupsId;
  private String accouNamePrevious;
  private String accouTypePrevious;
  private String accountPreviousId;
  private String timestamp;

  public ContactRow() {
    this(null, null, true, null, null, null, null, null, null);
  }

  public ContactRow(Integer id, String name) {
    this(id, name, false, null, null, null, null, null, null);
  }

  public ContactRow(Integer id, String name, String accouNamePrevious, String accouTypePrevious) {
    this(id, name, false, null, null, accouNamePrevious, accouTypePrevious, null, null);
  }

  /**
   * @param id
   * @param name
   * @param sync
   * @param idTable
   * @param accouNamePrevious
   * @param accouTypePrevious
   * @param timestamp
   * @param uuid
   */
  public ContactRow(Integer id,
      String name,
      Boolean sync,
      Integer idTable,
      String accouNamePrevious,
      String accouTypePrevious,
      String timestamp,
      String uuid) {
    this(id, name, sync, null, idTable, accouNamePrevious, accouTypePrevious, timestamp, uuid);
  }

  /**
   * @param id
   * @param name
   * @param sync
   * @param groupsId
   * @param idTable
   * @param accouNamePrevious
   * @param accouTypePrevious
   * @param timestamp
   * @param uuid
   */
  public ContactRow(Integer id,
      String name,
      Boolean sync,
      List<String> groups,
      Integer idTable,
      String accouNamePrevious,
      String accouTypePrevious,
      String timestamp,
      String uuid) {
    super(id, name, sync, idTable, uuid);
    this.groupsId = groups == null ? new ArrayList<String>() : groups;
    this.accouNamePrevious = accouNamePrevious;
    this.accouTypePrevious = accouTypePrevious;
    this.timestamp = timestamp;
  }




  public Set<String> fetchGroupMembersId(ContentResolver contentResolver, String groupId) {
    Set<String> groupMembers = new HashSet<String>();
    String where = CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" + groupId + " AND "
        + CommonDataKinds.GroupMembership.MIMETYPE + "='"
        + CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'";
    String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID};
    Cursor cursor = contentResolver.query(Data.CONTENT_URI, projection, where, null, null);
    while (cursor.moveToNext()) {
      groupMembers.add(cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID)));
    }
    cursor.close();
    return groupMembers;
  }

  /**
   * Get number of member in group defined by groupId.
   *
   * @param contentResolver content resolver
   * @param groupId group id
   * @return number of members
   */
  public static int fetchGroupMembersCount(ContentResolver contentResolver, String groupId) {
    Set<String> groupMembers = new HashSet<String>();
    Cursor cursor = null;
    try {
      StringBuilder where = new StringBuilder();
      where.append(CommonDataKinds.GroupMembership.GROUP_ROW_ID)
          .append("=")
          .append(groupId)
          .append(" AND ")
          .append(CommonDataKinds.GroupMembership.MIMETYPE)
          .append("='")
          .append(CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
          .append("'");
      String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID};
      cursor = contentResolver.query(Data.CONTENT_URI, projection, where.toString(), null, null);
      while (cursor.moveToNext()) {
        groupMembers.add(cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID)));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (cursor != null && !cursor.isClosed()) {
          cursor.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    return groupMembers.size();
  }

  /**
   * Fetch group member with raw_id, display_name_primary, account_type, account_name.
   *
   * @param contentResolver content resolver
   * @param groupId group id.
   * @return set of contactRow
   */
  public List<ContactRow> fetchGroupMembers(ContentResolver contentResolver, String groupId) {
    Set<ContactRow> groupMembers = new HashSet<ContactRow>();
    Cursor cursor = null;
    try {
      StringBuilder where = new StringBuilder();
      where.append(CommonDataKinds.GroupMembership.GROUP_ROW_ID)
          .append("=")
          .append(groupId)
          .append(" AND ")
          .append(CommonDataKinds.GroupMembership.MIMETYPE)
          .append("='")
          .append(CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
          .append("'");
      String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID,
          RawContacts.DISPLAY_NAME_PRIMARY, RawContacts.ACCOUNT_NAME, RawContacts.ACCOUNT_TYPE};
      cursor = contentResolver.query(Data.CONTENT_URI, projection, where.toString(), null,
          RawContacts.DISPLAY_NAME_PRIMARY + " COLLATE LOCALIZED ASC");
      while (cursor.moveToNext()) {
        ContactRow contactShow = new ContactRow(
            cursor.getInt(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID)),
            cursor.getString(cursor.getColumnIndex(RawContacts.DISPLAY_NAME_PRIMARY)),
            cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_NAME)),
            cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_TYPE)));
        contactShow.getGroupsId().add(groupId);
        groupMembers.add(contactShow);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }
    return new ArrayList<ContactRow>(groupMembers);
  }

  public static List<ContactRow> fetchAllRawContact(ContentResolver contentResolver, String where) {
    List<ContactRow> contactRows = new ArrayList<ContactRow>();
    Cursor cursor = null;

    try {
      String[] projection = new String[]{
          RawContacts.DISPLAY_NAME_PRIMARY, RawContacts.ACCOUNT_NAME,
          RawContacts.ACCOUNT_TYPE, RawContacts._ID, RawContacts.SYNC1,
          RawContacts.SYNC2, RawContacts.SYNC3, RawContacts.SYNC4};

      cursor = contentResolver.query(RawContacts.CONTENT_URI, projection, where, null,
          RawContacts.DISPLAY_NAME_PRIMARY + " COLLATE LOCALIZED ASC");
      while (cursor.moveToNext()) {
        ContactRow contactRow = new ContactRow();
        contactRow.setAccouNamePrevious(cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_NAME)));
        contactRow.setAccouTypePrevious(cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_TYPE)));
        contactRow.setName(cursor.getString(cursor.getColumnIndex(RawContacts.DISPLAY_NAME_PRIMARY)));
        contactRow.setId(cursor.getInt(cursor.getColumnIndex(RawContacts._ID)));
        contactRow.setSync(cursor.getInt(cursor.getColumnIndex(RawContacts.SYNC1)) == 1);
        contactRow.setLastSyncTime(cursor.getString(cursor.getColumnIndex(RawContacts.SYNC2)));
        contactRow.setConverted(
            AndroidDB.SET_CONVERT.equals(cursor.getString(cursor.getColumnIndex(RawContacts.SYNC3))));
        contactRow.setUuid(cursor.getString(cursor.getColumnIndex(RawContacts.SYNC4)));
        contactRows.add(contactRow);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }
    return contactRows;
  }

  public static List<ContactRow> fetchGroupMembersName(ContentResolver contentResolver,
      Integer groupId) {
    SortedSet<ContactRow> groupMembers = new TreeSet<ContactRow>(new RowComparator());
    Cursor cursor = null;
    try {
      String where = CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" + groupId + " AND "
          + CommonDataKinds.GroupMembership.MIMETYPE + "='"
          + CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'";
      String[] projection = new String[]{Data.DISPLAY_NAME, Data.CONTACT_ID};
      cursor = contentResolver.query(Data.CONTENT_URI, projection, where, null, Data.DISPLAY_NAME
          + " COLLATE LOCALIZED ASC");
      while (cursor.moveToNext()) {
        ContactRow contactRow = new ContactRow();
        contactRow.setName(cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME)));
        contactRow.setId(cursor.getInt(cursor.getColumnIndex(Data.CONTACT_ID)));
        Log.i(TAG, contactRow.toString());
        groupMembers.add(contactRow);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (cursor != null && !cursor.isClosed()) {
          cursor.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return new ArrayList<ContactRow>(groupMembers);
  }

  public String getGroupsIdToString() {
    StringBuilder builder = new StringBuilder();
    for (String groupId : groupsId) {
      builder.append(groupId);
      builder.append(",");
    }
    return builder.toString();
  }

  public static List<String> getGroupsIdFromString(String stringId) {
    return Arrays.asList(stringId.split(","));
  }

  public List<String> getGroupsId() {
    return groupsId;
  }

  public void setGroupsId(List<String> groupNames) {
    this.groupsId = groupNames;
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



  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    ContactRow other = (ContactRow) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "ContactRow [name=" + name + ", id=" + id + ", sync=" + sync + ", groupsId=" + groupsId
        + ", idTable=" + idTable + ", accouNamePrevious=" + accouNamePrevious
        + ", accouTypePrevious=" + accouTypePrevious + ", timestamp=" + timestamp + ", uuid="
        + uuid + "]";
  }

  /**
   * @return Returns the accountPreviousId.
   */
  public String getAccountPreviousId() {
    return accountPreviousId;
  }

  /**
   * @param accountPreviousId The accountPreviousId to set.
   */
  public void setAccountPreviousId(String accountPreviousId) {
    this.accountPreviousId = accountPreviousId;
  }
}
