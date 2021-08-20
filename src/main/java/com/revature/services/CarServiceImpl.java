package com.revature.services;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.beans.Car;
import com.revature.data.CarDao;
import com.revature.dto.CarDto;

import reactor.core.publisher.Mono;

public class CarServiceImpl implements CarService {
	private static Logger log = LogManager.getLogger(UserServiceImpl.class);

	CarDao carDao;
	
	@Autowired
	public CarServiceImpl(CarDao carDao) {
		this.carDao=carDao;
	}

	@Override
	public Mono<Car> createCar(String location, String make, String model, Integer year, String rentalPlace,
			Double costPerDay) {
		Car car = new Car();
		car.setLocation(location);
		car.setMake(make);
		car.setModel(model);
		car.setYear(year);
		car.setRentalPlace(rentalPlace);
		car.setCostPerDay(costPerDay);
		car.setId(UUID.randomUUID());
		car.setInUse(true);
		return carDao.save(new CarDto(car)).map(cDto -> cDto.getCar());
	}

}
