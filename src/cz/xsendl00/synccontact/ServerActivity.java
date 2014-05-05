package cz.xsendl00.synccontact;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ServerActivity extends Activity {
  
  private final String TAG = "ServerActivity";
  static final int PICK_CONTACT_REQUEST = 1;  // The request code
  private AccountData ad;
  private boolean removed = false;
  private final Handler handler = new Handler();
  private ProgressDialog progressBar;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_server);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Check which request we're responding to
    if (requestCode == PICK_CONTACT_REQUEST) {
      // Make sure the request was successful
      if (resultCode == RESULT_OK) {
        Log.i(TAG, "onActivityResult ...");
        removed = data.getBooleanExtra("REMOVED", false);
      }
    }
  }
  
  /**
   * Remove connection, remove account.
   * @param view
   */
  public void removeActivity(View view) {
    Intent intent = new Intent(this, RemoveServerActivity.class);
    startActivityForResult(intent, PICK_CONTACT_REQUEST);
  }
  
  
  /**
   * Edit connection, edit account
   * @param view
   */
  public void editActivity(View view) {
    if (ad == null) {
      new ButtonTask(ServerActivity.this).execute();
    } else {
      onTaskCompletedButton(ad);
    }
  }
  
  protected void onResume() {
    super.onResume();
    if (removed) {
      finish();
    } else {
      initActivity();
    }
  }
  
  /**
   * Load server's data from account. 
   */
  private void initActivity() {
    new LoadTask(ServerActivity.this).execute();
  }
  
  private class LoadTask extends AsyncTask<Void, Void, AccountData> {
    private Activity activity;

    public LoadTask(Activity activity) {
      this.activity = activity;
    }
    @Override
    protected AccountData doInBackground(Void... params) {
      return AccountData.getAccountData(activity);
    }
    protected void onPostExecute(AccountData p) {
      ((ServerActivity) activity).onTaskCompleted(p);
    }
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
  
  private class ButtonTask extends AsyncTask<Void, Void, AccountData> {
    private Activity activity;

    public ButtonTask(Activity activity) {
      this.activity = activity;
    }
    @Override
    protected AccountData doInBackground(Void... params) {
      return AccountData.getAccountData(activity);
    }
    protected void onPostExecute(AccountData p) {
      ((ServerActivity) activity).onTaskCompletedButton(p);
    }
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
  
  public void onTaskCompletedButton(AccountData ad) {
    this.ad = ad;
    Intent intent = new Intent(this, AddServerActivity.class);
    intent.putExtra(Constants.PAR_PORT, ad.getPort());
    intent.putExtra(Constants.PAR_HOST, ad.getHost());
    intent.putExtra(Constants.PAR_BASEDN, ad.getBaseDn());
    intent.putExtra(Constants.PAR_USERNAME, ad.getName());
    intent.putExtra(Constants.PAR_PASSWORD, ad.getPassword());
    intent.putExtra(Constants.PAR_ENCRYPTION, ad.getEncryption());
    intent.putExtra(Constants.PAR_IS_ADDING_NEW_ACCOUNT, false);
    
    startActivity(intent);
  }
  
  public void onTaskCompleted(AccountData ad) {
    this.ad = ad;
    if (ad != null && ad.getHost() != null) {
      TextView name = (TextView) findViewById(R.id.server_name);
      TextView address = (TextView) findViewById(R.id.server_address);
      TextView port = (TextView) findViewById(R.id.server_port);
      TextView encry = (TextView) findViewById(R.id.server_security);
      
      name.setText(ad.getHost());
      address.setText(ad.getBaseDn());
      port.setText(ad.getPort().toString());
      
      encry.setText(security(ad.getEncryption()));
      Log.i(TAG, "Init, get value from account");
    } else {
      // repaitn activity to add new seerver
    }
  }
  
  private String security(Integer i) {
    String str = null;
    if (i == 0) {
      str = "Default";
    } else if (i == 1) {
      str = "encryption";
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
      ServerUtilities.attemptAuth(new ServerInstance(ad), handler, ServerActivity.this, true);
      break;
    case R.id.action_help:
      intent = new Intent(this, HelpActivity.class);
      startActivity(intent);
      break;
    case R.id.action_settings:
      intent = new Intent(this, SettingsActivity.class);
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
    progressBar.setMessage("Autentication...");
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
      Toast.makeText(this, "Test connection succesfully", Toast.LENGTH_SHORT).show();
    } else {
      showDialog(message);
      Log.e(TAG, "onAuthenticationResult: failed to authenticate");
    }
  }
  private void showDialog(String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(message).setTitle("error");
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
          // User clicked OK button
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }
}
