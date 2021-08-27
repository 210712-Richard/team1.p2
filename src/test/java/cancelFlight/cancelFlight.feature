Feature: Login as a user to cancel a flight

Scenario: As a logged in staff member, send a put request to confirm a reservation

Background:
* def flightId = '4051e4ef-8630-4582-954b-a9c9542fb1d7'
* def flightUrl = homeUrl + '/reservations/' + flightId + '/' + 'flight'

Given url flightUrl
And def loggedIn = call read('classpath:login/login.feature')
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn != { type: 'VACATIONER' }
And request {status: "CONFIRMED"}
When method delete
Then status 200
And match response contains { status: 'CLOSED' }

# Reset reservation status
Given url statusUrl
And cookie SESSION = loggedIn.sessionCookie
When method patch
Then status 200
And match response contains { status: 'AWAITING' }