package cz.xsendl00.synccontact.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.Mapping;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class HelperSQL extends SQLiteOpenHelper {

  private static final String TAG = "HelperSQL";
  
  private static final String DATABASE_NAME = "SyncContact";
  private static final int DATABASE_VERSION = 1;
  private static final String GROUP_TABLE_NAME = "groupGoogle";
  // Contacts Table Columns names
  private static final String GROUP_KEY_ID = "id";
  private static final String GROUP_KEY_ID_GROUP = "group_id";
  private static final String GROUP_KEY_GROUP = "groupName";
  private static final String GROUP_KEY_SIZE = "groupSize";
  private static final String GROUP_KEY_SYNC = "sync";
  private static final String CREATE_GROUP_TABLE = "CREATE TABLE IF NOT EXISTS " + GROUP_TABLE_NAME + 
      " (" + GROUP_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      GROUP_KEY_GROUP + " TEXT, " +
      GROUP_KEY_SIZE + " INTEGER, " +
      GROUP_KEY_SYNC + " INTEGER," +
      GROUP_KEY_ID_GROUP + " TEXT " +");";
  
  private static final String CONTACT_TABLE_NAME = "contactGoogle";
  
  private static final String CONTACT_KEY_ID = "id";
  private static final String CONTACT_KEY_ID_CONTACT = "contact_id";
  private static final String CONTACT_KEY_NAME = "contactName";
  private static final String CONTACT_KEY_SYNC = "sync";
  private static final String CONTACT_KEY_ACCOUNT_NAME = "account_name_previous";
  private static final String CONTACT_KEY_ACCOUNT_TYPE = "account_type_previous";
  private static final String CONTACT_KEY_TIMESTAMP = "timestamp";
  private static final String CONTACT_KEY_UUID = "uuid";
  
  private static final String CREATE_CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS " + CONTACT_TABLE_NAME + 
      " (" + CONTACT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      CONTACT_KEY_NAME + " TEXT, " +
      CONTACT_KEY_SYNC + " INTEGER, " +
      CONTACT_KEY_ACCOUNT_NAME + " TEXT, " +
      CONTACT_KEY_ACCOUNT_TYPE + " TEXT, " +
      CONTACT_KEY_TIMESTAMP + " TEXT, " +
      CONTACT_KEY_UUID + " TEXT, " +
      CONTACT_KEY_ID_CONTACT + " TEXT " +");";
  
  private Context context;
  
  public HelperSQL(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
      this.context = context;
  }

  /* Create table*/
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_GROUP_TABLE);
    db.execSQL(CREATE_CONTACT_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE_NAME);
    // Create tables again
    onCreate(db);
  }
  
  //Adding new group
  public void addGroup(GroupRow group) {
    SQLiteDatabase db = this.getWritableDatabase();
    
    ContentValues values = new ContentValues();
    values.put(GROUP_KEY_GROUP, group.getName());
    values.put(GROUP_KEY_SYNC, group.isSync());
    values.put(GROUP_KEY_ID_GROUP, group.getId());
    values.put(GROUP_KEY_SIZE, group.getSize());
 
    // Inserting Row
    db.insert(GROUP_TABLE_NAME, null, values);
    db.close(); // Closing database connection
  }

  //Getting single group
  public GroupRow getGroup(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
    
    Cursor cursor = db.query(GROUP_TABLE_NAME, new String[] { GROUP_KEY_ID,
        GROUP_KEY_GROUP, GROUP_KEY_SIZE, GROUP_KEY_SYNC, GROUP_KEY_ID_GROUP }, GROUP_KEY_ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null);
    if (cursor != null) {
        cursor.moveToFirst();
    }
    return new GroupRow(cursor.getString(1), cursor.getString(4), cursor.getInt(2), cursor.getInt(3)>0, cursor.getInt(0));
  }

  //Getting All Groups
  public List<GroupRow> getAllGroups() {
    List<GroupRow> groupList = new ArrayList<GroupRow>();
    String selectQuery = "SELECT  * FROM " + GROUP_TABLE_NAME;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        GroupRow group = new GroupRow();
        group.setIdTable(cursor.getInt(0));
        group.setName(cursor.getString(1));
        group.setSize(cursor.getInt(2));
        group.setSync(cursor.getInt(3)>0);
        group.setId(cursor.getString(4));
        // Adding group to list
        groupList.add(group);
      } while (cursor.moveToNext());
    }
    db.close();
    return groupList;
  }

  // Getting groups Count
  public int getGroupCount() {
    String countQuery = "SELECT  * FROM " + GROUP_TABLE_NAME;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    cursor.close();

    db.close();
    return cursor.getCount();
  }

  // Updating single group
  public int updateGroup(GroupRow group) {
    SQLiteDatabase db = this.getWritableDatabase();
    
    ContentValues values = new ContentValues();
    values.put(GROUP_KEY_GROUP, group.getName());
    values.put(GROUP_KEY_SYNC, group.isSync());
    values.put(GROUP_KEY_ID_GROUP, group.getId());
    values.put(GROUP_KEY_SIZE, group.getSize());
    //Log.i(TAG, "update:" + group.toString());
    int res = db.update(GROUP_TABLE_NAME, values, GROUP_KEY_ID + " = ?", new String[] { String.valueOf(group.getIdTable()) });
    db.close();
    return res;
  }
/*
  public static ContentProviderOperation updateRawContactStatus() {
    ArrayList<ContentProviderOperation> ops = 
        new ArrayList<ContentProviderOperation>();
     ops.add(
    ContentProviderOperation.newUpdate(ContentUris.withAppendedId(rawContactUri, rawContactId))
          .withValue(RawContacts.ACCOUNT_NAME, status)
          .withValue(RawContacts.ACCOUNT_TYPE, status).build()
          );
     
     try {
       getContentResolver().
          applyBatch(ContactsContract.AUTHORITY, ops);
    } catch (RemoteException e) {
       // do s.th.
    } catch (OperationApplicationException e) {
       // do s.th.
    }
    
  } 
  */
  // Deleting single group
  public void deleteGroup(GroupRow group) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(GROUP_TABLE_NAME, GROUP_KEY_ID + " = ?", new String[] { String.valueOf(group.getIdTable()) });
    db.close();
  }
  
  // Insert data from db 
  public void fillGroups(ArrayList<GroupRow> groups) {
    List<GroupRow> grTable = getAllGroups();
    for (GroupRow gr : groups) {
      boolean found = false;
      for (GroupRow grT : grTable) {
        //Log.i(TAG, gr.getId() +":"+ grT.getId());
        if (gr.getId().equals(grT.getId())) {
          gr.setSync(grT.isSync());
          gr.setIdTable(grT.getIdTable());
          found = true;
          //Log.i(TAG, "before: " + gr.toString() + ", after:" + grT.toString());
          break;
        }
      }
      if ( !found ) {
       //Log.i(TAG, "add to db: " + gr.toString());
        addGroup(gr);
      }
    }
  }
  
  // Adding new contact
  public int addContact(ContactRow contact) {
    SQLiteDatabase db = this.getWritableDatabase();
    
    ContentValues values = new ContentValues();
    values.put(CONTACT_KEY_NAME, contact.getName());
    values.put(CONTACT_KEY_SYNC, contact.isSync());
    values.put(CONTACT_KEY_ID_CONTACT, contact.getId());
    values.put(CONTACT_KEY_ACCOUNT_NAME, contact.getAccouNamePrevious());
    values.put(CONTACT_KEY_ACCOUNT_TYPE, contact.getAccouTypePrevious());
    values.put(CONTACT_KEY_TIMESTAMP, contact.getTimestamp());
    values.put(CONTACT_KEY_UUID, contact.getUuid());
 
    // Inserting Row
    int id = (int)db.insert(CONTACT_TABLE_NAME, null, values);
    db.close(); // Closing database connection
    return id; 
  }
  
  // Getting single contact
  public ContactRow getContact(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
    
    Cursor cursor = db.query(CONTACT_TABLE_NAME, new String[] { CONTACT_KEY_ID, CONTACT_KEY_NAME, CONTACT_KEY_SYNC, CONTACT_KEY_ID_CONTACT, 
        CONTACT_KEY_ACCOUNT_NAME, CONTACT_KEY_ACCOUNT_TYPE, CONTACT_KEY_TIMESTAMP , CONTACT_KEY_UUID}, CONTACT_KEY_ID + "=?",
            new String[] { String.valueOf(id) }, null, null, null, null);
    if (cursor != null) {
        cursor.moveToFirst();
    }
    return new ContactRow(cursor.getString(7), cursor.getString(1), cursor.getInt(2)>0, cursor.getInt(0), cursor.getString(3), 
        cursor.getString(4), cursor.getString(5), cursor.getString(6));
  }
  
  // Getting All Contacts
  public List<ContactRow> getAllContacts() {
    List<ContactRow> contactList = new ArrayList<ContactRow>();
    String selectQuery = "SELECT  * FROM " + CONTACT_TABLE_NAME;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        // Adding contact to list
        contactList.add(new ContactRow(cursor.getString(7), cursor.getString(1), cursor.getInt(2)>0, cursor.getInt(0), cursor.getString(3), 
            cursor.getString(4), cursor.getString(5), cursor.getString(5)));
      } while (cursor.moveToNext());
    }
    db.close();
    return contactList;
  }
  
  // Getting sync contact id 
  public List<String> getSyncContactsId() {
    List<String> contactsId = new ArrayList<String>();
    String selectQuery = "SELECT " + CONTACT_KEY_ID_CONTACT + " FROM " + CONTACT_TABLE_NAME + " WHERE " + CONTACT_KEY_SYNC + " =1";
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        // Adding contact to list
        contactsId.add(cursor.getString(0));
      } while (cursor.moveToNext());
    }
    db.close();
    return contactsId;
  }
  
  // Getting sync contact id 
  public List<ContactRow> getSyncContacts() {
    List<ContactRow> contacts = new ArrayList<ContactRow>();
    String selectQuery = "SELECT " + CONTACT_KEY_ID_CONTACT +","+ CONTACT_KEY_UUID + " FROM " + CONTACT_TABLE_NAME + " WHERE " + CONTACT_KEY_SYNC + " =1";
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        // Adding contact to list
        ContactRow con = new ContactRow();
        con.setId(cursor.getString(0));
        con.setUuid(cursor.getString(1));
        contacts.add(con);
        Log.i(TAG, "get Sync contac: "+con.toString());
      } while (cursor.moveToNext());
    }
    db.close();
    return contacts;
  }
  
  public void updateContactDb(Map<String, GoogleContact> map) {
    for (Map.Entry<String, GoogleContact> entry : map.entrySet()) {
      String selectQuery = "SELECT " + CONTACT_KEY_ID_CONTACT + " FROM " + CONTACT_TABLE_NAME + " WHERE " + CONTACT_KEY_UUID + " ="+entry.getKey();
      SQLiteDatabase db = this.getWritableDatabase();
      Cursor cursor = db.rawQuery(selectQuery, null);
      if (cursor.moveToFirst()) {
        // get contact from db and all data
        GoogleContact contact = Mapping.mappingContactFromDB(context.getContentResolver(), cursor.getString(0));
        // compare
        ContentValues values = new ContentValues();
        values.putAll(GoogleContact.compare(contact, entry.getValue()));
      }
      cursor.close();
    }
  }
  

  
  // Getting contacts Count
  public int getContactCount() {
    String countQuery = "SELECT  * FROM " + CONTACT_TABLE_NAME;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    cursor.close();

    return cursor.getCount();
  }
  
  // Updating single contact
  public int updateContact(ContactRow contact) {
    SQLiteDatabase db = this.getWritableDatabase();
    
    ContentValues values = new ContentValues();
    values.put(CONTACT_KEY_NAME, contact.getName());
    values.put(CONTACT_KEY_SYNC, contact.isSync());
    values.put(CONTACT_KEY_ID_CONTACT, contact.getId());
    values.put(CONTACT_KEY_TIMESTAMP, contact.getTimestamp());
    //values.put(CONTACT_KEY_ACCOUNT, contact.getIdAccountPrevious());
    //Log.i(TAG, "update:" + contact.toString());
    int res = db.update(CONTACT_TABLE_NAME, values, CONTACT_KEY_ID + " = ?", new String[] { String.valueOf(contact.getIdTable()) });
    db.close();
    return res;
  }
  
  // Updating single contact
  public int updateContactSync(ContactRow contact) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(CONTACT_KEY_SYNC, contact.isSync());
    int res = db.update(CONTACT_TABLE_NAME, values, CONTACT_KEY_ID + " = ?", new String[] { String.valueOf(contact.getIdTable()) });
    db.close();
    return res;
  }
  
  // Deleting single contact
  public void deleteContact(ContactRow contact) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(CONTACT_TABLE_NAME, CONTACT_KEY_ID + " = ?", new String[] { String.valueOf(contact.getIdTable()) });
    db.close();
  }
  
  // Insert data from db 
  public void fillContacts(ArrayList<ContactRow> contacts) {
    List<ContactRow> conTable = getAllContacts();
    String timestamp = ContactRow.createTimestamp();
    Log.i(TAG, timestamp);
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
        //Log.i(TAG, con.toString());
        con.setTimestamp(timestamp);
        con.setIdTable(addContact(con));
        importContactToSyncAccount(Integer.parseInt(con.getId()));
      }
    }
  }
  
  private void importContactToSyncAccount(Integer id) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    Log.i(TAG, id.toString());
    ops.add(ContentProviderOperation.newUpdate(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, id))
       .withValue(RawContacts.ACCOUNT_NAME, Constants.ACCOUNT_NAME)
       .withValue(RawContacts.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE).build()
     );
     
    try {
      ContentProviderResult[] con = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
      for (ContentProviderResult cn : con) {
        Log.i(TAG, cn.toString());
      }
    } catch (RemoteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String newerTimestamp() {
    String str = null;
    String selectQuery = "SELECT MAX(" + CONTACT_KEY_TIMESTAMP + ") FROM " + CONTACT_TABLE_NAME;
    //Log.i(TAG, selectQuery);
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToNext()) {
      str = cursor.getString(0);
      //Log.i(TAG, "max TIMESTAMP" + cursor.getString(0));
    }
    return str;
  }
}
