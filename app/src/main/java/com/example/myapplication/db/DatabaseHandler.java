package com.example.myapplication.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.db.models.Account;
import com.example.myapplication.db.models.BaseModel;
import com.example.myapplication.db.models.Bucket;
import com.example.myapplication.db.models.FrequencyType;
import com.example.myapplication.db.models.Transaction;
import com.example.myapplication.db.models.TransactionType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Budget.db";

    public Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Enable foreign key constraints
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TransactionType.SQL_CREATE_TABLE);

        Log.i(DatabaseHandler.class.toString(),"Initialize " + TransactionType.class + " Table ...");

        create(new TransactionType("debit", "debit"), db);
        create(new TransactionType("credit", "credit"), db);

        Log.i(DatabaseHandler.class.toString(),"... Done");

        db.execSQL(FrequencyType.SQL_CREATE_TABLE);

        Log.i(DatabaseHandler.class.toString(),"Initialize " + FrequencyType.class + " Table ...");

        create(new FrequencyType("yearly", 1.0 / 12.0, 1.0, "yearly"), db);
        create(new FrequencyType("monthly", 1.0, 12.0, "monthly"), db);
        create(new FrequencyType("bi_monthly_other", 1.0 / 2.0, 6.0, "bi_monthly_other"), db);
        create(new FrequencyType("bi_monthly_2x", 2.0, 24.0, "bi_monthly_2x"), db);
        create(new FrequencyType("tri_monthly_other", 1.0 / 3.0, 4.0, "tri_monthly_other"), db);
        create(new FrequencyType("tri_monthly_3x", 3.0, 36.0, "tri_monthly_3x"), db);
        create(new FrequencyType("weekly", 13.0 / 3.0, 52.0, "weekly"), db);
        create(new FrequencyType("bi_weekly_other", 13.0 / 6.0, 26.0, "bi_weekly_other"), db);
        create(new FrequencyType("bi_weekly_2x", 26.0 / 3.0, 104.0, "bi_weekly_2x"), db);

        Log.i(DatabaseHandler.class.toString(),"... Done");

        db.execSQL(Bucket.SQL_CREATE_TABLE);

        Log.i(DatabaseHandler.class.toString(),"Initialize " + Bucket.class + " Table ...");

        create(new Bucket("Payroll"), db);
        create(new Bucket("Loans"), db);
        create(new Bucket("Utilities"), db);
        create(new Bucket("Insurance"), db);
        create(new Bucket("Pet"), db);
        create(new Bucket("Lagnaippe"), db);
        create(new Bucket("Donations"), db);
        create(new Bucket("Tadlet"), db);
        create(new Bucket("Automobile"), db);
        create(new Bucket("Supervision"), db);
        create(new Bucket("食べ物"), db);
        create(new Bucket("Savings"), db);
        create(new Bucket("Investments"), db);

        Log.i(DatabaseHandler.class.toString(),"... Done");

        db.execSQL(Account.SQL_CREATE_TABLE);

        Log.i(DatabaseHandler.class.toString(),"Initialize " + Account.class + " Table ...");

        create(new Account("None", "Endless Void"), db);
        create(new Account("Chase Savings", "Savings"), db);
        create(new Account("Chase Checking", "Checking"), db);
        create(new Account("Capital One", "Savings"), db);
        create(new Account("Maple", "Savings"), db);
        create(new Account("Fidelity", "Savings"), db);

        Log.i(DatabaseHandler.class.toString(),"... Done");

        db.execSQL(Transaction.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Transaction.SQL_DELETE_TABLE);
        db.execSQL(Account.SQL_DELETE_TABLE);
        db.execSQL(Bucket.SQL_DELETE_TABLE);
        db.execSQL(FrequencyType.SQL_CREATE_TABLE);
        db.execSQL(TransactionType.SQL_CREATE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private String getTableName(Class<? extends BaseModel> clazz) {
        String tableName = null;
        try {
            tableName = (String) clazz.getMethod("getTableName").invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return tableName;
    }

    private String[] getColumns(Class<? extends BaseModel> clazz) {
        String[] columns = null;
        try {
            columns = (String[]) clazz.getMethod("getColumns").invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return columns;
    }

    private <T extends BaseModel> BaseModel processResults(Cursor cursor, Class<? extends BaseModel> clazz) {
        BaseModel resultSet = null;
        try {
            resultSet = (BaseModel) clazz.getMethod("processResults", Cursor.class).invoke(null,cursor);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public boolean create(BaseModel model, SQLiteDatabase db) {
        long id = db.insert(getTableName(model.getClass()), null, model.getContentValues());
        return id > 0;
    }

    public boolean create(BaseModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean successful = create(model, db);
        db.close();
        return successful;
    }

    public boolean update(BaseModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.update(getTableName(model.getClass()),
                model.getContentValues(),
                BaseModel.Entry._ID + "= ? ",
                new String[] { Integer.toString(model.getId()) } );
        db.close();
        return affectedRows > 0;
    }

    public Integer delete(BaseModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.delete(getTableName(model.getClass()),
                BaseModel.Entry._ID + "= ? ",
                new String[] { Integer.toString(model.getId()) });
        db.close();
        return affectedRows;
    }

    public <T extends BaseModel> BaseModel getEntry(int id, Class<? extends BaseModel> clazz) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(getTableName(clazz), getColumns(clazz), BaseModel.Entry._ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        BaseModel baseModel = processResults(cursor, clazz);
        db.close();

        // return BaseModel object
        return baseModel;
    }

    public List<? extends BaseModel> getAllEntries(Class<? extends BaseModel> clazz) {
        List<BaseModel> entryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(getTableName(clazz), getColumns(clazz),
                null,null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BaseModel baseModel = processResults(cursor, clazz);
                // Adding BaseModel object to list
                entryList.add(baseModel);
            } while (cursor.moveToNext());
        }

        db.close();
        return entryList;
    }

    public List<Transaction> getTransaction() {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(Transaction.SQL_SELECT_ALL_TABLE(), null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = (Transaction) processResults(cursor, Transaction.class);
                // Adding BaseModel object to list
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        db.close();
        return transactionList;
    }

    public List<Bucket> getBucket() {
        List<Bucket> bucketList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(Bucket.SQL_SELECT_ALL_TABLE(), null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Bucket bucket = (Bucket) processResults(cursor, Bucket.class);
                // Adding BaseModel object to list
                bucketList.add(bucket);
            } while (cursor.moveToNext());
        }

        db.close();
        return bucketList;
    }

    private String getSelectAllTableSql(Class<? extends BaseModel> clazz) {
        String tableName = null;
        try {
            tableName = (String) clazz.getMethod("SQL_SELECT_ALL_TABLE").invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return tableName;
    }

    public List<? extends BaseModel> getAllDetailedEntries(Class<? extends BaseModel> clazz) {
        List<BaseModel> entryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(getSelectAllTableSql(clazz), null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BaseModel baseModel = processResults(cursor, clazz);
                // Adding BaseModel object to list
                entryList.add(baseModel);
            } while (cursor.moveToNext());
        }

        db.close();
        return entryList;
    }
}
