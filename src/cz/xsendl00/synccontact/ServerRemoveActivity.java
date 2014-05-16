package cz.xsendl00.synccontact;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Remove server connection.
 */
@EActivity(R.layout.activity_remove_server)
public class ServerRemoveActivity extends Activity {

  /**
   * AndroidDB.
   */
  @Bean
  public AndroidDB androidDB;
  private static final String TAG = "ServerRemoveActivity";
  private final Handler handler = new Handler();
  private ContactManager contactManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    contactManager = ContactManager.getInstance(ServerRemoveActivity.this);
  }

  /**
   * Click on remove server.
   *
   * @param view unused
   */
  public void removeActivity(@SuppressWarnings("unused") View view) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ServerRemoveActivity.this);
    alertDialogBuilder.setTitle("Remove server connection");
    alertDialogBuilder.setMessage("Do you want to really remove connection/account?")
        .setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int id) {
            ServerRemoveActivity.this.removeAccount();
          }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }
        });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();

  }

  /**
   *
   */
  @Background
  public void removeAccount() {


    AccountManagerCallback<Boolean> callback = new AccountManagerCallback<Boolean>() {

      @Override
      public void run(AccountManagerFuture<Boolean> arg0) {
        Log.i(TAG, "UCER S smazan");
        ServerRemoveActivity.this.sendBack();
      }
    };
    removeAccount(callback);
    contactManager.setAccountData(null);
    Toast toast = Toast.makeText(this, "Server connection/account was removed.", Toast.LENGTH_SHORT);
    toast.show();
  }

  /**
   * Remove accounts.
   * @param accountManagerCallback accountManagerCallback
   */
  @Background
  public void removeAccount(AccountManagerCallback<Boolean> accountManagerCallback) {
    AccountManager manager = AccountManager.get(ServerRemoveActivity.this);
    Account[] accounts = manager.getAccountsByType(Constants.ACCOUNT_TYPE);
    for (Account ac : accounts) {
      exportContactBack();
      manager.removeAccount(ac, accountManagerCallback, handler);
      break;
    }
  }

  /**
   *
   */
  @Background
  public void exportContactBack() {
    if (!contactManager.isContactsLocalInit() && !contactManager.isContactsLocalReload()) {
      contactManager.reloadContact();
    }
    androidDB.exportContactsFromSyncAccount(ServerRemoveActivity.this,
        contactManager.getContactsLocal());
  }

  private void sendBack() {
    Editor editor = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE).edit();
    editor.putBoolean(Constants.PREFS_START_FIRST, true);
    editor.commit();
    Intent databackIntent = new Intent();
    databackIntent.putExtra("REMOVED", true);
    setResult(Activity.RESULT_OK, databackIntent);
    finish();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.sync_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
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
}
