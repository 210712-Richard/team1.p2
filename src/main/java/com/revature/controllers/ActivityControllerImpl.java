package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.beans.Activity;
import com.revature.beans.User;
import com.revature.services.ActivityService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/activities")
public class ActivityControllerImpl implements ActivityController {
	private static Logger log = LogManager.getLogger(ActivityControllerImpl.class);

	private ActivityService actService;
	
	@Autowired
	public ActivityControllerImpl(ActivityService actService) {
		this.actService=actService;
	}

	@Override
	@GetMapping("/{location}")
	public ResponseEntity<Flux<Activity>> viewAllActivities(@PathVariable("location") String location, WebSession session) {
		User loggedUser = (User) session.getAttribute("loggedUser");
		if (loggedUser == null) {
			return ResponseEntity.status(401).body(Flux.empty());
		}
		return ResponseEntity.ok(actService.getAllActivities(location));
	}

}
