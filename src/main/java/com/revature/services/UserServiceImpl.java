package com.revature.services;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.data.UserDao;
import com.revature.dto.UserDto;

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
		Mono<User> usern = userDao.findByUsernameAndPassword(username, password).map(user -> user.getUser());
		
		return usern;
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
		userDao.save(new UserDto(user));
		return Mono.just(user); 
	} 			
	
	public boolean checkAvailability(String newName) {
		Mono<UserDto> u = userDao.findByUsername(newName);
		return (u == null) ? false : true;
}
}
