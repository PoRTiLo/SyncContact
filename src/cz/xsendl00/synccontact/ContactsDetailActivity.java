package cz.xsendl00.synccontact;

import java.util.ArrayList;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.Utils;

/**
 * Show contacts of group.
 *
 * @author xsendl00
 */
@EActivity
public class ContactsDetailActivity extends ListActivity {

  @Bean
  protected AndroidDB androidDB;
  private static final String TAG = "ContactsDetailActivity";
  private ContactManager contactManager;
  private Integer groupId;
  private boolean isSync;
  //private List<ContactRow> contactRows;
  private ArrayAdapter<ContactRow> adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    Intent intent = getIntent();
    groupId = intent.getIntExtra(Constants.INTENT_ID, 0);
    isSync = intent.getBooleanExtra(Constants.INTENT_SYNC, true);
    String groupName = intent.getStringExtra(Constants.INTENT_NAME);
    this.setTitle(groupName);
    contactManager = ContactManager.getInstance(ContactsDetailActivity.this);
    if (contactManager.getLocalGroupsContacts() == null || contactManager.getLocalGroupsContacts().size() < 0) {
      loadData();
    } else {
      init();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (!contactManager.isLocalGroupsContactsInit()) {
      new Thread(new Runnable() {

        @Override
        public void run() {
          contactManager.getLocalGroupsContacts();
          reloadGui();
        }
      }).start();
    } else {
      reloadGui();
    }
  }

  /**
   * Reload GUI.
   */
  @UiThread
  public void reloadGui() {
    adapter.notifyDataSetChanged();
    Log.i(TAG, "datachange" + contactManager.getLocalGroupsContacts().get(groupId));
    init();
  }

  /**
   * Initialize data for showing list of names.
   */
  @UiThread
  public void init() {
    if (contactManager.getLocalGroupsContacts().get(groupId) != null) {
      adapter = new ArrayAdapter<ContactRow>(this,
          android.R.layout.simple_list_item_1, contactManager.getLocalGroupsContacts().get(groupId));
      setListAdapter(adapter);
    } else {
      adapter = new ArrayAdapter<ContactRow>(this, android.R.layout.simple_list_item_1);
      setListAdapter(adapter);
    }
    adapter.setNotifyOnChange(true);
  }

  /**
   * Load data into {@link ContactManager}.
   * @return thread
   */
  public Thread loadData() {
    final ProgressDialog progressDialog = ProgressDialog.show(ContactsDetailActivity.this, "",
        getText(R.string.progress_loading), true);
    progressDialog.setCanceledOnTouchOutside(false);
    final Runnable runnable = new Runnable() {
      @Override
      public void run() {
        contactManager.getLocalGroupsContacts();
        progressDialog.dismiss();
      }
    };
    return Utils.performOnBackgroundThread(runnable);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.contacts_menu_detail, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
      case R.id.action_add_poeple:
        intent = new Intent(this, ContactsDetailAddActivity_.class);
        ArrayList<Integer> values = null;
        if (contactManager.getLocalGroupsContacts().get(groupId) != null) {
          values = new ArrayList<Integer>();
          for (ContactRow contactRow : this.contactManager.getLocalGroupsContacts().get(groupId)) {
            values.add(contactRow.getId());
          }
        }
        intent.putExtra(Constants.INTENT_ID, groupId);
        intent.putExtra(Constants.INTENT_SYNC, isSync);
        intent.putIntegerArrayListExtra(Constants.INTENT_SELECTED, values);
        startActivity(intent);
        break;
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

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    ContactRow contactRow = contactManager.getLocalGroupsContacts().get(groupId).get(position);
    int idContact = androidDB.getIdContact(ContactsDetailActivity.this,
        contactManager.getLocalGroupsContacts().get(groupId).get(position).getId());
    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idContact);
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(contactUri);
    Log.i(TAG, "Open detail of: " + contactRow.getName() + ", id_contact:" + contactRow.getId());
    startActivity(i);
  }
}
