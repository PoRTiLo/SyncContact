package cz.xsendl00.synccontact;

import java.util.ArrayList;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Fragment for contact data.
 * @author portilo
 *
 */
public class ContactFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private SelectContactListActivity activity;
  private ListView listRow;
  private RowContactAdapter adapter;
  private boolean first = false;
  private static boolean selectAll;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    activity = (SelectContactListActivity) getActivity();
    first = activity.isFirst();
    selectAll = isSelectedAll();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View rootView = null;
    if (first) {
      rootView = inflater.inflate(R.layout.fragment_contact, container, false);
    } else {
      rootView = inflater.inflate(R.layout.fragment_contact_simply, container, false);
    }
    return rootView;
  }

  public void onResume() {
    super.onResume();
    listRow = (ListView) getActivity().findViewById(R.id.list_contact);
    adapter = new RowContactAdapter(getActivity().getApplicationContext(), activity.getPair().getContactList(), this);
    listRow.setAdapter(adapter);

    if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
          Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, 1); 
          Intent i = new Intent(Intent.ACTION_VIEW);
          i.setData(contactUri);
          startActivityForResult(i, 1);
        }
      });
    }
  }
  
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
        ((SelectContactListActivity) getActivity()).update();
        break;
      case R.id.action_add_group:
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
        //String newText = selectAll ? "Select all" : "No select";
        selectAll = selectAll ? false : true;
        selectAll(selectAll);
        //item.setTitle(newText);
        break;
      case android.R.id.home:
        // TODO
        Log.i("Conatctfragmemnt", "jsem tuuuuuuuuuuuuuuuuuuuuu");
        break;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private boolean isSelectedAll() {
    boolean selectAll = true;
    for (ContactRow contactRow : activity.getPair().getContactList()) {
      if (!contactRow.isSync()) {
        selectAll =false;
        break;
      }
    }
    return selectAll;
  }
  
  private void selectAll(final boolean result) {
    new Thread(new Runnable() {
      public void run() {
        updateDB(activity.getPair().getContactList(), result);
      }
    }).start();
    for (ContactRow contactRow : activity.getPair().getContactList()) {
      contactRow.setSync(result);
    }
    adapter.notifyDataSetChanged();
    new Thread(new Runnable() {
      public void run() {
        for (GroupRow groupRow : activity.getPair().getGroupsList()) {
          groupRow.setSync(result);
          HelperSQL db = new HelperSQL(getActivity());
          db.updateGroupSync(groupRow);
        }
      }
    }).start();
  }
  
  private void updateDB(final ArrayList<ContactRow> contacts, final boolean result) {
    new Thread(new Runnable() {
      public void run() {
        HelperSQL db = new HelperSQL(getActivity());
        db.updateContactsSync(contacts, result);
      }
    }).start();
  }
  
  @Override
  public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
    final int pos = (Integer) buttonView.getTag();
    if (pos != ListView.INVALID_POSITION) {
      ContactRow p = activity.getPair().getContactList().get(pos);
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
        ArrayList<ContactRow> contacts = new ArrayList<ContactRow>();
        contacts.add(p);
        updateDB(contacts, isChecked);
      }
    }
  }

}
