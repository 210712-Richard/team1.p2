Feature: Reserve a Car successfully
Scenario: Reserve a Car for a vacation successfully

Background:
	* def vac = call read('classpath:createVacation/createVacation.feature')
	* def loggedIn = call read('classpath:login/login.feature')
	* def vacObj = vac.vacObj

Given url 'http://localhost:8080/cars/Los%20Angeles,%20CA'
When method get
Then status 200
And def cars = JSON.parse(karate.extractAll(response, "(\\{.*?\\})", 1))
And match each cars contains { id: '#notnull' }
And def car = cars[0]

Given url 'http://localhost:8080/reservations'
And request {type: CAR, reservedId: '#(car.id)', vacationId: '#(vacObj.id)' }
And cookie SESSION = loggedIn.sessionCookie 
When method post
Then status 200
And match response contains { id: '#notnull' }
And def resObj = response