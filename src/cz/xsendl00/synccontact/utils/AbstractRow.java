/*
 * Copyright (C) 2014 by xsendl00.*/
package cz.xsendl00.synccontact.utils;

import java.util.UUID;


public class AbstractRow {

  protected String name;
  protected Integer id;
  protected String uuid;
  protected Boolean sync;
  protected Integer idTable;
  protected String lastSyncTime;
  protected boolean isConverted;
  protected boolean deleted;



  /**
   * @return Returns the deleted.
   */
  public boolean isDeleted() {
    return deleted;
  }




  /**
   * @param deleted The deleted to set.
   */
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }



  /**
   * @return Returns the isConverted.
   */
  public boolean isConverted() {
    return isConverted;
  }



  /**
   * @param isConverted The isConverted to set.
   */
  public void setConverted(boolean isConverted) {
    this.isConverted = isConverted;
  }


  /**
   * @return Returns the lastSyncTime.
   */
  public String getLastSyncTime() {
    return lastSyncTime;
  }


  /**
   * @param lastSyncTime The lastSyncTime to set.
   */
  public void setLastSyncTime(String lastSyncTime) {
    this.lastSyncTime = lastSyncTime;
  }

  public AbstractRow(Integer id, String name, Boolean sync, Integer idTable, String uuid) {
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


  public String getUuidFirst() {
    return this.uuid;
  }
  /**
   * Get UUID.
   *
   * @return UUID.
   */
  public String getUuid() {
    if (this.uuid == null) {
      this.uuid = generateUUID();
    }
    return this.uuid;
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

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String toStringSync() {
    return "Id: " + id + ", sync: " + sync;

  }
}
