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
import cz.xsendl00.synccontact.utils.GroupRow;

public class RowGroupAdapter extends BaseAdapter {

  private Context context;
  private List<GroupRow> data;
  private ViewHolder holder;
  private GroupFragment par;
  private ContactFragment parC;

  public RowGroupAdapter(Context context, List<GroupRow> data, GroupFragment par) {
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

    private TextView groupName;
    private TextView groupSize;
    private CheckBox checkSync;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    holder = null;

    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    if (convertView == null) {
      convertView = inflater.inflate(R.layout.row_group, null);
      holder = new ViewHolder();
      holder.groupName = (TextView) convertView.findViewById(R.id.row_group_name);
      holder.groupSize = (TextView) convertView.findViewById(R.id.row_group_size);
      holder.checkSync = (CheckBox) convertView.findViewById(R.id.row_group_sync);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.checkSync.setTag(position);
    holder.checkSync.setOnCheckedChangeListener(par);
    GroupRow group = (GroupRow) getItem(position);
    holder.groupName.setText(group.getName());
    holder.groupSize.setText("Number of contact: " + group.getSize());
    holder.checkSync.setChecked(group.isSync());
    return convertView;
  }
}
