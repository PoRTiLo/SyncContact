package cz.xsendl00.synccontact;

import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Activity before merge activity. Show base info.
 *
 * @author portilo
 */
@EActivity(R.layout.activity_info_merge)
public class InfoMergeActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  /**
   * @param view unused
   */
  public void go2MergeActivity(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, ContactsMergeActivity_.class);
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
        break;
      default:
        break;
    }
    return true;
  }
}
