package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.beans.Car;
import com.revature.beans.User;
import com.revature.services.CarService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cars")
public class CarControllerImpl implements CarController {
	private static Logger log = LogManager.getLogger(UserControllerImpl.class);

	CarService carService;
	
	@Autowired
	public CarControllerImpl(CarService carService) {
		this.carService=carService;
	}
	
	@PutMapping
	public Mono<ResponseEntity<Car>> rentCar(@RequestBody Car c, WebSession session) {		
		return carService.createCar(c.getLocation(), c.getMake(), c.getModel(), c.getYear(),
				c.getRentalPlace(), c.getCostPerDay())
				.map(car -> ResponseEntity.status(201).body(car));
	}

}
