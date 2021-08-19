package com.revature.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.beans.User;
import com.revature.beans.UserType;
import com.revature.beans.Vacation;
import com.revature.data.UserDao;
import com.revature.data.VacationDao;
import com.revature.dto.UserDto;
import com.revature.dto.VacationDto;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class UserServiceTests {

	// Tells Spring to put mocks into service
	@InjectMocks
	private UserServiceImpl service;

	// Tells Spring what to mock
	@Mock
	private UserDao userDao;

	@Mock
	private VacationDao vacDao;

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
		
	}

	@Test
	public void testLoginInvalid() {
		
	}

	@Test
	public void testRegisterValid() {
		
	}

	@Test
	public void testRegisterInvalid() {

	}

	@Test
	void testCreateVacationValid() {
		Vacation vac = new Vacation();
		vac.setUsername(user.getUsername());
		vac.setId(UUID.randomUUID());
		vac.setDestination("Los Angeles, CA");
		vac.setPartySize(4);
		vac.setDuration(1);
		vac.setStartTime(LocalDateTime.now());
		vac.setEndTime(LocalDateTime.now().plus(Period.of(0, 0, 1)));

		Mockito.when(userDao.findByUsername(user.getUsername())).thenReturn(Mono.just(new UserDto(user)));
		Mockito.when(userDao.save(Mockito.any())).thenReturn(Mono.just(new UserDto(user)));
		Mockito.when(vacDao.save(Mockito.any())).thenReturn(Mono.just(new VacationDto(vac)));
		Mono<Vacation> monoVac = service.createVacation(user.getUsername(), vac.getDestination(), vac.getStartTime(),
				vac.getEndTime(), vac.getPartySize(), vac.getDuration());

		StepVerifier.create(monoVac)
		.expectNextMatches(v -> {
			return user.getUsername().equals(v.getUsername()) && vac.getDestination().equals(v.getDestination())
					&& vac.getStartTime().equals(v.getStartTime()) && vac.getEndTime().equals(v.getEndTime())
					&& vac.getPartySize().equals(v.getPartySize()) && vac.getDuration().equals(v.getDuration())
					&& v.getId() != null && v.getActivities() != null && v.getReservations() != null
					&& v.getTotal() == 0.0;
		})
		.verifyComplete();
	}

	@Test
	void testCreateVacationInvalid() {
		
		//Check if end date is before start date
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

		StepVerifier.create(monoVac)
		.expectNextMatches(v -> new Vacation().equals(v))
		.verifyComplete();
		
		//Check if same date return empty vacation
		vac.setEndTime(vac.getStartTime());

		monoVac = service.createVacation(user.getUsername(), vac.getDestination(), vac.getStartTime(),
				vac.getEndTime(), vac.getPartySize(), vac.getDuration());

		StepVerifier.create(monoVac)
		.expectNextMatches(v -> new Vacation().equals(v))
		.verifyComplete();
		
		//Make sure the daos are never called.
		Mockito.verifyNoInteractions(vacDao);
		Mockito.verifyNoInteractions(userDao);
	}
	
}