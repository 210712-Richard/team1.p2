package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.beans.Flight;
import com.revature.beans.Car;
import com.revature.beans.Hotel;
import com.revature.beans.Reservation;
import com.revature.beans.ReservationStatus;
import com.revature.beans.ReservationType;
import com.revature.beans.Vacation;
import com.revature.data.CarDao;
import com.revature.data.FlightDao;
import com.revature.data.HotelDao;
import com.revature.data.ReservationDao;
import com.revature.data.VacationDao;
import com.revature.dto.ReservationDto;
import com.revature.dto.VacationDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ReservationServiceTest {
	// Tells Spring to put mocks into service
	@InjectMocks
	private ReservationServiceImpl service;

	// Tells Spring what to mock
	@Mock
	private ReservationDao resDao;

	@Mock
	private VacationDao vacDao;

	@Mock
	private HotelDao hotelDao;

	@Mock
	private CarDao carDao;

	@Mock
	private FlightDao flightDao;

	private Vacation vac;

	private Hotel hotel;

	private Flight flight;
	private Car car;

	@BeforeAll
	static void beforeAll() {

	}

	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.openMocks(this);

		vac = new Vacation();
		vac.setId(UUID.randomUUID());
		vac.setStartTime(LocalDateTime.now());
		vac.setDuration(5);
		vac.setEndTime(LocalDateTime.now().plus(Period.of(0, 0, vac.getDuration())));
		vac.setPartySize(3);
		vac.setUsername("test");
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
		car.setLocation("testLocation");
		car.setMake("Audi");
		car.setModel("R8");
		car.setYear(2006);
		car.setRentalPlace("testPlace");
		car.setCostPerDay(120.00);
		car.setInUse(false);

	}

	@Test
	void testReserveHotelValidNoReservations() {

		Reservation res = new Reservation();
		res.setUsername(vac.getUsername());
		res.setVacationId(vac.getId());
		res.setId(UUID.randomUUID());
		res.setReservedId(hotel.getId());
		res.setReservedName(hotel.getName());
		res.setDuration(vac.getDuration());
		res.setCost(hotel.getCostPerNight() * res.getDuration());
		res.setType(ReservationType.HOTEL);
		res.setStarttime(vac.getStartTime());

		// Set up the returns
		when(resDao.save(Mockito.any())).thenReturn(Mono.just(new ReservationDto(res)));
		when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		when(resDao.findAll()).thenReturn(Flux.empty());

		Mono<Reservation> resMono = service.reserveHotel(hotel, vac);

		// Check to make sure the reservation was set correctly
		StepVerifier.create(resMono)
				.expectNextMatches(r -> r.getId() != null
						&& r.getCost() == (vac.getDuration() * hotel.getCostPerNight())
						&& hotel.getId().equals(r.getReservedId()) && vac.getDuration().equals(r.getDuration())
						&& hotel.getName().equals(r.getReservedName()) && vac.getStartTime().equals(r.getStarttime())
						&& ReservationStatus.AWAITING.equals(r.getStatus())
						&& ReservationType.HOTEL.equals(r.getType()))
				.verifyComplete();

		assertFalse(vac.getReservations().isEmpty(), "Assert that the reservations list is no longer empty.");
		assertEquals(res.getCost(), vac.getTotal(), "Assert that the reservation cost was set to the vacation total.");
	}

	@Test
	void testReserveHotelValidWithReservations() {

		Reservation res = new Reservation();
		res.setUsername(vac.getUsername());
		res.setVacationId(vac.getId());
		res.setId(UUID.randomUUID());
		res.setReservedId(hotel.getId());
		res.setReservedName(hotel.getName());
		res.setDuration(vac.getDuration());
		res.setCost(hotel.getCostPerNight() * res.getDuration());
		res.setType(ReservationType.HOTEL);
		res.setStarttime(vac.getStartTime());

		Reservation setRes1 = new Reservation();
		setRes1.setUsername("otherTest1");
		setRes1.setVacationId(UUID.randomUUID());
		setRes1.setId(UUID.randomUUID());
		setRes1.setReservedId(hotel.getId());
		setRes1.setReservedName(hotel.getName());
		setRes1.setDuration(vac.getDuration());
		setRes1.setCost(19.99);
		setRes1.setType(ReservationType.HOTEL);
		setRes1.setStarttime(vac.getStartTime().minus(Period.of(0, 0, 1)));
		setRes1.setStatus(ReservationStatus.AWAITING);

		Reservation setRes2 = new Reservation();
		setRes2.setUsername("otherTest2");
		setRes2.setVacationId(UUID.randomUUID());
		setRes2.setId(UUID.randomUUID());
		setRes2.setReservedId(hotel.getId());
		setRes2.setReservedName(hotel.getName());
		setRes2.setDuration(1);
		setRes2.setCost(19.99);
		setRes2.setType(ReservationType.HOTEL);
		setRes2.setStarttime(vac.getStartTime().plus(Period.of(0, 0, 1)));
		setRes2.setStatus(ReservationStatus.AWAITING);

		// This reservation is after the end time of the current reservation, so the
		// reservation should go through
		Reservation setRes3 = new Reservation();
		setRes3.setUsername("otherTest3");
		setRes3.setVacationId(UUID.randomUUID());
		setRes3.setId(UUID.randomUUID());
		setRes3.setReservedId(hotel.getId());
		setRes3.setReservedName(hotel.getName());
		setRes3.setDuration(3);
		setRes3.setCost(19.99);
		setRes3.setType(ReservationType.HOTEL);
		setRes3.setStarttime(vac.getStartTime().plus(Period.of(0, 0, res.getDuration() + 1)));
		setRes3.setStatus(ReservationStatus.AWAITING);

		ReservationDto[] resArray = { new ReservationDto(setRes1), new ReservationDto(setRes2),
				new ReservationDto(setRes3) };

		// Set up the returns
		when(resDao.save(Mockito.any())).thenReturn(Mono.just(new ReservationDto(res)));
		when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		when(resDao.findAll()).thenReturn(Flux.fromArray(resArray));

		Mono<Reservation> resMono = service.reserveHotel(hotel, vac);

		// Check to make sure the reservation was set correctly
		StepVerifier.create(resMono)
				.expectNextMatches(r -> r.getId() != null
						&& r.getCost() == (vac.getDuration() * hotel.getCostPerNight())
						&& hotel.getId().equals(r.getReservedId()) && vac.getDuration().equals(r.getDuration())
						&& hotel.getName().equals(r.getReservedName()) && vac.getStartTime().equals(r.getStarttime())
						&& ReservationStatus.AWAITING.equals(r.getStatus())
						&& ReservationType.HOTEL.equals(r.getType()))
				.verifyComplete();

		assertFalse(vac.getReservations().isEmpty(), "Assert that the reservations list is no longer empty.");
		assertEquals(res.getCost(), vac.getTotal(), "Assert that the reservation cost was set to the vacation total.");
	}

	@Test
	void testReserveHotelInvalid() {

		Reservation setRes1 = new Reservation();
		setRes1.setUsername("otherTest1");
		setRes1.setVacationId(UUID.randomUUID());
		setRes1.setId(UUID.randomUUID());
		setRes1.setReservedId(hotel.getId());
		setRes1.setReservedName(hotel.getName());
		setRes1.setDuration(vac.getDuration());
		setRes1.setCost(19.99);
		setRes1.setType(ReservationType.HOTEL);
		setRes1.setStarttime(vac.getStartTime().minus(Period.of(0, 0, 1)));
		setRes1.setStatus(ReservationStatus.AWAITING);

		Reservation setRes2 = new Reservation();
		setRes2.setUsername("otherTest2");
		setRes2.setVacationId(UUID.randomUUID());
		setRes2.setId(UUID.randomUUID());
		setRes2.setReservedId(hotel.getId());
		setRes2.setReservedName(hotel.getName());
		setRes2.setDuration(1);
		setRes2.setCost(19.99);
		setRes2.setType(ReservationType.HOTEL);
		setRes2.setStarttime(vac.getStartTime().plus(Period.of(0, 0, 1)));
		setRes2.setStatus(ReservationStatus.AWAITING);

		Reservation setRes3 = new Reservation();
		setRes3.setUsername("otherTest3");
		setRes3.setVacationId(UUID.randomUUID());
		setRes3.setId(UUID.randomUUID());
		setRes3.setReservedId(hotel.getId());
		setRes3.setReservedName(hotel.getName());
		setRes3.setDuration(3);
		setRes3.setCost(19.99);
		setRes3.setType(ReservationType.HOTEL);
		setRes3.setStarttime(vac.getStartTime().plus(Period.of(0, 0, 4)));
		setRes3.setStatus(ReservationStatus.AWAITING);

		ReservationDto[] resArray = { new ReservationDto(setRes1), new ReservationDto(setRes2),
				new ReservationDto(setRes3) };

		Reservation res = new Reservation();
		res.setUsername(vac.getUsername());
		res.setVacationId(UUID.randomUUID());
		res.setId(UUID.randomUUID());
		res.setReservedId(hotel.getId());
		res.setReservedName(hotel.getName());
		res.setDuration(vac.getDuration());
		res.setCost(19.99);
		res.setType(ReservationType.HOTEL);
		res.setStarttime(vac.getStartTime());

		when(resDao.save(Mockito.any())).thenReturn(Mono.just(new ReservationDto(res)));
		when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		when(resDao.findAll()).thenReturn(Flux.fromArray(resArray));

		Mono<Reservation> resMono = service.reserveHotel(hotel, vac);

		StepVerifier.create(resMono).expectComplete().verify();

		Mockito.verifyNoInteractions(vacDao);
	}

	@Test
	void testConfirmReservation() {

		Reservation carRes = new Reservation();
		carRes.setType(ReservationType.CAR);
		carRes.setId(UUID.randomUUID());
		carRes.setReservedId(UUID.randomUUID());
		carRes.setVacationId(UUID.randomUUID());
		carRes.setUsername("test");
		carRes.setReservedName("Confirm Res Test");
		carRes.setStarttime(LocalDateTime.now());
		carRes.setCost(200.00);
		carRes.setDuration(4);

		Mockito.when(resDao.findByUuid(carRes.getId())).thenReturn(Mono.just(new ReservationDto(carRes)));

		Mono<Reservation> monoCarRes = service.confirmReservation(carRes.getId().toString());

		StepVerifier.create(monoCarRes).expectNextMatches(res -> res.getStatus() == ReservationStatus.CONFIRMED)
				.verifyComplete();

	}

	// CAR TESTS
	@Test
	void testReserveCarValidNoReservations() {

		Reservation res = new Reservation();
		res.setUsername(vac.getUsername());
		res.setVacationId(vac.getId());
		res.setId(UUID.randomUUID());
		res.setReservedId(car.getId());
		res.setReservedName(car.getMake() + car.getModel());
		res.setDuration(vac.getDuration());
		res.setCost(car.getCostPerDay() * res.getDuration());
		res.setType(ReservationType.CAR);
		res.setStarttime(vac.getStartTime());

		// Set up the returns
		when(resDao.save(Mockito.any())).thenReturn(Mono.just(new ReservationDto(res)));
		when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		when(resDao.findAll()).thenReturn(Flux.empty());

		Mono<Reservation> resMono = service.reserveCar(car, vac);

		// Check to make sure the reservation was set correctly
		StepVerifier.create(resMono)
				.expectNextMatches(r -> r.getId() != null && r.getCost() == (vac.getDuration() * car.getCostPerDay())
						&& car.getId().equals(r.getReservedId()) && vac.getDuration().equals(r.getDuration())
						&& r.getReservedName().equals(car.getMake() + car.getModel())
						&& vac.getStartTime().equals(r.getStarttime())
						&& ReservationStatus.AWAITING.equals(r.getStatus()) && ReservationType.CAR.equals(r.getType()))
				.verifyComplete();

		assertFalse(vac.getReservations().isEmpty(), "Assert that the reservations list is no longer empty.");
		assertEquals(res.getCost(), vac.getTotal(), "Assert that the reservation cost was set to the vacation total.");
	}

	@Test
	void testReserveCarValidWithReservations() {

		Reservation res = new Reservation();
		res.setUsername(vac.getUsername());
		res.setVacationId(vac.getId());
		res.setId(UUID.randomUUID());
		res.setReservedId(car.getId());
		res.setReservedName(car.getMake() + car.getModel());
		res.setDuration(vac.getDuration());
		res.setCost(car.getCostPerDay() * res.getDuration());
		res.setType(ReservationType.CAR);
		res.setStarttime(vac.getStartTime());

		Reservation setRes1 = new Reservation();
		setRes1.setUsername("otherTest1");
		setRes1.setVacationId(UUID.randomUUID());
		setRes1.setId(UUID.randomUUID());
		setRes1.setReservedId(car.getId());
		setRes1.setReservedName(car.getMake() + car.getModel());
		setRes1.setDuration(vac.getDuration());
		setRes1.setCost(19.99);
		setRes1.setType(ReservationType.CAR);
		setRes1.setStarttime(vac.getStartTime().minus(Period.of(0, 0, 1)));
		setRes1.setStatus(ReservationStatus.AWAITING);

		Reservation setRes2 = new Reservation();
		setRes2.setUsername("otherTest2");
		setRes2.setVacationId(UUID.randomUUID());
		setRes2.setId(UUID.randomUUID());
		setRes2.setReservedId(car.getId());
		setRes2.setReservedName(car.getMake() + car.getModel());
		setRes2.setDuration(1);
		setRes2.setCost(19.99);
		setRes2.setType(ReservationType.CAR);
		setRes2.setStarttime(vac.getStartTime().plus(Period.of(0, 0, 1)));
		setRes2.setStatus(ReservationStatus.AWAITING);

		// This reservation is after the end time of the current reservation, so the
		// reservation should go through
		Reservation setRes3 = new Reservation();
		setRes3.setUsername("otherTest3");
		setRes3.setVacationId(UUID.randomUUID());
		setRes3.setId(UUID.randomUUID());
		setRes3.setReservedId(car.getId());
		setRes3.setReservedName(car.getMake() + car.getModel());
		setRes3.setDuration(3);
		setRes3.setCost(19.99);
		setRes3.setType(ReservationType.CAR);
		setRes3.setStarttime(vac.getStartTime().plus(Period.of(0, 0, res.getDuration() + 1)));
		setRes3.setStatus(ReservationStatus.AWAITING);

		ReservationDto[] resArray = { new ReservationDto(setRes1), new ReservationDto(setRes2),
				new ReservationDto(setRes3) };

		// Set up the returns
		when(resDao.save(Mockito.any())).thenReturn(Mono.just(new ReservationDto(res)));
		when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		when(resDao.findAll()).thenReturn(Flux.fromArray(resArray));

		Mono<Reservation> resMono = service.reserveCar(car, vac);

		// Check to make sure the reservation was set correctly
		StepVerifier.create(resMono)
				.expectNextMatches(r -> r.getId() != null && r.getCost() == (vac.getDuration() * car.getCostPerDay())
						&& car.getId().equals(r.getReservedId()) && vac.getDuration().equals(r.getDuration())
						&& r.getReservedName().equals(car.getMake() + car.getModel())
						&& vac.getStartTime().equals(r.getStarttime())
						&& ReservationStatus.AWAITING.equals(r.getStatus()) && ReservationType.CAR.equals(r.getType()))
				.verifyComplete();

		assertFalse(vac.getReservations().isEmpty(), "Assert that the reservations list is no longer empty.");
		assertEquals(res.getCost(), vac.getTotal(), "Assert that the reservation cost was set to the vacation total.");
	}

	@Test
	void testReserveCarInvalid() {

		Reservation res = new Reservation();
		res.setUsername(vac.getUsername());
		res.setVacationId(UUID.randomUUID());
		res.setId(UUID.randomUUID());
		res.setReservedId(car.getId());
		res.setReservedName(car.getMake() + car.getModel());
		res.setDuration(vac.getDuration());
		res.setCost(19.99);
		res.setType(ReservationType.CAR);
		res.setStarttime(vac.getStartTime());

		when(resDao.save(Mockito.any())).thenReturn(Mono.just(new ReservationDto(res)));
		when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		car.setInUse(true);

		Mono<Reservation> resMono = service.reserveCar(car, vac);

		StepVerifier.create(resMono).expectComplete().verify();

		Mockito.verifyNoInteractions(vacDao);
	}
	// END CAR TESTS

	@Test
	void testReserveFlightValidNoReservations() {

		Reservation res = new Reservation();
		res.setUsername(vac.getUsername());
		res.setVacationId(vac.getId());
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setDuration(0);
		res.setCost(flight.getTicketPrice());
		res.setType(ReservationType.FLIGHT);
		res.setStarttime(flight.getDepartingDate());

		// Set up the returns
		when(resDao.save(Mockito.any())).thenReturn(Mono.just(new ReservationDto(res)));
		when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		when(resDao.findAll()).thenReturn(Flux.empty());

		Mono<Reservation> resMono = service.reserveFlight(flight, vac);

		// Check to make sure the reservation was set correctly
		StepVerifier.create(resMono)
				.expectNextMatches(r -> r.getId() != null && r.getCost() == (flight.getTicketPrice())
						&& flight.getId().equals(r.getReservedId()) && r.getDuration() == 0
						&& flight.getAirline().equals(r.getReservedName())
						&& flight.getDepartingDate().equals(r.getStarttime())
						&& ReservationStatus.AWAITING.equals(r.getStatus())
						&& ReservationType.FLIGHT.equals(r.getType()))
				.verifyComplete();

		assertFalse(vac.getReservations().isEmpty(), "Assert that the reservations list is no longer empty.");
		assertEquals(res.getCost(), vac.getTotal(), "Assert that the reservation cost was set to the vacation total.");
	}

	@Test
	void testReserveFlightValidWithReservations() {

		Reservation res = new Reservation();
		res.setUsername(vac.getUsername());
		res.setVacationId(vac.getId());
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setDuration(0);
		res.setCost(flight.getTicketPrice());
		res.setType(ReservationType.FLIGHT);
		res.setStarttime(flight.getDepartingDate());

		Reservation setRes1 = new Reservation();
		setRes1.setUsername("otherTest1");
		setRes1.setVacationId(UUID.randomUUID());
		setRes1.setId(UUID.randomUUID());
		setRes1.setReservedId(flight.getId());
		setRes1.setReservedName(flight.getAirline());
		setRes1.setDuration(0);
		setRes1.setCost(150.00);
		setRes1.setType(ReservationType.FLIGHT);
		setRes1.setStarttime(flight.getDepartingDate().minus(Period.of(0, 0, 1)));
		setRes1.setStatus(ReservationStatus.AWAITING);

		Reservation setRes2 = new Reservation();
		setRes2.setUsername("otherTest2");
		setRes2.setVacationId(UUID.randomUUID());
		setRes2.setId(UUID.randomUUID());
		setRes2.setReservedId(flight.getId());
		setRes2.setReservedName(flight.getAirline());
		setRes2.setDuration(0);
		setRes2.setCost(150.00);
		setRes2.setType(ReservationType.FLIGHT);
		setRes2.setStarttime(flight.getDepartingDate().plus(Period.of(0, 0, 1)));
		setRes2.setStatus(ReservationStatus.AWAITING);

		// This reservation is after the end time of the current reservation, so the
		// reservation should go through
		Reservation setRes3 = new Reservation();
		setRes3.setUsername("otherTest3");
		setRes3.setVacationId(UUID.randomUUID());
		setRes3.setId(UUID.randomUUID());
		setRes3.setReservedId(flight.getId());
		setRes3.setReservedName(flight.getAirline());
		setRes3.setDuration(0);
		setRes3.setCost(150.00);
		setRes3.setType(ReservationType.FLIGHT);
		setRes3.setStarttime(flight.getDepartingDate().plus(Period.of(0, 0, res.getDuration() + 1)));
		setRes3.setStatus(ReservationStatus.AWAITING);

		ReservationDto[] resArray = { new ReservationDto(setRes1), new ReservationDto(setRes2),
				new ReservationDto(setRes3) };

		// Set up the returns
		when(resDao.save(Mockito.any())).thenReturn(Mono.just(new ReservationDto(res)));
		when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		when(resDao.findAll()).thenReturn(Flux.fromArray(resArray));

		Mono<Reservation> resMono = service.reserveFlight(flight, vac);

		// Check to make sure the reservation was set correctly
		StepVerifier.create(resMono)
				.expectNextMatches(r -> r.getId() != null && r.getCost() == (flight.getTicketPrice())
						&& flight.getId().equals(r.getReservedId()) && r.getDuration() == 0
						&& flight.getAirline().equals(r.getReservedName())
						&& flight.getDepartingDate().equals(r.getStarttime())
						&& ReservationStatus.AWAITING.equals(r.getStatus())
						&& ReservationType.FLIGHT.equals(r.getType()))
				.verifyComplete();

		assertFalse(vac.getReservations().isEmpty(), "Assert that the reservations list is no longer empty.");
		assertEquals(res.getCost(), vac.getTotal(), "Assert that the reservation cost was set to the vacation total.");
	}

	@Test
	void testReserveFlightInvalid() {

		Reservation setRes1 = new Reservation();
		setRes1.setUsername("otherTest1");
		setRes1.setVacationId(UUID.randomUUID());
		setRes1.setId(UUID.randomUUID());
		setRes1.setReservedId(flight.getId());
		setRes1.setReservedName(flight.getAirline());
		setRes1.setDuration(0);
		setRes1.setCost(150.00);
		setRes1.setType(ReservationType.FLIGHT);
		setRes1.setStarttime(flight.getDepartingDate());
		setRes1.setStatus(ReservationStatus.AWAITING);

		Reservation setRes2 = new Reservation();
		setRes2.setUsername("otherTest2");
		setRes2.setVacationId(UUID.randomUUID());
		setRes2.setId(UUID.randomUUID());
		setRes2.setReservedId(flight.getId());
		setRes2.setReservedName(flight.getAirline());
		setRes2.setDuration(0);
		setRes2.setCost(150.00);
		setRes2.setType(ReservationType.FLIGHT);
		setRes2.setStarttime(flight.getDepartingDate());
		setRes2.setStatus(ReservationStatus.AWAITING);

		Reservation setRes3 = new Reservation();
		setRes3.setUsername("otherTest3");
		setRes3.setVacationId(UUID.randomUUID());
		setRes3.setId(UUID.randomUUID());
		setRes3.setReservedId(flight.getId());
		setRes3.setReservedName(flight.getAirline());
		setRes3.setDuration(0);
		setRes3.setCost(150.00);
		setRes3.setType(ReservationType.FLIGHT);
		setRes3.setStarttime(flight.getDepartingDate());
		setRes3.setStatus(ReservationStatus.AWAITING);

		ReservationDto[] resArray = { new ReservationDto(setRes1), new ReservationDto(setRes2),
				new ReservationDto(setRes3) };

		Reservation res = new Reservation();
		res.setUsername(vac.getUsername());
		res.setVacationId(UUID.randomUUID());
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setDuration(0);
		res.setCost(150.00);
		res.setType(ReservationType.FLIGHT);
		res.setStarttime(flight.getDepartingDate());

		when(resDao.save(Mockito.any())).thenReturn(Mono.just(new ReservationDto(res)));
		when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		when(resDao.findAll()).thenReturn(Flux.fromArray(resArray));

		Mono<Reservation> resMono = service.reserveFlight(flight, vac);

		StepVerifier.create(resMono).expectComplete().verify();

		Mockito.verifyNoInteractions(vacDao);
	}

}
