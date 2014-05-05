package cz.xsendl00.synccontact;

import java.util.ArrayList;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.database.HelperSQL;
import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GroupFragment extends Fragment implements
    android.widget.CompoundButton.OnCheckedChangeListener {

  private static final String TAG = "GroupFrgment";
  private ListView listRow;
  private RowGroupAdapter adapter;
  private Pair pair;
  private boolean first = false;

  OnHeadlineSelectedListener mCallback;

  // newInstance constructor for creating fragment with arguments
  public static GroupFragment newInstance(Pair p, boolean first) {
    GroupFragment groupFragment = new GroupFragment();
    Bundle args = new Bundle();
    args.putParcelable("pair", p);
    args.putBoolean("FIRST", first);
    groupFragment.setArguments(args);
    return groupFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    pair = getArguments().getParcelable("pair");
    first = getArguments().getBoolean("FIRST");
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
      throw new ClassCastException(activity.toString()
          + " must implement OnHeadlineSelectedListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mCallback = null;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View rootView = null;
    if (first) {
      rootView = inflater.inflate(R.layout.fragment_group, container, false);
    } else {
      rootView = inflater.inflate(R.layout.fragment_group_simply, container,
          false);
    }

    return rootView;
  }

  public void onResume() {
    super.onResume();
    listRow = (ListView) getActivity().findViewById(R.id.list_group);
    adapter = new RowGroupAdapter(getActivity().getApplicationContext(),
        this.pair.getGroupsList(), this);
    listRow.setAdapter(adapter);
    if (listRow != null) {
      listRow.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
          if (pair.getGroupsList().get(position).getSize() != 0) {
            Intent i = new Intent(getActivity().getApplicationContext(),
                ContactsActivity.class);
            i.putExtra("ID", pair.getGroupsList().get(position).getId());
            i.putExtra("NAME", pair.getGroupsList().get(position).getName());
            startActivity(i);
          } else {
            Toast toast = Toast.makeText(getActivity(), R.string.group_toast,
                Toast.LENGTH_SHORT);
            toast.show();
          }
        }
      });
    }
  }

  @Override
  public void onCheckedChanged(final CompoundButton buttonView,
      final boolean isChecked) {
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
            final ArrayList<String> list = ContactRow.fetchGroupMembersId(
                getActivity().getContentResolver(), id);
            new Thread(new Runnable() {
              public void run() {
                for (String id : list) {
                  for (ContactRow c : pair.getContactList()) {
                    if (c.getId().equals(id)) {
                      c.setSync(isChecked);
                      break;
                    }
                  }
                }
              }
            }).start();
            for (String id : list) {
              db.updateContactSync(id, isChecked);
            }
          }
        };
        new Thread(runnable).start();
      }
    }
  }

}
