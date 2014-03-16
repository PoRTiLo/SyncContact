package cz.xsendl00.synccontact;

import com.xsendl00.synccontact.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

  
  private Button map;
  private Button add;
  private Button show;
  private Button help;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
      super.onWindowFocusChanged(hasFocus);
      if (hasFocus == true) {
        RelativeLayout f = (RelativeLayout) findViewById(R.id.main_activity);
        conf(f);
      }
  }
  
  private void conf(RelativeLayout f) {
    int x = (int) f.getRight()/2;
    int y = (int) (f.getBottom())/5;
    map = (Button) findViewById(R.id.button_map);
    map.getLayoutParams().height = y*3;
    map.getLayoutParams().width = x;
    map.setLayoutParams(map.getLayoutParams());

    show = (Button) findViewById(R.id.button_show_sight);
    show.getLayoutParams().height = y*2;
    show.getLayoutParams().width = x;    
    show.setLayoutParams(show.getLayoutParams());    

    help = (Button) findViewById(R.id.button_help);
    help.getLayoutParams().height = y*2;
    help.getLayoutParams().width = x;
    help.setLayoutParams(help.getLayoutParams());

    add = (Button) findViewById(R.id.button_add_sight);
    add.getLayoutParams().height = y*3;
    add.getLayoutParams().width = x;
    add.setLayoutParams(add.getLayoutParams());
  }
  
  /**
   * Called when the user clicks on the Server button.
   * 
   * @param view
   */
  public void startServerActivity(View view) {
    Intent intent = new Intent(this, AddServerActivity.class);
    startActivity(intent);
  }
  
  public void startContactActivity(View view) {
	  Intent intent = new Intent(this, ContactsListActivity.class);
	  startActivity(intent);
  }
}
