package cz.xsendl00.synccontact.adapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.Synchronization;


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
//    try {

      // use the account manager to request the credentials
//      AccountData accountData = new AccountData();
//      accountData.setPassword(accountManager.blockingGetAuthToken(account,
//          Constants.AUTHTOKEN_TYPE, true));
//      accountData.setHost(accountManager.getUserData(account, Constants.PAR_HOST));
//      accountData.setName(accountManager.getUserData(account, Constants.PAR_USERNAME));
//      accountData.setPort(Integer.parseInt(accountManager.getUserData(account, Constants.PAR_PORT)));
//      accountData.setEncryption(Integer.parseInt(accountManager.getUserData(account,
//          Constants.PAR_ENCRYPTION)));
//
//      ServerInstance ldapServer = new ServerInstance(accountData);

//      accountData.setSearchFilter(accountManager.getUserData(account, Constants.PAR_SEARCHFILTER));
//      accountData.setBaseDn(accountManager.getUserData(account, Constants.PAR_BASEDN));

      ServerInstance ldapServer = new ServerInstance(AccountData.getAccountData(context));
      //syncResult.
      try {
        new Synchronization().synchronization(ldapServer, context, handler);
      } catch (RemoteException e) {
        e.printStackTrace();
      } catch (OperationApplicationException e) {
        e.printStackTrace();
      }
      Log.d(TAG, "Calling contactManager's sync contacts");

//    } catch (final AuthenticatorException e) {
//      syncResult.stats.numParseExceptions++;
//      Log.e(TAG, "AuthenticatorException", e);
//    } catch (final OperationCanceledException e) {
//      Log.e(TAG, "OperationCanceledExcetpion", e);
//    } catch (final IOException e) {
//      Log.e(TAG, "IOException", e);
//      syncResult.stats.numIoExceptions++;
//    }
  }
}
