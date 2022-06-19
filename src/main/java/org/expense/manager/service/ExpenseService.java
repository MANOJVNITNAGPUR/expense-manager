package org.expense.manager.service;

import org.expense.manager.service.dto.ExpenseDto;
import org.expense.manager.service.dto.TripExpenseSummaryDto;
import org.expense.manager.service.dto.UserPaymentDto;

import java.util.List;

public interface ExpenseService {

    boolean addExpense(ExpenseDto expenseDto);

    TripExpenseSummaryDto getTeamExpenseSummary(String team);

    List<UserPaymentDto> getTransitivePaymentSummary(String team);
}
