package cz.xsendl00.synccontact.contact;

import java.util.List;

import android.util.Log;

public class ID {


  private static final String TAG = "ID";

  private String id;
  private String type;
  private String category;

  /**
   * @param type type, als TYPE_HOME, ....
   * @param cat als protocol PROTOCOL_AIM ...
   * @param id id from db
   */
  public ID(String type, String cat, String id) {
    this.type = type;
    this.category = cat;
    this.setId(id);
  }

  @Override
  public String toString() {
    return "ID [id=" + id + ", type=" + type + ", category=" + category + "]";
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * @param list array of IDs
   * @param type like TYPE_HOME
   * @param cat like PROTOCOL_AIM
   * @return
   */
  public static String getIdByValue(List<ID> list, String type, String cat) {
    if (list != null && list.size() > 0) {
      for (ID id : list) {
        Log.i(TAG, id.toString() + "vstup:" + type + ", " + cat);
        if (id.getType() != null
            && id.getType().equals(type)
            && ((id.getCategory() == null && cat == null) || (id.getCategory() != null
                && cat != null && id.equals(cat)))) {
          Log.i(TAG, "return:" + id.getId());
          return id.getId();
        }
      }
    }
    Log.i(TAG, "return: null");
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    ID other = (ID) obj;
    if (category == null) {
      if (other.category != null) {
        return false;
      }
    } else if (!category.equals(other.category)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      return false;
    }
    return true;
  }


}
