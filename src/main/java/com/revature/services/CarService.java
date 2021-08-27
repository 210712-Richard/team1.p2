package com.revature.services;

import java.util.UUID;

import com.revature.beans.Car;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarService {
	/**
	 * Get a specific car
	 * @param location The location of the car
	 * @param id The id of the car
	 * @return The car
	 */
	public Mono<Car> getCar(String location, UUID id);
	
	/**
	 * Get all the cars for a location
	 * @param location The location of the cars
	 * @return The cars in the location
	 */
	public Flux<Car> getCarsByLocation(String location);

}
