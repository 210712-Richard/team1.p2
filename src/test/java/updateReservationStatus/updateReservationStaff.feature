Feature: Update reservation status as staff for reservation type 

Scenario: As a logged in flight staff, send a put request to set reservation 
status to awaiting

Background:
* def vac = call read('updateFlightReservation.feature')
* def res = vac.resObj
* def loggedIn = call read('loginStaff.feature')
* def statusUrl = reservationUrl + '/' + res.id + '/status'

Given url statusUrl
And request { status: 'AWAITING' }
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'FLIGHT_STAFF' }
When method put
Then status 200
And match response contains { status: 'AWAITING' }
