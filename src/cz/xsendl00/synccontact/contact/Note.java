package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * Notes about the contact.
 * @author portilo
 *
 */
public class Note  implements ContactInterface {
  
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
  
}
