Feature: Update reservation status as staff for reservation type 

Scenario: As a logged in flight staff, send a patch request to set reservation 
status to awaiting

Background:
* def resId = 'c350668f-ebae-4dbf-b517-a17d75c0fb3a'
* def statusUrl = homeUrl + '/reservations/' + resId + '/' + 'awaiting'

Given url statusUrl
And def loggedIn = call read('loginStaff.feature')
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'FLIGHT_STAFF' }
When method patch
Then status 200
And match response contains { status: 'AWAITING' }
