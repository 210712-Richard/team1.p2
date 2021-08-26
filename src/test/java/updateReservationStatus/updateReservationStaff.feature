Feature: Update reservation status as staff for reservation type 

Scenario: As a logged in flight staff, send a patch request to set reservation 
status to awaiting

Background:
* def vac = call read('updateReservationUser.feature')
* def res = vac.resObj
* def loggedIn = call read('loginStaff.feature')
* def statusUrl = reservationUrl + '/' + res.id + '/' + 'awaiting'

Given url statusUrl
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'FLIGHT_STAFF' }
When method patch
Then status 200
And match response contains { status: 'AWAITING' }
