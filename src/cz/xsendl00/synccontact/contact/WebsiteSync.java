package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Website;
import cz.xsendl00.synccontact.utils.Constants;

public class WebsiteSync extends AbstractType implements ContactInterface {

  private String websiteHomepage;
  private String websiteBlog;
  private String websiteProfile;
  private String websiteHome;
  private String websiteWork;
  private String websiteFtp;
  private String websiteOther;
  
  public String getWebsiteHomepage() {
    return websiteHomepage;
  }
  public void setWebsiteHomepage(String websiteHomepage) {
    this.websiteHomepage = websiteHomepage;
  }
  public String getWebsiteBlog() {
    return websiteBlog;
  }
  public void setWebsiteBlog(String websiteBlog) {
    this.websiteBlog = websiteBlog;
  }
  public String getWebsiteProfile() {
    return websiteProfile;
  }
  public void setWebsiteProfile(String websiteProfile) {
    this.websiteProfile = websiteProfile;
  }
  public String getWebsiteHome() {
    return websiteHome;
  }
  public void setWebsiteHome(String websiteHome) {
    this.websiteHome = websiteHome;
  }
  public String getWebsiteWork() {
    return websiteWork;
  }
  public void setWebsiteWork(String websiteWork) {
    this.websiteWork = websiteWork;
  }
  public String getWebsiteFtp() {
    return websiteFtp;
  }
  public void setWebsiteFtp(String websiteFtp) {
    this.websiteFtp = websiteFtp;
  }
  public String getWebsiteOther() {
    return websiteOther;
  }
  public void setWebsiteOther(String websiteOther) {
    this.websiteOther = websiteOther;
  }

  @Override
  public String toString() {
    return "Website [websiteHomepage=" + websiteHomepage + ", websiteBlog="
        + websiteBlog + ", websiteProfile=" + websiteProfile + ", websiteHome="
        + websiteHome + ", websiteWork=" + websiteWork + ", websiteFtp="
        + websiteFtp + ", websiteOther=" + websiteOther + "]";
  }
  @Override
  public void defaultValue() {
    websiteHomepage = Constants.WEBSITE_HOMEPAGE;
    websiteBlog = Constants.WEBSITE_BLOG;
    websiteProfile = Constants.WEBSITE_PROFILE;
    websiteHome = Constants.WEBSITE_HOME;
    websiteWork = Constants.WEBSITE_WORK;
    websiteFtp = Constants.WEBSITE_FTP;
    websiteOther = Constants.WEBSITE_OTHER;
  }
  
  public static ContentValues compare(WebsiteSync obj1, WebsiteSync obj2) {
    ContentValues values = new ContentValues();
    if (obj1 == null && obj2 != null) { // update from LDAP
      if (obj2.getWebsiteBlog() != null) {
        values.put(Constants.WEBSITE_BLOG, obj2.getWebsiteBlog());
      }
      if (obj2.getWebsiteFtp() != null) {
        values.put(Constants.WEBSITE_FTP, obj2.getWebsiteFtp());
      }
      if (obj2.getWebsiteHome() != null) {
        values.put(Constants.WEBSITE_HOME, obj2.getWebsiteHome());
      }
      if (obj2.getWebsiteHomepage() != null) {
        values.put(Constants.WEBSITE_HOMEPAGE, obj2.getWebsiteHomepage());
      }
      if (obj2.getWebsiteOther() != null) {
        values.put(Constants.WEBSITE_OTHER, obj2.getWebsiteOther());
      }
      if (obj2.getWebsiteProfile() != null) {
        values.put(Constants.WEBSITE_PROFILE, obj2.getWebsiteProfile());
      }
      if (obj2.getWebsiteWork() != null) {
        values.put(Constants.WEBSITE_WORK, obj2.getWebsiteWork());
      }
    } else if (obj1 == null && obj2 == null) { // nothing
      
    } else if (obj1 != null && obj2 == null) { // clear data in db
      if (obj1.getWebsiteBlog() != null) {
        values.putNull(Constants.WEBSITE_BLOG);
      }
      if (obj1.getWebsiteFtp() != null) {
        values.putNull(Constants.WEBSITE_FTP);
      }
      if (obj1.getWebsiteHome() != null) {
        values.putNull(Constants.WEBSITE_HOME);
      }
      if (obj1.getWebsiteHomepage() != null) {
        values.putNull(Constants.WEBSITE_HOMEPAGE);
      }
      if (obj1.getWebsiteOther() != null) {
        values.putNull(Constants.WEBSITE_OTHER);
      }
      if (obj1.getWebsiteProfile() != null) {
        values.putNull(Constants.WEBSITE_PROFILE);
      }
      if (obj1.getWebsiteWork() != null) {
        values.putNull(Constants.WEBSITE_WORK);
      }
    } else if (obj1 != null && obj2 != null) { // merge
      if (obj2.getWebsiteBlog() != null) {
        values.put(Constants.WEBSITE_BLOG, obj2.getWebsiteBlog());
      } else {
        values.putNull(Constants.WEBSITE_BLOG);
      }
      if (obj2.getWebsiteFtp() != null) {
        values.put(Constants.WEBSITE_FTP, obj2.getWebsiteFtp());
      } else {
        values.putNull(Constants.WEBSITE_FTP);
      }
      if (obj2.getWebsiteHome() != null) {
        values.put(Constants.WEBSITE_HOME, obj2.getWebsiteHome());
      } else {
        values.putNull(Constants.WEBSITE_HOME);
      }
      if (obj2.getWebsiteHomepage() != null) {
        values.put(Constants.WEBSITE_HOMEPAGE, obj2.getWebsiteHomepage());
      } else {
        values.putNull(Constants.WEBSITE_HOMEPAGE);
      }
      if (obj2.getWebsiteOther() != null) {
        values.put(Constants.WEBSITE_OTHER, obj2.getWebsiteOther());
      } else {
        values.putNull(Constants.WEBSITE_OTHER);
      }
      if (obj2.getWebsiteProfile() != null) {
        values.put(Constants.WEBSITE_PROFILE, obj2.getWebsiteProfile());
      } else {
        values.putNull(Constants.WEBSITE_WORK);
      }
      if (obj2.getWebsiteWork() != null) {
        values.put(Constants.WEBSITE_WORK, obj2.getWebsiteWork());
      } else {
        values.putNull(Constants.WEBSITE_WORK);
      }
    }
    return values;
  }
  
  /**
   * 
   * @param id        raw_contact id
   * @param value     name of protocol
   * @param protocol  like {@link Website.PROTOCOl_AIM}
   * @param type      like Website.TYPE_HOME
   * @return
   */
  public static ContentProviderOperation add(String id, String value, int type) {
    return ContentProviderOperation.newInsert(Data.CONTENT_URI)
    .withValue(Data.RAW_CONTACT_ID, id)
    .withValue(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE)
    .withValue(Website.TYPE, type)
    .withValue(Website.DATA, value)
    .build();
  }
  
  public static ContentProviderOperation update(String id, String value, int type) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{String.valueOf(id)})
        .withValue(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE)
        .withValue(Website.DATA, value)
        .build();
  }
  

  public static ArrayList<ContentProviderOperation> operation(String id, WebsiteSync em1, WebsiteSync em2) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      if (em2.getWebsiteBlog() != null) {
        ops.add(add(id, em2.getWebsiteBlog(), Website.TYPE_BLOG));
      }
      if (em2.getWebsiteFtp() != null) {
        ops.add(add(id, em2.getWebsiteFtp(), Website.TYPE_FTP));
      }
      if (em2.getWebsiteHome() != null) {
        ops.add(add(id, em2.getWebsiteHome(), Website.TYPE_HOME));
      }
      if (em2.getWebsiteOther() != null) {
        ops.add(add(id, em2.getWebsiteOther(), Website.TYPE_OTHER));
      }
      if (em2.getWebsiteHomepage() != null) {
        ops.add(add(id, em2.getWebsiteHomepage(), Website.TYPE_HOMEPAGE));
      }
      if (em2.getWebsiteProfile() != null) {
        ops.add(add(id, em2.getWebsiteProfile(), Website.TYPE_PROFILE));
      }
      if (em2.getWebsiteWork() != null) {
        ops.add(add(id, em2.getWebsiteWork(), Website.TYPE_WORK));
      }
    } else if (em1 == null && em2 == null) { // nothing
      
    } else if (em1 != null && em2 == null) { // delete
      if (em1.getWebsiteBlog() != null ) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_BLOG), null)));
      }
      if (em1.getWebsiteFtp() != null ) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_FTP), null)));
      }
      if (em1.getWebsiteHome() != null ) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_HOMEPAGE), null)));
      }
      if (em1.getWebsiteOther() != null ) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_OTHER), null)));
      }
      if (em1.getWebsiteHomepage() != null ) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_HOMEPAGE), null)));
      }
      if (em1.getWebsiteProfile() != null ) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_PROFILE), null)));
      }
      if (em1.getWebsiteWork() != null ) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_WORK), null)));
      }
      
    } else if (em1 != null && em2 != null) { // clear or update data in db
      if (em2.getWebsiteBlog() != null) {
        ops.add(add(id, em2.getWebsiteBlog(), Website.TYPE_BLOG));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_BLOG), null)));
      }
      if (em2.getWebsiteFtp() != null) {
        ops.add(add(id, em2.getWebsiteFtp(), Website.TYPE_FTP));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_FTP), null)));
      }
      if (em2.getWebsiteHome() != null) {
        ops.add(add(id, em2.getWebsiteHome(), Website.TYPE_HOME));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_HOMEPAGE), null)));
      }
      if (em2.getWebsiteOther() != null) {
        ops.add(add(id, em2.getWebsiteOther(), Website.TYPE_OTHER));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_OTHER), null)));
      }
      if (em2.getWebsiteHomepage() != null) {
        ops.add(add(id, em2.getWebsiteHomepage(), Website.TYPE_HOMEPAGE));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_HOMEPAGE), null)));
      }
      if (em2.getWebsiteProfile() != null) {
        ops.add(add(id, em2.getWebsiteProfile(), Website.TYPE_PROFILE));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_PROFILE), null)));
      }
      if (em2.getWebsiteWork() != null) {
        ops.add(add(id, em2.getWebsiteWork(), Website.TYPE_WORK));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_WORK), null)));
      }
    }
    return ops.size() > 0 ? ops : null;
  }
}
