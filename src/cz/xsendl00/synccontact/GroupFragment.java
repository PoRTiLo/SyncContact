package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.Set;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GroupFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "GroupFrgment";
  private ListView listRow;
  private RowGroupAdapter adapter;
  private boolean first = false;
  private static boolean selectAll;
  private SelectContactListActivity activity;

  //OnHeadlineSelectedListener mCallback;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    activity = (SelectContactListActivity) getActivity();
    first = activity.isFirst();
    selectAll = isSelectedAll();
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
        Log.i(TAG, "jsem tuuuuuuuuuuuuuuuuuuuuu");
        selectAll = selectAll ? false : true;
        selectAll(selectAll);
        //item.setTitle(newText);
        break;
      case android.R.id.home:
        // TODO
        Log.i(TAG, "jsem tuuuuuuuuuuuuuuuuuuuuu");
        break;
      default:
        break;
    }
    
    return super.onOptionsItemSelected(item);
  }
  
  private boolean isSelectedAll() {
    boolean selectAll = true;
    for (GroupRow groupRow : activity.getPair().getGroupsList()) {
      if (!groupRow.isSync()) {
        selectAll =false;
        break;
      }
    }
    return selectAll;
  }
  
  private void selectAll(final boolean result) {
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        for (GroupRow groupRow : activity.getPair().getGroupsList()) {
          groupRow.setSync(result);
        }
        adapter.notifyDataSetChanged();
      }
    });
    
    new Thread(new Runnable() {
      public void run() {
        updateDB(result, activity.getPair().getGroupsList());
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

  public void onResume() {
    super.onResume();
    selectAll = isSelectedAll();
    listRow = (ListView) getActivity().findViewById(R.id.list_group);
    adapter = new RowGroupAdapter(getActivity().getApplicationContext(),
        activity.getPair().getGroupsList(), this);
    listRow.setAdapter(adapter);
    if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
          if (activity.getPair().getGroupsList().get(position).getSize() != 0) {
            Intent i = new Intent(getActivity().getApplicationContext(),
                ContactsActivity.class);
            i.putExtra("ID", activity.getPair().getGroupsList().get(position).getId());
            i.putExtra("NAME", activity.getPair().getGroupsList().get(position).getName());
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
      GroupRow p = activity.getPair().getGroupsList().get(pos);
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
        ArrayList<GroupRow> groups = new ArrayList<GroupRow>();
        groups.add(p);
        updateDB(isChecked, groups);
      }
    }
  }
  
  private void updateDB(final boolean isChecked, final ArrayList<GroupRow> groups) {
    final ContentResolver contentResolver =  getActivity().getContentResolver();
    new Thread(new Runnable() {
      public void run() {
        for (GroupRow groupRow : groups) {
          final String id = groupRow.getId();
          if (getActivity() != null) {
            final Set<String> list = new ContactRow().fetchGroupMembersId(contentResolver, id);
            if (list != null) {
              for (String id1 : list) {
                for (ContactRow c : activity.getPair().getContactList()) {
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
          }
          else {
            Log.i(TAG, "je nulllllll");
          }
        }
      }
    };
    new Thread(runnable).start();
  }
}
