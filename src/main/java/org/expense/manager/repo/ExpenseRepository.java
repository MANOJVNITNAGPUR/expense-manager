package org.expense.manager.repo;

import org.expense.manager.repo.dao.ExpenseDao;

import java.util.List;

public interface ExpenseRepository {
    void addExpense(String team,ExpenseDao expenseDao);

    List<ExpenseDao> getTeamExpenses(String team);
}
