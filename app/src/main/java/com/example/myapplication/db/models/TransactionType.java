package com.example.myapplication.db.models;

import android.content.ContentValues;
import android.database.Cursor;

public class TransactionType extends BaseModel {

    public static class Entry extends BaseModel.Entry {
        public static String TABLE_NAME = "transaction_type";
        public static String COL_RESOURCE_ID = "resource_id";
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Entry.TABLE_NAME + "( \n" +
                    Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    Entry.COL_NAME + " TEXT  NOT NULL, \n" +
                    Entry.COL_RESOURCE_ID + " TEXT  NOT NULL)";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;

    private String resourceId;

    public TransactionType(String name, String resourceId) {
        this.name = name;
        this.resourceId = resourceId;
    }

    public TransactionType(Integer id, String name, String resourceId) {
        this.id = id;
        this.name = name;
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public static String[] getColumns() {
        return new String[] { Entry._ID, Entry.COL_NAME, Entry.COL_RESOURCE_ID };
    }

    public static <T extends BaseModel> BaseModel processResults(Cursor cursor) {
        return new TransactionType(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
    }

    @Override
    public String getDisplayText() {
        return name;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Entry._ID, id);
        contentValues.put(Entry.COL_NAME, name);
        contentValues.put(Entry.COL_RESOURCE_ID, resourceId);
        return contentValues;
    }

    public static String getTableName() {
        return Entry.TABLE_NAME;
    }

    @Override
    public String toString() {
        return "TransactionType{" +
                "id=" + id + ", " +
                "name='" + name + "', " +
                "resourceId='" + resourceId + "'}";
    }
}
