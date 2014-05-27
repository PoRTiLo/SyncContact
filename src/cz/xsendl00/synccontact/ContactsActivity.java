package cz.xsendl00.synccontact;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import cz.xsendl00.synccontact.activity.fragment.ContactFragment;
import cz.xsendl00.synccontact.activity.fragment.GroupFragment;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Contact activity.
 *
 * @author portilo
 */
@EActivity(R.layout.activity_contacts)
public class ContactsActivity extends Activity {

  private static final String TAG = "ContactsActivity";
  private ContactManager contactManager;
  private boolean first = false;
  private Menu mMenu;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    Intent intent = getIntent();
    first = intent.getBooleanExtra(Constants.INTENT_FIRST, false);

    contactManager = ContactManager.getInstance(getApplicationContext());
    init();
    if (!contactManager.isLocalContactsInit() || !contactManager.isLocalGroupsInit()) {
      loadData();
    } else {
      init();
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.mMenu = menu;
    if (first) {
      getMenuInflater().inflate(R.menu.contacts_menu_simply, menu);
    } else {
      getMenuInflater().inflate(R.menu.contacts_menu, menu);
    }
    return super.onCreateOptionsMenu(menu);
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

  /**
   * Initialize tabs. Call it after data in {@link ContactManager} are loaded.
   */
  @UiThread
  protected void init() {
    ActionBar actionBar = getActionBar();
    actionBar.removeAllTabs();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    actionBar.addTab(actionBar.newTab()
        .setText(getString(R.string.def_group))
        .setIcon(R.drawable.ic_action_group)
        .setTabListener(new MyTabListener<GroupFragment>(this, "GROUP", GroupFragment.class)));
    actionBar.addTab(actionBar.newTab()
        .setText(getString(R.string.def_contact))
        .setIcon(R.drawable.ic_action_person)
        .setTabListener(new MyTabListener<ContactFragment>(this, "CONTACT", ContactFragment.class)));

  }

  /**
   * Do after data from database is loaded.
   */
  protected Thread loadData() {
    final ProgressDialog progressDialog = ProgressDialog.show(ContactsActivity.this, "",
        getText(R.string.progress_loading), true);
    progressDialog.setCancelable(true);
    progressDialog.setCanceledOnTouchOutside(false);
    final Runnable runnable = new Runnable() {

      @Override
      public void run() {
        Log.i(TAG, "Load data start");
        contactManager.getLocalGroupsContacts();
        contactManager.convertContact2NewAccount();
        contactManager.updateGroupsUuid();

        refresh();
        init();
        progressDialog.dismiss();

        Log.i(TAG, "Load data after initGroup");
      }
    };
    return Utils.performOnBackgroundThread(runnable);
  }

  @UiThread
  public void refresh() {
    GroupFragment groupFragment = (GroupFragment) getFragmentManager().findFragmentByTag("GROUP");
    if (groupFragment != null) {
      groupFragment.updateAdapter();
    }

    ContactFragment contactFragment = (ContactFragment) getFragmentManager().findFragmentByTag(
        "CONTACT");
    if (contactFragment != null) {
      contactFragment.updateAdapter();
    }
  }

  /**
   * Update data, call by icon (refresh) in menu.
   */
  @Background
  public void reinitData() {
    setRefreshActionButtonState(true);
    contactManager.setLocalContactsInit(false);
    contactManager.setLocalGroupsInit(false);
    contactManager.setLocalGroupsContactsInit(false);
    contactManager.getLocalGroupsContacts();
    refresh();
    setRefreshActionButtonState(false);
  }

  /**
   * Call after clock on button. Got to next activity {@link InfoMergeActivity}.
   *
   * @param view unused
   */
  public void go2InfoMerge(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, InfoMergeActivity_.class);
    startActivity(intent);
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
    // TODO:
    // Log.i(TAG, "onPause");
    //
    // Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
    // for (Thread thread : threadSet) {
    // Log.i(TAG, thread.toString());
    // }
    // BackgroundExecutor.cancelAll("loadData", true);
    // Log.i(TAG, "onPause - delete");
    // // setRefreshActionButtonState(false);
    super.onPause();
  }

  public void addGroup() {
    LayoutInflater li = LayoutInflater.from(ContactsActivity.this);
    View promptsView = li.inflate(R.layout.add_group, null);
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactsActivity.this);
    alertDialogBuilder.setView(promptsView);

    final EditText userInput = (EditText) promptsView.findViewById(R.id.add_group_name);
    alertDialogBuilder.setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int id) {
            createGroup(userInput.getText().toString());

          }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }
        });
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }

  /**
   * Create new group.
   * @param name name of group
   */
  private void createGroup(String name) {
    // create group
    // show list of contact
    ProgressDialog progressDialog = new ProgressDialog(ContactsActivity.this);
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.show();

    Integer id = new AndroidDB().addGroup(ContactsActivity.this, name, null);
    Log.i(TAG, "GROUP id:" + id);
    progressDialog.dismiss();

    Intent i = new Intent(ContactsActivity.this, ContactsDetailActivity_.class);
    i.putExtra(Constants.INTENT_ID, id);
    i.putExtra(Constants.INTENT_SYNC, true);
    i.putExtra(Constants.INTENT_NAME, name);
    startActivity(i);
  }
}
