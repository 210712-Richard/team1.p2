package com.revature.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.revature.dto.ActivityDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ActivityDao extends ReactiveCassandraRepository<ActivityDto, String> {
	Flux<ActivityDto> findByLocation(String location);
	Mono<ActivityDto> findByLocationAndId(String location, UUID id);
}