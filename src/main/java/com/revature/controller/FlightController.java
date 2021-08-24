package com.revature.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Flight;

import reactor.core.publisher.Flux;

public interface FlightController {
	
	public ResponseEntity<Flux<Flight>> getFlightsByDestination(String destination, WebSession session);
}