package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.beans.Flight;
import com.revature.beans.Flight;
import com.revature.data.FlightDao;
import com.revature.dto.FlightDto;
import com.revature.dto.FlightDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FlightServiceTest {
	
	@InjectMocks
	private FlightServiceImpl service;
	
	@Mock
	private FlightDao flightDao;
	
	private Flight flight;
	
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
		
	}
	
	@Test
	void testGetFlightValid() {
		Mockito.when(flightDao.findByDestinationAndId(flight.getDestination(), flight.getId())).thenReturn(Mono.just(new FlightDto(flight)));
		
		Mono<Flight> monoFlight = service.getFlight(flight.getDestination(), flight.getId());
		
		StepVerifier.create(monoFlight).expectNext(flight).verifyComplete();
		
	}
	
	@Test
	void testGetFlightInvalidLocation() {
		String invalidLocation = "San Diego, CA";
		
		ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
		
		Mockito.when(flightDao.findByDestinationAndId(Mockito.anyString(), Mockito.any())).thenReturn(Mono.empty());
		
		Mono<Flight> monoFlight = service.getFlight(invalidLocation, flight.getId());
		
		StepVerifier.create(monoFlight).expectNextMatches(c -> c.getId() == null).verifyComplete();
		
		Mockito.verify(flightDao).findByDestinationAndId(stringCaptor.capture(), uuidCaptor.capture());
		
		assertEquals(invalidLocation, stringCaptor.getValue(), "Assert that the string passed in is the correct string");
		assertEquals(flight.getId(), uuidCaptor.getValue(), "Assert that the uuid passed is in the correct uuid");
		
	}
	
	@Test
	void testGetFlightInvalidId() {
		UUID invalidId = UUID.randomUUID();
		
		ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
		
		Mockito.when(flightDao.findByDestinationAndId(Mockito.anyString(), Mockito.any())).thenReturn(Mono.empty());
		
		Mono<Flight> monoFlight = service.getFlight(flight.getDestination(), invalidId);
		
		StepVerifier.create(monoFlight).expectNextMatches(c -> c.getId() == null).verifyComplete();
		
		Mockito.verify(flightDao).findByDestinationAndId(stringCaptor.capture(), uuidCaptor.capture());
		
		assertEquals(flight.getDestination(), stringCaptor.getValue(), "Assert that the string passed in is the correct string");
		assertEquals(invalidId, uuidCaptor.getValue(), "Assert that the uuid passed is in the correct uuid");
		
	}
	
	@Test
	void testGetFlightsByLocationValid() {
		Flight flight2 = new Flight();
		flight2.setId(UUID.randomUUID());
		flight2.setId(UUID.randomUUID());
		flight2.setDestination("Los Angeles, CA");
		flight2.setAirline("Test Air");
		flight2.setDepartingDate(LocalDateTime.now());
		flight2.setStartingLocation("Detroit, Michigan");
		flight2.setTicketPrice(99.99);
		flight2.setOpenSeats(20);
		
		FlightDto[] Flights = {new FlightDto(flight), new FlightDto(flight2)};
		
		Mockito.when(flightDao.findByDestination(flight.getDestination())).thenReturn(Flux.fromArray(Flights));
		
		Flux<Flight> fluxFlights = service.getFlightsByDestination(flight.getDestination());
		
		StepVerifier.create(fluxFlights).expectNext(flight).expectNext(flight2).verifyComplete();
		
	}
	
	@Test
	void tetGetFlightsByLocationInvalid() {
		
		Mockito.when(flightDao.findByDestination(flight.getDestination())).thenReturn(Flux.empty());
		
		Flux<Flight> fluxFlights = service.getFlightsByDestination(flight.getDestination());
		
		StepVerifier.create(fluxFlights).expectComplete().verify();
		
	}
}
