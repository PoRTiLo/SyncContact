package cz.xsendl00.synccontact.utils;

public class ContactShow {
  private String name;
  private String id;
  private String phoneNumber;
  private String phoneDisplayName;
  private String phoneType;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneDisplayName() {
    return phoneDisplayName;
  }

  public void setPhoneDisplayName(String phoneDisplayName) {
    this.phoneDisplayName = phoneDisplayName;
  }

  public String getPhoneType() {
    return phoneType;
  }

  public void setPhoneType(String phoneType) {
    this.phoneType = phoneType;
  }
  
  @Override
  public String toString() {
    return "Id: "+id + ", name: " + name + ", phoneNumber: " + phoneNumber + ", phoneDisplayName: " + phoneDisplayName + ", phoneType: " + phoneType + "\n";
    
  }
}
