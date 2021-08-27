package com.revature.services;

import java.util.UUID;

import com.revature.beans.Flight;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlightService {
	/**
	 * Get a specific flight
	 * @param destination The destination of the flight
	 * @param id The id of the flight
	 * @return The flight
	 */
	public Mono<Flight> getFlight(String destination, UUID id);
	
	/**
	 * Get all the flights for a destination
	 * @param destination The destination of the flights
	 * @return The flights in the destination
	 */
	public Flux<Flight> getFlightsByDestination(String destination);
}
