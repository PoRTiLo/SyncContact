package cz.xsendl00.synccontact.activity.first;

import cz.xsendl00.synccontact.ContactsActivity;
import cz.xsendl00.synccontact.HelpActivity;
import cz.xsendl00.synccontact.R;
import cz.xsendl00.synccontact.SettingsActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Info about sync activity.
 * @author portilo
 *
 */
public class InfoSyncActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_info);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  /**
   * Go to ContactActivity.
   * @param view
   */
  public void selectActivity(View view) {
    Intent intent = new Intent(this, ContactsActivity.class);
    intent.putExtra("FIRST", true);
    startActivity(intent);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.sync_menu, menu);
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
