package cz.xsendl00.synccontact;


import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.Synchronization;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;

@EActivity(R.layout.activity_synchronization)
public class SynchronizationActivity extends Activity {

  private static final String TAG = "SynchronizationActivity";

  @ViewById(R.id.synchronization_time)
  protected TextView editTextTime;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public void onResume() {
    super.onResume();
    getLastSyncTime();
  }

  @AfterViews
  protected void getLastSyncTime() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        HelperSQL db = new HelperSQL(getApplicationContext());
        final String str = db.newerTimestamp();
        editTextTime.post(new Runnable() {
          @Override
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
   * @param view unused
   */
  @SuppressWarnings("unused")
  public void synchronizationActivity(View view) {
    SyncTask rt = new SyncTask();
    rt.execute();
  }

  private class SyncTask extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog progressDialog;
    @Override
    protected Boolean doInBackground(Void...params) {
      try {
        Synchronization synchronization = new Synchronization(getApplicationContext());
        synchronization.synchronization(new ServerInstance(AccountData.getAccountData(getApplicationContext())),
            getApplicationContext());
      } catch (RemoteException e) {
        e.printStackTrace();
      } catch (OperationApplicationException e) {
        e.printStackTrace();
      }
      return true;
    }
    @Override
    protected void onPostExecute(Boolean bool) {
      progressDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = ProgressDialog.show(SynchronizationActivity.this, Constants.AC_DOWNLOADING,
          Constants.AC_DOWNLOADING_DATA_SERVER, true);
    }
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
