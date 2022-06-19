package org.expense.manager.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TripExpenseSummaryDto {
    private double totalExpenseAmount;
    private List<TripCategoryExpenseDto> categoryExpenses;
    private List<TripParticipantBalanceDto> participantBalanceSheet;
}
