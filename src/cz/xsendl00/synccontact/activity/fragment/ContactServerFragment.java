package cz.xsendl00.synccontact.activity.fragment;

import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import cz.xsendl00.synccontact.ContactsServerActivity;
import cz.xsendl00.synccontact.HelpActivity_;
import cz.xsendl00.synccontact.R;
import cz.xsendl00.synccontact.SettingsActivity_;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;


/**
 * Fragment for contact.
 *
 * @author xsendl00
 */
@EFragment
public class ContactServerFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "ContactServerFragment";
  private ContactsServerActivity activity;
  private ListView listRow;
  private RowContactServerAdapter adapter;
  private ContactManager contactManager;
  private static boolean selectAll = true;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    activity = (ContactsServerActivity) getActivity();
    contactManager = ContactManager.getInstance(activity);
    //selected();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View rootView = null;
    rootView = inflater.inflate(R.layout.fragment_contact, container, false);
    Button button = (Button) rootView.findViewById(R.id.fragment_contact_button);
    if (activity.isFirst() && contactManager.getServerContact2Import().isEmpty()) {
      button.setText("Skip");
    } else {
      button.setText("Imported selected");
    }
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    listRow = (ListView) getActivity().findViewById(R.id.list_contact);
    adapter = new RowContactServerAdapter(getActivity().getApplicationContext(), contactManager.getServerContact2Import(), this);
    listRow.setAdapter(adapter);

    //changeShowingData();
  }

  /**
   * Check if all is selected.
   */
  @Background
  protected void selected() {
    String where = RawContacts.SYNC1 + "=1";
    List<ContactRow> contactRowDb = ContactRow.fetchAllRawContact(activity.getContentResolver(), where);
    int countSelect = 0;
    for (ContactRow contactRowServer : contactManager.getServerContact2Import()) {
      int i = 0;
      boolean found = false;
      for (ContactRow contactRow2 : contactRowDb) {
        if (contactRowServer.getName() != null && contactRowServer.getName().equals(contactRow2.getName())) {
          contactRowServer.setSync(true);
          contactRowDb.remove(i);
          found = true;
          countSelect++;
          break;
        }
        i++;
      }
      if (!found) {
        contactRowServer.setSync(false);
      }
    }
    selectAll = countSelect == contactManager.getServerContact2Import().size();
    //updateAdapter();
  }

  private void selectAll(final boolean result) {
    for (ContactRow contactRow : contactManager.getServerContact2Import()) {
      contactRow.setSync(result);
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    MenuItem item = menu.findItem(R.id.action_select);
    String newText = !selectAll ? getString(R.string.group_select_all) : getString(R.string.group_select_no);
    item.setTitle(newText);
    super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
      case R.id.action_refresh:
        activity.reinitData();
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
//      case R.id.action_show:
//        item.setTitle(activity.importAll ? getString(R.string.menu_show_imported) : getString(R.string.menu_show_all));
//        activity.importAll  = activity.importAll ? false : true;
//        changeShowingData();
//        break;
      case android.R.id.home:
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
      ContactRow p = null;
      //
        p = contactManager.getServerContact2Import().get(pos);
//      } else {
//        p = contactManager.getContactsServer().get(pos);
//      }
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
      }
    } else {
      Log.w(TAG, "onCheckedChanged pos is > contactManager.getServerContact2Import() :" + pos
          + " : " + contactManager.getServerContact2Import().size());
    }
  }
  /**
   * Update adapter in main thread.
   */
  @UiThread
  public void updateAdapter() {
    //changeShowingData();
    adapter = new RowContactServerAdapter(getActivity().getApplicationContext(), contactManager.getServerContact2Import(), this);
    listRow.setAdapter(adapter);
    adapter.notifyDataSetChanged();
  }

//  private void changeShowingData() {
//    if (activity.importAll) {
//      adapter = new RowContactServerAdapter(getActivity().getApplicationContext(),
//          contactManager.getServerContact2Import(), this);
//      listRow.setAdapter(adapter);
//    } else {
//      adapter = new RowContactServerAdapter(getActivity().getApplicationContext(),
//          contactManager.getContactsServer(), this);
//      listRow.setAdapter(adapter);
//    }
//  }
}
