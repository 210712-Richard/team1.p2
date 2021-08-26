package com.revature.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
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
import com.revature.services.ActivityService;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ActivityControllerTest {
	
	@InjectMocks
	private ActivityControllerImpl controller;
	
	@Mock
	private ActivityService actService;
	
	private WebSession session;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		session = Mockito.mock(WebSession.class);
	}
	
	@Test
	void testViewAllActivitiesNotEmpty() {
		String location = "Location";
		
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
		
		List<Activity> acts = new ArrayList<>();
		acts.add(act1);
		acts.add(act2);
		
		Mockito.when(actService.getAllActivities(location)).thenReturn(Flux.fromIterable(acts));
		
		ResponseEntity<Flux<Activity>> fluxActEntity = controller.viewAllActivities(location, session);
		
		assertEquals(200, fluxActEntity.getStatusCodeValue(), "Assert that the status code is 200");
		
		Flux<Activity> fluxAct = fluxActEntity.getBody();
		
		StepVerifier.create(fluxAct).expectNext(act1).expectNext(act2).verifyComplete();
		
	}
	
	@Test
	void testViewAllActivitiesEmpty() {
		String location = "Location";
		
		Mockito.when(actService.getAllActivities(location)).thenReturn(Flux.empty());
		
		ResponseEntity<Flux<Activity>> fluxActEntity = controller.viewAllActivities(location, session);
		
		assertEquals(200, fluxActEntity.getStatusCodeValue(), "Assert that the status code is 200");
		
		Flux<Activity> fluxAct = fluxActEntity.getBody();
		
		StepVerifier.create(fluxAct).verifyComplete();
	}
}


