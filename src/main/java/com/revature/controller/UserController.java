package com.revature.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Activity;
import com.revature.beans.User;
import com.revature.beans.Vacation;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserController {
	
	String LOGGED_USER = "loggedUser";
	
	public Mono<ResponseEntity<User>> login(User user, WebSession session);

	public Mono<ResponseEntity<Void>> logout(WebSession session);

	public Mono<ResponseEntity<User>> register(User user, String name);

	public Mono<ResponseEntity<Vacation>> createVacation(Vacation vacation, String username, WebSession session);

	public Mono<ResponseEntity<Vacation>> getVacation(String username, String id, WebSession session);
	
	public Mono<ResponseEntity<Void>> deleteUser(String username, WebSession session);
	
	public  Mono<ResponseEntity<Activity>> chooseActivities(Activity activity, String username, String vacId, WebSession session);


}