package org.expense.manager.expense.impl;

import org.expense.manager.expense.ExpenseCreator;
import org.expense.manager.repo.TripRepository;
import org.expense.manager.repo.dao.EqualExpenseDao;
import org.expense.manager.repo.dao.ExpenseDao;
import org.expense.manager.repo.dao.TripDao;
import org.expense.manager.repo.impl.TripRepositoryImpl;
import org.expense.manager.service.dto.ExpenseDto;
import org.expense.manager.service.expense.EqualExpenseSplit;
import org.expense.manager.service.expense.ExpenseSplit;

import java.util.*;

public class EqualExpenseCreator implements ExpenseCreator {

    private TripRepository teamRepository;

    public EqualExpenseCreator(){
        teamRepository = TripRepositoryImpl.getInstance();
    }

    @Override
    public ExpenseDao createExpense(ExpenseDto expenseDto) {
        TripDao team = teamRepository.getTrip(expenseDto.getTeam()).get();
        int teamSize = team.getParticipants().size();
        double splitAmount = Math.round(expenseDto.getAmount()/teamSize);
        Set<String> teamMembers = new HashSet<>(team.getParticipants());
        teamMembers.remove(expenseDto.getPaidBy());
        List<ExpenseSplit> expenseSplitList =this.getExpenseSplits(teamMembers,splitAmount);
        return new EqualExpenseDao(expenseDto.getTeam(),expenseDto.getCategory(),expenseDto.getPaidBy(),expenseDto.getAmount(),expenseSplitList);
    }

    private List<ExpenseSplit> getExpenseSplits(Set<String> teamMembers,double splitAmount){
        List<ExpenseSplit> expenseSplitList = new ArrayList<>();
        ExpenseSplit split;
        for(String teamMember : teamMembers){
             split = new EqualExpenseSplit(teamMember);
            split.setAmount(splitAmount);
            expenseSplitList.add(split);
        }
        return expenseSplitList;
    }
}
