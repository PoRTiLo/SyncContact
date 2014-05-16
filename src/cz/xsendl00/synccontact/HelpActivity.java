package cz.xsendl00.synccontact;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Activity which show info.
 *
 * @author portilo
 *
 */
@EActivity(R.layout.activity_help)
public class HelpActivity extends Activity {

  private static final String TAG = "HelpActivity";
  private boolean first;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    first = getIntent().getBooleanExtra(Constants.INTENT_FIRST, false);
    Log.i(TAG, "onCreate FIRST" + first);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    if (!first) {
      inflater.inflate(R.menu.help_menu, menu);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = null;
    switch (item.getItemId()) {
    case R.id.action_settings:
      intent = new Intent(this, SettingsActivity_.class);
      startActivity(intent);
      break;
    case android.R.id.home:
      if (first) {
        Log.i(TAG, "id home FIRST" + first);
        finish();
        return true;
      } else {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
          TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent)
              .startActivities();
        } else {
          NavUtils.navigateUpTo(this, upIntent);
        }
        return true;
      }
    default:
      break;
    }
    return super.onOptionsItemSelected(item);
  }
}
