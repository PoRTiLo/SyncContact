package cz.xsendl00.synccontact.authenticator;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import cz.xsendl00.synccontact.utils.Constants;

public class AccountData {

  private String name;
  private String password;
  private Integer port;
  private String host;
  private String baseDn;
  private Integer encryption = 0;
  private boolean newAccount = true;
  private String searchFilter = "(objectClass=googleContact)";

  public AccountData() {
    this(null, null, null, null, null, 0, true);
  }

  public AccountData(String name,
      String password,
      Integer port,
      String host,
      String baseDn,
      Integer encryption,
      boolean newAccount) {
    this.name = name;
    this.port = port;
    this.host = host;
    this.baseDn = baseDn;
    this.password = password;
    this.encryption = encryption;
    this.newAccount = newAccount;
  }

  public AccountData(AccountData aData) {
    this(aData.getName(), aData.getPassword(), aData.getPort(), aData.getHost(), aData.getBaseDn(),
        aData.getEncryption(), aData.isNewAccount());
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPassword(String pass) {
    this.password = pass;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEncryption(Integer enc) {
    this.encryption = enc;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public void setBaseDNs(String[] baseDNs) {
    this.baseDn = "";
    for (String str : baseDNs) {
      this.baseDn += str;
    }
  }

  public String getBaseDn() {
    return baseDn;
  }

  public void setBaseDn(String base) {
    this.baseDn = base;
  }

  public Integer getPort() {
    return port;
  }

  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }

  public Integer getEncryption() {
    return encryption;
  }

  public boolean isNewAccount() {
    return newAccount;
  }

  public void setNewAccount(boolean newAccount) {
    this.newAccount = newAccount;
  }

  public String getSearchFilter() {
    return searchFilter;
  }

  public void setSearchFilter(String searchFilter) {
    this.searchFilter = searchFilter;
  }

  @Override
  public String toString() {
    return "name: " + name + ", password: " + password + ", port: " + port + ", host: " + host
        + ", baseDn: " + baseDn + ", encryption: " + encryption + ", newAccount: " + newAccount
        + ", filter: " + searchFilter;
  }

  public static AccountData getAccountData(Context context) {

    AccountData accountData = new AccountData();
    AccountManager manager = AccountManager.get(context);
    Account[] accounts = manager.getAccountsByType(Constants.ACCOUNT_TYPE);
    // TODO : mel by byt jen jeden, ale rasdsi poresit
    for (Account account : accounts) {
      try {
        accountData.setPassword(manager.blockingGetAuthToken(account, Constants.AUTHTOKEN_TYPE,
            true));
        accountData.setHost(manager.getUserData(account, Constants.PAR_HOST));
        accountData.setName(manager.getUserData(account, Constants.PAR_USERNAME));
        accountData.setSearchFilter(manager.getUserData(account, Constants.PAR_SEARCHFILTER));
        accountData.setBaseDn(manager.getUserData(account, Constants.PAR_BASEDN));
        accountData.setPort(Integer.parseInt(manager.getUserData(account, Constants.PAR_PORT)));
        accountData.setEncryption(Integer.parseInt(manager.getUserData(account,
            Constants.PAR_ENCRYPTION)));
        accountData.setNewAccount(false);
        return accountData;
      } catch (OperationCanceledException e) {
        e.printStackTrace();
      } catch (AuthenticatorException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    Log.i("AccountDATA", "Account is empty");
    return null;

  }

  public static Bundle toBundle(final AccountData ac) {
    Bundle userData = new Bundle();
    userData.putString(Constants.PAR_USERNAME, ac.getName());
    userData.putString(Constants.PAR_PORT, ac.getPort() + "");
    userData.putString(Constants.PAR_HOST, ac.getHost());
    userData.putString(Constants.PAR_ENCRYPTION, ac.getEncryption() + "");
    userData.putString(Constants.PAR_SEARCHFILTER, ac.getSearchFilter());
    userData.putString(Constants.PAR_BASEDN, ac.getBaseDn());
    return userData;
  }
}
