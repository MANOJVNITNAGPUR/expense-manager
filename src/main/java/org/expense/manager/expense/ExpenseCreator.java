package org.expense.manager.expense;

import org.expense.manager.repo.dao.ExpenseDao;
import org.expense.manager.service.dto.ExpenseDto;

public interface ExpenseCreator {
    ExpenseDao createExpense(ExpenseDto expenseDto);
}
