package com.revature.services;

import java.util.UUID;

import com.revature.beans.Hotel;

import reactor.core.publisher.Mono;

public interface HotelService {
	public Mono<Hotel> getHotel(String location, UUID id);
}
