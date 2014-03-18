package cz.xsendl00.synccontact.client;

/**
 * The 'person' object class is the basis of an entry that represents a
   human being.
   
  olcObjectClasses: {4}( 2.5.6.6 NAME 'person' DESC 'RFC2256: a person' SUP top 
 STRUCTURAL MUST ( sn $ cn ) MAY ( userPassword $ telephoneNumber $ seeAlso $ 
 description ) )
   
 * @author portilo
 *
 */
public class Person {

  //MUST
  private String cn;
  private String sn;
  //MAY
  private String userPassword;
  private String telephoneNumber;
  private String seeAlso;
  private String description;
  public String getCn() {
    return cn;
  }
  public void setCn(String cn) {
    this.cn = cn;
  }
  public String getSn() {
    return sn;
  }
  public void setSn(String sn) {
    this.sn = sn;
  }
  public String getUserPassword() {
    return userPassword;
  }
  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }
  public String getTelephoneNumber() {
    return telephoneNumber;
  }
  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }
  public String getSeeAlso() {
    return seeAlso;
  }
  public void setSeeAlso(String seeAlso) {
    this.seeAlso = seeAlso;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
}
