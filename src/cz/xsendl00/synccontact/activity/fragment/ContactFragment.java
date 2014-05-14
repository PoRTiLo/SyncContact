package cz.xsendl00.synccontact.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.androidannotations.annotations.EFragment;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.ListView;
import cz.xsendl00.synccontact.ContactsActivity;
import cz.xsendl00.synccontact.HelpActivity;
import cz.xsendl00.synccontact.R;
import cz.xsendl00.synccontact.SettingsActivity;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.AndroidDB;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;

/**
 * Fragment for contact data.
 *
 * @author portilo
 *
 */
@EFragment
public class ContactFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private ContactsActivity activity;
  private ListView listRow;
  private RowContactAdapter adapter;
  private boolean first = false;
  private static boolean selectAll;
  private ContactManager contactManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    activity = (ContactsActivity) getActivity();
    first = activity.isFirst();
    contactManager = ContactManager.getInstance(activity);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View rootView = null;
    if (first) {
      rootView = inflater.inflate(R.layout.fragment_contact, container, false);
    } else {
      rootView = inflater.inflate(R.layout.fragment_contact_simply, container,
          false);
    }
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    isSelectedAll();
    listRow = (ListView) getActivity().findViewById(R.id.list_contact);
    adapter = new RowContactAdapter(getActivity().getApplicationContext(),
        contactManager.getContactsLocal(), this);
    listRow.setAdapter(adapter);

    if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
          int idContact = AndroidDB.getIdContact(getActivity(),
              contactManager.getContactsLocal().get(position).getId());
          Uri contactUri = ContentUris.withAppendedId(
              ContactsContract.Contacts.CONTENT_URI, idContact);
          Intent i = new Intent(Intent.ACTION_VIEW);
          i.setData(contactUri);
          startActivity(i);

        }
      });
    }
  }

  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    MenuItem item = menu.findItem(R.id.action_select);
    String newText = !selectAll ? "Select all" : "No select";
    item.setTitle(newText);
    super.onPrepareOptionsMenu(menu);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
    case R.id.action_refresh:
      ((ContactsActivity) getActivity()).reinitData();
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
      selectAll = selectAll ? false : true;
      selectAll(selectAll);
      break;
    case android.R.id.home:
      break;
    default:
      break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void isSelectedAll() {
    for (ContactRow contactRow : contactManager.getContactsLocal()) {
      if (!contactRow.isSync()) {
        selectAll = false;
        break;
      }
    }
  }

  private void selectAll(final boolean result) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        updateDB(contactManager.getContactsLocal(), result);
      }
    }).start();
    for (ContactRow contactRow : contactManager.getContactsLocal()) {
      contactRow.setSync(result);
    }
    adapter.notifyDataSetChanged();
    new Thread(new Runnable() {
      @Override
      public void run() {
        for (GroupRow groupRow : contactManager.getGroupsLocal()) {
          groupRow.setSync(result);
          HelperSQL db = new HelperSQL(getActivity());
          db.updateGroupSync(groupRow);
        }
      }
    }).start();
  }

  private void updateDB(final List<ContactRow> contacts,
      final boolean result) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        HelperSQL db = new HelperSQL(getActivity());
        db.updateContactsSync(contacts, result);
      }
    }).start();
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView,
      final boolean isChecked) {
    final int pos = (Integer) buttonView.getTag();
    if (pos != ListView.INVALID_POSITION) {
      ContactRow p = contactManager.getContactsLocal().get(pos);
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
        ArrayList<ContactRow> contacts = new ArrayList<ContactRow>();
        contacts.add(p);
        updateDB(contacts, isChecked);
      }
    }
  }

}