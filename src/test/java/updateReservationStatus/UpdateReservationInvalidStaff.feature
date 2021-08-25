Feature: Attempt to update reservation status as staff for a different 
reservation type

Scenario: As a logged in car staff, send a patch request to confirm a 
flight reservation

Background:
* def resId = 'c350668f-ebae-4dbf-b517-a17d75c0fb3a'
* def statusUrl = homeUrl + '/reservations/' + resId + '/' + 'confirm'

Given url statusUrl
And def loggedIn = call read('loginInvalidStaff.feature')
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'CAR_STAFF' }
When method patch
Then status 403