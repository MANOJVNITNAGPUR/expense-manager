package org.expense.manager.expense.factory;

import org.expense.manager.enums.ExpenseSplitType;
import org.expense.manager.expense.ExpenseCreator;
import org.expense.manager.repo.dao.ExpenseDao;
import org.expense.manager.expense.impl.EqualExpenseCreator;
import org.expense.manager.expense.impl.PercentageExpenseCreator;
import org.expense.manager.service.dto.ExpenseDto;

public class ExpenseCreatorFactory {

    private static ExpenseCreator equalExpenseCreator = new EqualExpenseCreator();
    private static ExpenseCreator percentageExpenseCreator = new PercentageExpenseCreator();

    public static ExpenseDao getExpenseDao(ExpenseDto expenseDto){
        ExpenseSplitType splitType = expenseDto.getExpenseSplitType();
        if(ExpenseSplitType.EQUAL.equals(splitType)){
            return equalExpenseCreator.createExpense(expenseDto);
        }else{
            return percentageExpenseCreator.createExpense(expenseDto);
        }
    }
}
