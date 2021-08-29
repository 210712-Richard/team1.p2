Feature: User can reschedule flight Successfully

Scenario: Login, create vacation, reserve flight, then reschedule flight

Background:
	* def res = call read('classpath:reserveFlight/reserveFlight.feature')
	* def loggedIn = call read('classpath:login/login.feature')
	* def resObj = res.resObj
	* def flights = res.flights
	
Given url reservationUrl + '/' + resObj.id
And request {reservedId: '#(flights[1].id)'}
And cookie SESSION = loggedIn.sessionCookie
When method patch
Then status 200
And match response contains { reservedId: '#(flights[1].id)' }