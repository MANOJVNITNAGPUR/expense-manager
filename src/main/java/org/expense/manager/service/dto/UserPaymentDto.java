package org.expense.manager.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPaymentDto {
    private String sender;
    private String receiver;
    private double amount;
}
