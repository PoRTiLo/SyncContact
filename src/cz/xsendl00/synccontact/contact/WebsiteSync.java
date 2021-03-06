package cz.xsendl00.synccontact.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Data;
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
    return "Website [websiteHomepage=" + websiteHomepage + ", websiteBlog=" + websiteBlog
        + ", websiteProfile=" + websiteProfile + ", websiteHome=" + websiteHome + ", websiteWork="
        + websiteWork + ", websiteFtp=" + websiteFtp + ", websiteOther=" + websiteOther + "]";
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
   * @param id raw_contact id
   * @param value name of protocol
   * @param protocol like {@link Website.PROTOCOl_AIM}
   * @param type like Website.TYPE_HOME
   * @return
   */
  public static ContentProviderOperation add(int id, String value, int type, boolean create) {
    if (create) {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValueBackReference(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE)
          .withValue(Website.TYPE, type)
          .withValue(Website.DATA, value)
          .build();
    } else {
      return ContentProviderOperation.newInsert(Data.CONTENT_URI)
          .withValue(Data.RAW_CONTACT_ID, id)
          .withValue(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE)
          .withValue(Website.TYPE, type)
          .withValue(Website.DATA, value)
          .build();
    }
  }

  public static ContentProviderOperation update(String id, String value, int type) {
    return ContentProviderOperation.newUpdate(Data.CONTENT_URI)
        .withSelection(Data._ID + "=?", new String[]{String.valueOf(id)})
        .withValue(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE)
        .withValue(Website.DATA, value)
        .build();
  }


  public static ArrayList<ContentProviderOperation> operation(int id,
      WebsiteSync em1,
      WebsiteSync em2,
      boolean create) {
    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    if (em1 == null && em2 != null) { // create new from LDAP and insert to db
      if (em2.getWebsiteBlog() != null) {
        ops.add(add(id, em2.getWebsiteBlog(), Website.TYPE_BLOG, create));
      }
      if (em2.getWebsiteFtp() != null) {
        ops.add(add(id, em2.getWebsiteFtp(), Website.TYPE_FTP, create));
      }
      if (em2.getWebsiteHome() != null) {
        ops.add(add(id, em2.getWebsiteHome(), Website.TYPE_HOME, create));
      }
      if (em2.getWebsiteOther() != null) {
        ops.add(add(id, em2.getWebsiteOther(), Website.TYPE_OTHER, create));
      }
      if (em2.getWebsiteHomepage() != null) {
        ops.add(add(id, em2.getWebsiteHomepage(), Website.TYPE_HOMEPAGE, create));
      }
      if (em2.getWebsiteProfile() != null) {
        ops.add(add(id, em2.getWebsiteProfile(), Website.TYPE_PROFILE, create));
      }
      if (em2.getWebsiteWork() != null) {
        ops.add(add(id, em2.getWebsiteWork(), Website.TYPE_WORK, create));
      }
    } else if (em1 == null && em2 == null) { // nothing

    } else if (em1 != null && em2 == null) { // delete
      if (em1.getWebsiteBlog() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_BLOG), null)));
      }
      if (em1.getWebsiteFtp() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_FTP),
            null)));
      }
      if (em1.getWebsiteHome() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_HOMEPAGE), null)));
      }
      if (em1.getWebsiteOther() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_OTHER), null)));
      }
      if (em1.getWebsiteHomepage() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_HOMEPAGE), null)));
      }
      if (em1.getWebsiteProfile() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_PROFILE), null)));
      }
      if (em1.getWebsiteWork() != null) {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_WORK), null)));
      }

    } else if (em1 != null && em2 != null) { // clear or update data in db
      if (em2.getWebsiteBlog() != null) {
        ops.add(add(id, em2.getWebsiteBlog(), Website.TYPE_BLOG, create));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_BLOG), null)));
      }
      if (em2.getWebsiteFtp() != null) {
        ops.add(add(id, em2.getWebsiteFtp(), Website.TYPE_FTP, create));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(), String.valueOf(Website.TYPE_FTP),
            null)));
      }
      if (em2.getWebsiteHome() != null) {
        ops.add(add(id, em2.getWebsiteHome(), Website.TYPE_HOME, create));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_HOMEPAGE), null)));
      }
      if (em2.getWebsiteOther() != null) {
        ops.add(add(id, em2.getWebsiteOther(), Website.TYPE_OTHER, create));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_OTHER), null)));
      }
      if (em2.getWebsiteHomepage() != null) {
        ops.add(add(id, em2.getWebsiteHomepage(), Website.TYPE_HOMEPAGE, create));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_HOMEPAGE), null)));
      }
      if (em2.getWebsiteProfile() != null) {
        ops.add(add(id, em2.getWebsiteProfile(), Website.TYPE_PROFILE, create));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_PROFILE), null)));
      }
      if (em2.getWebsiteWork() != null) {
        ops.add(add(id, em2.getWebsiteWork(), Website.TYPE_WORK, create));
      } else {
        ops.add(GoogleContact.delete(ID.getIdByValue(em1.getID(),
            String.valueOf(Website.TYPE_WORK), null)));
      }
    }
    return ops.size() > 0 ? ops : null;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((websiteBlog == null) ? 0 : websiteBlog.hashCode());
    result = prime * result + ((websiteFtp == null) ? 0 : websiteFtp.hashCode());
    result = prime * result + ((websiteHome == null) ? 0 : websiteHome.hashCode());
    result = prime * result + ((websiteHomepage == null) ? 0 : websiteHomepage.hashCode());
    result = prime * result + ((websiteOther == null) ? 0 : websiteOther.hashCode());
    result = prime * result + ((websiteProfile == null) ? 0 : websiteProfile.hashCode());
    result = prime * result + ((websiteWork == null) ? 0 : websiteWork.hashCode());
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    WebsiteSync other = (WebsiteSync) obj;
    if (websiteBlog == null) {
      if (other.websiteBlog != null) {
        return false;
      }
    } else if (!websiteBlog.equals(other.websiteBlog)) {
      return false;
    }
    if (websiteFtp == null) {
      if (other.websiteFtp != null) {
        return false;
      }
    } else if (!websiteFtp.equals(other.websiteFtp)) {
      return false;
    }
    if (websiteHome == null) {
      if (other.websiteHome != null) {
        return false;
      }
    } else if (!websiteHome.equals(other.websiteHome)) {
      return false;
    }
    if (websiteHomepage == null) {
      if (other.websiteHomepage != null) {
        return false;
      }
    } else if (!websiteHomepage.equals(other.websiteHomepage)) {
      return false;
    }
    if (websiteOther == null) {
      if (other.websiteOther != null) {
        return false;
      }
    } else if (!websiteOther.equals(other.websiteOther)) {
      return false;
    }
    if (websiteProfile == null) {
      if (other.websiteProfile != null) {
        return false;
      }
    } else if (!websiteProfile.equals(other.websiteProfile)) {
      return false;
    }
    if (websiteWork == null) {
      if (other.websiteWork != null) {
        return false;
      }
    } else if (!websiteWork.equals(other.websiteWork)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean isNull() {
    return websiteHomepage == null && websiteBlog == null && websiteProfile == null && websiteHome == null
        && websiteWork == null && websiteFtp == null && websiteOther == null;
  }



}
