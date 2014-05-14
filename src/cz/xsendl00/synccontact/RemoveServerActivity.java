package cz.xsendl00.synccontact;

import com.googlecode.androidannotations.annotations.EActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import cz.xsendl00.synccontact.utils.Constants;

@EActivity(R.layout.activity_remove_server)
public class RemoveServerActivity extends Activity {

  private static final String TAG = "RemoveServerActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  public void removeActivity(@SuppressWarnings("unused") View view) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RemoveServerActivity.this);
    // set title
    alertDialogBuilder.setTitle("Your Title");
    // set dialog message
    alertDialogBuilder
      .setMessage("Click yes to exit!")
      .setCancelable(false)
      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            RemoveServerActivity.this.removeAccount();
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

  private void removeAccount() {
    final Handler handler = new Handler();

    AccountManagerCallback<Boolean> callback = new AccountManagerCallback<Boolean>() {
      @Override
      public void run(AccountManagerFuture<Boolean> arg0) {
        Log.i(TAG, "UCER S smazan");
        RemoveServerActivity.this.sendBack();
      }
    };

    new RemoveTask(RemoveServerActivity.this, handler, callback).execute();



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

  private class RemoveTask extends AsyncTask<Void, Void, Boolean> {
    private Activity activity;
    private Handler handler;
    private AccountManagerCallback<Boolean> callback;
    public RemoveTask(Activity activity, Handler handler,  AccountManagerCallback<Boolean> callback) {
      this.activity = activity;
      this.handler = handler;
      this.callback = callback;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
      //final Handler handler = new Handler ();
      AccountManager manager = AccountManager.get(activity);
      Account[] accounts = manager.getAccountsByType(Constants.ACCOUNT_TYPE);
      for (Account ac : accounts) {
        manager.removeAccount(ac, callback, handler);
        break;
      }

      return null;

    }
    @Override
    protected void onPostExecute(Boolean b) {
      //((ServerActivity) activity).onTaskCompletedButton(p);
    }
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
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
