function fn() {
	var env = karate.env; // get the java system property 'karate.env'
	
	if (!env) {
		env = 'dev'; // a custom default
	}

	var config = {
		loginUrl: 'http://localhost:8080/users'
	}
	
	// If no response from server
	karate.configure('connectTimeout', 5000);
	karate.configure('readTimeout', 5000);
	return config;
}