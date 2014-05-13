package cz.xsendl00.synccontact.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.ContactRowComparator;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.Utils;
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

public class ContactManager {
  private static final String TAG = "ContactManager";
  
  
  private static ContactManager instance = null;
  private List<GroupRow> groupsList;
  private List<ContactRow> contactList;
  private List<ContactRow> contactListLDAP;
  private Map<String, List<ContactRow>> groupContactMap;
  private Context context;
  private boolean loaded = false;
  private boolean groupListInit = false;
  private boolean contactListInit = false;
  private boolean contactsLDAPInit = false;
  
  public boolean isLoaded() {
    return loaded;
  }
  
  public void setLoaded(boolean val) {
    this.loaded = val;
  }
  private ContactManager(Context context) {
    this.context = context;
  }
  
  public static ContactManager getInstance(Context context) {
    if (instance == null) {
      instance = new ContactManager(context);
    }
    return instance;
  }
  
  public void reloadManager() {
    reloadContact();
    reloadGroup();
  }
  
  /**
   * Reload contact from local database.
   */
  public void reloadContact() {
    Log.i(TAG, "start --- reloadContact --- : " + Utils.startTime());
    HelperSQL db = new HelperSQL(context);
    contactList.clear();
    contactList.addAll(db.getAllContacts());
    Log.i(TAG, "end --- reloadContact --- : " + Utils.getTime());
  }
  
  /**
   * Initialize contact. Get all contact from contact provider database and add them into local database.
   * Add only if contact not exist.
   */
  public void initContact() {
    Log.i(TAG, "start --- initContact --- : " + Utils.startTime());
    contactList = new ArrayList<ContactRow>();
    contactList.addAll(ContactRow.fetchAllContact(context.getContentResolver()));
    HelperSQL db = new HelperSQL(context);
    db.fillContacts(contactList);
    contactListInit = true;
    Log.i(TAG, "end --- initContact --- : " + Utils.getTime());
  }
  
  /**
   * Initialize group from contact provider database.
   */
  public void initGroup() {
    Log.i(TAG, "start --- initGroup --- : " + Utils.startTime());
    groupsList = new ArrayList<GroupRow>();
    groupsList.addAll(GroupRow.fetchGroups(context.getContentResolver()));
    Log.i(TAG, "groupsList size:" + groupsList.size());
    
    for (GroupRow group : groupsList) {
      group.setSize(ContactRow.fetchGroupMembersCount(context.getContentResolver(), group.getId()));
      Log.i(TAG, "id:" + group.getId() + "group size:" + group.getSize());
    }
    HelperSQL db = new HelperSQL(context);
    db.fillGroups(groupsList);
    groupListInit = true;
    Log.i(TAG, "end --- initGroup --- : " + Utils.getTime());
  }
  
  /**
   * Init LdAP contact.
   * @param handler
   */
  public void initLDAP(Handler handler) {
    Log.i(TAG, "start --- initLDAP --- : " + Utils.startTime());
    contactListLDAP = new ArrayList<ContactRow>();
    contactListLDAP.addAll(ServerUtilities.fetchLDAPContacts(new ServerInstance(AccountData.getAccountData(context)), context, handler));
    
    Collections.sort(contactListLDAP, new ContactRowComparator());
    contactsLDAPInit = true;
    Log.i(TAG, "end --- initLDAP --- : " + Utils.getTime());
  }
  
  public void reloadGroup() {
    
  }

  public List<ContactRow> getContactList() {
    if (contactList == null) {
      initContact();
    }
    return contactList;
  }

  public List<GroupRow> getGroupsList() {
    if (groupsList == null) {
      initGroup();
    }
    return groupsList;
  }

  
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

  public boolean isGroupListInit() {
    return groupListInit;
  }

  public void setGroupListInit(boolean groupListInit) {
    this.groupListInit = groupListInit;
  }

  public boolean isContactListInit() {
    return contactListInit;
  }

  public void setContactListInit(boolean contactListInit) {
    this.contactListInit = contactListInit;
  }

  public boolean isContactsLDAPInit() {
    return contactsLDAPInit;
  }

  public void setContactsLDAPInit(boolean contactsLDAP) {
    this.contactsLDAPInit = contactsLDAP;
  }
  
  public List<ContactRow> getContactListLDAP() {
    return contactListLDAP;
  }
}
