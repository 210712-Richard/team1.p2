package com.revature.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Activity;
import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.services.ActivityService;
import com.revature.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UserControllerTest {

	@InjectMocks
	private UserControllerImpl controller;

	@Mock
	private UserService userService;

	@Mock
	private ActivityService actService;

	private WebSession session;

	private User user;

	private Vacation vac;

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);

		user = new User();
		user.setUsername("test");
		user.setPassword("password");
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("test@email.com");
		user.setBirthday(LocalDate.now());
		user.setType(UserType.VACATIONER);

		vac = new Vacation();
		vac.setUsername(user.getUsername());
		vac.setId(UUID.randomUUID());
		vac.setDestination("Los Angeles, CA");
		vac.setPartySize(4);
		vac.setDuration(1);
		vac.setStartTime(LocalDateTime.now());
		vac.setEndTime(LocalDateTime.now().plus(Period.of(0, 0, vac.getDuration())));

		user.getVacations().add(vac.getId());

		session = Mockito.mock(WebSession.class);
		Mockito.when(session.getAttribute(UserController.LOGGED_USER)).thenReturn(user);
	}

	@Test
	void testLogout() {
		Mono<ResponseEntity<Void>> monoVoid = controller.logout(session);

		StepVerifier.create(monoVoid).expectNextMatches(re -> re.equals(ResponseEntity.noContent().build()))
				.verifyComplete();

		Mockito.verify(session).invalidate();
	}

	@Test
	void testLoginValid() {
		Mockito.when(userService.login(user.getUsername(), user.getPassword())).thenReturn(Mono.just(user));

		Mono<ResponseEntity<User>> monoUser = controller.login(user, session);

		StepVerifier.create(monoUser).expectNext(ResponseEntity.ok(user)).verifyComplete();
	}

	@Test
	void testLoginInvalidNotFound() {
		Mockito.when(userService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(Mono.just(new User()));

		Mono<ResponseEntity<User>> monoUser = controller.login(user, session);

		StepVerifier.create(monoUser).expectNext(ResponseEntity.notFound().build()).verifyComplete();

	}

	@Test
	void testLoginInvalidBadRequest() {
		Mono<ResponseEntity<User>> monoUser = controller.login(null, session);

		StepVerifier.create(monoUser).expectNext(ResponseEntity.badRequest().build()).verifyComplete();

		Mockito.verifyNoInteractions(userService);

	}

	@Test
	void testRegisterValid() {
		Mockito.when(userService.checkAvailability(user.getUsername())).thenReturn(Mono.just(false));
		Mockito.when(userService.register(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(),
				user.getLastName(), user.getBirthday(), user.getType())).thenReturn(Mono.just(user));

		Mono<ResponseEntity<User>> monoUser = controller.register(user, user.getUsername());

		StepVerifier.create(monoUser).expectNext(ResponseEntity.status(201).body(user)).verifyComplete();

	}

	@Test
	void testRegisterInvalidConflict() {
		Mockito.when(userService.checkAvailability(user.getUsername())).thenReturn(Mono.just(true));

		Mono<ResponseEntity<User>> monoUser = controller.register(user, user.getUsername());

		StepVerifier.create(monoUser).expectNext(ResponseEntity.status(409).build()).verifyComplete();

	}
	
	@Test
	void testRegisterInvalidNullPassword() {
		user.setPassword(null);
		
		Mono<ResponseEntity<User>> monoUser = controller.register(user, user.getUsername());

		StepVerifier.create(monoUser).expectNext(ResponseEntity.badRequest().build()).verifyComplete();

	}
	
	@Test
	void testRegisterInvalidNullEmail() {
		user.setEmail(null);
		
		Mono<ResponseEntity<User>> monoUser = controller.register(user, user.getUsername());

		StepVerifier.create(monoUser).expectNext(ResponseEntity.badRequest().build()).verifyComplete();
	}
	
	@Test
	void testRegisterInvalidNullFirstName() {
		user.setFirstName(null);
		
		Mono<ResponseEntity<User>> monoUser = controller.register(user, user.getUsername());

		StepVerifier.create(monoUser).expectNext(ResponseEntity.badRequest().build()).verifyComplete();
	}
	
	@Test
	void testRegisterInvalidNullLastName() {
		user.setLastName(null);
		
		Mono<ResponseEntity<User>> monoUser = controller.register(user, user.getUsername());

		StepVerifier.create(monoUser).expectNext(ResponseEntity.badRequest().build()).verifyComplete();
	}
	
	@Test
	void testRegisterInvalidNullBirthday() {
		user.setBirthday(null);
		
		Mono<ResponseEntity<User>> monoUser = controller.register(user, user.getUsername());

		StepVerifier.create(monoUser).expectNext(ResponseEntity.badRequest().build()).verifyComplete();
	}
	
	@Test
	void testRegisterInvalidNullType() {
		user.setType(null);
		
		Mono<ResponseEntity<User>> monoUser = controller.register(user, user.getUsername());

		StepVerifier.create(monoUser).expectNext(ResponseEntity.badRequest().build()).verifyComplete();
	}

	@Test
	void testDeleteUser() {
		Mockito.when(userService.login(user.getUsername(), user.getPassword())).thenReturn(Mono.just(user));
		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(vac));

		// Need to create a list to make sure the correct list is passed in the test
		List<Vacation> list = new ArrayList<>();
		list.add(vac);
		Mockito.when(userService.deleteUser(user.getUsername(), list)).thenReturn(Mono.empty());

		Mono<ResponseEntity<Void>> monoVoid = controller.deleteUser(user.getUsername(), session);

		StepVerifier.create(monoVoid).expectNextMatches(re -> re.getStatusCodeValue() == 204).verifyComplete();

		Mockito.verify(session).invalidate();

	}



	@Test
	void testGetVacationValid() {
		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(vac));

		Mono<ResponseEntity<Vacation>> monoVac = controller.getVacation(user.getUsername(), vac.getId().toString(),
				session);
		
		StepVerifier.create(monoVac).expectNext(ResponseEntity.ok(vac)).verifyComplete();
	}
	
	@Test
	void testGetVacationInvalidNotFound() {
		Mockito.when(userService.getVacation(user.getUsername(), vac.getId())).thenReturn(Mono.just(new Vacation()));

		Mono<ResponseEntity<Vacation>> monoVac = controller.getVacation(user.getUsername(), vac.getId().toString(),
				session);
		
		StepVerifier.create(monoVac).expectNext(ResponseEntity.notFound().build()).verifyComplete();
	}
	
	@Test
	void testGetVacationInvalidBadRequest() {
		String badId = "Bad ID";
		Mono<ResponseEntity<Vacation>> monoVac = controller.getVacation(user.getUsername(), badId,
				session);
		
		StepVerifier.create(monoVac).expectNext(ResponseEntity.badRequest().build()).verifyComplete();
	}
	
	@Test
	void testGetActivitiesValid() {
		Activity act1 = new Activity();
		act1.setId(UUID.randomUUID());
		act1.setName("Activity1");
		act1.setDescription("Description");
		act1.setLocation("Los Angeles, CA");
		act1.setMaxParticipants(100);
		act1.setDate(LocalDateTime.now());
		act1.setCost(19.99);
		
		Activity act2 = new Activity();
		act2.setId(UUID.randomUUID());
		act2.setName("Activity2");
		act2.setDescription("Description2");
		act2.setLocation("Los Angeles, CA");
		act2.setMaxParticipants(50);
		act2.setDate(LocalDateTime.now());
		act2.setCost(79.99);
		
		vac.getActivities().add(act1);
		vac.getActivities().add(act2);
		
		Mockito.when(userService.getActivities(vac.getId(), user.getUsername())).thenReturn(Flux.fromIterable(vac.getActivities()));
		
		ResponseEntity<Flux<Activity>> fluxActEntity = controller.getActivities(user.getUsername(), vac.getId().toString(), session);
		
		assertEquals(200, fluxActEntity.getStatusCodeValue(), "Assert that the status code is 200");
		
		Flux<Activity> fluxAct = fluxActEntity.getBody();
		
		StepVerifier.create(fluxAct).expectNext(act1).expectNext(act2).verifyComplete();
	}
	
	@Test
	void testGetActivitiesInvalid() {
		String wrongId = "Wrong ID";
		ResponseEntity<Flux<Activity>> fluxActEntity = controller.getActivities(user.getUsername(), wrongId, session);
		
		assertEquals(400, fluxActEntity.getStatusCodeValue(), "Assert that a 400 is the status code");
		
	}


}
