package cz.xsendl00.synccontact.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Class for data about groups.
 */
public class GroupRow extends AbstractRow {

  private Integer size;
  private List<String> mebersUuids = new ArrayList<String>();
  private static ArrayList<GroupRow> groups = null;

  /**
   * Class for data about groups.
   */
  public GroupRow() {
    this(null, null);
  }

  /**
   * Class for data about groups.
   * @param name name name of group
   * @param id id in provider contact database.
   */
  public GroupRow(String name, Integer id) {
    this(name, id, null, false, null, null);
  }

  /**
   * Class for data about groups.
   * @param name name of group
   * @param id id in provider contact database.
   * @param size number of members
   * @param sync true/false is sync/no
   * @param idTable id of local table
   * @param uuid UUID
   */
  public GroupRow(String name, Integer id, Integer size, boolean sync, Integer idTable, String uuid) {
    super(id, name, sync, idTable, uuid);
    this.setId(id);
    this.setName(name);
    this.setSize(size);
    this.sync = sync;
    this.setIdTable(idTable);
  }


  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "GroupRow [size=" + size + ", mebersUuids=" + mebersUuids + ", name=" + name + ", id="
        + id + ", uuid=" + uuid + ", sync=" + sync + ", idTable=" + idTable + "]";
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * List initialize of {@link GroupRow} from database local..
   * @param contentResolver contentResolver
   * @return list of {@link GroupRow}
   */
  public static ArrayList<GroupRow> fetchGroups(ContentResolver contentResolver, String where) {
    Cursor cursor = null;
    try {
      String[] projection = new String[]{ContactsContract.Groups._ID, ContactsContract.Groups.TITLE,
          ContactsContract.Groups.SYNC1, ContactsContract.Groups.SYNC2, ContactsContract.Groups.SYNC3,
          ContactsContract.Groups.SYNC4};
      cursor = contentResolver.query(ContactsContract.Groups.CONTENT_URI, projection, where, null,
          ContactsContract.Groups.TITLE + " COLLATE LOCALIZED ASC");
      groups = new ArrayList<GroupRow>();
      while (cursor.moveToNext()) {
        GroupRow groupRow = new GroupRow();
        groupRow.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE)));
        groupRow.setId(cursor.getInt(cursor.getColumnIndex(ContactsContract.Groups._ID)));
        groupRow.setSync(cursor.getInt(cursor.getColumnIndex(ContactsContract.Groups.SYNC1)) == 1);
        groupRow.setLastSyncTime(cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.SYNC2)));
        groupRow.setUuid(cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.SYNC4)));
        groups.add(groupRow);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
        if (cursor != null && !cursor.isClosed()) {
          cursor.close();
        }
    }

    Collections.sort(groups, new Comparator<GroupRow>() {

      @Override
      public int compare(GroupRow lhs, GroupRow rhs) {
        return rhs.getName().compareTo(lhs.getName()) < 0 ? 0 : -1;
      }
    });
    return groups;
  }

  /**
   * @return Returns the mebersUuids.
   */
  public List<String> getMebersUuids() {
    return mebersUuids;
  }

  /**
   * @param mebersUuids The mebersUuids to set.
   */
  public void setMebersUuids(List<String> mebersUuids) {
    this.mebersUuids = mebersUuids;
  }
}
