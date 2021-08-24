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

import com.revature.beans.Reservation;
import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.data.HotelDao;
import com.revature.data.ReservationDao;
import com.revature.data.UserDao;
import com.revature.data.VacationDao;
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

	@Autowired
	public UserServiceImpl(UserDao userDao, VacationDao vacDao, HotelDao hotelDao, ReservationDao resDao) {
		this.userDao = userDao;
		this.vacDao = vacDao;
		this.resDao = resDao;
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
		
		Mono<List<Reservation>> reserveds = Flux.from(vacDao.findByUsernameAndId(username, id))
				.map(vac -> {
					if (vac.getReservations() == null) {
						vac.setReservations(new ArrayList<>());
					}
					return vac.getReservations();
				})
				.flatMap(l -> {
					log.debug("The list from the vacation: " + l);
					if (l.isEmpty()) {
						return Flux.fromIterable(new ArrayList<Reservation>());
					}
					else {
						return Flux.fromIterable(l)
						.flatMap(uuid -> resDao.findByUuid(uuid))
						.map(ReservationDto::getReservation);
					}
				}).collectList()
				.switchIfEmpty(Mono.just(new ArrayList<Reservation>()));
				
		
		Mono<Tuple2<List<Reservation>, Vacation>> zippedMono = reserveds.zipWith(monoVac).switchIfEmpty(Mono.empty());
		return zippedMono.map(t -> {
			Vacation vac = t.getT2();
			log.debug("Vacation received: " + vac);
			List<Reservation> resList = t.getT1();
			log.debug("Reservation List received: " + resList);
			vac.setReservations(resList);
			return vac;
		}).switchIfEmpty(Mono.just(new Vacation()));
	}
    
	public Mono<User> deleteUserAccount(String username) {
        Mono<User> user = userDao.findByUsername(username).map(UserDto::getUser)
                .switchIfEmpty(Mono.empty());
        Mono<List<Vacations>> vac = Flux.from(userDao.findByUsername(username)
        		.map(u -> {
					if (u.getVacations() == null) {
						u.setVacations(new ArrayList<>());
					}
					return u.getVacations();
        		Mono<List<Reservation>> reserveds = Flux.from(vacDao.findByUsername(username))
				.map(v-> {
					if (v.getReservations() == null) {
						v.setReservations(new ArrayList<>());
					}
					return v.getReservations();
				})
				.flatMap(r -> {
					log.debug("The list from the reservation: " + r);
					if (r.isNotEmpty()) {
						return Flux.fromIterable(r)
								.flatMap(uuid -> resDao.deleteById(uuid))
								.map(ReservationDto::getReservation);
					}
				})
					
					.flatMap(v -> {
						log.debug("The list from the vacation: " + v);
						if (v.isNotEmpty()) {
							return Flux.fromIterable(v)
									.flatMap(username -> vacDao.deleteById(username))
									.map(VacationDto::getVacations);
						}
					})
						.flatMap(u -> {
							log.debug("user: " + u);
							if (u.isNotEmpty()) {
								return Flux.fromIterable(l)
										.flatMap(username -> userDao.deleteByUsername(username))
										.map(UserDto::getUser);
							}
						});
        		}
	}
}
						
		
	
	

   
	
    		 
    			 
    	
 				
 		
 		
    			 
 
 

