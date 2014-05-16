package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.Data;
import cz.xsendl00.synccontact.utils.Constants;

/**
 * A data kind representing an organization.
 *
 * @author portilo
 */
public class OrganizationSync extends AbstractType implements ContactInterface {

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

  public void setOrganizationWorkJobDescription(String organizationWorkJobDescription) {
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

  public void setOrganizationWorkOfficeLocation(String organizationWorkOfficeLocation) {
    this.organizationWorkOfficeLocation = organizationWorkOfficeLocation;
  }

  public String getOrganizationWorkPhoneticNameStyle() {
    return organizationWorkPhoneticNameStyle;
  }

  public void setOrganizationWorkPhoneticNameStyle(String organizationWorkPhoneticNameStyle) {
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

  public void setOrganizationOtherJobDescription(String organizationOtherJobDescription) {
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

  public void setOrganizationOtherPhoneticName(String organizationOtherPhoneticName) {
    this.organizationOtherPhoneticName = organizationOtherPhoneticName;
  }

  public String getOrganizationOtherOfficeLocation() {
    return organizationOtherOfficeLocation;
  }

  public void setOrganizationOtherOfficeLocation(String organizationOtherOfficeLocation) {
    this.organizationOtherOfficeLocation = organizationOtherOfficeLocation;
  }

  public String getOrganizationOtherPhoneticNameStyle() {
    return organizationOtherPhoneticNameStyle;
  }

  public void setOrganizationOtherPhoneticNameStyle(String organizationOtherPhoneticNameStyle) {
    this.organizationOtherPhoneticNameStyle = organizationOtherPhoneticNameStyle;
  }

  @Override
  public String toString() {
    return "Organization [organizationWorkCompany=" + organizationWorkCompany
        + ", organizationWorkTitle=" + organizationWorkTitle + ", organizationWorkDepartment="
        + organizationWorkDepartment + ", organizationWorkJobDescription="
        + organizationWorkJobDescription + ", organizationWorkSymbol=" + organizationWorkSymbol
        + ", organizationWorkPhoneticName=" + organizationWorkPhoneticName
        + ", organizationWorkOfficeLocation=" + organizationWorkOfficeLocation
        + ", organizationWorkPhoneticNameStyle=" + organizationWorkPhoneticNameStyle
        + ", organizationOtherCompany=" + organizationOtherCompany + ", organizationOtherTitle="
        + organizationOtherTitle + ", organizationOtherDepartment=" + organizationOtherDepartment
        + ", organizationOtherJobDescription=" + organizationOtherJobDescription
        + ", organizationOtherSymbol=" + organizationOtherSymbol
        + ", organizationOtherPhoneticName=" + organizationOtherPhoneticName
        + ", organizationOtherOfficeLocation=" + organizationOtherOfficeLocation
        + ", organizationOtherPhoneticNameStyle=" + organizationOtherPhoneticNameStyle + "]";
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

  public static ContentValues compare(OrganizationSync obj1, OrganizationSync obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getOrganizationOtherCompany() != null) {
        values.put(Constants.ORGANIZATION_OTHER_COMPANY, obj2.getOrganizationOtherCompany());
      }
      if (obj2.getOrganizationOtherDepartment() != null) {
        values.put(Constants.ORGANIZATION_OTHER_DEPARTMENT, obj2.getOrganizationOtherDepartment());
      }
      if (obj2.getOrganizationOtherJobDescription() != null) {
        values.put(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION,
            obj2.getOrganizationOtherJobDescription());
      }
      if (obj2.getOrganizationOtherOfficeLocation() != null) {
        values.put(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION,
            obj2.getOrganizationOtherOfficeLocation());
      }
      if (obj2.getOrganizationOtherPhoneticName() != null) {
        values.put(Constants.ORGANIZATION_OTHER_PHONETIC_NAME,
            obj2.getOrganizationOtherPhoneticName());
      }
      if (obj2.getOrganizationOtherPhoneticNameStyle() != null) {
        values.put(Constants.ORGANIZATION_OTHER_PHONETIC_NAME,
            obj2.getOrganizationOtherPhoneticNameStyle());
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
        values.put(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION,
            obj2.getOrganizationWorkJobDescription());
      }
      if (obj2.getOrganizationWorkOfficeLocation() != null) {
        values.put(Constants.ORGANIZATION_WORK_OFFICE_LOCATION,
            obj2.getOrganizationWorkOfficeLocation());
      }
      if (obj2.getOrganizationWorkPhoneticName() != null) {
        values.put(Constants.ORGANIZATION_WORK_PHONETIC_NAME,
            obj2.getOrganizationWorkPhoneticName());
      }
      if (obj2.getOrganizationWorkPhoneticNameStyle() != null) {
        values.put(Constants.ORGANIZATION_WORK_PHONETIC_NAME,
            obj2.getOrganizationWorkPhoneticNameStyle());
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
        values.put(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION,
            obj2.getOrganizationOtherJobDescription());
      } else {
        values.putNull(Constants.ORGANIZATION_OTHER_JOB_DESCRIPTION);
      }
      if (obj2.getOrganizationOtherOfficeLocation() != null) {
        values.put(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION,
            obj2.getOrganizationOtherOfficeLocation());
      } else {
        values.putNull(Constants.ORGANIZATION_OTHER_OFFICE_LOCATION);
      }
      if (obj2.getOrganizationOtherPhoneticName() != null) {
        values.put(Constants.ORGANIZATION_OTHER_PHONETIC_NAME,
            obj2.getOrganizationOtherPhoneticName());
      } else {
        values.putNull(Constants.ORGANIZATION_OTHER_PHONETIC_NAME);
      }
      if (obj2.getOrganizationOtherPhoneticNameStyle() != null) {
        values.put(Constants.ORGANIZATION_OTHER_PHONETIC_NAME_STYLE,
            obj2.getOrganizationOtherPhoneticNameStyle());
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
        values.put(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION,
            obj2.getOrganizationWorkJobDescription());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_JOB_DESCRIPTION);
      }
      if (obj2.getOrganizationWorkOfficeLocation() != null) {
        values.put(Constants.ORGANIZATION_WORK_OFFICE_LOCATION,
            obj2.getOrganizationWorkOfficeLocation());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_OFFICE_LOCATION);
      }
      if (obj2.getOrganizationWorkPhoneticName() != null) {
        values.put(Constants.ORGANIZATION_WORK_PHONETIC_NAME,
            obj2.getOrganizationWorkPhoneticName());
      } else {
        values.putNull(Constants.ORGANIZATION_WORK_PHONETIC_NAME);
      }
      if (obj2.getOrganizationWorkPhoneticNameStyle() != null) {
        values.put(Constants.ORGANIZATION_WORK_PHONETIC_NAME_STYLE,
            obj2.getOrganizationWorkPhoneticNameStyle());
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


  public static ContentProviderOperation add(int id,
      Map<String, String> values,
      int type,
      boolean create) {
    ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
    if (create) {
      operationBuilder.withValueBackReference(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE)
          .withValue(Organization.TYPE, type);
    } else {
      operationBuilder.withValue(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE)
          .withValue(Organization.TYPE, type);
    }


    Iterator<String> iter = values.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      String val = values.get(key);
      operationBuilder.withValue(key, val);
    }
    return operationBuilder.build();
  }

  public static ContentProviderOperation update(String id, Map<String, String> values, int type) {
    ContentProviderOperation.Builder operationBuilder = ContentProviderOperation.newUpdate(Data.CONTENT_URI);
    operationBuilder.withSelection(Data._ID + "=?", new String[]{id})
        .withValue(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE)
        .withValue(Organization.TYPE, type);

    Iterator<String> iter = values.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      String val = values.get(key);
      operationBuilder.withValue(key, val);
    }
    return operationBuilder.build();
  }


  public static ArrayList<ContentProviderOperation> operation(int id,
      OrganizationSync em1,
      OrganizationSync em2,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      Map<String, String> value = new HashMap<String, String>();
      if (em2.getOrganizationOtherCompany() != null) {
        value.put(Organization.DATA1, em2.getOrganizationOtherCompany());
      }
      if (em2.getOrganizationOtherDepartment() != null) {
        value.put(Organization.DATA5, em2.getOrganizationOtherDepartment());
      }
      if (em2.getOrganizationOtherJobDescription() != null) {
        value.put(Organization.DATA6, em2.getOrganizationOtherJobDescription());
      }
      if (em2.getOrganizationOtherOfficeLocation() != null) {
        value.put(Organization.DATA9, em2.getOrganizationOtherOfficeLocation());
      }
      if (em2.getOrganizationOtherPhoneticName() != null) {
        value.put(Organization.DATA8, em2.getOrganizationOtherPhoneticName());
      }
      if (em2.getOrganizationOtherPhoneticNameStyle() != null) {
        value.put(Organization.DATA10, em2.getOrganizationOtherPhoneticNameStyle());
      }
      if (em2.getOrganizationOtherSymbol() != null) {
        value.put(Organization.DATA7, em2.getOrganizationOtherSymbol());
      }
      if (em2.getOrganizationOtherTitle() != null) {
        value.put(Organization.DATA4, em2.getOrganizationOtherTitle());
      }
      if (value.size() > 0) {
        ops.add(add(id, value, Organization.TYPE_OTHER, create));
      }
      value.clear();
      if (em2.getOrganizationWorkCompany() != null) {
        value.put(Organization.DATA1, em2.getOrganizationWorkCompany());
      }
      if (em2.getOrganizationWorkDepartment() != null) {
        value.put(Organization.DATA5, em2.getOrganizationWorkDepartment());
      }
      if (em2.getOrganizationWorkJobDescription() != null) {
        value.put(Organization.DATA6, em2.getOrganizationWorkJobDescription());
      }
      if (em2.getOrganizationWorkOfficeLocation() != null) {
        value.put(Organization.DATA9, em2.getOrganizationWorkOfficeLocation());
      }
      if (em2.getOrganizationWorkPhoneticName() != null) {
        value.put(Organization.DATA8, em2.getOrganizationWorkPhoneticName());
      }
      if (em2.getOrganizationWorkPhoneticNameStyle() != null) {
        value.put(Organization.DATA10, em2.getOrganizationWorkPhoneticNameStyle());
      }
      if (em2.getOrganizationWorkSymbol() != null) {
        value.put(Organization.DATA7, em2.getOrganizationWorkSymbol());
      }
      if (em2.getOrganizationWorkTitle() != null) {
        value.put(Organization.DATA4, em2.getOrganizationWorkTitle());
      }
      if (value.size() > 0) {
        ops.add(add(id, value, Organization.TYPE_WORK, create));
      }
    } else if (em1 == null && em2 == null) { // nothing

    } else if (em1 != null && em2 == null) { // delete
      ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
          String.valueOf(Organization.TYPE_OTHER), null)));
      ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
          String.valueOf(Organization.TYPE_WORK), null)));
    } else if (em1 != null && em2 != null) { // clear or update data in db
      Map<String, String> value = new HashMap<String, String>();
      if ((em2.getOrganizationOtherCompany() != null && em1.getOrganizationOtherCompany() != null && !em2.getOrganizationOtherCompany()
          .equals(em1.getOrganizationOtherCompany()))
          || (em2.getOrganizationOtherCompany() != null && em1.getOrganizationOtherCompany() == null)
          || (em2.getOrganizationOtherCompany() == null && em1.getOrganizationOtherCompany() != null)) {
        value.put(Organization.DATA1, em2.getOrganizationOtherCompany());
      }

      if ((em2.getOrganizationOtherDepartment() != null
          && em1.getOrganizationOtherDepartment() != null && !em2.getOrganizationOtherDepartment()
          .equals(em1.getOrganizationOtherDepartment()))
          || (em2.getOrganizationOtherDepartment() != null && em1.getOrganizationOtherDepartment() == null)
          || (em2.getOrganizationOtherDepartment() == null && em1.getOrganizationOtherDepartment() != null)) {
        value.put(Organization.DATA5, em2.getOrganizationOtherDepartment());
      }

      if ((em2.getOrganizationOtherJobDescription() != null
          && em1.getOrganizationOtherJobDescription() != null && !em2.getOrganizationOtherJobDescription()
          .equals(em1.getOrganizationOtherJobDescription()))
          || (em2.getOrganizationOtherJobDescription() != null && em1.getOrganizationOtherJobDescription() == null)
          || (em2.getOrganizationOtherJobDescription() == null && em1.getOrganizationOtherJobDescription() != null)) {
        value.put(Organization.DATA6, em2.getOrganizationOtherJobDescription());
      }

      if ((em2.getOrganizationOtherOfficeLocation() != null
          && em1.getOrganizationOtherOfficeLocation() != null && !em2.getOrganizationOtherOfficeLocation()
          .equals(em1.getOrganizationOtherOfficeLocation()))
          || (em2.getOrganizationOtherOfficeLocation() != null && em1.getOrganizationOtherOfficeLocation() == null)
          || (em2.getOrganizationOtherOfficeLocation() == null && em1.getOrganizationOtherOfficeLocation() != null)) {
        value.put(Organization.DATA9, em2.getOrganizationOtherOfficeLocation());
      }

      if ((em2.getOrganizationOtherPhoneticName() != null
          && em1.getOrganizationOtherPhoneticName() != null && !em2.getOrganizationOtherPhoneticName()
          .equals(em1.getOrganizationOtherPhoneticName()))
          || (em2.getOrganizationOtherPhoneticName() != null && em1.getOrganizationOtherPhoneticName() == null)
          || (em2.getOrganizationOtherPhoneticName() == null && em1.getOrganizationOtherPhoneticName() != null)) {
        value.put(Organization.DATA8, em2.getOrganizationOtherPhoneticName());
      }

      if ((em2.getOrganizationOtherPhoneticNameStyle() != null
          && em1.getOrganizationOtherPhoneticNameStyle() != null && !em2.getOrganizationOtherPhoneticNameStyle()
          .equals(em1.getOrganizationOtherPhoneticNameStyle()))
          || (em2.getOrganizationOtherPhoneticNameStyle() != null
              && em1.getOrganizationOtherPhoneticNameStyle() == null)
          || (em2.getOrganizationOtherPhoneticNameStyle() == null
              && em1.getOrganizationOtherPhoneticNameStyle() != null)) {
        value.put(Organization.DATA10, em2.getOrganizationOtherPhoneticNameStyle());
      }

      if ((em2.getOrganizationOtherSymbol() != null && em1.getOrganizationOtherSymbol() != null
          && !em2.getOrganizationOtherSymbol().equals(em1.getOrganizationOtherSymbol()))
          || (em2.getOrganizationOtherSymbol() != null && em1.getOrganizationOtherSymbol() == null)
          || (em2.getOrganizationOtherSymbol() == null && em1.getOrganizationOtherSymbol() != null)) {
        value.put(Organization.DATA7, em2.getOrganizationOtherSymbol());
      }

      if ((em2.getOrganizationOtherTitle() != null && em1.getOrganizationOtherTitle() != null
          && !em2.getOrganizationOtherTitle().equals(em1.getOrganizationOtherTitle()))
          || (em2.getOrganizationOtherTitle() != null && em1.getOrganizationOtherTitle() == null)
          || (em2.getOrganizationOtherTitle() == null && em1.getOrganizationOtherTitle() != null)) {
        value.put(Organization.DATA4, em2.getOrganizationOtherTitle());
      }
      if (value.size() > 0) {
        ops.add(update(ID.getIdByValue(em1.getID(), String.valueOf(Organization.TYPE_OTHER), null),
            value, Organization.TYPE_OTHER));
      }
      value.clear();
      if ((em2.getOrganizationWorkCompany() != null && em1.getOrganizationWorkCompany() != null && !em2.getOrganizationWorkCompany()
          .equals(em1.getOrganizationWorkCompany()))
          || (em2.getOrganizationWorkCompany() != null && em1.getOrganizationWorkCompany() == null)
          || (em2.getOrganizationWorkCompany() == null && em1.getOrganizationWorkCompany() != null)) {
        value.put(Organization.DATA1, em2.getOrganizationWorkCompany());
      }

      if ((em2.getOrganizationWorkDepartment() != null
          && em1.getOrganizationWorkDepartment() != null && !em2.getOrganizationWorkDepartment()
          .equals(em1.getOrganizationWorkDepartment()))
          || (em2.getOrganizationWorkDepartment() != null && em1.getOrganizationWorkDepartment() == null)
          || (em2.getOrganizationWorkDepartment() == null && em1.getOrganizationWorkDepartment() != null)) {
        value.put(Organization.DATA5, em2.getOrganizationWorkDepartment());
      }

      if ((em2.getOrganizationWorkJobDescription() != null
          && em1.getOrganizationWorkJobDescription() != null && !em2.getOrganizationWorkJobDescription()
          .equals(em1.getOrganizationWorkJobDescription()))
          || (em2.getOrganizationWorkJobDescription() != null && em1.getOrganizationWorkJobDescription() == null)
          || (em2.getOrganizationWorkJobDescription() == null && em1.getOrganizationWorkJobDescription() != null)) {
        value.put(Organization.DATA6, em2.getOrganizationWorkJobDescription());
      }

      if ((em2.getOrganizationWorkOfficeLocation() != null
          && em1.getOrganizationWorkOfficeLocation() != null && !em2.getOrganizationWorkOfficeLocation()
          .equals(em1.getOrganizationWorkOfficeLocation()))
          || (em2.getOrganizationWorkOfficeLocation() != null && em1.getOrganizationWorkOfficeLocation() == null)
          || (em2.getOrganizationWorkOfficeLocation() == null && em1.getOrganizationWorkOfficeLocation() != null)) {
        value.put(Organization.DATA9, em2.getOrganizationWorkOfficeLocation());
      }

      if ((em2.getOrganizationWorkPhoneticName() != null
          && em1.getOrganizationWorkPhoneticName() != null && !em2.getOrganizationWorkPhoneticName()
          .equals(em1.getOrganizationWorkPhoneticName()))
          || (em2.getOrganizationWorkPhoneticName() != null && em1.getOrganizationWorkPhoneticName() == null)
          || (em2.getOrganizationWorkPhoneticName() == null && em1.getOrganizationWorkPhoneticName() != null)) {
        value.put(Organization.DATA8, em2.getOrganizationWorkPhoneticName());
      }

      if ((em2.getOrganizationWorkPhoneticNameStyle() != null
          && em1.getOrganizationWorkPhoneticNameStyle() != null && !em2.getOrganizationWorkPhoneticNameStyle()
          .equals(em1.getOrganizationWorkPhoneticNameStyle()))
          || (em2.getOrganizationWorkPhoneticNameStyle() != null && em1.getOrganizationWorkPhoneticNameStyle() == null)
          || (em2.getOrganizationWorkPhoneticNameStyle() == null && em1.getOrganizationWorkPhoneticNameStyle() != null)) {
        value.put(Organization.DATA10, em2.getOrganizationWorkPhoneticNameStyle());
      }

      if ((em2.getOrganizationWorkSymbol() != null && em1.getOrganizationWorkSymbol() != null && !em2.getOrganizationWorkSymbol()
          .equals(em1.getOrganizationWorkSymbol()))
          || (em2.getOrganizationWorkSymbol() != null && em1.getOrganizationWorkSymbol() == null)
          || (em2.getOrganizationWorkSymbol() == null && em1.getOrganizationWorkSymbol() != null)) {
        value.put(Organization.DATA7, em2.getOrganizationWorkSymbol());
      }

      if ((em2.getOrganizationWorkTitle() != null && em1.getOrganizationWorkTitle() != null && !em2.getOrganizationWorkTitle()
          .equals(em1.getOrganizationWorkTitle()))
          || (em2.getOrganizationWorkTitle() != null && em1.getOrganizationWorkTitle() == null)
          || (em2.getOrganizationWorkTitle() == null && em1.getOrganizationWorkTitle() != null)) {
        value.put(Organization.DATA4, em2.getOrganizationWorkTitle());
      }
      if (value.size() > 0) {
        ops.add(update(ID.getIdByValue(em1.getID(), String.valueOf(Organization.TYPE_WORK), null),
            value, Organization.TYPE_WORK));
      }
    }
    return ops.size() > 0 ? ops : null;
  }


}
