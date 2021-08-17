package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.revature.beans.User;
import com.revature.services.UserService;
import com.revature.services.UserServiceImpl;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController{
	private static Logger log = LogManager.getLogger(UserServiceImpl.class);
	
	UserService userService;
	
	@Autowired
	public UserControllerImpl(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
	public Mono<ResponseEntity<User>> login(@RequestBody User user, WebSession session){
		return null;
	}
	
	@DeleteMapping
	public Mono<ResponseEntity<Void>> logout(WebSession session){
		return null;
	}
	
	@PutMapping(value="{username}", produces=MediaType.APPLICATION_NDJSON_VALUE) 
	public Mono<ResponseEntity<User>> register(@RequestBody User user, @PathVariable("username") String name){
		return null;
	}

}
