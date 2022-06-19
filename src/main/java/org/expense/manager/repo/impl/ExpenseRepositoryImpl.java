package org.expense.manager.repo.impl;

import org.expense.manager.repo.ExpenseRepository;
import org.expense.manager.repo.dao.ExpenseDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ExpenseRepositoryImpl implements ExpenseRepository {

    private Map<String, List<ExpenseDao>> teamExpenses;
    private AtomicLong expenseId;
    private ReentrantReadWriteLock lock;
    private ReentrantReadWriteLock.WriteLock writer;
    private ReentrantReadWriteLock.ReadLock reader;


    private static class InstanceHolder{
        static final ExpenseRepositoryImpl INSTANCE = new ExpenseRepositoryImpl();
    }

    public static ExpenseRepositoryImpl getInstance(){
        return ExpenseRepositoryImpl.InstanceHolder.INSTANCE;
    }

    private ExpenseRepositoryImpl(){
        teamExpenses = new HashMap<>();
        expenseId = new AtomicLong(0);
        lock = new ReentrantReadWriteLock(true);
        writer  = lock.writeLock();
        reader = lock.readLock();
    }

    @Override
    public void addExpense(String team,ExpenseDao expenseDao) {
        try{
            writer.lock();
            expenseDao.setId(expenseId.incrementAndGet());
            List<ExpenseDao> expenses = teamExpenses.getOrDefault(expenseDao.getTeam(),new ArrayList<>());
            expenses.add(expenseDao);
            teamExpenses.put(team,expenses);
        }
        finally {
            writer.unlock();
        }
    }

    @Override
    public List<ExpenseDao> getTeamExpenses(String team) {
        try{
            reader.lock();
            return teamExpenses.get(team);
        }finally {
            reader.unlock();
        }
    }

}
