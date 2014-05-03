package cz.xsendl00.synccontact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class WelcomeActivity extends Activity {

  private Toast toast;
  private long lastBackPressTime = 0;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);
  }
  
  public void addServerActivity(View view) {
    Intent intent = new Intent(this, AddServerActivity.class);
    startActivity(intent);
  }
  
  @Override
  public void onBackPressed() {
    if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
      toast = Toast.makeText(this, "Press back again to close this app", 4000);
      toast.show();
      this.lastBackPressTime = System.currentTimeMillis();
    } else {
      if (toast != null) {
      toast.cancel();
    }
    super.onBackPressed();
    
   }
  }
}
