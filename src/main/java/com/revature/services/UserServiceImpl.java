  
package com.revature.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.beans.Activity;
import com.revature.beans.Reservation;
import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.data.ActivityDao;
import com.revature.data.HotelDao;
import com.revature.data.ReservationDao;
import com.revature.data.UserDao;
import com.revature.data.VacationDao;
import com.revature.dto.ActivityDto;
import com.revature.dto.ReservationDto;
import com.revature.dto.UserDto;
import com.revature.dto.VacationDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
public class UserServiceImpl implements UserService {
	private static Logger log = LogManager.getLogger(UserServiceImpl.class);

	private UserDao userDao;

	private VacationDao vacDao;

	private ReservationDao resDao;

	private ActivityDao actDao;

	@Autowired
	public UserServiceImpl(UserDao userDao, VacationDao vacDao, HotelDao hotelDao, ReservationDao resDao,
			ActivityDao actDao) {
		this.userDao = userDao;
		this.vacDao = vacDao;
		this.resDao = resDao;
		this.actDao = actDao;
	}

	@Override
	public Mono<User> login(String username, String password) {
		return userDao.findByUsernameAndPassword(username, password).map(u -> u.getUser())
				.switchIfEmpty(Mono.just(new User()));
	}

	@Override
	public Mono<User> register(String username, String password, String email, String firstName, String lastName,
			LocalDate birthday, UserType type) {

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setBirthday(birthday);
		user.setType(type);
		return userDao.save(new UserDto(user)).map(uDto -> uDto.getUser());
	}

	@Override
	public Mono<Vacation> createVacation(String username, String destination, LocalDateTime startTime,
			LocalDateTime endTime, Integer partySize, Integer duration) {
		// Check to see if the end time is set correctly
		if (!endTime.isAfter(startTime)) {
			return Mono.just(new Vacation());
		}

		// Create the new vacation
		Vacation vac = new Vacation();
		vac.setUsername(username);
		vac.setId(UUID.randomUUID());
		vac.setDestination(destination);
		vac.setStartTime(startTime);
		vac.setEndTime(endTime);
		vac.setPartySize(partySize);
		vac.setDuration(duration);

		// Save the vacation id to the user and save the vacation to the database
		return userDao.findByUsername(username).flatMap(u -> {
			// Make sure vacations list isn't null
			if (u.getVacations() == null) {
				u.setVacations(new ArrayList<>());
			}
			// Add the vacation id to the user's list and save the user
			u.getVacations().add(vac.getId());
			return userDao.save(u);
			// Return the vacation object after saving the vacation to the database
		}).zipWith(vacDao.save(new VacationDto(vac))).flatMap(t -> Mono.just(t.getT2().getVacation()));
	}

	public Mono<Boolean> checkAvailability(String newName) {
		return userDao.existsByUsername(newName);
	}

	@Override
	public Mono<Vacation> getVacation(String username, UUID id) {
		Mono<Vacation> monoVac = vacDao.findByUsernameAndId(username, id).map(VacationDto::getVacation)
				.switchIfEmpty(Mono.empty());
		// Get the list of activities in the vacation
		Mono<List<Activity>> activities = Flux.from(vacDao.findByUsernameAndId(username, id)).map(vac -> {
			if (vac.getActivities() == null) {
				vac.setActivities(new ArrayList<>());
			}
			return vac.getActivities();
		}).flatMap(l -> {
			log.debug("The list from the vacation: %s", l);
			if (l.isEmpty()) {
				return Flux.fromIterable(new ArrayList<Activity>());
			} else {
				//Need to create an infinite flux to make sure all the activities are retrieved
				Flux<Vacation> fluxVac = Flux.from(monoVac)
						.flatMap(v -> Flux.<Vacation>generate(sink -> sink.next(v)));
				
				//Zip the fluxes together and iterate until all the activities have been obtained.
				return Flux.fromIterable(l).zipWith(fluxVac)
						.flatMap(t -> actDao.findByLocationAndId(t.getT2().getDestination(), t.getT1()))
						.map(ActivityDto::getActivity);
			}
		}).collectList().switchIfEmpty(Mono.just(new ArrayList<Activity>()));

		Mono<List<Reservation>> reserveds = Flux.from(vacDao.findByUsernameAndId(username, id)).map(vac -> {
			if (vac.getReservations() == null) {
				vac.setReservations(new ArrayList<>());
			}
			return vac.getReservations();
		}).flatMap(l -> {
			log.debug("The list from the vacation: " + l);
			if (l.isEmpty()) {
				return Flux.fromIterable(new ArrayList<Reservation>());
			} else {
				return Flux.fromIterable(l).flatMap(uuid -> resDao.findByUuid(uuid))
						.map(ReservationDto::getReservation);
			}
		}).collectList().switchIfEmpty(Mono.just(new ArrayList<Reservation>()));
		
		//Need to first set the activities
		Mono<Vacation> zippedVacMono = activities.zipWith(monoVac).map(t -> {
			t.getT2().setActivities(t.getT1());
			return t.getT2();
		});
		
		//Then set the reservations and return
		Mono<Tuple2<List<Reservation>, Vacation>> zippedFinalMono = reserveds.zipWith(zippedVacMono)
				.switchIfEmpty(Mono.empty());
		return zippedFinalMono.map(t -> {
			Vacation vac = t.getT2();
			log.debug("Vacation received: " + vac);
			List<Reservation> resList = t.getT1();
			log.debug("Reservation List received: " + resList);
			vac.setReservations(resList);
			return vac;
		}).switchIfEmpty(Mono.just(new Vacation()));
	}
    
	@Override
	public Mono<Void> deleteUser(String username, List<Vacation> vacList) {

		Flux.fromIterable(vacList)
				.map(v -> v.getReservations())
				.flatMap(l -> Flux.fromIterable(l))
				.map(r -> resDao.deleteByUuid(r.getId()).subscribe())
				.zipWith(vacDao.deleteByUsername(username))
				.collectList().subscribe();
				userDao.deleteByUsername(username).subscribe();
				return Mono.empty();
	}
}
	
