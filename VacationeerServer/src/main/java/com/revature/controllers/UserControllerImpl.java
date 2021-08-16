package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.services.UserService;
import com.revature.services.UserServiceImpl;

@RestController
@RequestMapping("/users")
public class UserControllerImpl {
	private static Logger log = LogManager.getLogger(UserServiceImpl.class);
	
	UserService userService;
	
	@Autowired
	public UserControllerImpl(UserService userService) {
		this.userService = userService;
	}

}
