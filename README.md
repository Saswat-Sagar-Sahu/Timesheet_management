# Timesheet Management Service

A Spring Boot–based backend service for managing employee timesheets.  
This project uses **Java 17**, **PostgreSQL**, and **OpenAPI (Swagger)** for API documentation.

---

## 🛠 Tech Stack

- **Java:** 17
- **Spring Boot:** 3.x
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA (Hibernate)
- **API Documentation:** OpenAPI 3 (Swagger UI)
- **Containerization:** Docker & Docker Compose
- **Build Tool:** Maven

---

## 📦 Prerequisites

Ensure the following are installed on your system:

- Java **17**
- Maven **3.8+**
- Docker & Docker Compose
- Git (optional)

Verify Java version:
```bash
java -version
```
---
## 🗄 Database Setup (PostgreSQL using Docker)
PostgreSQL is run locally using Docker Compose.
Start PostgreSQL
```bash
docker compose up -d
```
Stop PostgreSQL
```bash
docker compose down
```
---

## ▶️ Running the Application
### 1️⃣ Build the project
```bash
mvn clean install
```
### 2️⃣ Run the Spring Boot application
```bash
mvn spring-boot:run
```
The application will start on: http://localhost:8080

---

## 📘 Swagger / OpenAPI Documentation
Swagger UI is enabled for API documentation and testing.
### Swagger URLs
- Swagger UI:
http://localhost:8080/swagger-ui.html
- OpenAPI JSON:
http://localhost:8080/v3/api-docs

