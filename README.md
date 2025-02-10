# hometest
This is a web application for managing users and generating reports using Java Spring Boot, Vaadin, and JasperReports.

## Features

- **User Management**:
  - Create, Read, Update, Delete.
  - Search users by their id, name, or gender.
  - Generate PDF reports of user data.

- **Report Generation**:
  - Generate a PDF report of all users.
  - Generate a PDF report for selected users.

## Technologies Used

- **Backend**:
  - Spring Boot
  - Spring Data JPA
  - H2 or MySQL Database
  - JasperReports for report generation

- **Frontend**:
  - Vaadin Framework

## API Endpoints
- **User Management**:
  - GET /users: Get all users
  - POST /users: Create a new user
  - PUT /users/{id}: Update an existing user
  - DELETE /users/{id}: Delete a user

## Installation

### Prerequisites

Ensure you have the following installed on your system:
- Java 17 or later
- Maven
- MySQL (or use an H2 in-memory database for quick testing)
- Spring Boot

### Steps

1. **Clone the repository**:
   ```bash
   git clone https://github.com/iqbalk18/hometest.git
2. **Configure the database**:
   ```bash
   spring.datasource.url=jdbc:mysql://localhost:3306/dbmanulife
   spring.datasource.username=root
   spring.datasource.password=YourPasswordHere
   spring.jpa.hibernate.ddl-auto=update
3. **Install dependencies**:
   ```bash
   mvn clean install
4. **Run the application**:
   ```bash
   mvn spring-boot:run
The application should be running : http://localhost:8080/
