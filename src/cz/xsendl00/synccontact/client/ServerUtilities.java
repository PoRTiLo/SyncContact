
package cz.xsendl00.synccontact.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.RootDSE;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldif.LDIFException;
import com.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.AddServerActivity;
import cz.xsendl00.synccontact.adapter.SyncService;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.utils.Constants;

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
  private static void sendResult(final String[] baseDNs, final Boolean result, final Handler handler, final Context context, final String message) {
    if (handler == null || context == null) {
      return;
    }
    handler.post(new Runnable() {
      public void run() {
        ((AddServerActivity) context).onAuthenticationResult(baseDNs, result, message);
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
				GoogleContact u = GoogleContact.valueOf(e, mappingBundle);
				if (u != null) {
					friendList.add(u);
				}
			}
		} catch (LDAPException e) {
			Log.v(TAG, "LDAPException on fetching contacts", e);
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			int icon = R.drawable.ic_launcher;
			CharSequence tickerText = "Error on LDAP Sync";
			Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
			Intent notificationIntent = new Intent(context, SyncService.class);
			PendingIntent contentIntent = PendingIntent.getService(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			notification.setLatestEventInfo(context, tickerText, e.getMessage().replace("\\n", " "), contentIntent);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(0, notification);
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


  public static Thread attemptAuth(final ServerInstance ldapServer, final Handler handler, final Context context) {
    final Runnable runnable = new Runnable() {
      public void run() {
        authenticate(ldapServer, handler, context);
      }
    };
    // run on background thread.
    return ServerUtilities.performOnBackgroundThread(runnable);
  }


  public static boolean authenticate(ServerInstance ldapServer, Handler handler, final Context context) {
    LDAPConnection connection = null;
    try {
      connection = ldapServer.getConnection(handler, context);
      if (connection != null) {
        RootDSE s = connection.getRootDSE();
        String[] baseDNs = null;
        if (s != null) {
          baseDNs = s.getNamingContextDNs();
        }
        sendResult(baseDNs, true, handler, context, null);
        return true;
      }
    } catch (LDAPException e) {
      Log.e(TAG, "Error authenticating", e);
      sendResult(null, false, handler, context, e.getMessage());
      return false;
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
    return false;
  }
}
