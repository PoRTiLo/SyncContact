package cz.xsendl00.synccontact.database;

import java.util.ArrayList;
import java.util.Map;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.Mapping;

/**
 * Class for working with Android database like update contact, get contact ...
 * 
 * @author portilo
 * 
 */
public class AndroidDB {

  private static final String TAG = "AndroidDB";
  
  /**
   * Update Contacts
   * 
   * @param context
   * @param differenceLDAP List of contact their attributes should be updated, added or removed from database.
   * @return
   * @throws OperationApplicationException
   * @throws RemoteException
   */
  public static boolean updateContactsDb(Context context,
      Map<String, GoogleContact> differenceLDAP) throws RemoteException,
      OperationApplicationException {
    
    ArrayList<ContentProviderOperation> op = new ArrayList<ContentProviderOperation>();

    for (Map.Entry<String, GoogleContact> entry : differenceLDAP.entrySet()) {
      Log.i(TAG, "zpracovava se: " + entry.getValue().getStructuredName().getDisplayName());
      HelperSQL s = new HelperSQL(context);
      Log.i(TAG, "create sql heleper ");
      String id = s.getContactId(entry.getKey());
      Log.i(TAG, "uuid:: " + entry.getKey() + " mapping to:" + id);
      GoogleContact gc = Mapping.mappingContactFromDB(context.getContentResolver(), id);
      Log.i(TAG, "z db se vzal: " + entry.getValue().getStructuredName().getDisplayName());
      op.addAll(GoogleContact.createOperationUpdate(gc, entry.getValue()));
    }
    for (ContentProviderOperation o : op) {
      Log.i(TAG, o.toString());
    }
    //Log.i(TAG, String.valueOf(op.size()));
    //ContentProviderResult[] contactUri = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);
    //for (ContentProviderResult a : contactUri) {
    //  Log.i(TAG, "res:" + a);
   // }

    return true;
  }
  
  public static void importContactToSyncAccount(Context context, Integer id) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    Log.i(TAG, id.toString());
    ops.add(ContentProviderOperation.newUpdate(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, id))
       .withValue(RawContacts.ACCOUNT_NAME, Constants.ACCOUNT_NAME)
       .withValue(RawContacts.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE)
       .build()
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
  
  public static void exportContactFromSyncAccount(Context context, Integer id, String accountName, String accountType) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    Log.i(TAG, id.toString());
    ops.add(ContentProviderOperation.newUpdate(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, id))
       .withValue(RawContacts.ACCOUNT_NAME, accountName)
       .withValue(RawContacts.ACCOUNT_TYPE, accountType)
       .build()
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
  
  
}