Feature: Attempt to update reservation status for a reservation with invalid
status input

Scenario: As a logged in vacationer, send a patch request to "cancel" -> (close)
a reservation

Background:
* def vac = call read('createReservation.feature')
* def res = vac.resObj
* def statusUrl = homeUrl + '/reservations/' + res.id + '/' + 'cancel'

Given url statusUrl
And def loggedIn = call read('loginUser.feature')
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'VACATIONER' }
When method patch
Then status 400