package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.beans.User;
import com.revature.services.UserService;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
@RequestMapping("/users")
public class UserControllerImpl {
	@Autowired
	private UserService userService;

	private static final Logger log = LogManager.getLogger(UserControllerImpl.class);

	// As a user, I can log in.
	@PostMapping // ("/users")
	public Mono<ResponseEntity<User>> login(@RequestBody User user, WebSession session) {
		if (user == null) {
			return Mono.just(ResponseEntity.badRequest().build());
		}

		return userService.login(user.getUsername(), user.getPassword()).single().map(u -> {
			if (u == null) {
				return ResponseEntity.notFound().build();
			}

			else {
				session.getAttributes().put("loggedUser", u);
				return ResponseEntity.ok(u);
			}
		});
	}

	// As a user, I can log out.
	@DeleteMapping
	public ResponseEntity<Void> logout(WebSession session) {
		session.invalidate();
		return ResponseEntity.noContent().build();
	}

	// As a user, I can register.
	@PutMapping("{username}")
	public Mono<ResponseEntity<User>> register(@RequestBody User u, @PathVariable("username") String username) {
		// check to see if that username is available
		return userService.checkAvailability(username).flatMap(b->{
			if (!b) {
				return userService.register(username, u.getPassword(), u.getEmail(), u.getFirstName(), u.getLastName(),
						u.getBirthday(), u.getType())
						.map(user -> ResponseEntity.status(201).body(user));
			}
			else {
				return Mono.just(ResponseEntity.status(409).build());
			}
			
		});
				
	}

}
