package cz.xsendl00.synccontact.activity.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import cz.xsendl00.synccontact.ContactsActivity;
import cz.xsendl00.synccontact.ContactsDetailActivity;
import cz.xsendl00.synccontact.HelpActivity;
import cz.xsendl00.synccontact.R;
import cz.xsendl00.synccontact.SettingsActivity;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;

/**
 * Fragmnet of group.
 * @author portilo
 *
 */

@EFragment
public class GroupFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "GroupFrgment";
  private ListView listRow;
  private RowGroupAdapter adapter;
  private boolean first = false;
  private static boolean selectAll;
  private ContactsActivity activity;
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
    for (GroupRow groupRow : contactManager.getGroupsLocal()) {
      if (!groupRow.isSync()) {
        selectAll = false;
        break;
      }
    }
  }

  private void selectAll(final boolean result) {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        for (GroupRow groupRow : contactManager.getGroupsLocal()) {
          groupRow.setSync(result);
        }
        adapter.notifyDataSetChanged();
      }
    });

    new Thread(new Runnable() {
      @Override
      public void run() {
        updateDB(result, contactManager.getGroupsLocal());
      }
    }).start();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View rootView = null;
    if (first) {
      rootView = inflater.inflate(R.layout.fragment_group, container, false);
    } else {
      rootView = inflater.inflate(R.layout.fragment_group_simply, container, false);
    }

    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (!contactManager.isContactsLocalInit() || !contactManager.isGroupsLocalInit()) {
      initContactManager();
    }
    isSelectedAll();
    listRow = (ListView) getActivity().findViewById(R.id.list_group);
    adapter = new RowGroupAdapter(getActivity().getApplicationContext(),
        contactManager.getGroupsLocal(), this);
    listRow.setAdapter(adapter);
    if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
          if (contactManager.getGroupsLocal().get(position).getSize() != 0) {
            Intent i = new Intent(getActivity().getApplicationContext(),
                ContactsDetailActivity.class);
            i.putExtra("ID", contactManager.getGroupsLocal().get(position).getId());
            i.putExtra("NAME", contactManager.getGroupsLocal().get(position).getName());
            startActivity(i);
          } else {
            Toast toast = Toast.makeText(getActivity(), R.string.group_toast,
                Toast.LENGTH_SHORT);
            toast.show();
          }
        }
      });
    }
  }

  @Override
  public void onCheckedChanged(final CompoundButton buttonView,
      final boolean isChecked) {
    int pos = (Integer) buttonView.getTag();
    if (pos != ListView.INVALID_POSITION) {
      GroupRow p = contactManager.getGroupsLocal().get(pos);
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
        ArrayList<GroupRow> groups = new ArrayList<GroupRow>();
        groups.add(p);
        updateDB(isChecked, groups);
      }
    }
  }

  private void updateDB(final boolean isChecked, final List<GroupRow> groups) {
    final ContentResolver contentResolver =  getActivity().getContentResolver();
    new Thread(new Runnable() {
      @Override
      public void run() {
        for (GroupRow groupRow : groups) {
          final String id = groupRow.getId();
          if (getActivity() != null) {
            final Set<String> list = new ContactRow().fetchGroupMembersId(contentResolver, id);
            if (list != null) {
              for (String id1 : list) {
                for (ContactRow c : contactManager.getContactsLocal()) {
                  if (c.getId().equals(id1)) {
                    c.setSync(isChecked);
                    break;
                  }
                }
              }
            }
          } else {
            Log.i(TAG, "je nulllllll taky");
          }
        }
      }
    }).start();

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        HelperSQL db = new HelperSQL(getActivity());
        for (GroupRow groupRow : groups) {
          final String id = groupRow.getId();
          db.updateGroupSync(groupRow);
          if (getActivity() != null) {
            final Set<String> list = new ContactRow().fetchGroupMembersId(contentResolver, id);
            if (list != null) {
              db.updateContactsSync(list, isChecked);
            }
          } else {
            Log.i(TAG, "je nulllllll");
          }
        }
      }
    };
    new Thread(runnable).start();
  }

  /**
   * Reload data in manager.
   */
  @Background
  public void initContactManager() {
    contactManager.reloadManager();
    adapter.notifyDataSetChanged();
  }

}
