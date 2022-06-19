package org.expense.manager.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TripCategoryExpenseDto {
    private String category;
    private double amount;
}
