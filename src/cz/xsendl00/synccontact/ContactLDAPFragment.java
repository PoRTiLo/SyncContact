package cz.xsendl00.synccontact;

import java.util.List;

import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;


/**
 * Fragment for contact.
 * 
 * @author portilo
 *
 */
public class ContactLDAPFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "ContactLDAPFragment";
  private LDAPContactActivity activity;
  private ListView listRow;
  private RowLDAPContactAdapter adapter;
  
  private static boolean selectAll;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    
    activity = (LDAPContactActivity) getActivity();
    selectAll = selected();
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = null;
    rootView = inflater.inflate(R.layout.fragment_contact, container, false);
    return rootView;
  }

  public void onResume() {
    super.onResume();
    listRow = (ListView) getActivity().findViewById(R.id.list_contact);
    Log.i(TAG, "size in ContactLDAPFragment :" + activity.getPair().getContactList().size());
    adapter = new RowLDAPContactAdapter(getActivity().getApplicationContext(), activity.getPair().getContactList(), this);
    listRow.setAdapter(adapter);
  }
  
  private boolean selected() {
    HelperSQL db = new HelperSQL(activity);
    List<ContactRow> contactRowDb = db.getContactsSync();
    int countSelect = 0;
    for (ContactRow contactRow : activity.getPair().getContactList()) {
      int i = 0;
      boolean found = false;
      for (ContactRow contactRow2 : contactRowDb) {
        if (contactRow.getUuid().equals(contactRow2.getUuid())) {
          contactRow.setSync(true);
          contactRowDb.remove(i);
          found = true;
          countSelect++;
          break;
        }
        i++;
      }
      if (!found) {
        contactRow.setSync(false);
      }
    }
    return selectAll = countSelect == activity.getPair().getContactList().size();
  }
  
  private void selectAll(final boolean result) {
    for (ContactRow contactRow : activity.getPair().getContactList()) {
      contactRow.setSync(result);
    }
    adapter.notifyDataSetChanged();
  }
  
  /**
   * 
   */
  public void onPrepareOptionsMenu (Menu menu) {
    MenuItem item = menu.findItem(R.id.action_select);
    String newText = !selectAll ? "Select all" : "No select";
    item.setTitle(newText);
    super.onPrepareOptionsMenu(menu);
  }
  
  
  /**
   * On selecting action bar icons
   * */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
      case R.id.action_refresh:
        ((LDAPContactActivity) getActivity()).update();
        break;
      case R.id.action_help:
        intent = new Intent(getActivity(), HelpActivity.class);
        startActivity(intent);
        break;
      case R.id.action_settings:
        intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.action_select:
        selectAll = selectAll ? false : true;
        selectAll(selectAll);
        break;
      case android.R.id.home:
        // TODO 
        Log.i("taddy:", "taddyyy");
        break;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    final int pos = (Integer) buttonView.getTag();
    if (pos != ListView.INVALID_POSITION) {
      ContactRow p = activity.getPair().getContactList().get(pos);
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
      }
    }
  }
}