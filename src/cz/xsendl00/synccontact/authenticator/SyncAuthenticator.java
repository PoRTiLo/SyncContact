package cz.xsendl00.synccontact.authenticator;

import java.util.List;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import cz.xsendl00.synccontact.ServerAddActivity;
import cz.xsendl00.synccontact.ServerAddActivity_;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;

public class SyncAuthenticator extends AbstractAccountAuthenticator {

  private final Context mContext;
  private static final String TAG = "SyncAuthenticator";
  private volatile Looper mMyLooper;

  public SyncAuthenticator(Context context) {
    super(context);
    mContext = context;
  }

  @Override
  public Bundle addAccount(AccountAuthenticatorResponse response,
      String accountType,
      String authTokenType,
      String[] requiredFeatures,
      Bundle options) throws NetworkErrorException {
    Log.i(TAG, "addAccount()");

    final AccountManager accountManager = AccountManager.get(mContext);
    if (accountManager.getAccountsByType(accountType).length == 0) {
      final Intent intent = new Intent(mContext, ServerAddActivity_.class);
      intent.putExtra(Constants.PAR_ACCOUNT_TYPE, accountType);
      intent.putExtra(Constants.PAR_AUTHTOKEN_TYPE, authTokenType);
      intent.putExtra(Constants.PAR_IS_ADDING_NEW_ACCOUNT, true);
      intent.putExtra(Constants.INTENT_FIRST, true);
      intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
      final Bundle bundle = new Bundle();
      bundle.putParcelable(AccountManager.KEY_INTENT, intent);
      return bundle;
    }
    final Bundle bundle = new Bundle();
    bundle.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_BAD_REQUEST);
    bundle.putString(AccountManager.KEY_ERROR_MESSAGE, smsg);
    handler.sendEmptyMessage(0);
    return bundle;
  }

  static final String smsg = "SyncContact account already exists.\nOnly one account is supported.";
  final Handler handler = new Handler() {

    @Override
    public void handleMessage(android.os.Message msg) {
      if (msg.what == 0) {
        Toast.makeText(mContext, smsg, Toast.LENGTH_LONG).show();
      }
    }
  };


  @Override
  public Bundle confirmCredentials(AccountAuthenticatorResponse response,
      Account account,
      Bundle options) throws NetworkErrorException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bundle getAuthToken(AccountAuthenticatorResponse response,
      Account account,
      String authTokenType,
      Bundle options) throws NetworkErrorException {
    Log.i(TAG, authTokenType);
    if (!authTokenType.equals(Constants.AUTHTOKEN_TYPE)) {
      final Bundle result = new Bundle();
      result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
      return result;
    }


    // Extract the username and password from the Account Manager, and ask
    // the server for an appropriate AuthToken.
    final AccountManager am = AccountManager.get(mContext);

    final AccountData ad = new AccountData();
    ad.setPassword(am.getPassword(account));
    ad.setHost(am.getUserData(account, Constants.PAR_HOST));
    ad.setName(am.getUserData(account, Constants.PAR_USERNAME));
    ad.setPort(Integer.parseInt(am.getUserData(account, Constants.PAR_PORT)));
    ad.setEncryption(Integer.parseInt(am.getUserData(account, Constants.PAR_ENCRYPTION)));
    if (ad.getPassword() != null) {
      if (ServerUtilities.authenticate(new ServerInstance(ad), null, mContext, false)) {
        final Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        result.putString(AccountManager.KEY_AUTHTOKEN, ad.getPassword());
        return result;
      }

    }

    // If we get here, then we couldn't access the user's password - so we
    // need to re-prompt them for their credentials. We do that by creating
    // an intent to display our AuthenticatorActivity.
    final Intent intent = new Intent(mContext, ServerAddActivity.class);
    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
    intent.putExtra(Constants.PAR_USERNAME, ad.getName());
    intent.putExtra(Constants.PAR_AUTHTOKEN_TYPE, authTokenType);
    final Bundle bundle = new Bundle();
    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
    return bundle;
  }

  @Override
  public String getAuthTokenLabel(String authTokenType) {
    Log.i(TAG, "getAuthTokenLabel()");
    return null;
  }

  @Override
  public Bundle hasFeatures(AccountAuthenticatorResponse response,
      Account account,
      String[] features) throws NetworkErrorException {
    final Bundle result = new Bundle();
    result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Bundle updateCredentials(AccountAuthenticatorResponse response,
      Account account,
      String authTokenType,
      Bundle options) throws NetworkErrorException {
    final Intent intent = new Intent(mContext, ServerAddActivity.class);
    intent.putExtra(Constants.PAR_USERNAME, account.name);
    intent.putExtra(Constants.PAR_AUTHTOKEN_TYPE, authTokenType);
    intent.putExtra(Constants.PAR_CONFIRMCREDENTIALS, false);
    final Bundle bundle = new Bundle();
    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
    return bundle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account)
      throws NetworkErrorException {
    Log.i(TAG, "remove account" + account.type);
    Bundle ret = super.getAccountRemovalAllowed(response, account);
    if (ret.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)) {
      new Thread(new Runnable() {

        @Override
        public void run() {
          HelperSQL db = new HelperSQL(mContext);
          List<ContactRow> contactRows = db.getAllContacts();
          new AndroidDB().exportContactsFromSyncAccount(mContext, contactRows);
        }
      }).start();
    }
    return ret;
  }
}
