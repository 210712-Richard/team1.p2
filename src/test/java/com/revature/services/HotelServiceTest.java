package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.beans.Hotel;
import com.revature.data.HotelDao;
import com.revature.dto.HotelDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class HotelServiceTest {
	@InjectMocks
	private HotelServiceImpl service;
	
	@Mock
	private HotelDao hotelDao;
	
	private Hotel hotel;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		
		hotel = new Hotel();
		hotel.setId(UUID.randomUUID());
		hotel.setLocation("Los Angeles, CA");
		hotel.setName("Test Hotel");
		hotel.setRoomsAvailable(20);
		hotel.setCostPerNight(59.99);
	}
	
	@Test
	void testGethotelValid() {
		Mockito.when(hotelDao.findByLocationAndId(hotel.getLocation(), hotel.getId())).thenReturn(Mono.just(new HotelDto(hotel)));
		
		Mono<Hotel> monohotel = service.getHotel(hotel.getLocation(), hotel.getId());
		
		StepVerifier.create(monohotel).expectNext(hotel).verifyComplete();
		
	}
	
	@Test
	void testGethotelInvalidLocation() {
		String invalidLocation = "San Diego, CA";
		
		ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
		
		Mockito.when(hotelDao.findByLocationAndId(Mockito.anyString(), Mockito.any())).thenReturn(Mono.empty());
		
		Mono<Hotel> monohotel = service.getHotel(invalidLocation, hotel.getId());
		
		StepVerifier.create(monohotel).expectNextMatches(c -> c.getId() == null).verifyComplete();
		
		Mockito.verify(hotelDao).findByLocationAndId(stringCaptor.capture(), uuidCaptor.capture());
		
		assertEquals(invalidLocation, stringCaptor.getValue(), "Assert that the string passed in is the correct string");
		assertEquals(hotel.getId(), uuidCaptor.getValue(), "Assert that the uuid passed is in the correct uuid");
		
	}
	
	@Test
	void testGetHotelInvalidId() {
		UUID invalidId = UUID.randomUUID();
		
		ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
		
		Mockito.when(hotelDao.findByLocationAndId(Mockito.anyString(), Mockito.any())).thenReturn(Mono.empty());
		
		Mono<Hotel> monohotel = service.getHotel(hotel.getLocation(), invalidId);
		
		StepVerifier.create(monohotel).expectNextMatches(c -> c.getId() == null).verifyComplete();
		
		Mockito.verify(hotelDao).findByLocationAndId(stringCaptor.capture(), uuidCaptor.capture());
		
		assertEquals(hotel.getLocation(), stringCaptor.getValue(), "Assert that the string passed in is the correct string");
		assertEquals(invalidId, uuidCaptor.getValue(), "Assert that the uuid passed is in the correct uuid");
		
	}
	
	@Test
	void testGetHotelsByLocationValid() {
		Hotel hotel2 = new Hotel();
		hotel2.setId(UUID.randomUUID());
		hotel2.setLocation(hotel.getLocation());
		hotel2.setName("Test2 Hotel");
		hotel2.setRoomsAvailable(70);
		hotel2.setCostPerNight(49.99);
		
		HotelDto[] hotels = {new HotelDto(hotel), new HotelDto(hotel2)};
		
		Mockito.when(hotelDao.findByLocation(hotel.getLocation())).thenReturn(Flux.fromArray(hotels));
		
		Flux<Hotel> fluxHotels = service.getHotelsByLocation(hotel.getLocation());
		
		StepVerifier.create(fluxHotels).expectNext(hotel).expectNext(hotel2).verifyComplete();
		
	}
	
	@Test
	void tetGetHotelsByLocationInvalid() {
		
		Mockito.when(hotelDao.findByLocation(hotel.getLocation())).thenReturn(Flux.empty());
		
		Flux<Hotel> fluxHotels = service.getHotelsByLocation(hotel.getLocation());
		
		StepVerifier.create(fluxHotels).expectComplete().verify();
		
	}

}
