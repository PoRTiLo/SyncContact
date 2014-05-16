package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.Data;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Notes about the contact.
 *
 * @author portilo
 */
public class NoteSync extends AbstractType implements ContactInterface {

  private String notes;

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Override
  public String toString() {
    return "Note [notes=" + notes + "]";
  }

  @Override
  public void defaultValue() {
    notes = Constants.NOTES;

  }

  public static ContentValues compare(NoteSync obj1, NoteSync obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getNotes() != null) {
        values.put(Constants.NOTES, obj2.getNotes());
      }
    } else if (obj1 == null && obj2 == null) { // nothing

    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getNotes() != null) {
        values.putNull(Constants.NOTES);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getNotes() != null) {
        values.put(Constants.NOTES, obj2.getNotes());
      } else {
        values.putNull(Constants.NOTES);
      }
    }
    return values;
  }

  /**
   * @param id raw_contact id
   * @param value name of protocol
   * @param protocol like {@link Nickname.PROTOCOl_AIM}
   * @param type like Nickname.TYPE_HOME
   * @return
   */
  public static ContentProviderOperation add(int id, String value, boolean create) {
    if (create) {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValueBackReference(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Relation.CONTENT_ITEM_TYPE)
          .withValue(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE)
          .withValue(Note.DATA1, value)
          .build();
    } else {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValue(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE)
          .withValue(Note.DATA1, value)
          .build();
    }
  }

  public static ContentProviderOperation update(String id, String value) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{String.valueOf(id)})
        .withValue(Data.MIMETYPE, Nickname.CONTENT_ITEM_TYPE)
        .withValue(Nickname.DATA, value)
        .build();
  }


  public static ArrayList<ContentProviderOperation> operation(int id,
      NoteSync em1,
      NoteSync em2,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      if (em2.getNotes() != null) {
        ops.add(add(id, em2.getNotes(), create));
      }
    } else if (em1 == null && em2 == null) { // nothing

    } else if (em1 != null && em2 == null) { // delete
      if (em1.getNotes() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), Note.CONTENT_ITEM_TYPE, null)));
      }
    } else if (em1 != null && em2 != null) { // clear or update data in db
      if (em2.getNotes() != null && em1.getNotes() == null) {
        ops.add(add(id, em2.getNotes(), create));
      } else if (em2.getNotes() != null && em1.getNotes() != null
          && !em2.getNotes().equals(em1.getNotes())) {
        ops.add(update(ID.getIdByValue(em1.getID(), Note.CONTENT_ITEM_TYPE, null), em2.getNotes()));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), Note.CONTENT_ITEM_TYPE, null)));
      }
    }
    return ops.size() > 0 ? ops : null;
  }
}
