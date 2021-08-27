package com.revature.services;

import java.time.LocalDateTime;
import java.util.UUID;

import com.revature.beans.Car;
import com.revature.beans.Flight;
import com.revature.beans.Hotel;
import com.revature.beans.Reservation;
import com.revature.beans.Vacation;

import reactor.core.publisher.Flux;
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

	/**
	 * Get a reservation
	 * @param resId The id of the reservation
	 * @return The reservation
	 */
	public Mono<Reservation> getReservation(UUID resId);
	
	/**
	 * Update a reservation status
	 * @param res The reservation being updated
	 * @param status The new status of the reservation
	 * @return The updated reservation
	 */
	public Mono<Reservation> updateReservation(Reservation res, String status);
	
	/**
	 * Get all the reservations in a vacation
	 * @param username The username of the user
	 * @param vacId The id of the vacation
	 * @return
	 */
	public Flux<Reservation> getReservations(String username, String vacId);
	
	/**
	 * Reschedule a reservation
	 * @param res The reservation being changed
	 * @param newReservedId The new reserved id. Used for vacationers to reschedule flights.
	 * @param startTime The new start time. If reserved id is set and it is a flight, will not be used
	 * @param duration The new duration. If reserved id is set and it is a flight, will not be used
	 * @return The rescheduled reservation
	 */
	public Mono<Reservation> rescheduleReservation(Reservation res, UUID newReservedId, LocalDateTime startTime, Integer duration);
}