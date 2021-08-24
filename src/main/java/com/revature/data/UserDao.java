package com.revature.data;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import com.revature.beans.User;
import com.revature.dto.UserDto;

import reactor.core.publisher.Mono;

@Repository
public interface UserDao extends ReactiveCassandraRepository<UserDto, String> {
	Mono<UserDto> findByUsername(String username);

	Mono<UserDto> findByUsernameAndPassword(String username, String password);

	Mono<Boolean> existsByUsername(String username);

	Mono<UserDto> deleteByUsername(String username);
}
