package cz.xsendl00.synccontact;

import android.app.Activity;
import android.os.Bundle;

public class HelpActivity extends Activity {

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_help);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }
}
