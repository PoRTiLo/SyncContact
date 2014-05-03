package cz.xsendl00.synccontact;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import cz.xsendl00.synccontact.R;

import cz.xsendl00.synccontact.utils.ContactRow;


public class RowContactAdapter extends BaseAdapter implements Filterable {

  private static final String TAG = "RowADAPTER";
  
  private Context context;
  private ArrayList<ContactRow> data;
  ViewHolder holder;
  ContactFragment par;
  
  public RowContactAdapter(Context context, ArrayList<ContactRow> data, ContactFragment par) {
    super();
    this.par = par;
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
    TextView contactName;
    TextView contactGroup;
    CheckBox checkSync;
  }
/*
  private class Inner extends Filter {

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      FilterResults results = new FilterResults();    
      // We implement here the filter logic    
      if (constraint == null || constraint.length() == 0) {        
        // No filter implemented we return all the list        
        results.values = planetList;        
        results.count = planetList.size();    
        }    
      else {        
        // We perform filtering operation        
        List<Planet> nPlanetList = new ArrayList<Planet>();                 
        for (Planet p : planetList) {            
          if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))                
            nPlanetList.add(p);        
          }                 
        results.values = nPlanetList;        
        results.count = nPlanetList.size();     
        }    
      return results; 
      }
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      // TODO Auto-generated method stub
      notifyDataSetChanged();
    }
    
  }
  */
  public View getView(int position, View convertView, ViewGroup parent) {
    holder = null;
    
    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.row_contact, null);
      holder = new ViewHolder();
      holder.contactName = (TextView)convertView.findViewById(R.id.row_contact_name);
      holder.contactGroup = (TextView)convertView.findViewById(R.id.row_conatct_in_group);
      holder.checkSync = (CheckBox)convertView.findViewById(R.id.row_contact_sync);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.checkSync.setTag(position);
    holder.checkSync.setOnCheckedChangeListener( (ContactFragment) par);
    ContactRow contact = (ContactRow) getItem(position);
    holder.contactName.setText(contact.getName());
    //holder.groupSize.setText("Number of contact: " + group.getSize());
    holder.checkSync.setChecked(contact.isSync());
    return convertView;
  }

  @Override
  public Filter getFilter() {
    // TODO Auto-generated method stub
    return null;//new Inner();
  }
}