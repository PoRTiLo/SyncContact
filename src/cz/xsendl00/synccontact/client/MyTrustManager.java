package cz.xsendl00.synccontact.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import cz.xsendl00.synccontact.utils.Constants;


import static com.unboundid.util.Debug.*;
import static com.unboundid.util.StaticUtils.*;
import static cz.xsendl00.synccontact.client.SSLMessages.*;



/**
 * This class provides an SSL trust manager that will interactively prompt the
 * user to determine whether to trust any certificate that is presented to it.
 * It provides the ability to cache information about certificates that had been
 * previously trusted so that the user is not prompted about the same
 * certificate repeatedly, and it can be configured to store trusted
 * certificates in a file so that the trust information can be persisted.
 */
public final class MyTrustManager implements X509TrustManager {
  /**
   * The message digest that will be used for MD5 hashes.
   */
  private static final MessageDigest MD5;



  /**
   * The message digest that will be used for SHA-1 hashes.
   */
  private static final MessageDigest SHA1;

  static {
    MessageDigest d = null;
    try {
      d = MessageDigest.getInstance("MD5");
    } catch (final Exception e) {
      debugException(e);
      throw new RuntimeException(e);
    }
    MD5 = d;

    d = null;
    try {
      d = MessageDigest.getInstance("SHA-1");
    } catch (final Exception e) {
      debugException(e);
      throw new RuntimeException(e);
    }
    SHA1 = d;
  }



  // Indicates whether to examine the validity dates for the certificate in
  // addition to whether the certificate has been previously trusted.
  private final boolean examineValidityDates;

  // The set of previously-accepted certificates.  The certificates will be
  // mapped from an all-lowercase hexadecimal string representation of the
  // certificate signature to a flag that indicates whether the certificate has
  // already been manually trusted even if it is outside of the validity window.
  private final ConcurrentHashMap<String,Boolean> acceptedCerts;

  // The print stream that will be used to display the prompt.
  //private final PrintStream out;

  // The path to the file to which the set of accepted certificates should be
  // persisted.
  private final String acceptedCertsFile;

  private final Handler handler;
  
  private final Context context;
  
  private boolean ok= true;
  
  private Integer result;

  /**
   * Creates a new instance of this prompt trust manager.  It will cache trust
   * information in memory but not on disk.
   */
  public MyTrustManager(String str, Handler handler, Context context) {
    this(str, true, handler, context);
  }

  /**
   * Creates a new instance of this prompt trust manager.  It may optionally
   * cache trust information on disk, and may also be configured to examine or
   * ignore validity dates.
   *
   * @param  acceptedCertsFile     The path to a file in which the certificates
   *                               that have been previously accepted will be
   *                               cached.  It may be {@code null} if the cache
   *                               should only be maintained in memory.
   * @param  examineValidityDates  Indicates whether to reject certificates if
   *                               the current time is outside the validity
   *                               window for the certificate.
   */
  public MyTrustManager(final String acceptedCertsFile, final boolean examineValidityDates, final Handler handler, final Context context) {
    this.handler = handler;
    this.context = context;
    this.acceptedCertsFile    = acceptedCertsFile;
    this.examineValidityDates = examineValidityDates;

    acceptedCerts = new ConcurrentHashMap<String,Boolean>();

    if (acceptedCertsFile != null) {
      BufferedReader r = null;
      try {
        final File f = new File(acceptedCertsFile);
        if (f.exists()) {
          r = new BufferedReader(new FileReader(f));
          while (true) {
            final String line = r.readLine();
            if (line == null) {
              break;
            }
            acceptedCerts.put(line, false);
          }
        }
      }
      catch (Exception e) {
        debugException(e);
      } finally {
        if (r != null) {
          try {
            r.close();
          } catch (Exception e) {
            debugException(e);
          }
        }
      }
    }
  }

  /**
   * Writes an updated copy of the trusted certificate cache to disk.
   *
   * @throws  IOException  If a problem occurs.
   */
  private void writeCacheFile() throws IOException {
    final File tempFile = new File(acceptedCertsFile + ".new");
    BufferedWriter w = null;
    try {
      w = new BufferedWriter(new FileWriter(tempFile));

      for (final String certBytes : acceptedCerts.keySet()) {
        w.write(certBytes);
        w.newLine();
      }
      Log.e("FILE", "value:" + w.toString());
    } finally {
      if (w != null) {
        w.close();
      }
    }

    final File cacheFile = new File(acceptedCertsFile);
    if (cacheFile.exists()) {
      final File oldFile = new File(acceptedCertsFile + ".previous");
      if (oldFile.exists()) {
        oldFile.delete();
      }
      cacheFile.renameTo(oldFile);
    }
    tempFile.renameTo(cacheFile);
  }



  /**
   * Indicates whether this trust manager would interactively prompt the user
   * about whether to trust the provided certificate chain.
   *
   * @param  chain  The chain of certificates for which to make the
   *                determination.
   *
   * @return  {@code true} if this trust manger would interactively prompt the
   *          user about whether to trust the certificate chain, or
   *          {@code false} if not (e.g., because the certificate is already
   *          known to be trusted).
   */
  public synchronized boolean wouldPrompt(final X509Certificate[] chain) {
    // See if the certificate is in the cache.  If it isn't then we will
    // prompt no matter what.
    final X509Certificate c = chain[0];
    final String certBytes = toLowerCase(toHex(c.getSignature()));
    final Boolean acceptedRegardlessOfValidity = acceptedCerts.get(certBytes);
    if (acceptedRegardlessOfValidity == null) {
      return true;
    }


    // If we shouldn't check validity dates, or if the certificate has already
    // been accepted when it's outside the validity window, then we won't
    // prompt.
    if (acceptedRegardlessOfValidity || (! examineValidityDates)) {
      return false;
    }


    // If the certificate is within the validity window, then we won't prompt.
    // If it's outside the validity window, then we will prompt to make sure the
    // user still wants to trust it.
    final Date currentDate = new Date();
    return (! (currentDate.before(c.getNotBefore()) || currentDate.after(c.getNotAfter())));
  }
  
  private void setResult(int result) {
    this.result = result;
    ok = false;
  }
  
  private void showCert(final Handler handler, final Context context, final String message) {
    handler.post(new Runnable() {  
      @Override  
      public void run() {  
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final TextView edit = new TextView(context);
        builder.setView(edit);
        builder.setTitle("error");
        builder.setPositiveButton("trust", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
              // User clicked OK button
            setResult(Constants.TRUST_YES);
          }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            setResult(Constants.TRUST_NO);
          }
        });
        builder.setNeutralButton("cert", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
          }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            edit.setText(message);
          }
        });
      }  
    });
  }

  private String createCertMessage(final X509Certificate[] chain, final boolean serverCert, final String validityWarning) throws CertificateException {
    StringBuilder builder = new StringBuilder();
    final X509Certificate c = chain[0];

    // If we've gotten here, then we need to display a prompt to the user.
    if (serverCert) {
      builder.append(INFO_PROMPT_SERVER_HEADING.get() + "\n");
    } else {
      builder.append(INFO_PROMPT_CLIENT_HEADING.get() + "\n");
    }
    
    if (chain.length == 1) {
      builder.append("\n" + WARN_PROMPT_SELF_SIGNED.get() + "\n");
    }
    
    if (validityWarning != null) {
      builder.append("\n" + validityWarning + "\n");
    }
    
    builder.append('\t' + INFO_PROMPT_SUBJECT.get(c.getSubjectX500Principal().getName(X500Principal.CANONICAL)) + "\n");
    builder.append("\t\t" + INFO_PROMPT_MD5_FINGERPRINT.get(getFingerprint(c, MD5)) + "\n");
    builder.append("\t\t" + INFO_PROMPT_SHA1_FINGERPRINT.get(getFingerprint(c, SHA1)) + "\n");

    for (int i=1; i < chain.length; i++) {
      builder.append('\t' + INFO_PROMPT_ISSUER_SUBJECT.get(i, chain[i].getSubjectX500Principal().getName(X500Principal.CANONICAL)) + "\n");
      builder.append("\t\t" + INFO_PROMPT_MD5_FINGERPRINT.get(getFingerprint(chain[i], MD5)) + "\n");
      builder.append("\t\t" + INFO_PROMPT_SHA1_FINGERPRINT.get(getFingerprint(chain[i], SHA1)) + "\n");
    }

    builder.append(INFO_PROMPT_VALIDITY.get(String.valueOf(c.getNotBefore()), String.valueOf(c.getNotAfter())) + "\n");
    return builder.toString();
  }
  /**
   * Performs the necessary validity check for the provided certificate array.
   *
   * @param  chain       The chain of certificates for which to make the
   *                     determination.
   * @param  serverCert  Indicates whether the certificate was presented as a
   *                     server certificate or as a client certificate.
   *
   * @throws  CertificateException  If the provided certificate chain should not
   *                                be trusted.
   */
  private synchronized void checkCertificateChain(final X509Certificate[] chain, final boolean serverCert) throws CertificateException {
    
    // See if the certificate is currently within the validity window.
    String validityWarning = null;
    final Date currentDate = new Date();
    final X509Certificate c = chain[0];
    if (examineValidityDates) {
      if (currentDate.before(c.getNotBefore())) {
        validityWarning = WARN_PROMPT_NOT_YET_VALID.get();
      } else if (currentDate.after(c.getNotAfter())) {
        validityWarning = WARN_PROMPT_EXPIRED.get();
      }
    }


    // If the certificate is within the validity window, or if we don't care
    // about validity dates, then see if it's in the cache.
    if ((! examineValidityDates) || (validityWarning == null)) {
      final String certBytes = toLowerCase(toHex(c.getSignature()));
      final Boolean accepted = acceptedCerts.get(certBytes);
      if (accepted != null) {
        if ((validityWarning == null) || (! examineValidityDates) || Boolean.TRUE.equals(accepted)) {
          // The certificate was found in the cache.  It's either in the
          // validity window, we don't care about the validity window, or has
          // already been manually trusted outside of the validity window.
          // We'll consider it trusted without the need to re-prompt.
          return;
        }
      }
    }

    String message = createCertMessage(chain, serverCert, validityWarning);
    showCert(handler, context, message);
    //TODO: hack, wait for user input, ugly while
    while (ok){}
    
    if (this.result == Constants.TRUST_NO) {
      try {  
        // The certificate should not be trusted.
        throw new CertificateException(ERR_CERTIFICATE_REJECTED_BY_USER.get());
      } catch (CertificateException ce) {
        throw ce;
      } catch (Exception e) {
        debugException(e);
      }
    }
  
    final String certBytes = toLowerCase(toHex(c.getSignature()));
    acceptedCerts.put(certBytes, (validityWarning != null));

    if (acceptedCertsFile != null) {
      try {
        writeCacheFile();
      } catch (Exception e) {
        debugException(e);
      }
    }
  }



  /**
   * Computes the fingerprint for the provided certificate using the given
   * digest.
   *
   * @param  c  The certificate for which to obtain the fingerprint.
   * @param  d  The message digest to use when creating the fingerprint.
   *
   * @return  The generated certificate fingerprint.
   *
   * @throws  CertificateException  If a problem is encountered while generating
   *                                the certificate fingerprint.
   */
  private static String getFingerprint(final X509Certificate c, final MessageDigest d) throws CertificateException {
    final byte[] encodedCertBytes = c.getEncoded();

    final byte[] digestBytes;
    synchronized (d) {
      digestBytes = d.digest(encodedCertBytes);
    }

    final StringBuilder buffer = new StringBuilder(3 * encodedCertBytes.length);
    toHex(digestBytes, ":", buffer);
    return buffer.toString();
  }



  /**
   * Indicate whether to prompt about certificates contained in the cache if the
   * current time is outside the validity window for the certificate.
   *
   * @return  {@code true} if the certificate validity time should be examined
   *          for cached certificates and the user should be prompted if they
   *          are expired or not yet valid, or {@code false} if cached
   *          certificates should be accepted even outside of the validity
   *          window.
   */
  public boolean examineValidityDates() {
    return examineValidityDates;
  }



  /**
   * Checks to determine whether the provided client certificate chain should be
   * trusted.
   *
   * @param  chain     The client certificate chain for which to make the
   *                   determination.
   * @param  authType  The authentication type based on the client certificate.
   *
   * @throws  CertificateException  If the provided client certificate chain
   *                                should not be trusted.
   */
  public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
    checkCertificateChain(chain, false);
  }



  /**
   * Checks to determine whether the provided server certificate chain should be
   * trusted.
   *
   * @param  chain     The server certificate chain for which to make the
   *                   determination.
   * @param  authType  The key exchange algorithm used.
   *
   * @throws  CertificateException  If the provided server certificate chain
   *                                should not be trusted.
   */
  public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
    checkCertificateChain(chain, true);
  }



  /**
   * Retrieves the accepted issuer certificates for this trust manager.  This
   * will always return an empty array.
   *
   * @return  The accepted issuer certificates for this trust manager.
   */
  public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0];
  }
}
