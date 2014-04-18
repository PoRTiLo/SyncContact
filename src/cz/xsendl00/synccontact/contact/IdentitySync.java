package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Identity;
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
public class IdentitySync extends AbstractType implements ContactInterface {
  
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
  
  public static ContentValues compare(IdentitySync obj1, IdentitySync obj2) {
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
  
  public static ContentProviderOperation add(String id, Map<String, String> values) {
    ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
    operationBuilder.withValue(Data.RAW_CONTACT_ID, id).withValue(Data.MIMETYPE, Identity.CONTENT_ITEM_TYPE);
    Iterator<String> iter = values.keySet().iterator();
    while(iter.hasNext()) {
      String key = (String)iter.next();
      String val = (String)values.get(key);
      operationBuilder.withValue(key, val);
    }
    return operationBuilder.build();
  }
  
  public static ContentProviderOperation update(String id, Map<String, String> values) {
    ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(Data.CONTENT_URI);
    operationBuilder.withSelection(Data._ID + "=?",
        new String[] { String.valueOf(id) }).withValue(Data.MIMETYPE,
        Identity.CONTENT_ITEM_TYPE);
    
    Iterator<String> iter = values.keySet().iterator();
    while(iter.hasNext()) {
      String key = (String)iter.next();
      String val = (String)values.get(key);
      operationBuilder.withValue(key, val);
    }
    return operationBuilder.build();
  }
  
  public static ArrayList<ContentProviderOperation> operation(String id, IdentitySync em1, IdentitySync em2) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    Map<String, String> value = new HashMap<String, String>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      
      if (em2.getIdentityNamespace() != null) {
        value.put(Identity.NAMESPACE, em2.getIdentityNamespace());
      }
      if (em2.getIdentityText() != null) {
        value.put(Identity.IDENTITY, em2.getIdentityText());
      }
      ops.add(add(id,value));
    } else if (em1 == null && em2 == null) { // nothing
      
    } else if (em1 != null && em2 == null) { // clear or update data in db
      if (em1.getIdentityNamespace() != null || em1.getIdentityText() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Identity.CONTENT_ITEM_TYPE), null)));
      }
    } else if (em1 != null && em2 != null) { // merge
      String i = ID.getIdByValue(em1.getID(), String.valueOf(Identity.CONTENT_ITEM_TYPE), null);
      if (em2.getIdentityNamespace() == null || em2.getIdentityText() == null) {
        ops.add(GoogleContact.delete(i));
      } else {
        if (em2.getIdentityNamespace() != null) {
          value.put(Identity.NAMESPACE, em2.getIdentityNamespace());
        } else if (em2.getIdentityText() != null) {
          value.put(Identity.IDENTITY, em2.getIdentityText());
        }
        ops.add(update(id,value));
      }
    }
    return ops.size() > 0 ? ops : null;
  }
}
