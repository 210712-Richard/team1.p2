package com.revature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Driver {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Driver.class);
		application.setWebApplicationType(WebApplicationType.NONE);
	}
}
