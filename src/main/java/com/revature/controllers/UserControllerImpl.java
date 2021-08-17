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

@RestController
@RequestMapping("/users")
public class UserControllerImpl {
	private static Logger log = LogManager.getLogger(UserControllerImpl.class);
	
	UserService userService;
	
	@Autowired
	public UserControllerImpl(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<User> login(@RequestBody User user, WebSession session){
		return null;
	}
	
	@DeleteMapping
	public ResponseEntity<Void> logout(WebSession session){
		return null;
	}
	
	@PutMapping(value="{username}", produces=MediaType.APPLICATION_NDJSON_VALUE) 
	public ResponseEntity<User> register(@RequestBody User user, @PathVariable("username") String name){
		return null;
	}

}
