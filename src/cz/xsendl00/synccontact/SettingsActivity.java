package cz.xsendl00.synccontact;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;
import cz.xsendl00.synccontact.client.ContactManager;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Settings activity.
 *
 * @author xsendl00
 */
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends Activity {

  private ContactManager contactManager;

  @ViewById(R.id.setting_check)
  protected CheckBox box;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    contactManager = ContactManager.getInstance(SettingsActivity.this);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  @AfterViews
  public void onResume() {
    super.onResume();
    if (!contactManager.isOnlyWifiInit()) {
      SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
      contactManager.setOnlyWifi(settings.getBoolean(Constants.PREFS_WIFI, false));
      contactManager.setOnlyWifiInit(true);
    }
    box.setChecked(contactManager.isOnlyWifi());
    box.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // is chkIos checked?
        if (((CheckBox) v).isChecked()) {
          Toast.makeText(SettingsActivity.this, "Set sync only by wifi", Toast.LENGTH_SHORT).show();
          contactManager.setOnlyWifi(true);
        } else {
          //Toast.makeText(SettingsActivity.this, "Bro, try Android :(", Toast.LENGTH_SHORT).show();
          contactManager.setOnlyWifi(false);
        }
        Editor editor = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(Constants.PREFS_WIFI, contactManager.isOnlyWifi());
        editor.commit();

      }
    });

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
          TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
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
