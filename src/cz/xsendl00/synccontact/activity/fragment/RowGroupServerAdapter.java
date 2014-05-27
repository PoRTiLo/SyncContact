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

/**
 * Adapter for {@link RowGroupServerAdapter}.
 */
public class RowGroupServerAdapter extends BaseAdapter {

  private Context context;
  private List<GroupRow> data;
  private ViewHolder holder;
  private GroupServerFragment groupServerFragment;

  /**
   * Constructor.
   * @param context context
   * @param data data
   * @param fragment parent fragment {@link RowGroupServerAdapter}
   */
  public RowGroupServerAdapter(Context context, List<GroupRow> data, GroupServerFragment fragment) {
    super();
    this.groupServerFragment = fragment;
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
    View view = convertView;
    if (convertView == null) {
      view = inflater.inflate(R.layout.row_group, null);
      holder = new ViewHolder();
      holder.groupName = (TextView) view.findViewById(R.id.row_group_name);
      holder.groupSize = (TextView) view.findViewById(R.id.row_group_size);
      holder.checkSync = (CheckBox) view.findViewById(R.id.row_group_sync);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }
    holder.checkSync.setTag(position);
    holder.checkSync.setOnCheckedChangeListener(groupServerFragment);
    GroupRow group = (GroupRow) getItem(position);
    holder.groupName.setText(group.getName());
    holder.groupSize.setText(groupServerFragment.getString(R.string.group_number_of) + group.getMebersUuids().size());
    holder.checkSync.setChecked(group.isSync());
    return view;
  }
}
