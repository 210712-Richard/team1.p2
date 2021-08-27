package com.revature.services;

import java.util.UUID;

import com.revature.beans.Hotel;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HotelService {
	/**
	 * Get a specific hotel
	 * @param location The location of the hotel
	 * @param id The id of the hotel
	 * @return The hotel
	 */
	public Mono<Hotel> getHotel(String location, UUID id);
	
	/**
	 * Get all the hotels for a location
	 * @param location The location of the hotels
	 * @return The hotels in the location
	 */
	public Flux<Hotel> getHotelsByLocation(String location);
}