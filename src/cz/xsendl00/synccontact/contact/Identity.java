package cz.xsendl00.synccontact.contact;

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
public class Identity {
  
  private String identityText = Constants.IDENTITY_TEXT;
  private String identityNamespace = Constants.IDENTITY_NAMESPACE;
  
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

}
