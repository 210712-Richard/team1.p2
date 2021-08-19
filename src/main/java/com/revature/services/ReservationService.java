package com.revature.services;

import java.util.UUID;

import com.revature.beans.Car;
import com.revature.beans.Flight;
import com.revature.beans.Hotel;
import com.revature.beans.Reservation;

import reactor.core.publisher.Mono;

public interface ReservationService {
	public Mono<Reservation> reserveHotel(Hotel hotel, UUID vacId, String username);
	public Mono<Reservation> reserveFlight(Flight flight, UUID vacId, String username);
	public Mono<Reservation> reserveCar(Car car, UUID vacId, String username);
	public Mono<Reservation> confirmReservation(String resId);
}
