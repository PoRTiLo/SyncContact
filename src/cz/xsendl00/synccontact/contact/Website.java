package cz.xsendl00.synccontact.contact;

import android.content.ContentValues;
import cz.xsendl00.synccontact.utils.Constants;

public class Website extends AbstractType implements ContactInterface {

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
  
  public static ContentValues compare(Website obj1, Website obj2) {
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
}
