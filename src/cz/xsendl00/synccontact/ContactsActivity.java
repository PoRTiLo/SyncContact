package cz.xsendl00.synccontact;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import cz.xsendl00.synccontact.activity.fragment.ContactFragment_;
import cz.xsendl00.synccontact.activity.fragment.GroupFragment_;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Contact activity.
 *
 * @author portilo
 */
@EActivity(R.layout.activity_contacts)
public class ContactsActivity extends Activity {

  /**
   * ProgressBar show by loading data.
   */
  @ViewById(R.id.activit_contacts_layout)
  protected LinearLayout linearLayoutProgress;
  private ContactManager contactManager;
  private boolean first = false;
  private Menu mMenu;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    Intent intent = getIntent();
    first = intent.getBooleanExtra(Constants.INTENT_FIRST, false);

    contactManager = ContactManager.getInstance(getApplicationContext());
    if (!contactManager.isContactsLocalInit() || !contactManager.isGroupsLocalInit()) {
      loadData();
    } else {
      init();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.mMenu = menu;
    if (first) {
      getMenuInflater().inflate(R.menu.contacts_menu_simply, menu);
    } else {
      getMenuInflater().inflate(R.menu.contacts_menu, menu);
    }
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Show ProgressBar instead of button for refresh (R.id.action_add_group).
   *
   * @param refreshing true/false - show/hide
   */
  @UiThread
  protected void setRefreshActionButtonState(final boolean refreshing) {
    if (mMenu != null) {
      final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);
      if (refreshItem != null) {
        if (refreshing) {
          refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
        } else {
          refreshItem.setActionView(null);
        }
      }
    }
  }

  /**
   * Initialize tabs. Call it after data in {@link ContactManager} are loaded.
   */
  @UiThread
  protected void init() {
    ActionBar actionBar = getActionBar();
    actionBar.removeAllTabs();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    actionBar.addTab(actionBar.newTab()
        .setText(getString(R.string.def_group))
        .setIcon(R.drawable.ic_action_group)
        .setTabListener(new MyTabListener<GroupFragment_>(this, "GROUP", GroupFragment_.class)));
    actionBar.addTab(actionBar.newTab()
        .setText(getString(R.string.def_contact))
        .setIcon(R.drawable.ic_action_person)
        .setTabListener(
            new MyTabListener<ContactFragment_>(this, "CONTACT", ContactFragment_.class)));
    linearLayoutProgress.setVisibility(View.GONE);
  }

  /**
   * Do after data from database is loaded.
   */
  @Background
  protected void loadData() {
    contactManager.initGroupsContacts();
    init();
  }

  /**
   * Update data, call by icon (refresh) in menu.
   */
  @Background
  public void reinitData() {
    setRefreshActionButtonState(true);
    contactManager.initGroupsContacts();
    init();
    setRefreshActionButtonState(false);
  }

  /**
   * Call after clock on button. Got to next activity {@link InfoMergeActivity}.
   *
   * @param view unused
   */
  public void go2InfoMerge(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, InfoMergeActivity_.class);
    startActivity(intent);
  }

  /**
   * First run this application.
   *
   * @return true first run, false not first run.
   */
  public boolean isFirst() {
    return first;
  }
}
