package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.data.UserDao;
import com.revature.dto.UserDto;


public class UserServiceTests {
	@InjectMocks
   private static UserServiceImpl service;
	@Mock
    private static UserDao userDao;
	private static User user;
	
	
	@BeforeAll
	public static void beforeAll() {
		
	}
	
	@BeforeEach
	public void beforeEach() {
		//Initializes the mocks
		MockitoAnnotations.openMocks(this);
		
		user = new User();
		user.setUsername("test");
		user.setPassword("password");
		user.setEmail("test@email.com");
		user.setFirstName("Test");
		user.setLastName("User");
		user.setBirthday(LocalDate.now());
		user.setType(UserType.VACTIONER);
	}
	
	@Test
	public void testLoginValid() {
			ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
			ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);

			Mockito.when(userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(Mono.just(new UserDto(user)));

			Mono<User> monoUser = service.login(user.getUsername(), user.getPassword());

			Mockito.verify(userDao).findByUsernameAndPassword(usernameCaptor.capture(), passwordCaptor.capture());

			StepVerifier.create(monoUser).expectNextMatches(u -> u.equals(user)).verifyComplete();
			
			assertEquals(user.getUsername(), usernameCaptor.getValue(), "Assert that the username passed in is the same username.");
			assertEquals(user.getPassword(), passwordCaptor.getValue(), "Assert that the password passed in is the same password.");

			
			
		}

	
	@Test
	public void testLoginInvalid() {
		
	}
	
	@Test
	public void testRegisterValid() {
		//Capture arguments
		ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
        //Set Mock returns
		Mockito.when(userDao.save(Mockito.any())).thenReturn(Mono.just(new UserDto(user)));
		//Call the method
		Mono<User> monoUser = service.register(user.getUsername(), user.getPassword(), user.getEmail(),
				user.getFirstName(), user.getLastName(), user.getBirthday(), user.getType());
		
		//Verify the mono and flux are correct
		StepVerifier.create(monoUser).expectNextMatches(u->user.equals(u)).verifyComplete();
		//Verify methods inside were called
		Mockito.verify(userDao).save(userCaptor.capture());
		
		// Make sure the captured value is correct
		User capUser = userCaptor.getValue().getUser();
		assertEquals(user.getUsername(), capUser.getUsername(), "Assert that user and capUser have the same username.");
		assertEquals(user.getPassword(), capUser.getPassword(), "Assert that user and capUser have the same password.");
		assertEquals(user.getEmail(), capUser.getEmail(), "Assert that the user and capUser have the same email.");
		assertEquals(user.getBirthday(), capUser.getBirthday(), "Assert that the user and capUser have the same birthday.");
		assertEquals(user.getType(), capUser.getType(), "Assert that the user and capUser have the same type.");
		assertEquals(user.getFirstName(), capUser.getFirstName(), "Assert that the user and capUser have the same first name.");
		assertEquals(user.getLastName(), capUser.getLastName(), "Assert that the user and capUser have the same last name.");

	}
	
	@Test
	public void testRegisterInvalid() {
		
	}
}
