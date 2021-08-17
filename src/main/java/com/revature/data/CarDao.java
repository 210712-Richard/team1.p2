package com.revature.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.revature.dto.CarDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CarDao extends ReactiveCassandraRepository<CarDto, String> {
	Flux<CarDto> findByLocation(String location);

	Mono<CarDto> findByLocationAndId(String location, UUID id);
}
