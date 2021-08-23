package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Car;

import reactor.core.publisher.Flux;

public interface CarController {
	
	public ResponseEntity<Flux<Car>> getCarsByLocation(String location, WebSession session);
}
