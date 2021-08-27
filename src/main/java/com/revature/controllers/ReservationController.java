package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Reservation;

import reactor.core.publisher.Mono;

public interface ReservationController {
	/**
	 * Create a reservation from a valid reservable, valid vacation, and valid type
	 * @param res The reservation with fields to add<br>
	 * reservedId: The Id of the reservable
	 * vacationId: The Id of the vacation
	 * type: The type of the reservable
	 * @param session The WebSession
	 * @return A Mono containing the new reservation
	 */
	public Mono<ResponseEntity<Reservation>> createReservation(Reservation res, WebSession session);
	public Mono<ResponseEntity<Reservation>> confirmReservation(String resId, WebSession session);
	public Mono<ResponseEntity<Reservation>> rescheduleReservation(Reservation res, String resId, WebSession session);
}
