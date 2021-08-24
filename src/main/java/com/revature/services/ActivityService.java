package com.revature.services;

import java.util.UUID;

import com.revature.beans.Activity;

import reactor.core.publisher.Flux;

public interface ActivityService {
	public Flux<Activity> getAllActivities(String location);
}
