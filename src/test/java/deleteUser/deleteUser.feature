Feature: Delete User 
 
Scenario: Verify that the existing user is successfully getting deleted 
 
Given url 'http://localhost:8080/users/testDelete'
And request { username: 'testDelete', password: 'password', email: 'test@email.com',  firstName:'Test', lastName:'User', birthday: '2000-01-01', type:'VACATIONER'}
When method put
Then status 201

Given url loginUrl
And request { username: 'testDelete', password: 'password' }
When method post
Then status 200
And match responseCookies contains { SESSION: '#notnull' }
And def sessionCookie = responseCookies.SESSION

Given url 'http://localhost:8080/users/testDelete'
And cookie SESSION = sessionCookie
When method delete
Then status 204
