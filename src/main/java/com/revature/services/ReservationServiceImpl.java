package com.revature.services;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.WebSession;

import com.revature.beans.Car;
import com.revature.beans.Flight;
import com.revature.beans.Hotel;
import com.revature.beans.Reservation;
import com.revature.data.CarDao;
import com.revature.data.FlightDao;
import com.revature.data.HotelDao;
import com.revature.data.ReservationDao;
import com.revature.data.VacationDao;

import reactor.core.publisher.Mono;

@Service
public class ReservationServiceImpl implements ReservationService{
	private static Logger log = LogManager.getLogger(ReservationServiceImpl.class);
	
	private ReservationDao resDao;
	private VacationDao vacDao;
	private HotelDao hotelDao;
	private FlightDao flightDao;
	private CarDao carDao;
	
	@Autowired
	public ReservationServiceImpl(ReservationDao resDao, VacationDao vacDao, HotelDao hotelDao, FlightDao flightDao,
			CarDao carDao) {
		super();
		this.resDao = resDao;
		this.vacDao = vacDao;
		this.hotelDao = hotelDao;
		this.flightDao = flightDao;
		this.carDao = carDao;
	}

	@Override
	public Mono<Reservation> reserveHotel(Hotel hotel, UUID vacId, String username) {
		return null;
	}

	@Override
	public Mono<Reservation> reserveFlight(Flight flight, UUID vacId, String username) {
		return null;
	}

	@Override
	public Mono<Reservation> reserveCar(Car car, UUID vacId, String username) {
		return null;
	}

	@Override
	public Mono<Reservation> confirmReservation(String resId) {
		return null;
	}
	
	
}
