package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

public class GroupRow implements Parcelable {

  private String name;
  private String id;
  private Integer idTable;
  private Integer size;
  private Boolean sync;
  private String uuid;
  private static ArrayList<GroupRow> groups = null;

  public GroupRow() {
    this(null, null);
  }

  public GroupRow(String name, String id) {
    this(name, id, null, false, null);
  }

  public GroupRow(String name, String id, Integer size, boolean sync, Integer idTable) {
    this.setId(id);
    this.setName(name);
    this.setSize(size);
    this.sync = sync;
    this.setIdTable(idTable);
  }

  @Override
  public String toString() {
    return "GroupRow [name=" + name + ", id=" + id
        + ", idTable=" + idTable + ", size=" + size + ", sync=" + sync + "]";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String toStringSync() {
    return "Id: "+id + ", sync: " + sync;

  }

  // TODO: list init only once, but what will be happen if add new group?
  public static ArrayList<GroupRow> fetchGroups(ContentResolver contentResolver) {
    Cursor cursor = null;
    try {
      String[] projection = new String[]{ContactsContract.Groups._ID, ContactsContract.Groups.TITLE};
      cursor = contentResolver.query(ContactsContract.Groups.CONTENT_URI, projection, null, null, null);
      groups = new ArrayList<GroupRow>();
      while (cursor.moveToNext()) {
        groups.add(new GroupRow(
          cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE)),
          cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID))
          )
        );
      }
    } catch(Exception ex) {
        ex.printStackTrace();
    } finally {
      try {
        if ( cursor != null && !cursor.isClosed() ) {
          cursor.close();
        }
      } catch(Exception ex) {
        ex.printStackTrace();
      }
    }

    Collections.sort(groups,new Comparator<GroupRow>() {
      @Override
      public int compare(GroupRow lhs, GroupRow rhs) {
        return rhs.getName().compareTo(lhs.getName()) < 0 ? 0 : -1;
      }
    });
    return groups;
  }

  public Boolean isSync() {
    return sync;
  }

  public void setSync(Boolean sync) {
    this.sync = sync;
  }

  public Integer getIdTable() {
    return idTable;
  }

  public void setIdTable(Integer idTable) {
    this.idTable = idTable;
  }

  @Override
  public int describeContents() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    if (this.idTable != null) {
      dest.writeInt(this.idTable);
    }
    dest.writeValue(this.sync);
    if (this.size != null) {
      dest.writeInt(this.size);
    }
  }

  public static final Parcelable.Creator<GroupRow> CREATOR = new Parcelable.Creator<GroupRow>() {
    @Override
    public GroupRow createFromParcel(Parcel in) {
      return new GroupRow(in);
    }

    @Override
    public GroupRow[] newArray(int size) {
      return new GroupRow[size];
    }
  };

  private GroupRow(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.sync = in.readInt() == 1;
    this.idTable = in.readInt();
    this.size = in.readInt();
  }

  /**
   * @return Returns the UUID.
   */
  public String getUuid() {
    if (uuid == null) {
      uuid = ContactRow.generateUUID();
    }
    return uuid;
  }

  /**
   * @param uuid The uuid to set.
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}