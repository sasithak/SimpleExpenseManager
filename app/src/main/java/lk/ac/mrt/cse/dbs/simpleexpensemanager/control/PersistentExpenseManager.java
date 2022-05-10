package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DatabaseModel;

public class PersistentExpenseManager extends ExpenseManager {
    Context context;
    DatabaseModel databaseModel;

    public PersistentExpenseManager(Context context) {
        this.context = context;
        setup();
    }

    @Override
    public void setup() {
        PersistentAccountDAO persistentAccountDAO = new PersistentAccountDAO();
        PersistentTransactionDAO persistentTransactionDAO = new PersistentTransactionDAO();
        databaseModel = new DatabaseModel(context, persistentAccountDAO, persistentTransactionDAO);
        setAccountsDAO(persistentAccountDAO);
        setTransactionsDAO(persistentTransactionDAO);
    }

    public void clearData() {
        databaseModel.clear();
    }
}
