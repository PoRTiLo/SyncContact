package cz.xsendl00.synccontact.ldap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.Mapping;

public class Synchronization {

  private final static String TAG = "Synchronization";
  
  public void synchronization(final ServerInstance ldapServer, final Context context) throws RemoteException, OperationApplicationException {
    // get sync user
    HelperSQL db = new HelperSQL(context);
    List<ContactRow> contactsId = db.getSyncContacts();
    Log.i(TAG, "contactsId: " + contactsId.size());
    // get contact with dirty flag
    Mapping mapping = new Mapping();
    Map<String, GoogleContact> contactsDirty = mapping.fetchDirtyContacts(context, contactsId);
    Log.i(TAG, "fetchDirtyContacts: " + contactsDirty.size());
    //for(GoogleContact c : contactsDirty) {
    //  Log.i(TAG, c.getStructuredName().getDisplayName());
    //}
    // get timestamp last synchronization
    String timestamp = db.newerTimestamp();
    Log.i(TAG, timestamp);
    // get contact from server which was newer than timestamp
    Map<String, GoogleContact> contactsServer = ServerUtilities.fetchModifyContactsLDAP(ldapServer, context, timestamp);
    Log.i(TAG, "fetchModifyContactsLDAP: " + contactsServer.size());
    //for(GoogleContact c : contactsServer) {
    //  Log.i(TAG, c.getStructuredName().getDisplayName());
    //}
    // intersection local change and LDAP change
    Map<String, GoogleContact> intersection = intersection(contactsDirty, contactsServer);
    // difference local change and intersection, must be update on LDAP server
    Map<String, GoogleContact> differenceDirty = difference(contactsDirty, intersection);
    // difference LDAP change and intersection, must be update on DB
    Map<String, GoogleContact> differenceLDAP = difference(contactsServer, intersection);
    
    // merge contact
    
    // update db syncContact.db
    
    // update db contact.db
    AndroidDB.updateContactsDb(context, differenceLDAP);
    // update server contact
    ServerUtilities.updateContactsLDAP(ldapServer, context, differenceLDAP);//differenceDirty
    // set new timestamp
    timestamp = ContactRow.createTimestamp();
    // set dirty flag to disable (0)
    db.updateContacts(differenceDirty, timestamp);
  }
  
  public <K, V> Map<K, V> intersection(final Map<K, V> map1, final Map<K, V> map2) {
    Log.i(TAG, "intersection: " + map1.size() + " to " + map2.size());
    Map<K, V> map = new HashMap<K, V>();
    for (Map.Entry<K, V> entry : map1.entrySet()) {
      if(map2.get(entry.getKey()) != null) {
        map.put(entry.getKey(), entry.getValue());
      }
    }
    Log.i(TAG, "intersection result: " + map.size());
    return map;
  }
  
  public <K, V> Map<K, V> difference(final Map<K, V> map1, final Map<K, V> map2) {
    Log.i(TAG, "difference: " + map1.size() + " to " + map2.size());
    Map<K, V> map = new HashMap<K, V>();
    map.putAll(map1);
    for (Map.Entry<K, V> entry : map2.entrySet()) {
      if(map.get(entry.getKey()) != null) {
        map.remove(entry.getKey());
      }
    }

    /*
    for (Map.Entry<K, V> entry : map.entrySet()) {
      GoogleContact g = (GoogleContact) entry.getValue();
      Log.i(TAG, "difference result: " + entry.getKey() + ":" + g.getStructuredName().getDisplayName());
    }
    
    Log.i(TAG, "difference result: " + map.size());*/
    return map;
  }
}
