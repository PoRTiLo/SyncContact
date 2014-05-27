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
import cz.xsendl00.synccontact.ContactsMergeActivity;
import cz.xsendl00.synccontact.R;
import cz.xsendl00.synccontact.utils.ContactRow;

/**
 * Adapter for merge row.
 *
 * @author xsendl00
 */
public class RowMergerAdapter extends BaseAdapter {

  private Context context;
  private List<ContactRow> data;
  private ViewHolder holder;
  private ContactsMergeActivity contactsMergeActivity;

  /**
   * Constructor.
   * @param context context
   * @param data data
   * @param activity ContactsMergeActivity
   */
  public RowMergerAdapter(Context context, List<ContactRow> data, ContactsMergeActivity activity) {
    super();
    this.contactsMergeActivity = activity;
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

    private TextView nameLocal;
    private TextView nameServer;
    private CheckBox check;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    holder = null;

    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    View view  = convertView;
    if (convertView == null) {
      view = inflater.inflate(R.layout.row_merge, null);
      holder = new ViewHolder();
      holder.nameLocal = (TextView) view.findViewById(R.id.merge_local_contact);
      holder.nameServer = (TextView) view.findViewById(R.id.merge_ldap_contact);
      holder.check = (CheckBox) view.findViewById(R.id.merge_sync);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }
    holder.check.setTag(position);
    holder.check.setOnCheckedChangeListener(contactsMergeActivity);
    ContactRow contactRow = (ContactRow) getItem(position);
    holder.nameServer.setText(contactRow.getName());
    holder.nameLocal.setText(contactRow.getName());
    holder.check.setChecked(contactRow.isSync());
    return view;
  }
}
