package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends SQLiteOpenHelper implements AccountDAO {
    // Database related constants
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "expense_manager.db";
    private static final String TABLE_NAME = "tbl_account";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ACCOUNT_NO = "account_no";
    private static final String COLUMN_BANK_NAME = "bank_name";
    private static final String COLUMN_ACCOUNT_HOLDER_NAME = "account_holder_name";
    private static final String COLUMN_BALANCE = "balance";

    // SQL queries
    private static final String CREATE_TABLE_QUERY =
            "create table " + TABLE_NAME + " ( " +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_ACCOUNT_NO + " text, " +
                    COLUMN_BANK_NAME + " text, " +
                    COLUMN_ACCOUNT_HOLDER_NAME + " text, " +
                    COLUMN_BALANCE + " real " +
            " )";

    public PersistentAccountDAO(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public List<String> getAccountNumbersList() {
        return null;
    }

    @Override
    public List<Account> getAccountsList() {
        return null;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return null;
    }

    @Override
    public void addAccount(Account account) {

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static String getColumnAccountNo() {
        return COLUMN_ACCOUNT_NO;
    }
}
