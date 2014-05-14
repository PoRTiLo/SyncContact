package cz.xsendl00.synccontact;


import com.googlecode.androidannotations.annotations.EActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import cz.xsendl00.synccontact.activity.fragment.ContactFragment;
import cz.xsendl00.synccontact.activity.fragment.GroupFragment_;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Contact activity.
 * @author portilo
 *
 */
@EActivity(R.layout.activity_contacts)
public class ContactsActivity extends Activity {

  //@Bean
  //ContactManager contactManager;
  private ProgressDialog progressDialog;
  private ContactManager contactManager;
  private boolean first = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    Intent intent = getIntent();
    first = intent.getBooleanExtra("FIRST", false);
    contactManager = ContactManager.getInstance(getApplicationContext());
    if (!contactManager.isContactsLocalInit() || !contactManager.isGroupsLocalInit()) {
      progressDialog = ProgressDialog.show(ContactsActivity.this,
          Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);
      new LoadTask(ContactsActivity.this).execute();

    } else {
      init();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    if (first) {
      // TODO;
      getMenuInflater().inflate(R.menu.contacts_menu_simply, menu);
    } else {
      getMenuInflater().inflate(R.menu.contacts_menu, menu);
    }

    return super.onCreateOptionsMenu(menu);
  }

  private void init() {
    ActionBar actionBar = getActionBar();
    actionBar.removeAllTabs();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    actionBar.addTab(actionBar
        .newTab()
        .setText("GROUP")
        .setIcon(R.drawable.ic_action_group)
        .setTabListener(new MyTabListener<GroupFragment_>(this, "GROUP", GroupFragment_.class))
        );
    actionBar.addTab(actionBar
        .newTab()
        .setText("CONTACT")
        .setIcon(R.drawable.ic_action_person)
        .setTabListener(new MyTabListener<ContactFragment>(this, "CONTACT", ContactFragment.class))
        );
  }

  /**
   * Do after data from database is loaded.
   */
  public void onLoadCompleted() {
    init();
  }

  /**
   * Update data, call by icon in menu.
   */
  public void reinitData() {
    progressDialog = ProgressDialog.show(ContactsActivity.this,
        Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);
    new  LoadTask(ContactsActivity.this).execute();
  }

  private class LoadTask extends AsyncTask<Void, Void, Boolean> {
    private Activity activity;

    public LoadTask(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      //contactManager.initGroup();
      //contactManager.initContact();
      contactManager.initGroupsContacts();
      return null;
    }

    @Override
    protected void onPostExecute(Boolean bool) {
      if (ContactsActivity.this.progressDialog != null) {
        ContactsActivity.this.progressDialog.dismiss();
      }
      ((ContactsActivity) activity).onLoadCompleted();
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();

    }
  }

  /**
   * Call after clock on button.
   * @param view unused
   */
  public void mainActivity(@SuppressWarnings("unused") View view) {
    Editor editor = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE).edit();
    editor.putBoolean(Constants.PREFS_START_FIRST, false);
    editor.commit();
    Intent intent = new Intent(this, InfoMergeActivity_.class);
    startActivity(intent);
  }

  /**
   * First run this application.
   * @return true first run, false not first run.
   */
  public boolean isFirst() {
    return first;
  }

  /**
   *
   * @param first run first
   */
  public void setFirst(boolean first) {
    this.first = first;
  }
}
