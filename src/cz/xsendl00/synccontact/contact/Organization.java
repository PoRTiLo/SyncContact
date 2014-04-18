package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing an organization.
 * @author portilo
 *
 */
public class Organization extends AbstractType implements ContactInterface {

  private String organizationWorkCompany;
  private String organizationWorkTitle;
  private String organizationWorkDepartment;
  private String organizationWorkJobDescription;
  private String organizationWorkSymbol;
  private String organizationWorkPhoneticName;
  private String organizationWorkOfficeLocation;
  private String organizationWorkPhoneticNameStyle;
  private String organizationOtherCompany;
  private String organizationOtherTitle;
  private String organizationOtherDepartment;
  private String organizationOtherJobDescription;
  private String organizationOtherSymbol;
  private String organizationOtherPhoneticName;
  private String organizationOtherOfficeLocation;
  private String organizationOtherPhoneticNameStyle;
  
  public String getOrganizationWorkCompany() {
    return organizationWorkCompany;
  }
  public void setOrganizationWorkCompany(String organizationWorkCompany) {
    this.organizationWorkCompany = organizationWorkCompany;
  }
  public String getOrganizationWorkTitle() {
    return organizationWorkTitle;
  }
  public void setOrganizationWorkTitle(String organizationWorkTitle) {
    this.organizationWorkTitle = organizationWorkTitle;
  }
  public String getOrganizationWorkDepartment() {
    return organizationWorkDepartment;
  }
  public void setOrganizationWorkDepartment(String organizationWorkDepartment) {
    this.organizationWorkDepartment = organizationWorkDepartment;
  }
  public String getOrganizationWorkJobDescription() {
    return organizationWorkJobDescription;
  }
  public void setOrganizationWorkJobDescription(
      String organizationWorkJobDescription) {
    this.organizationWorkJobDescription = organizationWorkJobDescription;
  }
  public String getOrganizationWorkSymbol() {
    return organizationWorkSymbol;
  }
  public void setOrganizationWorkSymbol(String organizationWorkSymbol) {
    this.organizationWorkSymbol = organizationWorkSymbol;
  }
  public String getOrganizationWorkPhoneticName() {
    return organizationWorkPhoneticName;
  }
  public void setOrganizationWorkPhoneticName(String organizationWorkPhoneticName) {
    this.organizationWorkPhoneticName = organizationWorkPhoneticName;
  }
  public String getOrganizationWorkOfficeLocation() {
    return organizationWorkOfficeLocation;
  }
  public void setOrganizationWorkOfficeLocation(
      String organizationWorkOfficeLocation) {
    this.organizationWorkOfficeLocation = organizationWorkOfficeLocation;
  }
  public String getOrganizationWorkPhoneticNameStyle() {
    return organizationWorkPhoneticNameStyle;
  }
  public void setOrganizationWorkPhoneticNameStyle(
      String organizationWorkPhoneticNameStyle) {
    this.organizationWorkPhoneticNameStyle = organizationWorkPhoneticNameStyle;
  }
  public String getOrganizationOtherCompany() {
    return organizationOtherCompany;
  }
  public void setOrganizationOtherCompany(String organizationOtherCompany) {
    this.organizationOtherCompany = organizationOtherCompany;
  }
  public String getOrganizationOtherTitle() {
    return organizationOtherTitle;
  }
  public void setOrganizationOtherTitle(String organizationOtherTitle) {
    this.organizationOtherTitle = organizationOtherTitle;
  }
  public String getOrganizationOtherDepartment() {
    return organizationOtherDepartment;
  }
  public void setOrganizationOtherDepartment(String organizationOtherDepartment) {
    this.organizationOtherDepartment = organizationOtherDepartment;
  }
  public String getOrganizationOtherJobDescription() {
    return organizationOtherJobDescription;
  }
  public void setOrganizationOtherJobDescription(
      String organizationOtherJobDescription) {
    this.organizationOtherJobDescription = organizationOtherJobDescription;
  }
  public String getOrganizationOtherSymbol() {
    return organizationOtherSymbol;
  }
  public void setOrganizationOtherSymbol(String organizationOtherSymbol) {
    this.organizationOtherSymbol = organizationOtherSymbol;
  }
  public String getOrganizationOtherPhoneticName() {
    return organizationOtherPhoneticName;
  }
  public void setOrganizationOtherPhoneticName(
      String organizationOtherPhoneticName) {
    this.organizationOtherPhoneticName = organizationOtherPhoneticName;
  }
  public String getOrganizationOtherOfficeLocation() {
    return organizationOtherOfficeLocation;
  }
  public void setOrganizationOtherOfficeLocation(
      String organizationOtherOfficeLocation) {
    this.organizationOtherOfficeLocation = organizationOtherOfficeLocation;
  }
  public String getOrganizationOtherPhoneticNameStyle() {
    return organizationOtherPhoneticNameStyle;
  }
  public void setOrganizationOtherPhoneticNameStyle(
      String organizationOtherPhoneticNameStyle) {
    this.organizationOtherPhoneticNameStyle = organizationOtherPhoneticNameStyle;
  }
  
  @Override
  public String toString() {
    return "Organization [organizationWorkCompany=" + organizationWorkCompany
        + ", organizationWorkTitle=" + organizationWorkTitle
        + ", organizationWorkDepartment=" + organizationWorkDepartment
        + ", organizationWorkJobDescription=" + organizationWorkJobDescription
        + ", organizationWorkSymbol=" + organizationWorkSymbol
        + ", organizationWorkPhoneticName=" + organizationWorkPhoneticName
        + ", organizationWorkOfficeLocation=" + organizationWorkOfficeLocation
        + ", organizationWorkPhoneticNameStyle="
        + organizationWorkPhoneticNameStyle + ", organizationOtherCompany="
        + organizationOtherCompany + ", organizationOtherTitle="
        + organizationOtherTitle + ", organizationOtherDepartment="
        + organizationOtherDepartment + ", organizationOtherJobDescription="
        + organizationOtherJobDescription + ", organizationOtherSymbol="
        + organizationOtherSymbol + ", organizationOtherPhoneticName="
        + organizationOtherPhoneticName + ", organizationOtherOfficeLocation="
        + organizationOtherOfficeLocation
        + ", organizationOtherPhoneticNameStyle="
        + organizationOtherPhoneticNameStyle + "]";
  }
  @Override
  public void defaultValue() {
    organizationWorkCompany = Constants.ORGANIZATION_WORK_COMPANY;
    organizationWorkTitle = Constants.ORGANIZATION_WORK_TITLE;
    organizationWorkDepartment = Constants.ORGANIZATION_WORK_DEPARTMENT;
    organizationWorkJobDescription = Constants.ORGANIZATION_WORK_JOB_DESCRIPTION;
    organizationWorkSymbol = Constants.ORGANIZATION_WORK_SYMBOL;
    organizationWorkPhoneticName = Constants.ORGANIZATION_WORK_PHONETIC_NAME;
    organizationWorkOfficeLocation = Constants.ORGANIZATION_WORK_OFFICE_LOCATION;
    organizationWorkPhoneticNameStyle = Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE;
    organizationOtherCompany = Constants.ORGANIZATION_OTHER_COMPANY;
    organizationOtherTitle = Constants.ORGANIZATION_OTHER_TITLE;
    organizationOtherDepartment = Constants.ORGANIZATION_OTHER_DEPARTMENT;
    organizationOtherJobDescription = Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION;
    organizationOtherSymbol = Constants.ORGANIZATION_OTHER_SYMBOL;
    organizationOtherPhoneticName = Constants.ORGANIZATION_OTHER_PHONETIC_NAME;
    organizationOtherOfficeLocation = Constants.ORGANIZATION_OTHER_OFFICE_LOCATION;
    organizationOtherPhoneticNameStyle = Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE;
  }
  
  public static ContentValues compare(Organization obj1, Organization obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getOrganizationOtherCompany() != null) {
        values.put(Constants.ORGANIZATION_OTHER_COMPANY, obj2.getOrganizationOtherCompany());
      }
      if (obj2.getOrganizationOtherDepartment() != null) {
        values.put(Constants.ORGANIZATION_OTHER_DEPARTMENT, obj2.getOrganizationOtherDepartment());
      }
      if (obj2.getOrganizationOtherJobDescription() != null) {
        values.put(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION, obj2.getOrganizationOtherJobDescription());
      }
      if (obj2.getOrganizationOtherOfficeLocation() != null) {
        values.put(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION, obj2.getOrganizationOtherOfficeLocation());
      }
      if (obj2.getOrganizationOtherPhoneticName() != null) {
        values.put(Constants.ORGANIZATION_OTHER_PHONETIC_NAME, obj2.getOrganizationOtherPhoneticName());
      }
      if (obj2.getOrganizationOtherPhoneticNameStyle() != null) {
        values.put(Constants.ORGANIZATION_OTHER_PHONETIC_NAME, obj2.getOrganizationOtherPhoneticNameStyle());
      }
      if (obj2.getOrganizationOtherSymbol() != null) {
        values.put(Constants.ORGANIZATION_OTHER_SYMBOL, obj2.getOrganizationOtherSymbol());
      }
      if (obj2.getOrganizationOtherTitle() != null) {
        values.put(Constants.ORGANIZATION_WORK_TITLE, obj2.getOrganizationOtherTitle());
      }
      if (obj2.getOrganizationWorkCompany() != null) {
        values.put(Constants.ORGANIZATION_WORK_COMPANY, obj2.getOrganizationWorkCompany());
      }
      if (obj2.getOrganizationWorkDepartment() != null) {
        values.put(Constants.ORGANIZATION_WORK_DEPARTMENT, obj2.getOrganizationWorkDepartment());
      }
      if (obj2.getOrganizationWorkJobDescription() != null) {
        values.put(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION, obj2.getOrganizationWorkJobDescription());
      }
      if (obj2.getOrganizationWorkOfficeLocation() != null) {
        values.put(Constants.ORGANIZATION_WORK_OFFICE_LOCATION, obj2.getOrganizationWorkOfficeLocation());
      }
      if (obj2.getOrganizationWorkPhoneticName() != null) {
        values.put(Constants.ORGANIZATION_WORK_PHONETIC_NAME, obj2.getOrganizationWorkPhoneticName());
      }
      if (obj2.getOrganizationWorkPhoneticNameStyle() != null) {
        values.put(Constants.ORGANIZATION_WORK_PHONETIC_NAME, obj2.getOrganizationWorkPhoneticNameStyle());
      }
      if (obj2.getOrganizationWorkSymbol() != null) {
        values.put(Constants.ORGANIZATION_WORK_SYMBOL, obj2.getOrganizationWorkSymbol());
      }
      if (obj2.getOrganizationWorkTitle() != null) {
        values.put(Constants.ORGANIZATION_WORK_TITLE, obj2.getOrganizationWorkTitle());
      }
    } else if (obj1 == null && obj2 == null) { // nothing
      
    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getOrganizationOtherCompany() != null) {
        values.putNull(Constants.ORGANIZATION_OTHER_COMPANY);
      }
      if (obj1.getOrganizationOtherDepartment() != null) {
        values.putNull(Constants.ORGANIZATION_OTHER_DEPARTMENT);
      }
      if (obj1.getOrganizationOtherJobDescription() != null) {
        values.putNull(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION);
      }
      if (obj1.getOrganizationOtherOfficeLocation() != null) {
        values.putNull(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION);
      }
      if (obj1.getOrganizationOtherPhoneticName() != null) {
        values.putNull(Constants.ORGANIZATION_OTHER_PHONETIC_NAME);
      }
      if (obj1.getOrganizationOtherPhoneticNameStyle() != null) {
        values.putNull(Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE);
      }
      if (obj1.getOrganizationOtherSymbol() != null) {
        values.putNull(Constants.ORGANIZATION_OTHER_SYMBOL);
      }
      if (obj1.getOrganizationOtherTitle() != null) {
        values.putNull(Constants.ORGANIZATION_OTHER_TITLE);
      }
      if (obj1.getOrganizationWorkCompany() != null) {
        values.putNull(Constants.ORGANIZATION_WORK_COMPANY);
      }
      if (obj1.getOrganizationWorkDepartment() != null) {
        values.putNull(Constants.ORGANIZATION_WORK_DEPARTMENT);
      }
      if (obj1.getOrganizationWorkJobDescription() != null) {
        values.putNull(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION);
      }
      if (obj1.getOrganizationWorkOfficeLocation() != null) {
        values.putNull(Constants.ORGANIZATION_WORK_OFFICE_LOCATION);
      }
      if (obj1.getOrganizationWorkPhoneticName() != null) {
        values.putNull(Constants.ORGANIZATION_WORK_PHONETIC_NAME);
      }
      if (obj1.getOrganizationWorkPhoneticNameStyle() != null) {
        values.putNull(Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE);
      }
      if (obj1.getOrganizationWorkSymbol() != null) {
        values.putNull(Constants.ORGANIZATION_WORK_SYMBOL);
      }
      if (obj1.getOrganizationWorkTitle() != null) {
        values.putNull(Constants.ORGANIZATION_WORK_TITLE);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getOrganizationOtherCompany() != null) {
        values.put(Constants.ORGANIZATION_OTHER_COMPANY, obj2.getOrganizationOtherCompany());
      } else {
        values.putNull(Constants.ORGANIZATION_OTHER_COMPANY);
      }
      if (obj2.getOrganizationOtherDepartment() != null) {
        values.put(Constants.ORGANIZATION_OTHER_DEPARTMENT, obj2.getOrganizationOtherDepartment());
      } else {
        values.putNull(Constants.ORGANIZATION_OTHER_DEPARTMENT);
      }
      if (obj2.getOrganizationOtherJobDescription() != null) {
        values.put(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION, obj2.getOrganizationOtherJobDescription());
      } else {
        values.putNull(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION);
      }
      if (obj2.getOrganizationOtherOfficeLocation() != null) {
        values.put(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION, obj2.getOrganizationOtherOfficeLocation());
      } else {
        values.putNull(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION);
      }
      if (obj2.getOrganizationOtherPhoneticName() != null) {
        values.put(Constants.ORGANIZATION_OTHER_PHONETIC_NAME, obj2.getOrganizationOtherPhoneticName());
      } else {
        values.putNull(Constants.ORGANIZATION_OTHER_PHONETIC_NAME);
      }
      if (obj2.getOrganizationOtherPhoneticNameStyle() != null) {
        values.put(Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE, obj2.getOrganizationOtherPhoneticNameStyle());
      } else {
        values.putNull(Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE);
      }
      if (obj2.getOrganizationOtherSymbol() != null) {
        values.put(Constants.ORGANIZATION_OTHER_SYMBOL, obj2.getOrganizationOtherSymbol());
      } else {
        values.putNull(Constants.ORGANIZATION_OTHER_SYMBOL);
      }
      if (obj2.getOrganizationOtherTitle() != null) {
        values.put(Constants.ORGANIZATION_WORK_TITLE, obj2.getOrganizationOtherTitle());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_TITLE);
      }
      if (obj2.getOrganizationWorkCompany() != null) {
        values.put(Constants.ORGANIZATION_WORK_COMPANY, obj2.getOrganizationWorkCompany());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_COMPANY);
      }
      if (obj2.getOrganizationWorkDepartment() != null) {
        values.put(Constants.ORGANIZATION_WORK_DEPARTMENT, obj2.getOrganizationWorkDepartment());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_DEPARTMENT);
      }
      if (obj2.getOrganizationWorkJobDescription() != null) {
        values.put(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION, obj2.getOrganizationWorkJobDescription());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION);
      }
      if (obj2.getOrganizationWorkOfficeLocation() != null) {
        values.put(Constants.ORGANIZATION_WORK_OFFICE_LOCATION, obj2.getOrganizationWorkOfficeLocation());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_OFFICE_LOCATION);
      }
      if (obj2.getOrganizationWorkPhoneticName() != null) {
        values.put(Constants.ORGANIZATION_WORK_PHONETIC_NAME, obj2.getOrganizationWorkPhoneticName());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_PHONETIC_NAME);
      }
      if (obj2.getOrganizationWorkPhoneticNameStyle() != null) {
        values.put(Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE, obj2.getOrganizationWorkPhoneticNameStyle());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE);
      }
      if (obj2.getOrganizationWorkSymbol() != null) {
        values.put(Constants.ORGANIZATION_WORK_SYMBOL, obj2.getOrganizationWorkSymbol());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_SYMBOL);
      }
      if (obj2.getOrganizationWorkTitle() != null) {
        values.put(Constants.ORGANIZATION_WORK_TITLE, obj2.getOrganizationWorkTitle());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_TITLE);
      }
    }
    return values;
  }
  
  
}
