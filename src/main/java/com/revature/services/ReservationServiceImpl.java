package com.revature.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.data.CarDao;
import com.revature.data.FlightDao;
import com.revature.data.HotelDao;
import com.revature.data.ReservationDao;
import com.revature.data.VacationDao;

@Service
public class ReservationServiceImpl {
	
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
	
	
}
