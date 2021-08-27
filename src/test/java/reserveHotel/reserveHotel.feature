Feature: Reserve a Hotel successfully

Scenario: Login, create a vacaction, get a hotel, and reserve a Hotel successfully

Background:
	* def vac = call read('classpath:createVacation/createVacation.feature')
	* def vacObj = vac.vacObj
	* def sessionCookie = vac.loggedIn.sessionCookie
	* def loggedIn = vac.loggedIn

Given url 'http://localhost:8080/hotels/Los%20Angeles,%20CA'
When method get
Then status 200
And match each response contains { id: '#notnull' }
And def hotel = response[0]

Given url 'http://localhost:8080/reservations'
And request { type: 'HOTEL', reservedId: '#(hotel.id)', vacationId: '#(vacObj.id)' }
And cookie SESSION = sessionCookie
When method post
Then status 200
And match response contains { id: '#notnull', duration: '#(vacObj.duration)', reservedId: '#(hotel.id)', cost: '#notnull', reservedName: '#notnull', status: 'AWAITING', type: 'HOTEL', username: '#(vacObj.username)', vacationId: '#(vacObj.id)' }
And def resObj = response