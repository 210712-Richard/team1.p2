package com.revature.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.beans.Activity;
import com.revature.data.ActivityDao;

import reactor.core.publisher.Flux;

@Service
public class ActivityServiceImpl implements ActivityService {	
	private ActivityDao actDao;
	
	@Autowired
	public ActivityServiceImpl(ActivityDao actDao) {
		super();
		this.actDao=actDao;
	}
	

	@Override
	public Flux<Activity> getAllActivities(String location) {
		return actDao.findByLocation(location).map(activityDto -> activityDto.getActivity());
	}

}
