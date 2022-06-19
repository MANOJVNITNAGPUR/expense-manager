package org.expense.manager.expense.impl;

import org.expense.manager.expense.ExpenseCreator;
import org.expense.manager.repo.TripRepository;
import org.expense.manager.repo.dao.ExpenseDao;
import org.expense.manager.repo.dao.PercentExpenseDao;
import org.expense.manager.repo.dao.TripDao;
import org.expense.manager.repo.impl.TripRepositoryImpl;
import org.expense.manager.service.dto.ExpenseDto;
import org.expense.manager.service.expense.ExpenseSplit;
import org.expense.manager.service.expense.PercentageExpenseSplit;

import java.util.*;

public class PercentageExpenseCreator implements ExpenseCreator {
    private TripRepository teamRepository;

    public PercentageExpenseCreator(){
        teamRepository = TripRepositoryImpl.getInstance();
    }
    @Override
    public ExpenseDao createExpense(ExpenseDto expenseDto) {
        TripDao team = teamRepository.getTrip(expenseDto.getTeam()).get();
        Set<String> teamMembers = new HashSet<>(team.getParticipants());
        teamMembers.remove(expenseDto.getPaidBy());
        List<ExpenseSplit> expenseSplitList = getExpenseSplits(teamMembers,expenseDto.getAmount(),expenseDto.getShares());
        return new PercentExpenseDao(expenseDto.getTeam(),expenseDto.getCategory(),expenseDto.getPaidBy(),expenseDto.getAmount(),expenseSplitList);
    }

    private List<ExpenseSplit> getExpenseSplits(Set<String> teamMembers, double amountPaid, Map<String,Double> percentShare){
        List<ExpenseSplit> expenseSplitList = new ArrayList<>();
        ExpenseSplit split;
        double amount;
        double teamMemberPercent;
        for(String teamMember : teamMembers){
            teamMemberPercent = percentShare.get(teamMember);
            split = new PercentageExpenseSplit(teamMember,teamMemberPercent);
            amount =(amountPaid *(teamMemberPercent/100));
            split.setAmount(amount);
            expenseSplitList.add(split);
        }
        return expenseSplitList;
    }
}
