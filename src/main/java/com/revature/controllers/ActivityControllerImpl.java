package com.revature.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.beans.Activity;
import com.revature.services.ActivityService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/activities")
public class ActivityControllerImpl implements ActivityController {
	private ActivityService actService;
	
	@Autowired
	public ActivityControllerImpl(ActivityService actService) {
		this.actService=actService;
	}

	@Override
	@GetMapping("/{location}")
	public ResponseEntity<Flux<Activity>> viewAllActivities(@PathVariable("location") String location, WebSession session) {
		return ResponseEntity.ok(actService.getAllActivities(location));
	}

}
