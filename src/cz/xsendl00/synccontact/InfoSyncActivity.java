package cz.xsendl00.synccontact;

import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Info about sync activity.
 * @author xsendl00
 *
 */
@EActivity(R.layout.activity_info)
public class InfoSyncActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  /**
   * Go to ContactActivity.
   * @param view unused.
   */
  public void selectActivity(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, ContactsActivity_.class);
    intent.putExtra(Constants.INTENT_FIRST, true);
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.settings_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
    case R.id.action_help:
      intent = new Intent(this, HelpActivity_.class);
      intent.putExtra(Constants.INTENT_FIRST, true);
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

    return true;
  }
}
