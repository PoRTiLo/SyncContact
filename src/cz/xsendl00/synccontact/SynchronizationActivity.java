package cz.xsendl00.synccontact;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.unboundid.ldap.sdk.LDAPException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.Synchronization;
import cz.xsendl00.synccontact.utils.Constants;
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
        final String str = new HelperSQL(SynchronizationActivity.this).newerTimestamp(); //new AndroidDB().newerTimestamp(SynchronizationActivity.this);
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
    if (contactManager.isOnlyWifi()) {
      if (!contactManager.checkInternet()) {
        Log.i(TAG, "neni wifi");
        Toast.makeText(SynchronizationActivity.this, "Set sync only by wifi. Synchronization not running.",
            Toast.LENGTH_LONG).show();
      } else {
        SyncTask rt = new SyncTask();
        rt.execute();
      }
    } else {
      SyncTask rt = new SyncTask();
      rt.execute();
    }
  }

  private class SyncTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog progressDialog;
    @Override
    protected String doInBackground(Void...params) {
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
      } catch (LDAPException e) {
        e.printStackTrace();
        Log.i(TAG, e.getResultCode().toString());
        Log.i(TAG, e.getExceptionMessage() != null ? e.getExceptionMessage() : "");
        Log.i(TAG, e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "");
        Log.i(TAG, e.getMatchedDN() != null ? e.getMatchedDN() : "");
        Log.i(TAG, e.getMessage() != null ? e.getMessage() : "");
        return e.getExceptionMessage();
      }
      return null;
    }
    @Override
    protected void onPostExecute(String error) {
      if (progressDialog != null) {
        progressDialog.dismiss();
      }
      if (error == null) {
        getLastSyncTime();
      } else {
        errorDialog(error);
      }
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog = ProgressDialog.show(SynchronizationActivity.this, getText(R.string.progress_synchronizing),
          getText(R.string.progress_synchronizing_text), true);
      progressDialog.setCanceledOnTouchOutside(false);
    }
  }

  public void errorDialog(final String error) {
    AlertDialog.Builder builder = new AlertDialog.Builder(SynchronizationActivity.this);
    final TextView edit = new TextView(SynchronizationActivity.this);
    edit.setText("Error to connection: " + contactManager.getAccountData().getHost() + ", " + contactManager.getAccountData().getBaseDn());
    builder.setView(edit);
    builder.setCancelable(false);
    //builder.setCanceledOnTouchOutside(false);
    builder.setTitle(SynchronizationActivity.this.getText(R.string.dialog_error_text));
    builder.setPositiveButton(SynchronizationActivity.this.getText(R.string.button_ok), new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int id) {
        // User clicked OK button
        setResult(Constants.TRUST_YES);
      }
    });
    builder.setNeutralButton(SynchronizationActivity.this.getText(R.string.button_show), new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        edit.setText(error);
      }
    });
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
