Feature: Register as test

Scenario: Register successfully

Given url 'http://localhost:8080/users/test78'
And request { username: 'test78', password: 'password', email: 'test@email.com',  firstName:'Test', lastName:'User', birthday: '2000-01-01', type:'VACATIONER'}
When method put
Then status 201
And match response contains { username: 'test78', password: 'password', email: 'test@email.com',  firstName:'Test', lastName:'User', birthday: '2000-01-01', type:'VACATIONER'}

#Login and delete the user
Given url loginUrl
And request { username: 'test78', password: 'password' }
When method post
Then status 200
And match responseCookies contains { SESSION: '#notnull' }
And def sessionCookie = responseCookies.SESSION

Given url 'http://localhost:8080/users/test78'
And cookie SESSION = sessionCookie
When method delete
Then status 204
