package com.revature;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

public class DatabaseCreator {

	private CqlSession session = CqlSession.builder().withConfigLoader(DriverConfigLoader.fromClasspath("application.conf")).withKeyspace("gacha").build();
	public static void main(String[] args) {
		
	}

}
