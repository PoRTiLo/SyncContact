package cz.xsendl00.synccontact;

import java.util.ArrayList;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
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

public class ContactFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private ArrayList<ContactRow> contactList;

  private ListView listRow;
  private RowContactAdapter adapter;
  private boolean first = false;

  // newInstance constructor for creating fragment with arguments
  public static ContactFragment newInstance(Pair p, boolean first) {
    Log.i("ContactFragment", "newInstance");
    ContactFragment contactFragment = new ContactFragment();
    Bundle args = new Bundle();
    args.putParcelable("pair", p);
    args.putBoolean("FIRST", first);
    contactFragment.setArguments(args);
    return contactFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Pair p = getArguments().getParcelable("pair");
    first = getArguments().getBoolean("FIRST");
    contactList = p.getContactList();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = null;
    if (first) {
      rootView = inflater.inflate(R.layout.fragment_contact, container, false);
    } else {
      rootView = inflater.inflate(R.layout.fragment_contact_simply, container, false);
    }
    return rootView;
  }

  public void onResume() {
    
    super.onResume();
    
    listRow = (ListView) getActivity().findViewById(R.id.list_contact);
    adapter = new RowContactAdapter(getActivity().getApplicationContext(), this.contactList, this);
    listRow.setAdapter(adapter);

    if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
          // TODO: show info -> edit contact
        }
      });
    }
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
    final int pos = (Integer) buttonView.getTag();
    if (pos != ListView.INVALID_POSITION) {
      ContactRow p = contactList.get(pos);
      final ContactRow p1 = p;
      if (p.isSync() != isChecked) {
        p.setSync(isChecked);
        new Thread(new Runnable() {
          public void run() {
            HelperSQL db = new HelperSQL(getActivity());
            db.updateContactSync(p1);
          }
        }).start();
      }
    }
  }

}
