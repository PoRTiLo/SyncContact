package cz.xsendl00.synccontact;

import java.util.ArrayList;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
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

public class ContactFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private Pair pair;

  private ListView listRow;
  private RowContactAdapter adapter;
  private boolean first = false;
  private static boolean selectAll;

  // newInstance constructor for creating fragment with arguments
  public static ContactFragment newInstance(Pair p, boolean first) {
    Log.i("ContactFragment", "newInstance");
    ContactFragment contactFragment = new ContactFragment();
    Bundle args = new Bundle();
    args.putParcelable("pair", p);
    args.putBoolean("FIRST", first);
    contactFragment.setArguments(args);
    return contactFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    pair = getArguments().getParcelable("pair");
    first = getArguments().getBoolean("FIRST");
    setHasOptionsMenu(true);
    selectAll = isSelectedAll();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    adapter = new RowContactAdapter(getActivity().getApplicationContext(), this.pair.getContactList(), this);
    listRow.setAdapter(adapter);

    if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
          // TODO: show info -> edit contact
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
        /*
         * Intent upIntent = NavUtils.getParentActivityIntent(this); if
         * (NavUtils.shouldUpRecreateTask(this, upIntent)) { // This activity is
         * NOT part of this app's task, so create a new task // when navigating
         * up, with a synthesized back stack. TaskStackBuilder.create(this) // Add
         * all of this activity's parents to the back stack
         * .addNextIntentWithParentStack(upIntent) // Navigate up to the closest
         * parent .startActivities(); } else { // This activity is part of this
         * app's task, so simply // navigate up to the logical parent activity.
         * NavUtils.navigateUpTo(this, upIntent); }
         */
        break;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private boolean isSelectedAll() {
    boolean selectAll = true;
    for (ContactRow contactRow : pair.getContactList()) {
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
        updateDB(pair.getContactList(), result);
      }
    }).start();
    for (ContactRow contactRow : pair.getContactList()) {
      contactRow.setSync(result);
    }
    adapter.notifyDataSetChanged();
    for (GroupRow groupRow : pair.getGroupsList()) {
      groupRow.setSync(result);
    }
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
      ContactRow p = pair.getContactList().get(pos);
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
        Log.i("COntactfragment", "change");
        ArrayList<ContactRow> contacts = new ArrayList<ContactRow>();
        contacts.add(p);
        updateDB(contacts, isChecked);
      }
    }
  }

}
