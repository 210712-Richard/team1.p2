package com.revature.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Reservation;

import reactor.core.publisher.Mono;

public interface ReservationController {
	public Mono<ResponseEntity<Reservation>> createReservation(Reservation res, WebSession session);
	public Mono<ResponseEntity<Reservation>> confirmReservation(String resId, WebSession session);
	public Mono<ResponseEntity<Reservation>> rescheduleReservation(Reservation res, String resId, WebSession session);
}
