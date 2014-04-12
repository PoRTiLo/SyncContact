package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * Notes about the contact.
 * @author portilo
 *
 */
public class Note {
  
  private String notes = Constants.NOTES;

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
}
