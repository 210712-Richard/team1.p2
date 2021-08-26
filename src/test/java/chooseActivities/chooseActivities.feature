Feature: Login as a user and choose activities for Vacation.
Scenario: As a logged in user, choose activities for Vacation.

Given url 'http://localhost:8080/users/test/vacations/64bc0f35-fa30-4d62-b99b-5fefb8b588da/activities'
And def loggedin = call read('classpath:login/login.feature')
And request {location: "Los Angeles, CA", id: "edc423ba-ed68-4349-8b4b-514ef869e0ec", name: "The Other Beach", description: "It is the other beach",cost: 10.0, date: "2021-09-09T00:00:00", maxParticipants: 100, username : "test"}
And cookie SESSION = loggedin.sessionCookie
When method post
Then status 200
And match response contains {location: "Los Angeles, CA", id: "edc423ba-ed68-4349-8b4b-514ef869e0ec", name: "The Other Beach", description: "It is the other beach",cost: 10.0, date: "2021-09-09T00:00:00", maxParticipants: 100, username : "test"}