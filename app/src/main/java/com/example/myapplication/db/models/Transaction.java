package com.example.myapplication.db.models;

import android.content.ContentValues;
import android.database.Cursor;

public class Transaction extends BaseModel {

    public static class Entry extends BaseModel.Entry {
        public static String TABLE_NAME = "my_transaction";
        public static String COL_PAYEE = "payee";
        public static String COL_TRANSACTION_TYPE = "transaction_type";
        public static String COL_AMOUNT = "amount";
        public static String COL_BUCKET = "bucket";
        public static String COL_FREQUENCY = "frequency";
        public static String COL_ACCOUNT_FROM = "account_from";
        public static String COL_ACCOUNT_TO = "account_to";
        public static String COL_COMMENT = "comment";
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + Entry.TABLE_NAME + "( \n" +
                    Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    Entry.COL_PAYEE + " TEXT  NOT NULL, \n" +
                    Entry.COL_TRANSACTION_TYPE + " INTEGER  NOT NULL, \n" +
                    Entry.COL_AMOUNT + " REAL  NOT NULL, \n" +
                    Entry.COL_BUCKET + " INTEGER  NOT NULL, \n" +
                    Entry.COL_FREQUENCY + " INTEGER  NOT NULL, \n" +
                    Entry.COL_ACCOUNT_FROM + " INTEGER  NOT NULL, \n" +
                    Entry.COL_ACCOUNT_TO + " INTEGER  NOT NULL, \n" +
                    Entry.COL_COMMENT + " TEXT  NOT NULL,\n" +
                    "FOREIGN KEY (" + Entry.COL_TRANSACTION_TYPE + ") " +
                    "REFERENCES " + TransactionType.Entry.TABLE_NAME + " (" + TransactionType.Entry._ID + ") " +
                    "ON DELETE RESTRICT ON UPDATE RESTRICT,\n " +
                    "FOREIGN KEY (" + Entry.COL_BUCKET + ") " +
                    "REFERENCES " + Bucket.Entry.TABLE_NAME + " (" + Bucket.Entry._ID + ") " +
                    "ON DELETE RESTRICT ON UPDATE RESTRICT,\n " +
                    "FOREIGN KEY (" + Entry.COL_FREQUENCY + ") " +
                    "REFERENCES " + FrequencyType.Entry.TABLE_NAME + " (" + FrequencyType.Entry._ID + ") " +
                    "ON DELETE RESTRICT ON UPDATE RESTRICT,\n " +
                    "FOREIGN KEY (" + Entry.COL_ACCOUNT_FROM + ") " +
                    "REFERENCES " + Account.Entry.TABLE_NAME + " (" + Account.Entry._ID + ") " +
                    "ON DELETE RESTRICT ON UPDATE RESTRICT,\n " +
                    "FOREIGN KEY (" + Entry.COL_ACCOUNT_TO + ") " +
                    "REFERENCES " + Account.Entry.TABLE_NAME + " (" + Account.Entry._ID + ") " +
                    "ON DELETE RESTRICT ON UPDATE RESTRICT)";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;

    private String payee;
    private Integer transactionType;
    private Double amount;
    private Integer bucket;
    private Integer frequency;
    private Integer accountFrom;
    private Integer accountTo;
    private String comment;

    public Transaction(String payee, Integer transactionType, Double amount, Integer bucket,
                       Integer frequency, Integer accountFrom, Integer accountTo, String comment) {
        this.payee = payee;
        this.transactionType = transactionType;
        this.amount = amount;
        this.bucket = bucket;
        this.frequency = frequency;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.comment = comment;
    }

    public Transaction(Integer id, String payee, Integer transactionType, Double amount,
                       Integer bucket, Integer frequency, Integer accountFrom, Integer accountTo,
                       String comment) {
        this.id = id;
        this.payee = payee;
        this.transactionType = transactionType;
        this.amount = amount;
        this.bucket = bucket;
        this.frequency = frequency;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.comment = comment;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getBucket() {
        return bucket;
    }

    public void setBucket(Integer bucket) {
        this.bucket = bucket;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Integer accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Integer getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Integer accountTo) {
        this.accountTo = accountTo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static String[] getColumns() {
        return new String[] { Entry._ID, Entry.COL_PAYEE, Entry.COL_TRANSACTION_TYPE,
                Entry.COL_AMOUNT, Entry.COL_BUCKET, Entry.COL_FREQUENCY, Entry.COL_ACCOUNT_FROM,
                Entry.COL_ACCOUNT_TO, Entry.COL_COMMENT };
    }

    public static <T extends BaseModel> BaseModel processResults(Cursor cursor) {
        Transaction transaction = new Transaction(cursor.getInt(cursor.getColumnIndex(Entry._ID)),
                cursor.getString(cursor.getColumnIndex(Entry.COL_PAYEE)),
                cursor.getInt(cursor.getColumnIndex(Entry.COL_TRANSACTION_TYPE)),
                cursor.getDouble(cursor.getColumnIndex(Entry.COL_AMOUNT)),
                cursor.getInt(cursor.getColumnIndex(Entry.COL_BUCKET)),
                cursor.getInt(cursor.getColumnIndex(Entry.COL_FREQUENCY)),
                cursor.getInt(cursor.getColumnIndex(Entry.COL_ACCOUNT_FROM)),
                cursor.getInt(cursor.getColumnIndex(Entry.COL_ACCOUNT_TO)),
                cursor.getString(cursor.getColumnIndex(Entry.COL_COMMENT)));

        return processResults(cursor, transaction);
    }

    public static String SQL_SELECT_ALL_TABLE() {
        String inner_alias = "mt";
        String outer_alias = "mt1";

        return "SELECT " + outer_alias + ".*, " +
                "      (" + outer_alias + "." + Entry.COL_MONTHLY_EXPENSE_AMOUNT + " + " + outer_alias + "." + Entry.COL_MONTHLY_INCOME_AMOUNT + ") AS " + Entry.COL_MONTHLY_AMOUNT + ", " +
                "      (" + outer_alias + "." + Entry.COL_YEARLY_EXPENSE_AMOUNT + " + " + outer_alias + "." + Entry.COL_YEARLY_INCOME_AMOUNT + ") AS " + Entry.COL_YEARLY_AMOUNT + " " +
                "FROM (SELECT " + inner_alias + ".*, " +
                "             (" + inner_alias + "." + Entry.COL_AMOUNT + " * (SELECT " + FrequencyType.Entry.COL_MONTHLY_RATIO + " FROM " + FrequencyType.Entry.TABLE_NAME + " WHERE " + FrequencyType.Entry._ID + " = " + inner_alias + "." + Entry.COL_FREQUENCY + ") * (CASE WHEN " + inner_alias + "." + Entry.COL_TRANSACTION_TYPE + " IN (SELECT " + TransactionType.Entry._ID + " FROM " + TransactionType.Entry.TABLE_NAME + " WHERE name IS 'debit') THEN -1 ELSE 0 END)) AS " + Entry.COL_MONTHLY_EXPENSE_AMOUNT + ", " +
                "        (" + inner_alias + "." + Entry.COL_AMOUNT + " * (SELECT " + FrequencyType.Entry.COL_MONTHLY_RATIO + " FROM " + FrequencyType.Entry.TABLE_NAME + " WHERE " + FrequencyType.Entry._ID + " = " + inner_alias + "." + Entry.COL_FREQUENCY + ") * (CASE WHEN " + inner_alias + "." + Entry.COL_TRANSACTION_TYPE + " IN (SELECT " + TransactionType.Entry._ID + " FROM " + TransactionType.Entry.TABLE_NAME + " WHERE name IS 'debit') THEN 0 ELSE 1 END)) AS " + Entry.COL_MONTHLY_INCOME_AMOUNT + ", " +
                "        (" + inner_alias + "." + Entry.COL_AMOUNT + " * (SELECT " + FrequencyType.Entry.COL_YEARLY_RATIO + " FROM " + FrequencyType.Entry.TABLE_NAME + " WHERE " + FrequencyType.Entry._ID + " = " + inner_alias + "." + Entry.COL_FREQUENCY + ") * (CASE WHEN " + inner_alias + "." + Entry.COL_TRANSACTION_TYPE + " IN (SELECT " + TransactionType.Entry._ID + " FROM " + TransactionType.Entry.TABLE_NAME + " WHERE name IS 'debit') THEN -1 ELSE 0 END)) AS " + Entry.COL_YEARLY_EXPENSE_AMOUNT + ", " +
                "        (" + inner_alias + "." + Entry.COL_AMOUNT + " * (SELECT " + FrequencyType.Entry.COL_YEARLY_RATIO + " FROM " + FrequencyType.Entry.TABLE_NAME + " WHERE " + FrequencyType.Entry._ID + " = " + inner_alias + "." + Entry.COL_FREQUENCY + ") * (CASE WHEN " + inner_alias + "." + Entry.COL_TRANSACTION_TYPE + " IN (SELECT " + TransactionType.Entry._ID + " FROM " + TransactionType.Entry.TABLE_NAME + " WHERE name IS 'debit') THEN 0 ELSE 1 END)) AS " + Entry.COL_YEARLY_INCOME_AMOUNT + " " +
                "        FROM " + Entry.TABLE_NAME + " " + inner_alias + ") " + outer_alias + ";";
    }

    @Override
    public String getDisplayText() {
        return payee;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Entry._ID, id);
        contentValues.put(Entry.COL_PAYEE, payee);
        contentValues.put(Entry.COL_TRANSACTION_TYPE, transactionType);
        contentValues.put(Entry.COL_AMOUNT, amount);
        contentValues.put(Entry.COL_BUCKET, bucket);
        contentValues.put(Entry.COL_FREQUENCY, frequency);
        contentValues.put(Entry.COL_ACCOUNT_FROM, accountFrom);
        contentValues.put(Entry.COL_ACCOUNT_TO, accountTo);
        contentValues.put(Entry.COL_COMMENT, comment);
        return contentValues;
    }

    public static String getTableName() {
        return Entry.TABLE_NAME;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id + ", " +
                "payee='" + payee + "', " +
                "transactionType=" + transactionType + ", " +
                "amount=" + amount + ", " +
                "bucket=" + bucket + ", " +
                "frequency=" + frequency + ", " +
                "accountFrom=" + accountFrom + ", " +
                "accountTo=" + accountTo + ", " +
                "comment=" + comment + "}";
    }
}
