Feature: Reserve a Hotel successfully

Scenario: Login, create a vacaction, and reserve a Hotel successfully

Background:
	* def vac = call read('classpath:createVacation/createVacation.feature')
	* def loggedIn = call read('classpath:login/login.feature')
	* def vacObj = vac.vacObj

Given url 'http://localhost:8080/reservations'
And request { type: 'HOTEL', reservedId: "cce2c968-e808-4b8f-9168-533239d3e169", vacationId: '#(vacObj.id)' }
And cookie SESSION = loggedIn.sessionCookie
When method post
Then status 200
 And match response contains { id: '#notnull', duration: '#(vacObj.duration)', reservedId: "cce2c968-e808-4b8f-9168-533239d3e169", cost: '#notnull', reservedName: '#notnull', status: 'AWAITING', type: 'HOTEL', username: '#(vacObj.username)', vacationId: '#(vacObj.id)' }