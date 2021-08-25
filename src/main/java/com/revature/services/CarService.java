package com.revature.services;

import java.util.UUID;

import com.revature.beans.Car;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarService {
	public Mono<Car> getCar(String location, UUID id);
	public Flux<Car> getCarsByLocation(String location);

}
