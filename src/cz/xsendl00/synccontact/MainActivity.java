package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldif.LDIFException;
import com.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.utils.Constants;
import cz.xsendl00.synccontact.utils.ContactDetail;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Identity;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.CommonDataKinds.SipAddress;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
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
  
  private LinkedHashMap<GroupRow, ArrayList<ContactRow>> groupList;

  private static final String TAG = "MainActivity";
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loadPreferences();
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
  
  public void startHelpActivity(View view) {
    Intent intent = new Intent(this, SelectContactListActivity.class);
    startActivity(intent);
  }
  
  // TODO: vnd.com.google.cursor.item/contact_misc add by google?
  private ArrayList<Attribute> fill(Cursor cursor) {
    
    ArrayList<Attribute> attributes = new ArrayList<Attribute>();
    String str = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE));
    
    if (str.equals(StructuredName.CONTENT_ITEM_TYPE)) {
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
        attributes.add(new Attribute(Constants.DISPLAY_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA2))) {
        attributes.add(new Attribute(Constants.GIVEN_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA2))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA3))) {
        attributes.add(new Attribute(Constants.FAMILY_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA3))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
        attributes.add(new Attribute(Constants.NAME_PREFIX, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
        attributes.add(new Attribute(Constants.MIDDLE_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
        attributes.add(new Attribute(Constants.NAME_SUFFIX, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
        attributes.add(new Attribute(Constants.PHONETIC_GIVEN_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
        attributes.add(new Attribute(Constants.PHONETIC_MIDDLE_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
      }
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
        attributes.add(new Attribute(Constants.PHONETIC_FAMILY_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
      }
    } else if (str.equals(Phone.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Phone.TYPE_CUSTOM) {
        //TYPE_CUSTOM. Put the actual type in LABEL.
        //String  LABEL DATA3
      } else if (type == Phone.TYPE_ASSISTANT) {
        attributes.add(new Attribute(Constants.PHONE_ASSISTANT, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_CALLBACK) {
        attributes.add(new Attribute(Constants.PHONE_CALLBACK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_CAR) {
        attributes.add(new Attribute(Constants.PHONE_CAR, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_COMPANY_MAIN) {
        attributes.add(new Attribute(Constants.PHONE_COMPANY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_FAX_HOME) {
        attributes.add(new Attribute(Constants.PHONE_FAX_HOME, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_FAX_WORK) {
        attributes.add(new Attribute(Constants.PHONE_FAX_WORK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_HOME) {
        attributes.add(new Attribute(Constants.PHONE_HOME, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_ISDN) {
        attributes.add(new Attribute(Constants.PHONE_ISDN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_MAIN) {
        attributes.add(new Attribute(Constants.PHONE_MAIN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_MMS) {
        attributes.add(new Attribute(Constants.PHONE_MMS, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_MOBILE) {
        attributes.add(new Attribute(Constants.PHONE_MOBILE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_OTHER) {
        attributes.add(new Attribute(Constants.PHONE_OTHER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_OTHER_FAX) {
        attributes.add(new Attribute(Constants.PHONE_OTHER_FAX, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_PAGER) {
        attributes.add(new Attribute(Constants.PHONE_PAGER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_RADIO) {
        attributes.add(new Attribute(Constants.PHONE_RADIO, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_TELEX) {
        attributes.add(new Attribute(Constants.PHONE_TELEX, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_TTY_TDD) {
        attributes.add(new Attribute(Constants.PHONE_TTY_TDD, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_WORK) {
        attributes.add(new Attribute(Constants.PHONE_WORK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_WORK_MOBILE) {
        attributes.add(new Attribute(Constants.PHONE_WORK_MOBILE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Phone.TYPE_WORK_PAGER) {
        attributes.add(new Attribute(Constants.PHONE_WORK_PAGER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else {
        Log.i("NOT SUPPORTED TYPE PHONE", "NOT SUPPORTED TYPE PHONE");
      }
    } else if (str.equals(Email.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Email.TYPE_HOME) {
        attributes.add(new Attribute(Constants.HOME_MAIL, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Email.TYPE_WORK) {
        attributes.add(new Attribute(Constants.WORK_MAIL, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Email.TYPE_OTHER) {
        attributes.add(new Attribute(Constants.OTHER_MAIL, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Email.TYPE_MOBILE) {
        attributes.add(new Attribute(Constants.MOBILE_MAIL, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Email.TYPE_CUSTOM) {
        // TODO:
      } else {
        Log.i("NOT SUPPORTED TYPE EMAIL", "NOT SUPPORTED TYPE EMAIL");
      }
    } else if (str.equals(Photo.CONTENT_ITEM_TYPE)) {
      
    } else if (str.equals(Organization.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Organization.TYPE_WORK) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_COMPANY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_TITLE, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_DEPARTMENT, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_SYMBOL, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_PHONETIC_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_OFFICE_LOCATION, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE, cursor.getString(cursor.getColumnIndex(Data.DATA10))));
        }
      } else if (type == Organization.TYPE_OTHER) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_COMPANY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_TITLE, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_DEPARTMENT, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_SYMBOL, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_PHONETIC_NAME, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
          attributes.add(new Attribute(Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE, cursor.getString(cursor.getColumnIndex(Data.DATA10))));
        }
      } else if (type == Organization.TYPE_CUSTOM) {
        // TODO:      String  LABEL DATA3 
      } else {
        
      }
    } else if (str.equals(Im.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      String protocol = cursor.getString(cursor.getColumnIndex(Data.DATA5));
      if (type == Im.TYPE_CUSTOM) {
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String  LABEL DATA3
      } else if (type == Im.TYPE_HOME) {
        if (protocol.equals(Im.PROTOCOL_CUSTOM)) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String  CUSTOM_PROTOCOL DATA6
       // TODO:
        } else if (protocol.equals(Im.PROTOCOL_AIM)) {
          attributes.add(new Attribute(Constants.IM_HOME_AIM, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_GOOGLE_TALK)) {
          attributes.add(new Attribute(Constants.IM_HOME_GOOGLE_TALK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_ICQ)) {
          attributes.add(new Attribute(Constants.IM_HOME_ICQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_JABBER)) {
          attributes.add(new Attribute(Constants.IM_HOME_JABBER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_MSN)) {
          attributes.add(new Attribute(Constants.IM_HOME_MSN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_NETMEETING)) {
          attributes.add(new Attribute(Constants.IM_HOME_NETMEETING, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_QQ)) {
          attributes.add(new Attribute(Constants.IM_HOME_QQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_SKYPE)) {
          attributes.add(new Attribute(Constants.IM_HOME_SKYPE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_YAHOO)) {
          attributes.add(new Attribute(Constants.IM_HOME_YAHOO, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else if (type == Im.TYPE_WORK && protocol.equals(Im.PROTOCOL_AIM)) {
        if (protocol.equals(Im.PROTOCOL_CUSTOM)) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String  CUSTOM_PROTOCOL DATA6
       // TODO:
        } else if (protocol.equals(Im.PROTOCOL_AIM)) {
          attributes.add(new Attribute(Constants.IM_WORK_AIM, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_GOOGLE_TALK)) {
          attributes.add(new Attribute(Constants.IM_WORK_GOOGLE_TALK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_ICQ)) {
          attributes.add(new Attribute(Constants.IM_WORK_ICQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_JABBER)) {
          attributes.add(new Attribute(Constants.IM_WORK_JABBER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_MSN)) {
          attributes.add(new Attribute(Constants.IM_WORK_MSN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_NETMEETING)) {
          attributes.add(new Attribute(Constants.IM_WORK_NETMEETING, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_QQ)) {
          attributes.add(new Attribute(Constants.IM_WORK_QQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_SKYPE)) {
          attributes.add(new Attribute(Constants.IM_WORK_SKYPE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_YAHOO)) {
          attributes.add(new Attribute(Constants.IM_WORK_YAHOO, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else if (type == Im.TYPE_OTHER && protocol.equals(Im.PROTOCOL_AIM)) {
        if (protocol.equals(Im.PROTOCOL_CUSTOM)) {
          // PROTOCOL_CUSTOM. Also provide the actual protocol name as CUSTOM_PROTOCOL.
          // String  CUSTOM_PROTOCOL DATA6
       // TODO:
        } else if (protocol.equals(Im.PROTOCOL_AIM)) {
          attributes.add(new Attribute(Constants.IM_OTHER_AIM, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_GOOGLE_TALK)) {
          attributes.add(new Attribute(Constants.IM_OTHER_GOOGLE_TALK, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_ICQ)) {
          attributes.add(new Attribute(Constants.IM_OTHER_ICQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_JABBER)) {
          attributes.add(new Attribute(Constants.IM_OTHER_JABBER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_MSN)) {
          attributes.add(new Attribute(Constants.IM_OTHER_MSN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_NETMEETING)) {
          attributes.add(new Attribute(Constants.IM_OTHER_NETMEETING, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_QQ)) {
          attributes.add(new Attribute(Constants.IM_OTHER_QQ, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_SKYPE)) {
          attributes.add(new Attribute(Constants.IM_OTHER_SKYPE, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else if (protocol.equals(Im.PROTOCOL_YAHOO)) {
          attributes.add(new Attribute(Constants.IM_OTHER_YAHOO, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        } else {
          Log.i("NOT SUPPORTED TYPE IM", "NOT SUPPORTED TYPE IM");
        }
      } else {}
    } else if (str.equals(Nickname.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Nickname.TYPE_DEFAULT) {
        attributes.add(new Attribute(Constants.NICKNAME_DEFAULT, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Nickname.TYPE_OTHER_NAME) {
        attributes.add(new Attribute(Constants.NICKNAME_OTHER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Nickname.TYPE_MAIDEN_NAME) {
        attributes.add(new Attribute(Constants.NICKNAME_MAIDEN, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Nickname.TYPE_SHORT_NAME) {
        attributes.add(new Attribute(Constants.NICKNAME_SHORT, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Nickname.TYPE_INITIALS) {
        attributes.add(new Attribute(Constants.NICKNAME_INITIALS, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Nickname.TYPE_CUSTOM) {
        // TODO: String  LABEL DATA3
      } else {
        Log.i("NOT SUPPORTED TYPE NICKNAME", "NOT SUPPORTED TYPE NICKANEME");
      }
    } else if (str.equals(Note.CONTENT_ITEM_TYPE)) {
      if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
        attributes.add(new Attribute(Constants.NOTES, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      }
    } else if (str.equals(StructuredPostal.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == StructuredPostal.TYPE_CUSTOM) {
        // TODO:
        //TYPE_CUSTOM. Put the actual type in LABEL.
        // String  LABEL DATA3
      } else if (type == StructuredPostal.TYPE_HOME) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
          attributes.add(new Attribute(Constants.HOME_FORMATTED_ADDRESS, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
          attributes.add(new Attribute(Constants.HOME_STREET, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
          attributes.add(new Attribute(Constants.HOME_POBOX, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
          attributes.add(new Attribute(Constants.HOME_NEIGHBORHOOD, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
          attributes.add(new Attribute(Constants.HOME_CITY, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
          attributes.add(new Attribute(Constants.HOME_REGION, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
          attributes.add(new Attribute(Constants.HOME_POSTAL_CODE, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
          attributes.add(new Attribute(Constants.HOME_COUNTRY, cursor.getString(cursor.getColumnIndex(Data.DATA10))));
        }
      } else if (type == StructuredPostal.TYPE_WORK) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
          attributes.add(new Attribute(Constants.WORK_FORMATTED_ADDRESS, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
          attributes.add(new Attribute(Constants.WORK_STREET, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
          attributes.add(new Attribute(Constants.WORK_POBOX, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
          attributes.add(new Attribute(Constants.WORK_NEIGHBORHOOD, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
          attributes.add(new Attribute(Constants.WORK_CITY, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
          attributes.add(new Attribute(Constants.WORK_REGION, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
          attributes.add(new Attribute(Constants.WORK_POSTAL_CODE, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
          attributes.add(new Attribute(Constants.WORK_COUNTRY, cursor.getString(cursor.getColumnIndex(Data.DATA10))));
        }
      } else if (type == StructuredPostal.TYPE_OTHER) {
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA1))) {
          attributes.add(new Attribute(Constants.OTHER_FORMATTED_ADDRESS, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA4))) {
          attributes.add(new Attribute(Constants.OTHER_STREET, cursor.getString(cursor.getColumnIndex(Data.DATA4))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA5))) {
          attributes.add(new Attribute(Constants.OTHER_POBOX, cursor.getString(cursor.getColumnIndex(Data.DATA5))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA6))) {
          attributes.add(new Attribute(Constants.OTHER_NEIGHBORHOOD, cursor.getString(cursor.getColumnIndex(Data.DATA6))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA7))) {
          attributes.add(new Attribute(Constants.OTHER_CITY, cursor.getString(cursor.getColumnIndex(Data.DATA7))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA8))) {
          attributes.add(new Attribute(Constants.OTHER_REGION, cursor.getString(cursor.getColumnIndex(Data.DATA8))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA9))) {
          attributes.add(new Attribute(Constants.OTHER_POSTAL_CODE, cursor.getString(cursor.getColumnIndex(Data.DATA9))));
        }
        if (!cursor.isNull(cursor.getColumnIndex(Data.DATA10))) {
          attributes.add(new Attribute(Constants.OTHER_COUNTRY, cursor.getString(cursor.getColumnIndex(Data.DATA10))));
        }
      } else {
        
      }
       
    } else if (str.equals(GroupMembership.CONTENT_ITEM_TYPE)) {
      // TODO:
      //long  GROUP_ROW_ID  DATA1
      //attributes.add(new Attribute(Constants., cursor.getString(cursor.getColumnIndex(Data.DATA1))));
    } else if (str.equals(Website.CONTENT_ITEM_TYPE)) {
      
    } else if (str.equals(Event.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == Event.TYPE_CUSTOM) {
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String  LABEL DATA3
      } else if (type == Event.TYPE_ANNIVERSARY) {
        attributes.add(new Attribute(Constants.EVENT_ANNIVERSARY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Event.TYPE_BIRTHDAY) {
        attributes.add(new Attribute(Constants.EVENT_BIRTHDAY, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == Event.TYPE_OTHER) {
        attributes.add(new Attribute(Constants.EVENT_OTHER, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else {
        Log.i("NOT SUPPORTED TYPE Event", "NOT SUPPORTED TYPE EVENT");
      }
    } else if (str.equals(Relation.CONTENT_ITEM_TYPE)) {
      
    } else if (str.equals(SipAddress.CONTENT_ITEM_TYPE)) {
      Integer type = cursor.getInt(cursor.getColumnIndex(Data.DATA2));
      if (type == SipAddress.TYPE_CUSTOM) {
        // TODO:
        // TYPE_CUSTOM. Put the actual type in LABEL.
        // String  LABEL DATA3
      } else if (type == SipAddress.TYPE_HOME) {
        attributes.add(new Attribute(Constants.HOME_SIP, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == SipAddress.TYPE_WORK) {
        attributes.add(new Attribute(Constants.WORK_SIP, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else if (type == SipAddress.TYPE_OTHER) {
        attributes.add(new Attribute(Constants.OTHER_SIP, cursor.getString(cursor.getColumnIndex(Data.DATA1))));
      } else {
        Log.i("NOT SUPPORTED TYPE SIP", "NOT SUPPORTED TYPE SIP");
      }
    } else if (str.equals(Identity.CONTENT_ITEM_TYPE)) {
      // A data kind representing an Identity related to the contact. 
      // This can be used as a signal by the aggregator to combine raw contacts into contacts, e.g. if two contacts have Identity rows with the same NAMESPACE and IDENTITY values the aggregator can know that they refer to the same person.
      Log.i("  IDENTITY", cursor.getString(cursor.getColumnIndex(Data.DATA1)));
      Log.i("  NAMESPACE", cursor.getString(cursor.getColumnIndex(Data.DATA2)));
    } else {
      Log.i("NOT SUPPORTED TYPE MIME", "NOT SUPPORTED TYPE MIME");
    }
    
    return attributes;
  }
  
  public void startMap(View view) {
    Log.i("aaa","taddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
    //Intent intent = new Intent(this, SelectContactListActivity.class);
    //startActivity(intent);
    //149/81 = 3184i372008420864d477
    //1 = 3184i1b589ff40ef5834c
    Cursor cursor = ContactDetail.fetchAllDataOfContact(getApplicationContext().getContentResolver(), "149");
    Log.i("aaa", cursor.toString());
    ArrayList<Attribute> attributes = new ArrayList<Attribute>();
    while (cursor.moveToNext()) {
      Log.i("aaa", cursor.getString(cursor.getColumnIndex(Data.MIMETYPE)));
      attributes.addAll(fill(cursor));
      Log.i("aaa", "data");
    }
    Log.i("aaa", "konec");
    cursor.close();
    
    /*
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_GOOGLE));
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_INET));
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_ORG));
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_PERSON));
    attributes.add(new Attribute(Constants.OBJECT_CLASS, Constants.OBJECT_CLASS_TOP));
    
    attributes.add(new Attribute(Constants.CN, "id_from table"));
    attributes.add(new Attribute(Constants.SN, "what?"));
    
    attributes.add(new Attribute(Constants.TITLE, "title"));
    attributes.add(new Attribute(Constants.X12_ADDRESS, ""));
    attributes.add(new Attribute(Constants.REGISTRED_ADDRESS, ""));
    attributes.add(new Attribute(Constants.DESTINATION_INDICATOR, ""));
    attributes.add(new Attribute(Constants.INTERNATIONAL_SDN_NUMBER, ""));
    attributes.add(new Attribute(Constants.FASCIMILE_TELEPHONE_NUMBER, ""));
    attributes.add(new Attribute(Constants.PREFERRED_DELIVERY_METHOD, ""));
    attributes.add(new Attribute(Constants.TELEX_NUMBER, ""));
    attributes.add(new Attribute(Constants.PHYSICAL_DELIVERY_OFFICE_NAME,""));
    attributes.add(new Attribute(Constants.OU, ""));
    attributes.add(new Attribute(Constants.ST, ""));
    attributes.add(new Attribute(Constants.L, ""));
    
 // InetOrgPerson
    attributes.add(new Attribute(Constants.AUDIO, ""));
    attributes.add(new Attribute(Constants.BUSSINES_CATEGORY, ""));
    attributes.add(new Attribute(Constants.CAR_LICENCE, ""));
    attributes.add(new Attribute(Constants.DEPARTMENT_NUMBER, ""));
    attributes.add(new Attribute(Constants.DISPLAY_NAME, ""));
    attributes.add(new Attribute(Constants.EMPLOYEE_NUMBER, ""));
    attributes.add(new Attribute(Constants.EMPLOYEE_TYPE, ""));
    attributes.add(new Attribute(Constants.GIVEN_NAME, ""));
    attributes.add(new Attribute(Constants.HOME_PHONE, ""));
    attributes.add(new Attribute(Constants.HOME_POSTAL_ADDRESS, ""));
    attributes.add(new Attribute(Constants.INITIALS, ""));
    attributes.add(new Attribute(Constants.JPEG_PHOTO, ""));
    attributes.add(new Attribute(Constants.LABELED_URI, ""));
    attributes.add(new Attribute(Constants.MAIL, ""));
    attributes.add(new Attribute(Constants.MANAGER, ""));
    attributes.add(new Attribute(Constants.MOBILE, ""));
    attributes.add(new Attribute(Constants.O, ""));
    attributes.add(new Attribute(Constants.PAGER, ""));
    attributes.add(new Attribute(Constants.PHOTO, ""));
    attributes.add(new Attribute(Constants.ROOM_NUMBER, ""));
    attributes.add(new Attribute(Constants.SECRETARY, ""));
    attributes.add(new Attribute(Constants.UID, ""));
    attributes.add(new Attribute(Constants.USER_CERTIFICATE, ""));
    attributes.add(new Attribute(Constants.X500_UNIQUE_IDENTIFIER, ""));
    attributes.add(new Attribute(Constants.PREFERRED_LANGUAGE, ""));
    attributes.add(new Attribute(Constants.USER_SMIME_CERTIFICATE, ""));
    attributes.add(new Attribute(Constants.USER_PKCS12, ""));
    attributes.add(new Attribute(Constants.GIVEN_NAME, ""));
    
  //GoogleContatc
    attributes.add(new Attribute(Constants.ADDITIONAL_NAME, ""));
    attributes.add(new Attribute(Constants.NAME_PREFIX, ""));
    attributes.add(new Attribute(Constants.NAME_SUFFIX, ""));
    attributes.add(new Attribute(Constants.NICKNAME, ""));
    attributes.add(new Attribute(Constants.SHORT_NAME, ""));
    attributes.add(new Attribute(Constants.MAIDEN_NAME, ""));
    attributes.add(new Attribute(Constants.GENDER, ""));
    attributes.add(new Attribute(Constants.NOTES, ""));
    attributes.add(new Attribute(Constants.HOME_MAIL, ""));
    attributes.add(new Attribute(Constants.WORK_MAIL, ""));
    attributes.add(new Attribute(Constants.WORK_PHONE, ""));
    attributes.add(new Attribute(Constants.WEBSITE, ""));
    */
    AddRequest addRequest = new AddRequest("dn", attributes);
    
    Log.i(TAG, addRequest.toLDIFString());
    
    
  }
}
