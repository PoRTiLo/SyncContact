package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.database.GroupSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class GroupFragment extends Fragment implements android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "GroupFrgment";
  private LinkedHashMap<GroupRow, ArrayList<ContactRow>> groupMemberList;
  private ListView listRow;
  private RowGroupAdapter adapter;
  private ArrayList<GroupRow> groupsList;
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
    listRow =  (ListView)getActivity().findViewById(R.id.list_group);
    
    adapter = new RowGroupAdapter(getActivity().getApplicationContext(), this.groupsList, this);
    
    listRow.setAdapter(adapter);
    
    if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         Log.i(TAG, "ciclick on position: "+position);
         Log.i(TAG, groupsList.get(position).getName());
         Log.i(TAG, (groupMemberList.get(groupsList.get(position)).toString()));
         // TODO: show list of contact
         //   Intent i = new Intent(getApplicationContext(), ShowSightsDetail.class);
         //   i.putExtra("sight_id", sight_id);
         //   startActivity(i);
          }
      });
  }
  }
  
  private void fetchGroup() {
    GroupSQL db = new GroupSQL(getActivity());
    db.fillGroups(this.groupsList);
  }
  
  private void initContactList() {
    
    Log.i(TAG, "init" + i.toString());
    i++;
    groupMemberList = new LinkedHashMap<GroupRow,ArrayList<ContactRow>>();
    this.groupsList = GroupRow.fetchGroups(getActivity().getContentResolver());
    for (GroupRow group : groupsList) {
      ArrayList<ContactRow> groupMembers =new ArrayList<ContactRow>();
      groupMembers.addAll(ContactRow.fetchGroupMembers(getActivity().getContentResolver(), group.getId()));
      group.setSize(groupMembers.size());
      groupMemberList.put(group, groupMembers);
    }
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    int pos = (Integer)buttonView.getTag();
    //Log.i(TAG, "Pos ["+pos+"]");         
    if (pos != ListView.INVALID_POSITION) {
      GroupRow p = groupsList.get(pos);
      if (p.isSync() != isChecked) {
        GroupSQL db = new GroupSQL(getActivity());
        p.setSync(isChecked);
        db.updateContact(p);
      }
    } 
    //Log.i(TAG, groupsList.toString());
  }

}
