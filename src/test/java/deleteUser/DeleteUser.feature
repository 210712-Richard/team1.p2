Feature: Delete User 
 
Scenario: Verify that the existing user is successfully getting deleted 
 
Given url 'http://localhost:8080/users/test'
And request { username: 'test'}
And method delete
Then status 204
And match responseCookies contains { SESSION: '#null' }
