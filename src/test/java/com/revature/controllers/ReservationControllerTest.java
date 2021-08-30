package com.revature.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ReservationControllerTest {

	@InjectMocks
	private ReservationControllerImpl controller;

	@Mock
	private UserService userService;

	@Mock
	private ReservationService resService;

	@Mock
	private HotelService hotelService;

	@Mock
	private CarService carService;

	@Mock
	private FlightService flightService;

	private WebSession session;

	private Vacation vac;
	private Hotel hotel;
	private Car car;
	private Flight flight;
	private User user;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);

		user = new User();
		user.setUsername("test");
		user.setPassword("password");
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("test@email.com");
		user.setBirthday(LocalDate.now());
		user.setType(UserType.VACATIONER);

		vac = new Vacation();
		vac.setId(UUID.randomUUID());
		vac.setStartTime(LocalDateTime.now());
		vac.setDuration(5);
		vac.setEndTime(LocalDateTime.now().plus(Period.of(0, 0, vac.getDuration())));
		vac.setPartySize(3);
		vac.setUsername(user.getUsername());
		vac.setDestination("Test City, Test State");

		hotel = new Hotel();
		hotel.setId(UUID.randomUUID());
		hotel.setName("Test Hotel");
		hotel.setLocation("Test City, Test State");
		hotel.setRoomsAvailable(3);
		hotel.setCostPerNight(20.00);

		flight = new Flight();
		flight.setId(UUID.randomUUID());
		flight.setAirline("Test Airline");
		flight.setDestination("Test City, Test State");
		flight.setOpenSeats(3);
		flight.setTicketPrice(150.00);
		flight.setDepartingDate(LocalDateTime.now());
		flight.setStartingLocation("Test City1, Test State1");

		car = new Car();
		car.setId(UUID.randomUUID());
		car.setLocation("Test City, Test State");
		car.setMake("Audi");
		car.setModel("R8");
		car.setYear(2006);
		car.setRentalPlace("testPlace");
		car.setCostPerDay(120.00);
		car.setInUse(false);

		session = Mockito.mock(WebSession.class);
	}

	@Test
	void testCreateReservationHotelValid() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(hotel.getId());
		res.setReservedName(hotel.getName());
		res.setStartTime(vac.getStartTime());
		res.setCost(hotel.getCostPerNight());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(vac));
		Mockito.when(hotelService.getHotel(hotel.getLocation(), hotel.getId())).thenReturn(Mono.just(hotel));
		Mockito.when(resService.reserveHotel(hotel, vac)).thenReturn(Mono.just(res));

		Mono<ResponseEntity<Reservation>> monoRes = controller.createReservation(res, session);

		StepVerifier.create(monoRes).expectNext(ResponseEntity.ok(res)).verifyComplete();
	}

	@Test
	void testCreateReservationHotelInvalidNoVacation() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(hotel.getId());
		res.setReservedName(hotel.getName());
		res.setStartTime(vac.getStartTime());
		res.setCost(hotel.getCostPerNight());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(new Vacation()));
		Mockito.when(hotelService.getHotel(hotel.getLocation(), hotel.getId())).thenReturn(Mono.just(hotel));

		Mono<ResponseEntity<Reservation>> monoRes = controller.createReservation(res, session);

		StepVerifier.create(monoRes).expectNext(ResponseEntity.notFound().build()).verifyComplete();

		Mockito.verifyNoInteractions(resService);
	}

	@Test
	void testCreateReservationHotelInvalidNoHotel() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(hotel.getId());
		res.setReservedName(hotel.getName());
		res.setStartTime(vac.getStartTime());
		res.setCost(hotel.getCostPerNight());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(vac));
		Mockito.when(hotelService.getHotel(hotel.getLocation(), hotel.getId())).thenReturn(Mono.just(new Hotel()));

		Mono<ResponseEntity<Reservation>> monoRes = controller.createReservation(res, session);

		StepVerifier.create(monoRes).expectNext(ResponseEntity.notFound().build()).verifyComplete();

		Mockito.verifyNoInteractions(resService);
	}

	@Test
	void testCreateReservationCarValid() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(car.getId());
		res.setReservedName(car.getMake());
		res.setStartTime(vac.getStartTime());
		res.setCost(car.getCostPerDay());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.CAR);

		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(vac));
		Mockito.when(carService.getCar(car.getLocation(), car.getId())).thenReturn(Mono.just(car));
		Mockito.when(resService.reserveCar(car, vac)).thenReturn(Mono.just(res));

		Mono<ResponseEntity<Reservation>> monoRes = controller.createReservation(res, session);

		StepVerifier.create(monoRes).expectNext(ResponseEntity.ok(res)).verifyComplete();
	}

	@Test
	void testCreateReservationCarInvalidNoVacation() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(car.getId());
		res.setReservedName(car.getMake());
		res.setStartTime(vac.getStartTime());
		res.setCost(car.getCostPerDay());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.CAR);

		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(new Vacation()));
		Mockito.when(carService.getCar(car.getLocation(), car.getId())).thenReturn(Mono.just(car));

		Mono<ResponseEntity<Reservation>> monoRes = controller.createReservation(res, session);

		StepVerifier.create(monoRes).expectNext(ResponseEntity.notFound().build()).verifyComplete();

		Mockito.verifyNoInteractions(resService);
	}

	@Test
	void testCreateReservationCarInvalidNoCar() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(car.getId());
		res.setReservedName(car.getMake());
		res.setStartTime(vac.getStartTime());
		res.setCost(car.getCostPerDay());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.CAR);

		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(vac));
		Mockito.when(carService.getCar(car.getLocation(), car.getId())).thenReturn(Mono.just(new Car()));

		Mono<ResponseEntity<Reservation>> monoRes = controller.createReservation(res, session);

		StepVerifier.create(monoRes).expectNext(ResponseEntity.notFound().build()).verifyComplete();

		Mockito.verifyNoInteractions(resService);
	}

	@Test
	void testCreateReservationFlightValid() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.FLIGHT);

		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(vac));
		Mockito.when(flightService.getFlight(flight.getDestination(), flight.getId())).thenReturn(Mono.just(flight));
		Mockito.when(resService.reserveFlight(flight, vac)).thenReturn(Mono.just(res));

		Mono<ResponseEntity<Reservation>> monoRes = controller.createReservation(res, session);

		StepVerifier.create(monoRes).expectNext(ResponseEntity.ok(res)).verifyComplete();
	}

	@Test
	void testCreateReservationFlightInvalidNoVacation() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.FLIGHT);

		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(new Vacation()));
		Mockito.when(flightService.getFlight(flight.getDestination(), flight.getId())).thenReturn(Mono.just(flight));

		Mono<ResponseEntity<Reservation>> monoRes = controller.createReservation(res, session);

		StepVerifier.create(monoRes).expectNext(ResponseEntity.notFound().build()).verifyComplete();

		Mockito.verifyNoInteractions(resService);
	}

	@Test
	void testCreateReservationFlightInvalidNoFlight() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.FLIGHT);

		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(vac));
		Mockito.when(flightService.getFlight(flight.getDestination(), flight.getId()))
				.thenReturn(Mono.just(new Flight()));

		Mono<ResponseEntity<Reservation>> monoRes = controller.createReservation(res, session);

		StepVerifier.create(monoRes).expectNext(ResponseEntity.notFound().build()).verifyComplete();

		Mockito.verifyNoInteractions(resService);
	}

	@Test
	void testCreateReservationInvalidBadRequest() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();

		StepVerifier.create(controller.createReservation(null, session)).expectNext(ResponseEntity.badRequest().build())
				.verifyComplete();

		StepVerifier.create(controller.createReservation(res, session)).expectNext(ResponseEntity.badRequest().build())
				.verifyComplete();

	}

	@Test
	void testRescheduleReservationValidVacationerNonFlight() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(vac.getStartTime().plus(Period.of(0, 0, 5)));
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(vac.getDuration() + 1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);

		Mockito.when(resService.getReservation(res.getId())).thenReturn(Mono.just(res));
		Mockito.when(resService.rescheduleReservation(res, null, returnRes.getStartTime(), returnRes.getDuration()))
				.thenReturn(Mono.just(returnRes));

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.ok(returnRes)).verifyComplete();
	}
	
	@Test
	void testRescheduleReservationValidVacationerFlight() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.FLIGHT);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(flight.getId());
		returnRes.setStartTime(null);
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(null);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.FLIGHT);

		Mockito.when(resService.getReservation(res.getId())).thenReturn(Mono.just(res));
		Mockito.when(resService.rescheduleReservation(res, returnRes.getReservedId(), returnRes.getStartTime(), returnRes.getDuration()))
				.thenReturn(Mono.just(returnRes));

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.ok(returnRes)).verifyComplete();
	}
	
	@Test
	void testRescheduleReservationValidStaff() {
		user.setType(UserType.HOTEL_STAFF);
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(vac.getStartTime().plus(Period.of(0, 0, 5)));
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(vac.getDuration() + 1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);

		Mockito.when(resService.getReservation(res.getId())).thenReturn(Mono.just(res));
		Mockito.when(resService.rescheduleReservation(res, null, returnRes.getStartTime(), returnRes.getDuration()))
				.thenReturn(Mono.just(returnRes));

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.ok(returnRes)).verifyComplete();
	}
	
	@Test
	void testRescheduleReservationInvalidStaffWrongStaff() {
		
		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(vac.getStartTime().plus(Period.of(0, 0, 5)));
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(vac.getDuration() + 1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);
		
		
		user.setUsername("carStaff");
		user.setType(UserType.CAR_STAFF);
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);



		Mockito.when(resService.getReservation(res.getId())).thenReturn(Mono.just(res));
		Mockito.when(resService.rescheduleReservation(res, null, returnRes.getStartTime(), returnRes.getDuration()))
				.thenReturn(Mono.just(returnRes));

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.status(403).build()).verifyComplete();
	}
	
	@Test
	void testRescheduleReservationInvalidVacationerFlightDurationStartTime() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.FLIGHT);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(vac.getStartTime().plus(Period.of(0, 0, 5)));
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(vac.getDuration() + 1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.FLIGHT);

		Mockito.when(resService.getReservation(res.getId())).thenReturn(Mono.just(res));
		Mockito.when(resService.rescheduleReservation(res, null, returnRes.getStartTime(), returnRes.getDuration()))
				.thenReturn(Mono.just(returnRes));

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.status(403).build()).verifyComplete();
	}
	
	@Test
	void testRescheduleReservationInvalidReservationStatus() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);
		res.setStatus(ReservationStatus.CONFIRMED);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(vac.getStartTime().plus(Period.of(0, 0, 5)));
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(vac.getDuration() + 1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);

		Mockito.when(resService.getReservation(res.getId())).thenReturn(Mono.just(res));
		Mockito.when(resService.rescheduleReservation(res, null, returnRes.getStartTime(), returnRes.getDuration()))
				.thenReturn(Mono.just(returnRes));

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.status(403).build()).verifyComplete();
	}
	
	@Test
	void testRescheduleReservationInvalidConflict() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(vac.getStartTime().plus(Period.of(0, 0, 5)));
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(vac.getDuration() + 1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);

		Mockito.when(resService.getReservation(res.getId())).thenReturn(Mono.just(res));
		Mockito.when(resService.rescheduleReservation(res, null, returnRes.getStartTime(), returnRes.getDuration()))
				.thenReturn(Mono.empty());

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.status(409).build()).verifyComplete();
	}
	
	@Test
	void testRescheduleReservationInvalidStartTimeNull() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(null);
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(vac.getDuration() + 1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.badRequest().build()).verifyComplete();

		Mockito.verifyNoInteractions(resService);
	}
	
	@Test
	void testRescheduleReservationInvalidDurationNull() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(vac.getStartTime());
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(null);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);


		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.badRequest().build()).verifyComplete();
	
		Mockito.verifyNoInteractions(resService);
	}
	
	@Test
	void testRescheduleReservationInvalidAllNull() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(null);
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(null);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);

		

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.badRequest().build()).verifyComplete();

		Mockito.verifyNoInteractions(resService);

	}
	
	@Test
	void testRescheduleReservationInvalidStartTimeBeforeNow() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(LocalDateTime.now().minus(Period.of(0, 0, 1)));
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(vac.getDuration() + 1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.badRequest().build()).verifyComplete();
	
		Mockito.verifyNoInteractions(resService);
	}
	
	@Test
	void testRescheduleReservationDurationNegative() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(vac.getStartTime());
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(-1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.badRequest().build()).verifyComplete();
	
		Mockito.verifyNoInteractions(resService);
	}
	
	@Test
	void testRescheduleReservationInvalidNotFound() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);

		Reservation returnRes = new Reservation();
		returnRes.setId(res.getId());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(vac.getStartTime().plus(Period.of(0, 0, 5)));
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(vac.getDuration() + 1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);

		Mockito.when(resService.getReservation(res.getId())).thenReturn(Mono.just(new Reservation()));

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, res.getId().toString(),
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.notFound().build()).verifyComplete();
	}
	
	@Test
	void testRescheduleReservationInvalidBadUUID() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);
		
		String badId = "Bad ID";
		Reservation returnRes = new Reservation();
		returnRes.setId(UUID.randomUUID());
		returnRes.setReservedName(flight.getAirline());
		returnRes.setReservedId(null);
		returnRes.setStartTime(vac.getStartTime().plus(Period.of(0, 0, 5)));
		returnRes.setCost(flight.getTicketPrice());
		returnRes.setVacationId(vac.getId());
		returnRes.setDuration(vac.getDuration() + 1);
		returnRes.setUsername(user.getUsername());
		returnRes.setType(ReservationType.HOTEL);

		Mono<ResponseEntity<Reservation>> monoRes = controller.rescheduleReservation(returnRes, badId,
				session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.badRequest().build()).verifyComplete();
		
		Mockito.verifyNoInteractions(resService);
	}
	
	@Test
	void testUpdateReservationStatusValidCar() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(car.getId());
		res.setReservedName(car.getMake());
		res.setStartTime(vac.getStartTime());
		res.setCost(car.getCostPerDay());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.CAR);
		res.setStatus(ReservationStatus.AWAITING);	
		
		Reservation updatedRes = new Reservation();
		updatedRes.setId(res.getId());
		updatedRes.setReservedId(car.getId());
		updatedRes.setReservedName(car.getMake());
		updatedRes.setStartTime(vac.getStartTime());
		updatedRes.setCost(car.getCostPerDay());
		updatedRes.setVacationId(vac.getId());
		updatedRes.setDuration(vac.getDuration());
		updatedRes.setUsername(user.getUsername());
		updatedRes.setType(ReservationType.CAR);		
		updatedRes.setStatus(ReservationStatus.CONFIRMED);		
		
		Mockito.when(resService.getReservation(res.getId()))
		.thenReturn(Mono.just(res));
		
		Mockito.when(resService.updateReservation(res, "CONFIRMED"))
		.thenReturn(Mono.just(updatedRes));

		Mono<ResponseEntity<Reservation>> monoRes = controller.
				updateReservationStatus(res, res.getId().toString(), session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.ok(updatedRes)).expectComplete();
	}
	
	@Test
	void testUpdateReservationStatusValidHotel() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(hotel.getId());
		res.setReservedName(hotel.getName());
		res.setStartTime(vac.getStartTime());
		res.setCost(hotel.getCostPerNight());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.HOTEL);
		res.setStatus(ReservationStatus.AWAITING);	
		
		Reservation updatedRes = new Reservation();
		updatedRes.setId(res.getId());
		updatedRes.setReservedId(hotel.getId());
		updatedRes.setReservedName(hotel.getName());
		updatedRes.setStartTime(vac.getStartTime());
		updatedRes.setCost(hotel.getCostPerNight());
		updatedRes.setVacationId(vac.getId());
		updatedRes.setDuration(vac.getDuration());
		updatedRes.setUsername(user.getUsername());
		updatedRes.setType(ReservationType.HOTEL);		
		updatedRes.setStatus(ReservationStatus.CONFIRMED);		
		
		Mockito.when(resService.getReservation(res.getId()))
		.thenReturn(Mono.just(res));
		
		Mockito.when(resService.updateReservation(res, "CONFIRMED"))
		.thenReturn(Mono.just(updatedRes));

		Mono<ResponseEntity<Reservation>> monoRes = controller.
				updateReservationStatus(res, res.getId().toString(), session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.ok(updatedRes)).expectComplete();
	}
		
	@Test
	void testUpdateReservationStatusValidFlight() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.FLIGHT);
		res.setStatus(ReservationStatus.AWAITING);	
		
		Reservation updatedRes = new Reservation();
		updatedRes.setId(res.getId());
		updatedRes.setReservedId(flight.getId());
		updatedRes.setReservedName(flight.getAirline());
		updatedRes.setStartTime(vac.getStartTime());
		updatedRes.setCost(flight.getTicketPrice());
		updatedRes.setVacationId(vac.getId());
		updatedRes.setDuration(vac.getDuration());
		updatedRes.setUsername(user.getUsername());
		updatedRes.setType(ReservationType.FLIGHT);		
		updatedRes.setStatus(ReservationStatus.CONFIRMED);		
		
		Mockito.when(resService.getReservation(res.getId()))
		.thenReturn(Mono.just(res));
		
		Mockito.when(resService.updateReservation(res, "CONFIRMED"))
		.thenReturn(Mono.just(updatedRes));

		Mono<ResponseEntity<Reservation>> monoRes = controller.
				updateReservationStatus(res, res.getId().toString(), session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.ok(updatedRes)).expectComplete();
	}
	
	@Test
	void testUpdateReservationStatusValidStaff() {
		User staff = new User();
		staff.setUsername("carTest");
		staff.setPassword("password");
		staff.setFirstName("Car");
		staff.setLastName("User");
		staff.setEmail("cartest@email.com");
		staff.setBirthday(LocalDate.now());
		staff.setType(UserType.CAR_STAFF);
		
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(staff);

		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(car.getId());
		res.setReservedName(car.getMake());
		res.setStartTime(vac.getStartTime());
		res.setCost(car.getCostPerDay());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.CAR);
		res.setStatus(ReservationStatus.CONFIRMED);	

		
		Reservation updatedRes = new Reservation();
		updatedRes.setId(res.getId());
		updatedRes.setReservedId(car.getId());
		updatedRes.setReservedName(car.getMake());
		updatedRes.setStartTime(vac.getStartTime());
		updatedRes.setCost(car.getCostPerDay());
		updatedRes.setVacationId(vac.getId());
		updatedRes.setDuration(vac.getDuration());
		updatedRes.setUsername(user.getUsername());
		updatedRes.setType(ReservationType.CAR);		
		updatedRes.setStatus(ReservationStatus.CLOSED);	
		
		Mockito.when(resService.getReservation(res.getId()))
		.thenReturn(Mono.just(res));
		
		Mockito.when(resService.updateReservation(res, "CLOSED"))
		.thenReturn(Mono.just(updatedRes));

		Mono<ResponseEntity<Reservation>> monoRes = controller.
				updateReservationStatus(res, res.getId().toString(), session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.ok(updatedRes)).expectComplete();
	}
	
	@Test
	void testUpdateReservationStatusInvalidStaff() {
		User staff = new User();
		staff.setUsername("carTest");
		staff.setPassword("password");
		staff.setFirstName("Car");
		staff.setLastName("User");
		staff.setEmail("cartest@email.com");
		staff.setBirthday(LocalDate.now());
		staff.setType(UserType.CAR_STAFF);
		
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(staff);
		
		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStartTime(vac.getStartTime());
		res.setCost(flight.getTicketPrice());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.FLIGHT);
		res.setStatus(ReservationStatus.AWAITING);	
		
		
		Mockito.when(resService.getReservation(res.getId()))
		.thenReturn(Mono.just(res));
		
		Mono<ResponseEntity<Reservation>> monoRes = controller.
				updateReservationStatus(res, res.getId().toString(), session);
		
		StepVerifier.create(monoRes).expectNext(ResponseEntity.status(403).build()).expectComplete();

	}
	
	@Test
	void testGetReservationsByTypeValid() {
		User staff = new User();
		staff.setUsername("carTest");
		staff.setPassword("password");
		staff.setFirstName("Car");
		staff.setLastName("User");
		staff.setEmail("cartest@email.com");
		staff.setBirthday(LocalDate.now());
		staff.setType(UserType.CAR_STAFF);
		
		Reservation res = new Reservation();
		res.setId(UUID.randomUUID());
		res.setReservedId(car.getId());
		res.setReservedName(car.getMake());
		res.setStartTime(vac.getStartTime());
		res.setCost(car.getCostPerDay());
		res.setVacationId(vac.getId());
		res.setDuration(vac.getDuration());
		res.setUsername(user.getUsername());
		res.setType(ReservationType.CAR);
		res.setStatus(ReservationStatus.CONFIRMED);	
		
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(staff);
		Mockito.when(resService.getReservationsByType(res.getType())).thenReturn(Flux.just(res));
		
		ResponseEntity<Flux<Reservation>> resFluxEntity = controller.getReservationsByType(session);
		
		assertEquals(200, resFluxEntity.getStatusCodeValue(), "Assert that the status code is 200");
		
		StepVerifier.create(resFluxEntity.getBody()).expectNext(res).verifyComplete();
	}
	
	@Test
	void testGetReservationsByTypeInvalid() {

		
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(null);
		
		ResponseEntity<Flux<Reservation>> resFluxEntity = controller.getReservationsByType(session);
		
		assertEquals(500, resFluxEntity.getStatusCodeValue(), "Assert that the status code is 500");
		
		Mockito.verifyNoInteractions(resService);
		
	}
	
}
