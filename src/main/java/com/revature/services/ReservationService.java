package com.revature.services;

import java.time.LocalDateTime;
import java.util.UUID;

import com.revature.beans.Car;
import com.revature.beans.Flight;
import com.revature.beans.Hotel;
import com.revature.beans.Reservation;
import com.revature.beans.Vacation;

import reactor.core.publisher.Mono;

public interface ReservationService {
	/**
	 * Create a reservation for a hotel
	 * @param hotel The hotel being reserved
	 * @param vacation The vacation the reservation will be added to
	 * @return The new reservation
	 */
	public Mono<Reservation> reserveHotel(Hotel hotel, Vacation vacation);
	
	/**
	 * Create a reservation for a flight
	 * @param flight The flight being reserved
	 * @param vacation The vacation the reservation will be added to
	 * @return The new reservation
	 */
	public Mono<Reservation> reserveFlight(Flight flight, Vacation vacation);
	
	/**
	 * Create a reservation for a car
	 * @param car The car being reserved
	 * @param vacation The vacation the reservation will be added to
	 * @return The new reservation
	 */
	public Mono<Reservation> reserveCar(Car car, Vacation vacation);
	public Mono<Reservation> confirmReservation(String resId);
	public Mono<Reservation> resetReservationStatus(String resId);
	public Mono<Reservation> getReservation(UUID resId);
	public Mono<Reservation> rescheduleReservation(Reservation res, UUID newReservedId, LocalDateTime startTime, Integer duration);
}

