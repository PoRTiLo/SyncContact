package cz.xsendl00.synccontact.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.Settings;
import android.util.Log;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.RowComparator;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Manager for working with contact and group.
 */

public class ContactManager {

  private static final String TAG = "ContactManager";

  private AccountData accountData;
  private static ContactManager instance = null;
  private List<GroupRow> groupsLocal;
  private List<GroupRow> groupsServer;
  private List<ContactRow> contactsLocal;
  private List<ContactRow> contactsServer;
  // key id group
  private Map<String, List<ContactRow>> groupsContacts;
  private Context context;
  private boolean groupsLocalInit = false;
  private boolean contactsLocalInit = false;
  private boolean contactsServerInit = false;
  private boolean groupsServerInit = false;
  private boolean contactsLocalReload = false;
  private boolean groupsLocalReload = false;


  private ContactManager(Context context) {
    this.context = context;
  }

  /**
   * Get instance of contact manager. It is singleton.
   *
   * @param context context
   * @return instance of contact manager.
   */
  public static ContactManager getInstance(Context context) {
    if (instance == null) {
      instance = new ContactManager(context);
      Log.i(TAG, "ContactManager is null, created new");
    }
    return instance;
  }

  private void isContactInGroup() {
    Utils util = new Utils();
    util.startTime(TAG, "isContactInGroup");
    for (Map.Entry<String, List<ContactRow>> entry : groupsContacts.entrySet()) {
      if (!entry.getValue().isEmpty()) {
        List<ContactRow> newList = new ArrayList<ContactRow>();
        for (ContactRow contactRow : contactsLocal) {
          if (entry.getValue().contains(contactRow)) {
            contactRow.getGroupsId().add(entry.getKey());
            newList.add(contactRow);
          }
        }
        entry.getValue().clear();
        entry.getValue().addAll(newList);
      }
    }
    List<ContactRow> noInGroups = new ArrayList<ContactRow>();
    for (ContactRow contactRow : contactsLocal) {
      if (contactRow.getGroupsId() == null || contactRow.getGroupsId().isEmpty()) {
        noInGroups.add(contactRow);
      }
    }
    groupsContacts.put("default", noInGroups);

    Thread updateContactIdGroupThread = new Thread(new Runnable() {

      @Override
      public void run() {
        HelperSQL db = new HelperSQL(context);
        db.updateContactIdGroup(contactsLocal);
      }
    });
    updateContactIdGroupThread.start();
    util.stopTime(TAG, "isContactInGroup");
  }


  /**
   * Initialize {@link AccountData}. Load data from Accounts.
   *
   * @return true if account exist, other false
   */
  public boolean initAccountData() {
    accountData = AccountData.getAccountData(context);
    if (accountData == null) {
      accountData = new AccountData();
      return false;
    }
    return true;
  }


  /**
   * Initialize groups of contacts, groups and contacts.
   */
  public void initGroupsContacts() {
    Utils util = new Utils();
    util.startTime(TAG, "initGroupsContacts");
    groupsContacts = new HashMap<String, List<ContactRow>>();
    // TODO: initGroup and initGroup in new thread andd it finish call

    initGroup();

    initContact();

    isContactInGroup();
    // new Thread(new Runnable() {
    //
    // @Override
    // public void run() {
    // isContactInGroup();
    // }
    // }).start();
    util.stopTime(TAG, "initGroupsContacts");
  }

  public void reloadInitGroupContacts() {

    reloadContact();
    reloadGroup();

  }

  public boolean isLoaded() {
    return contactsLocalReload && groupsLocalReload;
  }

  /**
   * Reload data in manager.
   */
  public void reloadManager() {
    Thread reloadContact = new Thread(new Runnable() {

      @Override
      public void run() {
        reloadContact();
      }
    });
    reloadContact.start();

    Thread reloadGroup = new Thread(new Runnable() {

      @Override
      public void run() {
        reloadGroup();
      }
    });
    reloadGroup.start();

    try {
      reloadGroup.join();
      reloadContact.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    for (Map.Entry<String, List<ContactRow>> entry : groupsContacts.entrySet()) {
      for (GroupRow groupRow : groupsLocal) {
        if (groupRow.getId().equals(entry.getKey()) && groupRow.getSize() > 0) {
          entry.getValue().clear();
          for (ContactRow contactRow : contactsLocal) {
            if (contactRow.getGroupsId().contains(entry.getKey())) {
              entry.getValue().add(contactRow);
            }
          }
          break;
        }
      }
    }
  }

  /**
   * Reload data in manager. It run in new thread.
   */
  public void reloadManagerBackgroun() {

    new Thread(new Runnable() {

      @Override
      public void run() {
        reloadContact();
      }
    }).start();

    new Thread(new Runnable() {

      @Override
      public void run() {
        reloadGroup();
      }
    }).start();

    new Thread(new Runnable() {

      @Override
      public void run() {
        reloadGroup();
      }
    }).start();
  }

  /**
   * Reload contact from local database.
   */
  public void reloadContact() {
    Utils util = new Utils();
    util.startTime(TAG, "reloadContact");
    HelperSQL db = new HelperSQL(context);
    contactsLocal = new ArrayList<ContactRow>();
    contactsLocal.addAll(db.getAllContacts());
    setContactsLocalReload(true);
    util.stopTime(TAG, "reloadContact");
  }

  /**
   * Initialize contact. Get all contact from contact provider database and add them into local
   * database. Add only if contact not exist.
   */
  public void initContact() {
    Utils util = new Utils();
    util.startTime(TAG, "initContact");
    contactsLocal = new ArrayList<ContactRow>();
    contactsLocal.addAll(new AndroidDB().fetchAllContact(context.getContentResolver()));
    HelperSQL db = new HelperSQL(context);
    db.fillContacts(contactsLocal);
    contactsLocalInit = true;
    setContactsLocalReload(false);
    util.stopTime(TAG, "initContact");
  }

  /**
   * Initialize group from contact provider database.
   */
  public void initGroup() {
    Utils util = new Utils();
    util.startTime(TAG, "initGroup");
    groupsLocal = new ArrayList<GroupRow>();
    groupsLocal.addAll(GroupRow.fetchGroups(context.getContentResolver()));
    Log.i(TAG, "groupsLocal size:" + groupsLocal.size());

    for (GroupRow group : groupsLocal) {
      List<ContactRow> contacRows = new ContactRow().fetchGroupMembers(
          context.getContentResolver(), group.getId());
      Collections.sort(contacRows, new RowComparator());
      groupsContacts.put(group.getId(), contacRows);
      group.setSize(contacRows.size());
      Log.i(TAG, "id:" + group.getId() + "group size:" + group.getSize() + contacRows.toString());
    }

    HelperSQL db = new HelperSQL(context);
    db.fillGroups(groupsLocal);
    groupsLocalInit = true;
    setGroupsLocalReload(false);

    util.stopTime(TAG, "initGroup");
  }

  /**
   * Download all contact (only uuid, name) from LDAP server.
   *
   * @param handler for check connection.
   */
  public void initContactsServer(Handler handler) {
    Utils util = new Utils();
    util.startTime(TAG, "initContactsServer");
    if (accountData == null) {
      initAccountData();
    }
    contactsServer = new ArrayList<ContactRow>();
    contactsServer.addAll(ServerUtilities.fetchLDAPContactsNameUUID(
        new ServerInstance(accountData), context, handler));
    // sort contact by name
    Collections.sort(contactsServer, new RowComparator());
    groupsServerInit = true;

    util.stopTime(TAG, "initContactsServer");
  }

  /**
   * Download all groups (uuid, name) from LDAP server.
   *
   * @param handler for check connection.
   */
  public void initGroupsServer(Handler handler) {
    Utils util = new Utils();
    util.startTime(TAG, "initGroupsServer");
    if (accountData == null) {
      initAccountData();
    }
    groupsServer = new ArrayList<GroupRow>();
    groupsServer.addAll(ServerUtilities.fetchLDAPGroups(new ServerInstance(accountData), context,
        handler));
    // sort contact by name
    Collections.sort(groupsServer, new RowComparator());
    groupsServerInit = true;

    util.stopTime(TAG, "initGroupsServer");
  }

  /**
   * Reload groups from local database.
   */
  public void reloadGroup() {
    Utils util = new Utils();
    util.startTime(TAG, "reloadGroup");
    HelperSQL helperSQL = new HelperSQL(context);
    groupsLocal = new ArrayList<GroupRow>();
    groupsLocal = helperSQL.getAllGroups();
    setGroupsLocalReload(true);
    util.stopTime(TAG, "reloadGroup");
  }

  /**
   * Get contact from local database. If is null call {@link #reloadContact()}.
   *
   * @return list of ContactRow.
   */
  public List<ContactRow> getContactsLocal() {
    if (contactsLocal == null) {
      reloadContact();
    }
    return contactsLocal;
  }

  public List<GroupRow> getGroupsLocal() {
    if (groupsLocal == null) {
      reloadGroup();
    }
    return groupsLocal;
  }


  public static void makeGroupVisible(String accountName, ContentResolver resolver) {
    try {
      ContentProviderClient client = resolver.acquireContentProviderClient(ContactsContract.AUTHORITY_URI);
      ContentValues cv = new ContentValues();
      cv.put(Groups.ACCOUNT_NAME, accountName);
      cv.put(Groups.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
      cv.put(Settings.UNGROUPED_VISIBLE, true);
      client.insert(
          Settings.CONTENT_URI.buildUpon()
              .appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true")
              .build(), cv);
    } catch (RemoteException e) {
      Log.d(TAG, "Cannot make the Group Visible");
    }
  }

  /**
   * Get synchronization contact in list. Contact get from contacList or load from database.
   *
   * @return
   */
  public List<ContactRow> getContactListSync() {
    if (contactsLocalReload) {
      reloadContact();
    }
    List<ContactRow> syncContacts = new ArrayList<ContactRow>();
    for (ContactRow contactRow : contactsLocal) {
      if (contactRow.isSync()) {
        syncContacts.add(contactRow);
      }
    }
    return syncContacts;
  }

  public boolean isGroupsLocalInit() {
    return groupsLocalInit;
  }

  public void setGroupsLocalInit(boolean groupListInit) {
    this.groupsLocalInit = groupListInit;
  }

  public boolean isContactsLocalInit() {
    return contactsLocalInit;
  }

  public void setContactListInit(boolean contactListInit) {
    this.contactsLocalInit = contactListInit;
  }

  public boolean isContactsServerInit() {
    return contactsServerInit;
  }

  public void setContactsServerInit(boolean contactsLDAP) {
    this.contactsServerInit = contactsLDAP;
  }

  public List<ContactRow> getContactsServer() {
    return contactsServer;
  }

  /**
   * @return Returns the groupsLocalReload.
   */
  public boolean isGroupsLocalReload() {
    return groupsLocalReload;
  }

  /**
   * @param groupsReloaded The groupsLocalReload to set.
   */
  public void setGroupsLocalReload(boolean groupsReloaded) {
    this.groupsLocalReload = groupsReloaded;
  }

  /**
   * @return Returns the contactsLocalReload.
   */
  public boolean isContactsLocalReload() {
    return contactsLocalReload;
  }

  /**
   * @param contactsReloaded The contactsLocalReload to set.
   */
  public void setContactsLocalReload(boolean contactsReloaded) {
    this.contactsLocalReload = contactsReloaded;
  }

  /**
   * @return Returns the accountData.
   */
  public AccountData getAccountData() {
    return accountData;
  }

  /**
   * @param accountData The accountData to set.
   */
  public void setAccountData(AccountData accountData) {
    this.accountData = accountData;
  }

  /**
   * @return Returns the groupsServer.
   */
  public List<GroupRow> getGroupsServer() {
    return groupsServer;
  }

  /**
   * @param groupsServer The groupsServer to set.
   */
  public void setGroupsServer(List<GroupRow> groupsServer) {
    this.groupsServer = groupsServer;
  }

  /**
   * @return Returns the groupsServerInit.
   */
  public boolean isGroupsServerInit() {
    return groupsServerInit;
  }

  /**
   * @param groupsServerInit The groupsServerInit to set.
   */
  public void setGroupsServerInit(boolean groupsServerInit) {
    this.groupsServerInit = groupsServerInit;
  }

  public Map<String, List<ContactRow>> getGroupsContacts() {
    return groupsContacts;
  }
}
