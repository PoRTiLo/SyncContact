package cz.xsendl00.synccontact.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.xsendl00.synccontact.utils.Constants;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;


public class SyncAdapter extends AbstractThreadedSyncAdapter {
	private static final String TAG = "LDAPSyncAdapter";

	private final AccountManager mAccountManager;
	private final Context mContext;

	private Date mLastUpdated;

	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContext = context;
		mAccountManager = AccountManager.get(context);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

		Log.d(TAG, "Start the sync.");
		//List<Contact> users = new ArrayList<Contact>();
		String authtoken = null;
		try {
		  
			// use the account manager to request the credentials
			authtoken = mAccountManager.blockingGetAuthToken(account, Constants.AUTHTOKEN_TYPE, true /* notifyAuthFailure */);
			/*final String host = mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_HOST);
			final String username = mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_USERNAME);
			final int port = Integer.parseInt(mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_PORT));
			final String sEnc = mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_ENCRYPTION);
			int encryption = 0;
			if (!TextUtils.isEmpty(sEnc)) {
				encryption = Integer.parseInt(sEnc);
			}
			ServerInstance ldapServer = new ServerInstance(host, port, encryption, username, authtoken);

			final String searchFilter = mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_SEARCHFILTER);
			final String baseDN = mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_BASEDN);

			// LDAP name mappings
			final Bundle mappingBundle = new Bundle();
			mappingBundle.putString(Contact.FIRSTNAME, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.FIRSTNAME));
			mappingBundle.putString(Contact.LASTNAME, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.LASTNAME));
			mappingBundle.putString(Contact.TELEPHONE, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.TELEPHONE));
			mappingBundle.putString(Contact.MOBILE, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.MOBILE));
			mappingBundle.putString(Contact.HOMEPHONE, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.HOMEPHONE));
			mappingBundle.putString(Contact.MAIL, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.MAIL));
			mappingBundle.putString(Contact.PHOTO, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.PHOTO));
			mappingBundle.putString(Contact.STREET, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.STREET));
			mappingBundle.putString(Contact.CITY, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.CITY));
			mappingBundle.putString(Contact.ZIP, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.ZIP));
			mappingBundle.putString(Contact.STATE, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.STATE));
			mappingBundle.putString(Contact.COUNTRY, mAccountManager.getUserData(account, SyncAuthenticatorActivity.PARAM_MAPPING + Contact.COUNTRY));
			users = LDAPUtilities.fetchContacts(ldapServer, baseDN, searchFilter, mappingBundle, mLastUpdated, this.getContext());
			if (users == null) {
				syncResult.stats.numIoExceptions++;
				return;
			}
			*/
			// update the last synced date.
			mLastUpdated = new Date();
			// update platform contacts.
			Log.d(TAG, "Calling contactManager's sync contacts");
		
		} catch (final AuthenticatorException e) {
			syncResult.stats.numParseExceptions++;
			Log.e(TAG, "AuthenticatorException", e);
		} catch (final OperationCanceledException e) {
			Log.e(TAG, "OperationCanceledExcetpion", e);
		} catch (final IOException e) {
			Log.e(TAG, "IOException", e);
			syncResult.stats.numIoExceptions++;
		}
	}
}
