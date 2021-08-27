Feature: Login as a user and choose activities for Vacation.
Scenario: As a logged in user, choose activities for Vacation.

Background:
	* def vac = call read('classpath:createVacation/createVacation.feature')
	* def vacObj = vac.vacObj
	* def sessionCookie = vac.loggedin.sessionCookie
	
Given url 'http://localhost:8080/users/test/vacations/'+ vacObj.id + '/activities'
And request {location: 'Los Angeles, CA', id: 'edc423ba-ed68-4349-8b4b-514ef869e0ec', name: 'The Other Beach', description: 'It is the other beach', cost: 10.0, date: '2021-09-09T00:00:00', maxParticipants: 100}
And cookie SESSION = sessionCookie
When method post
Then status 200
<<<<<<< HEAD
And match response contains {location: 'Los Angeles, CA', id: 'edc423ba-ed68-4349-8b4b-514ef869e0ec', name: 'The Other Beach', description: 'It is the other beach', cost: 10.0, date: '2021-09-09T00:00:00', maxParticipants: 100}
=======
And match response contains {location: 'Los Angeles, CA', id: 'edc423ba-ed68-4349-8b4b-514ef869e0ec', name: 'The Other Beach', description: 'It is the other beach', cost: 10.0, date: '2021-09-09T00:00:00', maxParticipants: 100}
>>>>>>> 31c953c473bbcc54f5f48f4b5070c39c3683ccf1
