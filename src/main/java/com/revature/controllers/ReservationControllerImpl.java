package com.revature.controllers;

import java.util.UUID;

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
import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.services.ReservationService;
import com.revature.services.UserService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reservations")
public class ReservationControllerImpl implements ReservationController {
	private static Logger log = LogManager.getLogger(ReservationControllerImpl.class);

	private ReservationService resService;
	private UserService userService;

	@Autowired
	public ReservationControllerImpl(ReservationService resService, UserService userService) {
		this.resService = resService;
		this.userService = userService;
	}

	@Override
	@LoggedInMono
	@PostMapping("hotel/{vacId}")
	public Mono<ResponseEntity<Reservation>> reserveHotel(@RequestBody Hotel hotel, @PathVariable("vacId") String vacId,
			WebSession session) {

		// Make sure the user is a vacationer account
		User loggedUser = (User) session.getAttribute("loggedUser");
		if (loggedUser == null || !UserType.VACATIONER.equals(loggedUser.getType())) {
			return Mono.just(ResponseEntity.status(403).build());
		}

		// Get the UUID from the path. If it isn't valid, send back a 400
		UUID vId = null;
		try {
			vId = UUID.fromString(vacId);
			log.debug("Vacation Id from path: " + vId);
		} catch (Exception e) {
			return Mono.just(ResponseEntity.status(400).build());
		}

		// Check to make sure the vacation exists before making the reservation
		return userService.getVacation(loggedUser.getUsername(), vId).flatMap(v -> {
			//If the vacation id is null, it means no valid vacation was found.
			if (v.getId() == null) {
				return Mono.just(ResponseEntity.notFound().build());
			//A vacation was found, so a reservation can be made.
			} else {
				return resService.reserveHotel(hotel, v)
						.map(r -> ResponseEntity.ok(r))
						.switchIfEmpty(Mono.just(ResponseEntity.status(409).build()));
			}
		});
	}

	@Override
	@LoggedInMono
	@PostMapping("flight/{vacId}")
	public Mono<ResponseEntity<Reservation>> reserveFlight(@RequestBody Flight flight,
			@PathVariable("vacId") String vacId, WebSession session) {
		return null;
	}

	@Override
	@LoggedInMono
	@PostMapping("car/{vacId}")
	public Mono<ResponseEntity<Reservation>> reserveCar(@RequestBody Car car, @PathVariable("vacId") String vacId,
			WebSession session) {
		return null;
	}

	@Override
	@LoggedInMono
	@PutMapping("{resId}/status")
	public Mono<ResponseEntity<Reservation>> confirmReservation(@PathVariable("resId") String resId,
			WebSession session) {
		return null;
	}
}
