package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Flight;

import reactor.core.publisher.Flux;

public interface FlightController {
	
	/**
	 * Get all the flights by a location
	 * @param location The location to lookup
	 * @param session The WebSession
	 * @return A ResponseEntity of a Flux of flights
	 */
	public ResponseEntity<Flux<Flight>> getFlightsByDestination(String destination, WebSession session);
}
