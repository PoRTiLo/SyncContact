package cz.xsendl00.synccontact.ldap;

import java.io.Serializable;

import javax.net.SocketFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.unboundid.ldap.sdk.ExtendedResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.extensions.StartTLSExtendedRequest;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Connection to server instance
 * @author portilo
 *
 */
public class ServerInstance implements Serializable {

  private static final String TAG = "ServerInstance";

  private static final long serialVersionUID = -7633400003887348205L;
  
  private AccountData accountData;

  public ServerInstance(final AccountData accountData) {
    this.accountData = new AccountData(accountData);
    this.accountData.setName(((accountData.getName() == null) || (accountData.getName().length() == 0) ? null : accountData.getName()));
    this.accountData.setPassword(((accountData.getPassword() == null) || (accountData.getPassword().length() == 0) ? null : accountData.getPassword()));
  }
  
  public AccountData getAccountdData() {
    return this.accountData;
  }
  
  /**
   * Create LDAP Connection.
   * @param socketFactory
   * @return LDAPConnection.
   * @throws LDAPException
   */
  private LDAPConnection createLDAPConnection(SocketFactory socketFactory) throws LDAPException {
    final LDAPConnectionOptions options = new LDAPConnectionOptions();
    options.setAutoReconnect(true);
    options.setConnectTimeoutMillis(30000);
    options.setFollowReferrals(false);
    options.setMaxMessageSize(0);
    return new LDAPConnection(socketFactory, options, accountData.getHost(), accountData.getPort());
  }
  
  /**
   * Get server connection.
   * @return
   * @throws LDAPException
   */
  public LDAPConnection getConnection(final Handler handler, final Context context) throws LDAPException {
    SocketFactory socketFactory = null;
    
    if (accountData.getEncryption() == Constants.SSL_TLS_INT) {
      Log.d(TAG, "Trying to connect with: SSL/TLS");
      final SSLUtil sslUtil = new SSLUtil(new MyTrustManager(context.getFilesDir().getPath().toString() + Constants.CERT_NAME, handler, context));
      try {
        socketFactory = sslUtil.createSSLSocketFactory();
      } catch (Exception e) {
        Log.e(TAG, "getConnection", e);
        throw new LDAPException(ResultCode.LOCAL_ERROR, "Cannot initialize SSL", e);
      }
    }

    final LDAPConnection conn = createLDAPConnection(socketFactory);

    if (accountData.getEncryption() == Constants.STARTTLS_INT) {
      Log.d(TAG, "Trying to connect with: STARTLS");
      final SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());//new MyTrustManager(context.getFilesDir().getPath().toString() + Constants.CERT_NAME, handler, context));
      try {
        final ExtendedResult r = conn.processExtendedOperation(new StartTLSExtendedRequest(sslUtil.createSSLContext()));
        if (r.getResultCode() != ResultCode.SUCCESS) {
          throw new LDAPException(r);
        }
      } catch (LDAPException le) {
        Log.e(TAG, "getConnection", le);
        conn.close();
        throw le;
      } catch (Exception e) {
        Log.e(TAG, "getConnection", e);
        conn.close();
        throw new LDAPException(ResultCode.CONNECT_ERROR, "Cannot initialize StartTLS", e);
      }
    }

    if ((accountData.getName() != null) && (accountData.getPassword() != null)) {
      try {
        conn.bind(accountData.getName(), accountData.getPassword());
      } catch (LDAPException le) {
        Log.e(TAG, "getConnection", le);
        conn.close();
        throw le;
      }
    }

    return conn;
  }
}

