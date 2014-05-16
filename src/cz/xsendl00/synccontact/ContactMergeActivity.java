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
import cz.xsendl00.synccontact.authenticator.AccountData;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.contact.GoogleContact;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.ldap.ServerInstance;
import cz.xsendl00.synccontact.ldap.ServerUtilities;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.Mapping;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Activity for Merge contact.
 * @author portilo
 *
 */
@EActivity(R.layout.activity_contact_merge)
public class ContactMergeActivity extends Activity implements
android.widget.CompoundButton.OnCheckedChangeListener {

  /**
   * Bean for print info about duration function.
   */
  @Bean
  protected Utils util;

  private static final String TAG = "ContactMergeActivity";

  private ListView listRow;
  private final Handler handler = new Handler();
  private RowMergerAdapter adapter;
  private ContactManager contactManager;
  private ProgressDialog progressDialog;
  private List<ContactRow> contactRows = new ArrayList<ContactRow>();;

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
    if (!contactManager.isContactsServerInit()) {
      progressDialog = ProgressDialog.show(ContactMergeActivity.this,
        getText(R.string.progress_downloading), getText(R.string.progress_downloading_text), true);
      //new InitTask(ContactMergeActivity.this).execute();
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
        button.setText("Skip");
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
  public void initTask() {
   Thread serverThread =  new Thread(new Runnable() {
      @Override
      public void run() {
        contactManager.initContactsServer(handler);
      }
    });
   serverThread.start();

   Thread contactThread =  new Thread(new Runnable() {
     @Override
     public void run() {
       contactManager.reloadContact();
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
           if (contactRow.getName().equals(contactRowLocal.getName())) {
             Log.i(TAG, contactRowLocal.getName() + ":" + contactRowLocal.getUuid() + contactRow.getUuid());
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
   if (ContactMergeActivity.this.progressDialog != null) {
     ContactMergeActivity.this.progressDialog.dismiss();
   }
   init();
  }

  /**
   * Load contact from LDAP server, only name.
   * @author portilo
   *
   */
//  private class InitTask extends AsyncTask<Void, Void, Boolean> {
//
//    private Activity activity;
//    public InitTask(Activity ac) {
//      activity = ac;
//    }
//
//    @Override
//    protected Boolean doInBackground(Void... params) {
//      contactManager.initContacsServer(handler);
//      // read contact from local database
//      contactManager.reloadContact();
//      for (ContactRow contactRow : contactManager.getContactsServer()) {
//        int pos = 0;
//        for (ContactRow contactRowLocal : contactManager.getContactsLocal()) {
//          if (contactRow.getName().equals(contactRowLocal.getName())) {
//            Log.i(TAG, contactRowLocal.getName() + ":" + contactRowLocal.getUuid() + contactRow.getUuid());
//            if (contactRowLocal.isSync() && !contactRowLocal.getUuid().equals(contactRow.getUuid())) {
//              contactRow.setIdTable(pos);
//              contactRow.setId(contactRowLocal.getId());
//              contactRows.add(contactRow);
//              break;
//            }
//          }
//          pos++;
//        }
//      }
//      return null;
//    }
//
//    @Override
//    protected void onPostExecute(Boolean bool) {
//      if (ContactMergeActivity.this.progressDialog != null) {
//        ContactMergeActivity.this.progressDialog.dismiss();
//      }
//      ((ContactMergeActivity) activity).onLoadCompleted();
//    }
//  }

  /**
   * Go to next activity. Go to {@link InfoLDAPActivity}. The next activity show info about LDAP import contact.
   */
  public void go2NextActivity() {
    Intent intent = new Intent(this, InfoLDAPActivity_.class);
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

  @Background
  public void merge() {
    List<ContactRow> forDb = new ArrayList<ContactRow>();
    // create list for saving merged contacts
    for (ContactRow contactRow : contactRows) {
      if (contactRow.isSync()) {
        forDb.add(contactRow);
      }
    }
    // update local contacts by uuid from server
    updateDatabase(forDb);
    importContacts2Server(forDb);
  }

  @Background
  public void updateDatabase(final List<ContactRow> forDb) {
    util.startTime(TAG, "updateDatabase");
    HelperSQL db = new HelperSQL(getApplicationContext());
    db.updateContactsUuid(forDb);
    util.stopTime(TAG, "updateDatabase");
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
            upload(contactRow);
          }
        }
      }
    }
    util.stopTime(TAG, "importContacts2Server");
    contactManager.initContactsServer(handler);
    contactManager.reloadContact();
  }
  @Background
  public void upload(final ContactRow contactRow2) {
    Mapping mapping = new Mapping();
    GoogleContact googleContact = mapping.mappingContactFromDB(getContentResolver(),
        contactRow2.getId(), contactRow2.getUuid());
    new ServerUtilities().addContactToServer(
        new ServerInstance(AccountData.getAccountData(getApplicationContext())),
        getApplicationContext(), googleContact);
  }

}
