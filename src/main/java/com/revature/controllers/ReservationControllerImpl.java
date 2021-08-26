package com.revature.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.revature.beans.ReservationStatus;
import com.revature.beans.ReservationType;
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

	@LoggedInMono
	@PostMapping
	public Mono<ResponseEntity<Reservation>> createReservation(@RequestBody Reservation res, WebSession session) {

		// Make sure the user is a vacationer account
		User loggedUser = (User) session.getAttribute(UserController.LOGGED_USER);
		String username = loggedUser != null ? loggedUser.getUsername() : "";

		// Make sure the required fields are not null
		if (res == null || res.getReservedId() == null || res.getVacationId() == null || res.getType() == null) {
			return Mono.just(ResponseEntity.status(400).build());
		}
		// Get the vacation and make sure the id is correct
		Mono<Vacation> monoVac = userService.getVacation(username, res.getVacationId());

		// Need to do different operations based on what is being reserved
		switch (res.getType()) {
		// Used to see if we are reserving a hotel
		case HOTEL:
			// Check to make sure the vacation and hotel exist before making the reservation
			Mono<Hotel> monoHotel = userService.getVacation(username, res.getVacationId())
					// If the vacation wasn't found, just return an empty Hotel object
					.flatMap(v -> v.getId() != null ? hotelService.getHotel(v.getDestination(), res.getReservedId())
							: Mono.just(new Hotel()));

			// Zip the two monos together and return the response entity needed
			return monoVac.zipWith(monoHotel).flatMap(t -> {
				Hotel hotel = t.getT2();
				log.debug("Hotel returned: " + hotel);
				Vacation vac = t.getT1();
				log.debug("Vacation obtained: " + vac);
				// If the vacation id is null, it means no valid vacation was found.
				if (vac.getId() == null || hotel.getId() == null) {
					return Mono.just(ResponseEntity.notFound().build());
					// A vacation was found, so a reservation can be made.
				} else {
					return resService.reserveHotel(hotel, vac).map(ResponseEntity::ok)
							.switchIfEmpty(Mono.just(ResponseEntity.status(409).build()));
				}
			});
		// Used to see if we're reserving a flight
		case FLIGHT:
			// Check to make sure the vacation and flight exist before making the
			// reservation
			Mono<Flight> monoFlight = userService.getVacation(username, res.getVacationId())
					// If the vacation wasn't found, just return an empty Flight object
					.flatMap(v -> v.getId() != null ? flightService.getFlight(v.getDestination(), res.getReservedId())
							: Mono.just(new Flight()));

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
					return resService.reserveFlight(flight, vac).map(ResponseEntity::ok)
							.switchIfEmpty(Mono.just(ResponseEntity.status(409).build()));
				}
			});
		// Used to see if we're reserving a car
		case CAR:
			// Check to make sure the vacation and flight exist before making the
			// reservation
			Mono<Car> monoCar = userService.getVacation(username, res.getVacationId())
					// If the vacation wasn't found, just return an empty Car object
					.flatMap(v -> v.getId() != null ? carService.getCar(v.getDestination(), res.getReservedId())
							: Mono.just(new Car()));

			// Zip the two monos together and return the response entity needed
			return monoVac.zipWith(monoCar).flatMap(t -> {
				Car car = t.getT2();
				log.debug("Car returned: " + car);
				Vacation vac = t.getT1();
				log.debug("Vacation received: " + vac);
				// If the vacation id is null, it means no valid vacation was found.
				if (vac.getId() == null || car.getId() == null) {
					return Mono.just(ResponseEntity.notFound().build());
					// A vacation was found, so a reservation can be made.
				} else {
					return resService.reserveCar(car, vac).map(ResponseEntity::ok)
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
	public Mono<ResponseEntity<Reservation>> updateReservationStatus(@RequestBody Reservation resStatus,
			@PathVariable("resId") String resId, WebSession session) {

		if (resId == null || resId.equals("") || resStatus.getStatus() == null)
			return Mono.just(ResponseEntity.badRequest().build());

		String status = resStatus.getStatus().toString();

		User loggedUser = session.getAttribute(UserController.LOGGED_USER);
		if (loggedUser == null)
			return Mono.just(ResponseEntity.status(401).build());

		if (loggedUser.getType() == UserType.VACATIONER
				&& status.equalsIgnoreCase(ReservationStatus.AWAITING.toString()))
			return Mono.just(ResponseEntity.status(403).build());

		log.debug("calling find reservation");
		Mono<Reservation> monoRes = resService.findReservation(resId);

		return monoRes.single().flatMap(r -> {
			if (r.getId() == null)
				return Mono.just(ResponseEntity.notFound().build());

			ReservationType type = r.getType();

			// If reservation status is already closed, do not allow users confirm
			if (r.getStatus() == ReservationStatus.CLOSED && loggedUser.getType() == UserType.VACATIONER) {
				return Mono.just(ResponseEntity.status(403).build());
			}

			log.debug("Reservation Type: " + type);
			log.debug("Logged Username: " + loggedUser.getUsername());
			log.debug("Logged user type: " + loggedUser.getType());

			// If logged in user didn't create reservation and is not staff
			if (!loggedUser.getUsername().equals(r.getUsername())
					&& !loggedUser.getType().toString().split("_")[0].equals(r.getType().toString()))
				return Mono.just(ResponseEntity.status(403).build());

			// If reservation start time already passed
			if (r.getStarttime().isBefore(LocalDateTime.now())
					&& !loggedUser.getType().toString().split("_")[0].equals(r.getType().toString()))
				
				return Mono.just(ResponseEntity.badRequest().build());

			return resService.updateReservation(r, status).flatMap(res -> {
				if (res.getId() == null) {
					// Invalid resId
					return Mono.just(ResponseEntity.notFound().build());
				}
				log.debug("Reservation matching ID: " + r.getId() + " updated\n" + r);
				return Mono.just(ResponseEntity.ok(r));
			});
		});
	}

	@LoggedInMono
	@PatchMapping("{resId}")
	public Mono<ResponseEntity<Reservation>> rescheduleReservation(@RequestBody Reservation res,
			@PathVariable("resId") String resId, WebSession session) {

		User loggedUser = session.getAttribute(UserController.LOGGED_USER);
		UUID id = null;

		// Make sure the res id is a uuid
		try {
			id = UUID.fromString(resId);
			log.debug("Reservation ID: %s", resId);
		} catch (Exception e) {
			return Mono.just(ResponseEntity.badRequest().build());
		}

		// Need to first get the reservation
		return resService.getReservation(id).flatMap(r -> {
			log.debug("Reservation received: %s", r);

			// If no reservation was found, return a 404
			if (r.getId() == null) {
				log.debug("No reservation found");
				return Mono.just(ResponseEntity.notFound().build());
			}

			// If the user is allowed to change the reservation, change the reservation and
			// send back the reservation
			else if (ReservationStatus.AWAITING.equals(r.getStatus())
					&& (r.getUsername().equals(loggedUser.getUsername())
							&& (r.getType().equals(ReservationType.FLIGHT) || res.getId() != null))
					|| r.getType().toString().equals(loggedUser.getType().toString().split("_")[0])) {
				log.debug("Reservation has been found and user can change startTime and duration");
				return resService.rescheduleReservation(r, res.getReservedId(), res.getStarttime(), res.getDuration())
						.map(re -> ResponseEntity.ok(re))
						// If an empty mono was returned, that means there is a scheduling conflict
						.switchIfEmpty(Mono.just(ResponseEntity.status(409).build()));
			}

			// The user cannot change this reservation
			log.debug("User cannot change startTime and duration for this reservation");
			return Mono.just(ResponseEntity.status(403).build());
		});
	}
}
