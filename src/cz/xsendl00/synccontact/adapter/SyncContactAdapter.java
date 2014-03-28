package cz.xsendl00.synccontact.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.Address;
import cz.xsendl00.synccontact.client.AddressType;
import cz.xsendl00.synccontact.client.GoogleContact;
import cz.xsendl00.synccontact.client.ServerInstance;
import cz.xsendl00.synccontact.client.ServerUtilities;
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
import android.text.TextUtils;
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
			final Bundle userMapping = new Bundle();
		// person 
			userMapping.putString(Constants.CN, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.CN));
      userMapping.putString(Constants.SN, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.SN));
      userMapping.putString(Constants.USER_PASSWORD, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.USER_PASSWORD));
      userMapping.putString(Constants.TELEPHONE_NUMBER, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.TELEPHONE_NUMBER));
      userMapping.putString(Constants.SEE_ALSO, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.SEE_ALSO));
      userMapping.putString(Constants.DESCRIPTION, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.DESCRIPTION));
   // OrganizationalPerson
      userMapping.putString(Constants.TITLE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.TITLE));
      userMapping.putString(Constants.X12_ADDRESS, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.X12_ADDRESS));
      userMapping.putString(Constants.REGISTRED_ADDRESS, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.REGISTRED_ADDRESS));
      userMapping.putString(Constants.DESTINATION_INDICATOR, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.DESTINATION_INDICATOR));
      userMapping.putString(Constants.INTERNATIONAL_SDN_NUMBER, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.INTERNATIONAL_SDN_NUMBER));
      userMapping.putString(Constants.FASCIMILE_TELEPHONE_NUMBER, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.FASCIMILE_TELEPHONE_NUMBER));
      userMapping.putString(Constants.PREFERRED_DELIVERY_METHOD, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.PREFERRED_DELIVERY_METHOD));
      userMapping.putString(Constants.TELEX_NUMBER, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.TELEX_NUMBER));
      userMapping.putString(Constants.PHYSICAL_DELIVERY_OFFICE_NAME, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.PHYSICAL_DELIVERY_OFFICE_NAME));
      userMapping.putString(Constants.OU, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.OU));
      userMapping.putString(Constants.ST, accountManager.getUserData(account,Constants.PAR_MAPPING + Constants.ST));
      userMapping.putString(Constants.L, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.L));
   // InetOrgPerson
      userMapping.putString(Constants.AUDIO, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.AUDIO));
      userMapping.putString(Constants.BUSSINES_CATEGORY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.BUSSINES_CATEGORY));
      userMapping.putString(Constants.CAR_LICENCE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.CAR_LICENCE));
      userMapping.putString(Constants.DEPARTMENT_NUMBER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.DEPARTMENT_NUMBER));
      userMapping.putString(Constants.DISPLAY_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.DISPLAY_NAME));
      userMapping.putString(Constants.EMPLOYEE_NUMBER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.EMPLOYEE_NUMBER));
      userMapping.putString(Constants.EMPLOYEE_TYPE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.EMPLOYEE_TYPE));
      userMapping.putString(Constants.GIVEN_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.GIVEN_NAME));
      userMapping.putString(Constants.HOME_PHONE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_PHONE));
      userMapping.putString(Constants.HOME_POSTAL_ADDRESS, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_POSTAL_ADDRESS));
      userMapping.putString(Constants.INITIALS, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.INITIALS));
      userMapping.putString(Constants.JPEG_PHOTO, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.JPEG_PHOTO));
      userMapping.putString(Constants.LABELED_URI, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.LABELED_URI));
      userMapping.putString(Constants.MAIL, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.MAIL));
      userMapping.putString(Constants.MANAGER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.MANAGER));
      userMapping.putString(Constants.MOBILE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.MOBILE));
      userMapping.putString(Constants.O, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.O));
      userMapping.putString(Constants.PAGER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.PAGER));
      userMapping.putString(Constants.PHOTO, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.PHOTO));
      userMapping.putString(Constants.ROOM_NUMBER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.ROOM_NUMBER));
      userMapping.putString(Constants.SECRETARY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.SECRETARY));
      userMapping.putString(Constants.UID, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.UID));
      userMapping.putString(Constants.USER_CERTIFICATE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.USER_CERTIFICATE));
      userMapping.putString(Constants.X500_UNIQUE_IDENTIFIER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.X500_UNIQUE_IDENTIFIER));
      userMapping.putString(Constants.PREFERRED_LANGUAGE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.PREFERRED_LANGUAGE));
      userMapping.putString(Constants.USER_SMIME_CERTIFICATE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.USER_SMIME_CERTIFICATE));
      userMapping.putString(Constants.USER_PKCS12, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.USER_PKCS12));
      userMapping.putString(Constants.GIVEN_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.GIVEN_NAME));
    //GoogleContatc
      userMapping.putString(Constants.ADDITIONAL_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.ADDITIONAL_NAME));
      userMapping.putString(Constants.NAME_PREFIX, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.NAME_PREFIX));
      userMapping.putString(Constants.NAME_SUFFIX, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.NAME_SUFFIX));
      userMapping.putString(Constants.NICKNAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.NICKNAME));
      userMapping.putString(Constants.SHORT_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.SHORT_NAME));
      userMapping.putString(Constants.MAIDEN_NAME, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.MAIDEN_NAME));
      userMapping.putString(Constants.GENDER, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.GENDER));
      userMapping.putString(Constants.NOTES, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.NOTES));
      userMapping.putString(Constants.HOME_MAIL, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_MAIL));
      userMapping.putString(Constants.WORK_MAIL, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_MAIL));
      userMapping.putString(Constants.WORK_PHONE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_PHONE));
      userMapping.putString(Constants.WEBSITE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WEBSITE));
      //GoogleContact - Home address
      userMapping.putString(Constants.HOME_CITY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_CITY));
      userMapping.putString(Constants.HOME_COUNTRY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_COUNTRY));
      userMapping.putString(Constants.HOME_EXTENDED_ADDRESS, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_EXTENDED_ADDRESS));
      userMapping.putString(Constants.HOME_POBOX, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_POBOX));
      userMapping.putString(Constants.HOME_REGION, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_REGION));
      userMapping.putString(Constants.HOME_STREET, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_STREET));
      userMapping.putString(Constants.HOME_POSTAL_CODE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.HOME_POSTAL_CODE));
      //GoogleContact - Work address
      userMapping.putString(Constants.WORK_CITY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_CITY));
      userMapping.putString(Constants.WORK_COUNTRY, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_COUNTRY));
      userMapping.putString(Constants.WORK_EXTENDED_ADDRESS, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_EXTENDED_ADDRESS));
      userMapping.putString(Constants.WORK_POBOX, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_POBOX));
      userMapping.putString(Constants.WORK_REGION, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_REGION));
      userMapping.putString(Constants.WORK_STREET, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_STREET));
      userMapping.putString(Constants.WORK_POSTAL_CODE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.WORK_POSTAL_CODE));
      //GoogleContact - postal address
      userMapping.putString(Constants.POST_OFFICE_BOX, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.POST_OFFICE_BOX));
      userMapping.putString(Constants.POSTAL_CODE, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.POSTAL_CODE));
      userMapping.putString(Constants.POSTAL_ADDRESS, accountManager.getUserData(account, Constants.PAR_MAPPING + Constants.POSTAL_ADDRESS));
			
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
