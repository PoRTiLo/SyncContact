package cz.xsendl00.synccontact;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;

/**
 * Show info about server connection.
 */
@EActivity(R.layout.activity_server)
public class ServerActivity extends Activity {

  @ViewById(R.id.server_name)
  TextView name;
  @ViewById(R.id.server_address)
  TextView address;
  @ViewById(R.id.server_port)
  TextView port;
  @ViewById(R.id.server_security)
  TextView encry;

  private static final String TAG = "ServerActivity";
  private static final int SERVER_DELETED = 1;  // The request code
  private static final int SERVER_CHANGED = 2;  // The request code
  private ContactManager contactManager;
  private boolean removed = false;
  private boolean edited = false;
  private final Handler handler = new Handler();
  private ProgressDialog progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    contactManager = ContactManager.getInstance(getApplicationContext());
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (removed) { // go in this activity from remove activity
      finish();
    } else if (edited) { // from edited
      initActivity();
    } else { // from other activity
      if (contactManager.getAccountData() == null) {
        initActivity();
      }
    }
  }

  /**
   * Load server's data from account.
   */
  @Background
  public void initActivity() {
    contactManager.initAccountData();
    init();
  }

  /**
   * Initialize activity. Call it after initialize {@link AccountData} in {@link ContactManager}.
   */
  @AfterViews
  @UiThread
  public void init() {
    if (contactManager.getAccountData() != null && contactManager.getAccountData().getHost() != null) {

      name.setText(contactManager.getAccountData().getHost());
      address.setText(contactManager.getAccountData().getBaseDn());
      port.setText(contactManager.getAccountData().getPort().toString());

      encry.setText(security(contactManager.getAccountData().getEncryption()));
      Log.i(TAG, "Init, get value from account");
    } else {
      // repaitn activity to add new seerver
    }
  }


  /**
   * Edit connection, edit account.
   * @param view unused
   */
  public void editActivity(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, ServerAddActivity_.class);
    startActivityForResult(intent, SERVER_CHANGED);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Check which request we're responding to
    if (requestCode == SERVER_DELETED) {
      // Make sure the request was successful
      if (resultCode == RESULT_OK) {
        Log.i(TAG, "Back from ServerRemoveActivity");
        removed = data.getBooleanExtra("REMOVED", false);
        finish();
        super.onActivityResult(requestCode, resultCode, data);
      }
    } else if (requestCode == SERVER_CHANGED) {
      if (resultCode == RESULT_OK) {
        Log.i(TAG, "Back from ServerAddActivity");
        edited  = data.getBooleanExtra("EDITED", false);
      }
    }
  }

  /**
   * Remove connection, remove account.
   * @param view unused
   */
  public void removeActivity(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, ServerRemoveActivity_.class);
    startActivityForResult(intent, SERVER_DELETED);
  }


  private String security(Integer i) {
    String str = null;
    if (i == 0) {
      str = "Default";
    } else if (i == 1) {
      str = "No ecryption";
    } else if (i == 2) {
      str = "SSL/TLS";
    } else if (i == 3) {
      str = "StartTLS";
    } else {
      str = "no def";
    }
    return str;
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.server_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
    case R.id.action_reconect:
      showProgressBar();
      ServerUtilities.attemptAuth(
          new ServerInstance(contactManager.getAccountData()), handler, ServerActivity.this, true);
      break;
    case R.id.action_help:
      intent = new Intent(this, HelpActivity_.class);
      startActivity(intent);
      break;
    case R.id.action_settings:
      intent = new Intent(this, SettingsActivity_.class);
      startActivity(intent);
      break;
    case android.R.id.home:
      finish();
      break;
    default:
      break;
    }

    return true;
  }

  private void showProgressBar() {
    progressBar = new ProgressDialog(ServerActivity.this);
    progressBar.setCancelable(true);
    progressBar.setCanceledOnTouchOutside(false);
    progressBar.setMessage(getText(R.string.progress_testing_connection));
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.show();
  }

  /**
   * Call after
   * @param baseDNs
   * @param result
   * @param message
   */
  public void onAuthenticationResult(String[] baseDNs, boolean result, String message) {
    Log.i(TAG, "onAuthenticationResult(" + result + ")");
    if (progressBar != null) {
      progressBar.dismiss();
    }

    if (result) {
      Toast.makeText(this, getText(R.string.toast_tested), Toast.LENGTH_SHORT).show();
    } else {
      showDialog(message);
      Log.e(TAG, "onAuthenticationResult: failed to authenticate");
    }
  }
  private void showDialog(String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(message).setTitle(getText(R.string.dialog_error));
    builder.setPositiveButton(getText(R.string.button_ok), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int id) {
          // User clicked OK button
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }
}
