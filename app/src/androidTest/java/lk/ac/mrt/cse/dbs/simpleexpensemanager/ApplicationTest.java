/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {
    private PersistentExpenseManager expenseManager;

    @Before
    public void init() {
        Context context = ApplicationProvider.getApplicationContext();
        expenseManager = new PersistentExpenseManager(context);

        // clear the content of the database before each test
        // expenseManager.clearData();
    }

    @Test
    public void testAddAccount() {
        String accNo = "5627X";
        String bankName = "BOC";
        String accHolder = "Sasitha Thathsara";
        double balance = 10000;
        expenseManager.addAccount(accNo, bankName, accHolder, balance);

        List<String> accNumbers = expenseManager.getAccountNumbersList();
        boolean exists = accNumbers.contains(accNo);

        assertTrue(exists);
    }

    @Test
    public void testUpdateAccountBalance() throws InvalidAccountException {
        String accNo = "8961Y";
        String bankName = "BOC";
        String accHolder = "Sasitha Thathsara";
        double balance = 10000;
        expenseManager.addAccount(accNo, bankName, accHolder, balance);

        // test an expense
        int day = 10, month = 5, year = 2022;
        ExpenseType exType = ExpenseType.EXPENSE;
        String amount = "5500";
        expenseManager.updateAccountBalance(accNo, day, month, year, exType, amount);

        double curBalance = expenseManager.getAccountsDAO().getAccount(accNo).getBalance();
        assertEquals(curBalance, 4500, 0.01);
        double trxAmount = expenseManager.getTransactionLogs().get(0).getAmount();
        assertEquals(Double.parseDouble(amount), trxAmount, 0.01);

        // test an income
        exType = ExpenseType.INCOME;
        amount = "7500";
        expenseManager.updateAccountBalance(accNo, day, month, year, exType, amount);

        curBalance = expenseManager.getAccountsDAO().getAccount(accNo).getBalance();
        assertEquals(curBalance, 12000, 0.01);
        trxAmount = expenseManager.getTransactionLogs().get(0).getAmount();
        assertEquals(Double.parseDouble(amount), trxAmount, 0.01);
    }

}