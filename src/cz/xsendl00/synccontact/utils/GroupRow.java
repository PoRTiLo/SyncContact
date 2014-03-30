package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

public class GroupRow {
  private String name;
  private String id;
  private Integer idTable;
  private Integer size;
  private Boolean sync;
  
  public GroupRow() {
    this(null, null, null, false, null);
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
  
  @Override
  public String toString() {
    return "Id: "+id + ", name: " + name + ", size: " + size + ", sync: " + sync + ", tableId:" + idTable;
    
  }
  
  public static ArrayList<GroupRow> fetchGroups(ContentResolver contentResolver){
    String[] projection = new String[]{ContactsContract.Groups._ID, ContactsContract.Groups.TITLE};
    Cursor cursor = contentResolver.query(ContactsContract.Groups.CONTENT_URI, projection, null, null, null);
    ArrayList<GroupRow> groupsList = new ArrayList<GroupRow>();
    while (cursor.moveToNext()) {
      groupsList.add(new GroupRow(
        cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE)), 
        cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID))
        )
      );
    }
    cursor.close();
    
    Collections.sort(groupsList,new Comparator<GroupRow>() {
      @Override
      public int compare(GroupRow lhs, GroupRow rhs) {
        return rhs.getName().compareTo(lhs.getName()) < 0 ? 0 : -1;
      }
    });
    return groupsList;
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
}