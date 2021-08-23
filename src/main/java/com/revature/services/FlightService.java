package com.revature.services;

import java.util.UUID;

import com.revature.beans.Flight;

import reactor.core.publisher.Mono;

public interface FlightService {
	public Mono<Flight> getFlight(String location, UUID id);
}
