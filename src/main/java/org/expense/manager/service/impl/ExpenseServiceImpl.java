package org.expense.manager.service.impl;

import org.expense.manager.expense.factory.ExpenseCreatorFactory;
import org.expense.manager.repo.ExpenseRepository;
import org.expense.manager.repo.dao.ExpenseDao;
import org.expense.manager.repo.impl.ExpenseRepositoryImpl;
import org.expense.manager.service.ExpenseService;
import org.expense.manager.service.dto.*;
import org.expense.manager.service.expense.ExpenseSplit;

import java.util.*;

public class ExpenseServiceImpl implements ExpenseService {

    private ExpenseRepository expenseRepository;

    private static class InstanceHolder{
        static final ExpenseServiceImpl INSTANCE = new ExpenseServiceImpl();
    }

    public static ExpenseServiceImpl getInstance(){
        return ExpenseServiceImpl.InstanceHolder.INSTANCE;
    }

    private ExpenseServiceImpl(){
        expenseRepository = ExpenseRepositoryImpl.getInstance();
    }


    @Override
    public boolean addExpense(ExpenseDto expenseDto) {
        ExpenseDao expenseDao = ExpenseCreatorFactory.getExpenseDao(expenseDto);
        expenseRepository.addExpense(expenseDto.getTeam(),expenseDao);
        return true;
    }

    @Override
    public TripExpenseSummaryDto getTeamExpenseSummary(String team) {
        List<ExpenseDao> expenseDaoList = expenseRepository.getTeamExpenses(team);
        double totalAmount = expenseDaoList.stream().mapToDouble(ExpenseDao::getAmount).sum();
        List<TripCategoryExpenseDto> categoryExpenseDtoList = this.getCategoryExpenses(expenseDaoList);
        List<TripParticipantBalanceDto> teamParticipantBalanceList = this.getTeamParticipantBalance(expenseDaoList);

        return TripExpenseSummaryDto.builder()
                .totalExpenseAmount(totalAmount)
                .categoryExpenses(categoryExpenseDtoList)
                .participantBalanceSheet(teamParticipantBalanceList)
                .build();
    }

    @Override
    public List<UserPaymentDto> getTransitivePaymentSummary(String team) {
        List<ExpenseDao> expenseDaoList = expenseRepository.getTeamExpenses(team);
        List<TripParticipantBalanceDto> teamParticipantBalanceList = this.getTeamParticipantBalance(expenseDaoList);
        Queue<ExpenseSplit> senderQueue = new PriorityQueue<>((split1,split2) -> Double.compare(split2.getAmount(),split1.getAmount()));
        Queue<ExpenseSplit> receiverQueue = new PriorityQueue<>((split1,split2) -> Double.compare(split2.getAmount(),split1.getAmount()));
        teamParticipantBalanceList.forEach(teamParticipantBalanceDto -> {
            int compareResult = Double.compare(teamParticipantBalanceDto.getOwesAmount(),teamParticipantBalanceDto.getOwedAmount());
            if(compareResult!=0){
                ExpenseSplit split = new ExpenseSplit(teamParticipantBalanceDto.getUser());
                if(compareResult==1){
                  split.setAmount(teamParticipantBalanceDto.getOwesAmount() - teamParticipantBalanceDto.getOwedAmount());
                  senderQueue.add(split);
                }else{
                    split.setAmount( teamParticipantBalanceDto.getOwedAmount()-teamParticipantBalanceDto.getOwesAmount());
                    receiverQueue.add(split);
                }
            }
        });
        return this.getPaymentSummary(senderQueue,receiverQueue);
    }

    private List<UserPaymentDto> getPaymentSummary(Queue<ExpenseSplit> senderQueue,Queue<ExpenseSplit> receiverQueue){
        List<UserPaymentDto> summary = new ArrayList<>();
        while (!senderQueue.isEmpty() && !receiverQueue.isEmpty()){
            ExpenseSplit sender = senderQueue.poll();
            ExpenseSplit receiver = receiverQueue.poll();
            UserPaymentDto paymentDto = UserPaymentDto.builder().sender(sender.getUser()).receiver(receiver.getUser()).build();
            int compareResult = Double.compare(sender.getAmount(),receiver.getAmount());
            if(compareResult == -1){
                paymentDto.setAmount(sender.getAmount());
                ExpenseSplit split = new ExpenseSplit(receiver.getUser());
                split.setAmount(receiver.getAmount()-sender.getAmount());
                receiverQueue.add(split);
            }else{
                if(compareResult==1){
                    paymentDto.setAmount(receiver.getAmount());
                    ExpenseSplit split = new ExpenseSplit(sender.getUser());
                    split.setAmount(sender.getAmount()-receiver.getAmount());
                    senderQueue.add(split);
                }else{
                    paymentDto.setAmount(sender.getAmount());
                }
            }
            summary.add(paymentDto);
        }
        return  summary;
    }

    private List<TripCategoryExpenseDto> getCategoryExpenses(List<ExpenseDao> expenseDaoList){
        Map<String,Double> categoryExpenses = new HashMap<>();
        for(ExpenseDao expense : expenseDaoList){
            double categoryExpense = categoryExpenses.getOrDefault(expense.getCategory(),0.0);
            categoryExpense+=expense.getAmount();
            categoryExpenses.put(expense.getCategory(),categoryExpense);
        }
        List<TripCategoryExpenseDto> categoryExpenseDtoList = new ArrayList<>();
        categoryExpenses.forEach((category,expenseAmount) -> {
            TripCategoryExpenseDto categoryExpenseDto = TripCategoryExpenseDto.builder()
                    .category(category)
                    .amount(expenseAmount)
                    .build();
            categoryExpenseDtoList.add(categoryExpenseDto);
        });
     return categoryExpenseDtoList;
    }

    private List<TripParticipantBalanceDto> getTeamParticipantBalance(List<ExpenseDao> expenseDaoList){
        Map<String, TripParticipantBalanceDto> balanceSheet = new HashMap<>();
        for(ExpenseDao expense : expenseDaoList){
            String paidBy = expense.getPaidBy();
            balanceSheet.putIfAbsent(paidBy,new TripParticipantBalanceDto(paidBy));
            TripParticipantBalanceDto payer = balanceSheet.get(paidBy);
            for(ExpenseSplit expenseSplit : expense.getSplits()){
                String paidTo = expenseSplit.getUser();
                balanceSheet.putIfAbsent(paidTo,new TripParticipantBalanceDto(paidTo));
                payer.addOwedAmount(expenseSplit.getAmount());
                TripParticipantBalanceDto receiver = balanceSheet.get(paidTo);
                receiver.addOwesAmount(expenseSplit.getAmount());
            }}
        return new ArrayList<>(balanceSheet.values());
    }
}
