package cz.xsendl00.synccontact.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.GoogleContact;
import cz.xsendl00.synccontact.client.ServerInstance;
import cz.xsendl00.synccontact.client.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.Mapping;

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


public class SyncContactAdapter extends AbstractThreadedSyncAdapter {
	private static final String TAG = "SyncContactAdapter";

	private final AccountManager accountManager;
	private final Context context;

	private Date lastUpdated;

	public SyncContactAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		this.context = context;
		accountManager = AccountManager.get(context);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

		Log.d(TAG, "Start the sync.");
		List<GoogleContact> users = new ArrayList<GoogleContact>();
		try {
		  
			// use the account manager to request the credentials
		  AccountData accountData = new AccountData();
		  accountData.setPassword(accountManager.blockingGetAuthToken(account, Constants.AUTHTOKEN_TYPE, true /* notifyAuthFailure */));
			
			accountData.setHost(accountManager.getUserData(account, Constants.PAR_HOST));
			accountData.setName(accountManager.getUserData(account, Constants.PAR_USERNAME));
			accountData.setPort(Integer.parseInt(accountManager.getUserData(account, Constants.PAR_PORT)));
			accountData.setEncryption(Integer.parseInt(accountManager.getUserData(account, Constants.PAR_ENCRYPTION)));
			
			ServerInstance ldapServer = new ServerInstance(accountData);

			accountData.setSearchFilter(accountManager.getUserData(account, Constants.PAR_SEARCHFILTER));
			accountData.setBaseDn((accountManager.getUserData(account, Constants.PAR_BASEDN)));
			
			// LDAP name mappings
			final Bundle userMapping = Mapping.mappingFrom(accountManager, account);
			
			users = ServerUtilities.fetchContacts(ldapServer, accountData, userMapping, lastUpdated, this.getContext());
			if (users == null) {
				syncResult.stats.numIoExceptions++;
				return;
			}

			// update the last synced date.
			lastUpdated = new Date();
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
