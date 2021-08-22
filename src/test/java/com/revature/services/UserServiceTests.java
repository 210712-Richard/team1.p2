package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.controllers.UserControllerImpl;
import com.revature.data.UserDao;
import com.revature.dto.UserDto;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class UserServiceTests {
	// Tells Spring to put mocks into service
	@InjectMocks
	private UserServiceImpl service;

	// Tells Spring what to mock
	@Mock
	private UserDao userDao;

	private User user;

	@BeforeAll
	public static void beforeAll() {

	}

	@BeforeEach
	public void beforeEach() {
		// Initializes the mocks
		MockitoAnnotations.openMocks(this);

		user = new User();
		user.setUsername("test");
		user.setPassword("password");
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("test@email.com");
		user.setBirthday(LocalDate.now());
		user.setType(UserType.VACATIONER);
	}

	@Test
	public void testLoginValid() {
		ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);

		Mockito.when(userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword()))
				.thenReturn(Mono.just(new UserDto(user)));

		Mono<User> monoUser = service.login(user.getUsername(), user.getPassword());

		Mockito.verify(userDao).findByUsernameAndPassword(usernameCaptor.capture(), passwordCaptor.capture());

		StepVerifier.create(monoUser).expectNextMatches(u -> u.equals(user)).verifyComplete();

		assertEquals(user.getUsername(), usernameCaptor.getValue(),
				"Assert that the username passed in is the same username.");
		assertEquals(user.getPassword(), passwordCaptor.getValue(),
				"Assert that the password passed in is the same password.");

	}

	@Test
	public void testLoginInvalid() {
		ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);

		Mockito.when(userDao.findByUsernameAndPassword(user.getUsername(), "pass"))
				.thenReturn(Mono.just(new UserDto(user)));

		Mono<User> monoUser = service.login(user.getUsername(), "pass");

		Mockito.verify(userDao).findByUsernameAndPassword(usernameCaptor.capture(), passwordCaptor.capture());

		StepVerifier.create(monoUser).expectError(NullPointerException.class);

		assertEquals(user.getUsername(), usernameCaptor.getValue(),
				"Assert that the username passed in is the same username.");
		assertNotEquals(user.getPassword(), passwordCaptor.getValue(),
				"Assert that the password passed in is different from user's password.");

	}
	
	@Test
	public void testLogout() {

	}

	@Test
	public void testRegisterValid() {

	}

	@Test
	public void testRegisterInvalid() {

	}

}
