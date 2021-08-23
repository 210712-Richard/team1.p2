package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.aspects.LoggedInFlux;
import com.revature.beans.Activity;
import com.revature.services.ActivityService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/activities")
public class ActivityControllerImpl implements ActivityController {
	private static Logger log = LogManager.getLogger(ReservationControllerImpl.class);

	private ActivityService actService;
	
	@Autowired
	public ActivityControllerImpl(ActivityService actService) {
		this.actService=actService;
	}
	
	
	@Override
	@LoggedInFlux
	@GetMapping("/{vacId}")
	public Flux<ResponseEntity<Activity>> viewActivities(String vacId, WebSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@LoggedInFlux
	@GetMapping
	public Flux<ResponseEntity<Activity>> viewAllActivities(String location, WebSession session) {
		return actService.getAllActivities(location).map(act -> ResponseEntity.ok(act));
	}

}
