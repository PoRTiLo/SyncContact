package cz.xsendl00.synccontact.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.unboundid.ldap.sdk.LDAPException;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.Settings;
import android.util.Log;
import android.util.SparseArray;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.database.AndroidDB;
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
  private List<GroupRow> localGroups;
  private List<ContactRow> localContacts;
  private SparseArray<List<ContactRow>> localGroupsContacts;
  private boolean localGroupsContactsInit = false;

  private boolean localContactsInit = false;
  private boolean localGroupsInit = false;
  private boolean contactsServerInit = false;
  private boolean groupsServerInit = false;
  private static ContactManager instance = null;
  private AccountData accountData;
  private List<GroupRow> groupsServer;
  private List<ContactRow> contactsServer;
  private List<ContactRow> serverContact2Import;
  private List<GroupRow> serverGroup2Import;
  private Context context;

  private boolean onlyWifi = false;


  /**
   * @return Returns the onlyWifi.
   */
  public boolean isOnlyWifi() {
    return onlyWifi;
  }



  /**
   * @param onlyWifi The onlyWifi to set.
   */
  public void setOnlyWifi(boolean onlyWifi) {
    this.onlyWifi = onlyWifi;
  }


  /**
   * @return Returns the onlyWifiInit.
   */
  public boolean isOnlyWifiInit() {
    return onlyWifiInit;
  }


  /**
   * @param onlyWifiInit The onlyWifiInit to set.
   */
  public void setOnlyWifiInit(boolean onlyWifiInit) {
    this.onlyWifiInit = onlyWifiInit;
  }


  private boolean onlyWifiInit = false;

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

  public List<GroupRow> getLocalGroups() {
    if (localGroups == null) {
      localGroups = GroupRow.fetchGroups(context.getContentResolver(), null);
      localGroupsInit = true;
    }
    if (!localGroupsInit) {
      localGroups.clear();
      localGroups.addAll(GroupRow.fetchGroups(context.getContentResolver(), null));
      localGroupsInit = true;
    }
    return localGroups;
  }

  public List<ContactRow> getLocalContacts() {
    String where = RawContacts.DELETED + "<>1";
    if (localContacts == null) {
      Log.i(TAG, "getLocalContacts is null");
      localContacts = ContactRow.fetchAllRawContact(context.getContentResolver(), where);
      localContactsInit = true;
    }
    if (!localContactsInit) {
      Log.i(TAG, "getLocalContacts add ALL");
      localContacts.clear();
      localContacts.addAll(ContactRow.fetchAllRawContact(context.getContentResolver(), where));
      localContactsInit = true;
    }
    return localContacts;
  }

  public List<ContactRow> getLocalContactsSyncModified() {
    String where = RawContacts.SYNC1 + "=1 AND "  + RawContacts.DIRTY + "=1";
    return ContactRow.fetchAllRawContact(context.getContentResolver(), where);
  }

  public List<GroupRow> getLocalGroupsSyncModified() {
    String where = Groups.SYNC1 + "=1 AND "  + Groups.DIRTY + "=1";
    return GroupRow.fetchGroups(context.getContentResolver(), where);
  }

  public SparseArray<List<ContactRow>> getLocalGroupsContacts() {
    getLocalGroups();
    getLocalContacts();

    if (localGroupsContacts == null) {
      localGroupsContacts = new SparseArray<List<ContactRow>>();
    }
    if (!localGroupsContactsInit) {
      for (GroupRow groupRow: localGroups) {
        List<ContactRow> contactRows = ContactRow.fetchGroupMembersName(context.getContentResolver(), groupRow.getId());
        List<ContactRow> listForGroups = new ArrayList<ContactRow>();
        for (ContactRow contactRow : localContacts) {
          if (contactRows.contains(contactRow)) {
            listForGroups.add(contactRow);
          }
        }
        if (listForGroups.size() == contactRows.size()) {
          Log.i(TAG, "mnoziny se rovnaji OK");
        } else {
          Log.i(TAG, "mnoziny se ne rovnaji BAD");
        }
        localGroupsContacts.put(groupRow.getId(), listForGroups);
        groupRow.setSize(contactRows.size());
      }
      // TODO: dodelat kdyz neni ve skupine
      //for (int i = 0; i < localGroupsContacts.size(); i++) {
      //  List<ContactRow> = localGroupsContacts.valueAt(i);
      //}
      localGroupsContactsInit = true;
    }
    return localGroupsContacts;
  }

  public void convertContact2NewAccount() {
    if (localContacts == null || !localContactsInit) {
      getLocalContacts();
    }
    new AndroidDB().importContactsToSyncAccount(context, localContacts);
  }

  public void updateGroupsUuid() {
    if (localGroups == null || !localGroupsInit) {
      getLocalGroups();
    }

    List<GroupRow> groupRows = new ArrayList<GroupRow>();
    for (GroupRow groupRow : localGroups) {
      if (groupRow.getUuidFirst() == null) {
        groupRow.getUuid();
        groupRows.add(groupRow);
      }
    }
    new AndroidDB().updateGroupsUuid(context, groupRows);
  }

  /**
   * Get contacts from server, which are not in mobile.
   * @return
   */
  public List<GroupRow> getServerGroup2Import(boolean... reload) {
    if (serverGroup2Import != null && reload.length <= 0) {
      return serverGroup2Import;
    } else {
      serverGroup2Import = new ArrayList<GroupRow>();
    }
    if (reload != null && reload.length > 0 && reload[0]) {
      localGroupsInit = false;
    }
    for (GroupRow groupRow : groupsServer) {
      boolean found = false;
      for (GroupRow groupRowLocal : getLocalGroups()) {
        if (groupRowLocal.getUuidFirst() != null
            && groupRow.getUuid().equals(groupRowLocal.getUuidFirst())) {
          found = true;
          break;
        }
      }
      if (!found) {
        serverGroup2Import.add(groupRow);
      }
    }
    return serverGroup2Import;
  }

  /**
   * Get contacts from server, which are not in mobile.
   * @return
   */
  public List<ContactRow> getServerContact2Import(boolean... reload) {
    if (serverContact2Import != null && reload.length <= 0) {
      return serverContact2Import;
    } else {
      serverContact2Import = new ArrayList<ContactRow>();
    }
    if (reload != null && reload.length > 0 && reload[0]) {
      localContactsInit = false;
    }
    for (ContactRow contactRowServer : contactsServer) {
      boolean found = false;
      for (ContactRow contactRowLocal : getLocalContacts()) {
        if (contactRowServer.getUuidFirst() != null
            && contactRowServer.getUuid().equals(contactRowLocal.getUuidFirst())) {
          found = true;
          break;
        }
      }
      if (!found) {
        serverContact2Import.add(contactRowServer);
      }
    }
    return serverContact2Import;
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
   * Download all contact (only uuid, name) from LDAP server.
   *
   * @param handler for check connection.
   * @throws LDAPException
   */
  public void initContactsServer(Handler handler) throws LDAPException {
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
    contactsServerInit = true;

    util.stopTime(TAG, "initContactsServer");
  }

  /**
   * Download all groups (uuid, name) from LDAP server.
   *
   * @param handler for check connection.
   * @throws LDAPException
   */
  public void initGroupsServer(Handler handler) throws LDAPException {
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
    setGroupsServerInit(true);

    util.stopTime(TAG, "initGroupsServer");
  }



  public static void makeGroupVisible(ContentResolver resolver) {
    try {
      ContentProviderClient client = resolver.acquireContentProviderClient(ContactsContract.AUTHORITY_URI);
      ContentValues cv = new ContentValues();
      cv.put(Groups.ACCOUNT_NAME, Constants.ACCOUNT_NAME);
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
   * @return Returns the localContactsInit.
   */
  public boolean isLocalContactsInit() {
    return localContactsInit;
  }

  /**
   * @param localContactsInit The localContactsInit to set.
   */
  public void setLocalContactsInit(boolean localContactsInit) {
    this.localContactsInit = localContactsInit;
  }

  /**
   * @return Returns the localGroupsInit.
   */
  public boolean isLocalGroupsInit() {
    return localGroupsInit;
  }

  /**
   * @param localGroupsInit The localGroupsInit to set.
   */
  public void setLocalGroupsInit(boolean localGroupsInit) {
    this.localGroupsInit = localGroupsInit;
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


  /**
   * @return Returns the localGroupsContactsInit.
   */
  public boolean isLocalGroupsContactsInit() {
    return localGroupsContactsInit;
  }


  /**
   * @param localGroupsContactsInit The localGroupsContactsInit to set.
   */
  public void setLocalGroupsContactsInit(boolean localGroupsContactsInit) {
    this.localGroupsContactsInit = localGroupsContactsInit;
  }

  public boolean checkInternet() {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
      boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
      if (!isWiFi) {
        return false;
      } else {
        return true;
      }
    }
    return false;
  }
}
