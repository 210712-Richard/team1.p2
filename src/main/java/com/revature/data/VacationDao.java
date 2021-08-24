package com.revature.data;

import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.revature.beans.User;
import com.revature.dto.VacationDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VacationDao extends ReactiveCassandraRepository<VacationDto, String> {
	Flux<VacationDto> findByUsername(String username);

	Mono<VacationDto> findByUsernameAndId(String username, UUID id);

	Mono<VacationDto> deletebyUsername(String username);

	Mono<User> deleteByUsername(String username);
}
