package com.revature.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.revature.dto.FlightDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FlightDao extends ReactiveCassandraRepository<FlightDto, String> {
	Flux<FlightDto> findByDestination(String destination);

	Mono<FlightDto> findByDestinationAndId(String destination, UUID id);
}
