Feature: Reserve a Flight successfully

Scenario: Login, create a vacation, get a flight, and reserve a flight successfully

Background:
	* def vac = call read('classpath:createVacation/createVacation.feature')
	* def loggedIn = call read('classpath:login/login.feature')
	* def vacObj = vac.vacObj

Given url 'http://localhost:8080/flights/Los%20Angeles,%20CA'
When method get
Then status 200
And match each response contains { id: '#notnull' }
And def flight = response[0]

Given url 'http://localhost:8080/reservations'
And request { type: 'FLIGHT', reservedId: '#(flight.id)', vacationId: '#(vacObj.id)' }
And cookie SESSION = loggedIn.sessionCookie
When method post
Then status 200
 And match response contains { id: '#notnull', duration: 0, reservedId: '#(flight.id)', cost: '#notnull', reservedName: '#(flight.airline)', status: 'AWAITING', type: 'FLIGHT', username: '#(vacObj.username)', vacationId: '#(vacObj.id)' }