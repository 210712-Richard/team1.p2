package com.revature.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.beans.Hotel;
import com.revature.services.HotelService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/hotels")
public class HotelControllerImpl implements HotelController{
	
	private HotelService hotelService;

	@Autowired
	public HotelControllerImpl(HotelService hotelService) {
		this.hotelService = hotelService;
	}

	@Override
	@GetMapping("{location}")
	public ResponseEntity<Flux<Hotel>> getHotelsByLocation(@PathVariable("location") String location, WebSession session) {
		return ResponseEntity.ok(hotelService.getHotelsByLocation(location));
	}
	
	
	
}
