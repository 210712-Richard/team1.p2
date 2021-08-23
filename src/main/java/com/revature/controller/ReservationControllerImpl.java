package com.revature.controller;

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
import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.services.CarService;
import com.revature.services.FlightService;
import com.revature.services.HotelService;
import com.revature.services.ReservationService;
import com.revature.services.UserService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reservations")
public class ReservationControllerImpl implements ReservationController {
	private static Logger log = LogManager.getLogger(ReservationControllerImpl.class);

	private ReservationService resService;
	private UserService userService;
	private HotelService hotelService;
	private FlightService flightService;
	private CarService carService;

	@Autowired
	public ReservationControllerImpl(ReservationService resService, UserService userService, HotelService hotelService,
			FlightService flightService, CarService carService) {
		this.resService = resService;
		this.userService = userService;
		this.hotelService = hotelService;
		this.flightService = flightService;
		this.carService = carService;
	}

	@Override
	@LoggedInMono
	@PostMapping
	public Mono<ResponseEntity<Reservation>> createReservation(@RequestBody Reservation res, WebSession session) {

		// Make sure the user is a vacationer account
		User loggedUser = (User) session.getAttribute("loggedUser");
		if (loggedUser == null || !UserType.VACATIONER.equals(loggedUser.getType())) {
			return Mono.just(ResponseEntity.status(403).build());
		}
		
		//Make sure the required fields are not null
		if (res == null || res.getReservedId() == null || res.getVacationId() == null || res.getType() == null) {
				return Mono.just(ResponseEntity.status(400).build());
		}
		//Get the vacation and make sure the id is correct
		Mono<Vacation> monoVac = userService.getVacation(loggedUser.getUsername(), res.getVacationId());
		
		//Need to do different operations based on what is being reserved
		switch (res.getType()) {
		// Used to see if we are reserving a hotel
		case HOTEL:
			// Check to make sure the vacation and hotel exist before making the reservation
			Mono<Hotel> monoHotel = userService.getVacation(loggedUser.getUsername(), res.getVacationId())
					.flatMap(v -> {
						// If the vacation wasn't found, just return an empty Hotel object
						return v.getId() != null ? hotelService.getHotel(v.getDestination(), res.getReservedId())
								: Mono.just(new Hotel());
					});

			
			// Zip the two monos together and return the response entity needed
			return monoVac.zipWith(monoHotel).flatMap(t -> {
				Hotel hotel = t.getT2();
				log.debug("Hotel returned: " + hotel);
				Vacation vac = t.getT1();
				log.debug("Vacation returned: " + vac);
				// If the vacation id is null, it means no valid vacation was found.
				if (vac.getId() == null || hotel.getId() == null) {
					return Mono.just(ResponseEntity.notFound().build());
					// A vacation was found, so a reservation can be made.
				} else {
					return resService.reserveHotel(hotel, vac).map(r -> ResponseEntity.ok(r))
							.switchIfEmpty(Mono.just(ResponseEntity.status(409).build()));
				}
			});
		// Used to see if we're reserving a flight
		case FLIGHT:
			// Check to make sure the vacation and flight exist before making the
			// reservation
			Mono<Flight> monoFlight = userService.getVacation(loggedUser.getUsername(), res.getVacationId())
					.flatMap(v -> {
						// If the vacation wasn't found, just return an empty Hotel object
						return v.getId() != null ? flightService.getFlight(v.getDestination(), res.getReservedId())
								: Mono.just(new Flight());
					});

			// Zip the two monos together and return the response entity needed
			return monoVac.zipWith(monoFlight).flatMap(t -> {
				Flight flight = t.getT2();
				log.debug("Flight returned: " + flight);
				Vacation vac = t.getT1();
				log.debug("Vacation returned: " + vac);
				// If the vacation id is null, it means no valid vacation was found.
				if (vac.getId() == null || flight.getId() == null) {
					return Mono.just(ResponseEntity.notFound().build());
					// A vacation was found, so a reservation can be made.
				} else {
					return resService.reserveFlight(flight, vac).map(r -> ResponseEntity.ok(r))
							.switchIfEmpty(Mono.just(ResponseEntity.status(409).build()));
				}
			});
		// Used to see if we're reserving a car
		case CAR:
			// Check to make sure the vacation and flight exist before making the
			// reservation
			Mono<Car> monoCar = userService.getVacation(loggedUser.getUsername(), res.getVacationId()).flatMap(v -> {
				// If the vacation wasn't found, just return an empty Hotel object
				return v.getId() != null ? carService.getCar(v.getDestination(), res.getReservedId())
						: Mono.just(new Car());
			});

			// Zip the two monos together and return the response entity needed
			return monoVac.zipWith(monoCar).flatMap(t -> {
				Car car = t.getT2();
				log.debug("Car returned: " + car);
				Vacation vac = t.getT1();
				log.debug("Vacation returned: " + vac);
				// If the vacation id is null, it means no valid vacation was found.
				if (vac.getId() == null || car.getId() == null) {
					return Mono.just(ResponseEntity.notFound().build());
					// A vacation was found, so a reservation can be made.
				} else {
					return resService.reserveCar(car, vac).map(r -> ResponseEntity.ok(r))
							.switchIfEmpty(Mono.just(ResponseEntity.status(409).build()));
				}
			});
		default:
			return Mono.just(ResponseEntity.status(400).build());
		}
	}

	@Override
	@LoggedInMono
	@PutMapping("{resId}/status")
	public Mono<ResponseEntity<Reservation>> confirmReservation(@PathVariable("resId") String resId,
			WebSession session) {
		return null;
	}
}
