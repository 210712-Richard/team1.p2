function fn() {
	var env = karate.env; // get the java system property 'karate.env'
	
	if (!env) {
		env = 'dev'; // a custom default
	}

	var config = {
		loginUrl: 'http://localhost:8080/users',
		registerUrl: 'http://localhost:8080/users/newTest',
		reserveHotelUrl: 'http://localhost:8080/reservations/ffb4ccc4-65d2-4615-a409-4bb6782a1129/hotel'
	}
	
	// don't waste time waiting for a server I didn't start
	karate.configure('connectTimeout', 5000);
	karate.configure('readTimeout', 5000);
	return config;
}

