package org.expense.manager.repo.impl;

import org.expense.manager.repo.dao.TripDao;
import org.expense.manager.repo.TripRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TripRepositoryImpl implements TripRepository {

    private Map<String, TripDao> tripMap;
    private AtomicLong tripId;
    private ReentrantReadWriteLock lock;
    private ReentrantReadWriteLock.WriteLock writer;
    private ReentrantReadWriteLock.ReadLock reader;

    private static class InstanceHolder{
        static final TripRepositoryImpl INSTANCE = new TripRepositoryImpl();
    }

    public static TripRepositoryImpl getInstance(){
        return InstanceHolder.INSTANCE;
    }

    private TripRepositoryImpl(){
        tripMap = new HashMap<>();
        tripId = new AtomicLong(0);
        lock = new ReentrantReadWriteLock(true);
        writer  = lock.writeLock();
        reader = lock.readLock();
    }

    @Override
    public boolean createTrip(String trip) {
        try{
            writer.lock();
            if(tripMap.containsKey(trip)){
                return false;
            }
            TripDao teamDao = TripDao.builder()
                    .id(tripId.incrementAndGet())
                    .name(trip)
                    .categories(new HashSet<>())
                    .participants(new HashSet<>())
                    .build();
            tripMap.put(trip,teamDao);
            return true;
        }finally {
            writer.unlock();
        }
    }

    @Override
    public Optional<TripDao> getTrip(String trip) {
        return Optional.ofNullable(tripMap.get(trip));
    }

    @Override
    public void updateTrip(TripDao tripDao) {
        tripMap.put(tripDao.getName(),tripDao);
    }
}
