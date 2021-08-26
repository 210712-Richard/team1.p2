package com.revature.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.beans.Car;
import com.revature.services.CarService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/cars")
public class CarControllerImpl implements CarController{
	
	private CarService carService;

	@Autowired
	public CarControllerImpl(CarService carService) {
		this.carService = carService;
	}

	@Override
	@GetMapping("{location}")
	public ResponseEntity<Flux<Car>> getCarsByLocation(@PathVariable("location") String location, WebSession session) {
		return ResponseEntity.ok(carService.getCarsByLocation(location));
	}
	
	
	
}
