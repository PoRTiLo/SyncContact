package cz.xsendl00.synccontact;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import cz.xsendl00.synccontact.utils.ContactRow;
import cz.xsendl00.synccontact.utils.GroupRow;

public class Pair implements Parcelable {
  
  //private LinkedHashMap<GroupRow, ArrayList<ContactRow>> groupMemberList  = new LinkedHashMap<GroupRow,ArrayList<ContactRow>>();
  private ArrayList<GroupRow> groupsList = new ArrayList<GroupRow>();
  private ArrayList<ContactRow> contactList = new ArrayList<ContactRow>();
  
  /*public LinkedHashMap<GroupRow, ArrayList<ContactRow>> getGroupMemberList() {
    return groupMemberList;
  }
  public void setGroupMemberList(LinkedHashMap<GroupRow, ArrayList<ContactRow>> groupMemberList) {
    this.groupMemberList = groupMemberList;
  }*/
  public ArrayList<GroupRow> getGroupsList() {
    return groupsList;
  }
  public void setGroupsList(ArrayList<GroupRow> groupsList) {
    this.groupsList = groupsList;
  }
  
  public void setGroupsList(List<GroupRow> groupsList) {
    if (groupsList != null) {
      this.groupsList.addAll(groupsList);
    }
  }
  
  public int describeContents() {
    return 0;
  }

  public Pair() {
    
  }
  public void writeToParcel(Parcel out, int flags) {
    
    out.writeList(this.groupsList);
    //out.writeBundle(this.groupMemberList);
  }

  public static final Parcelable.Creator<Pair> CREATOR = new Parcelable.Creator<Pair>() {
    public Pair createFromParcel(Parcel in) {
      return new Pair(in);
    }

    public Pair[] newArray(int size) {
      return new Pair[size];
    }
  };

  private Pair(Parcel in) {
    in.readList(groupsList, GroupRow.class.getClassLoader());
    in.readList(contactList, ContactRow.class.getClassLoader());
  }
  
  public ArrayList<ContactRow> getContactList() {
    return contactList;
  }
  public void setContactList(ArrayList<ContactRow> contactList) {
    this.contactList = contactList;
  }
  
  public void setContactList(List<ContactRow> contactList) {
    if (contactList != null) {
      this.contactList.addAll(contactList);
    }
  }
}
