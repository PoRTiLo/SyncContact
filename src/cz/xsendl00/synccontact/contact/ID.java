package cz.xsendl00.synccontact.contact;

import java.util.List;

public class ID {
  private String id;
  private String type;
  private String category;
  
  public ID(String type, String cat, String id) {
    this.type = type;
    this.category = cat;
    this.setId(id);
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
  
  public static String getIdByValue(List<ID> list, String type, String cat) {
    if (list != null && list.size() > 0) {
      for (ID id : list) {
        if (id.getType().equals(type) && id.getCategory().equals(cat)) {
          return id.getId();
        }
      }
    }
    return null;
  }
}