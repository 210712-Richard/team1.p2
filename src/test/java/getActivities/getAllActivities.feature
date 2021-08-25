Feature: Login as a user and get all activities in a location

Scenario: Without being logged in, request activities and be unauthorized

Given url 'http://localhost:8080/activities/Los%20Angeles,%20CA'
When method get
Then status 401

Scenario: As a logged-in user, send a get request that retrieves all available activities

Background:
	* def loggedIn = call read('classpath:login/login.feature')

Given url 'http://localhost:8080/activities/Los%20Angeles,%20CA'
And cookie SESSION = loggedIn.sessionCookie
When method get
Then status 200
And match each response contains { id: '#notnull' }
