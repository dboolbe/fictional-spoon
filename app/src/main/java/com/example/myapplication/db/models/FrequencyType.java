package com.example.myapplication.db.models;

import android.content.ContentValues;
import android.database.Cursor;

public class FrequencyType extends BaseModel {

    public static class Entry extends BaseModel.Entry {
        public static String TABLE_NAME = "frequency_type";
        public static String COL_MONTHLY_RATIO = "monthly_ratio";
        public static String COL_YEARLY_RATIO = "yearly_ratio";
        public static String COL_RESOURCE_ID = "resource_id";
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Entry.TABLE_NAME + "( \n" +
                    Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    Entry.COL_NAME + " TEXT  NOT NULL, \n" +
                    Entry.COL_MONTHLY_RATIO + " REAL  NOT NULL, \n" +
                    Entry.COL_YEARLY_RATIO + " REAL  NOT NULL, \n" +
                    Entry.COL_RESOURCE_ID + " TEXT  NOT NULL)";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;

    private Double monthlyRatio;
    private Double yearlyRatio;
    private String resourceId;

    public FrequencyType(String name, Double monthlyRatio, Double yearlyRatio, String resourceId) {
        this.name = name;
        this.monthlyRatio = monthlyRatio;
        this.yearlyRatio = yearlyRatio;
        this.resourceId = resourceId;
    }

    public FrequencyType(Integer id, String name, Double monthlyRatio, Double yearlyRatio,
                         String resourceId) {
        this.id = id;
        this.name = name;
        this.monthlyRatio = monthlyRatio;
        this.yearlyRatio = yearlyRatio;
        this.resourceId = resourceId;
    }

    public Double getMonthlyRatio() {
        return monthlyRatio;
    }

    public void setMonthlyRatio(Double monthlyRatio) {
        this.monthlyRatio = monthlyRatio;
    }

    public Double getYearlyRatio() {
        return yearlyRatio;
    }

    public void setYearlyRatio(Double yearlyRatio) {
        this.yearlyRatio = yearlyRatio;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public static String[] getColumns() {
        return new String[] { Entry._ID, Entry.COL_NAME, Entry.COL_MONTHLY_RATIO,
                Entry.COL_YEARLY_RATIO, Entry.COL_RESOURCE_ID };
    }

    public static <T extends BaseModel> BaseModel processResults(Cursor cursor) {
        return new FrequencyType(cursor.getInt(0), cursor.getString(1),
                cursor.getDouble(2), cursor.getDouble(3),
                cursor.getString(4));
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
        contentValues.put(Entry.COL_MONTHLY_RATIO, monthlyRatio);
        contentValues.put(Entry.COL_YEARLY_RATIO, yearlyRatio);
        contentValues.put(Entry.COL_RESOURCE_ID, resourceId);
        return contentValues;
    }

    public static String getTableName() {
        return Entry.TABLE_NAME;
    }

    @Override
    public String toString() {
        return "FrequencyType{" +
                "id=" + id + ", " +
                "name='" + name + "', " +
                "monthlyRatio=" + monthlyRatio + ", " +
                "yearlyRatio=" + yearlyRatio + ", " +
                "resourceId='" + resourceId + "'}";
    }
}
