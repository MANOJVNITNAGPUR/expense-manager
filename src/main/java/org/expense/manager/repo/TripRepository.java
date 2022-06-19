package org.expense.manager.repo;

import org.expense.manager.repo.dao.TripDao;

import java.util.Optional;

public interface TripRepository {
    boolean createTrip(String trip);

    Optional<TripDao> getTrip(String trip);

    void updateTrip(TripDao teamDao);
}
