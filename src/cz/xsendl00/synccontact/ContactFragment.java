package cz.xsendl00.synccontact;

import java.util.ArrayList;

import com.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;

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

public class ContactFragment extends Fragment implements android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "ContactFragment";
  
  private ArrayList<ContactRow> contactList;
  private ListView listRow;
  private RowContactAdapter adapter;
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
    initContact();
    fetchContact();
    return rootView;
  }

  private void initContact() {
    contactList = ContactRow.fetchAllContact(getActivity().getContentResolver());
  }
  
  private void fetchContact() {
    HelperSQL db = new HelperSQL(getActivity());
    db.fillContacts(this.contactList);
  }
  
  public void onResume() {
    super.onResume();
    listRow =  (ListView)getActivity().findViewById(R.id.list_contact);
    
    adapter = new RowContactAdapter(getActivity().getApplicationContext(), this.contactList, this);
    
    listRow.setAdapter(adapter);
    
    if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         ///Log.i(TAG, "ciclick on position: "+position);
         //Log.i(TAG, contactList.get(position).getName());
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
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    int pos = (Integer)buttonView.getTag();
    //Log.i(TAG, "Pos ["+pos+"]");         
    if (pos != ListView.INVALID_POSITION) {
      ContactRow p = contactList.get(pos);
      if (p.isSync() != isChecked) {
        HelperSQL db = new HelperSQL(getActivity());
        p.setSync(isChecked);
        db.updateContact(p);
      }
    } 
    //Log.i(TAG, groupsList.toString());
  }
}
