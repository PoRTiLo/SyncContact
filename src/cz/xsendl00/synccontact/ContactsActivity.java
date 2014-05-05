package cz.xsendl00.synccontact;

import java.util.ArrayList;

import cz.xsendl00.synccontact.utils.ContactRow;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class ContactsActivity extends ListActivity {

  private String id;

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
    new Load(id, ContactsActivity.this).execute();
  }

  private void onTaskCompleted(ArrayList<ContactRow> list) {
    String[] values = new String[list.size()];
    int i = 0;
    for (ContactRow c : list) {
      values[i++] = c.getName();
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(adapter);
  }

  private class Load extends AsyncTask<Void, Void, ArrayList<ContactRow>> {

    private String id;
    private Activity activity;

    public Load(String id, Activity activ) {
      this.id = id;
      this.activity = activ;
    }

    @Override
    protected ArrayList<ContactRow> doInBackground(Void... params) {
      ArrayList<ContactRow> a = ContactRow.fetchGroupMembers(
          activity.getContentResolver(), id);
      return a;
    }

    protected void onPostExecute(ArrayList<ContactRow> p) {
      ((ContactsActivity) activity).onTaskCompleted(p);
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
}
