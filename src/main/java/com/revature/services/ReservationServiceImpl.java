package com.revature.services;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.beans.Car;
import com.revature.beans.Flight;
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
import reactor.core.publisher.Mono;

@Service
public class ReservationServiceImpl implements ReservationService {
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
	public Mono<Reservation> reserveHotel(Hotel hotel, Vacation vacation) {
		// Create the reservation
		Reservation res = new Reservation();

		res.setUsername(vacation.getUsername());
		res.setVacationId(vacation.getId());
		res.setId(UUID.randomUUID());
		res.setReservedId(hotel.getId());
		res.setReservedName(hotel.getName());
		res.setDuration(vacation.getDuration());
		res.setCost(hotel.getCostPerNight() * res.getDuration());
		res.setType(ReservationType.HOTEL);
		res.setStarttime(vacation.getStartTime());

		// Add the reservation to the vacation
		vacation.getReservations().add(res);
		vacation.setTotal(vacation.getTotal() + res.getCost());

		// Save the reservation and the vacation and return the reservation
		return isAvailable(hotel.getId(), hotel.getRoomsAvailable(), ReservationType.HOTEL, res.getStarttime(), res.getDuration())
				.flatMap(b -> {
					//If there are rooms available
					if (b) {
						return vacDao.save(new VacationDto(vacation))
							.flatMap(v -> resDao.save(new ReservationDto(res)))
							.map(rDto -> rDto.getReservation());
					}
					return Mono.empty();
				});
				
	}

	@Override
	public Mono<Reservation> reserveFlight(Flight flight, Vacation vacation) {
		return null;
	}

	@Override
	public Mono<Reservation> reserveCar(Car car, Vacation vacation) {
		// Create the car
				Reservation res = new Reservation();

				res.setUsername(vacation.getUsername());
				res.setVacationId(vacation.getId());
				res.setId(UUID.randomUUID());
				res.setReservedId(car.getId());
				res.setReservedName(car.getMake()+car.getModel());
				res.setDuration(vacation.getDuration());
				res.setCost(car.getCostPerDay() * res.getDuration());
				res.setType(ReservationType.CAR);
				res.setStarttime(vacation.getStartTime());

				// Save the reservation and the vacation and return the reservation
				if (Boolean.FALSE.equals(car.getInUse())) {
					// Add the reservation to the vacation
					vacation.getReservations().add(res);
					vacation.setTotal(vacation.getTotal() + res.getCost());
					return vacDao.save(new VacationDto(vacation))
							.flatMap(v -> resDao.save(new ReservationDto(res)))
							.map(ReservationDto::getReservation);
				}
				return Mono.empty();
	}

	@Override
	public Mono<Reservation> confirmReservation(String resId) {
		UUID id = UUID.fromString(resId);
		return resDao.findByUuid(id).map(res -> {
				Reservation r = res.getReservation();
				
				if(ReservationStatus.CLOSED != r.getStatus()) {
					r.setStatus(ReservationStatus.CONFIRMED);
					ReservationDto rdto = new ReservationDto(r);
					resDao.save(rdto);
					return r;
				}
	
				return null;
				
		}).switchIfEmpty(Mono.just(new Reservation()));
	}
	
	
	// For testing purposes mainly
	public Mono<Reservation> resetReservationStatus (String resId) {
		UUID id = UUID.fromString(resId);
		return resDao.findByUuid(id).single().map(res -> {
				Reservation r = res.getReservation();
				
				r.setStatus(ReservationStatus.AWAITING);
				ReservationDto rdto = new ReservationDto(r);
				resDao.save(rdto);
				return r;
	
		}).switchIfEmpty(Mono.just(new Reservation()));
	}

	private Mono<Boolean> isAvailable(UUID id, Integer available, ReservationType type, LocalDateTime startTime, Integer duration) {
		// Get when the reservation would end
		LocalDateTime endTime = startTime.plus(Period.of(0, 0, duration));
		
		//Get the number of potential reservation conflicts
		Mono<Integer> intMono = resDao.findAll().map(rDto -> rDto.getReservation())
				.filter(r -> {
					LocalDateTime rEndTime = r.getStarttime().plus(Period.of(0, 0, duration));
					return r.getReservedId().equals(id) 
					&& r.getType().equals(type)
					&& !r.getStatus().equals(ReservationStatus.CLOSED)
					&& ((rEndTime.isAfter(startTime) && !rEndTime.isAfter(endTime))
							|| (r.getStarttime().isBefore(endTime) 
									&& !r.getStarttime().isBefore(startTime)));
				})
				.collectList()
				.map(rDtoList -> rDtoList.size());
		
		return intMono.map(i -> i < available);
	}
}
