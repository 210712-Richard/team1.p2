package com.revature.controllers;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.aspects.LoggedInMono;
import com.revature.aspects.VacationerCheck;
import com.revature.beans.User;
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
	public Mono<ResponseEntity<User>> login(@RequestBody User user, WebSession session) {
		if (user == null) {
			return Mono.just(ResponseEntity.badRequest().build());
		}

		return userService.login(user.getUsername(), user.getPassword()).single().map(u -> {
			if (u.getUsername() == null) {
				return ResponseEntity.notFound().build();
			}

			else {
				session.getAttributes().put(LOGGED_USER, u);
				return ResponseEntity.ok(u);
			}
		});
	}

	@DeleteMapping
	public Mono<ResponseEntity<Void>> logout(WebSession session) {
		session.invalidate();

		return Mono.just(ResponseEntity.noContent().build());
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

	@VacationerCheck
	@PostMapping("{username}/vacations")
	public Mono<ResponseEntity<Vacation>> createVacation(@RequestBody Vacation vacation,
			@PathVariable("username") String username, WebSession session) {

		return userService.createVacation(username, vacation.getDestination(), vacation.getStartTime(),
				vacation.getEndTime(), vacation.getPartySize(), vacation.getDuration()).flatMap(v -> {
					if (v.getId() == null) {
						return Mono.just(ResponseEntity.status(400).build());
					} else {
						return Mono.just(ResponseEntity.status(201).body(v));
					}
				});
	}

	@LoggedInMono
	@Override
	@GetMapping("{username}/vacations/{vacationid}")
	public Mono<ResponseEntity<Vacation>> getVacation(@PathVariable("username") String username,
			@PathVariable("vacationid") String id, WebSession session) {
		User loggedUser = (User) session.getAttribute(LOGGED_USER);

		if (loggedUser == null || !username.equals(loggedUser.getUsername())) {
			return Mono.just(ResponseEntity.status(403).build());
		}
		UUID vacId = null;
		try {
			vacId = UUID.fromString(id);
		} catch (Exception e) {
			return Mono.just(ResponseEntity.badRequest().build());
		}
		return userService.getVacation(username, vacId).map(v -> {
			log.debug("Vacation received: " + v);
			if (v.getId() == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(v);
			}
		});
	}

}
