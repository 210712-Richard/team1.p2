package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.aspects.LoggedInMono;
import com.revature.beans.Car;
import com.revature.beans.Flight;
import com.revature.beans.Hotel;
import com.revature.beans.Reservation;
import com.revature.services.ReservationService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reservations")
public class ReservationControllerImpl implements ReservationController {
	private static Logger log = LogManager.getLogger(ReservationControllerImpl.class);

	private ReservationService resService;

	@Autowired
	public ReservationControllerImpl(ReservationService resService) {
		this.resService = resService;
	}

	@Override
	@LoggedInMono
	@PostMapping("{vacId}/hotel")
	public Mono<ResponseEntity<Reservation>> reserveHotel(@RequestBody Hotel hotel, @PathVariable("vacId") String vacId,
			WebSession session) {
		return null;
	}

	@Override
	@LoggedInMono
	@PostMapping("{vacId}/flight")
	public Mono<ResponseEntity<Reservation>> reserveFlight(@RequestBody Flight flight,
			@PathVariable("vacId") String vacId, WebSession session) {
		return null;
	}

	@Override
	@LoggedInMono
	@PostMapping("{vacId}/car")
	public Mono<ResponseEntity<Reservation>> reserveCar(@RequestBody Car car, @PathVariable("vacId") String vacId,
			WebSession session) {
		return null;
	}

	@Override
	@LoggedInMono
	@PutMapping("{vacId}/status")
	public Mono<ResponseEntity<Reservation>> confirmReservation(@PathVariable("vacId") String resId,
			WebSession session) {
		return null;
	}
}
