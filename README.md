# Product API

RESTful Product Management API built with Spring Boot 4.x, JWT Authentication, and PostgreSQL.

## Tech Stack
- Java 17
- Spring Boot 4.x
- Spring Security + JWT
- PostgreSQL
- Swagger / OpenAPI 3
- Docker + Docker Compose
- JUnit 5 + Mockito

## Getting Started

### Run with Docker
```bash
docker-compose up --build
```

### Run locally
```bash
# Start PostgreSQL first, then:
mvn spring-boot:run
```

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | /api/v1/auth/register | None | Register user |
| POST | /api/v1/auth/login | None | Login |
| POST | /api/v1/auth/refresh | None | Refresh token |
| POST | /api/v1/auth/logout | Bearer | Logout |
| GET | /api/v1/products | Bearer | Get all products |
| POST | /api/v1/products | Bearer | Create product |
| GET | /api/v1/products/{id} | Bearer | Get product |
| PUT | /api/v1/products/{id} | Bearer | Update product |
| DELETE | /api/v1/products/{id} | ADMIN | Delete product |
| GET | /api/v1/items/product/{id} | Bearer | Get items |
| POST | /api/v1/items/product/{id} | Bearer | Add item |
| PUT | /api/v1/items/{id} | Bearer | Update item |
| DELETE | /api/v1/items/{id} | ADMIN | Delete item |

## Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

## Test
```bash
mvn test
```