package com.revature.controllers;

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
import org.springframework.web.server.WebSession;

import com.revature.beans.Car;
import com.revature.beans.Flight;
import com.revature.beans.Hotel;
import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.services.CarService;
import com.revature.services.FlightService;
import com.revature.services.HotelService;
import com.revature.services.ReservationService;
import com.revature.services.UserService;

public class ReservationControllerTest {
	
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
		
		session = Mockito.mock(WebSession.class);
	}
	
	@Test
	void testCreateReservationHotelValid() {
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);
	}
}
