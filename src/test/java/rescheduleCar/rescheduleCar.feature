Feature: Staff Member can Reschedule Reservation Successfully

Scenario: Staff member gets a reservation then reschedules it

Background:
	* def res = call read('classpath:reserveCar/reserveCar.feature')
	* def loggedIn = call read('classpath:login/loginHotelStaff.feature')
	* def resObj = res.resObj
	
Given url reservationUrl + '/' + resObj.id + '/startTime/duration'
And request {starttime: '2021-08-20T10:00', duration: 6 }
And cookie SESSION = loggedIn.sessionCookie
When method put
Then status 200
And match response contains {starttime: '2021-08-20T10:00:00', duration: 6 }
