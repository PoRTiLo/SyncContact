package cz.xsendl00.synccontact;


import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.Utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

  
  private Button map;
  private Button add;
  private Button show;
  private Button help;
  private Boolean setsyntContact;
  private final Handler handler = new Handler();
  ProgressDialog progressDialog;
  

  private static final String TAG = "MainActivity";
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    loadPreferences();
    
  }
  
  protected void onStart() {
    super.onStart();
    
  }
  
  protected void onResume() {
    super.onResume();
    conf();
  }
  
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus == true) {
      conf();
    }
  }

  
  
  private void loadPreferences() {
    Log.i(TAG, "Load preferens");
    SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
    boolean startFirst = settings.getBoolean(Constants.PREFS_START_FIRST, true);
    Log.i(TAG, "Load preferens: startFirst = " + startFirst);
    if (startFirst) {
      // load first route
      Intent intent = new Intent(this, WelcomeActivity.class);
      startActivity(intent);
      this.finish();
    }
    
    
    Log.i(TAG, "Load preferens: Set sync contact = " + setsyntContact);
  }
  
  
  private void conf() {
    RelativeLayout f = (RelativeLayout) findViewById(R.id.main_activity);
    int x = (int) f.getRight()/2;
    int y = (int) (f.getBottom())/5;
    map = (Button) findViewById(R.id.button_map);
    map.getLayoutParams().height = y*3;
    map.getLayoutParams().width = x;
    map.setLayoutParams(map.getLayoutParams());

    show = (Button) findViewById(R.id.button_show_sight);
    show.getLayoutParams().height = y*2;
    show.getLayoutParams().width = x;    
    show.setLayoutParams(show.getLayoutParams());    

    help = (Button) findViewById(R.id.button_help);
    help.getLayoutParams().height = y*2;
    help.getLayoutParams().width = x;
    help.setLayoutParams(help.getLayoutParams());

    add = (Button) findViewById(R.id.button_add_sight);
    add.getLayoutParams().height = y*3;
    add.getLayoutParams().width = x;
    add.setLayoutParams(add.getLayoutParams());
  }
  
  /**
   * Called when the user clicks on the Server button.
   * 
   * @param view
   */
  public void startServerActivity(View view) {
    SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
    boolean startFirst = settings.getBoolean(Constants.PREFS_START_FIRST, true);
    if (startFirst) {
      Intent intent = new Intent(this, WelcomeActivity.class);
      startActivity(intent);
    } else {
      Intent intent = new Intent(this, ServerActivity.class);
      startActivity(intent);
    }
  }
  
  public void startContactActivity(View view) {
    Intent intent = new Intent(this, SelectContactListActivity.class);
    startActivity(intent);
  }
  
  public void startHelpActivity(View view) {
    Intent intent = new Intent(this, HelpActivity.class);
    startActivity(intent);
  }
  
  public void startSyncActivity(View view) {
    Intent intent = new Intent(this, SynchronizationActivity.class);
    startActivity(intent);
  }
  
  
  public void startMap(View view) {
   /* EmailSync em2 = new EmailSync();
    EmailSync em1 = new EmailSync();
    em2.setHomeMail("ddd");
    em1.setHomeMail("aaa");
    ArrayList<ContentProviderOperation> op = EmailSync.operation(null, em1, em2);
    if (op != null) {
      System.out.println("not null");
    } else {
      System.out.println("null");
    }
    for (ContentProviderOperation p : op) {
      System.out.println(p.toString());
    }
    */
    //ContentValue a = EmailSync.operation(null, null, null);
    //Log.i(TAG, GoogleContact.defaultValue().toString());
    
    //Mapping.mappingRequest(getContentResolver(), "149", "baseDn");
    //Mapping.mappingContactFromDB(getContentResolver(), "149");
    //Mapping.fetchDirtyContacts(getApplicationContext());
    UpdateTask rt = new UpdateTask();
    rt.execute();
    //Log.i(TAG, ContactRow.createTimestamp());
    //ServerUtilities.synchronization(new ServerInstance(AccountData.getAccountData(getApplicationContext())), getApplicationContext());
    
  }
  
  private class UpdateTask extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog progressDialog;
    @Override
    protected Boolean doInBackground(Void...params) {
      //ServerUtilities.addContactsToServer(new ServerInstance(AccountData.getAccountData(getApplicationContext())), handler, getApplicationContext());
      //ServerUtilities.fetchModifyTimestamp(new ServerInstance(AccountData.getAccountData(getApplicationContext())), getApplicationContext());
      //ServerUtilities.fetchModifyTimestampContacts(new ServerInstance(AccountData.getAccountData(getApplicationContext())), 
      //    getApplicationContext(), "20140328193405Z");
      try {
        ServerUtilities.synchronization(new ServerInstance(AccountData.getAccountData(getApplicationContext())), getApplicationContext());
      } catch (RemoteException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (OperationApplicationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return true;
    }
    protected void onPostExecute(Boolean bool) {
      progressDialog.dismiss();
    }
    
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = ProgressDialog.show(MainActivity.this, "Downloading...","Downloading data from server", true);
    }
  }
}
