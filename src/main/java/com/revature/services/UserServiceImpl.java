package com.revature.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.data.HotelDao;
import com.revature.data.UserDao;
import com.revature.data.VacationDao;
import com.revature.dto.VacationDto;

import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
	private static Logger log = LogManager.getLogger(UserServiceImpl.class);

	UserDao userDao;

	VacationDao vacDao;

	HotelDao hotelDao;

	@Autowired
	public UserServiceImpl(UserDao userDao, VacationDao vacDao, HotelDao hotelDao) {
		this.userDao = userDao;
		this.vacDao = vacDao;
		this.hotelDao = hotelDao;
	}

	@Override
	public Mono<User> login(String username, String password) {
		Mono<User> returnUser = userDao.findByUsernameAndPassword(username, password).map(u -> u.getUser())
				.switchIfEmpty(Mono.just(new User()));

		return returnUser;
	}

	@Override
	public Mono<User> register(String username, String password, String email, String firstName, String lastName,
			LocalDate birthday, UserType type) {
		return Mono.just(new User(username, password, email, firstName, lastName, birthday, type));
	}

	@Override
	public Mono<Vacation> createVacation(String username, String destination, LocalDateTime startTime,
			LocalDateTime endTime, Integer partySize, Integer duration) {
		// Check to see if the end time is set correctly
		if (!endTime.isAfter(startTime)) {
			return Mono.just(new Vacation());
		}
		Vacation vac = new Vacation();
		vac.setUsername(username);
		vac.setId(UUID.randomUUID());
		vac.setDestination(destination);
		vac.setStartTime(startTime);
		vac.setEndTime(endTime);
		vac.setPartySize(partySize);
		vac.setDuration(duration);

		return userDao.findByUsername(username).flatMap((u) -> {
			u.getVacations().add(vac.getId());
			return userDao.save(u);
		}).flatMap(u->vacDao.save(new VacationDto(vac)).flatMap((v) -> {
			return Mono.just(v.getVacation());
		}));
	}

}
