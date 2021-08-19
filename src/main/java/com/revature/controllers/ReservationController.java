package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Car;
import com.revature.beans.Flight;
import com.revature.beans.Hotel;
import com.revature.beans.Reservation;

import reactor.core.publisher.Mono;

public interface ReservationController {
	public Mono<ResponseEntity<Reservation>> reserveHotel(Hotel hotel, String vacId, WebSession session);
	public Mono<ResponseEntity<Reservation>> reserveFlight(Flight flight, String vacId, WebSession session);
	public Mono<ResponseEntity<Reservation>> reserveCar(Car car, String vacId, WebSession session);
	public Mono<ResponseEntity<Reservation>> confirmReservation(String resId, WebSession session);
}