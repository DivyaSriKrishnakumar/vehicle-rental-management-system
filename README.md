# Vehicle Rental Management System

## Overview

Vehicle Rental Management System is a secure REST API backend application developed using Spring Boot.

The project manages:

- Vehicles
- Users
- Rental Bookings
- JWT Authentication
- Role-Based Authorization
- Booking Conflict Validation
- DTO Architecture
- Pagination
- Swagger API Documentation
- Global Exception Handling

This project was designed as a portfolio-quality backend system following layered architecture and production-style REST API design principles.

---

# Features

## Authentication & Authorization

- JWT Authentication
- Spring Security Integration
- Stateless Authentication
- USER and ADMIN Roles
- Protected APIs
- Role-Based Access Control
- Custom AccessDenied Handling
- Secure Booking Ownership Validation

---

## Jenkins CI/CD Testing
Updated Jenkins webhook testing

---

## Vehicle Management

- Add New Vehicles
- Update Vehicle Information
- Delete Vehicles
- Get Vehicle By ID
- Get All Vehicles
- Vehicle Availability Management
- Pagination Support
- DTO-Based Request/Response Handling
- Validation Support

---

## Booking Management

- Create Booking
- Cancel Booking
- Booking History API
- User-Specific Booking Retrieval
- Admin Access To All Bookings
- Booking Overlap Conflict Prevention
- Vehicle Maintenance Availability Checks
- Booking Status Tracking

---

# Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming Language |
| Spring Boot | Backend Framework |
| Spring Security | Authentication & Authorization |
| JWT | Token-Based Authentication |
| Spring Data JPA | Database Access |
| Hibernate | ORM |
| H2 Database | Development Database |
| Maven | Dependency Management |
| Swagger/OpenAPI | API Documentation |
| Postman | API Testing |

---

# Architecture

The project follows layered architecture:

```text
Controller Layer
↓
Service Layer
↓
Repository Layer
↓
Database
```

---

# Project Structure

```text
src/main/java
│
├── controller
│   ├── AuthController
│   ├── VehicleController
│   └── BookingController
│
├── service
│   ├── VehicleService
│   ├── BookingService
│   └── impl
│       ├── VehicleServiceImpl
│       └── BookingServiceImpl
│
├── repository
│   ├── VehicleRepository
│   ├── BookingRepository
│   └── UserRepository
│
├── entity
│   ├── Vehicle
│   ├── Booking
│   └── AppUser
│
├── dto
│   ├── VehicleRequestDTO
│   ├── VehicleResponseDTO
│   ├── BookingRequestDTO
│   └── BookingResponseDTO
│
├── security
│   ├── JwtFilter
│   ├── JwtUtil
│   ├── SecurityConfig
│   └── CustomAccessDeniedHandler
│
├── exception
│   ├── GlobalExceptionHandler
│   ├── ResourceNotFoundException
│   ├── BadRequestException
│   └── BookingAlreadyCancelledException
│
└── enums
    └── BookingStatus
```

---

# Security & Roles

## USER Role

A USER can:

- View Vehicles
- Create Bookings Only For Themselves
- Cancel Only Their Own Bookings
- View Their Own Booking History

---

## ADMIN Role

An ADMIN can:

- Add Vehicles
- Update Vehicles
- Delete Vehicles
- Create Bookings For Any User
- Cancel Any Booking
- View All Bookings
- Manage Vehicle Availability

---

# Database Entities

## Vehicle Entity

Represents vehicles available for rental.

### Fields

| Field | Type |
|-------|------|
| id | Long |
| name | String |
| type | String |
| available | Boolean |

---

## Booking Entity

Represents booking transactions.

### Fields

| Field | Type |
|---|---|
| id | Long |
| vehicleId | Long |
| customerId | Long |
| startDate | LocalDate |
| endDate | LocalDate |
| status | BookingStatus |

---

## AppUser Entity

Represents authenticated users.

### Fields

| Field | Type |
|---|---|
| id | Long |
| username | String |
| password | String |
| role | String |

---

# DTO Architecture

DTOs are used to separate API contracts from database entities.

## VehicleRequestDTO

Used when creating/updating vehicles.

## VehicleResponseDTO

Used when returning vehicle data.

## BookingRequestDTO

Used for booking creation.

## BookingResponseDTO

Used when returning booking details.

---

# API Endpoints

# Authentication APIs

## Register User

```http
POST /auth/register
```

### Request Body

```json
{
  "username": "divya",
  "password": "password123",
  "role": "USER"
}
```

---

## Login

```http
POST /auth/login
```

### Request Body

```json
{
  "username": "divya",
  "password": "password123"
}
```

### Response

```json
{
  "token": "JWT_TOKEN"
}
```

---

# Vehicle APIs

## Get All Vehicles

```http
GET /vehicles?page=0&size=5
```

### Features

- Pagination Support
- DTO Response
- Public Access

---

## Get Vehicle By ID

```http
GET /vehicles/{id}
```

---

## Add Vehicle

```http
POST /vehicles
```

### ADMIN Only

### Request Body

```json
{
  "id": 101,
  "name": "Toyota Corolla",
  "type": "Sedan",
  "available": true
}
```

---

## Update Vehicle

```http
PUT /vehicles/{id}
```

### ADMIN Only

---

## Delete Vehicle

```http
DELETE /vehicles/{id}
```

### ADMIN Only

---

# Booking APIs

## Create Booking

```http
POST /bookings
```

### USER Rules

- USER can book only for themselves
- customerId must match logged-in user

### ADMIN Rules

- ADMIN can create bookings for any user

### Request Body

```json
{
  "vehicleId": 1,
  "customerId": 1,
  "startDate": "2026-05-20",
  "endDate": "2026-05-25"
}
```

---

## Cancel Booking

```http
PATCH /bookings/{id}/cancel
```

### Rules

- USER can cancel only own bookings
- ADMIN can cancel any booking

---

## Get My Bookings

```http
GET /bookings/my
```

Returns only bookings of logged-in user.

---

## Get All Bookings

```http
GET /bookings
```

### ADMIN Only

Returns all bookings in system.

---

# Validation Rules

## Vehicle Validation

- Vehicle ID Required
- Vehicle Name Cannot Be Blank
- Vehicle Type Cannot Be Blank
- Vehicle Availability Required
- Duplicate Vehicle ID Prevention

---

## Booking Validation

- Start Date Required
- End Date Required
- Start Date Must Be Before End Date
- Vehicle Must Exist
- User Must Exist
- Vehicle Must Be Available
- Booking Overlap Prevention
- Already Cancelled Booking Prevention
- USER Ownership Validation

---

# Pagination

Vehicle APIs support pagination.

Example:

```http
GET /vehicles?page=0&size=5
```

### Parameters

| Parameter | Description |
|---|---|
| page | Page Number |
| size | Number Of Records |

---

# Booking Conflict Validation

The system prevents overlapping bookings.

A vehicle cannot be booked if:

- Existing booking start date overlaps
- Existing booking end date overlaps
- Vehicle already reserved during selected period

Implemented using custom JPQL query.

---

# Exception Handling

Global exception handling implemented using:

```java
@RestControllerAdvice
```

---

## Custom Exceptions

| Exception | Purpose |
|---|---|
| ResourceNotFoundException | Missing Resources |
| BadRequestException | Invalid Requests |
| BookingAlreadyCancelledException | Duplicate Cancellation |

---

## Standard Error Response

```json
{
  "message": "Vehicle not found",
  "status": 404,
  "timestamp": "2026-05-13T10:00:00"
}
```

---

# JWT Security

Protected APIs require:

```http
Authorization: Bearer JWT_TOKEN
```

JWT token contains:

- username
- role
- expiration

---

# Swagger/OpenAPI Documentation

Swagger UI available at:

```text
http://localhost:8080/swagger-ui/index.html
```

### Features

- Interactive API Testing
- JWT Authorization Support
- DTO Schemas
- Request/Response Examples
- API Documentation

---

# H2 Database Console

Available at:

```text
http://localhost:8080/h2-console
```

---

# Running The Application

## Clone Repository

```bash
git clone https://github.com/DivyaSriKrishnakumar/vehicle-rental-management-system.git
```

---

## Navigate Into Project

```bash
cd vehicle-rental-management-system
```

---

## Build Project

```bash
mvn clean install
```

---

## Run Application

```bash
mvn spring-boot:run
```

---

# Testing Tools

The project was tested using:

- Postman
- Swagger UI
- H2 Console

---

# Author

## Divya Sri Krishnakumar

Backend project focused on enterprise-style Spring Boot REST API architecture and secure booking management.

