package cz.xsendl00.synccontact.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import com.unboundid.ldap.sdk.LDAPException;

import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.Mapping;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Synchronization.
 */
@EBean
public class Synchronization {

  @Bean
  protected AndroidDB androidDB;

  @Bean
  protected Utils utils;

  @Bean
  protected Mapping mapping;


  @Bean
  protected ServerUtilities serverUtilities;

  private static final String TAG = "Synchronization";
 // private final Handler handler = new Handler();


  private void init(final Context context) {
    if (androidDB == null) {
      androidDB = new AndroidDB();
    }
    if (utils == null) {
      utils = new Utils();
    }

    if (mapping == null) {
      mapping = new Mapping();
    }

    if (serverUtilities == null) {
      serverUtilities = new ServerUtilities();
    }
  }



  /**
   * Process synchronization.
   * @param ldapServer server instance
   * @throws RemoteException
   * @throws OperationApplicationException
   */
  //TODO jsendler 19.05.2014 : sync group, pokud je ve skupine noce voneho musim pridat
  public void synchronization(final ServerInstance ldapServer, final Context context, final Handler handler) throws RemoteException, OperationApplicationException {
    init(context);

 // get timestamp last synchronization
    String timestamp = androidDB.newerTimestamp(context);
    if (timestamp == null) {
      timestamp = utils.createTimestamp();
    }
    Log.i(TAG, "Last time synchonization : " + timestamp);

    final List<Integer> deletedLocalContactIds = new ArrayList<Integer>();
    final Map<String, GoogleContact> contactsLocalDirty = new HashMap<String, GoogleContact>();
    Thread readLocalContactThread = readLocalContacts(context, contactsLocalDirty, deletedLocalContactIds);

    //final Map<>
    final List<GroupRow> localGroupsModify = new ArrayList<GroupRow>();
    final List<GroupRow> localGroupsModifyDeleted = new ArrayList<GroupRow>();
    Thread readLocalGroupsThread = readLocalGroups(context, localGroupsModify, localGroupsModifyDeleted);

    //TODO: only contacts synchonizitions??
    // get contacts from server which are newer than timestamp
    utils.startTime(TAG, "get server newer contacts");
    Map<String, GoogleContact> contactsServer = serverUtilities.fetchModifyContactsServer(ldapServer, context, handler, timestamp);
    utils.stopTime(TAG, "get server newer contacts size : " + contactsServer.size());
    // get groups from server which are newer than timestamp
    utils.startTime(TAG, "get server newer groups");
    List<GroupRow> groupsServer = serverUtilities.fetchModifyGroupsServer(ldapServer, context, handler, timestamp);
    utils.stopTime(TAG, "get server newer groups size : " + groupsServer.size());

    try {
      readLocalContactThread.join();
      readLocalGroupsThread.join();
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

    // intersection local change and LDAP change
    utils.startTime(TAG, "intersection localContacts :" + contactsLocalDirty.size() + ", server newer contacts : " + contactsServer.size());
    Map<String, GoogleContact> intersectionLocal2Server = intersection(contactsLocalDirty, contactsServer);
    utils.stopTime(TAG, "intersection is : " + intersectionLocal2Server.size());

    // difference local change and intersection, must be update on LDAP server
    utils.startTime(TAG, "difference localContacts : " + contactsLocalDirty.size() + ", intersection : " + intersectionLocal2Server.size());
    final Map<String, GoogleContact> difference2Server = difference(contactsLocalDirty, intersectionLocal2Server);
    utils.stopTime(TAG, "difference localContacts : " + difference2Server.size());

    // difference LDAP change and intersection, must be update on DB
    utils.startTime(TAG, "difference serveContacts : " + contactsServer.size() + ", intersection : " + intersectionLocal2Server.size());
    final Map<String, GoogleContact> difference2Local = difference(contactsServer, intersectionLocal2Server);
    utils.stopTime(TAG, "difference serveContacts : " + difference2Local.size());

    utils.startTime(TAG, "resolve conflict");
    Map<String, GoogleContact> conflictServer = new HashMap<String, GoogleContact>();
    for (Map.Entry<String, GoogleContact> entry : intersectionLocal2Server.entrySet()) {
      GoogleContact localContact = contactsLocalDirty.get(entry.getKey());
      GoogleContact serverContact = contactsServer.get(entry.getKey());
      if (localContact.equals(serverContact)) {
        //TODO: only update last time sync and modify bit
        difference2Local.put(entry.getKey(), entry.getValue());
        difference2Server.put(entry.getKey(), entry.getValue());
      } else if (localContact.isDeleted()) {
        difference2Server.put(entry.getKey(), localContact);
      } else {
        Log.i(TAG, "contact_time : " + timestamp + ", server_time : " + serverContact.getTimestamp());
        if (timestamp.compareTo(serverContact.getTimestamp()) > 0) {
          difference2Server.put(entry.getKey(), localContact);
        } else { // server contacts is newer or value is same
          difference2Local.put(entry.getKey(), serverContact);
        }
      }
    }
    utils.stopTime(TAG, "resolve conflict, conflictServer : "  + conflictServer.size());

    // update group
    try {
      serverUtilities.removeOrAddorUpdateServerGroup(ldapServer, context, handler, localGroupsModify);
    } catch (LDAPException e) {
      e.printStackTrace();
    }

    // update db contact.db
    Thread localThread = updateLocalDatabse(context, difference2Local);
    // update server contacts
    Thread serverThread = updateServerContacts(context, handler, ldapServer, difference2Server);

    try {
      localThread.join();
      serverThread.join();
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

 // remove contact from local database
    final Map<String, GoogleContact> toDeleteOnserver = androidDB.deleteContacts(context); //, deletedLocalContactIds);

    // remove group local
    final List<GroupRow> toDeleteGroupOnServer = androidDB.deleteGroups(context); //, deletedLocalContactIds);
    // update group
    try {
      serverUtilities.removeOrAddorUpdateServerGroup(ldapServer, context, handler, toDeleteGroupOnServer);
    } catch (LDAPException e) {
      e.printStackTrace();
    }

    updateServerContacts(context, handler, ldapServer, toDeleteOnserver);
        // set dirty flag to disable (0)
    androidDB.cleanModifyStatusNewTimestamp(context, contactsLocalDirty);
    androidDB.cleanModifyStatusNewTimestampGroup(context, localGroupsModify);
//      }
//    }).start();
  }


  /**
   * Create list of all modification and sync contacts, and list of deleted sync contacts
   * @param context Context
   * @param contactsLocalDirty local modification and sync contacts
   * @param deletedLocalContactIds local deleted and sync contacts
   * @return {@link Thread}
   */
  private Thread readLocalContacts(final Context context,
      final Map<String, GoogleContact> contactsLocalDirty, final List<Integer> deletedLocalContactIds) {
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
        Utils readUtil = new Utils();
        ContactManager contactManager = ContactManager.getInstance(context);
        readUtil.startTime(TAG, "get local dirty contacts");
        contactsLocalDirty.putAll(
            mapping.fetchDirtyContacts(context.getContentResolver(),
            contactManager.getLocalContactsSyncModified(),
            deletedLocalContactIds)
        );
        readUtil.stopTime(TAG, "get local dirty contacts size :" + contactsLocalDirty.size()
            + ", deleted:" + deletedLocalContactIds.size());
      }
    };
    return Utils.performOnBackgroundThread(runnable);
  }


  private Thread readLocalGroups(final Context context, final List<GroupRow> localGroupsModify, final List<GroupRow> localGroupsModifyDeleted) {
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
        Utils readUtil = new Utils();
        readUtil.startTime(TAG, "get local dirty groups");
        ContactManager contactManager = ContactManager.getInstance(context);
        localGroupsModify.addAll(contactManager.getLocalGroupsSyncModified());
        for (GroupRow groupRow : localGroupsModify) {
          if (groupRow.isDeleted()) {
            localGroupsModifyDeleted.add(groupRow);
          }
        }
        readUtil.stopTime(TAG, "get local dirty groups size :" + localGroupsModify.size()
            + ", deleted:" + localGroupsModifyDeleted.size());
      }
    };
    return Utils.performOnBackgroundThread(runnable);
  }

  public Thread updateServerContacts(final Context context, final Handler handler, final ServerInstance ldapServer, final Map<String, GoogleContact> difference2Server) {
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
      Utils utilServer = new Utils();
      utilServer.startTime(TAG, "update server contacts");
      try {
        // update or added
        serverUtilities.updateContactsServer(ldapServer, context, handler, difference2Server);
      } catch (LDAPException e) {
        //TODO:dat uzivateli info o chybe
        e.printStackTrace();
      } // differenceDirty
      utilServer.stopTime(TAG, "update server contacts");

      }
    };
    // run on background thread.
    return Utils.performOnBackgroundThread(runnable);
  }

  public Thread updateLocalDatabse(final Context context, final Map<String, GoogleContact> difference2Local) {
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          Utils utilLocal = new Utils();
          utilLocal.startTime(TAG, "update local contacts");
          Map<String, GoogleContact> noConflictEntry = new HashMap<String, GoogleContact>();
          Map<String, GoogleContact> conflictEntry = new HashMap<String, GoogleContact>();
          List<ContactRow> contactRows = ContactRow.fetchAllRawContact(context.getContentResolver(), null);
          for (Map.Entry<String, GoogleContact> entry : difference2Local.entrySet()) {
            boolean found = false;
            for (ContactRow contactRow : contactRows) {
              if (contactRow.getUuidFirst().equals(entry.getKey())) {
                if (contactRow.isSync()) {
                  entry.getValue().setId(contactRow.getId());
                  noConflictEntry.put(entry.getKey(), entry.getValue());
                }
                found = true;
              }
            }
            //todo check if is in sync group
            if (!found) {
              conflictEntry.put(entry.getKey(), entry.getValue());
            }
          }
          androidDB.updateContactsDatabase(context, noConflictEntry);
          androidDB.addContacts2Database(context, conflictEntry, null);
          utilLocal.stopTime(TAG, "update local contacts");
        } catch (RemoteException e) {
          e.printStackTrace();
        } catch (OperationApplicationException e) {
          e.printStackTrace();
        }

      }
    };
    // run on background thread.
    return Utils.performOnBackgroundThread(runnable);
  }



  private void remove() {
 // remove contact from provider database, which is set as deleted
    //CALLER_IS_SYNCADAPTER
    //ContentResolver cr = context.getContentResolver();
    //Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, String[]{}, null, null, null);



      //      String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
    //        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
        //    System.out.println("The uri is " + uri.toString());
//          //  cr.delete(uri, null, null);
//
//
//    Uri uri = Uri.withAppendedPath(RawContacts.CONTENT_URI, deletedLocalContactIds.get(0));
//    Uri uri1 = Uri.withAppendedPath(uri, ContactsContract.CALLER_IS_SYNCADAPTER);
//    Log.i(TAG, uri1.toString());
//    int i = context.getContentResolver().delete(uri1 , null, null);
//    Log.i(TAG, "res:" + i);


    //elete(ContactsContract.Data.CONTENT_URI,
    //    ContactsContract.Data.RAW_CONTACT_ID + EQUALS
    //            + rawContactID, null);
  }

  /**
   * Intersection based same UUID.
   * @param map1
   * @param map2
   * @return map
   */
  private <K, V> Map<K, V> intersection(final Map<K, V> map1, final Map<K, V> map2) {
    Map<K, V> map = new HashMap<K, V>();
    for (Map.Entry<K, V> entry : map1.entrySet()) {
      if (map2.get(entry.getKey()) != null) {
        map.put(entry.getKey(), entry.getValue());
      }
    }
    return map;
  }

  /**
   * Difference two maps based on their key like uuid
   * @param map1
   * @param map2
   * @return
   */
  private <K, V> Map<K, V> difference(final Map<K, V> map1, final Map<K, V> map2) {
    Map<K, V> map = new HashMap<K, V>();
    map.putAll(map1);
    for (Map.Entry<K, V> entry : map2.entrySet()) {
      if (map.get(entry.getKey()) != null) {
        map.remove(entry.getKey());
      }
    }
    return map;
  }
}
