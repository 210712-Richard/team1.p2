package com.revature.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.beans.Reservation;
import com.revature.beans.ReservationStatus;
import com.revature.beans.ReservationType;
import com.revature.data.CarDao;
import com.revature.data.FlightDao;
import com.revature.data.HotelDao;
import com.revature.data.ReservationDao;
import com.revature.data.UserDao;
import com.revature.data.VacationDao;
import com.revature.dto.ReservationDto;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


public class ReservationServiceTests {

	// Tells Spring to put mocks into service
	@InjectMocks
	private ReservationServiceImpl service;

	// Tells Spring what to mock
	@Mock
	private UserDao userDao;
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

	private Reservation carRes;
	private Reservation flightRes;
	private Reservation hotelRes;
	private Reservation vacRes;
	
	@BeforeAll
	public static void beforeAll() {

	}

	@BeforeEach
	public void beforeEach() {
		// Initializes the mocks
		MockitoAnnotations.openMocks(this);

		service = new ReservationServiceImpl(resDao, vacDao, hotelDao, flightDao, carDao);

	}


}
