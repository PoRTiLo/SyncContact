package cz.xsendl00.synccontact;

import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Welcome activity.
 *
 * @author portilo
 */
@EActivity(R.layout.activity_info_welcome)
public class InfoWelcomeActivity extends Activity {

  private Toast toast;
  private long lastBackPressTime = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  /**
   * Open activity for configuration connection to server LDAP.
   *
   * @param view unused
   */
  public void addServerActivity(@SuppressWarnings("unused") View view) {
    Intent intent = new Intent(this, ServerAddActivity_.class);
    intent.putExtra(Constants.INTENT_FIRST, true);
    startActivity(intent);
  }

  @Override
  public void onBackPressed() {
    if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
      toast = Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_SHORT);
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
      default:
        break;
    }

    return true;
  }
}
