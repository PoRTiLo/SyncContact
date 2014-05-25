package cz.xsendl00.synccontact.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.Mapping;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Class for working with Android database like update contact, get contact ...
 *
 * @author portilo
 */
@EBean
public class AndroidDB {

  @Bean
  protected Mapping mapping;

  public static final String MIMETYPE = "vnd.android.cursor.item/cz.synccontact";
  public static final String SET_CONVERT = "is_converted";
  private static final String TAG = "AndroidDB";
  private static final int MAX_OPERATIONS_IYELD = 480;


  public void removeContactFromGroup(final Context context, final List<Integer> contacts, final Integer groupId) {
    for (Integer rawId : contacts) {
      String[] projection = new String[]{Data._ID};
      String where = Data.RAW_CONTACT_ID + "=" + rawId + " AND "
          + Data.MIMETYPE + "='" + CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "' AND "
          + Data.DATA1 + "=" + groupId;

      Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI, projection, where, null, null);
      String idData = null;
      while (cursor.moveToNext()) {
        idData = cursor.getString(cursor.getColumnIndex(Data._ID));
        break;
      }
      try {
        Uri uri = Uri.withAppendedPath(Data.CONTENT_URI, idData);
        int deleted = context.getContentResolver().delete(uri, null, null);
        Log.i(TAG, "deleted rows:" + deleted);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


  public void addContact2Group(final Context context, final List<ContactRow> contacts, Integer groupId) {
    ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
    for (ContactRow contactRow : contacts) {
      op.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValue(Data.RAW_CONTACT_ID, contactRow.getId())
          .withValue(Data.MIMETYPE, CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
          .withValue(Data.DATA1, groupId)
          .build());
      if (op.size() > MAX_OPERATIONS_IYELD) {
        try {
          ContentProviderResult[] contactUri = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
          for (ContentProviderResult a : contactUri) {
            Log.i(TAG, "res:" + a.toString());
          }
        } catch (RemoteException e) {
          e.printStackTrace();
        } catch (OperationApplicationException e) {
          e.printStackTrace();
        }
        op.clear();
      }
    }
    try {
      ContentProviderResult[] contactUri = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
      for (ContentProviderResult a : contactUri) {
        Log.i(TAG, "res:" + a.toString());
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
  }

  public int addGroup(final Context context, final String name, final String notes) {
    Integer resultId = null;
    ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
    op.add(ContentProviderOperation.newInsert(Groups.CONTENT_URI)
        .withValue(Groups.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE)
        .withValue(Groups.ACCOUNT_NAME, Constants.ACCOUNT_NAME)
        .withValue(Groups.TITLE, name)
        .withValue(Groups.NOTES, notes)
        .withValue(Groups.SYNC1, "1")
        //.withValue(RawContacts.SYNC2, new Utils().createTimestamp())
        .withValue(RawContacts.SYNC3, AndroidDB.SET_CONVERT)
        .withValue(Groups.SYNC4, ContactRow.generateUUID())
        .build());

    try {
      ContentProviderResult[] contactUri = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
      for (ContentProviderResult a : contactUri) {
        Log.i(TAG, "res:" + a.uri.getLastPathSegment());
        if (a.uri.getLastPathSegment() != null) {
          try {
            resultId = Integer.parseInt(a.uri.getLastPathSegment());

          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
    return resultId;
  }

  /**
   * Clear dirty bit in raw_contact.
   *
   * @param context context
   * @param contactsDirty contactsDirty list of contact for set dirty bit to 0
   */
  public void cleanModifyStatusNewTimestamp(final Context context,
      final Map<String, GoogleContact> contactsDirty) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    int count = 0;
    String timestamp = new Utils().createTimestamp();
    for (Map.Entry<String, GoogleContact> entry : contactsDirty.entrySet()) {
      ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(
          ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, entry.getValue().getId()))
          .withValue(RawContacts.DIRTY, 0)
          .withValue(RawContacts.SYNC2, timestamp);
      if (count >= MAX_OPERATIONS_IYELD) {
        operationBuilder.withYieldAllowed(true);
        count = 0;
      }
      ops.add(operationBuilder.build());
      count++;
    }
    try {
//      for ()
//      ops.subList(start, end);
//      if (i % batchsize == 0) {
//        contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
//        ops = new ArrayList<ContentProviderOperation>(100);
//    }
      ContentProviderResult[] con = context.getContentResolver().applyBatch(
          ContactsContract.AUTHORITY, ops);
      Log.i(TAG, " cleanModifyStatus done: " + con.length);
      for (ContentProviderResult cn : con) {
        Log.i(TAG, cn.toString());
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Clear dirty bit in raw_contact.
   *
   * @param context context
   * @param contactsDirty contactsDirty list of contact for set dirty bit to 0
   */
  public void cleanModifyStatusNewTimestamp(final Context context, final List<Integer> contactsDirty) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    int count = 0;
    String timestamp = new Utils().createTimestamp();
    for (Integer id : contactsDirty) {
      ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(
          ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, id))
          .withValue(RawContacts.DIRTY, 0)
          .withValue(RawContacts.SYNC2, timestamp);
      if (count >= MAX_OPERATIONS_IYELD) {
        operationBuilder.withYieldAllowed(true);
        count = 0;
      }
      ops.add(operationBuilder.build());
      count++;
    }
    try {
      ContentProviderResult[] con = context.getContentResolver().applyBatch(
          ContactsContract.AUTHORITY, ops);
      Log.i(TAG, " cleanModifyStatus done: " + con.length);
      for (ContentProviderResult cn : con) {
        Log.i(TAG, cn.toString());
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update Contacts.
   *
   * @param context context
   * @param contacts List of contact their attributes should be updated, added or removed from
   * database.
   * @param handler handler
   * @return true/false
   * @throws OperationApplicationException OperationApplicationException
   * @throws RemoteException RemoteException
   */
  public boolean addContacts2Database(
      final Context context, final Map<String, GoogleContact> contacts, final Handler handler)
      throws RemoteException, OperationApplicationException {

    ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
    // for every contact do:
    List<Integer> addedContactsId = new ArrayList<Integer>();
    for (Map.Entry<String, GoogleContact> entry : contacts.entrySet()) {
      handler.sendMessage(handler.obtainMessage());
      ArrayList<ContentProviderOperation> list =
          GoogleContact.createOperationNew(new GoogleContact(), entry.getValue(), op.size());
      if (list.size() + op.size() < MAX_OPERATIONS_IYELD) {
        op.addAll(list);
      } else {
        addedContactsId.addAll(applyBatch(context, op));
        op.clear();
        op.addAll(list);
      }
    }
    addedContactsId.addAll(applyBatch(context, op));
    if (!addedContactsId.isEmpty()) {
      cleanModifyStatusNewTimestamp(context, addedContactsId);
    }

    return true;
  }


  private List<Integer> applyBatch(final Context context, ArrayList<ContentProviderOperation> op) {
    List<Integer> addedContactsId = new ArrayList<Integer>();
    try {
      ContentProviderResult[] contactUri = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
      for (ContentProviderResult a : contactUri) {
        Log.i(TAG, "res:" + a.uri.getLastPathSegment());
        if (a.uri.getLastPathSegment() != null) {
          try {
            int i = Integer.parseInt(a.uri.getLastPathSegment());
            addedContactsId.add(i);
          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
    return addedContactsId;
  }
  /**
   * Update Contacts.
   *
   * @param context context
   * @param differenceLDAP List of contact their attributes should be updated, added or removed from
   * database.
   * @return true/false
   * @throws OperationApplicationException OperationApplicationException
   * @throws RemoteException RemoteException
   */
  public boolean updateContactsDb(final Context context,
      final Map<String, GoogleContact> differenceLDAP) throws RemoteException,
      OperationApplicationException {

    ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();
    // for every contact do:
    for (Map.Entry<String, GoogleContact> entry : differenceLDAP.entrySet()) {
      Log.i(TAG, "uuid:: " + entry.getKey() + " mapping to:" + entry.getValue().getId());
      GoogleContact googleContact = mapping.mappingContactFromDB(context.getContentResolver(), entry.getValue().getId(),
          entry.getKey());
      Log.i(TAG, "local:" + googleContact.toString());
      Log.i(TAG, "server:" + entry.getValue().toString());
      Log.i(TAG, "z db se vzal: " + entry.getValue().getStructuredName().getDisplayName());
      op.addAll(GoogleContact.createOperationUpdate(googleContact, entry.getValue()));
    }
    try {
      for (ContentProviderOperation o : op) {
        Log.i(TAG, o.toString());
      }
      Log.i(TAG, String.valueOf(op.size()));
      ContentProviderResult[] contactUri = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
      for (ContentProviderResult a : contactUri) {
        Log.i(TAG, "res:" + a);
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
    return true;
  }

  public void updateGroupsUuid(Context context, List<GroupRow> groupRows) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    int count = 0;
    for (GroupRow groupRow : groupRows) {
      ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(
          ContentUris.withAppendedId(Groups.CONTENT_URI, groupRow.getId()))
          .withValue(Groups.SYNC4, groupRow.getUuid());
      if (count >= MAX_OPERATIONS_IYELD) {
        operationBuilder.withYieldAllowed(true);
        count = 0;
      }
      ops.add(operationBuilder.build());
      count++;
    }
    try {
      ContentProviderResult[] con = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
      Log.i(TAG, "updateGroupsUuid done: " + con.length);
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
  }

  public void updateContactsUuid(Context context, List<ContactRow> contactRows) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    int count = 0;
    for (ContactRow contactRow : contactRows) {
      ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(
          ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, contactRow.getId()))
          .withValue(RawContacts.SYNC4, contactRow.getUuid());
      if (count >= MAX_OPERATIONS_IYELD) {
        operationBuilder.withYieldAllowed(true);
        count = 0;
      }
      ops.add(operationBuilder.build());
      count++;
    }
    try {
      ContentProviderResult[] con = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
      Log.i(TAG, "updateContactsUui done: " + con.length);
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
  }

  public void updateContactsSync(final Context context, final List<ContactRow> contactRows, final boolean sync) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    for (ContactRow contactRow : contactRows) {
      ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(
          ContentUris.withAppendedId(RawContacts.CONTENT_URI, contactRow.getId()))
          .withValue(RawContacts.SYNC1, sync);
      ops.add(operationBuilder.build());
    }
    try {
      ContentProviderResult[] con = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
      Log.i(TAG, "updateContactsSync done: " + con.length + ": " + con.toString());
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
  }

  public void updateGroupsSync(final Context context, final List<GroupRow> groupRows, final boolean sync) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    for (GroupRow groupRow : groupRows) {
      ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(
          ContentUris.withAppendedId(ContactsContract.Groups.CONTENT_URI, groupRow.getId()))
          .withValue(Groups.SYNC1, sync);
      ops.add(operationBuilder.build());
    }
    try {
      ContentProviderResult[] con = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
      Log.i(TAG, "updateGroupSync done: " + con.length);
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
  }

  private Uri addIsSyncAdapter(Uri uri, boolean isSyncOperation) {
    if (isSyncOperation) {
      return uri.buildUpon()
          .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
          .build();
    }
    return uri;
  }

  private ArrayList<ContentProviderOperation> createBackendDataAccount(List<ContactRow> contactRows) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    int count = 0;
    for (ContactRow contactRow : contactRows) {
      ContentProviderOperation.Builder operationBuilder  = ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValue(Data.RAW_CONTACT_ID, contactRow.getId())
          .withValue(Data.MIMETYPE, MIMETYPE)
          .withValue(Data.DATA1, Constants.ACCOUNT_NAME.equals(contactRow.getAccouNamePrevious()) ? null : contactRow.getAccouNamePrevious())
          .withValue(Data.DATA2, Constants.ACCOUNT_TYPE.equals(contactRow.getAccouTypePrevious()) ? null : contactRow.getAccouTypePrevious());
      if (count >= MAX_OPERATIONS_IYELD) {
        operationBuilder.withYieldAllowed(true);
        count = 0;
      }
      count++;
      ops.add(operationBuilder.build());
      Log.i(TAG, "DATA:" + (operationBuilder.build()).toString());
    }
    return ops;
  }

  /**
   * Import contact to new SyncContact account.
   *
   * @param context context
   * @param contactRows contactRows
   */
  public void importContactsToSyncAccount(Context context, List<ContactRow> contactRows) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    int count = 0;
    for (ContactRow contactRow : contactRows) {
      if (!Constants.ACCOUNT_NAME.equals(contactRow.getAccouNamePrevious())) {
        ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(
            ContentUris.withAppendedId(RawContacts.CONTENT_URI, contactRow.getId()))
            .withValue(RawContacts.SYNC3, SET_CONVERT)
            .withValue(RawContacts.SYNC4, contactRow.getUuid())
            .withValue(RawContacts.ACCOUNT_NAME, Constants.ACCOUNT_NAME)
            .withValue(RawContacts.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
        if (count >= MAX_OPERATIONS_IYELD) {
          operationBuilder.withYieldAllowed(true);
          count = 0;
        }
        ops.add(operationBuilder.build());
        count++;
        Log.i(TAG, "modify raw_contact:" + operationBuilder.build().toString());
      }
    }
    try {
      ops.addAll(createBackendDataAccount(contactRows));
      ContentProviderResult[] con = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
      Log.i(TAG, "importContactsToSyncAccount done: " + con.length);
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Convert contact to default account from syncContact account.
   *
   * @param context context activity
   * @param contactRows list of contacts for exporting to previous account.
   */
  public void exportContactsFromSyncAccount(final Context context) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    // get all contact with previsou account data
    String where = Data.MIMETYPE + "='" + MIMETYPE + "'";
    String[] projection = new String[]{Data.RAW_CONTACT_ID, Data.DATA1, Data.DATA2};

    Cursor cursor = context.getContentResolver().query(
        ContactsContract.Data.CONTENT_URI, projection, where, null, null);
    List<ContactRow> contactRows = new ArrayList<ContactRow>();
    try {
      while (cursor.moveToNext()) {
        ContactRow contactRow = new ContactRow();
        contactRow.setId(cursor.getInt(cursor.getColumnIndex(Data.RAW_CONTACT_ID)));
        contactRow.setAccouNamePrevious(cursor.getString(cursor.getColumnIndex(Data.DATA1)));
        contactRow.setAccouTypePrevious(cursor.getString(cursor.getColumnIndex(Data.DATA2)));
        contactRows.add(contactRow);
      }
      Log.i(TAG, contactRows.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }

    int size = contactRows.size();
    int i = 1;
    int count = 0;
    for (ContactRow contactRow : contactRows) {
      ContentProviderOperation.Builder operationBuilder  = ContentProviderOperation.newUpdate(
          ContentUris.withAppendedId(RawContacts.CONTENT_URI, contactRow.getId()))
          .withValue(RawContacts.ACCOUNT_NAME, contactRow.getAccouNamePrevious())
          .withValue(RawContacts.ACCOUNT_TYPE, contactRow.getAccouTypePrevious());
      if (count >= MAX_OPERATIONS_IYELD) {
        operationBuilder.withYieldAllowed(true);
        count = 0;
      }
      count++;
      ops.add(operationBuilder.build());
      Log.i(TAG, contactRow.getId() + ", exportContactsFromSyncAccount: " + i++ + "/" + size
          + ", to:" + contactRow.getAccouNamePrevious());
    }
    try {
      context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
      Log.i(TAG, "Contacts exported");
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
    removeRowData(context, contactRows);
  }

  private void removeRowData(final Context context, List<ContactRow> contactRows) {
    try{
      int deleted = context.getContentResolver().delete(Data.CONTENT_URI, Data.MIMETYPE + " = ?", new String[] {MIMETYPE});
      Log.i(TAG, "deleted rows:" + deleted);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Get contact Id by raw id.
   *
   * @param context context
   * @param idRaw idRaw
   * @return id
   */
  public int getIdContact(final Context context, Integer idRaw) {
    Cursor cursor = null;
    int id = 0;
    try {
      cursor = context.getContentResolver().query(RawContacts.CONTENT_URI,
          new String[]{RawContacts.CONTACT_ID}, RawContacts._ID + "=?", new String[]{idRaw.toString()}, null);
      while (cursor.moveToNext()) {
        id = cursor.getInt(cursor.getColumnIndex(RawContacts.CONTACT_ID));
        break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return id;
  }


  /**
   * Get display name from table raw_contact.
   *
   * @param contentResolver contentResolver
   * @param idRaw id contact
   * @return name
   */
  public String fetchContactName(ContentResolver contentResolver, Integer idRaw) {
    String name = null;
    Cursor cursor = null;
    try {
      cursor = contentResolver.query(RawContacts.CONTENT_URI,
          new String[]{RawContacts.DISPLAY_NAME_PRIMARY}, RawContacts._ID + "=?",
          new String[]{idRaw.toString()}, null);
      while (cursor.moveToNext()) {
        name = cursor.getString(cursor.getColumnIndex(RawContacts.DISPLAY_NAME_PRIMARY));
        break;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }
    return name;
  }

  public void deleteContacts(final Context context, List<Integer> contacts) {
    for (Integer id : contacts) {
    int deleted = context.getContentResolver().delete(addIsSyncAdapter(RawContacts.CONTENT_URI, true), RawContacts._ID + " = ?", new String[] { id.toString() });
    Log.i(TAG, "deleted: " + id + ", result:" + deleted);
    }
  }


  /**
   * Get deleted contacts id.
   *
   * @param contentResolver contentResolver
   * @return list of ids deleted contacts
   */
  public List<String> fetchContactsDeleted(ContentResolver contentResolver) {
    List<String> deletedContacts = new ArrayList<String>();
    Cursor cursor = null;
    try {
      cursor = contentResolver.query(RawContacts.CONTENT_URI, new String[]{RawContacts._ID},
          RawContacts.DELETED + "<>0", null, null);
      while (cursor.moveToNext()) {
        deletedContacts.add(cursor.getString(cursor.getColumnIndex(RawContacts._ID)));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }
    return deletedContacts;
  }


  /**
   * Fetch all contact from contact provider database.
   *
   * @param contentResolver resolver
   * @return list of ContactRow.
   */
  public List<ContactRow> fetchAllContact(ContentResolver contentResolver) {
    List<ContactRow> contacts = new ArrayList<ContactRow>();
    Cursor cursor = null;
    try {
      String[] projection = new String[]{RawContacts._ID, RawContacts.DISPLAY_NAME_PRIMARY,
          RawContacts.ACCOUNT_NAME, RawContacts.ACCOUNT_TYPE};
      cursor = contentResolver.query(RawContacts.CONTENT_URI, projection, null, null,
          RawContacts.DISPLAY_NAME_PRIMARY + " COLLATE LOCALIZED ASC");
      while (cursor.moveToNext()) {
        ContactRow contactShow = new ContactRow(
            cursor.getInt(cursor.getColumnIndex(RawContacts._ID)),
            cursor.getString(cursor.getColumnIndex(RawContacts.DISPLAY_NAME_PRIMARY)),
            cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_NAME)),
            cursor.getString(cursor.getColumnIndex(RawContacts.ACCOUNT_TYPE)));
        contacts.add(contactShow);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }
    return contacts;
  }

  /**
   * Newer timestamp of last synchronization. Get from database.
   * @param contentResolver contentResolver
   * @return last timestamp.
   */
  public String newerTimestamp(ContentResolver contentResolver) {
    Cursor cursor = null;
    String time = null;
    try {
      cursor = contentResolver.query(RawContacts.CONTENT_URI, new String[]{RawContacts.SYNC2}, null, null, null);
      List<String> times = new ArrayList<String>();
      while (cursor.moveToNext()) {
        String str = cursor.getString(cursor.getColumnIndex(RawContacts.SYNC2));
        if (str != null) {
          times.add(str);
        }
      }
      if (!times.isEmpty()) {
        Collections.sort(times);
        time = times.get(times.size() - 1);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }
    return time;
  }

  /**
   * Newer timestamp of last synchronization. Get from database.
   * @param contentResolver contentResolver
   * @return last timestamp.
   */
  public String getModifiedTime(ContentResolver contentResolver, Integer id) {
    Cursor cursor = null;
    String time = null;
    try {
      cursor = contentResolver.query(
          ContactsContract.Contacts.CONTENT_URI,
          new String[]{ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP},
          RawContacts._ID + "=" + id, null, null);
      while (cursor.moveToNext()) {
        time = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));
        break;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (cursor != null && !cursor.isClosed()) {
        cursor.close();
      }
    }
    return time;
  }

}
