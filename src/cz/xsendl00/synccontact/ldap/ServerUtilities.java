
package cz.xsendl00.synccontact.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.androidannotations.annotations.EBean;
import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ModifyRequest;
import com.unboundid.ldap.sdk.RootDSE;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import cz.xsendl00.synccontact.AddServerActivity;
import cz.xsendl00.synccontact.InfoLDAPActivity_;
import cz.xsendl00.synccontact.R;
import cz.xsendl00.synccontact.ServerActivity;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.Mapping;

@EBean
public class ServerUtilities {

  //@Bean
  //protected Mapping mapping;

  private static final String TAG = "ServerUtilities";

  /**
   * New Thread.
   *
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
   *
   * @param baseDNs
   * @param result
   * @param handler
   * @param context
   * @param message
   */
  private static void sendResult(final String[] baseDNs,
      final Boolean result,
      final Handler handler,
      final Context context,
      final String message,
      final boolean test) {
    if (handler == null || context == null) {
      return;
    }
    handler.post(new Runnable() {
      @Override
      public void run() {
        if (test) {
          ((ServerActivity) context).onAuthenticationResult(baseDNs, result, message);
        } else {
          ((AddServerActivity) context).onAuthenticationResult(baseDNs, result, message);
        }
      }
    });
  }


  /**
   * Add a contact to LDAP.
   * @param ldapServer server instance
   * @param context context
   * @param googleContact contact
   */
  public void addContactToServer(final ServerInstance ldapServer, final Context context, GoogleContact googleContact) {
    LDAPConnection connection = null;

    try {
      connection = ldapServer.getConnection(null, context);
    } catch (LDAPException e1) {
      e1.printStackTrace();
    }
    try {
      AddRequest addRequest = new Mapping().mappingAddRequest(googleContact, ldapServer.getAccountdData().getBaseDn());
      if (addRequest != null) {
        LDAPResult ldapResult = connection.add(addRequest);
        Log.i(TAG, "Add to server : " + googleContact.getUuid() + "---" + ldapResult.toString());
        notification(context, googleContact.getUuid());
      }
    } catch (LDAPException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add new group to server.
   * @param ldapServer server instance
   * @param context context
   * @param group group for add
   */
  public void addGroup2Server(final ServerInstance ldapServer, final Context context, GroupRow group) {
    LDAPConnection connection = null;

    try {
      connection = ldapServer.getConnection(null, context);
    } catch (LDAPException e1) {
      e1.printStackTrace();
    }

    try {
      AddRequest addRequest = new Mapping().createGroupAddRequest(group, ldapServer.getAccountdData().getBaseDn(), context);
      if (addRequest != null) {
        LDAPResult ldapResult = connection.add(addRequest);
        Log.i(TAG, "Add group to server : " + group.getUuid() + "---" + ldapResult.toString());
        notification(context, group.getUuid());
      }
    } catch (LDAPException e) {
      e.printStackTrace();
    }
  }


  @SuppressLint("NewApi")
  public void notification(Context context, String text) {
    Notification noti = new Notification.Builder(context)
    .setContentTitle("New :" + text)
    .setContentText("aa")
    .setSmallIcon(R.drawable.ic_action_reconect)
    .build();
    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    int icon = R.drawable.ic_launcher;
    CharSequence tickerText = "Error on LDAP Sync";
    Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
    Intent notificationIntent = new Intent(context, InfoLDAPActivity_.class);
    PendingIntent contentIntent = PendingIntent.getService(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    notification.setLatestEventInfo(context, tickerText, text, contentIntent);
    notification.flags = Notification.FLAG_AUTO_CANCEL;
    mNotificationManager.notify(0, notification);
  }

  /**
   * retrieves a count of entries.
   * @param ldapServer instance of server
   * @param context context
   * @return entry count number of entry
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

  /**
   * Get contact from LDAP server and import them in database.
   * @param ldapServer
   * @param context
   * @param handler
   * @param uuid The uuid of contact, which will be added to phone.
   * @return the new {@link GoogleContact} contact.
   */
  public GoogleContact fetchLDAPContact(final ServerInstance ldapServer, final Context context, final Handler handler, final String uuid) {
    GoogleContact googleContactOut = null;
    LDAPConnection connection = null;
    try {

      connection = ldapServer.getConnection(handler, context);
      String filter = "(&(objectClass=googleContact)(" + Constants.UUID + "=" + uuid + "))";
      SearchResult searchResult = connection.search("ou=users,dc=synccontact,dc=xsendl00,dc=cz", SearchScope.SUB, filter, Mapping.createAttributes());

      for (SearchResultEntry e : searchResult.getSearchEntries()) {
        Log.i(TAG, e.toString());
        final GoogleContact googleContact = Mapping.mappingContactFromLDAP(e);
        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              String id = null;
              ContentProviderResult[] contactUri = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, new GoogleContact().createOperationNew(new GoogleContact(), googleContact));
              for (ContentProviderResult a : contactUri) {
                if (id == null) {
                  id = a.uri.getLastPathSegment();
                }
                Log.i(TAG, "id:" + id);
                Log.i(TAG, "res:" + a.toString());
              }
              HelperSQL db = new HelperSQL(context);
              ContactRow contactRow = new ContactRow(id,
                  googleContact.getStructuredName().getDisplayName(), true, null, Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE, ContactRow.createTimestamp(), googleContact.getUuid());
              List<ContactRow> contacts = new ArrayList<ContactRow>();
              contacts.add(contactRow);
              db.addContacts(contacts);
            } catch (RemoteException e) {
              e.printStackTrace();
            } catch (OperationApplicationException e) {
              e.printStackTrace();
            }
          }
        }).start();
        googleContactOut = googleContact;
        break;

      }

    } catch (LDAPException e) {
     /* Log.v(TAG, "LDAPException on fetching contacts", e);
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

    return googleContactOut;
  }

  /**
   * Get all contact from server. Only display name and UUID attributes will be received.
   * @param ldapServer
   * @param context
   * @return List of ContactRow
   */
  public static List<ContactRow> fetchLDAPContactsNameUUID(final ServerInstance ldapServer, final Context context, final Handler handler) {
    List<ContactRow> contactsServer = new ArrayList<ContactRow>();
    LDAPConnection connection = null;
    try {
      connection = ldapServer.getConnection(handler, context);
      String filter = "(objectClass=googleContact)";
      SearchResult searchResult = connection.search("ou=users,dc=synccontact,dc=xsendl00,dc=cz", SearchScope.SUB,
          filter, Mapping.createAttributesSimply());
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
      String filter = "(&(objectClass=googleContact)(" + Constants.LDAP_MODIFY_TIME_STAMP + ">=" + timestamp + "))";
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



  public void updateContactsLDAP(final ServerInstance ldapServer, final Context context, Map<String, GoogleContact> differenceDirty) {
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
      String filter = "(&(objectClass=googleContact)(" + Constants.UUID + "=" + uuid + "))";
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

  public static Thread attemptAuth(final ServerInstance ldapServer, final Handler handler, final Context context, final boolean test) {
    final Runnable runnable = new Runnable() {
      @Override
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
