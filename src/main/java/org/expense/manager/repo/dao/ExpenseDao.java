package org.expense.manager.repo.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.expense.manager.service.expense.ExpenseSplit;

import java.util.List;

@Data

public abstract class ExpenseDao {
    private long id;
    private String team;
    private String category;
    private String paidBy;
    private double amount;
    private List<ExpenseSplit> splits;

    public ExpenseDao(String team,String category,String paidBy,double amount,List<ExpenseSplit> splits){
        this.team = team;
        this.category = category;
        this.paidBy = paidBy;
        this.amount =amount;
        this.splits = splits;
    }

    public abstract boolean validateExpense();
}
