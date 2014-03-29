package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;

import com.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactShow;
import cz.xsendl00.synccontact.utils.Group;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

  
  private Button map;
  private Button add;
  private Button show;
  private Button help;
  private Boolean setsyntContact;
  
  private LinkedHashMap<Group, ArrayList<ContactShow>> groupList;

  private static final String TAG = "MainActivity";
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loadPreferences();
    //pokus();
    initContactList();
    //Log.i(TAG, groupList.toString());
  }
  
  private ArrayList<ContactShow> fetchGroupMembers(String groupId){
    ArrayList<ContactShow> groupMembers = new ArrayList<ContactShow>();
    String where = CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" +groupId + " AND " + 
        CommonDataKinds.GroupMembership.MIMETYPE + "='" + CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'";
    String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID, Data.DISPLAY_NAME};
    Cursor cursor = getContentResolver().query(Data.CONTENT_URI, projection, where,null, Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    while (cursor.moveToNext()) {
      ContactShow contactShow = new ContactShow();
      contactShow.setName(cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME)));
      contactShow.setId(cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID)));
      /*Cursor phoneFetchCursor = getContentResolver().query(Phone.CONTENT_URI,new String[]{Phone.NUMBER,Phone.DISPLAY_NAME,Phone.TYPE}, 
          Phone.CONTACT_ID+"=" + contactShow.getId(), null, null);
      int i = 0;
      while (phoneFetchCursor.moveToNext()) {
        Log.i(TAG, "tady:"+i++ + phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.DISPLAY_NAME)) + ":" +cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME)));
        contactShow.setPhoneNumber(phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.NUMBER)));
        contactShow.setPhoneDisplayName(phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.DISPLAY_NAME)));
        contactShow.setPhoneType(phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.TYPE)));
      }
      phoneFetchCursor.close();*/
      groupMembers.add(contactShow);
    } 
    cursor.close();
    return groupMembers;
  }
  
  private void initContactList(){
    groupList = new LinkedHashMap<Group,ArrayList<ContactShow>>();
    ArrayList<Group> groupsList = Group.fetchGroups(getContentResolver());
      for (Group group : groupsList) {;
        ArrayList<ContactShow> groupMembers =new ArrayList<ContactShow>();
        groupMembers.addAll(fetchGroupMembers(group.getId()));
        group.setSize(groupMembers.size());
        groupList.put(group, groupMembers);
      }
}
  
  
  
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
      super.onWindowFocusChanged(hasFocus);
      if (hasFocus == true) {
        RelativeLayout f = (RelativeLayout) findViewById(R.id.main_activity);
        conf(f);
      }
  }
  
  private void loadPreferences() {
    Log.i(TAG, "Load preferens");
    SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
    setsyntContact = settings.getBoolean(Constants.SET_SYNC_CONTACT, false);
    Log.i(TAG, "Load preferens: Set sync contact = " + setsyntContact);
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
