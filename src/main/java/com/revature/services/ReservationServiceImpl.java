package com.revature.services;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
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

import reactor.core.publisher.Flux;
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
		return isAvailable(res.getId(), hotel.getId(), hotel.getRoomsAvailable(), ReservationType.HOTEL,
				res.getStarttime(), res.getDuration()).flatMap(b -> {
					// If there are rooms available
					if (Boolean.TRUE.equals(b)) {
						return vacDao.save(new VacationDto(vacation)).flatMap(v -> resDao.save(new ReservationDto(res)))
								.map(rDto -> rDto.getReservation());
					}
					return Mono.empty();
				});

	}

	@Override
	public Mono<Reservation> reserveFlight(Flight flight, Vacation vacation) {

		// Create flight reservation.

		Reservation res = new Reservation();
		res.setCost(flight.getTicketPrice());
		res.setDuration(0);
		res.setId(UUID.randomUUID());
		res.setReservedId(flight.getId());
		res.setReservedName(flight.getAirline());
		res.setStarttime(flight.getDepartingDate());
		res.setType(ReservationType.FLIGHT);
		res.setUsername(vacation.getUsername());
		res.setVacationId(vacation.getId());

		// add reservation
		vacation.getReservations().add(res);
		vacation.setTotal(vacation.getTotal() + res.getCost());

		return isAvailable(res.getId(), flight.getId(), flight.getOpenSeats(), ReservationType.FLIGHT,
				res.getStarttime(), res.getDuration()).flatMap(b -> {
					// If flight is available
					if (Boolean.TRUE.equals(b)) {
						return vacDao.save(new VacationDto(vacation)).flatMap(v -> resDao.save(new ReservationDto(res)))
								.map(rDto -> rDto.getReservation());
					}
					return Mono.empty();
				});

	}

	@Override
	public Mono<Reservation> reserveCar(Car car, Vacation vacation) {
		// Create the car
		Reservation res = new Reservation();

		res.setUsername(vacation.getUsername());
		res.setVacationId(vacation.getId());
		res.setId(UUID.randomUUID());
		res.setReservedId(car.getId());
		res.setReservedName(car.getMake() + car.getModel());
		res.setDuration(vacation.getDuration());
		res.setCost(car.getCostPerDay() * res.getDuration());
		res.setType(ReservationType.CAR);
		res.setStarttime(vacation.getStartTime());

		// Save the reservation and the vacation and return the reservation
		if (Boolean.FALSE.equals(car.getInUse())) {
			// Add the reservation to the vacation
			vacation.getReservations().add(res);
			vacation.setTotal(vacation.getTotal() + res.getCost());
			return vacDao.save(new VacationDto(vacation)).flatMap(v -> resDao.save(new ReservationDto(res)))
					.map(ReservationDto::getReservation);
		}
		return Mono.empty();
	}
	
	@Override
	public Mono<Reservation> findReservation(String resId) {
		UUID id = UUID.fromString(resId);
		log.debug("called findReservation with ID: " + resId);
		return resDao.findByUuid(id).map(res -> {
			Reservation r = res.getReservation() 
					== null ? new Reservation() : res.getReservation();
			log.debug("Returning result: " + r);
			return r;
		});
		
	}
	
	@Override
	public Mono<Reservation> updateReservation(String resId, String status) {
		// Assume user auth and input verification has been performed by
		// controller layer when this function is called
		UUID id = UUID.fromString(resId);
		
		Mono<Reservation> monoRes = resDao.findByUuid(id).flatMap(res -> {
			Reservation r = res.getReservation() == null ? 
					new Reservation() : res.getReservation();
			ReservationStatus resStatus = ReservationStatus.getStatus(status);
			if(r.getId() == null || resStatus == null) {
				log.debug("updateReservation called, no result from DB");
				r = new Reservation();
			}
			
			r.setStatus((resStatus));
			log.debug("Value of r: " + r);

			return Mono.just(r);
		});

			return monoRes.flatMap(res -> {
				if(res.getId() == null) {
					return Mono.just(res);
				}
				
				resDao.save(new ReservationDto(res)).subscribe();
				vacDao.findByUsernameAndId(res.getUsername(), res.getVacationId())
			.map(vac -> {
				Vacation v = vac.getVacation();
				Reservation updatedRes = null;
				for(Reservation reservation : v.getReservations()) {
					if(reservation.getId() == res.getId()) {
						log.debug("Updating reservation status in vacation's list");
						reservation.setStatus(ReservationStatus.getStatus(status));
						updatedRes = reservation;
					}
				}
				return updatedRes;
			});
				return Mono.just(res);
		});
	}
	
	@Override
	public Flux<Reservation> getReservations(String username, String vacId) {
		UUID id = UUID.fromString(vacId);
		return vacDao.findByUsernameAndId(username, id).map(vdto -> {
			Vacation v = vdto == null ? new VacationDto().getVacation() : vdto.getVacation();
			log.debug("getReservations called. Vacation Result: " + v);
			return v;
		}).flatMapMany(v -> {
			log.debug("Reservations List: " + v.getReservations());
			Flux<Reservation> reservations = Flux.fromIterable(v.getReservations());
			return reservations;
		});
	}
	
	@Override
	private Mono<Boolean> isAvailable(UUID resId, UUID id, Integer available, ReservationType type,
			LocalDateTime startTime, Integer duration) {
		log.trace("Checking if reservations are available");

		// Get when the reservation would end
		LocalDateTime endTime = startTime.plus(Period.of(0, 0, duration));

		// Get the number of potential reservation conflicts
		Mono<Integer> intMono = resDao.findAll().map(rDto -> rDto.getReservation()).filter(r -> {
			LocalDateTime rEndTime = r.getStarttime().plus(Period.of(0, 0, duration));
			return !r.getId().equals(resId) && r.getReservedId().equals(id) && r.getType().equals(type)
					&& !r.getStatus().equals(ReservationStatus.CLOSED)
					&& ((!r.getType().equals(ReservationType.FLIGHT)
							&& (!startTime.isAfter(rEndTime) && !endTime.isBefore(r.getStarttime())))
							|| (r.getType().equals(ReservationType.FLIGHT) && startTime.equals(r.getStarttime())));
		}).collectList().map(rDtoList -> rDtoList.size());

		return intMono.map(i -> i < available);
	}

	@Override
	public Mono<Reservation> rescheduleReservation(Reservation res, LocalDateTime startTime, Integer duration) {

		res.setStarttime(startTime);

		return vacDao.findByUsernameAndId(res.getUsername(), res.getVacationId()).flatMap(v -> {
			if (!res.getType().equals(ReservationType.FLIGHT)) {
				Integer oldDur = res.getDuration();
				Double oldCost = res.getCost();
				res.setDuration(duration);
				res.setCost(res.getCost() / oldDur * res.getDuration());
				v.setTotal(v.getTotal() - oldCost + res.getCost());
			}
			log.debug("New Reservation: " + res);
			return vacDao.save(v);
		}).flatMap(v -> {
			switch (res.getType()) {
			case HOTEL:
				return rescheduleHotel(res, v.getDestination());
			case CAR:
				return rescheduleCar(res, v.getDestination());
			case FLIGHT:
				return rescheduleFlight(res, v.getDestination());
			default:
				return Mono.empty();
			}
		});
	}
	
	@Override
	private Mono<Reservation> rescheduleHotel(Reservation res, String location){
		return hotelDao.findByLocationAndId(location, res.getReservedId())
				.flatMap(h -> isAvailable(res.getId(), h.getId(), h.getRoomsAvailable(), ReservationType.HOTEL,
						res.getStarttime(), res.getDuration()).flatMap(b -> {
							// If there are rooms available
							if (Boolean.TRUE.equals(b)) {
								return resDao.save(new ReservationDto(res)).map(rDto -> rDto.getReservation());
							}
							return Mono.empty();
						}));
	}

	@Override
	private Mono<Reservation> rescheduleCar(Reservation res, String location){
		return carDao.findByLocationAndId(location, res.getReservedId())
				.flatMap(c -> isAvailable(res.getId(), c.getId(), 1, ReservationType.CAR, res.getStarttime(),
						res.getDuration()).flatMap(b -> {
							// If there are spots available
							if (Boolean.TRUE.equals(b)) {
								log.debug("The reservation will be rescheduled");
								return resDao.save(new ReservationDto(res)).map(rDto -> rDto.getReservation());
							}
							return Mono.empty();
						}));
	}
	
	@Override
	private Mono<Reservation> rescheduleFlight(Reservation res, String location) {

		return flightDao.findByDestinationAndId(location, res.getReservedId())
				.flatMap(f -> isAvailable(res.getId(), f.getId(), f.getOpenSeats(), ReservationType.FLIGHT,
						res.getStarttime(), res.getDuration()).flatMap(b -> {
							log.debug("Checked if flights were available");
							// If there are rooms available
							if (Boolean.TRUE.equals(b)) {
								log.debug("The reservation will be rescheduled");
								return resDao.save(new ReservationDto(res)).map(rDto -> rDto.getReservation());
							}
							return Mono.empty();
						}));
	}
}
