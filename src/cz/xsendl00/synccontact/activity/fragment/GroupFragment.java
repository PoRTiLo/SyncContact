package cz.xsendl00.synccontact.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import cz.xsendl00.synccontact.ContactsDetailActivity_;
import cz.xsendl00.synccontact.HelpActivity_;
import cz.xsendl00.synccontact.R;
import cz.xsendl00.synccontact.SettingsActivity_;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;

/**
 * Fragment of group.
 *
 * @author portilo
 */
@EFragment
public class GroupFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "GroupFragment";
  private ListView listRow;
  private RowGroupAdapter adapter;
  private static boolean selectAll;
  private ContactsActivity activity;
  private ContactManager contactManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    activity = (ContactsActivity) getActivity();
    contactManager = ContactManager.getInstance(activity);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View rootView = null;
    if (activity.isFirst()) {
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
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          if (contactManager.getGroupsLocal().get(position).getSize() != 0) {
            Intent i = new Intent(getActivity().getApplicationContext(),
                ContactsDetailActivity_.class);
            i.putExtra(Constants.INTENT_FIRST, activity.isFirst());
            i.putExtra(Constants.INTENT_ID, contactManager.getGroupsLocal().get(position).getId());
            i.putExtra(Constants.INTENT_NAME, contactManager.getGroupsLocal()
                .get(position)
                .getName());
            startActivity(i);
          } else {
            Toast toast = Toast.makeText(getActivity(), R.string.group_toast, Toast.LENGTH_SHORT);
            toast.show();
          }
        }
      });
    }
  }

  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    MenuItem item = menu.findItem(R.id.action_select);
    String newText = !selectAll
        ? getString(R.string.group_select_all)
        : getString(R.string.group_select_no);
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
        intent = new Intent(getActivity(), HelpActivity_.class);
        if (activity.isFirst()) {
          intent.putExtra(Constants.INTENT_FIRST, true);
        }
        startActivity(intent);
        break;
      case R.id.action_settings:
        intent = new Intent(getActivity(), SettingsActivity_.class);
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
    boolean isSelectAll = true;
    for (GroupRow groupRow : contactManager.getGroupsLocal()) {
      if (!groupRow.isSync()) {
        isSelectAll = false;
        break;
      }
    }
    selectAll = isSelectAll ? true : false;
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
        updateGroups(result, contactManager.getGroupsLocal());
        updateDB(result, contactManager.getGroupsLocal());
      }
    }).start();
  }


  @Override
  public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
    int pos = (Integer) buttonView.getTag();
    if (pos != ListView.INVALID_POSITION) {
      GroupRow p = contactManager.getGroupsLocal().get(pos);
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
        ArrayList<GroupRow> groups = new ArrayList<GroupRow>();
        groups.add(p);
        updateGroups(isChecked, groups);
        updateDB(isChecked, groups);
      }
    }
  }

  /**
   * Update data in {@link ContactManager}.
   *
   * @param isChecked true/false select/no select
   * @param groups list of groups
   */
  @Background
  protected void updateGroups(final boolean isChecked, final List<GroupRow> groups) {
    //final ContentResolver contentResolver = getActivity().getContentResolver();
    for (GroupRow groupRow : groups) {
      final String id = groupRow.getId();
      List<ContactRow> list = contactManager.getGroupsContacts().get(id);
      for (ContactRow contactRow : list) {
        contactRow.setSync(isChecked);
      }
//      if (getActivity() != null) {
//        final Set<String> list = new ContactRow().fetchGroupMembersId(contentResolver, id);
//        if (list != null) {
//          for (String id1 : list) {
//            for (ContactRow c : contactManager.getContactsLocal()) {
//              if (c.getId().equals(id1)) {
//                c.setSync(isChecked);
//                break;
//              }
//            }
//          }
//        }
//      } else {
//        Log.i(TAG, "je nulllllll taky");
//      }
    }
  }

  /**
   * Update database after change selected groups.
   *
   * @param isChecked true/false select/no select
   * @param groups list of groups
   */
  @Background
  protected void updateDB(final boolean isChecked, final List<GroupRow> groups) {
    //final ContentResolver contentResolver = getActivity().getContentResolver();
    HelperSQL db = new HelperSQL(getActivity());
    for (GroupRow groupRow : groups) {
      final String id = groupRow.getId();

      db.updateGroupSync(groupRow);
      List<ContactRow> list = contactManager.getGroupsContacts().get(id);
      db.updateContactsSync(list, isChecked);

//      if (getActivity() != null) {
//        final Set<String> list = new ContactRow().fetchGroupMembersId(contentResolver, id);
//        if (list != null) {
//          db.updateContactsSync(list, isChecked);
//        }
//      } else {
//        Log.i(TAG, "je nulllllll");
//      }
    }
  }

  /**
   * Reload data in manager.
   */
  @Background
  protected void initContactManager() {
    contactManager.initGroupsContacts(); //.reloadManager();
    updateAdapter();
  }

  /**
   * Update adapter in main thread.
   */
  @UiThread
  protected void updateAdapter() {
    adapter.notifyDataSetChanged();
  }

}
