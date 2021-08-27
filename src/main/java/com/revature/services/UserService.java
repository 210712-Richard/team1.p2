package com.revature.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.revature.beans.Activity;
import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
	
	/**
	 * Get the user based on username and password
	 * @param username The username of the user
	 * @param password The password of the user
	 * @return The user
	 */
	public Mono<User> login(String username, String password);

	/**
	 * Create a new user with the parameters
	 * @param username The username of the user
	 * @param password The password of the user
	 * @param email The email of the user
	 * @param firstName The first name of the user
	 * @param lastName The last name of the user
	 * @param birthday The birthday of the user
	 * @param type The type of the user
	 * @return The new user
	 */
	public Mono<User> register(String username, String password, String email, String firstName, String lastName,
			LocalDate birthday, UserType type);

	/**
	 * Create a vacation with the parameters
	 * @param username The user creating the vacation
	 * @param destination The destination of the vacation
	 * @param startTime When the vacation starts
	 * @param endTime When the vacation ends
	 * @param partySize The amount of people attending the vacation
	 * @param duration The number of days the vacation is
	 * @return The new vacation
	 */
	public Mono<Vacation> createVacation(String username, String destination, LocalDateTime startTime,
			LocalDateTime endTime, Integer partySize, Integer duration);

	/**
	 * Check if the username is already in the database
	 * @param username The username being evaluated
	 * @return False if the username is available, true otherwise
	 */
	public Mono<Boolean> checkAvailability(String username);

	/**
	 * Get a specific vacation
	 * @param username The username of who created the vacation
	 * @param id The id of the vacation
	 * @return The vacation
	 */
	public Mono<Vacation> getVacation(String username, UUID id);

	/**
	 * Get the activities associated with a vacation
	 * @param id The id of the vacation
	 * @param username The username of the user who created the vacation
	 * @return The list of activities in the vacation
	 */
	public Flux<Activity> getActivities(UUID id, String username);

	/**
	 * Delete the user and all vacations
	 * @param username The username of the user
	 * @param vacList The list of vacations
	 * @return A void Mono
	 */
	public Mono<Void> deleteUser(String username, List<Vacation> vacList);
}
