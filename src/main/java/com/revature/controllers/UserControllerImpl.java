package com.revature.controllers;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import com.revature.aspects.LoggedInFlux;
import com.revature.aspects.LoggedInMono;
import com.revature.aspects.VacationerCheck;
import com.revature.beans.Activity;
import com.revature.beans.User;
import com.revature.beans.Vacation;
import com.revature.services.ActivityService;
import com.revature.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {
	private static Logger log = LogManager.getLogger(UserControllerImpl.class);

	UserService userService;
	ActivityService activityService;

	@Autowired
	public UserControllerImpl(UserService userService, ActivityService activityService) {
		this.userService = userService;
		this.activityService = activityService;
	}

	@PostMapping
	public Mono<ResponseEntity<User>> login(@RequestBody User user, WebSession session) {
		if (user == null || user.getUsername() == null || user.getPassword() == null) {
			log.debug("Username and/or password were not found");
			return Mono.just(ResponseEntity.badRequest().build());
		}

		return userService.login(user.getUsername(), user.getPassword()).single().map(u -> {
			log.debug("User found: {}", u);
			if (u.getUsername() == null) {
				return ResponseEntity.notFound().build();
			}

			else {
				session.getAttributes().put(LOGGED_USER, u);
				return ResponseEntity.ok(u);
			}
		});
	}

	@LoggedInMono
	@DeleteMapping
	public Mono<ResponseEntity<Void>> logout(WebSession session) {
		session.invalidate();
		return Mono.just(ResponseEntity.noContent().build());
	}

	@PutMapping("{username}")
	public Mono<ResponseEntity<User>> register(@RequestBody User u, @PathVariable("username") String username) {
		
		//Check to make sure none of the fields are null
		if (u.getPassword() == null || u.getEmail() == null || u.getFirstName() == null 
				|| u.getLastName() == null
				|| u.getBirthday() == null || u.getType() == null) {
			log.debug("One of the user fields are null");
			return Mono.just(ResponseEntity.badRequest().build());
		}
		// check to see if that username is available
		return userService.checkAvailability(username).flatMap(b -> {
			if (Boolean.FALSE.equals(b)) {
				log.debug("User is being registered");
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
		LocalDateTime endTime = vacation.getStartTime().plus(Period.of(0, 0, vacation.getDuration()));
		
		return userService.createVacation(username, vacation.getDestination(), vacation.getStartTime(),
				endTime, vacation.getPartySize(), vacation.getDuration()).flatMap(v -> {
					
					if (v.getId() == null) {
						return Mono.just(ResponseEntity.badRequest().build());
					} else {
						return Mono.just(ResponseEntity.status(201).body(v));
					}
				});
	}

	@VacationerCheck
	@Override
	@GetMapping("{username}/vacations/{vacationid}")
	public Mono<ResponseEntity<Vacation>> getVacation(@PathVariable("username") String username,
			@PathVariable("vacationid") String id, WebSession session) {
		UUID vacId = null;
		try {
			vacId = UUID.fromString(id);
		} catch (Exception e) {
			log.debug("ID wasn't a UUID");
			return Mono.just(ResponseEntity.badRequest().build());
		}
		return userService.getVacation(username, vacId).map(v -> {
			log.debug("Vacation received: {}", v);
			if (v.getId() == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(v);
			}
		});
	}

	@LoggedInFlux
	@GetMapping(value = "{username}/vacations/{vacationid}/activities", produces = MediaType.APPLICATION_NDJSON_VALUE)
	public ResponseEntity<Flux<Activity>> getActivities(@PathVariable("username") String username,
			@PathVariable("vacationid") String id, WebSession session) {
		UUID vacId = null;
		try {
			vacId = UUID.fromString(id);
		} catch (Exception e) {
			log.debug("ID wasn't a UUID");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(userService.getActivities(vacId, username));
	}

	@VacationerCheck
	@DeleteMapping("{username}")
	public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("username") String username, WebSession session) {
		User loggedUser = (User) session.getAttribute(LOGGED_USER);
		loggedUser = loggedUser == null ? new User() : loggedUser;
		session.invalidate();
		return Flux.from(userService.login(username, loggedUser.getPassword()))
				.map(u -> u.getVacations())
				.flatMap(l -> Flux.fromIterable(l))
				.flatMap(uuid -> userService.getVacation(username, uuid))
				.collectList().map(list -> userService.deleteUser(username, list))
				.map(v -> ResponseEntity.status(204).build());
	}

	@Override
	@PostMapping("{username}/vacations/{vacationid}/activities")
	public Mono<ResponseEntity<Activity>> chooseActivities(@RequestBody Activity activity,
			@PathVariable("username") String username, @PathVariable("vacationid") String id, WebSession session) {
		UUID vacId = null;
		try {
			vacId = UUID.fromString(id);
		} catch (Exception e) {
			return Mono.just(ResponseEntity.badRequest().build());
		}

		return userService.chooseActivities(username, vacId, activity).flatMap(a -> {
			if (a.getId() == null) {
				return Mono.just(ResponseEntity.status(409).build());
			} else {
				return Mono.just(ResponseEntity.status(200).body(a));
			}
		});

	}

}
