package cz.xsendl00.synccontact.database;

import java.util.ArrayList;
import java.util.Map;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.utils.Mapping;

/**
 * Class for working with Android database like update contact, get contact ...
 * 
 * @author portilo
 * 
 */
public class AndroidDB {

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
      GoogleContact gc = Mapping.mappingContactFromDB(context.getContentResolver(), entry.getKey());
      op.addAll(GoogleContact.createOperationUpdate(gc, entry.getValue()));
    }

    ContentProviderResult[] contactUri = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, op);

    return true;
  }
  
}
