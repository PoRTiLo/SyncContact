package cz.xsendl00.synccontact.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.Data;



public class ContactDetail {

  private static final String[] PROJECTION = { 
        Data._ID,
        Data.MIMETYPE,
        Data.DATA1,
        Data.DATA2,
        Data.DATA3,
        Data.DATA4,
        Data.DATA5,
        Data.DATA6,
        Data.DATA7,
        Data.DATA8,
        Data.DATA9,
        Data.DATA10,
        Data.DATA11,
        Data.DATA12,
        Data.DATA13,
        Data.DATA14,
        Data.DATA15
    };
  
//Defines the selection clause
  private static final String SELECTION = Data.RAW_CONTACT_ID + " = ?";// Data.LOOKUP_KEY + " = ?";
  // Defines the array to hold the search criteria
  private static String[] mSelectionArgs = { "" };
  /*
   * Defines a variable to contain the selection value. Once you
   * have the Cursor from the Contacts table, and you've selected
   * the desired row, move the row's LOOKUP_KEY value into this
   * variable.
   */
  //private static String mLookupKey = "3184i1b589ff40ef5834c";
  
  /*
   * Defines a string that specifies a sort order of MIME type
   */
  private static final String SORT_ORDER = Data.MIMETYPE;
  
  /**
   * 
   * @param contentResolver
   * @param mLookupKey  Defines a variable to contain the selection value. Once you
   *                    have the Cursor from the Contacts table, and you've selected
   *                    the desired row, move the row's LOOKUP_KEY value into this
   *                    variable.
   * @return
   */
  public static Cursor fetchAllDataOfContact(ContentResolver contentResolver, String mLookupKey) {
    mSelectionArgs[0] = mLookupKey;
    return contentResolver.query(Data.CONTENT_URI, PROJECTION, SELECTION, mSelectionArgs, SORT_ORDER);
  }
}