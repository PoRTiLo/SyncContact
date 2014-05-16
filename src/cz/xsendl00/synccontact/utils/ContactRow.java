package cz.xsendl00.synccontact.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.format.Time;
import android.util.Log;

public class ContactRow {


  private static final String TAG = "ContactShow";

  private static ArrayList<ContactRow> contacts = null;
  private String name;
  private String id;
  private Boolean sync;
  private List<String> groupsId;
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
  public ContactRow(String id,
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
  public ContactRow(String id,
      String name,
      Boolean sync,
      List<String> groups,
      Integer idTable,
      String accouNamePrevious,
      String accouTypePrevious,
      String timestamp,
      String uuid) {
    this.id = id;
    this.name = name;
    this.sync = sync;
    this.groupsId = groups == null ? new ArrayList<String>() : groups;
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
            cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID)),
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

  public static List<ContactRow> fetchGroupMembersName(ContentResolver contentResolver,
      String groupId) {
    SortedSet<ContactRow> groupMembers = new TreeSet<ContactRow>(new ContactRowComparator());
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
        contactRow.setId(cursor.getString(cursor.getColumnIndex(Data.CONTACT_ID)));
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

  /**
   * Fetch all contact from contact provider database.
   *
   * @param contentResolver resolver
   * @return list of ContactRow.
   */
  public static ArrayList<ContactRow> fetchAllContact(ContentResolver contentResolver) {
    Cursor cursor = null;
    try {
      contacts = new ArrayList<ContactRow>();
      String[] projection = new String[]{RawContacts._ID, RawContacts.DISPLAY_NAME_PRIMARY,
          RawContacts.ACCOUNT_NAME, RawContacts.ACCOUNT_TYPE};
      cursor = contentResolver.query(RawContacts.CONTENT_URI, projection, null, null,
          RawContacts.DISPLAY_NAME_PRIMARY + " COLLATE LOCALIZED ASC");
      while (cursor.moveToNext()) {
        ContactRow contactShow = new ContactRow(
            cursor.getString(cursor.getColumnIndex(RawContacts._ID)),
            cursor.getString(cursor.getColumnIndex(RawContacts.DISPLAY_NAME_PRIMARY)),
            cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_NAME)),
            cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_TYPE)));
        contacts.add(contactShow);
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
    return contacts;
  }

  public Boolean isSync() {
    return sync;
  }

  public void setSync(Boolean sync) {
    this.sync = sync;
  }

  public List<String> getGroupsId() {
    return groupsId;
  }

  public void setGroupsId(List<String> groupNames) {
    this.groupsId = groupNames;
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

  /**
   * Get new timestamp "yyyyMMddHHmmss".
   *
   * @return The {@link String} representation timestamp.
   */
  public static String createTimestamp() {
    Time now = new Time(Time.getCurrentTimezone());
    now.setToNow();
    StringBuilder builder = new StringBuilder();
    builder.append(now.year);
    builder.append(now.month > 9 ? now.month : "0" + now.month);
    builder.append(now.hour > 9 ? now.hour : "0" + now.hour);
    builder.append(now.minute > 9 ? now.minute : "0" + now.minute);
    builder.append(now.second > 9 ? now.second : "0" + now.second);
    builder.append("Z");
    return builder.toString();
  }

  @SuppressLint("SimpleDateFormat")
  public static String timestamptoDate(String str) {
    SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMddHHmmss");
    String out = null;
    try {
      out = df1.parse(str).toString();
    } catch (ParseException e) {
      Log.e(TAG, "Can not formated timestamp from db to readable string :" + out);
      out = "No synchronization.";
    }
    return out;
  }

  /**
   * Generate UUID.
   *
   * @return UUID.toString
   */
  public static String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  /**
   * Get UUID.
   *
   * @return UUID.
   */
  public String getUuid() {
    if (this.uuid == null) {
      uuid = generateUUID();
    }
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
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
}
