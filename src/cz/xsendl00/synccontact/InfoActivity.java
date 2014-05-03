package cz.xsendl00.synccontact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InfoActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_info);
  }

  public void selectActivity(View view) {
    Intent intent = new Intent(this, SelectContactListActivity.class);
    startActivity(intent);
  }
}
