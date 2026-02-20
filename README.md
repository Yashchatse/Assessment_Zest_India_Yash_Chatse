# 🛒 Product API

A production-ready RESTful API for product management, built as part of the **Zest India IT Pvt Ltd** Java Backend Developer assignment.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Run Locally](#run-locally)
  - [Run with Docker](#run-with-docker)
- [API Reference](#api-reference)
- [Authentication Flow](#authentication-flow)
- [Security](#security)
- [Testing](#testing)
- [Environment Variables](#environment-variables)

---

## Overview

Product API provides full CRUD operations for managing products and their associated items. It uses JWT-based authentication with refresh token rotation, role-based access control, and is fully containerized with Docker.

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                        Client                           │
│              (Postman / Swagger UI / App)                │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP Requests
                         ▼
┌─────────────────────────────────────────────────────────┐
│                   Spring Boot App                        │
│                                                         │
│  ┌─────────────┐    ┌──────────────┐   ┌─────────────┐  │
│  │  Controller │───▶│   Service    │──▶│  Repository │  │
│  │  Layer      │    │   Layer      │   │  Layer      │  │
│  └─────────────┘    └──────────────┘   └──────┬──────┘  │
│         │                                      │         │
│  ┌──────▼──────┐    ┌──────────────┐           │         │
│  │  JWT Filter │    │ Exception    │           │         │
│  │  (Security) │    │ Handler      │           │         │
│  └─────────────┘    └──────────────┘           │         │
└─────────────────────────────────────────────────┼───────┘
                                                  │ JPA/Hibernate
                                                  ▼
┌─────────────────────────────────────────────────────────┐
│                    PostgreSQL Database                   │
│                                                         │
│   ┌──────────────┐         ┌──────────────────────┐     │
│   │   product    │─────────│        item          │     │
│   │  id          │  1 : N  │  id                  │     │
│   │  product_name│         │  product_id (FK)     │     │
│   │  created_by  │         │  quantity            │     │
│   │  created_on  │         └──────────────────────┘     │
│   │  modified_by │                                      │
│   │  modified_on │         ┌──────────────────────┐     │
│   └──────────────┘         │       users          │     │
│                            │  id                  │     │
│                            │  username            │     │
│                            │  password (BCrypt)   │     │
│                            │  email               │     │
│                            │  role                │     │
│                            │  refresh_token       │     │
│                            └──────────────────────┘     │
└─────────────────────────────────────────────────────────┘
```

### Request Flow

```
Request → JwtAuthenticationFilter → Controller → Service → Repository → DB
                                                    ↓
Response ←─────────────────────── DTO/Mapper ←─── Entity
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.x |
| Security | Spring Security + JWT (JJWT 0.12.3) |
| Database | PostgreSQL 17 |
| ORM | Hibernate / Spring Data JPA |
| API Docs | SpringDoc OpenAPI 3 (Swagger UI) |
| Testing | JUnit 5 + Mockito |
| Build | Maven |
| Container | Docker + Docker Compose |

---

## Project Structure

```
src/
├── main/java/com/zest/product_api/
│   ├── config/
│   │   ├── SecurityConfig.java        # Spring Security configuration
│   │   ├── SwaggerConfig.java         # OpenAPI / Swagger setup
│   │   ├── JacksonConfig.java         # Jackson ObjectMapper configuration
│   │   └── JwtConfig.java             # JWT properties
│   ├── controller/
│   │   ├── AuthController.java        # Register, login, refresh, logout
│   │   ├── ProductController.java     # Product CRUD endpoints
│   │   └── ItemController.java        # Item endpoints
│   ├── service/
│   │   ├── interfaces/
│   │   │   ├── AuthService.java
│   │   │   ├── ProductService.java
│   │   │   └── ItemService.java
│   │   └── impl/
│   │       ├── AuthServiceImpl.java
│   │       ├── ProductServiceImpl.java
│   │       └── ItemServiceImpl.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── ProductRepository.java
│   │   └── ItemRepository.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── Item.java
│   │   └── Role.java                  # Enum: ROLE_USER, ROLE_ADMIN
│   ├── dto/
│   │   ├── request/
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   ├── ProductRequest.java
│   │   │   └── ItemRequest.java
│   │   └── response/
│   │       ├── AuthResponse.java
│   │       ├── ProductResponse.java
│   │       ├── ItemResponse.java
│   │       └── ApiResponse.java       # Generic wrapper: { success, message, data }
│   ├── security/
│   │   ├── JwtTokenProvider.java      # Token generation & validation
│   │   ├── JwtAuthenticationFilter.java
│   │   └── UserDetailsServiceImpl.java
│   ├── mapper/
│   │   ├── ProductMapper.java
│   │   └── ItemMapper.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       ├── ResourceNotFoundException.java
│       └── UnauthorizedException.java
└── test/java/com/zest/product_api/
    ├── service/
    │   ├── ProductServiceTest.java    # 7 unit tests
    │   ├── ItemServiceTest.java       # 5 unit tests
    │   └── AuthServiceTest.java       # 5 unit tests
    └── controller/
        ├── ProductControllerTest.java
        ├── ItemControllerTest.java
        └── AuthControllerTest.java
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 15+ (for local run)
- Docker + Docker Compose (for containerized run)

---

### Run Locally

**Step 1 — Create the database:**

```sql
CREATE DATABASE productdb;
```

**Step 2 — Update `application.properties`:**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/productdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

**Step 3 — Build and run:**

```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

**Step 4 — Open Swagger UI:**

```
http://localhost:8080/swagger-ui/index.html
```

---

### Run with Docker

No setup needed. Docker handles everything including the database.

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/product-api.git
cd product-api

# Build and start
docker-compose up --build

# Run in background
docker-compose up --build -d

# Stop
docker-compose down

# Stop and remove data
docker-compose down -v
```

**Access the app:**

```
Swagger UI  →  http://localhost:8080/swagger-ui/index.html
API Docs    →  http://localhost:8080/v3/api-docs
```

---

## API Reference

### Authentication

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| POST | `/api/v1/auth/register` | None | Register new user |
| POST | `/api/v1/auth/login` | None | Login, get JWT tokens |
| POST | `/api/v1/auth/refresh` | Refresh Token header | Refresh access token |
| POST | `/api/v1/auth/logout` | Bearer Token | Logout, invalidate token |

### Products

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| GET | `/api/v1/products` | Bearer Token | Get all products (paginated) |
| POST | `/api/v1/products` | Bearer Token | Create new product |
| GET | `/api/v1/products/{id}` | Bearer Token | Get product by ID |
| PUT | `/api/v1/products/{id}` | Bearer Token | Update product |
| DELETE | `/api/v1/products/{id}` | ADMIN role | Delete product |

### Items

| Method | Endpoint | Auth Required | Description |
|---|---|---|---|
| GET | `/api/v1/items/product/{productId}` | Bearer Token | Get items for a product |
| POST | `/api/v1/items/product/{productId}` | Bearer Token | Add item to product |
| PUT | `/api/v1/items/{id}` | Bearer Token | Update item |
| DELETE | `/api/v1/items/{id}` | ADMIN role | Delete item |

### Sample Request & Response

**Register:**
```json
POST /api/v1/auth/register
{
  "username": "admin",
  "email": "admin@zest.com",
  "password": "admin123",
  "role": "ROLE_ADMIN"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1...",
    "refreshToken": "eyJhbGciOiJIUzI1...",
    "tokenType": "Bearer",
    "username": "admin",
    "role": "ROLE_ADMIN"
  },
  "timestamp": "2026-02-20T10:00:00"
}
```

**Create Product:**
```json
POST /api/v1/products
Authorization: Bearer eyJhbGciOiJIUzI1...

{
  "productName": "Laptop"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Product created",
  "data": {
    "id": 1,
    "productName": "Laptop",
    "createdBy": "admin",
    "createdOn": "2026-02-20T10:00:00",
    "items": []
  },
  "timestamp": "2026-02-20T10:00:00"
}
```

---

## Authentication Flow

```
1. Register   POST /auth/register  →  Returns accessToken + refreshToken
                                              ↓
2. Use API    Add header: Authorization: Bearer <accessToken>
                                              ↓
3. Token      accessToken expires in 15 minutes
   Expires              ↓
4. Refresh    POST /auth/refresh   →  Returns new accessToken + refreshToken
              Header: Refresh-Token: <refreshToken>
                                              ↓
5. Logout     POST /auth/logout    →  Invalidates refreshToken in database
```

**Token Details:**
- Access Token expires in **15 minutes**
- Refresh Token expires in **7 days**
- Refresh tokens are **rotated** on every refresh (old token invalidated)

---

## Security

- Passwords hashed with **BCrypt**
- Stateless sessions using **JWT**
- Refresh token stored in database for rotation and revocation
- **ADMIN role** required for all DELETE operations
- CORS configured for cross-origin requests
- CSRF disabled (stateless API)

---

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProductServiceTest

# Run with coverage
mvn test jacoco:report
```

**Test coverage:**

| Test Class | Tests | Type |
|---|---|---|
| ProductServiceTest | 7 | Unit |
| ItemServiceTest | 5 | Unit |
| AuthServiceTest | 5 | Unit |
| ProductControllerTest | 4 | Integration |
| ItemControllerTest | 4 | Integration |
| AuthControllerTest | 4 | Integration |

Tests use **H2 in-memory database** so no PostgreSQL is needed to run tests.

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/productdb` | Database URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Database password |
| `APP_JWT_SECRET` | (set in properties) | JWT signing secret |
| `APP_JWT_EXPIRATION` | `900000` | Access token expiry (ms) |
| `APP_JWT_REFRESH_EXPIRATION` | `604800000` | Refresh token expiry (ms) |

---

## License

This project was built as a technical assignment for **Zest India IT Pvt Ltd**.