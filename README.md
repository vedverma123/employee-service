# Employee Service

The Employee Service is a Spring Boot microservice designed for managing employee records. It supports features like adding, updating, retrieving, and deleting employee information. 
The service is designed to handle time zone conversions, ensuring all timestamps are stored in UTC, with jurisdictional validation and proper mapping between DTOs and entities.

## Technologies
#### Java 17
#### Spring Boot
#### JPA (Hibernate)
#### Lombok

## Endpoints
`POST /employee:` Add a new employee.

`GET /employee/{id}`: Retrieve employee by ID.

`PUT /employee/{id}`: Update employee details.

`DELETE /employee/{id}`: Remove employee.

## In Memory Database
Currently, application is configured to run with H2 database accessible at url [http://localhost:8551/h2-console/login.jsp]()
with following properties.

