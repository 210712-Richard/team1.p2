Feature: Delete User 
 
Scenario: Verify that the existing user is successfully getting deleted 
 
Given url 'http://localhost:8080/users/test'
And def loggedIn = call read('classpath:login/login.feature')
And cookie SESSION = loggedIn.sessionCookie
And method delete
Then status 204

Given url loginUrl
And request {username: 'test', password: 'password' }
And method post
Then status 404

def reg = call read('classpath:Register:register.feature)