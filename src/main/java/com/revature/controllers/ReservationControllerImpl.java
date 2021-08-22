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
import com.revature.beans.ReservationStatus;
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
	@PostMapping("{resId}/hotel")
	public Mono<ResponseEntity<Reservation>> reserveHotel(@RequestBody Hotel hotel, @PathVariable("resId") String resId,
			WebSession session) {
		return null;
	}

	@Override
	@LoggedInMono
	@PostMapping("{resId}/flight")
	public Mono<ResponseEntity<Reservation>> reserveFlight(@RequestBody Flight flight,
			@PathVariable("resId") String resId, WebSession session) {
		return null;
	}

	@Override
	@LoggedInMono
	@PostMapping("{resId}/car")
	public Mono<ResponseEntity<Reservation>> reserveCar(@RequestBody Car car, @PathVariable("resId") String vacId,
			WebSession session) {
		return null;
	}

	@Override
	@LoggedInMono
	@PutMapping("{resId}/status")
	public Mono<ResponseEntity<Reservation>> confirmReservation(@PathVariable("resId") String resId, WebSession session) {
		if(resId == null || resId.equals("")) 
			return Mono.just(ResponseEntity.badRequest().build());
		
		return resService.confirmReservation(resId).single().map(res -> {
			log.debug("resevation result from DB: " + res);
			if(res.getReservedId() == null) 
				return ResponseEntity.notFound().build();
					
			log.debug("resevation updated: " + res);			
			return ResponseEntity.ok(res);
		});
	}
}
