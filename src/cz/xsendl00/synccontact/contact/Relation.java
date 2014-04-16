package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
import cz.xsendl00.synccontact.utils.Constants;

public class Relation extends AbstractType implements ContactInterface{

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
  return "Relation [relationAssistant=" + relationAssistant
      + ", relationBrother=" + relationBrother + ", relationChild="
      + relationChild + ", relationDomesticPartner="
      + relationDomesticPartner + ", relationFather=" + relationFather
      + ", relationFriend=" + relationFriend + ", relationManager="
      + relationManager + ", relationMother=" + relationMother
      + ", relationParent=" + relationParent + ", relationPartner="
      + relationPartner + ", relationRefferedBy=" + relationRefferedBy
      + ", relationRelative=" + relationRelative + ", relationSister="
      + relationSister + ", relationSpouse=" + relationSpouse + "]";
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
  
  public static ContentValues compare(Relation obj1, Relation obj2) {
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
}
