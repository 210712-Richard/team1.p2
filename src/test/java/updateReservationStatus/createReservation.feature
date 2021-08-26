Feature: Login as a user and create a vacation

Scenario: As a logged in user, send a post request to create a vacation, 
receive that vacation then attach a flight to new vacation package

Given url 'http://localhost:8080/users/test/vacations'
And def loggedIn = call read('loginUser.feature')
And request { destination: "Los Angeles, CA", startTime: "2021-10-24T10:00", endTime: "2021-11-21T10:00", partySize: 4, duration: 2}
And cookie SESSION = loggedIn.sessionCookie
When method post
Then status 201
And match response contains { username: "test", id: "#notnull", destination: "Los Angeles, CA", startTime: "2021-10-24T10:00:00", endTime: "2021-11-21T10:00:00", partySize: 4, duration: 2}
And def vacObj = response

Given url 'http://localhost:8080/flights/Los%20Angeles,%20CA'
When method get
Then status 200
And match each response contains { id: '#notnull' }
And def flight = response[0]

Given url 'http://localhost:8080/reservations'
And request { type: 'FLIGHT', reservedId: '#(flight.id)', vacationId: '#(vacObj.id)' }
And cookie SESSION = loggedIn.sessionCookie
When method post
Then status 200
And match response contains { id: '#notnull', reservedId: '#(flight.id)', cost: '#notnull', reservedName: '#notnull', status: 'AWAITING', type: 'FLIGHT', username: '#(vacObj.username)', vacationId: '#(vacObj.id)' }
And def resObj = response