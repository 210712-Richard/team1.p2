Feature: Attempt to update reservation status with invalid status input

Scenario: As a logged in staff, send a put request to "OPEN" -> (AWAITING) a reservation

Background:
* def vac = call read('updateFlightReservation.feature')
* def res = vac.resObj
* def loggedIn = call read('loginStaff.feature')
* def statusUrl = reservationUrl + '/' + res.id + '/status'

Given url statusUrl
And request { status: 'OPEN' }
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'FLIGHT_STAFF' }
When method put
Then status 400