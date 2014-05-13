package cz.xsendl00.synccontact.activity.first;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import cz.xsendl00.synccontact.R;
import cz.xsendl00.synccontact.activity.fragment.RowMergerAdapter;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;

/**
 * Activity for Merge contact.
 * @author portilo
 *
 */
public class ContactMergeActivity extends Activity implements
android.widget.CompoundButton.OnCheckedChangeListener {
  
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
    setContentView(R.layout.activity_contact_merge);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    contactManager = ContactManager.getInstance(getApplicationContext());
  }
  
  @Override
  public void onResume() {
    super.onResume();
    mustInit();

    /*if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
          int idContact = AndroidDB.getIdContact(getActivity(), activity.getPair().getContactList().get(position).getId());
          Uri contactUri = ContentUris.withAppendedId(
              ContactsContract.Contacts.CONTENT_URI, idContact);
          Intent i = new Intent(Intent.ACTION_VIEW);
          i.setData(contactUri);
          startActivity(i);
          
        }
      });
    }*/
  }

  private void mustInit() {
    
    if(!contactManager.isContactsLDAPInit()) {
      progressDialog = ProgressDialog.show(ContactMergeActivity.this,
        Constants.AC_LOADING, Constants.AC_LOADING_TEXT_DB, true);
      new LoadTask(ContactMergeActivity.this).execute();
    } else {
      init();
    }
  }
  
  public void onLoadCompleted() {
    Log.i(TAG, "onLoadCompleted");
    init();
  }
  
  private void init() {
    Log.i(TAG, "init");
    listRow = (ListView) findViewById(R.id.list_merge);

    if (contactRows != null) {
      if (contactRows.isEmpty()) {
        Button button = (Button) findViewById(R.id.contact_merge_button);
        button.setText("Skip");
      }
      Log.i(TAG, "contactRows neni null");
      adapter = new RowMergerAdapter(getApplicationContext(), contactRows, this);
    } else {
      if (contactManager.getContactListLDAP().isEmpty()) {
        Button button = (Button) findViewById(R.id.contact_merge_button);
        button.setText("Skip");
      }
      adapter = new RowMergerAdapter(getApplicationContext(), contactManager.getContactListLDAP(), this);
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
   * Load contact from LDAP server, only name.
   * @author portilo
   *
   */
  private class LoadTask extends AsyncTask<Void, Void, Boolean> {
    ;
    private Activity activity;

    public LoadTask(Activity activity) {
      this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      contactManager.initLDAP(handler);
      // 
      //new Thread(new Runnable() {
      //  public void run() {
          for (ContactRow contactRow : contactManager.getContactListLDAP()) {
            int pos = 0;
            for (ContactRow contactRowLocal : contactManager.getContactList()) {
              if (contactRow.getName().equals(contactRowLocal.getName())) {
                if (contactRowLocal.getUuid() == null || !contactRowLocal.getUuid().equals(contactRow.getUuid())) {
                  contactRow.setIdTable(pos);
                  //contactRow.setId(contactRowLocal.getId());
                  contactRows.add(contactRow);
                  break;
                }
              }
              pos++;
            }
          }
      //  }
      //}).start();
      
      return null;
    }

    protected void onPostExecute(Boolean bool) {
      if (ContactMergeActivity.this.progressDialog != null) {
        Log.i(TAG, "dismiss");
        ContactMergeActivity.this.progressDialog.dismiss();
      }
      ((ContactMergeActivity) activity).onLoadCompleted();
    }

    protected void onPreExecute() {
      super.onPreExecute();

    }
  }

  public void nextActivity() {
    Intent intent = new Intent(this, InfoLDAPActivity.class);
    startActivity(intent);
  }

  public void merge(View view) {
    new Thread(new Runnable() {
      public void run() {
        List<ContactRow> forDb = new ArrayList<ContactRow>();
        for (ContactRow contactRow : contactRows) {
          if (contactRow.isSync()) {
            ContactRow contactRowLocal = contactManager.getContactList().get(contactRow.getIdTable());
            contactRow.setId(contactRowLocal.getId());
            forDb.add(contactRow);
          }
        }
        HelperSQL db = new HelperSQL(getApplicationContext());
        db.updateContactsUuid(forDb);
        contactManager.reloadContact();
      }
    }).start();
    nextActivity();
  }
  
}
