Feature: User can Reschedule Hotel Reservation Successfully

Scenario: User logs in, creates a hotel reservation, then reschedules it successfully

Background:
	* def res = call read('classpath:reserveHotel/reserveHotel.feature')
	* def loggedIn = call read('classpath:login/login.feature')
	* def resObj = res.resObj
	
Given url reservationUrl + '/' + resObj.id
And request {starttime: '2022-08-20T10:00', duration: 6 }
And cookie SESSION = loggedIn.sessionCookie
When method patch
Then status 200
And match response contains {starttime: '2022-08-20T10:00:00', duration: 6 }
