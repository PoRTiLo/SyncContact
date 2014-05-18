package cz.xsendl00.synccontact;

import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;

/**
 * Show contacts of group.
 *
 * @author portilo
 */
@EActivity
public class ContactsDetailActivity extends ListActivity {

  @Bean
  protected AndroidDB androidDB;
  /**
   * ProgressBar show by loading data.
   */
  @ViewById(R.id.activit_contacts_layout)
  protected LinearLayout linearLayoutProgress;
  private static final String TAG = "ContactsDetailActivity";
  private ContactManager contactManager;
  private String groupId;
  private boolean first;
  private List<ContactRow> contactRows;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    Intent intent = getIntent();
    groupId = intent.getStringExtra(Constants.INTENT_ID);
    first = intent.getBooleanExtra(Constants.INTENT_FIRST, false);
    String groupName = intent.getStringExtra(Constants.INTENT_NAME);
    this.setTitle(groupName);
    contactManager = ContactManager.getInstance(ContactsDetailActivity.this);
    if (contactManager.getGroupsContacts() == null || contactManager.getGroupsContacts().isEmpty()) {
      loadData();
    } else {
      init();
    }
  }

  /**
   * Initialize data for showing list of names.
   */
  @UiThread
  public void init() {
    this.contactRows = contactManager.getGroupsContacts().get(groupId);
    String[] values = new String[this.contactRows.size()];
    int i = 0;
    for (ContactRow contactRow : this.contactRows) {
      values[i++] = contactRow.getName();
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(adapter);
  }

  /**
   * Load data into {@link ContactManager}.
   */
  @Background
  public void loadData() {
    contactManager.initGroupsContacts();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    if (first) {
      inflater.inflate(R.menu.settings_menu, menu);
    } else {
      inflater.inflate(R.menu.sync_menu, menu);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
      case R.id.action_help:
        intent = new Intent(this, HelpActivity_.class);
        if (first) {
          intent.putExtra(Constants.INTENT_FIRST, true);
        }
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
    ContactRow contactRow = contactRows.get(position);
    int idContact = androidDB.getIdContact(ContactsDetailActivity.this,
        contactManager.getContactsLocal().get(position).getId());
    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, idContact);
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(contactUri);
    Log.i(TAG, "Open detail of: " + contactRow.getName() + ", id_contact:" + contactRow.getId());
    startActivity(i);
  }
}
