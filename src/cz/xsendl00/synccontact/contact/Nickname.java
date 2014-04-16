package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing the contact's nickname. For example, for Bob Parr ("Mr. Incredible"):
 * @author portilo
 *
 */
public class Nickname extends AbstractType implements ContactInterface {
  
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
  
  public static ContentValues compare(Nickname obj1, Nickname obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getNicknameDefault() != null) {
        values.put(Constants.NICKNAME_DEFAULT, obj2.getNicknameDefault());
      }
      if (obj2.getNicknameInitials() != null) {
        values.put(Constants.NICKNAME_INITIALS, obj2.getNicknameInitials());
      }
      if (obj2.getNicknameMaiden() != null) {
        values.put(Constants.NICKNAME_MAIDEN, obj2.getNicknameMaiden());
      }
      if (obj2.getNicknameOther() != null) {
        values.put(Constants.NICKNAME_OTHER, obj2.getNicknameOther());
      }
      if (obj2.getNicknameShort() != null) {
        values.put(Constants.NICKNAME_SHORT, obj2.getNicknameShort());
      }
    } else if (obj1 == null && obj2 == null) { // nothing
      
    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getNicknameDefault() != null) {
        values.putNull(Constants.NICKNAME_DEFAULT);
      }
      if (obj1.getNicknameInitials() != null) {
        values.putNull(Constants.NICKNAME_INITIALS);
      }
      if (obj1.getNicknameMaiden() != null) {
        values.putNull(Constants.NICKNAME_MAIDEN);
      }
      if (obj1.getNicknameOther() != null) {
        values.putNull(Constants.NICKNAME_OTHER);
      }
      if (obj1.getNicknameShort() != null) {
        values.putNull(Constants.NICKNAME_SHORT);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getNicknameDefault() != null) {
        values.put(Constants.NICKNAME_DEFAULT, obj2.getNicknameDefault());
      } else {
        values.putNull(Constants.NICKNAME_DEFAULT);
      }
      if (obj2.getNicknameInitials() != null) {
        values.put(Constants.NICKNAME_INITIALS, obj2.getNicknameInitials());
      } else {
        values.putNull(Constants.NICKNAME_INITIALS);
      }
      if (obj2.getNicknameMaiden() != null) {
        values.put(Constants.NICKNAME_MAIDEN, obj2.getNicknameMaiden());
      } else {
        values.putNull(Constants.NICKNAME_MAIDEN);
      }
      if (obj2.getNicknameOther() != null) {
        values.put(Constants.NICKNAME_OTHER, obj2.getNicknameOther());
      } else {
        values.putNull(Constants.NICKNAME_OTHER);
      }
      if (obj2.getNicknameShort() != null) {
        values.put(Constants.NICKNAME_SHORT, obj2.getNicknameShort());
      } else {
        values.putNull(Constants.NICKNAME_SHORT);
      }
    }
    return values;
  }
}
