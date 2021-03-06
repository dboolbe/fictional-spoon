package com.example.myapplication.db.models;

import android.content.ContentValues;
import android.database.Cursor;

public class Account extends BaseModel {

    public static class Entry extends BaseModel.Entry {
        public static String TABLE_NAME = "account";
        public static String COL_DESCRIPTION = "description";
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Entry.TABLE_NAME + "( \n" +
                    Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    Entry.COL_NAME + " TEXT  NOT NULL, \n" +
                    Entry.COL_DESCRIPTION + " TEXT  NOT NULL)";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;

    private String description;

    public Account(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Account(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static String[] getColumns() {
        return new String[] { Entry._ID, Entry.COL_NAME, Entry.COL_DESCRIPTION };
    }

    public static <T extends BaseModel> BaseModel processResults(Cursor cursor) {
        Account account = new Account(cursor.getInt(cursor.getColumnIndex(Entry._ID)),
                cursor.getString(cursor.getColumnIndex(Entry.COL_NAME)),
                cursor.getString(cursor.getColumnIndex(Entry.COL_DESCRIPTION)));

        return processResults(cursor, account);
    }

    public static String SQL_SELECT_ALL_TABLE() {
        String inner_alias = "mt";
        String outer_alias = "mt1";

        return "SELECT tt.*, " +
                "(tt.monthly_income_amount + tt.monthly_expense_amount) AS monthly_difference, " +
                "(tt.yearly_income_amount + tt.yearly_expense_amount) AS yearly_difference, " +
                "(tt.monthly_expense_amount / (CASE WHEN tt.monthly_income_amount = 0 THEN 1 ELSE tt.monthly_income_amount END)) AS monthly_expense_ratio, " +
                "(tt.yearly_expense_amount / (CASE WHEN tt.yearly_income_amount = 0 THEN 1 ELSE tt.yearly_income_amount END)) AS yearly_expense_ratio " +
                "FROM (SELECT gt._id, " +
                "gt.name, " +
                "gt.description, " +
                "SUM(gt.monthly_expense_amount) AS monthly_expense_amount, " +
                "SUM(gt.monthly_income_amount) AS monthly_income_amount, " +
                "SUM(gt.yearly_expense_amount) AS yearly_expense_amount, " +
                "SUM(gt.yearly_income_amount) AS yearly_income_amount, " +
                "SUM(gt.monthly_amount) AS monthly_amount, " +
                "SUM(gt.yearly_amount) AS yearly_amount " +
                "FROM (SELECT atv.*, " +
                "SUM(vt.monthly_expense_amount) AS monthly_expense_amount, " +
                "SUM(vt.monthly_income_amount) AS monthly_income_amount, " +
                "SUM(vt.yearly_expense_amount) AS yearly_expense_amount, " +
                "SUM(vt.yearly_income_amount) AS yearly_income_amount, " +
                "SUM(vt.monthly_amount) AS monthly_amount, " +
                "SUM(vt.yearly_amount) AS yearly_amount " +
                "FROM (SELECT mt1.*, " +
                "(mt1.monthly_expense_amount + mt1.monthly_income_amount) AS monthly_amount, " +
                "(mt1.yearly_expense_amount + mt1.yearly_income_amount) AS yearly_amount " +
                "FROM (SELECT mt.*, " +
                "(mt.amount * (SELECT monthly_ratio FROM frequency_type WHERE _id = mt.frequency) * (CASE WHEN mt.transaction_type IN (SELECT _id FROM transaction_type WHERE name IS 'debit') THEN -1 ELSE 0 END)) AS monthly_expense_amount, " +
                "(mt.amount * (SELECT monthly_ratio FROM frequency_type WHERE _id = mt.frequency) * (CASE WHEN mt.transaction_type IN (SELECT _id FROM transaction_type WHERE name IS 'debit') THEN 0 ELSE 1 END)) AS monthly_income_amount, " +
                "(mt.amount * (SELECT yearly_ratio FROM frequency_type WHERE _id = mt.frequency) * (CASE WHEN mt.transaction_type IN (SELECT _id FROM transaction_type WHERE name IS 'debit') THEN -1 ELSE 0 END)) AS yearly_expense_amount, " +
                "(mt.amount * (SELECT yearly_ratio FROM frequency_type WHERE _id = mt.frequency) * (CASE WHEN mt.transaction_type IN (SELECT _id FROM transaction_type WHERE name IS 'debit') THEN 0 ELSE 1 END)) AS yearly_income_amount " +
                "FROM my_transaction mt) mt1) vt " +
                "INNER JOIN account atv ON atv._id = vt.account_from " +
                "GROUP BY atv._id " +
                "UNION SELECT vta.*, " +
                "0 AS monthly_expense_amount, " +
                "0 AS monthly_income_amount, " +
                "0 AS yearly_expense_amount, " +
                "0 AS yearly_income_amount, " +
                "0 AS monthly_amount, " +
                "0 AS yearly_amount " +
                "FROM account vta " +
                "UNION SELECT atv.*, " +
                "SUM(vt.monthly_expense_amount) AS monthly_expense_amount, " +
                "SUM(vt.monthly_income_amount) AS monthly_income_amount, " +
                "SUM(vt.yearly_expense_amount) AS yearly_expense_amount, " +
                "SUM(vt.yearly_income_amount) AS yearly_income_amount, " +
                "SUM(vt.monthly_amount) AS monthly_amount, " +
                "SUM(vt.yearly_amount) AS yearly_amount " +
                "FROM (SELECT mt1.*, " +
                "(mt1.monthly_expense_amount + mt1.monthly_income_amount) AS monthly_amount, " +
                "(mt1.yearly_expense_amount + mt1.yearly_income_amount) AS yearly_amount " +
                "FROM (SELECT mt.*, " +
                "(mt.amount * (SELECT monthly_ratio FROM frequency_type WHERE _id = mt.frequency) * (CASE WHEN mt.transaction_type IN (SELECT _id FROM transaction_type WHERE name IS 'credit') THEN -1 ELSE 0 END)) AS monthly_expense_amount, " +
                "(mt.amount * (SELECT monthly_ratio FROM frequency_type WHERE _id = mt.frequency) * (CASE WHEN mt.transaction_type IN (SELECT _id FROM transaction_type WHERE name IS 'credit') THEN 0 ELSE 1 END)) AS monthly_income_amount, " +
                "(mt.amount * (SELECT yearly_ratio FROM frequency_type WHERE _id = mt.frequency) * (CASE WHEN mt.transaction_type IN (SELECT _id FROM transaction_type WHERE name IS 'credit') THEN -1 ELSE 0 END)) AS yearly_expense_amount, " +
                "(mt.amount * (SELECT yearly_ratio FROM frequency_type WHERE _id = mt.frequency) * (CASE WHEN mt.transaction_type IN (SELECT _id FROM transaction_type WHERE name IS 'credit') THEN 0 ELSE 1 END)) AS yearly_income_amount " +
                "FROM my_transaction mt) mt1) vt " +
                "INNER JOIN account atv ON atv._id = vt.account_to " +
                "GROUP BY atv._id) gt " +
                "GROUP BY gt._id) tt;";
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
        contentValues.put(Entry.COL_DESCRIPTION, description);
        return contentValues;
    }

    public static String getTableName() {
        return Entry.TABLE_NAME;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id + ", " +
                "name='" + name + "', " +
                "description='" + description + "'}";
    }
}
