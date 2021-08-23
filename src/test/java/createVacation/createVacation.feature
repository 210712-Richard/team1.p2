Feature: Login as a user and create a vacation
Scenario: As a logged in user, send a post request to create a vacation and receive that vacation

Given url 'http://localhost:8080/users/test/vacations'
And def loggedIn = call read('classpath:login/login.feature')
And request { destination: "Los Angeles, CA", startTime: "2021-08-19T10:00", endTime: "2021-08-21T10:00", partySize: 4, duration: 2}
And cookie SESSION = loggedIn.sessionCookie
When method post
Then status 201
And match response contains { username: "test", id: "#notnull", destination: "Los Angeles, CA", startTime: "2021-08-19T10:00:00", endTime: "2021-08-21T10:00:00", partySize: 4, duration: 2}
And def vacObj = response
<<<<<<< HEAD



=======
>>>>>>> 64d8b18bb92d65eba73ea9170de6b4753a85ce79
