Feature: Login as a user and get all activities in a location

Scenario: As a logged-in user, send a get request that retrieves all available activities

Given url 'http://localhost:8080/activities/Los%20Angeles,%20CA'
When method get
Then status 200
And def acts = JSON.parse(karate.extractAll(response, "(\\{.*?\\})", 1))
And match each acts contains {id: '#notnull' }
