package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Reservation;

import reactor.core.publisher.Mono;

public interface ReservationController {
	public Mono<ResponseEntity<Reservation>> createReservation(Reservation res, WebSession session);
	public Mono<ResponseEntity<Reservation>> updateReservationStatus(String resId, String status, WebSession session);
	
}
