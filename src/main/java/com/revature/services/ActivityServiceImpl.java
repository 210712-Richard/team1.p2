package com.revature.services;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.beans.Activity;
import com.revature.beans.Vacation;
import com.revature.data.ActivityDao;
import com.revature.data.VacationDao;
import com.revature.dto.ActivityDto;
import com.revature.dto.VacationDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ActivityServiceImpl implements ActivityService {
	private static Logger log = LogManager.getLogger(ActivityServiceImpl.class);
	
	private ActivityDao actDao;
	private VacationDao vacDao;
	
	@Autowired
	public ActivityServiceImpl(ActivityDao actDao, VacationDao vacDao) {
		super();
		this.actDao=actDao;
		this.vacDao=vacDao;
	}
	
	@Override
	public Flux<Activity> getActivities(UUID id, String username) {
		Mono<Vacation> monoVac = vacDao.findByUsernameAndId(username, id).map(VacationDto::getVacation);

		return Flux.from(vacDao.findByUsernameAndId(username, id)).map(vac -> {
			if (vac.getActivities() == null) {
				vac.setActivities(new ArrayList<>());
			}
			return vac.getActivities();
		}).flatMap(l -> {
			log.debug("The list from the vacation: " + l);
			if (l.isEmpty()) {
				return Flux.fromIterable(new ArrayList<Activity>());
			} else {
				//Need to create an infinite flux to make sure all the activities are retrieved
				Flux<Vacation> fluxVac = Flux.from(monoVac)
						.flatMap(v -> Flux.<Vacation>generate(sink -> sink.next(v)));
				
				//Zip the fluxes together and iterate until all the activities have been obtained.
				return Flux.fromIterable(l).zipWith(fluxVac)
						.flatMap(t -> actDao.findByLocationAndId(t.getT2().getDestination(), t.getT1()))
						.map(ActivityDto::getActivity);
			}
		});
	
	}

	@Override
	public Flux<Activity> getAllActivities(String location) {
		return actDao.findByLocation(location).map(activityDto -> activityDto.getActivity());
	}

}
