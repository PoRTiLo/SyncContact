package cz.xsendl00.synccontact.authenticator;

import cz.xsendl00.synccontact.AddServerActivity;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SyncAuthenticator extends AbstractAccountAuthenticator {

  private final Context mContext;
  private static final String TAG = "SyncAuthenticator";
  
  public SyncAuthenticator(Context context) {
    super(context);
    mContext = context;
  }

  @Override
  public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
    Log.i(TAG, "addAccount()");
    final Intent intent = new Intent(mContext, AddServerActivity.class);
    
    intent.putExtra(Constants.PAR_ACCOUNT_TYPE, accountType);
    intent.putExtra(Constants.PAR_AUTHTOKEN_TYPE, authTokenType);
    intent.putExtra(Constants.PAR_IS_ADDING_NEW_ACCOUNT, true);
    
    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
    final Bundle bundle = new Bundle();
    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
    
    return bundle;
  }

  @Override
  public Bundle confirmCredentials(AccountAuthenticatorResponse response,
      Account account, Bundle options) throws NetworkErrorException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Bundle editProperties(AccountAuthenticatorResponse response,
      String accountType) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
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
      if (ServerUtilities.authenticate(new ServerInstance(ad), null, mContext)) {
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
    final Intent intent = new Intent(mContext, AddServerActivity.class);
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
  public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
    final Bundle result = new Bundle();
    result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
    return result;
  }

  @Override
  public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
    final Intent intent = new Intent(mContext, AddServerActivity.class);
    intent.putExtra(Constants.PAR_USERNAME, account.name);
    intent.putExtra(Constants.PAR_AUTHTOKEN_TYPE, authTokenType);
    intent.putExtra(Constants.PAR_CONFIRMCREDENTIALS, false);
    final Bundle bundle = new Bundle();
    bundle.putParcelable(AccountManager.KEY_INTENT, intent);
    return bundle;
  }

}
