Employee Service
Description
The Employee Service is a Spring Boot microservice designed for managing employee records. It supports features like adding, updating, retrieving, and deleting employee information. The service is designed to handle time zone conversions, ensuring all timestamps are stored in UTC, with jurisdictional validation and proper mapping between DTOs and entities.

Features
CRUD Operations: Basic employee management functions.
Time Zone Handling: Automatic conversion of local times to UTC.
Jurisdiction Validation: Ensures employee jurisdiction is valid.
Technologies
Java 17
Spring Boot
JPA (Hibernate)
Lombok
Endpoints
POST /employee: Add a new employee.
GET /employee/{id}: Retrieve employee by ID.
PUT /employee/{id}: Update employee details.
DELETE /employee/{id}: Remove employee.
