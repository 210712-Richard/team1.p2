package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.services.UserService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {
	private static Logger log = LogManager.getLogger(UserControllerImpl.class);

	UserService userService;

	@Autowired
	public UserControllerImpl(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public Mono<ResponseEntity<User>> login(@RequestBody User user, WebSession session){
		if (user == null) {
			return Mono.just(ResponseEntity.badRequest().build());
		}

		return userService.login(user.getUsername(), user.getPassword()).single().map(u -> {
			if (u.getUsername() == null) {
				return ResponseEntity.notFound().build();
			}

			else {
				session.getAttributes().put("loggedUser", u);
				return ResponseEntity.ok(u);
			}
		});
	}

	@DeleteMapping
	public Mono<ResponseEntity<Void>> logout(WebSession session) {
		return null;
	}

	@PutMapping("{username}")
	public Mono<ResponseEntity<User>> register(@RequestBody User u, @PathVariable("username") String username) {
		// check to see if that username is available
		return userService.checkAvailability(username).flatMap(b -> {
			if (!b) {
				return userService.register(username, u.getPassword(), u.getEmail(), u.getFirstName(), u.getLastName(),
						u.getBirthday(), u.getType()).map(user -> ResponseEntity.status(201).body(user));
			} else {
				return Mono.just(ResponseEntity.status(409).build());
			}

		});

	}

	@PostMapping("{username}/vacations")
	public Mono<ResponseEntity<Vacation>> createVacation(@RequestBody Vacation vacation,
			@PathVariable("username") String username, WebSession session) {
		User loggedUser = (User) session.getAttribute("loggedUser");
		log.debug("Logged in user: " + loggedUser);

		// If the user is not logged in
		if (loggedUser == null) {
			return Mono.just(ResponseEntity.status(401).build());
		}

		// If the logged in user is not the same user specified or is not a vacationer
		if (!username.equals(loggedUser.getUsername()) || !UserType.VACATIONER.equals(loggedUser.getType())) {
			return Mono.just(ResponseEntity.status(403).build());
		}

		return userService.createVacation(username, vacation.getDestination(), vacation.getStartTime(),
				vacation.getEndTime(), vacation.getPartySize(), vacation.getDuration()).flatMap(v -> {
					if (v.getId() == null) {
						return Mono.just(ResponseEntity.status(400).build());
					} else {
						return Mono.just(ResponseEntity.status(201).body(v));
					}
				});
	}

}
