package cz.xsendl00.synccontact.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.androidannotations.annotations.EBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;

@EBean
public class HelperSQL extends SQLiteOpenHelper {

  private static final String TAG = "HelperSQL";

  private static final String DATABASE_NAME = "SyncContact";
  private static final int DATABASE_VERSION = 1;
  private static final String GROUP_TABLE_NAME = "groupGoogle";
  // Contacts Table Columns names
  private static final String GROUP_KEY_ID = "id_";
  private static final String GROUP_KEY_ID_GROUP = "group_id_";
  private static final String GROUP_KEY_GROUP = "groupName_";
  private static final String GROUP_KEY_SIZE = "groupSize_";
  private static final String GROUP_KEY_SYNC = "sync_";
  private static final String GROUP_KEY_UUID = "uuid_";
  private static final String CREATE_GROUP_TABLE = "CREATE TABLE IF NOT EXISTS "
      + GROUP_TABLE_NAME
      + " ("
      + GROUP_KEY_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + GROUP_KEY_GROUP
      + " TEXT, "
      + GROUP_KEY_SIZE
      + " INTEGER, "
      + GROUP_KEY_SYNC
      + " INTEGER, "
      + GROUP_KEY_UUID
      + " INTEGER, "
      + GROUP_KEY_ID_GROUP + " TEXT " + ");";

  private static final String CONTACT_TABLE_NAME = "contactGoogle";

  private static final String CONTACT_KEY_ID = "id_";
  private static final String CONTACT_KEY_ID_CONTACT = "contact_id_";
  private static final String CONTACT_KEY_NAME = "contactName_";
  private static final String CONTACT_KEY_SYNC = "sync_";
  private static final String CONTACT_KEY_ACCOUNT_NAME = "account_name_previous_";
  private static final String CONTACT_KEY_ACCOUNT_TYPE = "account_type_previous_";
  private static final String CONTACT_KEY_TIMESTAMP = "timestamp_";
  private static final String CONTACT_KEY_UUID = "uuid_";
  private static final String CONTACT_KEY_GROUPS_ID = "groups_id_";

  private static final String CREATE_CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS "
      + CONTACT_TABLE_NAME
      + " ("
      + CONTACT_KEY_ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + CONTACT_KEY_NAME
      + " TEXT, "
      + CONTACT_KEY_SYNC
      + " INTEGER, "
      + CONTACT_KEY_ACCOUNT_NAME
      + " TEXT, "
      + CONTACT_KEY_ACCOUNT_TYPE
      + " TEXT, "
      + CONTACT_KEY_TIMESTAMP
      + " TEXT, "
      + CONTACT_KEY_UUID
      + " TEXT, "
      + CONTACT_KEY_GROUPS_ID
      + " TEXT, "
      + CONTACT_KEY_ID_CONTACT
      + " TEXT " + ");";

  private Context context;
  private ContactManager contactManager;

  //@Bean
  //protected AndroidDB androidDB;

  /**
   * Create instance database.
   * @param context context
   */
  public HelperSQL(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    contactManager = ContactManager.getInstance(context);
    this.context = context;
  }

  /* Create table */
  /**
   * {@inheritDoc}
   */
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_GROUP_TABLE);
    db.execSQL(CREATE_CONTACT_TABLE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE_NAME);
    // Create tables again
    onCreate(db);
  }

  /**
   * Drop database.
   */
  public void drop() {
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE_NAME);
    onCreate(db);
  }

  /**
   * Adding new group.
   *
   * @param group new group
   */
  public void addGroup(GroupRow group) {
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      ContentValues values = new ContentValues();
      values.put(GROUP_KEY_GROUP, group.getName());
      values.put(GROUP_KEY_SYNC, group.isSync());
      values.put(GROUP_KEY_ID_GROUP, group.getId());
      values.put(GROUP_KEY_SIZE, group.getSize());
      values.put(GROUP_KEY_UUID, group.getUuid());
      // Inserting Row
      db.insert(GROUP_TABLE_NAME, null, values);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (db.isOpen()) {
          db.close(); // Closing database connection
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public List<GroupRow> getGroupsSync() {
    List<GroupRow> groupRows = new ArrayList<GroupRow>();
    String selectQuery = "SELECT " + GROUP_KEY_GROUP + "," + GROUP_KEY_UUID
        + "," + GROUP_KEY_SYNC + " FROM " + GROUP_TABLE_NAME + " WHERE "
        + GROUP_KEY_SYNC + " =1";
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        // Adding contact to list
        GroupRow groupRow = new GroupRow();
        groupRow.setName(cursor.getString(0));
        groupRow.setUuid(cursor.getString(1));
        groupRow.setSync(cursor.getInt(2) == 1);
        groupRows.add(groupRow);
      } while (cursor.moveToNext());
      cursor.close();
    }
    db.close();
    return groupRows;

  }

  /**
   * Getting All Groups.
   * @return List of group
   */
  public List<GroupRow> getAllGroups() {
    List<GroupRow> groupList = new ArrayList<GroupRow>();
    String selectQuery = "SELECT  * FROM " + GROUP_TABLE_NAME;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        GroupRow group = new GroupRow();
        group.setIdTable(cursor.getInt(0));
        group.setName(cursor.getString(1));
        group.setSize(cursor.getInt(2));
        group.setSync(cursor.getInt(3) > 0);
        group.setUuid(cursor.getString(4));
        group.setId(cursor.getString(5));
        // Adding group to list
        groupList.add(group);
      } while (cursor.moveToNext());
      cursor.close();
    }
    db.close();
    return groupList;
  }

  public GroupRow getGroupUuidName(String id) {
    String selectQuery = "SELECT " + GROUP_KEY_UUID +", " + GROUP_KEY_GROUP + " FROM "
          + GROUP_TABLE_NAME + " WHERE " + GROUP_KEY_ID_GROUP + " =" + id;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    GroupRow result = new GroupRow();
    if (cursor.moveToFirst()) {
      do {
        result.setUuid(cursor.getString(0));
        result.setName(cursor.getString(1));
      } while (cursor.moveToNext());
      cursor.close();
    }
    db.close();
    return result;
  }
  /**
   * Updating single group. Only SYNC
   * @param group Group for update
   * @return result of success.
   */
  public int updateGroupSync(GroupRow group) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(GROUP_KEY_SYNC, group.isSync());
    int res = db.update(GROUP_TABLE_NAME, values, GROUP_KEY_ID_GROUP + " = ?",
        new String[] { String.valueOf(group.getId()) });
    db.close();
    return res;
  }

  public void updateGroupsUuid(List<GroupRow> groups) {

    String sql = "UPDATE " + GROUP_TABLE_NAME + " SET " + GROUP_KEY_UUID
        + " =? WHERE " + GROUP_KEY_ID_GROUP + "=?";
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      db.beginTransaction();
      SQLiteStatement stmt = db.compileStatement(sql);
      for (GroupRow groupRow : groups) {
        stmt.bindString(1, groupRow.getUuid());
        stmt.bindLong(2, Long.valueOf(groupRow.getId()));
        stmt.executeUpdateDelete();
        stmt.clearBindings();
      }
      db.setTransactionSuccessful();
      Log.i(TAG, "Contact update: " + groups.size());
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        contactManager.setContactsLocalReload(false);
        db.endTransaction();
        if (db.isOpen()) {
          db.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Deleting single group.
   * @param group Deleted group
   */
  public void deleteGroup(GroupRow group) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(GROUP_TABLE_NAME, GROUP_KEY_ID + " = ?",
        new String[] { String.valueOf(group.getIdTable()) });
    db.close();
  }

  /**
   * Insert data from database..
   * @param groups list of groups
   */
  public void fillGroups(List<GroupRow> groups) {
    List<GroupRow> grTable = getAllGroups();
    for (GroupRow gr : groups) {
      boolean found = false;
      for (GroupRow grT : grTable) {
        if (gr.getId().equals(grT.getId())) {
          gr.setSync(grT.isSync());
          gr.setUuid(grT.getUuid());
          gr.setIdTable(grT.getIdTable());
          found = true;
          break;
        }
      }
      if (!found) {
        addGroup(gr);
      }
    }
  }

  /**
   * Adding new contacts.
   *
   * @param contacts new contact
   */
  public void addContacts(List<ContactRow> contacts) {

    String sql = "INSERT INTO " + CONTACT_TABLE_NAME + " ( " + CONTACT_KEY_NAME
        + ", " + CONTACT_KEY_SYNC + ", " + CONTACT_KEY_ID_CONTACT + ", "
        + CONTACT_KEY_ACCOUNT_NAME + ", " + CONTACT_KEY_ACCOUNT_TYPE + ", "
        + CONTACT_KEY_TIMESTAMP + ", " + CONTACT_KEY_UUID + ", " + CONTACT_KEY_GROUPS_ID
        + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      db.beginTransaction();
      SQLiteStatement stmt = db.compileStatement(sql);

      for (ContactRow contactRow : contacts) {
        stmt.bindString(1, contactRow.getName());
        stmt.bindLong(2, contactRow.isSync() ? 1 : 0);
        stmt.bindString(3, contactRow.getId());
        stmt.bindString(4, contactRow.getAccouNamePrevious());
        stmt.bindString(5, contactRow.getAccouTypePrevious());
        stmt.bindString(6, contactRow.getTimestamp());
        stmt.bindString(7, contactRow.getUuid());
        stmt.bindString(8, contactRow.getGroupsIdToString());

        stmt.executeInsert();
        stmt.clearBindings();
      }
      Log.i(TAG, "Insert entries: " + contacts.size() + " into "
          + CONTACT_TABLE_NAME);
      db.setTransactionSuccessful();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      db.endTransaction();
      if (db.isOpen()) {
        db.close(); // Closing database connection
      }
    }
  }

  public List<ContactRow> getContactsSync() {
    List<ContactRow> contactList = new ArrayList<ContactRow>();
    String selectQuery = "SELECT " + CONTACT_KEY_NAME + "," + CONTACT_KEY_UUID
        + "," + CONTACT_KEY_SYNC + " FROM " + CONTACT_TABLE_NAME + " WHERE "
        + CONTACT_KEY_SYNC + " =1";
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        // Adding contact to list
        ContactRow contactRow = new ContactRow();
        contactRow.setName(cursor.getString(0));
        contactRow.setUuid(cursor.getString(1));
        contactRow.setSync(cursor.getInt(2) == 1);
        contactList.add(contactRow);
      } while (cursor.moveToNext());
      cursor.close();
    }
    db.close();
    return contactList;

  }

  // Getting All Contacts
  public List<ContactRow> getAllContacts() {
    List<ContactRow> contactList = new ArrayList<ContactRow>();
    String selectQuery = "SELECT * FROM " + CONTACT_TABLE_NAME;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        // Adding contact to list
        contactList.add(new ContactRow(cursor.getString(8),
            cursor.getString(1), cursor.getInt(2) > 0,
            ContactRow.getGroupsIdFromString(cursor.getString(7)),
            cursor.getInt(0), cursor.getString(3), cursor.getString(4),
            cursor.getString(5), cursor.getString(6)));
      } while (cursor.moveToNext());
      cursor.close();
    }
    db.close();
    return contactList;
  }

  /**
   * Getting all ContactRow.
   *
   * @return map of contactRow.
   */
  public Map<String, ContactRow> getAllContactsMap() {
    Map<String, ContactRow> contactList = new HashMap<String, ContactRow>();
    String selectQuery = "SELECT * FROM " + CONTACT_TABLE_NAME;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        // Adding contact to list
        contactList.put(
            cursor.getString(6),
            new ContactRow(cursor.getString(8), cursor.getString(1),
                cursor.getInt(2) > 0, ContactRow.getGroupsIdFromString(cursor.getString(7)),
                cursor.getInt(0), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6)));
      } while (cursor.moveToNext());
      cursor.close();
    }
    db.close();
    return contactList;
  }

  // Getting sync contact id
  public List<String> getSyncContactsId() {
    List<String> contactsId = new ArrayList<String>();
    String selectQuery = "SELECT " + CONTACT_KEY_ID_CONTACT + " FROM "
        + CONTACT_TABLE_NAME + " WHERE " + CONTACT_KEY_SYNC + " =1";
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        // Adding contact to list
        contactsId.add(cursor.getString(0));
      } while (cursor.moveToNext());
      cursor.close();
    }
    db.close();
    return contactsId;
  }

  // Getting sync contact id
  public String getContactId(String uuid) {
    Log.i(TAG, " getContactId");
    String contactsId = null;
    String selectQuery = "SELECT " + CONTACT_KEY_ID_CONTACT + " FROM "
        + CONTACT_TABLE_NAME + " WHERE " + CONTACT_KEY_UUID + " ='" + uuid
        + "'";
    Log.i(TAG, selectQuery);
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      contactsId = cursor.getString(0);
      cursor.close();
    }
    db.close();
    return contactsId;
  }

  /**
   * Getting sync contact id.
   *
   * @return list of ContactRow
   */
  public List<ContactRow> getSyncContacts() {
    List<ContactRow> contacts = new ArrayList<ContactRow>();
    Cursor cursor = null;
    SQLiteDatabase db = null;
    try {
      String selectQuery = "SELECT " + CONTACT_KEY_ID_CONTACT + ","
          + CONTACT_KEY_UUID + " FROM " + CONTACT_TABLE_NAME + " WHERE "
          + CONTACT_KEY_SYNC + " =1";
      db = this.getReadableDatabase();
      cursor = db.rawQuery(selectQuery, null);
      if (cursor.moveToFirst()) {
        do {
          // Adding contact to list
          ContactRow con = new ContactRow();
          con.setId(cursor.getString(0));
          con.setUuid(cursor.getString(1));
          contacts.add(con);
          Log.i(TAG, "get Sync contac: " + con.toString());
        } while (cursor.moveToNext());
        cursor.close();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (cursor != null && !cursor.isClosed()) {
          cursor.close();
        }
        if (db.isOpen()) {
          db.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return contacts;
  }

  /**
   * Updating synchronization in list of ContactRow.
   *
   * @param contacts
   *          List of contact for updating
   */
  public void updateContactsUuid(List<ContactRow> contacts) {

    String sql = "UPDATE " + CONTACT_TABLE_NAME + " SET " + CONTACT_KEY_UUID
        + " =? WHERE " + CONTACT_KEY_ID_CONTACT + "=?";
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      db.beginTransaction();
      SQLiteStatement stmt = db.compileStatement(sql);
      for (ContactRow contactRow : contacts) {
        stmt.bindString(1, contactRow.getUuid());
        stmt.bindLong(2, Long.valueOf(contactRow.getId()));
        stmt.executeUpdateDelete();
        stmt.clearBindings();
      }
      db.setTransactionSuccessful();
      Log.i(TAG, "Contact update: " + contacts.size());
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        contactManager.setContactsLocalReload(false);
        db.endTransaction();
        if (db.isOpen()) {
          db.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Updating synchronization in list of ContactRow.
   *
   * @param contacts
   *          List of contact for updating
   * @param sync
   *          true or false.
   */
  public void updateContactsSync(List<ContactRow> contacts, boolean sync) {

    String sql = "UPDATE " + CONTACT_TABLE_NAME + " SET " + CONTACT_KEY_SYNC
        + " =? WHERE " + CONTACT_KEY_ID_CONTACT + "=?";
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      db.beginTransaction();
      SQLiteStatement stmt = db.compileStatement(sql);
      for (ContactRow contactRow : contacts) {
        stmt.bindLong(1, sync ? 1 : 0);
        stmt.bindLong(2, Long.valueOf(contactRow.getId()));
        stmt.executeUpdateDelete();
        stmt.clearBindings();
      }
      db.setTransactionSuccessful();
      Log.i(TAG, "Contact update: " + contacts.size() + ", set "
          + CONTACT_KEY_SYNC + ":" + sync);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        db.endTransaction();
        if (db.isOpen()) {
          db.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Updating synchronization in list of ContactRow.
   *
   * @param listId
   *          List of contact for updating
   * @param sync
   *          true or false.
   */
  public void updateContactsSync(Set<String> listId, boolean sync) {

    String sql = "UPDATE " + CONTACT_TABLE_NAME + " SET " + CONTACT_KEY_SYNC
        + " =? WHERE " + CONTACT_KEY_ID_CONTACT + "=?";
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      db.beginTransaction();
      SQLiteStatement stmt = db.compileStatement(sql);
      for (String id : listId) {
        stmt.bindLong(1, sync ? 1 : 0);
        stmt.bindLong(2, Long.valueOf(id));
        stmt.executeUpdateDelete();
        stmt.clearBindings();
      }
      db.setTransactionSuccessful();
      Log.i(TAG, "Contact update: " + listId.size() + ", set "
          + CONTACT_KEY_SYNC + ":" + sync);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        db.endTransaction();
        if (db.isOpen()) {
          db.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public void updateContactIdGroup(List<ContactRow> contactRows) {
    String sql = "UPDATE " + CONTACT_TABLE_NAME + " SET " + CONTACT_KEY_GROUPS_ID
        + " =? WHERE " + CONTACT_KEY_UUID + "=?";
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      db.beginTransaction();
      SQLiteStatement stmt = db.compileStatement(sql);
      for (ContactRow contactRow : contactRows) {
        stmt.bindString(1, contactRow.getGroupsIdToString());
        stmt.bindString(2, contactRow.getId());
        stmt.executeUpdateDelete();
        stmt.clearBindings();
      }
      db.setTransactionSuccessful();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        db.endTransaction();
        Log.i(TAG, "updateContactIdGroup");
        if (db.isOpen()) {
          db.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Update column SYNC and TIMESTAMP for all entries in map googleContacts.
   *
   * @param googleContacts
   * @param timestamp
   */
  public void updateContacts(Map<String, GoogleContact> googleContacts,
      String timestamp) {

    String sql = "UPDATE " + CONTACT_TABLE_NAME + " SET " + CONTACT_KEY_SYNC
        + " =?, " + CONTACT_KEY_TIMESTAMP + " =? WHERE " + CONTACT_KEY_UUID
        + "=?";
    SQLiteDatabase db = this.getWritableDatabase();
    try {
      db.beginTransaction();
      SQLiteStatement stmt = db.compileStatement(sql);
      for (String entry : googleContacts.keySet()) {
        stmt.bindLong(1, 0);
        stmt.bindString(2, timestamp);
        stmt.bindString(3, entry);
        stmt.executeUpdateDelete();
        stmt.clearBindings();
      }
      db.setTransactionSuccessful();
      Log.i(TAG, "Contact update: " + googleContacts.size() + ", set "
          + CONTACT_KEY_SYNC + ":" + false + ", and " + CONTACT_KEY_TIMESTAMP
          + ":" + timestamp);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        db.endTransaction();
        if (db.isOpen()) {
          db.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  // Deleting single contact
  public void deleteContact(ContactRow contact) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(CONTACT_TABLE_NAME, CONTACT_KEY_ID + " = ?",
        new String[] { String.valueOf(contact.getIdTable()) });
    db.close();
  }

  /**
   * Insert data to database.
   *
   * @param contacts
   */
  public void fillContacts(List<ContactRow> contacts) {
    List<ContactRow> conTable = getAllContacts();
    List<ContactRow> add = new ArrayList<ContactRow>();
    String timestamp = ContactRow.createTimestamp();
    for (ContactRow con : contacts) {
      boolean found = false;
      for (ContactRow conT : conTable) {
        if (con.getId().equals(conT.getId())) {
          con.setSync(conT.isSync());
          con.setIdTable(conT.getIdTable());
          con.setAccouNamePrevious(conT.getAccouNamePrevious());
          con.setAccouTypePrevious(conT.getAccouTypePrevious());
          con.setTimestamp(conT.getTimestamp());
          found = true;
          break;
        }
      }
      if (!found) {
        con.setTimestamp(timestamp);
        // con.setIdTable();
        add.add(con);
      }
    }
    addContacts(add);
    new AndroidDB().importContactsToSyncAccount(context, add);
  }

  /**
   * Newer timestamp of last synchronization. Get from database.
   *
   * @return last timestamp.
   */
  public String newerTimestamp() {
    String str = null;
    String selectQuery = "SELECT MAX(" + CONTACT_KEY_TIMESTAMP + ") FROM "
        + CONTACT_TABLE_NAME;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToNext()) {
      str = cursor.getString(0);
      cursor.close();
    }
    db.close();
    return str;
  }
}
