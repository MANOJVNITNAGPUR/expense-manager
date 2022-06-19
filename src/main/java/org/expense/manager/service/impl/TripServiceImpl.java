package org.expense.manager.service.impl;

import org.expense.manager.repo.TripRepository;
import org.expense.manager.repo.dao.TripDao;
import org.expense.manager.repo.impl.TripRepositoryImpl;
import org.expense.manager.service.TripService;
import org.expense.manager.service.dto.TripDto;

public class TripServiceImpl implements TripService {

    private TripRepository teamRepository;

    private TripServiceImpl(){
       teamRepository = TripRepositoryImpl.getInstance();
    }

    private static class InstanceHolder{
        static final TripServiceImpl INSTANCE = new TripServiceImpl();
    }

    public static TripServiceImpl getInstance(){
        return InstanceHolder.INSTANCE;
    }


    @Override
    public boolean createTeam(String trip) {
        return teamRepository.createTrip(trip);
    }

    @Override
    public TripDto getTrip(String trip) {
        TripDao tripDao = teamRepository.getTrip(trip).get();
       return TripDto.builder()
               .id(tripDao.getId())
               .name(tripDao.getName())
               .participants(tripDao.getParticipants())
               .categories(tripDao.getCategories()).build();
    }

    @Override
    public boolean addTeamCategory(String trip, String category) {
        return teamRepository.getTrip(trip).map(tripDao -> {
            tripDao.getCategories().add(category);
            teamRepository.updateTrip(tripDao);
            return true;
        }).orElse(Boolean.FALSE);
    }

    @Override
    public boolean addTeamParticipant(String trip, String participant) {
        return teamRepository.getTrip(trip).map(tripDao -> {
            tripDao.getParticipants().add(participant);
            teamRepository.updateTrip(tripDao);
            return true;
        }).orElse(Boolean.FALSE);
    }

}
