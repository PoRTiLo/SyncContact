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


/**
 * Adapter for {@link ContactServerFragment}.
 */
public class RowContactServerAdapter extends BaseAdapter {

  private Context context;
  private List<ContactRow> data;
  private ViewHolder holder;
  private ContactServerFragment contactServerFragment;

  /**
   * Constructor.
   *
   * @param context context
   * @param data data
   * @param fragment {@link ContactServerFragment}
   */
  public RowContactServerAdapter(Context context,
      List<ContactRow> data,
      ContactServerFragment fragment) {
    super();
    this.contactServerFragment = fragment;
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
    private CheckBox checkSync;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    holder = null;

    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    View view = convertView;
    if (view == null) {
      view = inflater.inflate(R.layout.row_contact, null);
      holder = new ViewHolder();
      holder.contactName = (TextView) view.findViewById(R.id.row_contact_name);
      holder.checkSync = (CheckBox) view.findViewById(R.id.row_contact_sync);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }
    holder.checkSync.setTag(position);
    holder.checkSync.setOnCheckedChangeListener(contactServerFragment);
    ContactRow contact = (ContactRow) getItem(position);
    holder.contactName.setText(contact.getName());
    holder.checkSync.setChecked(contact.isSync());
    return view;
  }
}
