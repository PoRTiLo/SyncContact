package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class Group {
  private String name;
  private String id;
  private Integer size;
  
  private static final String TAG = "Group";
  
  public Group(String name, String id) {
    this(name, id, null);
  }
  
  public Group(String name, String id, Integer size) {
    this.setId(id);
    this.setName(name);
    this.setSize(size);
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
    return "Id: "+id + ", name: " + name + ", zise: " + size;
    
  }
  
  public static ArrayList<Group> fetchGroups(ContentResolver contentResolver){
    String[] projection = new String[]{ContactsContract.Groups._ID, ContactsContract.Groups.TITLE};
    Cursor cursor = contentResolver.query(ContactsContract.Groups.CONTENT_URI, projection, null, null, null);
    ArrayList<Group> groupsList = new ArrayList<Group>();
    while (cursor.moveToNext()) {
      groupsList.add(new Group(
        cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE)), 
        cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID))
        )
      );
    }
    cursor.close();
    
    Collections.sort(groupsList,new Comparator<Group>() {
      @Override
      public int compare(Group lhs, Group rhs) {
        return rhs.getName().compareTo(lhs.getName()) < 0 ? 0 : -1;
      }
    });
    Log.i(TAG, groupsList.toString());
    return groupsList;
  }
}