package com.revature.services;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.data.UserDao;

import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
	private static Logger log = LogManager.getLogger(UserServiceImpl.class);

	UserDao userDao;

	@Autowired
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public Mono<User> login(String username, String password) {
		return userDao.findByUsernameAndPassword(username, password).map(u -> u.getUser())
				.switchIfEmpty(Mono.just(new User()));
	}

	@Override
	public Mono<User> register(String username, String password, String email, String firstName, String lastName,
			LocalDate birthday, UserType type) {
		return null;
	}
}
