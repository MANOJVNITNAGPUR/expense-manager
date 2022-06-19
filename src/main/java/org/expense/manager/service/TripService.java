package org.expense.manager.service;

import org.expense.manager.service.dto.TripDto;

public interface TripService {
    boolean createTeam(String trip);
    TripDto getTrip(String trip);
    boolean addTeamCategory(String trip,String category);
    boolean addTeamParticipant(String trip,String participant);
}
