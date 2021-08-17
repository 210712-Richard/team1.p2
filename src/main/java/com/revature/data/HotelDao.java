package com.revature.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.revature.dto.HotelDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface HotelDao extends ReactiveCassandraRepository<HotelDto, String> {
	Flux<HotelDto> findByLocation(String location);

	Mono<HotelDto> findByLocationAndId(String location, UUID id);
}
