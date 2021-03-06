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
		res.setStartTime(vacation.getStartTime());

		log.debug("The new hotel reservation: {}", res);

		// Add the reservation to the vacation
		vacation.getReservations().add(res);
		vacation.setTotal(vacation.getTotal() + res.getCost());

		log.debug("The vacation now: {}", vacation);

		// Save the reservation and the vacation and return the reservation
		return isAvailable(res.getId(), hotel.getId(), hotel.getRoomsAvailable(), ReservationType.HOTEL,
				res.getStartTime(), res.getDuration()).flatMap(b -> {
					// If there are rooms available
					if (Boolean.TRUE.equals(b)) {
						log.debug("Saving the vacation");
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
		res.setStartTime(flight.getDepartingDate());
		res.setType(ReservationType.FLIGHT);
		res.setUsername(vacation.getUsername());
		res.setVacationId(vacation.getId());

		log.debug("The new flight reservation: {}", res);

		// add reservation
		vacation.getReservations().add(res);
		vacation.setTotal(vacation.getTotal() + res.getCost());

		log.debug("The modified vacation: {}", vacation);

		return isAvailable(res.getId(), flight.getId(), flight.getOpenSeats(), ReservationType.FLIGHT,
				res.getStartTime(), res.getDuration()).flatMap(b -> {
					// If flight is available
					if (Boolean.TRUE.equals(b)) {
						log.debug("Saving the vacation");
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
		res.setStartTime(vacation.getStartTime());

		log.debug("The new car reservation: {}", res);

		// Save the reservation and the vacation and return the reservation
		if (Boolean.FALSE.equals(car.getInUse())) {
			// Add the reservation to the vacation
			vacation.getReservations().add(res);
			vacation.setTotal(vacation.getTotal() + res.getCost());
			log.debug("The changed vacation: {}", vacation);
			return vacDao.save(new VacationDto(vacation)).flatMap(v -> resDao.save(new ReservationDto(res)))
					.map(ReservationDto::getReservation);
		}
		return Mono.empty();
	}

	public Mono<Reservation> updateReservation(Reservation res, String status) {
		// Make sure the resId is a uuid

		return vacDao.findByUsernameAndId(res.getUsername(), res.getVacationId()).flatMap(vac -> {
			// If the reservation status is cancelled, change vacation total to reflect that
			if (ReservationStatus.CANCELLED.toString().equals(status)) {
				vac.setTotal(vac.getTotal() - res.getCost());
				vacDao.save(vac).subscribe();
			}
			res.setStatus(ReservationStatus.valueOf(status));
			return resDao.save(new ReservationDto(res)).map(r -> r.getReservation());

		});
	}

	@Override
	public Flux<Reservation> getReservations(String username, String vacId) {
		UUID id = UUID.fromString(vacId);
		return vacDao.findByUsernameAndId(username, id).map(vdto -> {
			Vacation v = vdto == null ? new VacationDto().getVacation() : vdto.getVacation();
			log.debug("getReservations called. Vacation Result: {}", v);
			return v;
		}).flatMapMany(v -> {
			log.debug("Reservations List: {}", v.getReservations());
			return Flux.fromIterable(v.getReservations());
		});
	}

	/**
	 * Checks to see if a reservation is available for a specific time and duration
	 * 
	 * @param resId     The id of the reservation that needs verifying.<br>
	 *                  Used to make sure it does not get added if a modification is
	 *                  being made
	 * @param id        The id of the reserveable
	 * @param available How many spots are available
	 * @param type      The type of the reservation
	 * @param startTime The start time of the reservation
	 * @param duration  How many days the reservation is for
	 * @return True if there are enough spots available, false otherwise
	 */
	private Mono<Boolean> isAvailable(UUID resId, UUID id, Integer available, ReservationType type,
			LocalDateTime startTime, Integer duration) {
		log.trace("Checking if reservations are available");

		// Get when the reservation would end
		LocalDateTime endTime = startTime.plus(Period.of(0, 0, duration));

		// Get the number of potential reservation conflicts
		Mono<Integer> intMono = resDao.findAll().map(rDto -> rDto.getReservation()).filter(r -> {
			LocalDateTime rEndTime = r.getStartTime().plus(Period.of(0, 0, duration));
			return !r.getId().equals(resId) && r.getReservedId().equals(id) && r.getType().equals(type)
					&& !r.getStatus().equals(ReservationStatus.CLOSED)
					&& ((!r.getType().equals(ReservationType.FLIGHT)
							&& (!startTime.isAfter(rEndTime) && !endTime.isBefore(r.getStartTime())))
							|| (r.getType().equals(ReservationType.FLIGHT) && startTime.equals(r.getStartTime())));
		}).collectList().map(rDtoList -> rDtoList.size());

		return intMono.map(i -> {
			log.debug("Amount in database for {}: {}", type, i);
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
					return rescheduleFlight(res, v, newReservedId, res.getStartTime(), 0);
				}
				return rescheduleFlight(res, v, res.getReservedId(), startTime, 0);
			default:
				return Mono.empty();
			}
		});
	}

	/**
	 * Reschedule a hotel reservation
	 * 
	 * @param res       The reservation being modified
	 * @param vac       The vacation the reservation is a part of
	 * @param startTime The new start time
	 * @param duration  The new duration
	 * @return The modified reservation
	 */
	private Mono<Reservation> rescheduleHotel(Reservation res, VacationDto vac, LocalDateTime startTime,
			Integer duration) {
		log.trace("Rescheduling hotel");
		log.trace("Arguments: res, {}, vac: {}, startTime: {}, duration: {}",
				res, vac, startTime, duration);
		// Get the hotel
		return hotelDao.findByLocationAndId(vac.getDestination(), res.getReservedId())
				// Make sure there are no reservation conflicts
				.flatMap(h -> isAvailable(res.getId(), h.getId(), h.getRoomsAvailable(), ReservationType.HOTEL,
						startTime, duration).flatMap(b -> {
							// If there are rooms available
							if (Boolean.TRUE.equals(b)) {
								return changeAndSaveReservation(res, vac, startTime, duration);
							}
							return Mono.empty();
						}));
	}

	/**
	 * Reschedule a car reservation
	 * 
	 * @param res       The reservation being modified
	 * @param vac       The vacation the reservation is a part of
	 * @param startTime The new start time
	 * @param duration  The new duration
	 * @return The modified reservation
	 */
	private Mono<Reservation> rescheduleCar(Reservation res, VacationDto vac, LocalDateTime startTime,
			Integer duration) {
		log.trace("Rescheduling car");
		log.trace("Arguments: res, {}, vac: {}, startTime: {}, duration: {}",
				res, vac, startTime, duration);
		// Get the car
		return carDao.findByLocationAndId(vac.getDestination(), res.getReservedId()).flatMap(
				// Make sure the reservation does not have any conflicts
				c -> isAvailable(res.getId(), c.getId(), 1, ReservationType.CAR, startTime, duration).flatMap(b -> {
					// If there are spots available
					if (Boolean.TRUE.equals(b)) {
						return changeAndSaveReservation(res, vac, startTime, duration);
					}
					return Mono.empty();
				}));
	}

	/**
	 * Reschedule a flight reservation
	 * 
	 * @param res           The reservation being modified
	 * @param vac           The vacation the reservation is a part of
	 * @param newReservedId The new reserved Id. Used to change what flight is
	 *                      reserved.
	 * @param startTime     The new start time. Can be null if reserved id is set.
	 * @param duration      The new duration. Can be null if reserved id is set.
	 * @return The modified reservation
	 */
	private Mono<Reservation> rescheduleFlight(Reservation res, VacationDto vac, UUID newReservedId,
			LocalDateTime startTime, Integer duration) {
		log.trace("Rescheduling flight");
		log.trace("Arguments: res: {}, vac: {}, newReservedId: {}, startTime: {}, duration, {}", 
				res, vac, newReservedId, startTime, duration);

		// Get the new flight
		return flightDao.findByDestinationAndId(vac.getDestination(), newReservedId)
				// Need to make sure the flight has seats available
				.flatMap(f -> isAvailable(res.getId(), f.getId(), f.getOpenSeats(), ReservationType.FLIGHT,
						LocalDateTime.ofInstant(f.getDepartingDate(), ZoneOffset.UTC), duration).flatMap(b -> {
							log.debug("Checked if flights were available");
							// If there are seats available
							if (Boolean.TRUE.equals(b)) {
								log.debug("Changing the reservation");
								// If the id is not null, then the flight is being changed
								if (!res.getReservedId().equals(newReservedId)) {
									log.debug("Changing the flight");

									// Need to get the old flight and the new flight to make adjustments
									return flightDao.findByDestinationAndId(vac.getDestination(), res.getReservedId())
											.zipWith(flightDao.findByDestinationAndId(vac.getDestination(),
													newReservedId))
											.flatMap(t -> {

												// Get the old flight and new flight
												FlightDto oldFlight = t.getT1();
												log.debug("The old flight: {}", oldFlight);
												FlightDto newFlight = t.getT2();
												log.debug("The new flight: {}", newFlight);

												// Subtract the old ticket price and add the new ticket price
												// to the vacation total
												vac.setTotal(vac.getTotal() - oldFlight.getTicketPrice()
														+ newFlight.getTicketPrice());

												// Set the reservation's new reservedId, price, start time, and name
												res.setReservedId(newReservedId);
												res.setCost(newFlight.getTicketPrice());
												res.setStartTime(LocalDateTime.ofInstant(newFlight.getDepartingDate(),
														ZoneOffset.UTC));
												res.setReservedName(newFlight.getAirline());

												// Save the vacation and the reservation and return
												log.debug("New Reservation: {}", res);
												log.debug("Vacation changed to: {}", vac);
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
		// Change the start time
		res.setStartTime(startTime);

		// Get the old duration and cost to change the vacation total
		Integer oldDur = res.getDuration();
		Double oldCost = res.getCost();
		// Set the duration, cost, and vacation total
		res.setDuration(duration);
		res.setCost(res.getCost() / oldDur * res.getDuration());
		vac.setTotal(vac.getTotal() - oldCost + res.getCost());

		// Save both and return the reservation
		log.debug("New Vacation: {}", vac);
		log.debug("New Reservation: {}", res);
		return resDao.save(new ReservationDto(res)).zipWith(vacDao.save(vac)).map(t -> t.getT1().getReservation());
	}

	@Override
	public Flux<Reservation> getReservationsByType(ReservationType type) {
		
		return resDao.findByType(type.toString()).map(rDto -> rDto.getReservation());
	}
}