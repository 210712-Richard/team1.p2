package com.revature.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.beans.Activity;
import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.data.ActivityDao;
import com.revature.dto.ActivityDto;

import reactor.core.publisher.Flux;

class ActivityServiceTest {
	
	@Mock
	private ActivityDao actDao;
	
	private User user;
	
	private Activity act;

	private Vacation vac;

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
		
		act = new Activity();
		act.setLocation("Los Angeles, CA");
		act.setId(UUID.randomUUID());
		act.setName("TestActivity");
		act.setDescription("A test activity");
		act.setCost(400.00);
		act.setDate(LocalDateTime.now().plusDays(2));
		act.setMaxParticipants(5);
		
	}
	
	@Test
	void testGetAllActivitiesInvalidLocation() {
		Mockito.when(actDao.findByLocation(null))
		.thenReturn(Flux.empty());
	}
	
	@Test
	void testGetAllActivitiesEmptyLocation() {
		Mockito.when(actDao.findByLocation("The Moon"))
		.thenReturn(Flux.empty());
	}
	
	@Test
	void testGetAllActivitiesValid() {
		Mockito.when(actDao.findByLocation(act.getLocation()))
		.thenReturn(Flux.just(new ActivityDto(act)));
	}

}
