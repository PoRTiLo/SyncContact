/*
 * Copyright (C) 2014 by xsendl00.*/
package cz.xsendl00.synccontact.utils;

import java.util.UUID;


public class AbstractRow {

  protected String name;
  protected String id;
  protected String uuid;
  protected Boolean sync;
  protected Integer idTable;

  public AbstractRow(String id, String name, Boolean sync, Integer idTable, String uuid) {
    this.id = id;
    this.name = name;
    this.sync = sync;
    this.idTable = idTable;
    this.uuid = uuid;
  }

  public Boolean isSync() {
    return sync;
  }

  public void setSync(Boolean sync) {
    this.sync = sync;
  }

  public Integer getIdTable() {
    return idTable;
  }

  public void setIdTable(Integer idTable) {
    this.idTable = idTable;
  }
  /**
   * Generate UUID.
   *
   * @return UUID.toString
   */
  public static String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  /**
   * Get UUID.
   *
   * @return UUID.
   */
  public String getUuid() {
    if (this.uuid == null) {
      uuid = generateUUID();
    }
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

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

  public String toStringSync() {
    return "Id: " + id + ", sync: " + sync;

  }
}
