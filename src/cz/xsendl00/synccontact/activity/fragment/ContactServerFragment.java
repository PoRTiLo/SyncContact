package cz.xsendl00.synccontact.activity.fragment;

import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactRow;


/**
 * Fragment for contact.
 *
 * @author portilo
 */
@EFragment
public class ContactServerFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "ContactServerFragment";
  private ContactsServerActivity activity;
  private ListView listRow;
  private RowContactServerAdapter adapter;
  private ContactManager contactManager;
  private static boolean selectAll;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    activity = (ContactsServerActivity) getActivity();
    contactManager = ContactManager.getInstance(activity);
    selected();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = null;
    rootView = inflater.inflate(R.layout.fragment_contact, container, false);
    Button button = (Button) rootView.findViewById(R.id.fragment_contact_button);
    if (activity.isFirst() && contactManager.getContactsServer().isEmpty()) {
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
    adapter = new RowContactServerAdapter(getActivity().getApplicationContext(),
        contactManager.getContactsServer(), this);
    listRow.setAdapter(adapter);
  }

  /**
   * Check if all is selected.
   */
  @Background
  protected void selected() {
    HelperSQL db = new HelperSQL(activity);
    List<ContactRow> contactRowDb = db.getContactsSync();
    int countSelect = 0;
    for (ContactRow contactRowServer : contactManager.getContactsServer()) {
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
    selectAll = countSelect == contactManager.getContactsServer().size();
  }

  private void selectAll(final boolean result) {
    for (ContactRow contactRow : contactManager.getContactsServer()) {
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
      case android.R.id.home:
        getActivity().finish();
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
      ContactRow p = contactManager.getContactsServer().get(pos);
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
      }
    }
  }
}
