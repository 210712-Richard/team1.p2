package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Car;
import com.revature.beans.User;

import reactor.core.publisher.Mono;

public interface CarController {
	public Mono<ResponseEntity<Car>> rentCar(Car car, WebSession session);
}
