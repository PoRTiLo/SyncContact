package cz.xsendl00.synccontact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Activity which show info.
 * 
 * @author portilo
 * 
 */
public class HelpActivity extends Activity {

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_help);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.help_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
    case R.id.action_settings:
      intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
      break;
    case android.R.id.home:
      Intent upIntent = NavUtils.getParentActivityIntent(this);
      if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
        TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent)
            .startActivities();
      } else {
        NavUtils.navigateUpTo(this, upIntent);
      }
      return true;
    default:
      break;
    }

    return super.onOptionsItemSelected(item);
  }
}
