package cz.xsendl00.synccontact;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.utils.Constants;
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

/**
 * 
 * @author portilo
 *
 */
public class MainActivity extends Activity {

  private Button map;
  private Button add;
  private Button show;
  private Button help;

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loadPreferences();
  }

  @Override
  protected void onResume() {
    super.onResume();
    conf();
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus == true) {
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
      Intent intent = new Intent(this, WelcomeActivity.class);
      startActivity(intent);
      this.finish();
    }
  }

  private void conf() {
    RelativeLayout f = (RelativeLayout) findViewById(R.id.main_activity);
    int x = (int) f.getRight() / 2;
    int y = (int) (f.getBottom()) / 5;
    map = (Button) findViewById(R.id.button_map);
    map.getLayoutParams().height = y * 3;
    map.getLayoutParams().width = x;
    map.setLayoutParams(map.getLayoutParams());

    show = (Button) findViewById(R.id.button_show_sight);
    show.getLayoutParams().height = y * 2;
    show.getLayoutParams().width = x;
    show.setLayoutParams(show.getLayoutParams());

    help = (Button) findViewById(R.id.button_help);
    help.getLayoutParams().height = y * 2;
    help.getLayoutParams().width = x;
    help.setLayoutParams(help.getLayoutParams());

    add = (Button) findViewById(R.id.button_add_sight);
    add.getLayoutParams().height = y * 3;
    add.getLayoutParams().width = x;
    add.setLayoutParams(add.getLayoutParams());
  }

  /**
   * Called when the user clicks on the Server button.
   * 
   * @param view
   */
  public void startServerActivity(View view) {
    SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME,
        MODE_PRIVATE);
    boolean startFirst = settings.getBoolean(Constants.PREFS_START_FIRST, true);
    if (startFirst) {
      Intent intent = new Intent(this, WelcomeActivity.class);
      startActivity(intent);
    } else {
      Intent intent = new Intent(this, ServerActivity.class);
      startActivity(intent);
    }
  }

  /**
   * Open contact activity. Call from main activity. 
   * @param view
   */
  public void startContactActivity(View view) {
    Intent intent = new Intent(this, SelectContactListActivity.class);
    startActivity(intent);
  }

  /**
   * Open help activity. Call from main activity.
   * @param view
   */
  public void startHelpActivity(View view) {
    //Intent intent = new Intent(this, HelpActivity.class);
    Intent intent = new Intent(this, LDAPContactActivity.class);
    startActivity(intent);
  }

  /**
   * Open sync activity. Call from main activity.
   * @param view
   */
  public void startSyncActivity(View view) {
    Intent intent = new Intent(this, SynchronizationActivity.class);
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
      intent = new Intent(this, HelpActivity.class);
      startActivity(intent);
      break;
    case R.id.action_settings:
      intent = new Intent(this, SettingsActivity.class);
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
