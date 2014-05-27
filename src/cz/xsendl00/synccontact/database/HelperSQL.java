/*
 * Copyright (C) 2014 by xsendl00.*/
package cz.xsendl00.synccontact.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * CRUD operations with timestamp. Timestamp is  in database.
 */
public class HelperSQL extends SQLiteOpenHelper {

  private static final String TAG = "HelperSQL";

  private static final String DATABASE_NAME = "SyncContact";
  private static final int DATABASE_VERSION = 1;
  private static final String TABLE_NAME = "timestamp";
  private static final String ID = "id_";
  private static final String TIMESTAMP = "timestamp_";
  private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TIMESTAMP + " TEXT ) ; ";


  public HelperSQL(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  /* Create table */
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE);
    // Create tables again
    onCreate(db);
  }


  public void updateTimestamp(String timestamp) {
    String time = newerTimestamp();
    SQLiteDatabase db = this.getWritableDatabase();
    if (time == null) { // add new
      ContentValues values = new ContentValues();
      values.put(TIMESTAMP, timestamp);
      int id = (int) db.insert(TABLE_NAME, null, values);
    } else { // update
      ContentValues values = new ContentValues();
      values.put(TIMESTAMP, timestamp);
      int res = db.update(TABLE_NAME, values, ID + " = ?", new String[]{"1"});
    }

    if (db != null && db.isOpen()) {
      db.close();
    }
  }

  /**
   * Newer timestamp of last synchronization. Get from database.
   * @return last timestamp.
   */
  public String newerTimestamp() {
    String str = null;
    String selectQuery = "SELECT " + TIMESTAMP + " FROM " + TABLE_NAME;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToNext()) {
      str = cursor.getString(0);
    }
    if (cursor != null && !cursor.isClosed()) {
      cursor.close();
    }
    if (db != null && db.isOpen()) {
      db.close();
    }

    return str;
  }
}
