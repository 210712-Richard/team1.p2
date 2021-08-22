package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.beans.Car;
import com.revature.data.CarDao;
import com.revature.dto.CarDto;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class CarServiceTest {
	
	@InjectMocks
	private CarServiceImpl service;
	
	@Mock
	private CarDao carDao;
	
	private Car car;
	
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
	}
	
	@Test
	void testGetCarValid() {
		Mockito.when(carDao.findByLocationAndId(car.getLocation(), car.getId())).thenReturn(Mono.just(new CarDto(car)));
		
		Mono<Car> monoCar = service.getCar(car.getLocation(), car.getId());
		
		StepVerifier.create(monoCar).expectNext(car).verifyComplete();
		
	}
	
	@Test
	void testGetCarInvalidLocation() {
		String invalidLocation = "San Diego, CA";
		
		ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
		
		Mockito.when(carDao.findByLocationAndId(Mockito.anyString(), Mockito.any())).thenReturn(Mono.empty());
		
		Mono<Car> monoCar = service.getCar(invalidLocation, car.getId());
		
		StepVerifier.create(monoCar).expectNextMatches(c -> c.getId() == null).verifyComplete();
		
		Mockito.verify(carDao).findByLocationAndId(stringCaptor.capture(), uuidCaptor.capture());
		
		assertEquals(invalidLocation, stringCaptor.getValue(), "Assert that the string passed in is the correct string");
		assertEquals(car.getId(), uuidCaptor.getValue(), "Assert that the uuid passed is in the correct uuid");
		
	}
	
	@Test
	void testGetCarInvalidId() {
		UUID invalidId = UUID.randomUUID();
		
		ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
		
		Mockito.when(carDao.findByLocationAndId(Mockito.anyString(), Mockito.any())).thenReturn(Mono.empty());
		
		Mono<Car> monoCar = service.getCar(car.getLocation(), invalidId);
		
		StepVerifier.create(monoCar).expectNextMatches(c -> c.getId() == null).verifyComplete();
		
		Mockito.verify(carDao).findByLocationAndId(stringCaptor.capture(), uuidCaptor.capture());
		
		assertEquals(car.getLocation(), stringCaptor.getValue(), "Assert that the string passed in is the correct string");
		assertEquals(invalidId, uuidCaptor.getValue(), "Assert that the uuid passed is in the correct uuid");
		
	}
}
