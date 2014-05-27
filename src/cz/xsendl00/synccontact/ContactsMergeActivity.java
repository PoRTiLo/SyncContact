package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import cz.xsendl00.synccontact.activity.fragment.RowMergerAdapter;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.Mapping;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Activity for Merge contact.
 *
 * @author portilo
 */
@EActivity(R.layout.activity_contacts_merge)
public class ContactsMergeActivity extends Activity implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  /**
   * Bean for print info about duration function.
   */
  @Bean
  protected Utils util;

  @Bean
  protected ServerUtilities serverUtilities;

  private static final String TAG = "ContactsMergeActivity";

  private ListView listRow;
  private final Handler handler = new Handler();
  private RowMergerAdapter adapter;
  private ContactManager contactManager;
  private ProgressDialog progressDialog;
  private List<ContactRow> contactRowsActivity = new ArrayList<ContactRow>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    contactManager = ContactManager.getInstance(getApplicationContext());
  }

  @Override
  public void onResume() {
    super.onResume();
    mustInit();
  }

  /**
   * Check contact manager if is initialized.
   */
  private void mustInit() {
    if (!contactManager.isContactsServerInit() || !contactManager.isGroupsServerInit()) {
      progressDialog = ProgressDialog.show(ContactsMergeActivity.this,
          getText(R.string.progress_downloading), getText(R.string.progress_downloading_text), true);
      progressDialog.setCanceledOnTouchOutside(false);
      initTask();
    } else {
      init();
    }
  }

  /**
   * Initialize GUI. Set adapter, listRow and name button based on run application first or size
   * contact in listView.
   */
  @UiThread
  protected void init() {
    listRow = (ListView) findViewById(R.id.list_merge);
    if (contactRowsActivity != null) {
      if (contactRowsActivity.isEmpty()) {
        Button button = (Button) findViewById(R.id.contact_merge_button);
        button.setText("Skip");
      }
      adapter = new RowMergerAdapter(getApplicationContext(), contactRowsActivity, this);
    } else {
      if (contactManager.getContactsServer().isEmpty()) {
        Button button = (Button) findViewById(R.id.contact_merge_button);
        button.setText(getString(R.string.info_merge_button_skip));
      }
      adapter = new RowMergerAdapter(getApplicationContext(), contactManager.getContactsServer(),
          this);
    }
    listRow.setAdapter(adapter);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    final int pos = (Integer) buttonView.getTag();
    if (pos != ListView.INVALID_POSITION) {
      ContactRow p = contactRowsActivity.get(pos);
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
      }
    }
  }

  /**
   * Initialize data for list in GUI.
   */
  @Background
  protected void initTask() {
    contactManager.initContactsServer(handler);
    contactManager.initGroupsServer(handler);
    for (ContactRow contactRowServer : contactManager.getContactsServer()) {
      int pos = 0;
      for (ContactRow contactRowLocal : contactManager.getLocalContacts()) {
        if (contactRowServer.getName() != null && contactRowLocal.getName() != null
            && contactRowServer.getName().equals(contactRowLocal.getName())) {
          if (contactRowLocal.isSync()
              && !contactRowLocal.getUuid().equals(contactRowServer.getUuid())) {
            contactRowServer.setIdTable(pos);
            contactRowServer.setId(contactRowLocal.getId());
            contactRowsActivity.add(contactRowServer);
            break;
          }
        }
        pos++;
      }
    }
    if (ContactsMergeActivity.this.progressDialog != null) {
      ContactsMergeActivity.this.progressDialog.dismiss();
    }
    init();
  }

  /**
   * Go to next activity. Go to {@link InfoServerContactsActivity}. The next activity show info
   * about LDAP import contact.
   */
  private void go2NextActivity() {
    Intent intent = new Intent(this, InfoServerContactsActivity_.class);
    startActivity(intent);
  }

  /**
   * Call after click on button. Merge
   *
   * @param view actual view.
   */
  public void mergeContactLocalwithLDAP(@SuppressWarnings("unused") View view) {
    mergeLocalServerContacts();
    go2NextActivity();
  }

  /**
   *
   */
  @Background
  protected void mergeLocalServerContacts() {
    List<ContactRow> forDb = new ArrayList<ContactRow>();
    // create list for saving merged contacts
    for (ContactRow contactRow : contactRowsActivity) {
      if (contactRow.isSync()) {
        forDb.add(contactRow);
        int index = contactManager.getLocalContacts().indexOf(contactRow);
        if (index != -1) {
          contactManager.getLocalContacts().get(index).setUuid(contactRow.getUuid());
        }
      }
    }
    // update local contacts by uuid from server
    final List<ContactRow> contacts2UpdateDatabse = forDb;
    importContacts2Server(forDb);


    Thread updateContactDatabase = new Thread(new Runnable() {

      @Override
      public void run() {
        updateContactsDatabase(contacts2UpdateDatabse);
      }
    });
    updateContactDatabase.start();


    Thread mergeGroup = new Thread(new Runnable() {

      @Override
      public void run() {
        mergeGroup();
      }
    });
    mergeGroup.start();

    try {
      updateContactDatabase.join();
      mergeGroup.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Background
  protected void mergeGroup() {
    util.startTime(TAG, "mergeGroup");
    Log.i(TAG, "contactManager.getGroupsLocal()" + contactManager.getLocalGroups().size());
    Log.i(TAG, "contactManager.getGroupsServer()" + contactManager.getGroupsServer().size());
    List<GroupRow> groupRows = new ArrayList<GroupRow>();
    List<GroupRow> groupRowsUpload = new ArrayList<GroupRow>();
    for (GroupRow groupRowLocal : contactManager.getLocalGroups()) {
      boolean found = false;
      for (GroupRow groupRowServer : contactManager.getGroupsServer()) {
        Log.i(TAG, "groupRowServer:" + groupRowServer.getName() + ":" + groupRowServer.getUuid());
        Log.i(TAG, "groupRow:" + groupRowLocal.getName() + ":" + groupRowLocal.getUuid());
        if (groupRowLocal.getName().equals(groupRowServer.getName())
            && !groupRowLocal.getUuid().equals(groupRowServer.getUuid())) {
          groupRowLocal.setUuid(groupRowServer.getUuid());
          groupRows.add(groupRowLocal);
          found = true;
          break;
        } else if (groupRowLocal.getName().equals(groupRowServer.getName())) {
          found = true;
          break;
        }
      }
      if (!found) {
        groupRowsUpload.add(groupRowLocal);
      }
    }
    // groupRows = util.intersection(contactManager.getGroupsLocal(), groupRows);
    Log.i(TAG, "groupRows" + groupRows.size());
    updateGroupDatabase(groupRows);
    serverUtilities.addGroup2Server(new ServerInstance(contactManager.getAccountData()),
        getApplicationContext(), handler, groupRowsUpload);
    util.stopTime(TAG, "mergeGroup");
  }

  @Background
  public void updateGroupDatabase(final List<GroupRow> forDb) {
    Utils util1 = new Utils();
    util1.startTime(TAG, "updateGroupDatabase");
    new AndroidDB().updateGroupsUuid(getApplicationContext(), forDb);
    util1.stopTime(TAG, "updateGroupDatabase");
  }

  private void updateContactsDatabase(final List<ContactRow> forDb) {
    Utils util1 = new Utils();
    util1.startTime(TAG, "updateDatabase");
    new AndroidDB().updateContactsUuid(getApplicationContext(), forDb);
    util1.stopTime(TAG, "updateDatabase");
  }

  @Background
  public void importContacts2Server(final List<ContactRow> noImports) {
    Utils util1 = new Utils();
    util1.startTime(TAG, "importContacts2Server");
    List<ContactRow> contact2ServerUpload = new ArrayList<ContactRow>();
    for (ContactRow contactRow : contactManager.getLocalContacts()) {
      if (contactRow.isSync()) {
        Log.i(TAG, "ContactRow wil is checked:" + contactRow.toString());
        boolean used = true;
        // no import contact which are signed like contact on server
        for (ContactRow noImport : noImports) {
          if (noImport.getId().equals(contactRow.getId())) {
            used = false;
            break;
          }
        }
        if (used) {
          // not import contact which are on server, they are not show in previous for cycle in
          // noImports
          for (ContactRow serversContact : contactManager.getContactsServer()) {
            if (serversContact.getUuid().equals(contactRow.getUuid())) {
              used = false;
              break;
            }
          }
          if (used) {
            contact2ServerUpload.add(contactRow);
          }
        }
      }
    }
    Mapping mapping = new Mapping();
    List<GoogleContact> googleContacts = new ArrayList<GoogleContact>();
    for (ContactRow contactRow : contact2ServerUpload) {
      GoogleContact googleContact = mapping.mappingContactFromDB(getContentResolver(),
          contactRow.getId(), contactRow.getUuid());
      googleContacts.add(googleContact);
    }

    serverUtilities.addContactToServer(new ServerInstance(contactManager.getAccountData()),
        getApplicationContext(), handler, googleContacts);
    util1.stopTime(TAG, "importContacts2Server");
    contactManager.initContactsServer(handler);
    // contactManager.reloadContact();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.settings_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
      case R.id.action_help:
        intent = new Intent(this, HelpActivity_.class);
        intent.putExtra(Constants.INTENT_FIRST, true);
        startActivity(intent);
        break;
      case android.R.id.home:
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
          TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
        } else {
          NavUtils.navigateUpTo(this, upIntent);
        }
        return true;
      default:
        break;
    }

    return true;
  }
}
