package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing an organization.
 * @author portilo
 *
 */
public class Organization  implements ContactInterface {

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
}
