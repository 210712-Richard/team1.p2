package com.revature.controller;

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
import org.springframework.web.server.WebSession;

import com.revature.beans.User;
import com.revature.controller.UserControllerImpl;
import com.revature.services.UserService;

import reactor.core.publisher.Mono;

public class UserControllerImpl {
	@Autowired
	private UserService userService;
	
	private static final Logger log = LogManager.getLogger(UserControllerImpl.class);

	
	// As a user, I can log in.
		@PostMapping // ("/users")
		public Mono<ResponseEntity<User>> login(@RequestBody User user, WebSession session){
			if(user == null) {
				return Mono.just(ResponseEntity.badRequest().build());
			}
			
			return userService.login(user.getUsername(), user.getPassword())
					.single().map(u -> { 
						if(u == null) {
							return ResponseEntity.notFound().build();
						}
						
						else {
							session.getAttributes().put("loggedUser", u);
							return ResponseEntity.ok(u);
						}
					});		
		}

		// As a user, I can log out.
		@DeleteMapping
		public ResponseEntity<Void> logout(WebSession session) {
			session.invalidate();
			return ResponseEntity.noContent().build();
		}
		
		// As a user, I can register.
		@PutMapping(value="/{username}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Object> register(@RequestBody User u, @PathVariable("username") String username) {
			// check to see if that username is available
			if (userService.checkAvailability(username)) {
				// actually register the user
				Mono<User> created = userService.register(username,u.getPassword(), u.getEmail(), u.getFirstName(), u.getLastName(), u.getBirthday(),u.getType());
				return ResponseEntity.ok(created);
			} else {
				return ResponseEntity.status(409).contentType(MediaType.TEXT_HTML).body("<html><body><div>CONFLICT</div></body></html>");
			}
		}
}
