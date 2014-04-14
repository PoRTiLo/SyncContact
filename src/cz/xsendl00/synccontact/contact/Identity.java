package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing an Identity related to the contact.
 * 
 * This can be used as a signal by the aggregator to combine raw contacts into contacts, 
 * e.g. if two contacts have Identity rows with the same NAMESPACE and IDENTITY values the 
 * aggregator can know that they refer to the same person.
 * @author portilo
 *
 */
public class Identity implements ContactInterface {
  
  private String identityText;
  private String identityNamespace;
  
  public String getIdentityText() {
    return identityText;
  }
  public void setIdentityText(String identityText) {
    this.identityText = identityText;
  }
  public String getIdentityNamespace() {
    return identityNamespace;
  }
  public void setIdentityNamespace(String identityNamespace) {
    this.identityNamespace = identityNamespace;
  }

  @Override
  public String toString() {
    return "Identity [identityText=" + identityText + ", identityNamespace="
        + identityNamespace + "]";
  }
  
  public void clear() {
    this.identityNamespace = null;
    this.identityText = null;
  }
  @Override
  public void defaultValue() {
    identityText = Constants.IDENTITY_TEXT;
    identityNamespace = Constants.IDENTITY_NAMESPACE;
  }
  
  public static ContentValues compare(Identity obj1, Identity obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getIdentityNamespace() != null) {
        values.put(Constants.IDENTITY_NAMESPACE, obj2.getIdentityNamespace());
      }
      if (obj2.getIdentityText() != null) {
        values.put(Constants.IDENTITY_TEXT, obj2.getIdentityText());
      }
    } else if (obj1 == null && obj2 == null) { // nothing
      
    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getIdentityNamespace() != null) {
        values.putNull(Constants.IDENTITY_NAMESPACE);
      }
      if (obj1.getIdentityText() != null) {
        values.putNull(Constants.IDENTITY_TEXT);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getIdentityNamespace() != null) {
        values.put(Constants.IDENTITY_NAMESPACE, obj2.getIdentityNamespace());
      } else {
        values.putNull(Constants.IDENTITY_NAMESPACE);
      } 
      if (obj2.getIdentityText() != null) {
        values.put(Constants.IDENTITY_TEXT, obj2.getIdentityText());
      } else {
        values.putNull(Constants.IDENTITY_TEXT);
      }
    }
    return values;
  }
}
