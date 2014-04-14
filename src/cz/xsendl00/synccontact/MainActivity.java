package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import com.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.Mapping;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
  
  private LinkedHashMap<GroupRow, ArrayList<ContactRow>> groupList;

  private static final String TAG = "MainActivity";
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loadPreferences();
  }
  
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
      super.onWindowFocusChanged(hasFocus);
      if (hasFocus == true) {
        RelativeLayout f = (RelativeLayout) findViewById(R.id.main_activity);
        conf(f);
      }
  }
  
  private void loadPreferences() {
    Log.i(TAG, "Load preferens");
    SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
    setsyntContact = settings.getBoolean(Constants.SET_SYNC_CONTACT, false);
    Log.i(TAG, "Load preferens: Set sync contact = " + setsyntContact);
  }
  
  
  private void conf(RelativeLayout f) {
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
    Intent intent = new Intent(this, AddServerActivity.class);
    startActivity(intent);
  }
  
  public void startContactActivity(View view) {
	  Intent intent = new Intent(this, ContactsListActivity.class);
	  startActivity(intent);
  }
  
  public void startHelpActivity(View view) {
    Intent intent = new Intent(this, SelectContactListActivity.class);
    startActivity(intent);
  }
  
  
  
  public void startMap(View view) {
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
      ServerUtilities.synchronization(new ServerInstance(AccountData.getAccountData(getApplicationContext())), getApplicationContext());
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
