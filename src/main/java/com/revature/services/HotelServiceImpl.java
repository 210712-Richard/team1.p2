package com.revature.services;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.beans.Hotel;
import com.revature.data.HotelDao;

import reactor.core.publisher.Mono;

@Service
public class HotelServiceImpl implements HotelService{
	private static Logger log = LogManager.getLogger(HotelServiceImpl.class);
	
	private HotelDao hotelDao;
	 @Autowired 
	 public HotelServiceImpl (HotelDao hotelDao) {
		 this.hotelDao = hotelDao;
		 
	
	 }
	 @Override
	 public Mono <Hotel> getHotel(String location, UUID id) {
		 return hotelDao.findByLocationAndId(location, id)
				 .map(hDto-> hDto.getHotel())
				 .switchIfEmpty(Mono.just(new Hotel()));
	 }
}
