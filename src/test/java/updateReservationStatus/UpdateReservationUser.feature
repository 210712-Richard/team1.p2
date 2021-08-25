Feature: Update reservation status as a vacationer for a reservation I booked

Scenario: As a logged in vacationer, send a patch request to confirm a reservation

Background:
* def resId = 'c350668f-ebae-4dbf-b517-a17d75c0fb3a'
* def statusUrl = homeUrl + '/reservations/' + resId + '/' + 'confirm'

Given url statusUrl
And def loggedIn = call read('loginUser.feature')
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'VACATIONER' }
When method patch
Then status 200
And match response contains { status: 'CONFIRMED' }
