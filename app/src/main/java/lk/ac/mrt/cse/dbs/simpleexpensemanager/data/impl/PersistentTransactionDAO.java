package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DatabaseModel;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    // Database related constants
    private static final String TABLE_NAME = "tbl_transaction";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ACCOUNT_NO = "account_no";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_EXPENSE_TYPE = "expense_type";
    private static final String COLUMN_AMOUNT = "amount";

    private DatabaseModel dbModel;

    public void setDbModel(DatabaseModel dbModel) {
        this.dbModel = dbModel;
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = sdf.format(date);
        String INSERT_SQL_QUERY =
                "insert into " + TABLE_NAME + " (" +
                        COLUMN_DATE + ", " +
                        COLUMN_ACCOUNT_NO + ", " +
                        COLUMN_EXPENSE_TYPE + ", " +
                        COLUMN_AMOUNT +
                        ") values (" +
                        "? , " +
                        "?, " +
                        expenseType.ordinal() + ", " +
                        amount +
                ")";
        SQLiteDatabase db = dbModel.getDBConnection(true);
        String[] selectionArgs = {dateStr, accountNo};
        Cursor cursor = db.rawQuery(INSERT_SQL_QUERY, selectionArgs);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String GET_TRANSACTIONS_SQL_QUERY = "select * from " + TABLE_NAME;
        SQLiteDatabase db = dbModel.getDBConnection(false);
        Cursor cursor = db.rawQuery(GET_TRANSACTIONS_SQL_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                Date date;
                try{
                    date = sdf.parse(cursor.getString(1));
                }
                catch (ParseException e) {
                    return null;
                }
                String accountNo = cursor.getString(2);
                ExpenseType expenseType = cursor.getInt(3) == 0
                        ? ExpenseType.EXPENSE
                        : ExpenseType.INCOME;
                double amount = cursor.getDouble(4);
                Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                transactions.add(transaction);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String GET_PAGINATED_TRANSACTIONS_SQL_QUERY =
                "select * from " + TABLE_NAME + " " +
                        "order by " + COLUMN_ID + " desc " +
                        "limit " + limit;
        SQLiteDatabase db = dbModel.getDBConnection(false);
        Cursor cursor = db.rawQuery(GET_PAGINATED_TRANSACTIONS_SQL_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                String accountNo = cursor.getString(1);
                Date date;
                try{
                    date = sdf.parse(cursor.getString(2));
                }
                catch (ParseException e) {
                    return null;
                }
                ExpenseType expenseType = cursor.getInt(3) == 0
                        ? ExpenseType.EXPENSE
                        : ExpenseType.INCOME;
                double amount = cursor.getDouble(4);
                Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                transactions.add(transaction);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactions;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_QUERY =
                "create table " + TABLE_NAME + " ( " +
                        COLUMN_ID + " integer primary key autoincrement, " +
                        COLUMN_ACCOUNT_NO + " text, " +
                        COLUMN_DATE + " date, " +
                        COLUMN_EXPENSE_TYPE + " text, " +
                        COLUMN_AMOUNT + " real " +
                ")";

        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE_SQL_QUERY = "drop table if exists " + TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE_SQL_QUERY);
        onCreate(sqLiteDatabase);
    }
}
