package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.User;
import com.revature.beans.Vacation;

import reactor.core.publisher.Mono;

public interface UserController {
	
	String LOGGED_USER = "loggedUser";
	
	public Mono<ResponseEntity<User>> login(User user, WebSession session);

	public Mono<ResponseEntity<Void>> logout(WebSession session);

	public Mono<ResponseEntity<User>> register(User user, String name);

	public Mono<ResponseEntity<Vacation>> createVacation(Vacation vacation, String username, WebSession session);

	public Mono<ResponseEntity<Vacation>> getVacation(String username, String id, WebSession session);

}
