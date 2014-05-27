package cz.xsendl00.synccontact.adapter;

import com.unboundid.ldap.sdk.LDAPException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.Synchronization;
import cz.xsendl00.synccontact.utils.Constants;


public class SyncContactAdapter extends AbstractThreadedSyncAdapter {

  private static final String TAG = "SyncContactAdapter";

  private final AccountManager accountManager;
  private final Context context;
  private final Handler handler = new Handler();

  public SyncContactAdapter(Context context, boolean autoInitialize) {
    super(context, autoInitialize);
    this.context = context;
    accountManager = AccountManager.get(context);
  }

  @Override
  public void onPerformSync(Account account,
      Bundle extras,
      String authority,
      ContentProviderClient provider,
      SyncResult syncResult) {

    Log.d(TAG, "Start the sync.");
    ContactManager contactManager = ContactManager.getInstance(context);
    if (!contactManager.isOnlyWifiInit()) {
      SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, context.MODE_PRIVATE);
      contactManager.setOnlyWifi(settings.getBoolean(Constants.PREFS_WIFI, false));
      contactManager.setOnlyWifiInit(true);
    }
    if (contactManager.isOnlyWifi()) {
      if (!contactManager.checkInternet()) {
        Log.i(TAG, "neni wifi");
      } else {
        ServerInstance ldapServer = new ServerInstance(AccountData.getAccountData(context));
        //syncResult.
        try {
          new Synchronization().synchronization(ldapServer, context, handler);
        } catch (RemoteException e) {
          e.printStackTrace();
        } catch (OperationApplicationException e) {
          e.printStackTrace();
        } catch (LDAPException e) {
          e.printStackTrace();
        }
      }
    } else {
      ServerInstance ldapServer = new ServerInstance(AccountData.getAccountData(context));
      //syncResult.
      try {
        new Synchronization().synchronization(ldapServer, context, handler);
      } catch (RemoteException e) {
        e.printStackTrace();
      } catch (OperationApplicationException e) {
        e.printStackTrace();
      } catch (LDAPException e) {
        e.printStackTrace();
      }
    }


      Log.d(TAG, "Calling contactManager's sync contacts");

  }
}
