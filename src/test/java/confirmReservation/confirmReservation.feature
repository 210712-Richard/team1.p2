Feature: Login as a staff member to confirm a reservation

Scenario: As a logged in staff member, send a put request to confirm a reservation

Background:
* def resId = '18b99fc8-7b15-438d-b849-c3d89250da5f'
* def statusUrl = homeUrl + '/reservations/' + resId + '/' + 'status'

Given url statusUrl
And def loggedIn = call read('classpath:login/login.feature')
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn != { type: 'VACATIONER' }
And request {status: "CONFIRMED"}
When method put
Then status 200
And match response contains { status: 'CONFIRMED' }

# Reset reservation status
Given url statusUrl
And cookie SESSION = loggedIn.sessionCookie
When method patch
Then status 200
And match response contains { status: 'AWAITING' }
