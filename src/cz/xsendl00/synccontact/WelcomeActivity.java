package cz.xsendl00.synccontact;


import com.googlecode.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Welcome activity.
 * @author portilo
 *
 */
@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends Activity {

  private Toast toast;
  private long lastBackPressTime = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  /**
   * Open activity for configuration connection to server LDAP.
   * @param view unused
   */
  public void addServerActivity(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, AddServerActivity_.class);
    startActivity(intent);
  }

  @Override
  public void onBackPressed() {
    if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
      toast = Toast.makeText(this, "Press back again to close this app",
          Toast.LENGTH_SHORT);
      toast.show();
      this.lastBackPressTime = System.currentTimeMillis();
    } else {
      if (toast != null) {
        toast.cancel();
      }
      super.onBackPressed();
    }
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
