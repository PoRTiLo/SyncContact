package cz.xsendl00.synccontact;

import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SynchronizationActivity extends Activity {

  private final String TAG = "SynchronizationActivity"; 
  
  private TextView editTextTime;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_synchronization);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getLastSyncTime();
  }

  private void getLastSyncTime () {
    new Thread(new Runnable() {
      public void run() {
        editTextTime = (TextView) findViewById(R.id.sync_time);
        HelperSQL db = new HelperSQL(getApplicationContext());
        final String str = db.newerTimestamp();
        editTextTime.post(new Runnable() {
          public void run() {
            editTextTime.setText(ContactRow.timestamptoDate(str));
            Log.i(TAG, "LastSync: " + editTextTime.getText());
          }
        });
      }
    }).start();
  }
  
  
  
  /**
   * Run synchronization by user's request.
   * @param view
   */
  public void synchronizationActivity(View view) {
    SyncTask rt = new SyncTask();
    rt.execute();
  }
  
  private class SyncTask extends AsyncTask<Void, Void, Boolean> {
    
    private ProgressDialog progressDialog;
    @Override
    protected Boolean doInBackground(Void...params) {
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
      progressDialog = ProgressDialog.show(SynchronizationActivity.this, Constants.AC_DOWNLOADING, Constants.AC_DOWNLOADING_DATA_SERVER, true);
    }
  }
}