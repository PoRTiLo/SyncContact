package cz.xsendl00.synccontact;

import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Settings activity.
 * @author portilo
 *
 */
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.settings_menu, menu);
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
