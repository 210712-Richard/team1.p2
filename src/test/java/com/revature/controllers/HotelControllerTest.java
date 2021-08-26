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

import com.revature.beans.Hotel;
import com.revature.services.HotelService;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class HotelControllerTest {
	@InjectMocks
	private HotelControllerImpl controller;
	
	@Mock
	private HotelService hotelService;
	
	private Hotel hotel;
	
	private WebSession session;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		
		hotel = new Hotel();
		hotel.setId(UUID.randomUUID());
		hotel.setLocation("Los Angeles, CA");
		hotel.setName("Test Hotel");
		hotel.setRoomsAvailable(20);
		hotel.setCostPerNight(59.99);
		
		session = Mockito.mock(WebSession.class);
	}
	
	@Test
	void testGetHotelsByLocationNonEmptyList() {
		Mockito.when(hotelService.getHotelsByLocation(hotel.getLocation())).thenReturn(Flux.just(hotel));
		
		ResponseEntity<Flux<Hotel>> fluxHotelEntity = controller.getHotelsByLocation(hotel.getLocation(), session);
		
		assertEquals(200, fluxHotelEntity.getStatusCodeValue(), "Assert that the status code is 200");
		
		StepVerifier.create(fluxHotelEntity.getBody()).expectNext(hotel).verifyComplete();
	}
	
	@Test
	void testGetHotelsByLocationEmptyList() {
		Mockito.when(hotelService.getHotelsByLocation(hotel.getLocation())).thenReturn(Flux.empty());
		
		ResponseEntity<Flux<Hotel>> fluxHotelEntity = controller.getHotelsByLocation(hotel.getLocation(), session);
		
		assertEquals(200, fluxHotelEntity.getStatusCodeValue(), "Assert that the status code is 200");
		
		StepVerifier.create(fluxHotelEntity.getBody()).verifyComplete();
	}
}
