package cz.xsendl00.synccontact;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import cz.xsendl00.synccontact.utils.Constants;

/**
 *
 * @author portilo
 *
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

  @ViewById(R.id.button_map)
  protected Button map;
  @ViewById(R.id.button_add_sight)
  protected Button add;
  @ViewById(R.id.button_show_sight)
  protected Button show;
  @ViewById(R.id.button_help)
  protected Button importButton;

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loadPreferences();
  }
/*
  @Override
  protected void onResume() {
    super.onResume();
    conf();
  }
*/
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      conf();
    }
  }

  private void loadPreferences() {
    Log.i(TAG, "Load preferens");
    SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME,
        MODE_PRIVATE);
    boolean startFirst = settings.getBoolean(Constants.PREFS_START_FIRST, true);
    Log.i(TAG, "Load preferens: startFirst = " + startFirst);
    if (startFirst) {
      // load first route
      Intent intent = new Intent(this, InfoWelcomeActivity_.class);
      startActivity(intent);
      this.finish();
    }
  }

  /**
   * Deign main window. Count size of elemnts.
   */
  @AfterViews
  @UiThread
  public void conf() {
    RelativeLayout f = (RelativeLayout) findViewById(R.id.main_activity);
    int x = f.getRight() / 2;
    int y = (f.getBottom()) / 5;
    map.getLayoutParams().height = y * 3;
    map.getLayoutParams().width = x;
    map.setLayoutParams(map.getLayoutParams());

    show.getLayoutParams().height = y * 2;
    show.getLayoutParams().width = x;
    show.setLayoutParams(show.getLayoutParams());

    importButton.getLayoutParams().height = y * 2;
    importButton.getLayoutParams().width = x;
    importButton.setLayoutParams(importButton.getLayoutParams());

    add.getLayoutParams().height = y * 3;
    add.getLayoutParams().width = x;
    add.setLayoutParams(add.getLayoutParams());
  }

  /**
   * Called when the user clicks on the Server button.
   *
   * @param view unused
   */
  public void startServerActivity(@SuppressWarnings("unused") View view) {
    SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME,
        MODE_PRIVATE);
    boolean startFirst = settings.getBoolean(Constants.PREFS_START_FIRST, true);
    if (startFirst) {
      Intent intent = new Intent(this, InfoWelcomeActivity_.class);
      startActivity(intent);
    } else {
      Intent intent = new Intent(this, ServerActivity_.class);
      startActivity(intent);
    }
  }

  /**
   * Open contact activity. Call from main activity.
   * @param view unused
   */
  public void startContactActivity(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, ContactsActivity_.class);
    startActivity(intent);
  }

  /**
   * Open server contact activity. Call from main activity.
   * @param view unused
   */
  public void startLDAPContactsActivity(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, ContactsServerActivity_.class);
    startActivity(intent);
  }

  /**
   * Open sync activity. Call from main activity.
   * @param view unused
   */
  public void startSyncActivity(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, SynchronizationActivity_.class);
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.sync_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
    case R.id.action_help:
      intent = new Intent(this, HelpActivity_.class);
      startActivity(intent);
      break;
    case R.id.action_settings:
      intent = new Intent(this, SettingsActivity_.class);
      startActivity(intent);
      break;
    case android.R.id.home:
      finish();
      break;
    default:
      break;
    }
    return true;
  }
}
