Feature: Attempt to update reservation status as staff for a different 
reservation type

Scenario: As a logged in flight staff, I cannot update hotel reservation with a put request

Background:
* def vac = call read('updateHotelReservation.feature')
* def res = vac.resObj
* def loggedIn = call read('loginStaff.feature')
* def statusUrl = reservationUrl + '/' + res.id + '/status'

Given url statusUrl
And request { status: 'CLOSED' }
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'FLIGHT_STAFF' }
When method put
Then status 403