package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Car;

import reactor.core.publisher.Flux;

public interface CarController {
	
	/**
	 * Get all the cars by a location
	 * @param location The location to lookup
	 * @param session The WebSession
	 * @return A ResponseEntity of a Flux of cars
	 */
	public ResponseEntity<Flux<Car>> getCarsByLocation(String location, WebSession session);
}
