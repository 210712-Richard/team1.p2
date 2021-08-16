package com.revature.services;

import java.time.LocalDate;

import com.revature.beans.User;
import com.revature.beans.UserType;

import reactor.core.publisher.Mono;

public interface UserService {
	public Mono<User> login(String username, String password);

	public Mono<User> register(String username, String password, String email, String firstName, String lastName,
			LocalDate birthday, UserType type);
}
