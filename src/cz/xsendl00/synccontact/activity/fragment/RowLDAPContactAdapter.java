package cz.xsendl00.synccontact.activity.fragment;

import java.util.List;

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

public class RowLDAPContactAdapter extends BaseAdapter {

  private Context context;
  private List<ContactRow> data;
  private ViewHolder holder;
  private ContactLDAPFragment par;
  private GroupFragment parG;

  public RowLDAPContactAdapter(Context context, List<ContactRow> data, ContactLDAPFragment par) {
    super();
    this.par = par;
    this.data = data;
    this.context = context;
  }

  @Override
  public int getCount() {
    return this.data.size();
  }

  @Override
  public Object getItem(int position) {
    return data.get(position);
  }

  @Override
  public long getItemId(int position) {
    return data.indexOf(getItem(position));
  }

  /* private view holder class */
  private class ViewHolder {

    private TextView contactName;
    private TextView contactGroup;
    private CheckBox checkSync;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    holder = null;

    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    if (convertView == null) {
      convertView = inflater.inflate(R.layout.row_contact, null);
      holder = new ViewHolder();
      holder.contactName = (TextView) convertView.findViewById(R.id.row_contact_name);
      holder.contactGroup = (TextView) convertView.findViewById(R.id.row_conatct_in_group);
      holder.checkSync = (CheckBox) convertView.findViewById(R.id.row_contact_sync);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.checkSync.setTag(position);
    holder.checkSync.setOnCheckedChangeListener(par);
    ContactRow contact = (ContactRow) getItem(position);
    holder.contactName.setText(contact.getName());
    // holder.groupSize.setText("Number of contact: " + group.getSize());
    holder.checkSync.setChecked(contact.isSync());
    return convertView;
  }
}
