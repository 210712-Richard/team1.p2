Feature: Login as carTest

Scenario: send a request and login successfully

Given url loginUrl
And request { username: 'carTest', password: 'password' }
When method post
Then status 200
And match response contains { username: 'carTest', birthday: '#notnull', type: '#notnull'}
And match responseCookies contains { SESSION: '#notnull' }
And def sessionCookie = responseCookies.SESSION
