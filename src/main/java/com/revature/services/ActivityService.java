package com.revature.services;

import com.revature.beans.Activity;

import reactor.core.publisher.Flux;

public interface ActivityService {
	/**
	 * Get all the activities in a location
	 * @param location The location of the activities
	 * @return The list of activities at the location
	 */
	public Flux<Activity> getAllActivities(String location);
}
