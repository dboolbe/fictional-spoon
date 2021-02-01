package com.example.myapplication.db.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

public abstract class BaseModel {

    static public class Entry implements BaseColumns {
        public static String COL_NAME = "name";

        // aggregated fields
        public static String COL_MONTHLY_AMOUNT = "monthly_amount";
        public static String COL_YEARLY_AMOUNT = "yearly_amount";
        public static String COL_MONTHLY_EXPENSE_AMOUNT = "monthly_expense_amount";
        public static String COL_MONTHLY_INCOME_AMOUNT = "monthly_income_amount";
        public static String COL_YEARLY_EXPENSE_AMOUNT = "yearly_expense_amount";
        public static String COL_YEARLY_INCOME_AMOUNT = "yearly_income_amount";
    }

    protected Integer id;
    protected String name;

    // aggregated fields
    protected Double monthlyAmount = 0.0;
    protected Double yearlyAmount = 0.0;
    protected Double monthlyExpenseAmount = 0.0;
    protected Double monthlyIncomeAmount = 0.0;
    protected Double yearlyExpenseAmount = 0.0;
    protected Double yearlyIncomeAmount = 0.0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMonthlyAmount() {
        return monthlyAmount;
    }

    public void setMonthlyAmount(Double monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }

    public Double getYearlyAmount() {
        return yearlyAmount;
    }

    public void setYearlyAmount(Double yearlyAmount) {
        this.yearlyAmount = yearlyAmount;
    }

    public Double getMonthlyExpenseAmount() {
        return monthlyExpenseAmount;
    }

    public void setMonthlyExpenseAmount(Double monthlyExpenseAmount) {
        this.monthlyExpenseAmount = monthlyExpenseAmount;
    }

    public Double getMonthlyIncomeAmount() {
        return monthlyIncomeAmount;
    }

    public void setMonthlyIncomeAmount(Double monthlyIncomeAmount) {
        this.monthlyIncomeAmount = monthlyIncomeAmount;
    }

    public Double getYearlyExpenseAmount() {
        return yearlyExpenseAmount;
    }

    public void setYearlyExpenseAmount(Double yearlyExpenseAmount) {
        this.yearlyExpenseAmount = yearlyExpenseAmount;
    }

    public Double getYearlyIncomeAmount() {
        return yearlyIncomeAmount;
    }

    public void setYearlyIncomeAmount(Double yearlyIncomeAmount) {
        this.yearlyIncomeAmount = yearlyIncomeAmount;
    }

    abstract public ContentValues getContentValues();

    static public String getTableName() {
        return null;
    }

    static public String[] getColumns() {
        return new String[] { Entry._ID };
    }

    static public <T extends BaseModel> BaseModel processResults(Cursor cursor) {
        return null;
    };

    static protected  <T extends BaseModel> BaseModel processResults(Cursor cursor, BaseModel baseModel) {

        // aggregated fields
        if(cursor.getColumnIndex(Entry.COL_MONTHLY_AMOUNT) >= 0) baseModel.setMonthlyAmount(cursor.getDouble(cursor.getColumnIndex(Entry.COL_MONTHLY_AMOUNT)));
        if(cursor.getColumnIndex(Entry.COL_YEARLY_AMOUNT) >= 0) baseModel.setYearlyAmount(cursor.getDouble(cursor.getColumnIndex(Entry.COL_YEARLY_AMOUNT)));
        if(cursor.getColumnIndex(Entry.COL_MONTHLY_EXPENSE_AMOUNT) >= 0) baseModel.setMonthlyExpenseAmount(cursor.getDouble(cursor.getColumnIndex(Entry.COL_MONTHLY_EXPENSE_AMOUNT)));
        if(cursor.getColumnIndex(Entry.COL_MONTHLY_INCOME_AMOUNT) >= 0) baseModel.setMonthlyIncomeAmount(cursor.getDouble(cursor.getColumnIndex(Entry.COL_MONTHLY_INCOME_AMOUNT)));
        if(cursor.getColumnIndex(Entry.COL_YEARLY_EXPENSE_AMOUNT) >= 0) baseModel.setYearlyExpenseAmount(cursor.getDouble(cursor.getColumnIndex(Entry.COL_YEARLY_EXPENSE_AMOUNT)));
        if(cursor.getColumnIndex(Entry.COL_YEARLY_INCOME_AMOUNT) >= 0) baseModel.setYearlyIncomeAmount(cursor.getDouble(cursor.getColumnIndex(Entry.COL_YEARLY_INCOME_AMOUNT)));

        return baseModel;
    };

    abstract public String getDisplayText();
}
