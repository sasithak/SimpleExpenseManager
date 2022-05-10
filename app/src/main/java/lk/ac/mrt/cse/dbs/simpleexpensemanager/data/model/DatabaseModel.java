package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class DatabaseModel extends SQLiteOpenHelper {
    private static final String DB_NAME = "190332D.db";
    private static final int DB_VERSION = 1;
    private PersistentAccountDAO accountDAO;
    private PersistentTransactionDAO transactionDAO;

    public DatabaseModel(
            @Nullable Context context,
            PersistentAccountDAO accountDAO,
            PersistentTransactionDAO transactionDAO
    ) {
        super(context, DB_NAME, null, DB_VERSION);
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
        accountDAO.setDbModel(this);
        transactionDAO.setDbModel(this);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        accountDAO.onCreate(sqLiteDatabase);
        transactionDAO.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        accountDAO.onUpgrade(sqLiteDatabase, i, i1);
        transactionDAO.onUpgrade(sqLiteDatabase, i, i1);
    }

    public SQLiteDatabase getDBConnection(boolean writable) {
        if (writable) return getWritableDatabase();
        return getReadableDatabase();
    }

    public void clear() {
        onUpgrade(getWritableDatabase(), DB_VERSION, DB_VERSION);
    }

}
