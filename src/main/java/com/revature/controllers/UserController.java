package com.revature.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.WebSession;

import com.revature.beans.User;

import reactor.core.publisher.Mono;

public interface UserController {
	public Mono<ResponseEntity<User>> login(User user, WebSession session);

	public Mono<ResponseEntity<Void>> logout(WebSession session);

	public Mono<ResponseEntity<User>> register(User user, String name);
}
