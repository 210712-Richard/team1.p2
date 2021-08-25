package com.revature.services;

import com.revature.beans.Car;
import com.revature.beans.Flight;
import com.revature.beans.Hotel;
import com.revature.beans.Reservation;
import com.revature.beans.Vacation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReservationService {
	public Mono<Reservation> reserveHotel(Hotel hotel, Vacation vacation);
	public Mono<Reservation> reserveFlight(Flight flight, Vacation vacation);
	public Mono<Reservation> reserveCar(Car car, Vacation vacation)	;
	public Mono<Reservation> findReservation(String resId);
	public Mono<Reservation> updateReservation(String resId, String status);
	public Flux<Reservation> getReservations(String username, String vacId);
	public Mono<Reservation> confirmReservation(String resId);
	public Mono<Reservation> resetReservationStatus(String resId);
}
