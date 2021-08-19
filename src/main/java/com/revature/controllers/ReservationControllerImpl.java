package com.revature.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class ReservationControllerImpl implements ReservationController{
	
	private ReservationService resService;
	
	@Autowired
	public ReservationControllerImpl(ReservationService resService) {
		this.resService = resService;
	}

	@Override
	@LoggedInMono
	public Mono<ResponseEntity<Reservation>> reserveHotel(Hotel hotel, WebSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@LoggedInMono
	public Mono<ResponseEntity<Reservation>> reserveFlight(Flight flight, WebSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@LoggedInMono
	public Mono<ResponseEntity<Reservation>> reserveCar(Car car, WebSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@LoggedInMono
	public Mono<ResponseEntity<Reservation>> confirmReservation(String resId, WebSession session) {
		// TODO Auto-generated method stub
		return null;
	}
}
