Feature: Update reservation status as a vacationer for a reservation I booked

Scenario: As a logged in vacationer, send a patch request to confirm a reservation

Background:
* def vac = call read('createReservation.feature')
* def vacObj = vac.vacObj
* def res = vac.resObj
* def loggedIn = vac.loggedIn
* def statusUrl = reservationUrl + '/' + res.id + '/status'

Given url statusUrl
And request { status: 'CONFIRMED' }
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'VACATIONER' }
When method put
Then status 200
And match response contains { status: 'CONFIRMED' }
And def resObj = response
