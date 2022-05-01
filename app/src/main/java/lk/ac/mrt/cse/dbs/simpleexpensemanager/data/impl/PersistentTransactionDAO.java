package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    // Database related constants
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "expense_manager.db";
    private static final String TABLE_NAME = "tbl_transaction";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ACCOUNT_NO = "account_no";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_EXPENSE_TYPE = "expense_type";
    private static final String COLUMN_AMOUNT = "amount";

    // SQL queries
    private static final String CREATE_TABLE_QUERY =
            "create table " + TABLE_NAME + " ( " +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_ACCOUNT_NO + " text, " +
                    COLUMN_DATE + " date, " +
                    COLUMN_EXPENSE_TYPE + " text, " +
                    COLUMN_AMOUNT + " real " +
                    " )" +
            "constraint fk_account " +
                    "foreign key " + COLUMN_ACCOUNT_NO + " " +
                    "references " + PersistentAccountDAO.getDbName() + "(" +
                            PersistentAccountDAO.getColumnAccountNo() +
                    ")";

    public PersistentTransactionDAO(@Nullable Context context) {
        super(context, "transaction.db", null, 1);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
