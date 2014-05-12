package cz.xsendl00.synccontact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Welcome activity.
 * @author portilo
 *
 */
public class WelcomeActivity extends Activity {

  private Toast toast;
  private long lastBackPressTime = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  /**
   * Open activity for configuration connection to server LDAP.
   * @param view
   */
  public void addServerActivity(View view) {
    Intent intent = new Intent(this, AddServerActivity.class);
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
