
package cz.xsendl00.synccontact.ldap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.RootDSE;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldif.LDIFException;
import cz.xsendl00.synccontact.AddServerActivity;
import cz.xsendl00.synccontact.ServerActivity;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.Mapping;

public class ServerUtilities {
	
  private static final String TAG = "ServerUtilities";

  /**
   * New Thread.
   * @param runnable
   * @return
   */
  public static Thread performOnBackgroundThread(final Runnable runnable) {
    final Thread t = new Thread() {
      @Override
      public void run() {
        try {
          runnable.run();
        } finally {
        }
      }
    };
    t.start();
    return t;
  }

  /**
	 * Send result, about connection to server, to activity. 
	 * @param baseDNs
	 * @param result
	 * @param handler
	 * @param context
	 * @param message
	 */
  private static void sendResult(final String[] baseDNs, final Boolean result, final Handler handler, final Context context, final String message, final boolean test) {
    if (handler == null || context == null) {
      return;
    }
    handler.post(new Runnable() {
      public void run() {
        if (test) {
          ((ServerActivity) context).onAuthenticationResult(baseDNs, result, message);
        } else {
          ((AddServerActivity) context).onAuthenticationResult(baseDNs, result, message);
        }
      }
    });
}
  
  private static AddRequest createContactRequest(final AccountData data) {
    try {
      AddRequest addRequest = new AddRequest(
          Constants.DN + ": " + "cn=pokus,ou=users," + data.getBaseDn(),
          Constants.OBJECT_CLASS_GOOGLE,
          Constants.OBJECT_CLASS_INET,
          Constants.OBJECT_CLASS_ORG,
          Constants.OBJECT_CLASS_PERSON,
          Constants.OBJECT_CLASS_TOP,
          "cn: aaa",
          "sn: bbb");
      
      
      Log.i(TAG, addRequest.toLDIFString());
      return addRequest;
    } catch (LDIFException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
    
  }
  
  
  
  public static void addContactsToServer(final ServerInstance ldapServer, Handler handler, final Context context) {
 // get sync user
    HelperSQL db = new HelperSQL(context);
    List<ContactRow> contactsId = db.getSyncContacts();
 // get timestamp last synchronization
    //String timestamp = db.newerTimestamp();
    // get contact from server which was newer than timestamp
    //List<GoogleContact> contactsServer = fetchModifyContacts(ldapServer, context, timestamp);
    
    LDAPConnection connection = null;
    
      try {
        connection = ldapServer.getConnection(handler, context);
      } catch (LDAPException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      for(ContactRow con : contactsId) {
        try {
        connection.add(Mapping.mappingRequest(context.getContentResolver(), String.valueOf(con.getIdTable()), 
            ldapServer.getAccountdData().getBaseDn(), con.getUuid()));
        } catch (LDAPException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    
  }

  public static void updateContacts(final ServerInstance ldapServer, final AccountData accountData, Handler handler, final Context context) {
    
    LDAPConnection connection = null;
    Log.i(TAG, "base Dn:" + accountData.getBaseDn() + ", tadddddddddddddddyyyyyy");
    try {
      connection = ldapServer.getConnection(null, null);
      connection.add(createContactRequest(accountData));
    } catch (LDAPException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static String fetchModifyTimestamp(final ServerInstance ldapServer, final Context context) {
    LDAPConnection connection = null;
    try {
      connection = ldapServer.getConnection(null, context);
      String filter = "(&(objectClass=googleContact)(" + Constants.LDAP_MODIFY_TIME_STAMP + ":generalizedTimeMatch:=20140328193405Z))";
      //Log.i(TAG, filter);
      SearchResult searchResult = connection.search(
          "ou=users,dc=synccontact,dc=xsendl00,dc=cz", SearchScope.SUB, 
          filter,
          Constants.LDAP_MODIFY_TIME_STAMP);
      //Log.i(TAG, searchResult.getEntryCount() + " entries returned.");
      for (SearchResultEntry e : searchResult.getSearchEntries()) {
        //Log.i(TAG, e.getAttributeValue(Constants.LDAP_MODIFY_TIME_STAMP));
        return e.getAttributeValue(Constants.LDAP_MODIFY_TIME_STAMP);
      }
    } catch (LDAPException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
  public static Map<String, GoogleContact> fetchModifyContactsLDAP(final ServerInstance ldapServer, final Context context, String timestamp) {
    LDAPConnection connection = null;
    Map<String, GoogleContact> contactsServer = new HashMap<String, GoogleContact>();
    try {
      connection = ldapServer.getConnection(null, context);
      String filter = "(&(objectClass=googleContact)(" + Constants.LDAP_MODIFY_TIME_STAMP + ">="+timestamp+"))";
      SearchResult searchResult = connection.search(
          "ou=users,dc=synccontact,dc=xsendl00,dc=cz", SearchScope.SUB, 
          filter,
          Mapping.createAttributes());
      for (SearchResultEntry e : searchResult.getSearchEntries()) {
        Log.i(TAG, e.getAttributeValue(Constants.UUID) + ", has att:" + e.hasAttribute(Constants.UUID));
        contactsServer.put(e.getAttributeValue(Constants.UUID), Mapping.mappingContactFromLDAP(e));
      }
    } catch (LDAPException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
    return contactsServer;
  }
  
  public static void synchronization(final ServerInstance ldapServer, final Context context) throws RemoteException, OperationApplicationException {
    // get sync user
    HelperSQL db = new HelperSQL(context);
    List<ContactRow> contactsId = db.getSyncContacts();
    Log.i(TAG, "contactsId: " + contactsId.size());
    // get contact with dirty flag
    Map<String, GoogleContact> contactsDirty = Mapping.fetchDirtyContacts(context, contactsId);
    Log.i(TAG, "fetchDirtyContacts: " + contactsDirty.size());
    //for(GoogleContact c : contactsDirty) {
    //  Log.i(TAG, c.getStructuredName().getDisplayName());
    //}
    // get timestamp last synchronization
    String timestamp = db.newerTimestamp();
    Log.i(TAG, timestamp);
    // get contact from server which was newer than timestamp
    Map<String, GoogleContact> contactsServer = fetchModifyContactsLDAP(ldapServer, context, timestamp);
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
    updateContactsLDAP(ldapServer, context, differenceLDAP);//differenceDirty
    // set new timestamp
    timestamp = ContactRow.createTimestamp();
    // set dirty flag to disable (0)
    db.updateContacts(differenceDirty, timestamp);
  }
  
  private static void updateContactsLDAP(final ServerInstance ldapServer, final Context context, Map<String, GoogleContact> differenceDirty) {
    for (Map.Entry<String, GoogleContact> entry : differenceDirty.entrySet()) {
      if (checkExistsContactLDAP(ldapServer, context, entry.getKey())) {
        // update
        updateContactLDAP(ldapServer, context, entry.getValue());
        Log.i(TAG, "update contact: " + entry.getKey());
      } else {
        // create new
        Log.i(TAG, "create new contact: " + entry.getKey());
      }
    }
  }
  
  private static void updateContactLDAP(final ServerInstance ldapServer, Context context, GoogleContact gc) {
    LDAPConnection connection = null;
    
    try {
      connection = ldapServer.getConnection(null, context);
    } catch (LDAPException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    try {
      connection.modify(Mapping.mappingRequest(gc, ldapServer.getAccountdData().getBaseDn()));
    } catch (LDAPException e) {
       // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  public static <K, V> Map<K, V> intersection(final Map<K, V> map1, final Map<K, V> map2) {
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
  
  public static boolean checkExistsContactLDAP(final ServerInstance ldapServer, final Context context, String uuid) {
    LDAPConnection connection = null;
    boolean  found = false;
    try {
      connection = ldapServer.getConnection(null, context);
      String filter = "(&(objectClass=googleContact)(" + Constants.UUID+ "=" + uuid + "))";
      SearchResult searchResult = connection.search(
          "ou=users,dc=synccontact,dc=xsendl00,dc=cz", SearchScope.SUB, 
          filter,
          Constants.UUID);
      for (SearchResultEntry e : searchResult.getSearchEntries()) {
        if (e.hasAttribute(Constants.UUID)) {
          found = true;
          break;
        }
      }
    } catch (LDAPException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
    return found;
    
  }
  
  public static <K, V> Map<K, V> difference(final Map<K, V> map1, final Map<K, V> map2) {
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
  
	public static List<GoogleContact> fetchContacts(final ServerInstance ldapServer, final AccountData accountData, final Bundle mappingBundle, 
	    final Date lastUpdated, final Context context) {
		final ArrayList<GoogleContact> friendList = new ArrayList<GoogleContact>();
		LDAPConnection connection = null;
		try {
		  Log.i(TAG, "base Dn:" + accountData.getBaseDn());
			connection = ldapServer.getConnection(null, context);
			
			SearchResult searchResult = connection.search(accountData.getBaseDn(), SearchScope.SUB, accountData.getSearchFilter(), getUsedAttributes(mappingBundle));
			Log.i(TAG, searchResult.getEntryCount() + " entries returned.");

			for (SearchResultEntry e : searchResult.getSearchEntries()) {
			  Log.i(TAG, e.toString());
				GoogleContact u = Mapping.mappingContactFromLDAP(e);
				friendList.add(u);
			}
		} catch (LDAPException e) {
			/*Log.v(TAG, "LDAPException on fetching contacts", e);
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			int icon = R.drawable.ic_launcher;
			CharSequence tickerText = "Error on LDAP Sync";
			Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
			Intent notificationIntent = new Intent(context, SyncService.class);
			PendingIntent contentIntent = PendingIntent.getService(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			notification.setLatestEventInfo(context, tickerText, e.getMessage().replace("\\n", " "), contentIntent);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(0, notification);*/
			return null;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return friendList;
	}

	private static String[] getUsedAttributes(Bundle mappingBundle) {
		ArrayList<String> ldapAttributes = new ArrayList<String>();
		String[] ldapArray = new String[mappingBundle.size()];
		for (String key : mappingBundle.keySet()) {
			ldapAttributes.add(mappingBundle.getString(key));
		}
		ldapArray = ldapAttributes.toArray(ldapArray);
		return ldapArray;
	}


  public static Thread attemptAuth(final ServerInstance ldapServer, final Handler handler, final Context context, final boolean test) {
    final Runnable runnable = new Runnable() {
      public void run() {
        authenticate(ldapServer, handler, context, test);
      }
    };
    // run on background thread.
    return ServerUtilities.performOnBackgroundThread(runnable);
  }


  public static boolean authenticate(ServerInstance ldapServer, Handler handler, final Context context, final boolean test) {
    LDAPConnection connection = null;
    try {
      connection = ldapServer.getConnection(handler, context);
      if (connection != null) {
        RootDSE s = connection.getRootDSE();
        String[] baseDNs = null;
        if (s != null) {
          baseDNs = s.getNamingContextDNs();
        }
        sendResult(baseDNs, true, handler, context, null, test);
        return true;
      }
    } catch (LDAPException e) {
      Log.e(TAG, "Error authenticating", e);
      sendResult(null, false, handler, context, e.getMessage(), test);
      return false;
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
    return false;
  }
}
