Feature: Reserve a Car successfully
Scenario: Reserve a Car for a vacation successfully

Background:
	* def vac = call read('classpath:createVacation/createVacation.feature')
	* def loggedIn = call read('classpath:login/login.feature')
	* def vacObj = vac.vacObj


Given url 'http://localhost:8080/reservations'
And request {type: CAR, reservedId: d1113b33-d001-4408-948c-462bb2a79aaf, vacationId: '#(vacObj.id)' }
And cookie SESSION = loggedIn.sessionCookie 
When method post
Then status 200
And match response contains { id: '#notnull' }