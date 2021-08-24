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
	public Flux<Activity> getAllActivities(String location) {
		return actDao.findByLocation(location).map(activityDto -> activityDto.getActivity());
	}

}
