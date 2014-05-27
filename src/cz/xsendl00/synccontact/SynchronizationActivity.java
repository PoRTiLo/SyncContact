package cz.xsendl00.synccontact;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.Synchronization;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Synchronization activity.
 */
@EActivity(R.layout.activity_synchronization)
public class SynchronizationActivity extends Activity {

  private Handler handler = new Handler();
  private static final String TAG = "SynchronizationActivity";

  @Bean
  protected Utils utils;

  @Bean
  protected Synchronization synchronization;
  /**
   * Place for date of last synchronization.
   */
  @ViewById(R.id.synchronization_time)
  protected TextView editTextTime;
  private ContactManager contactManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    contactManager = ContactManager.getInstance(SynchronizationActivity.this);
  }

  @Override
  public void onResume() {
    super.onResume();
    getLastSyncTime();
  }

  /**
   * Get last synchronization time from database.
   */
  @AfterViews
  protected void getLastSyncTime() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        final String str = new AndroidDB().newerTimestamp(SynchronizationActivity.this);
        editTextTime.post(new Runnable() {
          @Override
          public void run() {
            editTextTime.setText(utils.timestamptoDate(str));
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
        if (contactManager.getAccountData() == null) {
          if (!contactManager.initAccountData()) {
            //TODO: call activity for edit connection
          }
        }
        synchronization.synchronization(new ServerInstance(contactManager.getAccountData()), getApplicationContext(), handler);
      } catch (RemoteException e) {
        e.printStackTrace();
      } catch (OperationApplicationException e) {
        e.printStackTrace();
      }
      return true;
    }
    @Override
    protected void onPostExecute(Boolean bool) {
      if (progressDialog != null) {
        progressDialog.dismiss();
      }
      getLastSyncTime();
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = ProgressDialog.show(SynchronizationActivity.this, getText(R.string.progress_synchronizing),
          getText(R.string.progress_synchronizing_text), true);
      progressDialog.setCanceledOnTouchOutside(false);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.sync_menu, menu);
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
