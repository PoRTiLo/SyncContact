package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.api.BackgroundExecutor;

import com.unboundid.ldap.sdk.LDAPException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cz.xsendl00.synccontact.activity.fragment.ContactServerFragment;
import cz.xsendl00.synccontact.activity.fragment.GroupServerFragment;
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Activity for data from server.
 *
 * @author xsendl00
 */
@EActivity(R.layout.activity_contacts_server)
public class ContactsServerActivity extends Activity {

  @Bean
  protected Utils util;
  private static final String TAG = "ContactsServerActivity";

  private final Handler handler = new Handler();
  private ContactManager contactManager;
  private boolean first = false;
  private Menu mMenu;
  private ProgressDialog progressDialog;
  public boolean importAll = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    Intent intent = getIntent();
    first = intent.getBooleanExtra(Constants.INTENT_FIRST, false);

    contactManager = ContactManager.getInstance(getApplicationContext());
    if (!contactManager.isContactsServerInit() || !contactManager.isGroupsServerInit()) {
      progressDialog = new ProgressDialog(ContactsServerActivity.this);
      progressDialog.setTitle(getText(R.string.progress_downloading));
      progressDialog.setMessage(getText(R.string.progress_downloading_text));
      progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      progressDialog.setProgress(0);
      progressDialog.setCanceledOnTouchOutside(false);
      progressDialog.show();
      loadData();
    } else {
      init();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.mMenu = menu;
    if (first) {
      getMenuInflater().inflate(R.menu.contacts_menu_server_simply, menu);
    } else {
      getMenuInflater().inflate(R.menu.contacts_menu_server, menu);
    }
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Initialize GUI.
   */
  @UiThread
  protected void init() {
    ActionBar actionBar = getActionBar();
    actionBar.removeAllTabs();
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    actionBar.addTab(actionBar.newTab()
        .setText(getString(R.string.def_group))
        .setIcon(R.drawable.ic_action_group)
        .setTabListener(
            new MyTabListener<GroupServerFragment>(this, "GROUP_LDAP", GroupServerFragment.class)));
    actionBar.addTab(actionBar.newTab()
        .setText(getString(R.string.def_contact))
        .setIcon(R.drawable.ic_action_person)
        .setTabListener(
            new MyTabListener<ContactServerFragment>(this, "CONTACT_LDAP",
                ContactServerFragment.class)));
  }

  /**
   * Download data from server.
   */
  @Background(id = "loadData")
  protected void loadData() {
    try {
      contactManager.initContactsServer(handler);
      progressDialog.incrementProgressBy(20);
      contactManager.initGroupsServer(handler);
      progressDialog.incrementProgressBy(20);
      contactManager.getServerContact2Import();
      progressDialog.incrementProgressBy(20);
      if (progressDialog != null) {
        progressDialog.dismiss();
      }
      init();
    } catch (LDAPException e) {
      e.printStackTrace();
      if (progressDialog != null) {
        progressDialog.dismiss();
      }
      errorDialog(e.getExceptionMessage());
    }

  }

  /**
   * Update data, call by icon (refresh) in menu.
   */
  @Background(id = "loadData")
  public void reinitData() {
    setRefreshActionButtonState(true);
    try {
      contactManager.initContactsServer(handler);
      contactManager.initGroupsServer(handler);
      contactManager.getServerContact2Import(true);
      fragmnetCall();
      setRefreshActionButtonState(false);
    } catch (LDAPException e) {
      e.printStackTrace();
      errorDialog(e.getExceptionMessage());
    }

  }
  @UiThread
  public void errorDialog(final String error) {
    AlertDialog.Builder builder = new AlertDialog.Builder(ContactsServerActivity.this);
    final TextView edit = new TextView(ContactsServerActivity.this);
    edit.setText("Error to connection: " + contactManager.getAccountData().getHost() + ", " + contactManager.getAccountData().getBaseDn());
    builder.setView(edit);
    builder.setCancelable(false);
    //builder.setCanceledOnTouchOutside(false);
    builder.setTitle(ContactsServerActivity.this.getText(R.string.dialog_error_text));
    builder.setPositiveButton(ContactsServerActivity.this.getText(R.string.button_ok), new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int id) {
        // User clicked OK button
        setResult(Constants.TRUST_YES);
      }
    });
    builder.setNeutralButton(ContactsServerActivity.this.getText(R.string.button_show), new DialogInterface.OnClickListener() {

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



  /**
   * Show ProgressBar instead of button for refresh (R.id.action_add_group).
   *
   * @param refreshing true/false - show/hide
   */
  @UiThread
  protected void setRefreshActionButtonState(final boolean refreshing) {
    if (mMenu != null) {
      final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);
      if (refreshItem != null) {
        if (refreshing) {
          refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
        } else {
          refreshItem.setActionView(null);
        }
      }
    }
  }

  @UiThread
  public void onImportCompleted(Integer result) {
    if (result != -1) {
      if (progressDialog != null) {
        progressDialog.dismiss();
      }
      String str = getText(R.string.toast_imported).toString()
          + result
          + getText(R.string.toast_imported_contacts).toString();
      Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
      toast.show();
      contactManager.setLocalContactsInit(false);
      contactManager.setLocalGroupsInit(false);
      new Thread(new Runnable() {

        @Override
        public void run() {
          contactManager.getLocalGroupsContacts();
        }
      }).start();
    }
    if (first) {
      new Thread(new Runnable() {

        @Override
        public void run() {
          AccountManager manager = AccountManager.get(ContactsServerActivity.this);
          Account[] accounts = manager.getAccountsByType(Constants.ACCOUNT_TYPE);
          if (accounts.length > 0 && accounts[0] != null) {
            ContentResolver.setSyncAutomatically(accounts[0], ContactsContract.AUTHORITY, true);
          }

        }
      }).start();


      Editor editor = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE).edit();
      editor.putBoolean(Constants.PREFS_START_FIRST, false);
      editor.commit();
      Intent intent = new Intent(this, MainActivity_.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }
  }

  /**
   * Import selected contact form server to android.
   *
   * @param view unused
   */
  public void go2InfoMerge(@SuppressWarnings("unused") View view) {
    if (contactManager.getContactsServer().isEmpty()) {
      onImportCompleted(-1);
    } else {
      progressDialog = new ProgressDialog(ContactsServerActivity.this);
      progressDialog.setTitle(getText(R.string.progress_downloading));
      progressDialog.setMessage(getText(R.string.progress_downloading_text));
      progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      progressDialog.setProgress(0);
      progressDialog.setCanceledOnTouchOutside(false);
      progressDialog.show();
      importContactsFromServer();
    }

  }

  Handler handler1 = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      progressDialog.incrementProgressBy(1);
    }
  };
  @UiThread
  protected void setProgresBar() {
    progressDialog.setTitle(getText(R.string.progress_importing));
    progressDialog.setMessage(getText(R.string.progress_importing_text));
  }

  @Background
  protected void importContactsFromServer() {

    List<ContactRow> contacts2Import = new ArrayList<ContactRow>();
    for (ContactRow contactRow : contactManager.getServerContact2Import()) {
      if (contactRow.isSync()) {
        contacts2Import.add(contactRow);
      }
    }

    List<String> contacts2ImportUuid = new ArrayList<String>();
    for (ContactRow contactRow : contacts2Import) {
      contacts2ImportUuid.add(contactRow.getUuidFirst());
    }

    final List<GroupRow> groups2Import = new ArrayList<GroupRow>();
    for (GroupRow groupRow : contactManager.getServerGroup2Import()) {
      if (groupRow.isSync()) {
        for (String uuid : groupRow.getMebersUuids()) {
          if (!contacts2ImportUuid.contains(uuid)) {
            ContactRow object = new ContactRow();
            object.setUuid(uuid);
            contacts2Import.add(object);
            Log.i(TAG, "add to contacts2Import: " + uuid);
          }
        }
      }
      groups2Import.add(groupRow);
    }

    progressDialog.setMax(contacts2Import.size());
    Log.i(TAG, "selected contact to import:" + contacts2Import.size());

    final Map<String, GoogleContact> contacts = new ServerUtilities().fetchServerContacts(
        new ServerInstance(AccountData.getAccountData(getApplicationContext())), getApplicationContext(),
        handler, handler1, contacts2Import);
    setProgresBar();
    progressDialog.setProgress(0);
    Thread thread = new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          new AndroidDB().addContacts2Database(getApplicationContext(), contacts, handler1);
          new AndroidDB().addGroups2DatabaseWithMebers(getApplicationContext(), groups2Import, handler1);
          contactManager.getServerContact2Import(true);
          contactManager.getServerGroup2Import(true);
        } catch (RemoteException e) {
          e.printStackTrace();
        } catch (OperationApplicationException e) {
          e.printStackTrace();
        }
      }
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    onImportCompleted(contacts2Import.size());
  }

  /**
   * First run this application.
   *
   * @return true first run, false not first run.
   */
  public boolean isFirst() {
    return first;
  }

  /**
   * If pressed back, background threads will be stopped.
   */
  @Override
  public void onPause() {
    super.onPause();
    Log.i(TAG, "back press");
    boolean mayInterruptIfRunning = true;
    BackgroundExecutor.cancelAll("loadData", mayInterruptIfRunning);
    Log.i(TAG, "back press - delete");
    // setRefreshActionButtonState(false);
  }

  @Override
  protected void onStop() {
    super.onStop();
    boolean mayInterruptIfRunning = true;
    BackgroundExecutor.cancelAll("loadData", mayInterruptIfRunning);
  }

  @UiThread
  public void fragmnetCall() {
    GroupServerFragment f2 = (GroupServerFragment) getFragmentManager().findFragmentByTag("GROUP_LDAP");
    if (f2 != null) {
      f2.updateAdapter();
    }
    ContactServerFragment f1 = (ContactServerFragment) getFragmentManager().findFragmentByTag("CONTACT_LDAP");
    if (f1 != null) {
      f1.updateAdapter(); // Your method of the fragment
    }
    Log.i(TAG, "fragmnetCall");
  }
}
