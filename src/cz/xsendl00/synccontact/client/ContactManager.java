package cz.xsendl00.synccontact.client;

import cz.xsendl00.synccontact.utils.Constants;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.Settings;
import android.util.Log;

public class ContactManager {
  private static final String TAG = "ContactManager";
  public static void makeGroupVisible(String accountName, ContentResolver resolver) {
    try {
      ContentProviderClient client = resolver.acquireContentProviderClient(ContactsContract.AUTHORITY_URI);
      ContentValues cv = new ContentValues();
      cv.put(Groups.ACCOUNT_NAME, accountName);
      cv.put(Groups.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
      cv.put(Settings.UNGROUPED_VISIBLE, true);
      client.insert(Settings.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build(), cv);
    } catch (RemoteException e) {
      Log.d(TAG, "Cannot make the Group Visible");
    }
  }
}
