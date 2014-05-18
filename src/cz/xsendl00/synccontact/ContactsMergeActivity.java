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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import cz.xsendl00.synccontact.activity.fragment.RowMergerAdapter;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import cz.xsendl00.synccontact.utils.Mapping;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Activity for Merge contact.
 * @author portilo
 *
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
  private List<ContactRow> contactRows = new ArrayList<ContactRow>();
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
      initTask();
    } else {
      init();
    }
  }

  /**
   * Initialize GUI. Set adapter, listRow and name button based on run application first or size contact in listView.
   */
  @UiThread
  protected void init() {
    listRow = (ListView) findViewById(R.id.list_merge);
    if (contactRows != null) {
      if (contactRows.isEmpty()) {
        Button button = (Button) findViewById(R.id.contact_merge_button);
        button.setText("Skip");
      }
      adapter = new RowMergerAdapter(getApplicationContext(), contactRows, this);
    } else {
      if (contactManager.getContactsServer().isEmpty()) {
        Button button = (Button) findViewById(R.id.contact_merge_button);
        button.setText(getString(R.string.info_merge_button_skip));
      }
      adapter = new RowMergerAdapter(getApplicationContext(), contactManager.getContactsServer(), this);
    }
    listRow.setAdapter(adapter);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    final int pos = (Integer) buttonView.getTag();
    if (pos != ListView.INVALID_POSITION) {
      ContactRow p = contactRows.get(pos);
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
   Thread serverThread =  new Thread(new Runnable() {
      @Override
      public void run() {
        contactManager.initContactsServer(handler);
        contactManager.initGroupsServer(handler);
      }
    });
   serverThread.start();

   Thread contactThread =  new Thread(new Runnable() {
     @Override
     public void run() {
       //contactManager.reloadContact();
       //contactManager.reloadGroup();
     }
   });
   contactThread.start();

   try {
    serverThread.join();
    contactThread.join();
  } catch (InterruptedException e) {
    e.printStackTrace();
  }


   new Thread(new Runnable() {
     @Override
     public void run() {
       for (ContactRow contactRow : contactManager.getContactsServer()) {
         int pos = 0;
         for (ContactRow contactRowLocal : contactManager.getContactsLocal()) {
           if (contactRow.getName() != null && contactRowLocal.getName() != null
               && contactRow.getName().equals(contactRowLocal.getName())) {
             if (contactRowLocal.isSync() && !contactRowLocal.getUuid().equals(contactRow.getUuid())) {
               contactRow.setIdTable(pos);
               contactRow.setId(contactRowLocal.getId());
               contactRows.add(contactRow);
               break;
             }
           }
           pos++;
         }
       }
     }
   }).start();
   if (ContactsMergeActivity.this.progressDialog != null) {
     ContactsMergeActivity.this.progressDialog.dismiss();
   }
   init();
  }

  /**
   * Go to next activity. Go to {@link InfoServerContactsActivity}.
   * The next activity show info about LDAP import contact.
   */
  private void go2NextActivity() {
    Intent intent = new Intent(this, InfoServerContactsActivity_.class);
    startActivity(intent);
  }

  /**
   * Call after click on button. Merge
   * @param view actual view.
   */
  public void mergeContactLocalwithLDAP(@SuppressWarnings("unused") View view) {
    merge();
    go2NextActivity();
  }

  /**
   *
   */
  @Background
  protected void merge() {
    List<ContactRow> forDb = new ArrayList<ContactRow>();
    // create list for saving merged contacts
    for (ContactRow contactRow : contactRows) {
      if (contactRow.isSync()) {
        forDb.add(contactRow);
        int index = contactManager.getContactsLocal().indexOf(contactRow);
        if (index != -1) {
          contactManager.getContactsLocal().get(index).setUuid(contactRow.getUuid());
        }
      }
    }
    // update local contacts by uuid from server
    final List<ContactRow> f = forDb;
    importContacts2Server(forDb);


    Thread updateDatabase =  new Thread(new Runnable() {
      @Override
      public void run() {
        updateDatabase(f);
      }
    });
    updateDatabase.start();


    Thread mergeGroup =  new Thread(new Runnable() {
      @Override
      public void run() {
        mergeGroup();
      }
    });
    mergeGroup.start();

    try {
      updateDatabase.join();
      mergeGroup.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    //contactManager.reloadGroup();
    //for (GroupRow groupRow : contactManager.getGroupsLocal()) {
    //  uploadGroup(groupRow);
    //}
  }

  @Background
  protected void mergeGroup() {
    util.startTime(TAG, "mergeGroup");
    Log.i(TAG, "contactManager.getGroupsLocal()" + contactManager.getGroupsLocal().size());
    Log.i(TAG, "contactManager.getGroupsServer()" + contactManager.getGroupsServer().size());
    List<GroupRow> groupRows = new ArrayList<GroupRow>();
    List<GroupRow> groupRowsUpload = new ArrayList<GroupRow>();
    for (GroupRow groupRow : contactManager.getGroupsLocal()) {
      boolean found = false;
      for (GroupRow groupRowServer : contactManager.getGroupsServer()) {
        Log.i(TAG, "groupRowServer:" + groupRowServer.getName() + ":" + groupRowServer.getUuid());
        Log.i(TAG, "groupRow:" + groupRow.getName() + ":" + groupRow.getUuid());
        if (groupRow.getName().equals(groupRowServer.getName())
            && !groupRow.getUuid().equals(groupRowServer.getUuid())) {
          groupRow.setUuid(groupRowServer.getUuid());
          groupRows.add(groupRow);
          found = true;
          break;
        } else if (groupRow.getName().equals(groupRowServer.getName())) {
          found = true;
        }
      }
      if (!found) {
        groupRowsUpload.add(groupRow);
      }
    }
    //groupRows = util.intersection(contactManager.getGroupsLocal(), groupRows);
    Log.i(TAG, "groupRows" + groupRows.size());
    updateGroupDatabase(groupRows);
    for (GroupRow groupRow : groupRowsUpload) {
      uploadGroup(groupRow);
    }
    util.stopTime(TAG, "mergeGroup");
  }

  @Background
  public void updateGroupDatabase(final List<GroupRow> forDb) {
    util.startTime(TAG, "updateGroupDatabase");
    HelperSQL db = new HelperSQL(getApplicationContext());
    db.updateGroupsUuid(forDb);
    util.stopTime(TAG, "updateGroupDatabase");
  }

  @Background
  public void updateDatabase(final List<ContactRow> forDb) {
    util.startTime(TAG, "updateDatabase");
    HelperSQL db = new HelperSQL(getApplicationContext());
    db.updateContactsUuid(forDb);
    //contactManager.initGroupsContacts();
    util.stopTime(TAG, "updateDatabase");
  }

  @Background
  protected void uploadGroup(GroupRow groupRow) {
    util.startTime(TAG, "uploadGroup:" + groupRow.toString());
    serverUtilities.addGroup2Server(new ServerInstance(contactManager.getAccountData()),
        getApplicationContext(), handler, groupRow);
    util.stopTime(TAG, "uploadGroup");
  }

  @Background
  public void importContacts2Server(final List<ContactRow> noImports) {
    util.startTime(TAG, "importContacts2Server");
    for (ContactRow contactRow : contactManager.getContactsLocal()) {
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
          // not import contact which are on server, they are not show in previous for cycle in noImports
          for (ContactRow serversContact : contactManager.getContactsServer()) {
            if (serversContact.getUuid().equals(contactRow.getUuid())) {
              used = false;
              break;
            }
          }
          if (used) {
            uploadContact(contactRow);
          }
        }
      }
    }
    util.stopTime(TAG, "importContacts2Server");
    contactManager.initContactsServer(handler);
    //contactManager.reloadContact();
  }
  @Background
  public void uploadContact(final ContactRow contactRow2) {
    Mapping mapping = new Mapping();
    GoogleContact googleContact = mapping.mappingContactFromDB(getContentResolver(),
        contactRow2.getId(), contactRow2.getUuid());
    serverUtilities.addContactToServer(
        new ServerInstance(contactManager.getAccountData()),
        getApplicationContext(), handler, googleContact);
  }
}
