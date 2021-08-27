package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Activity;
import com.revature.beans.User;
import com.revature.beans.Vacation;

import reactor.core.publisher.Mono;

public interface UserController {
	
	/**
	 * The attribute that the loggedUser is saved to
	 */
	String LOGGED_USER = "loggedUser";
	
	/**
	 * Logs the user in
	 * @param user The user being logged in
	 * @param session The WebSession
	 * @return The logged in user
	 */
	public Mono<ResponseEntity<User>> login(User user, WebSession session);

	/**
	 * Logs the user out by invalidating the session
	 * @param session The WebSession
	 * @return An empty body
	 */
	public Mono<ResponseEntity<Void>> logout(WebSession session);
	
	/**
	 * Creates a new vacationer
	 * @param user The user being created
	 * @param name The username of the new user. Needs to be unique
	 * @return The new user
	 */
	public Mono<ResponseEntity<User>> register(User user, String name);

	/**
	 * Create a new vacation
	 * @param vacation The vacation being created
	 * @param username The username of the user creating the vacation
	 * @param session The WebSession
	 * @return The new vacation
	 */
	public Mono<ResponseEntity<Vacation>> createVacation(Vacation vacation, String username, WebSession session);

	/**
	 * Get a vacation that the user previously created
	 * @param username The username of the user
	 * @param id The id of the vacation to retrieve
	 * @param session The WebSession
	 * @return The vacation
	 */
	public Mono<ResponseEntity<Vacation>> getVacation(String username, String id, WebSession session);
	
	/**
	 * Delete the user and all vacations and reservations
	 * @param username The username of the user
	 * @param session The WebSession
	 * @return An empty response
	 */
	public Mono<ResponseEntity<Void>> deleteUser(String username, WebSession session);

	/**
	 * Add an activity to the vacation list
	 * @param activity The activity to add
	 * @param username The username of the user
	 * @param vacId The id of the vacation
	 * @param session The WebSession
	 * @return The activity added to the vacation
	 */
	public  Mono<ResponseEntity<Activity>> chooseActivities(Activity activity, String username, String vacId, WebSession session);

}
