Feature: Logout as Logged In User

Scenario: send a request and logout successfully

Given url loginUrl
And def loggedin = call read('classpath:login/login.feature')
And request { destination: "Los Angeles, CA", startTime: "2021-08-19T10:00", endTime: "2021-08-21T10:00", partySize: 4, duration: 2}
And cookie SESSION = loggedin.sessionCookie
When method delete
Then status 204
