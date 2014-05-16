package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.Data;
import cz.xsendl00.synccontact.utils.Constants;

public class RelationSync extends AbstractType implements ContactInterface {

  private String relationAssistant;
  private String relationBrother;
  private String relationChild;
  private String relationDomesticPartner;
  private String relationFather;
  private String relationFriend;
  private String relationManager;
  private String relationMother;
  private String relationParent;
  private String relationPartner;
  private String relationRefferedBy;
  private String relationRelative;
  private String relationSister;
  private String relationSpouse;

  public String getRelationAssistant() {
    return relationAssistant;
  }

  public void setRelationAssistant(String relationAssistant) {
    this.relationAssistant = relationAssistant;
  }

  public String getRelationBrother() {
    return relationBrother;
  }

  public void setRelationBrother(String relationBrother) {
    this.relationBrother = relationBrother;
  }

  public String getRelationChild() {
    return relationChild;
  }

  public void setRelationChild(String relationChild) {
    this.relationChild = relationChild;
  }

  public String getRelationDomesticPartner() {
    return relationDomesticPartner;
  }

  public void setRelationDomesticPartner(String relationDomesticPartner) {
    this.relationDomesticPartner = relationDomesticPartner;
  }

  public String getRelationFather() {
    return relationFather;
  }

  public void setRelationFather(String relationFather) {
    this.relationFather = relationFather;
  }

  public String getRelationFriend() {
    return relationFriend;
  }

  public void setRelationFriend(String relationFriend) {
    this.relationFriend = relationFriend;
  }

  public String getRelationManager() {
    return relationManager;
  }

  public void setRelationManager(String relationManager) {
    this.relationManager = relationManager;
  }

  public String getRelationMother() {
    return relationMother;
  }

  public void setRelationMother(String relationMother) {
    this.relationMother = relationMother;
  }

  public String getRelationParent() {
    return relationParent;
  }

  public void setRelationParent(String relationParent) {
    this.relationParent = relationParent;
  }

  public String getRelationPartner() {
    return relationPartner;
  }

  public void setRelationPartner(String relationPartner) {
    this.relationPartner = relationPartner;
  }

  public String getRelationRefferedBy() {
    return relationRefferedBy;
  }

  public void setRelationRefferedBy(String relationRefferedBy) {
    this.relationRefferedBy = relationRefferedBy;
  }

  public String getRelationRelative() {
    return relationRelative;
  }

  public void setRelationRelative(String relationRelative) {
    this.relationRelative = relationRelative;
  }

  public String getRelationSister() {
    return relationSister;
  }

  public void setRelationSister(String relationSister) {
    this.relationSister = relationSister;
  }

  public String getRelationSpouse() {
    return relationSpouse;
  }

  public void setRelationSpouse(String relationSpouse) {
    this.relationSpouse = relationSpouse;
  }


  @Override
  public String toString() {
    return "Relation [relationAssistant=" + relationAssistant + ", relationBrother="
        + relationBrother + ", relationChild=" + relationChild + ", relationDomesticPartner="
        + relationDomesticPartner + ", relationFather=" + relationFather + ", relationFriend="
        + relationFriend + ", relationManager=" + relationManager + ", relationMother="
        + relationMother + ", relationParent=" + relationParent + ", relationPartner="
        + relationPartner + ", relationRefferedBy=" + relationRefferedBy + ", relationRelative="
        + relationRelative + ", relationSister=" + relationSister + ", relationSpouse="
        + relationSpouse + "]";
  }

  @Override
  public void defaultValue() {
    relationAssistant = Constants.RELATION_ASSISTANT;
    relationBrother = Constants.RELATION_BROTHER;
    relationChild = Constants.RELATION_CHILD;
    relationDomesticPartner = Constants.RELATION_DOMESTIC_PARTNER;
    relationFather = Constants.RELATION_FATHER;
    relationFriend = Constants.RELATION_FRIEND;
    relationManager = Constants.RELATION_MANAGER;
    relationMother = Constants.RELATION_MOTHER;
    relationParent = Constants.RELATION_PARENT;
    relationPartner = Constants.RELATION_PARTNER;
    relationRefferedBy = Constants.RELATION_REFFERED_BY;
    relationRelative = Constants.RELATION_RELATIVE;
    relationSister = Constants.RELATION_SISTER;
    relationSpouse = Constants.RELATION_SPOUSE;

  }

  public static ContentValues compare(RelationSync obj1, RelationSync obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getRelationAssistant() != null) {
        values.put(Constants.RELATION_ASSISTANT, obj2.getRelationAssistant());
      }
      if (obj2.getRelationBrother() != null) {
        values.put(Constants.RELATION_BROTHER, obj2.getRelationBrother());
      }
      if (obj2.getRelationChild() != null) {
        values.put(Constants.RELATION_CHILD, obj2.getRelationChild());
      }
      if (obj2.getRelationDomesticPartner() != null) {
        values.put(Constants.RELATION_DOMESTIC_PARTNER, obj2.getRelationDomesticPartner());
      }
      if (obj2.getRelationFather() != null) {
        values.put(Constants.RELATION_FATHER, obj2.getRelationFather());
      }
      if (obj2.getRelationFriend() != null) {
        values.put(Constants.RELATION_FRIEND, obj2.getRelationFriend());
      }
      if (obj2.getRelationManager() != null) {
        values.put(Constants.RELATION_MANAGER, obj2.getRelationManager());
      }
      if (obj2.getRelationMother() != null) {
        values.put(Constants.RELATION_MOTHER, obj2.getRelationMother());
      }
      if (obj2.getRelationParent() != null) {
        values.put(Constants.RELATION_PARENT, obj2.getRelationParent());
      }
      if (obj2.getRelationPartner() != null) {
        values.put(Constants.RELATION_PARTNER, obj2.getRelationPartner());
      }
      if (obj2.getRelationRefferedBy() != null) {
        values.put(Constants.RELATION_REFFERED_BY, obj2.getRelationRefferedBy());
      }
      if (obj2.getRelationRelative() != null) {
        values.put(Constants.RELATION_RELATIVE, obj2.getRelationRelative());
      }
      if (obj2.getRelationSister() != null) {
        values.put(Constants.RELATION_SISTER, obj2.getRelationSister());
      }
      if (obj2.getRelationSpouse() != null) {
        values.put(Constants.RELATION_SPOUSE, obj2.getRelationSpouse());
      }
    } else if (obj1 == null && obj2 == null) { // nothing

    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getRelationAssistant() != null) {
        values.putNull(Constants.RELATION_ASSISTANT);
      }
      if (obj1.getRelationBrother() != null) {
        values.putNull(Constants.RELATION_BROTHER);
      }
      if (obj1.getRelationChild() != null) {
        values.putNull(Constants.RELATION_CHILD);
      }
      if (obj1.getRelationDomesticPartner() != null) {
        values.putNull(Constants.RELATION_DOMESTIC_PARTNER);
      }
      if (obj1.getRelationFather() != null) {
        values.putNull(Constants.RELATION_FATHER);
      }
      if (obj1.getRelationFriend() != null) {
        values.putNull(Constants.RELATION_FRIEND);
      }
      if (obj1.getRelationManager() != null) {
        values.putNull(Constants.RELATION_MANAGER);
      }
      if (obj1.getRelationMother() != null) {
        values.putNull(Constants.RELATION_MOTHER);
      }
      if (obj1.getRelationParent() != null) {
        values.putNull(Constants.RELATION_PARENT);
      }
      if (obj1.getRelationPartner() != null) {
        values.putNull(Constants.RELATION_PARTNER);
      }
      if (obj1.getRelationRefferedBy() != null) {
        values.putNull(Constants.RELATION_REFFERED_BY);
      }
      if (obj1.getRelationRelative() != null) {
        values.putNull(Constants.RELATION_RELATIVE);
      }
      if (obj1.getRelationSister() != null) {
        values.putNull(Constants.RELATION_SISTER);
      }
      if (obj1.getRelationSpouse() != null) {
        values.putNull(Constants.RELATION_SPOUSE);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getRelationAssistant() != null) {
        values.put(Constants.RELATION_ASSISTANT, obj2.getRelationAssistant());
      } else {
        values.putNull(Constants.RELATION_ASSISTANT);
      }
      if (obj2.getRelationBrother() != null) {
        values.put(Constants.RELATION_BROTHER, obj2.getRelationBrother());
      } else {
        values.putNull(Constants.RELATION_BROTHER);
      }
      if (obj2.getRelationChild() != null) {
        values.put(Constants.RELATION_CHILD, obj2.getRelationChild());
      } else {
        values.putNull(Constants.RELATION_CHILD);
      }
      if (obj2.getRelationDomesticPartner() != null) {
        values.put(Constants.RELATION_DOMESTIC_PARTNER, obj2.getRelationDomesticPartner());
      } else {
        values.putNull(Constants.RELATION_DOMESTIC_PARTNER);
      }
      if (obj2.getRelationFather() != null) {
        values.put(Constants.RELATION_FATHER, obj2.getRelationFather());
      } else {
        values.putNull(Constants.RELATION_FATHER);
      }
      if (obj2.getRelationFriend() != null) {
        values.put(Constants.RELATION_FRIEND, obj2.getRelationFriend());
      } else {
        values.putNull(Constants.RELATION_FRIEND);
      }
      if (obj2.getRelationManager() != null) {
        values.put(Constants.RELATION_MANAGER, obj2.getRelationManager());
      } else {
        values.putNull(Constants.RELATION_MANAGER);
      }
      if (obj2.getRelationMother() != null) {
        values.put(Constants.RELATION_MOTHER, obj2.getRelationMother());
      } else {
        values.putNull(Constants.RELATION_MOTHER);
      }
      if (obj2.getRelationParent() != null) {
        values.put(Constants.RELATION_PARENT, obj2.getRelationParent());
      } else {
        values.putNull(Constants.RELATION_PARENT);
      }
      if (obj2.getRelationPartner() != null) {
        values.put(Constants.RELATION_PARTNER, obj2.getRelationPartner());
      } else {
        values.putNull(Constants.RELATION_PARTNER);
      }
      if (obj2.getRelationRefferedBy() != null) {
        values.put(Constants.RELATION_REFFERED_BY, obj2.getRelationRefferedBy());
      } else {
        values.putNull(Constants.RELATION_REFFERED_BY);
      }
      if (obj2.getRelationRelative() != null) {
        values.put(Constants.RELATION_RELATIVE, obj2.getRelationRelative());
      } else {
        values.putNull(Constants.RELATION_RELATIVE);
      }
      if (obj2.getRelationSister() != null) {
        values.put(Constants.RELATION_SISTER, obj2.getRelationSister());
      } else {
        values.putNull(Constants.RELATION_SISTER);
      }
      if (obj2.getRelationSpouse() != null) {
        values.put(Constants.RELATION_SPOUSE, obj2.getRelationSpouse());
      } else {
        values.putNull(Constants.RELATION_SPOUSE);
      }
    }
    return values;
  }

  /**
   * @param id
   * @param value
   * @param type
   * @param create
   * @return
   */
  public static ContentProviderOperation add(int id, String value, int type, boolean create) {
    if (create) {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValueBackReference(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Relation.CONTENT_ITEM_TYPE)
          .withValue(Relation.TYPE, type)
          .withValue(Relation.DATA, value)
          .build();
    } else {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValue(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Relation.CONTENT_ITEM_TYPE)
          .withValue(Relation.TYPE, type)
          .withValue(Relation.DATA, value)
          .build();
    }
  }

  public static ContentProviderOperation update(int id, String value, int type) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{String.valueOf(id)})
        .withValue(Data.MIMETYPE, Relation.CONTENT_ITEM_TYPE)
        .withValue(Relation.DATA, value)
        .build();
  }


  public static ArrayList<ContentProviderOperation> operation(int id,
      RelationSync em1,
      RelationSync em2,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      if (em2.getRelationAssistant() != null) {
        ops.add(add(id, em2.getRelationAssistant(), Relation.TYPE_ASSISTANT, create));
      }
      if (em2.getRelationBrother() != null) {
        ops.add(add(id, em2.getRelationBrother(), Relation.TYPE_BROTHER, create));
      }
      if (em2.getRelationChild() != null) {
        ops.add(add(id, em2.getRelationChild(), Relation.TYPE_CHILD, create));
      }
      if (em2.getRelationDomesticPartner() != null) {
        ops.add(add(id, em2.getRelationDomesticPartner(), Relation.TYPE_DOMESTIC_PARTNER, create));
      }
      if (em2.getRelationFather() != null) {
        ops.add(add(id, em2.getRelationFather(), Relation.TYPE_FATHER, create));
      }
      if (em2.getRelationFriend() != null) {
        ops.add(add(id, em2.getRelationFriend(), Relation.TYPE_FRIEND, create));
      }
      if (em2.getRelationManager() != null) {
        ops.add(add(id, em2.getRelationManager(), Relation.TYPE_MANAGER, create));
      }
      if (em2.getRelationMother() != null) {
        ops.add(add(id, em2.getRelationMother(), Relation.TYPE_MOTHER, create));
      }
      if (em2.getRelationParent() != null) {
        ops.add(add(id, em2.getRelationParent(), Relation.TYPE_PARENT, create));
      }
      if (em2.getRelationPartner() != null) {
        ops.add(add(id, em2.getRelationPartner(), Relation.TYPE_PARTNER, create));
      }
      if (em2.getRelationRefferedBy() != null) {
        ops.add(add(id, em2.getRelationRefferedBy(), Relation.TYPE_REFERRED_BY, create));
      }
      if (em2.getRelationRelative() != null) {
        ops.add(add(id, em2.getRelationRelative(), Relation.TYPE_RELATIVE, create));
      }
      if (em2.getRelationSister() != null) {
        ops.add(add(id, em2.getRelationSister(), Relation.TYPE_SISTER, create));
      }
      if (em2.getRelationSpouse() != null) {
        ops.add(add(id, em2.getRelationSpouse(), Relation.TYPE_SPOUSE, create));
      }
    } else if (em1 == null && em2 == null) { // nothing

    } else if (em1 != null && em2 == null) { // delete
      if (em1.getRelationAssistant() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_ASSISTANT), null)));
      }
      if (em1.getRelationBrother() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_BROTHER), null)));
      }
      if (em1.getRelationChild() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_CHILD), null)));
      }
      if (em1.getRelationDomesticPartner() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_DOMESTIC_PARTNER), null)));
      }
      if (em1.getRelationFather() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_FATHER), null)));
      }
      if (em1.getRelationFriend() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_FRIEND), null)));
      }
      if (em1.getRelationManager() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_MANAGER), null)));
      }
      if (em1.getRelationMother() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_MOTHER), null)));
      }
      if (em1.getRelationParent() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_PARENT), null)));
      }
      if (em1.getRelationPartner() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_PARTNER), null)));
      }
      if (em1.getRelationRefferedBy() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_REFERRED_BY), null)));
      }
      if (em1.getRelationRelative() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_RELATIVE), null)));
      }
      if (em1.getRelationSister() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_SISTER), null)));
      }
      if (em1.getRelationSpouse() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Relation.TYPE_SPOUSE), null)));
      }
    } else if (em1 != null && em2 != null) { // clear or update data in db
      ArrayList<ContentProviderOperation> op;
      op = createOps(em1.getRelationAssistant(), em2.getRelationAssistant(), em1.getID(),
          Relation.TYPE_ASSISTANT, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      op = createOps(em1.getRelationBrother(), em2.getRelationBrother(), em1.getID(),
          Relation.TYPE_BROTHER, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getRelationDomesticPartner(), em2.getRelationDomesticPartner(),
          em1.getID(), Relation.TYPE_DOMESTIC_PARTNER, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getRelationFather(), em2.getRelationFather(), em1.getID(),
          Relation.TYPE_FATHER, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getRelationFriend(), em2.getRelationFriend(), em1.getID(),
          Relation.TYPE_FRIEND, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getRelationManager(), em2.getRelationManager(), em1.getID(),
          Relation.TYPE_MANAGER, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getRelationMother(), em2.getRelationMother(), em1.getID(),
          Relation.TYPE_MOTHER, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }
      op = createOps(em1.getRelationParent(), em2.getRelationParent(), em1.getID(),
          Relation.TYPE_PARENT, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getRelationPartner(), em2.getRelationPartner(), em1.getID(),
          Relation.TYPE_PARTNER, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getRelationRefferedBy(), em2.getRelationRefferedBy(), em1.getID(),
          Relation.TYPE_REFERRED_BY, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getRelationRelative(), em2.getRelationRelative(), em1.getID(),
          Relation.TYPE_RELATIVE, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getRelationSister(), em2.getRelationSister(), em1.getID(),
          Relation.TYPE_SISTER, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

      op = createOps(em1.getRelationSpouse(), em2.getRelationSpouse(), em1.getID(),
          Relation.TYPE_SPOUSE, id, create);
      if (op != null && op.size() > 0) {
        ops.addAll(op);
      }

    }
    return ops.size() > 0 ? ops : null;
  }

  /**
   * @param value1
   * @param value2
   * @param ids
   * @param type
   * @param id
   * @return
   */
  private static ArrayList<ContentProviderOperation> createOps(String value1,
      String value2,
      List<ID> ids,
      int type,
      int id,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (value2 != null || value2 != null) {
      String i = ID.getIdByValue(ids, String.valueOf(type), null);
      if (i == null && value2 != null) {
        ops.add(add(id, value2, type, create));
      } else if (i != null && value2 == null) {
        ops.add(GoogleContact.delete(i));
      } else if (i != null && value2 != null && !value2.equals(value1)) {
        ops.add(update(id, value2, type));
      }
    }
    return ops.size() > 0 ? ops : null;
  }
}
