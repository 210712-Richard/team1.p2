package com.revature.services;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.beans.Car;
import com.revature.data.CarDao;

import reactor.core.publisher.Mono;

@Service
public class CarServiceImpl implements CarService {

	private static Logger log = LogManager.getLogger(HotelServiceImpl.class);

	private CarDao carDao;
	
	@Autowired
	public CarServiceImpl(CarDao carDao) {
		this.carDao = carDao;
	}

	@Override
	public Mono<Car> getCar(String destination, UUID id) {
		return carDao.findByLocationAndId(destination, id).map(cDto -> cDto.getCar());
	}
}