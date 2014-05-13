package cz.xsendl00.synccontact;

import java.util.List;

import cz.xsendl00.synccontact.utils.ContactRow;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Show contacts of group.
 * 
 * @author portilo
 * 
 */
public class ContactsDetailActivity extends ListActivity {

  private static final String TAG = "ContactsDetailActivity";
  private String id;
  private List<ContactRow> list;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    Intent intent = getIntent();
    id = intent.getStringExtra("ID");
    String name = intent.getStringExtra("NAME");
    this.setTitle(name);
    init();
  }

  private void init() {
    new Load(id, ContactsDetailActivity.this).execute();
  }

  private void onTaskCompleted(List<ContactRow> list) {
    this.list = list;
    String[] values = new String[list.size()];
    int i = 0;
    for (ContactRow contactRow : list) {
      values[i++] = contactRow.getName();
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(adapter);
  }

  private class Load extends AsyncTask<Void, Void, List<ContactRow>> {

    private String id;
    private Activity activity;

    public Load(String id, Activity activ) {
      this.id = id;
      this.activity = activ;
    }

    @Override
    protected List<ContactRow> doInBackground(Void... params) {
      return ContactRow
          .fetchGroupMembersName(activity.getContentResolver(), id);
    }

    protected void onPostExecute(List<ContactRow> contacts) {
      ((ContactsDetailActivity) activity).onTaskCompleted(contacts);
    }

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
      intent = new Intent(this, HelpActivity.class);
      startActivity(intent);
      break;
    case R.id.action_settings:
      intent = new Intent(this, SettingsActivity.class);
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
    ContactRow contactRow = list.get(position);
    Uri contactUri = ContentUris.withAppendedId(
        ContactsContract.Contacts.CONTENT_URI,
        Integer.valueOf(contactRow.getId()));
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(contactUri);
    Log.i(TAG, "Open detail of: " + contactRow.getName() + ", id_contact:" + contactRow.getId());
    startActivity(i);
  }
}
