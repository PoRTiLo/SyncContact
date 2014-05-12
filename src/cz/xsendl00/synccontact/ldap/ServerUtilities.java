
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
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ModifyRequest;
import com.unboundid.ldap.sdk.RootDSE;
import com.unboundid.ldap.sdk.SearchRequest;
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
  
  public static void addContactToServer(final ServerInstance ldapServer, final Context context, GoogleContact gc) {
    LDAPConnection connection = null;
    
    try {
      connection = ldapServer.getConnection(null, context);
    } catch (LDAPException e1) {
      e1.printStackTrace();
    }
    try {
      AddRequest addRequest = Mapping.mappingAddRequest(gc, ldapServer.getAccountdData().getBaseDn());
      if (addRequest != null) {
        connection.add(addRequest);
      }
    } catch (LDAPException e) {
      e.printStackTrace();
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
  
  
  /**
   * retrieves a count of entries
   * @param ldapServer 
   * @param context 
   * @return entry count
   */
  public static int getEntryCount(final ServerInstance ldapServer, final Context context) {
    LDAPConnection connection = null;
    int size = 0;
    try {
      connection = ldapServer.getConnection(null, context);
      String filter = "(objectClass=googleContact)";
      size = connection.search("ou=users,dc=synccontact,dc=xsendl00,dc=cz", SearchScope.SUB, filter,
          Constants.UUID).getEntryCount();
    } catch (LDAPException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
   
    return size;
  }
  
  public GoogleContact fetchLDAPContact(final ServerInstance ldapServer, final Context context, final Handler handler, final String uuid) {
    GoogleContact googleContact = null;
    LDAPConnection connection = null;
    try {
      
      connection = ldapServer.getConnection(handler, context);
      String filter = "(&(objectClass=googleContact)(" + Constants.UUID + "=" + uuid + "))";
      SearchResult searchResult = connection.search("ou=users,dc=synccontact,dc=xsendl00,dc=cz", SearchScope.SUB, filter, Mapping.createAttributes());
      
      for (SearchResultEntry e : searchResult.getSearchEntries()) {
        Log.i(TAG, e.toString());
        googleContact = Mapping.mappingContactFromLDAP(e);
      }
      new Thread(new Runnable() {
        public void run() {
          googleContact.
        }
      }).start();
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

    return googleContact;
  }
  
  /**
   * Get all contact from server. Only display name and UUID attributes will be received. 
   * @param ldapServer
   * @param context
   * @return List of ContactRow
   */
  public static List<ContactRow> fetchLDAPContacts(final ServerInstance ldapServer, final Context context, final Handler handler) {
    List<ContactRow> contactsServer = new ArrayList<ContactRow>();
    LDAPConnection connection = null;
    try {
      connection = ldapServer.getConnection(handler, context);
      String filter = "(objectClass=googleContact)";
      SearchResult searchResult = connection.search("ou=users,dc=synccontact,dc=xsendl00,dc=cz", SearchScope.SUB, filter,
          Mapping.createAttributesSimply());
      for (SearchResultEntry e : searchResult.getSearchEntries()) {
        ContactRow contactRow = new ContactRow();
        contactRow.setUuid(e.getAttributeValue(Constants.UUID));
        contactRow.setName(e.getAttributeValue(Constants.DISPLAY_NAME));
        contactsServer.add(contactRow);
      }
    } catch (LDAPException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
    return contactsServer;
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
      e.printStackTrace();
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
    return contactsServer;
  }
  
  
  
  public static void updateContactsLDAP(final ServerInstance ldapServer, final Context context, Map<String, GoogleContact> differenceDirty) {
    for (Map.Entry<String, GoogleContact> entry : differenceDirty.entrySet()) {
      if (checkExistsContactLDAP(ldapServer, context, entry.getKey())) {
        // update
        updateContactLDAP(ldapServer, context, entry.getValue());
        Log.i(TAG, "update contact: " + entry.getKey());
      } else {
        // create new
        addContactToServer(ldapServer, context, entry.getValue());
        Log.i(TAG, "create new contact: " + entry.getKey());
      }
    }
  }
  
  private static void updateContactLDAP(final ServerInstance ldapServer, Context context, GoogleContact gc) {
    LDAPConnection connection = null;
    
    try {
      connection = ldapServer.getConnection(null, context);
    } catch (LDAPException e1) {
      e1.printStackTrace();
    }
    try {
      ModifyRequest modifyRequest = Mapping.mappingRequest(gc, ldapServer.getAccountdData().getBaseDn());
      if (modifyRequest != null) {
        LDAPResult result = connection.modify(modifyRequest);
        //result.getDiagnosticMessage()
      }
    } catch (LDAPException e) {
      e.printStackTrace();
    }
    
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
