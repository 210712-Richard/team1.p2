package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Hotel;

import reactor.core.publisher.Flux;

public interface HotelController {
	
	/**
	 * Get all the hotels by a location
	 * @param location The location to lookup
	 * @param session The WebSession
	 * @return A ResponseEntity of a Flux of hotels
	 */
	public ResponseEntity<Flux<Hotel>> getHotelsByLocation(String location, WebSession session);
}
