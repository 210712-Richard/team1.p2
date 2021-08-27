package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Activity;

import reactor.core.publisher.Flux;

public interface ActivityController {
	/**
	 * View all activities in a location
	 * @param location The location being found
	 * @param session The WebSession
	 * @return A ResponseEntity containing a Flux of Activities
	 */
	public ResponseEntity<Flux<Activity>> viewAllActivities(String location, WebSession session);

}
