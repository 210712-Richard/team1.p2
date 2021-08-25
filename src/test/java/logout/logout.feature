Feature: Logout as Logged In User

Scenario: send a request and logout successfully

Given url loginUrl
And def loggedin = call read('classpath:login/login.feature')
And cookie SESSION = loggedin.sessionCookie
When method delete
Then status 204