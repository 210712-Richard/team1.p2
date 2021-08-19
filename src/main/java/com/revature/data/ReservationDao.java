package com.revature.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.revature.dto.ReservationDto;

import reactor.core.publisher.Mono;

@Repository
public interface ReservationDao extends ReactiveCassandraRepository<ReservationDto, UUID> {
	Mono<ReservationDto> findById(UUID id);
}
