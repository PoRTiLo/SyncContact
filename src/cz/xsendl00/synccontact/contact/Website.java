package cz.xsendl00.synccontact.contact;

import cz.xsendl00.synccontact.utils.Constants;

public class Website implements ContactInterface {

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
}
