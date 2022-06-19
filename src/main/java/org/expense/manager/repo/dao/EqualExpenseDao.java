package org.expense.manager.repo.dao;

import org.expense.manager.service.expense.EqualExpenseSplit;
import org.expense.manager.service.expense.ExpenseSplit;

import java.util.List;

public class EqualExpenseDao extends ExpenseDao{

    public EqualExpenseDao(String team, String category, String paidBy, double amount, List<ExpenseSplit> splits){
        super(team,category,paidBy,amount,splits);
    }

    @Override
    public boolean validateExpense() {
        for (ExpenseSplit split : getSplits()) {
            if (!(split instanceof EqualExpenseSplit)) {
                return false;
            }
        }

        return true;
    }
}
