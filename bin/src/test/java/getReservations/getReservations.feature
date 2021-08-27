Feature: Get reservations successfully

Scenario: Login, create a vacaction, reserve a hotel, and get the reservation successfully

Background:
	* def res = call read('classpath:reserveHotel/reserveHotel.feature')
	* def vacObj = res.vac.vacObj
	* def resObj = res.resObj
	* def sessionCookie = res.sessionCookie

Given url 'http://localhost:8080/users/test/vacations/' + vacObj.id
And cookie SESSION = sessionCookie
When method Get
Then status 200
And match response contains { reservations: '#notnull' }
And match response.reservations[0] == resObj
