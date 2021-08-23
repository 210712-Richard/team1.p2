Feature: Login as a staff member to confirm a reservation

Scenario: As a logged in staff member, send a put request to confirm a reservation

Background:
* def resId = '9fc0d648-0cd3-411f-b486-3ad34526bb5d'
* def statusUrl = homeUrl + '/reservations/' + resId + '/' + 'status'

Given url statusUrl
And def loggedIn = call read('classpath:login/login.feature')
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn != { type: 'VACATIONER' }
When method put
Then status 200
And match response contains { status: 'CONFIRMED' }

# Reset reservation status
Given url statusUrl
And cookie SESSION = loggedIn.sessionCookie
When method patch
Then status 200
And match response contains { status = 'AWAITING' }
