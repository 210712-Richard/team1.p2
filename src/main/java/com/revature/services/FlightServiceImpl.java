package com.revature.services;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.beans.Flight;
import com.revature.data.FlightDao;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightServiceImpl implements FlightService {
	private static Logger log = LogManager.getLogger(FlightServiceImpl.class);

	private FlightDao flightDao;

	@Autowired
	public FlightServiceImpl(FlightDao flightDao) {
		this.flightDao = flightDao;
	}

	@Override
	public Mono<Flight> getFlight(String destination, UUID id) {
		return flightDao.findByDestinationAndId(destination, id).map(fDto -> {
			Flight flight = fDto.getFlight();
			log.debug("Flight found: {}", flight);
			return flight;
		}).switchIfEmpty(Mono.just(new Flight()));
	}
	
	@Override
	public Flux<Flight> getFlightsByDestination(String destination) {
		return flightDao.findByDestination(destination).map(fDto -> {
			Flight flight = fDto.getFlight();
			log.debug("Flight found: {}", flight);
			return flight;
		});
	}

}
