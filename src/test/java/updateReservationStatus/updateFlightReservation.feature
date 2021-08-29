Feature: Update reservation status as a vacationer for a flight I booked

Scenario: As a logged in vacationer, send a put request to confirm a booked flight

Background:
* def vac = call read('createReservation.feature')
* def vacObj = vac.vacObj
* def loggedIn = vac.loggedIn

# Search available flights to LA
Given url 'http://localhost:8080/flights/Los%20Angeles,%20CA'
When method get
Then status 200
And def flights = JSON.parse(karate.extractAll(response, "(\\{.*?\\})", 1))
And match each flights contains { id: '#notnull' }
And def flight = flights[0]

# Book first available flight
Given url 'http://localhost:8080/reservations'
And request { type: 'FLIGHT', reservedId: '#(flight.id)', vacationId: '#(vacObj.id)' }
And cookie SESSION = loggedIn.sessionCookie
When method post
Then status 200
And match response contains { id: '#notnull', reservedId: '#(flight.id)', cost: '#notnull', reservedName: '#notnull', status: 'AWAITING', type: 'FLIGHT', username: '#(vacObj.username)', vacationId: '#(vacObj.id)' }
And def resId = response.id
And def statusUrl = reservationUrl + '/' + resId + '/status'

# Confirm booking
Given url statusUrl
And request { status: 'CONFIRMED' }
And cookie SESSION = loggedIn.sessionCookie
And match loggedIn.response contains { type: 'VACATIONER' }
When method put
Then status 200
And match response contains { status: 'CONFIRMED' }
And def resObj = response
