package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * Notes about the contact.
 * @author portilo
 *
 */
public class Note extends AbstractType implements ContactInterface {
  
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
  
  public static ContentValues compare(Note obj1, Note obj2) {
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
}
