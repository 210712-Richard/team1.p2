package com.revature.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Flight;
import com.revature.services.FlightService;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FlightControllerTest {
	@InjectMocks
	private FlightControllerImpl controller;
	
	@Mock
	private FlightService flightService;
	
	private Flight flight;
	
	private WebSession session;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		
		flight = new Flight();
		flight.setId(UUID.randomUUID());
		flight.setDestination("Los Angeles, CA");
		flight.setAirline("Test Air");
		flight.setDepartingDate(LocalDateTime.now());
		flight.setStartingLocation("Detroit, Michigan");
		flight.setTicketPrice(99.99);
		flight.setOpenSeats(20);
		
		session = Mockito.mock(WebSession.class);
	}
	
	@Test
	void testGetFlightsByDestinationNonEmptyList() {
		Mockito.when(flightService.getFlightsByDestination(flight.getDestination())).thenReturn(Flux.just(flight));
		
		ResponseEntity<Flux<Flight>> fluxFlightEntity = controller.getFlightsByDestination(flight.getDestination(), session);
		
		assertEquals(200, fluxFlightEntity.getStatusCodeValue(), "Assert that the status code is 200");
		
		StepVerifier.create(fluxFlightEntity.getBody()).expectNext(flight).verifyComplete();
	}
	
	@Test
	void testGetFlightsByDestinationEmptyList() {
		Mockito.when(flightService.getFlightsByDestination(flight.getDestination())).thenReturn(Flux.empty());
		
		ResponseEntity<Flux<Flight>> fluxFlightEntity = controller.getFlightsByDestination(flight.getDestination(), session);
		
		assertEquals(200, fluxFlightEntity.getStatusCodeValue(), "Assert that the status code is 200");
		
		StepVerifier.create(fluxFlightEntity.getBody()).verifyComplete();
	}
}