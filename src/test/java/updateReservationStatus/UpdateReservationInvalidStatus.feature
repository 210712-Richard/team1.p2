Feature: Attempt to update reservation status for a reservation with invalid
status input

Scenario: As a logged in vacationer, send a patch request to "cancel" -> (close)
a reservation

Background:
* def resId = 'c350668f-ebae-4dbf-b517-a17d75c0fb3a'
* def statusUrl = homeUrl + '/reservations/' + resId + '/' + 'cancel'

Given url statusUrl
And def loggedIn = call read('loginUser.feature')
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'VACATIONER' }
When method patch
Then status 400