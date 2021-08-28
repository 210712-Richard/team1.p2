Feature: Reserve a Flight successfully

Scenario: Login, create a vacation, get a flight, and reserve a flight successfully

Background:
	* def vac = call read('classpath:createVacation/createVacation.feature')
	* def loggedIn = call read('classpath:login/login.feature')
	* def vacObj = vac.vacObj

Given url 'http://localhost:8080/flights/Los%20Angeles,%20CA'
When method get
Then status 200
And def flights = JSON.parse(karate.extractAll(response, "(\\{.*?\\})", 1))
And match each flights contains { id: '#notnull' }

Given url 'http://localhost:8080/reservations'
And request { type: 'FLIGHT', reservedId: '#(flights[0].id)', vacationId: '#(vacObj.id)' }
And cookie SESSION = loggedIn.sessionCookie
When method post
Then status 200
And match response contains { id: '#notnull', duration: 0, reservedId: '#(flights[0].id)', cost: '#notnull', reservedName: '#notnull', status: 'AWAITING', type: 'FLIGHT', username: '#(vacObj.username)', vacationId: '#(vacObj.id)' }
And def resObj = response