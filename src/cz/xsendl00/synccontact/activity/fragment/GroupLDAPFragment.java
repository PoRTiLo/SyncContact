package cz.xsendl00.synccontact.activity.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ListView;
import cz.xsendl00.synccontact.ContactsActivity;
import cz.xsendl00.synccontact.HelpActivity;
import cz.xsendl00.synccontact.LDAPContactActivity;
import cz.xsendl00.synccontact.R;
import cz.xsendl00.synccontact.SettingsActivity;

public class GroupLDAPFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "GroupFrgment";
  private ListView listRow;
  private RowGroupAdapter adapter;
  private boolean first = false;
  private static boolean selectAll;
  private LDAPContactActivity activity;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    activity = (LDAPContactActivity) getActivity();
    //selectAll = isSelectedAll();
  }

  @Override
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
        //String newText = selectAll ? "Select all" : "No select";
        Log.i(TAG, "jsem tuuuuuuuuuuuuuuuuuuuuu");
        selectAll = selectAll ? false : true;
        //selectAll(selectAll);
        //item.setTitle(newText);
        break;
      case android.R.id.home:
        break;
      default:
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    // TODO Auto-generated method stub

  }

}
