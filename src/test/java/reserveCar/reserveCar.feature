Feature: Reserve a Car successfully
Scenario: Reserve a Car for a vacation successfully

Given url 'http://localhost:8080/reservations
And request {type: CAR, reservedId: d1113b33-d001-4408-948c-462bb2a79aaf, vacationId: '#notnull' }
When method post
Then status 200
And match response contains { id: '#notnull' }