package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.database.GroupSQL;
import cz.xsendl00.synccontact.utils.ContactShow;
import cz.xsendl00.synccontact.utils.Group;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;


public class GroupFragment extends Fragment implements android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "GroupFrgment";
  private LinkedHashMap<Group, ArrayList<ContactShow>> groupMemberList;
  private ListView listRow;
  private RowAdapter adapter;
  private ArrayList<Group> groupsList;
  ArrayList<String> checkedValue;
  private static Integer i = 0;

  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_group, container, false);
    initContactList();
    fetchGroup();
    //Log.i(TAG, groupList.toString());
    return rootView;
  }
  
  public void onResume() {
    super.onResume();
    listRow =  (ListView)getActivity().findViewById(R.id.list);
    
    adapter = new RowAdapter(getActivity().getApplicationContext(), this.groupsList, this);
    
    listRow.setAdapter(adapter);
  }
  
  private void fetchGroup() {
    GroupSQL db = new GroupSQL(getActivity());
    
    db.fillGroups(this.groupsList);
    
  }
  
  private void initContactList() {
    
    Log.i(TAG, "init" + i.toString());
    i++;
    groupMemberList = new LinkedHashMap<Group,ArrayList<ContactShow>>();
    this.groupsList = Group.fetchGroups(getActivity().getContentResolver());
    for (Group group : groupsList) {
      ArrayList<ContactShow> groupMembers =new ArrayList<ContactShow>();
      groupMembers.addAll(fetchGroupMembers(group.getId()));
      group.setSize(groupMembers.size());
      groupMemberList.put(group, groupMembers);
    }
  }
  
  private ArrayList<ContactShow> fetchGroupMembers(String groupId){
    ArrayList<ContactShow> groupMembers = new ArrayList<ContactShow>();
    String where = CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" +groupId + " AND " + 
        CommonDataKinds.GroupMembership.MIMETYPE + "='" + CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'";
    String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID, Data.DISPLAY_NAME};
    Cursor cursor = getActivity().getContentResolver().query(Data.CONTENT_URI, projection, where,null, Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    while (cursor.moveToNext()) {
      ContactShow contactShow = new ContactShow();
      contactShow.setName(cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME)));
      contactShow.setId(cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID)));
      groupMembers.add(contactShow);
    } 
    cursor.close();
    return groupMembers;
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    int pos = (Integer)buttonView.getTag();
    //Log.i(TAG, "Pos ["+pos+"]");         
    if (pos != ListView.INVALID_POSITION) {
      Group p = groupsList.get(pos);
      if (p.isSync() != isChecked) {
        GroupSQL db = new GroupSQL(getActivity());
        p.setSync(isChecked);
        db.updateContact(p);
      }
    } 
    //Log.i(TAG, groupsList.toString());
  }

}
