package com.revature.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.beans.Flight;
import com.revature.services.FlightService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/flights")
public class FlightControllerImpl implements FlightController{
	
	private FlightService flightService;

	@Autowired
	public FlightControllerImpl(FlightService flightService) {
		this.flightService = flightService;
	}

	@Override
	@GetMapping("{destination}")
	public ResponseEntity<Flux<Flight>> getFlightsByDestination(@PathVariable("destination") String destination, WebSession session) {
		return ResponseEntity.ok(flightService.getFlightsByDestination(destination));
	}
	
	
	
}