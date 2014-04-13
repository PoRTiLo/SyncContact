package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing the contact's nickname. For example, for Bob Parr ("Mr. Incredible"):
 * @author portilo
 *
 */
public class Nickname implements ContactInterface {
  
  private String nicknameDefault;
  private String nicknameOther;
  private String nicknameMaiden;
  private String nicknameShort;
  private String nicknameInitials;
  
  public Nickname() {
  }
  
  public Nickname(String nicknameDefault, String nicknameOther, String nicknameMaiden, String nicknameShort, String nicknameInitials) {
    this.nicknameDefault = nicknameDefault;
    this.nicknameInitials = nicknameInitials;
    this.nicknameMaiden = nicknameMaiden;
    this.nicknameOther = nicknameOther;
    this.nicknameShort = nicknameShort;
  }
  
  public String getNicknameDefault() {
    return nicknameDefault;
  }
  public void setNicknameDefault(String nicknameDefault) {
    this.nicknameDefault = nicknameDefault;
  }
  public String getNicknameOther() {
    return nicknameOther;
  }
  public void setNicknameOther(String nicknameOther) {
    this.nicknameOther = nicknameOther;
  }
  public String getNicknameMaiden() {
    return nicknameMaiden;
  }
  public void setNicknameMaiden(String nicknameMaiden) {
    this.nicknameMaiden = nicknameMaiden;
  }
  public String getNicknameShort() {
    return nicknameShort;
  }
  public void setNicknameShort(String nicknameShort) {
    this.nicknameShort = nicknameShort;
  }
  public String getNicknameInitials() {
    return nicknameInitials;
  }
  public void setNicknameInitials(String nicknameInitials) {
    this.nicknameInitials = nicknameInitials;
  }

  @Override
  public String toString() {
    return "Nickname [nicknameDefault=" + nicknameDefault + ", nicknameOther="
        + nicknameOther + ", nicknameMaiden=" + nicknameMaiden
        + ", nicknameShort=" + nicknameShort + ", nicknameInitials="
        + nicknameInitials + "]";
  }
  
  public void defaultValue() {
    nicknameDefault = Constants.NICKNAME_DEFAULT;
    nicknameOther = Constants.NICKNAME_OTHER;
    nicknameMaiden = Constants.NICKNAME_MAIDEN;
    nicknameShort = Constants.NICKNAME_SHORT;
    nicknameInitials = Constants.NICKNAME_INITIALS;
  }
}
