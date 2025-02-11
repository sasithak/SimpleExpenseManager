package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DatabaseModel;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    // Database related constants
    private static final String TABLE_NAME = "tbl_account";
    private static final String COLUMN_ACCOUNT_NO = "account_no";
    private static final String COLUMN_BANK_NAME = "bank_name";
    private static final String COLUMN_ACCOUNT_HOLDER_NAME = "account_holder_name";
    private static final String COLUMN_BALANCE = "balance";

    private DatabaseModel dbModel;

    public void setDbModel(DatabaseModel dbModel) {
        this.dbModel = dbModel;
    }

    @Override
    public String toString() {
        return "PersistentAccountDAO{}";
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbers = new ArrayList<String>();

        String getAccountNumbersQuery = "select " + COLUMN_ACCOUNT_NO + " from " + TABLE_NAME;
        SQLiteDatabase db = dbModel.getDBConnection(false);
        Cursor cursor = db.rawQuery(getAccountNumbersQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String accountNo = cursor.getString(0);
                accountNumbers.add(accountNo);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<Account>();

        String getAccountsQuery = "select * from " + TABLE_NAME;
        SQLiteDatabase db = dbModel.getDBConnection(false);
        Cursor cursor = db.rawQuery(getAccountsQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getDouble(3);
                Account account = new Account(accountNo, bankName, accountHolderName, balance);
                accounts.add(account);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = null;

        String GET_ACCOUNT_SQL_QUERY = "select * from " + TABLE_NAME +
                " where " + COLUMN_ACCOUNT_NO + "=?";
        SQLiteDatabase db = dbModel.getDBConnection(false);
        Cursor cursor = db.rawQuery(GET_ACCOUNT_SQL_QUERY, new String[]{accountNo});
        if (cursor.moveToFirst()) {
            do {
                accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getDouble(3);
                account = new Account(accountNo, bankName, accountHolderName, balance);
            } while(cursor.moveToNext());
        } else {
            throw new InvalidAccountException("Account number not found.");
        }
        cursor.close();
        db.close();

        return account;
    }

    @Override
    public void addAccount(Account account) {
        String INSERT_SQL_QUERY =
                "insert into " + TABLE_NAME + " (" +
                        COLUMN_ACCOUNT_NO + ", " +
                        COLUMN_BANK_NAME + ", " +
                        COLUMN_ACCOUNT_HOLDER_NAME + ", " +
                        COLUMN_BALANCE +
                ") values (?, ?, ?, " + account.getBalance() + ")";
        SQLiteDatabase db = dbModel.getDBConnection(true);
        String[] selectionArgs = {
                account.getAccountNo(),
                account.getBankName(),
                account.getAccountHolderName()
        };
        Cursor cursor = db.rawQuery(INSERT_SQL_QUERY, selectionArgs);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String DELETE_SQL_QUERY =
                "delete from " + TABLE_NAME + " " +
                        "where " + COLUMN_ACCOUNT_NO + "=?";
        SQLiteDatabase db = dbModel.getDBConnection(true);
        String[] selectionArgs = {accountNo};
        Cursor cursor = db.rawQuery(DELETE_SQL_QUERY, selectionArgs);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        double newBalance = expenseType == ExpenseType.EXPENSE
                ? account.getBalance() - amount
                : account.getBalance() + amount;
        String UPDATE_BALANCE_SQL_QUERY =
                "update " + TABLE_NAME + " " +
                        "set " + COLUMN_BALANCE + " = " + newBalance + " " +
                        "where " + COLUMN_ACCOUNT_NO + "=?";
        SQLiteDatabase db = dbModel.getDBConnection(true);
        String[] selectionArgs = {accountNo};
        Cursor cursor = db.rawQuery(UPDATE_BALANCE_SQL_QUERY, selectionArgs);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_QUERY =
                "create table " + TABLE_NAME + " ( " +
                        COLUMN_ACCOUNT_NO + " text primary key, " +
                        COLUMN_BANK_NAME + " text, " +
                        COLUMN_ACCOUNT_HOLDER_NAME + " text, " +
                        COLUMN_BALANCE + " real " +
                " )";

        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE_SQL_QUERY = "drop table if exists " + TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE_SQL_QUERY);
        onCreate(sqLiteDatabase);
    }

    public static String getColumnAccountNo() {
        return COLUMN_ACCOUNT_NO;
    }
}
