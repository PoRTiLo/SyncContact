package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
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
  //private LinkedHashMap<GroupRow, ArrayList<ContactRow>> groupMemberList;
  private ListView listRow;
  private RowGroupAdapter adapter;
  //private ArrayList<GroupRow> groupsList;
  private Pair pair;

  OnHeadlineSelectedListener mCallback;
  
  // newInstance constructor for creating fragment with arguments
  public static GroupFragment newInstance(Pair p) {
    GroupFragment groupFragment = new GroupFragment();
    Bundle args = new Bundle();
    args.putParcelable("pair", p);
    groupFragment.setArguments(args);
    return groupFragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    pair = getArguments().getParcelable("pair");
    //groupsList = pair.getGroupsList();
    //groupMemberList = pair.getGroupMemberList();
  }

  // Container Activity must implement this interface
  public interface OnHeadlineSelectedListener {
      public void onArticleSelected(Pair p);
  }
  
  @Override
  public void onAttach(Activity activity) {
      super.onAttach(activity);
      try {
          mCallback = (OnHeadlineSelectedListener) activity;
      } catch (ClassCastException e) {
          throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
      }
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    mCallback = null;
  }
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View rootView = inflater.inflate(R.layout.fragment_group, container, false);
    return rootView;
  }
  
  public void onResume() {
    super.onResume();
    listRow =  (ListView)getActivity().findViewById(R.id.list_group);
    adapter = new RowGroupAdapter(getActivity().getApplicationContext(), this.pair.getGroupsList(), this);
    listRow.setAdapter(adapter);
    if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         //Log.i(TAG, "ciclick on position: "+position);
         //Log.i(TAG, groupsList.get(position).getName());
         //Log.i(TAG, (groupMemberList.get(groupsList.get(position)).toString()));
         // TODO: show list of contact
         //   Intent i = new Intent(getApplicationContext(), ShowSightsDetail.class);
         //   i.putExtra("sight_id", sight_id);
         //   startActivity(i);
          }
      });
    }
  }

  @Override
  public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
 // mCallback.onArticleSelected(pair);
    int pos = (Integer) buttonView.getTag();
    if (pos != ListView.INVALID_POSITION) {
      GroupRow p = pair.getGroupsList().get(pos);
      if (p.isSync() != isChecked) {
        HelperSQL db = new HelperSQL(getActivity());
        p.setSync(isChecked);
        final String id = p.getId();
        db.updateGroupSync(p);
        Runnable runnable = new Runnable() {
          @Override
          public void run() {
            HelperSQL db = new HelperSQL(getActivity());

            ArrayList<String> list = ContactRow.fetchGroupMembersId(
                getActivity().getContentResolver(), id);
            for (String id : list) {
              db.updateContactSync(id, isChecked);
              for (ContactRow c : pair.getContactList()) {
                if (c.getId().equals(id)) {
                  c.setSync(isChecked);
                }
              }
            }
          }
        };
        new Thread(runnable).start();
      }
      
    }
  }

}
