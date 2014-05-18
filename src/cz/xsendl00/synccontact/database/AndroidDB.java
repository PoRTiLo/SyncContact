package cz.xsendl00.synccontact.database;

import java.util.ArrayList;
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
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.Mapping;

/**
 * Class for working with Android database like update contact, get contact ...
 *
 * @author portilo
 */
@EBean
public class AndroidDB {

  @Bean
  Mapping mapping;

  private static final String TAG = "AndroidDB";
  private static final int MAX_OPERATIONS_IYELD = 500;

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
  public boolean updateContactsDb(final Context context, final Map<String, GoogleContact> differenceLDAP)
      throws RemoteException, OperationApplicationException {

    ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();

    // for every contact do:
    for (Map.Entry<String, GoogleContact> entry : differenceLDAP.entrySet()) {
      //Log.i(TAG, "zpracovava se: " + entry.getValue().getStructuredName().getDisplayName());
      HelperSQL helperSQL = new HelperSQL(context);
      // get raw_id from local database
      String id = helperSQL.getContactId(entry.getKey());

      Log.i(TAG, "uuid:: " + entry.getKey() + " mapping to:" + id);
      GoogleContact googleContact = mapping.mappingContactFromDB(context.getContentResolver(), id,
          entry.getKey());
      Log.i(TAG, "z db se vzal: " + entry.getValue().getStructuredName().getDisplayName());
      op.addAll(GoogleContact.createOperationUpdate(googleContact, entry.getValue()));
    }
    for (ContentProviderOperation o : op) {
      Log.i(TAG, o.toString());
    }
    // Log.i(TAG, String.valueOf(op.size()));
    // ContentProviderResult[] contactUri =
    // context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
    // for (ContentProviderResult a : contactUri) {
    // Log.i(TAG, "res:" + a);
    // }

    return true;
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
      if (count < MAX_OPERATIONS_IYELD) {
        ops.add(ContentProviderOperation.newUpdate(
            ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                Integer.valueOf(contactRow.getId())))
            .withValue(RawContacts.ACCOUNT_NAME, Constants.ACCOUNT_NAME)
            .withValue(RawContacts.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE)
            .build());
        count = 0;
      } else {
        ops.add(ContentProviderOperation.newUpdate(
            ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                Integer.valueOf(contactRow.getId())))
            .withValue(RawContacts.ACCOUNT_NAME, Constants.ACCOUNT_NAME)
            .withValue(RawContacts.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE)
            .withYieldAllowed(true)
            .build());
      }
      count++;
    }
    try {
      ContentProviderResult[] con = context.getContentResolver().applyBatch(
          ContactsContract.AUTHORITY, ops);
      Log.i(TAG, "importContactsToSyncAccount done: " + con.length);
      // for (ContentProviderResult cn : con) {
      // Log.i(TAG, cn.toString());
      // }
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
  public void exportContactsFromSyncAccount(final Context context, List<ContactRow> contactRows) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    int size = contactRows.size();
    int i = 1;
    int count = 0;
    for (ContactRow contactRow : contactRows) {
      if (count < MAX_OPERATIONS_IYELD) {
        ops.add(ContentProviderOperation.newUpdate(
            ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                Integer.valueOf(contactRow.getId())))
            .withValue(RawContacts.ACCOUNT_NAME, contactRow.getAccouNamePrevious())
            .withValue(RawContacts.ACCOUNT_TYPE, contactRow.getAccouTypePrevious())
            .build());
      } else {
        ops.add(ContentProviderOperation.newUpdate(
            ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                Integer.valueOf(contactRow.getId())))
            .withValue(RawContacts.ACCOUNT_NAME, Constants.ACCOUNT_NAME)
            .withValue(RawContacts.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE)
            .withYieldAllowed(true)
            .build());
      }
      count++;
      Log.i(TAG, contactRow.getId() + ", exportContactsFromSyncAccount: " + i++ + "/" + size
          + ", to:" + contactRow.getAccouNamePrevious());
    }
    try {
      context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
      new Thread(new Runnable() {

        @Override
        public void run() {
          new HelperSQL(context).drop();
        }
      }).start();
      Log.i(TAG, "Contacts exported");
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
  }


  /**
   * get contact Id by raw id.
   *
   * @param context context
   * @param idRaw idRaw
   * @return id
   */
  public int getIdContact(final Context context, String idRaw) {
    Cursor cursor = null;
    int id = 0;
    try {
      cursor = context.getContentResolver().query(RawContacts.CONTENT_URI,
          new String[]{RawContacts.CONTACT_ID}, RawContacts._ID + "=?", new String[]{idRaw}, null);
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
   *
   * @param contentResolver
   * @param idRaw
   * @return
   */
  public String fetchContactName(ContentResolver contentResolver, String idRaw) {
    String name = null;
    Cursor cursor = null;
    try {
      cursor = contentResolver.query(RawContacts.CONTENT_URI,
          new String[]{RawContacts.DISPLAY_NAME_PRIMARY}, RawContacts._ID + "=?", new String[]{idRaw}, null);
      while (cursor.moveToNext()) {
        name = cursor.getString(cursor.getColumnIndex(RawContacts.DISPLAY_NAME_PRIMARY));
        break;
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
    return name;
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

}
