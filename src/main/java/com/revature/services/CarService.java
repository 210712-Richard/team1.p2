package com.revature.services;

import com.revature.beans.Car;
import com.revature.beans.User;

import reactor.core.publisher.Mono;

public interface CarService {
	public Mono<Car> createCar(String location, String make, String model, Integer year, String rentalPlace, Double costPerDay);

}
