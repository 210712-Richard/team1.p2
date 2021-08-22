package com.revature.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.WebSession;

import com.revature.beans.Hotel;

import reactor.core.publisher.Flux;

public interface HotelController {
	
	public ResponseEntity<Flux<Hotel>> getHotelsByLocation(String location, WebSession session);
}
