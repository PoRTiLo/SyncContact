package cz.xsendl00.synccontact.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
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
  protected HelperSQL helperSQL;

  @Bean
  protected ServerUtilities serverUtilities;

  private static final String TAG = "Synchronization";
  private final Handler handler = new Handler();


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

    if (helperSQL == null) {
      helperSQL = new HelperSQL(context);
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
  public void synchronization(final ServerInstance ldapServer, final Context context) throws RemoteException, OperationApplicationException {
    init(context);

 // get timestamp last synchronization
    String timestamp = helperSQL.newerTimestamp();
    Log.i(TAG, "Last time synchonization : " + timestamp);

    // TODO jsendler 19.05.2014: reinitialize database, find out changes
    final Map<String, GoogleContact> contactsDirty = new HashMap<String, GoogleContact>();
    Thread readLocalThread = new Thread(new Runnable() {

      @Override
      public void run() {
        // get sync user
        Utils readUtil = new Utils();
        readUtil.startTime(TAG, "get local sync contacts");
        List<ContactRow> contactsId = helperSQL.getSyncContacts();
        readUtil.stopTime(TAG, "get local sync contacts size :" + contactsId.size());

        // get contact with dirty flag
        readUtil.startTime(TAG, "get local dirty contacts");
        contactsDirty.putAll(mapping.fetchDirtyContacts(context.getContentResolver(), contactsId));
        readUtil.stopTime(TAG, "get local dirty contacts size :" + contactsDirty.size());
      }
    });
    readLocalThread.start();

    //TODO: only contacts synchonizitions??
    // get contacts from server which are newer than timestamp
    utils.startTime(TAG, "get server newer contacts");
    Map<String, GoogleContact> contactsServer =
        ServerUtilities.fetchModifyContactsLDAP(ldapServer, context, handler, timestamp);
    utils.stopTime(TAG, "get server newer contacts size : " + contactsServer.size());


    try {
      readLocalThread.join();
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

    // intersection local change and LDAP change
    utils.startTime(TAG, "intersection localContacts :" + contactsDirty.size()
        + ", server newer contacts : " + contactsServer.size());
    Map<String, GoogleContact> intersection = intersection(contactsDirty, contactsServer);
    utils.stopTime(TAG, "intersection is : " + intersection.size());

    // difference local change and intersection, must be update on LDAP server
    utils.startTime(TAG, "difference localContacts : "
        + contactsDirty.size() + ", intersection : " + intersection.size());
    final Map<String, GoogleContact> difference2Server = difference(contactsDirty, intersection);
    utils.stopTime(TAG, "difference localContacts : " + difference2Server.size());

    // difference LDAP change and intersection, must be update on DB
    utils.startTime(TAG, "difference serveContacts : "
        + contactsServer.size() + ", intersection : " + intersection.size());
    final Map<String, GoogleContact> difference2Local = difference(contactsServer, intersection);
    utils.stopTime(TAG, "difference serveContacts : " + difference2Local.size());

    utils.startTime(TAG, "conflict");
    Map<String, GoogleContact> conflictServer = new HashMap<String, GoogleContact>();
    //Map<String, GoogleContact> conflictLocal = new HashMap<String, GoogleContact>();
    for (Map.Entry<String, GoogleContact> entry : intersection.entrySet()) {
      GoogleContact localContact = contactsDirty.get(entry.getKey());
      GoogleContact serverContact = contactsServer.get(entry.getKey());
      if (localContact.equals(serverContact)) {
        difference2Local.put(entry.getKey(), entry.getValue());
        difference2Server.put(entry.getKey(), entry.getValue());
      } else {
        conflictServer.put(entry.getKey(), serverContact);
        //conflictLocal.put(entry.getKey(), localContact);
      }
    }
    utils.stopTime(TAG, "conflictServer : "  + conflictServer.size());
    // merge contact - > use data from server
    difference2Local.putAll(conflictServer);
//    //difference2Server.putAll(conflictServer);
//
//    // update db syncContact.db
//
    // update db contact.db
    Utils utilLocal = new Utils();
    utilLocal.startTime(TAG, "update local contacts");
    Thread localThread = new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          androidDB.updateContactsDb(context, difference2Local);
        } catch (RemoteException e) {
          e.printStackTrace();
        } catch (OperationApplicationException e) {
          e.printStackTrace();
        }
      }
    });
    localThread.start();
    utilLocal.stopTime(TAG, "update local contacts");
//
//    // update server contacts
//    Utils utilServer = new Utils();
//    utilServer.startTime(TAG, "update server contacts");
//    Thread serverThread = new Thread(new Runnable() {
//
//      @Override
//      public void run() {
//        serverUtilities.updateContactsServer(ldapServer, context, handler, difference2Server); // differenceDirty
//      }
//    });
//    serverThread.start();
//    utilServer.stopTime(TAG, "update server contacts");



    // set new timestamp
    timestamp = utils.createTimestamp();
    Log.i(TAG, "new timestamp:" + timestamp);
    helperSQL.updateContacts(contactsDirty, timestamp);
    //helperSQL.updateContacts(difference2Local, timestamp);

    // set dirty flag to disable (0)
  }

  /**
   * Synchronization on settings app.
   *
   * @param ldapServer
   * @param context
   * @throws RemoteException
   * @throws OperationApplicationException
   */
  public void uploadContact(final ServerInstance ldapServer, final Context context)
      throws RemoteException, OperationApplicationException {
    // get sync user
    // Log.i(TAG, "contacts: " + contactManager.getContactListSync());
    // // get timestamp last synchronization
    // String timestamp = db.newerTimestamp();
    // Log.i(TAG, timestamp);
    // // get contact from server which was newer than timestamp
    // Map<String, GoogleContact> contactsServer =
    // ServerUtilities.fetchModifyContactsLDAP(ldapServer, context, timestamp);
    // Log.i(TAG, "fetchModifyContactsLDAP: " + contactsServer.size());
    //
    // // intersection local change and LDAP change
    // List<ContactRow> intersection = intersection(contacts, contactsServer);
    // // difference local change and intersection, must be update on LDAP server
    // difference(contacts, intersection);
    // // fetch all contact to must be done on server
    // Map<String, GoogleContact> contactsDirty = new Mapping().fetchDirtyContacts(context,
    // contacts);
    // // difference LDAP change and intersection, must be update on DB
    // difference(contactsServer, intersection);
    //
    // // merge contact
    //
    // // update db syncContact.db
    //
    // // update db contact.db
    // AndroidDB.updateContactsDb(context, contactsServer);
    // // update server contact
    // ServerUtilities.updateContactsLDAP(ldapServer, context, contactsDirty);//differenceDirty
    // // set new timestamp
    // timestamp = ContactRow.createTimestamp();
    // // set dirty flag to disable (0)
    // //db.updateContacts(differenceDirty, timestamp);
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
   * Intersection of local and LDAP contacts.
   *
   * @param lis1
   * @param map2
   * @return list
   */
  private List<ContactRow> intersection(final List<ContactRow> list1,
      final Map<String, GoogleContact> map2) {
    Log.i(TAG, "intersection: " + list1.size() + " to " + map2.size());
    List<ContactRow> list = new ArrayList<ContactRow>();
    for (ContactRow entry : list1) {
      if (map2.get(entry.getUuid()) != null) {
        list.add(entry);
      }
    }
    Log.i(TAG, "intersection result: " + list.size());
    return list;
  }

  private void difference(List<ContactRow> list1, final List<ContactRow> list2) {
    Log.i(TAG, "difference: " + list1.size() + " to " + list2.size());
    for (ContactRow contactRow : list2) {
      list1.remove(contactRow);
    }
  }

  private void difference(Map<String, GoogleContact> map1, final List<ContactRow> list2) {
    Log.i(TAG, "difference: " + map1.size() + " to " + list2.size());
    for (ContactRow contactRow : list2) {
      map1.remove(contactRow.getUuid());
    }
  }

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
