# Vacationeer

## Overview
  Vacationeer allows users to reserve hotel rooms, rental cars, and plane tickets in one application, as well as allows staff members to confirm, cancel, and reschedule the reservations.

## Technologies
* Spring Boot
* Spring WebFlux
* Spring Data
* Reactor
* JUnit
* Karate
* Log4J
* SonarCloud
* Docker
* AWS Keyspaces

## Features
### Features List
* Users can register for an account.
* Users can login to an account.
* Users can create vacation.
* Users can Reserve Flight, Hotel & Car.
* Users can Reschedule Flight, Hotel & Car reservation.
* Users can cancel Flight and Hote reservation.
* Users can Check out from Hotel.
* Users can choose activities for vacation.
* Users can view chosen activities and available activities for vacation.
* Users can Delete their account.
* Users can Logout.

### To-Do List
* Notifications to let users know when their reservation changes
* Password hashing
* Ability for staff members to create other staff members

## Getting Started
### What is Needed to Run
1. AWS User with access to Keyspaces
2. ENV Variables
  1. AWS_USER: The username for the AWS Keyspaces user.
  2. AWS_PASS: The password for the AWS Keyspaces user.
3. Docker

### Running the application
1. While in the main repository, run the command `docker build -t vacationeer .`.
2. To run the container, run the command `docker run -p 8080:8080 -e AWS_USER -e AWS_PASS vacationeer`.

## Usage
* With the application running, requests can be made to the server using the url `http://localhost:8080`.

## Contributors
* Team Lead - Michael McInerney [(GitHub)](https://github.com/mcinerneym)
* Steven G. [(GitHub)](https://github.com/steven-gsx)
* Kyle Bricker [(GitHub)](https://github.com/KyleBricker)
* Elizabeth Kelvin [(GitHub)](https://github.com/elizabethkelvin)
