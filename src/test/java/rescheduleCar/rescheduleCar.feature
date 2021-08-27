Feature: Staff Member can Reschedule Reservation Successfully

Scenario: Staff member gets a reservation then reschedules it

Background:
	* def res = call read('classpath:reserveCar/reserveCar.feature')
	* def loggedIn = call read('classpath:login/loginHotelStaff.feature')
	* def resObj = res.resObj
	
Given url reservationUrl + '/' + resObj.id
And request {starttime: '2022-10-20T10:00', duration: 6 }
And cookie SESSION = loggedIn.sessionCookie
When method patch
Then status 200
And match response contains {starttime: '2022-10-20T10:00:00', duration: 6 }
