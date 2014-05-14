package cz.xsendl00.synccontact.activity.fragment;

import java.util.List;

import cz.xsendl00.synccontact.ContactMergeActivity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import cz.xsendl00.synccontact.R;
import cz.xsendl00.synccontact.utils.ContactRow;

/**
 * Adapter for merge row.
 * @author portilo
 *
 */
public class RowMergerAdapter extends BaseAdapter {
  
  private Context context;
  private List<ContactRow> data;
  ViewHolder holder;
  ContactMergeActivity contactMergeActivity;
  
  /**
   * 
   * @param context
   * @param data
   * @param activity
   */
  public RowMergerAdapter(Context context, List<ContactRow> data, ContactMergeActivity activity) {
    super();
    this.contactMergeActivity = activity;
    this.data = data;
    this.context = context;
  }

  public int getCount() {
    return this.data.size();
  }

  public Object getItem(int position) {
    return data.get(position);
  }

  public long getItemId(int position) {
    return data.indexOf(getItem(position));
  }
  
  /*private view holder class*/
  private class ViewHolder {
    TextView nameLocal;
    TextView nameServer;
    CheckBox check;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    holder = null;
    
    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.row_merge, null);
      holder = new ViewHolder();
      holder.nameLocal = (TextView)convertView.findViewById(R.id.merge_local_contact);
      holder.nameServer = (TextView)convertView.findViewById(R.id.merge_ldap_contact);
      holder.check = (CheckBox)convertView.findViewById(R.id.merge_sync);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.check.setTag(position);
    holder.check.setOnCheckedChangeListener(contactMergeActivity);
    ContactRow contactRow = (ContactRow) getItem(position);
    holder.nameServer.setText(contactRow.getName());
    holder.nameLocal.setText(contactRow.getName());
    holder.check.setChecked(contactRow.isSync());
    return convertView;
  }
}