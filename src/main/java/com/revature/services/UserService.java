package com.revature.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;

import reactor.core.publisher.Mono;

public interface UserService {
	public Mono<User> login(String username, String password);

	public Mono<User> register(String username, String password, String email, String firstName, String lastName,
			LocalDate birthday, UserType type);

	public Mono<Vacation> createVacation(String username, String destination, LocalDateTime startTime,
			LocalDateTime endTime, Integer partySize, Integer duration);

	public Mono<Boolean> checkAvailability(String username);

	public Mono<Vacation> getVacation(String username, UUID id);

	public Mono<User> deleteUserAccount(String username);

	public Mono<User> deletebyUsername(String username);
}