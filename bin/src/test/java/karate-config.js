function fn() {
	var env = karate.env; // get the java system property 'karate.env'
	
	if (!env) {
		env = 'dev'; // a custom default
	}

	var config = {
		homeUrl: 'http://localhost:8080',
		loginUrl: 'http://localhost:8080/users',
		registerUrl: 'http://localhost:8080/users/newTest',
		reservationUrl: 'http://localhost:8080/reservations'
	}
	
	// don't waste time waiting for a server I didn't start
	karate.configure('connectTimeout', 5000);
	karate.configure('readTimeout', 5000);
	return config;
}

