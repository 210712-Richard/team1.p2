package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Activity;

import reactor.core.publisher.Flux;

public interface ActivityController {
	public ResponseEntity<Flux<Activity>> viewAllActivities(String location, WebSession session);

}
