package com.revature.services;

import com.revature.beans.Activity;

import reactor.core.publisher.Flux;

public interface ActivityService {
	public Flux<Activity> getAllActivities(String location);
}
