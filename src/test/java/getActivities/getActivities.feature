Feature: Login as a user and get all activities in a vacation

Scenario: Without being logged in, request activities and be unauthorized

Background:
	* def vac = call read('classpath:createVacation/createVacation.feature')
	* def vacObj = vac.vacObj

Given url 'http://localhost:8080/users/test/vacations/'+vacObj.id+'/activities'
When method get
Then status 401

Scenario: As a logged-in user, send a get request that retrieves that user's activities

Background:
	* def vac = call read('classpath:chooseActivities/chooseActivities.feature')
	* def vacObj = vac.vacObj
	* def loggedIn = call read('classpath:login/login.feature')

Given url 'http://localhost:8080/users/test/vacations/'+vacObj.id+'/activities'
And cookie SESSION = loggedIn.sessionCookie
When method get
Then status 200
And def acts = JSON.parse(karate.extractAll(response, "(\\{.*?\\})", 1))
And match each acts contains { id: '#notnull' }
