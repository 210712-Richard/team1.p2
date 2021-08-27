Feature: Reserve a Flight successfully

Scenario: Login, create a vacation, get a flight, and reserve a flight successfully

Background:
	* def vac = call read('classpath:createVacation/createVacation.feature')
	* def loggedIn = call read('classpath:login/login.feature')
	* def vacObj = vac.vacObj


Given url 'http://localhost:8080/reservations'
And request { type: 'FLIGHT', reservedId: 'c333b75c-6f74-4499-95e8-9e1ec5d06839', vacationId: '#(vacObj.id)' }
And cookie SESSION = loggedIn.sessionCookie
When method post
Then status 200
 And match response contains { id: '#notnull', duration: 0, reservedId: 'c333b75c-6f74-4499-95e8-9e1ec5d06839', cost: '#notnull', reservedName: '#notnull', status: 'AWAITING', type: 'FLIGHT', username: '#(vacObj.username)', vacationId: '#(vacObj.id)' }