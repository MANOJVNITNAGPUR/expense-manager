package org.expense.manager.service.expense;

import lombok.Data;

@Data
public class ExpenseSplit {
    private String user;
    private double amount;

    public ExpenseSplit(String user) {
        this.user = user;
    }
}
