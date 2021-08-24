Feature: Login as a user and get all activities in a vacation
Scenario: As a logged-in user, send a get request that retrieves that user's activities

Background:
	* def vac = call read('classpath:createVacation/createVacation.feature')
	* def loggedIn = call read('classpath:login/login.feature')
	* def vacObj = vac.vacObj

Given url 'http://localhost:8080/users/test/activities/'+vacObj.id
When method get
Then status 200
And match each response contains { id: '#notnull' }
And def act = response[0]
