Feature: Register as test
Scenario: Register successfully
Given url 'http://localhost:8080/users/test'
And request { username: 'test', password: 'password', email: 'test@email.com',  firstName:'Test', lastName:'User', birthday: '2000-01-01', type:'VACTIONER'}
When method put
Then status 201
And match response contains { username: 'test', password: 'password', email: 'test@email.com',  firstName:'Test', lastName:'User', birthday: '2000-01-01', type:'VACTIONER'}
