package com.revature.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Car;
import com.revature.services.CarService;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class CarControllerTest {
	@InjectMocks
	private CarControllerImpl controller;
	
	@Mock
	private CarService carService;
	
	private Car car;
	
	private WebSession session;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		
		car = new Car();
		car.setId(UUID.randomUUID());
		car.setLocation("Los Angeles, CA");
		car.setMake("Ford");
		car.setModel("Mustang");
		car.setYear(1980);
		car.setRentalPlace("Enterprise");
		car.setCostPerDay(199.99);
		car.setInUse(false);
		
		session = Mockito.mock(WebSession.class);
	}
	
	@Test
	void testGetCarsByLocationNonEmptyList() {
		Mockito.when(carService.getCarsByLocation(car.getLocation())).thenReturn(Flux.just(car));
		
		ResponseEntity<Flux<Car>> fluxCarEntity = controller.getCarsByLocation(car.getLocation(), session);
		
		assertEquals(200, fluxCarEntity.getStatusCodeValue(), "Assert that the status code is 200");
		
		StepVerifier.create(fluxCarEntity.getBody()).expectNext(car).verifyComplete();
	}
	
	@Test
	void testGetCarsByLocationEmptyList() {
		Mockito.when(carService.getCarsByLocation(car.getLocation())).thenReturn(Flux.empty());
		
		ResponseEntity<Flux<Car>> fluxCarEntity = controller.getCarsByLocation(car.getLocation(), session);
		
		assertEquals(200, fluxCarEntity.getStatusCodeValue(), "Assert that the status code is 200");
		
		StepVerifier.create(fluxCarEntity.getBody()).verifyComplete();
	}
}