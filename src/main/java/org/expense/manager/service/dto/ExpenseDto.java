package org.expense.manager.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.expense.manager.enums.ExpenseSplitType;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDto {
    private String team;
    private String category;
    private String paidBy;
    private double amount;
    private ExpenseSplitType expenseSplitType;
    private Map<String,Double> shares;
}
