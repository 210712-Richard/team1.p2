package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.beans.Activity;
import com.revature.beans.Reservation;
import com.revature.beans.ReservationType;
import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.data.ActivityDao;
import com.revature.data.ReservationDao;
import com.revature.data.UserDao;
import com.revature.data.VacationDao;
import com.revature.dto.ActivityDto;
import com.revature.dto.ReservationDto;
import com.revature.dto.UserDto;
import com.revature.dto.VacationDto;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UserServiceTests {
	// Tells Spring to put mocks into service
	@InjectMocks
	private UserServiceImpl service;

	// Tells Spring what to mock
	@Mock
	private UserDao userDao;

	@Mock
	private VacationDao vacDao;

	@Mock
	private ReservationDao resDao;
	@Mock
	private ActivityDao actDao;

	private User user;

	private Vacation vac;

	private Reservation res;
	
	private Activity act;
	
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

		vac = new Vacation();
		vac.setUsername(user.getUsername());
		vac.setId(UUID.randomUUID());
		vac.setDestination("Los Angeles, CA");
		vac.setPartySize(4);
		vac.setDuration(1);
		vac.setStartTime(LocalDateTime.now());
		vac.setEndTime(LocalDateTime.now().plus(Period.of(0, 0, 1)));
		
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
	void testLoginValid() {
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
	void testLoginInvalid() {
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
	void testRegisterValid() {

		// Capture arguments
		ArgumentCaptor<UserDto> userCaptor = ArgumentCaptor.forClass(UserDto.class);
		// Set Mock returns
		Mockito.when(userDao.save(Mockito.any())).thenReturn(Mono.just(new UserDto(user)));
		// Call the method
		Mono<User> monoUser = service.register(user.getUsername(), user.getPassword(), user.getEmail(),
				user.getFirstName(), user.getLastName(), user.getBirthday(), user.getType());

		// Verify the mono and flux are correct
		StepVerifier.create(monoUser).expectNextMatches(u -> user.equals(u)).verifyComplete();
		// Verify methods inside were called
		Mockito.verify(userDao).save(userCaptor.capture());

		// Make sure the captured value is correct
		User capUser = userCaptor.getValue().getUser();
		assertEquals(user.getUsername(), capUser.getUsername(), "Assert that user and capUser have the same username.");
		assertEquals(user.getPassword(), capUser.getPassword(), "Assert that user and capUser have the same password.");
		assertEquals(user.getEmail(), capUser.getEmail(), "Assert that the user and capUser have the same email.");
		assertEquals(user.getBirthday(), capUser.getBirthday(),
				"Assert that the user and capUser have the same birthday.");
		assertEquals(user.getType(), capUser.getType(), "Assert that the user and capUser have the same type.");
		assertEquals(user.getFirstName(), capUser.getFirstName(),
				"Assert that the user and capUser have the same first name.");
		assertEquals(user.getLastName(), capUser.getLastName(),
				"Assert that the user and capUser have the same last name.");

	}
	
	@Test
	void testCreateVacationValid() {

		Mockito.when(userDao.findByUsername(user.getUsername())).thenReturn(Mono.just(new UserDto(user)));
		Mockito.when(userDao.save(Mockito.any())).thenReturn(Mono.just(new UserDto(user)));
		Mockito.when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		Mono<Vacation> monoVac = service.createVacation(user.getUsername(), vac.getDestination(), vac.getStartTime(),
				vac.getEndTime(), vac.getPartySize(), vac.getDuration());

		StepVerifier.create(monoVac).expectNextMatches(v -> {
			return user.getUsername().equals(v.getUsername()) && vac.getDestination().equals(v.getDestination())
					&& vac.getStartTime().equals(v.getStartTime()) && vac.getEndTime().equals(v.getEndTime())
					&& vac.getPartySize().equals(v.getPartySize()) && vac.getDuration().equals(v.getDuration())
					&& v.getId() != null && v.getActivities() != null && v.getReservations() != null
					&& v.getTotal() == 0.0;
		}).verifyComplete();
	}

	@Test
	void testCreateVacationInvalid() {

		// Check if end date is before start date
		Vacation vac = new Vacation();
		vac.setUsername(user.getUsername());
		vac.setId(UUID.randomUUID());
		vac.setDestination("Los Angeles, CA");
		vac.setPartySize(4);
		vac.setDuration(1);
		vac.setStartTime(LocalDateTime.now());
		vac.setEndTime(LocalDateTime.now().minus(Period.of(0, 0, 1)));

		Mono<Vacation> monoVac = service.createVacation(user.getUsername(), vac.getDestination(), vac.getStartTime(),
				vac.getEndTime(), vac.getPartySize(), vac.getDuration());

		StepVerifier.create(monoVac).expectNextMatches(v -> new Vacation().equals(v)).verifyComplete();

		// Check if same date return empty vacation
		vac.setEndTime(vac.getStartTime());

		monoVac = service.createVacation(user.getUsername(), vac.getDestination(), vac.getStartTime(), vac.getEndTime(),
				vac.getPartySize(), vac.getDuration());

		StepVerifier.create(monoVac).expectNextMatches(v -> new Vacation().equals(v)).verifyComplete();

		// Make sure the daos are never called.
		Mockito.verifyNoInteractions(vacDao);
		Mockito.verifyNoInteractions(userDao);
	}

	@Test
	void testCheckAvailabilityValid() {
		Mockito.when(userDao.existsByUsername(user.getUsername())).thenReturn(Mono.just(true));

		Mono<Boolean> monoBool = service.checkAvailability(user.getUsername());

		StepVerifier.create(monoBool).expectNextMatches(b -> b.equals(true)).verifyComplete();
	}

	@Test
	void testCheckAvailabilityInvalid() {
		Mockito.when(userDao.existsByUsername("wrong")).thenReturn(Mono.just(false));

		Mono<Boolean> monoBool = service.checkAvailability("wrong");

		StepVerifier.create(monoBool).expectNextMatches(b -> b.equals(false)).verifyComplete();
	}

	@Test
	void testGetVacationValidEmptyList() {
		Mockito.when(vacDao.findByUsernameAndId(user.getUsername(), vac.getId()))
				.thenReturn(Mono.just(new VacationDto(vac)));

		Mono<Vacation> vacMono = service.getVacation(user.getUsername(), vac.getId());

		StepVerifier.create(vacMono).expectNextMatches(vDto -> vDto.equals(vac)).verifyComplete();

		Mockito.verifyNoInteractions(resDao);

	}

	@Test
	void testGetVacationValidNullList() {
		vac.setReservations(null);

		Mockito.when(vacDao.findByUsernameAndId(user.getUsername(), vac.getId()))
				.thenReturn(Mono.just(new VacationDto(vac)));

		Mono<Vacation> vacMono = service.getVacation(user.getUsername(), vac.getId());

		StepVerifier.create(vacMono).expectNextMatches(vDto -> {
			return vac.getId().equals(vDto.getId()) && vac.getActivities().equals(vDto.getActivities())
					&& vac.getDestination().equals(vDto.getDestination())
					&& vac.getDuration().equals(vDto.getDuration()) && vac.getStartTime().equals(vDto.getStartTime())
					&& vac.getEndTime().equals(vDto.getEndTime()) && vac.getPartySize().equals(vDto.getPartySize())
					&& vac.getUsername().equals(vDto.getUsername()) && vac.getTotal().equals(vDto.getTotal())
					&& vDto.getReservations() != null;
		}).verifyComplete();

		Mockito.verifyNoInteractions(resDao);
	}

	@Test
	void testGetVacationValidNonEmptyList() {

		// Create a reservation to be added to the list
		Reservation res = new Reservation();
		res.setUsername(vac.getUsername());
		res.setVacationId(vac.getId());
		res.setId(UUID.randomUUID());
		res.setDuration(vac.getDuration());
		res.setType(ReservationType.HOTEL);
		res.setStarttime(vac.getStartTime());

		vac.getReservations().add(res);

		Mockito.when(vacDao.findByUsernameAndId(user.getUsername(), vac.getId()))
				.thenReturn(Mono.just(new VacationDto(vac)));
		Mockito.when(resDao.findByUuid(res.getId())).thenReturn(Mono.just(new ReservationDto(res)));

		Mono<Vacation> vacMono = service.getVacation(user.getUsername(), vac.getId());

		StepVerifier.create(vacMono).expectNextMatches(vDto -> vDto.equals(vac)).verifyComplete();

	}

	@Test
	void testGetVacationInvalid() {
		String wrongUsername = "Wrong Username";
		
		Mockito.when(vacDao.findByUsernameAndId(wrongUsername, vac.getId()))
				.thenReturn(Mono.empty());

		Mono<Vacation> vacMono = service.getVacation(wrongUsername, vac.getId());

		StepVerifier.create(vacMono).expectNextMatches(vDto -> vDto.getId() == null).verifyComplete();
	}
	
	
	@Test
	void testGetActivities() {

		List<Activity> vacActs = vac.getActivities();
		vacActs.add(act);
		vac.setActivities(vacActs);
		
		Mockito.when(vacDao.findByUsernameAndId(user.getUsername(), vac.getId()))
			.thenReturn(Mono.just(new VacationDto(vac)));
		Mockito.when(actDao.findByLocationAndId(vac.getDestination(), act.getId()))
		.thenReturn(Mono.just(new ActivityDto(act)));
		Mockito.when(actDao.findByLocationAndId(null, act.getId()))
		.thenReturn(Mono.empty());
		Mockito.when(actDao.findByLocationAndId(vac.getDestination(), null))
		.thenReturn(Mono.empty());
		Mockito.when(actDao.findByLocationAndId(null,null))
		.thenReturn(Mono.empty());
	
		StepVerifier.create(service.getActivities(vac.getId(), user.getUsername())).expectNext(act).verifyComplete();
		StepVerifier.create(service.getActivities(null, user.getUsername())).verifyComplete();
		StepVerifier.create(service.getActivities(vac.getId(), null)).verifyComplete();
		StepVerifier.create(service.getActivities(null,null)).verifyComplete();
		
	}
	
	@Test
	void testdeleteUser() {
		
		 List<Vacation> vacList = new ArrayList<Vacation>();
		 vacList.add(vac);
		 Mockito.when(userDao.deleteByUsername(user.getUsername())).thenReturn(Mono.empty());
		 Mockito.when(resDao.deleteByUuid(Mockito.any())).thenReturn(Mono.empty());
		 Mockito.when(vacDao.deleteByUsername(user.getUsername())).thenReturn(Mono.empty());
		 ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
		 ArgumentCaptor<String> vacUsernameCaptor = ArgumentCaptor.forClass(String.class);
			
	     Mono<Void> monoUser = service.deleteUser(user.getUsername(), vacList);
	        
	        StepVerifier.create(monoUser).expectComplete().verify();
	        Mockito.verify(userDao).deleteByUsername(usernameCaptor.capture());
	        Mockito.verify(vacDao).deleteByUsername(vacUsernameCaptor.capture());
	        
	        assertEquals(user.getUsername(), usernameCaptor.getValue(),"Assert username passed in is the same username.");
	        assertEquals(user.getUsername(), vacUsernameCaptor.getValue(),"Assert username passed in is the same username.");
	}
	
	@Test
	void testdeleteUserInvalid() {
		
		 List<Vacation> vacList = new ArrayList<Vacation>();
		 vacList.add(null);
		 Mockito.when(userDao.deleteByUsername(user.getUsername())).thenReturn(Mono.empty());
		 Mockito.when(resDao.deleteByUuid(Mockito.any())).thenReturn(Mono.empty());
		 Mockito.when(vacDao.deleteByUsername(user.getUsername())).thenReturn(Mono.empty());
		 ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
		 ArgumentCaptor<String> vacUsernameCaptor = ArgumentCaptor.forClass(String.class);
			
	     Mono<Void> monoUser = service.deleteUser(user.getUsername(), vacList);
	        
	        StepVerifier.create(monoUser).expectError(NullPointerException.class);
	        Mockito.verify(userDao).deleteByUsername(usernameCaptor.capture());
	        Mockito.verify(vacDao).deleteByUsername(vacUsernameCaptor.capture());
	        
	        assertEquals(user.getUsername(), usernameCaptor.getValue(),"Assert username passed in is the same username.");
	        assertEquals(user.getUsername(), vacUsernameCaptor.getValue(),"Assert username passed in is the same username.");
	}
	
	@Test
	void testChooseActivities() {
		
      Mockito.when(vacDao.findByUsernameAndId(user.getUsername(), vac.getId())).thenReturn(Mono.just(new VacationDto(vac)));
	  Mockito.when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
	  Mockito.when(actDao.save(Mockito.any())).thenReturn(Mono.just(new ActivityDto(act)));
	
	  Mono<Activity> monoAct = service.chooseActivities(user.getUsername(), vac.getId(), act);
    
	  StepVerifier.create(monoAct).expectNextMatches(aDto -> aDto.equals(act)).verifyComplete();
	}
}
	   
		
		
		
		
        	
	
	



	