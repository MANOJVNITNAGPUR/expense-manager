package org.expense.manager.repo.dao;

import org.expense.manager.service.expense.EqualExpenseSplit;
import org.expense.manager.service.expense.ExpenseSplit;
import org.expense.manager.service.expense.PercentageExpenseSplit;

import java.util.List;

public class PercentExpenseDao extends ExpenseDao{
    public PercentExpenseDao(String team, String category, String paidBy, double amount, List<ExpenseSplit> splits){
        super(team,category,paidBy,amount,splits);
    }

    @Override
    public boolean validateExpense() {
        for (ExpenseSplit split : getSplits()) {
            if (!(split instanceof EqualExpenseSplit)) {
                return false;
            }
        }

        double totalSplitPercent = 0.0;
        for(ExpenseSplit split : getSplits()){
            PercentageExpenseSplit expenseSplit = (PercentageExpenseSplit) split;
            totalSplitPercent+=expenseSplit.getPercent();
        }
        return Double.compare(100,totalSplitPercent)!=0;
    }
}
