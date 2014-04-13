package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

public class Relation implements ContactInterface{

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
}
