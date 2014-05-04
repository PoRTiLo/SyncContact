package cz.xsendl00.synccontact;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ServerActivity extends Activity {
  
  private final String TAG = "ServerActivity";
  static final int PICK_CONTACT_REQUEST = 1;  // The request code
  private AccountData ad;
  private boolean removed = false;
  
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
      Log.i(TAG, "rwemoved, repaint to only add :)");
      finish();
    } else {
      initActivity();
    }
  }
  
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
}
