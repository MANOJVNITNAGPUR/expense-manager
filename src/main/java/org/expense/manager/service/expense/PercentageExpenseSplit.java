package org.expense.manager.service.expense;

import lombok.Getter;

@Getter
public class PercentageExpenseSplit extends ExpenseSplit{
    private double percent;
    public PercentageExpenseSplit(String user,double percent) {
        super(user);
        this.percent = percent;
    }
}
