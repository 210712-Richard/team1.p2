package com.revature.services;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;

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
import com.revature.dto.FlightDto;
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
			return Flux.fromIterable(v.getReservations());
		});
	}
	
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

		return intMono.map(i -> {
			log.debug("Amount in database for " + type + ": " + i);
			return i < available;
		});
	}

	@Override
	public Mono<Reservation> getReservation(UUID resId) {
		return resDao.findByUuid(resId).map(r -> r.getReservation()).switchIfEmpty(Mono.just(new Reservation()));
	}

	@Override
	public Mono<Reservation> rescheduleReservation(Reservation res, UUID newReservedId, LocalDateTime startTime,
			Integer duration) {

		return vacDao.findByUsernameAndId(res.getUsername(), res.getVacationId()).flatMap(v -> {
			switch (res.getType()) {
			case HOTEL:
				return rescheduleHotel(res, v, startTime, duration);
			case CAR:
				return rescheduleCar(res, v, startTime, duration);
			case FLIGHT:
				if (newReservedId != null) {
					log.debug("Rescheduling flight by id");
					return rescheduleFlight(res, v, newReservedId, res.getStarttime(), 0);
				}
				return rescheduleFlight(res, v, res.getReservedId(), startTime, 0);
			default:
				return Mono.empty();
			}
		});
	}

	private Mono<Reservation> rescheduleHotel(Reservation res, VacationDto vac, LocalDateTime startTime,
			Integer duration) {
		log.trace("Rescheduling hotel");
		log.trace("Arguments: res" + res + ", vac: " + vac + ", startTime:" + startTime + ", duration: " + duration);
		//Get the hotel
		return hotelDao.findByLocationAndId(vac.getDestination(), res.getReservedId())
				//Make sure there are no reservation conflicts
				.flatMap(h -> isAvailable(res.getId(), h.getId(), h.getRoomsAvailable(), ReservationType.HOTEL,
						startTime, duration).flatMap(b -> {
							// If there are rooms available
							if (Boolean.TRUE.equals(b)) {
								return changeAndSaveReservation(res, vac, startTime, duration);
							}
							return Mono.empty();
						}));
	}

	private Mono<Reservation> rescheduleCar(Reservation res, VacationDto vac, LocalDateTime startTime,
			Integer duration) {
		log.trace("Rescheduling car");
		log.trace("Arguments: res" + res + ", vac: " + vac + ", startTime:" + startTime + ", duration: " + duration);
		//Get the car
		return carDao.findByLocationAndId(vac.getDestination(), res.getReservedId()).flatMap(
				//Make sure the reservation does not have any conflicts
				c -> isAvailable(res.getId(), c.getId(), 1, ReservationType.CAR, startTime, duration).flatMap(b -> {
					// If there are spots available
					if (Boolean.TRUE.equals(b)) {
						return changeAndSaveReservation(res, vac, startTime, duration);
					}
					return Mono.empty();
				}));
	}

	private Mono<Reservation> rescheduleFlight(Reservation res, VacationDto vac, UUID newReservedId,
			LocalDateTime startTime, Integer duration) {
		log.trace("Rescheduling flight");
		log.trace("Arguments: res" + res + ", vac: " + vac + ", newReservedId," + newReservedId.toString()
				+ ", startTime:" + startTime + ", duration: " + duration);
		
		//Get the new flight
		return flightDao.findByDestinationAndId(vac.getDestination(), newReservedId)
				//Need to make sure the flight has seats available
				.flatMap(f -> isAvailable(res.getId(), f.getId(), f.getOpenSeats(), ReservationType.FLIGHT,
						LocalDateTime.ofInstant(f.getDepartingDate(), ZoneOffset.UTC), duration).flatMap(b -> {
							log.debug("Checked if flights were available");
							// If there are seats available
							if (Boolean.TRUE.equals(b)) {
								log.debug("Changing the reservation");
								//If the id is not null, then the flight is being changed
								if (!res.getReservedId().equals(newReservedId)) {
									log.debug("Changing the flight");
									
									//Need to get the old flight and the new flight to make adjustments
									return flightDao.findByDestinationAndId(vac.getDestination(), res.getReservedId())
											.zipWith(flightDao.findByDestinationAndId(vac.getDestination(),
													newReservedId))
											.flatMap(t -> {
												
												//Get the old flight and new flight
												FlightDto oldFlight = t.getT1();
												log.debug("The old flight: " + oldFlight);
												FlightDto newFlight = t.getT2();
												log.debug("The new flight" + newFlight);
												
												//Subtract the old ticket price and add the new ticket price
												//to the vacation total
												vac.setTotal(vac.getTotal() - oldFlight.getTicketPrice()
														+ newFlight.getTicketPrice());
												
												//Set the reservation's new reservedId, price, start time, and name
												res.setReservedId(newReservedId);
												res.setCost(newFlight.getTicketPrice());
												res.setStarttime(LocalDateTime.ofInstant(newFlight.getDepartingDate(),
														ZoneOffset.UTC));
												res.setReservedName(newFlight.getAirline());
												
												//Save the vacation and the reservation and return
												log.debug("New Reservation: " + res);
												log.debug("Vacation changed to: " + vac);
												return resDao.save(new ReservationDto(res)).zipWith(vacDao.save(vac))
														.map(tuple -> tuple.getT1().getReservation());
											});
								} else {
									return changeAndSaveReservation(res, vac, startTime, duration);
								}
							}
							return Mono.empty();
						}));
	}

	private Mono<Reservation> changeAndSaveReservation(Reservation res, VacationDto vac, LocalDateTime startTime,
			Integer duration) {
		log.debug("The reservation will be rescheduled");
		//Change the start time
		res.setStarttime(startTime);
		
		//Get the old duration and cost to change the vacation total
		Integer oldDur = res.getDuration();
		Double oldCost = res.getCost();
		//Set the duration, cost, and vacation total
		res.setDuration(duration);
		res.setCost(res.getCost() / oldDur * res.getDuration());
		vac.setTotal(vac.getTotal() - oldCost + res.getCost());
		
		//Save both and return the reservation
		log.debug("New Vacation: " + vac);
		log.debug("New Reservation: " + res);
		return resDao.save(new ReservationDto(res)).zipWith(vacDao.save(vac)).map(t -> t.getT1().getReservation());
	}
}
