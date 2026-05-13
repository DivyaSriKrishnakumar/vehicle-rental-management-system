Vehicle Rental Management System
Overview
Vehicle Rental Management System is a secure REST API backend application developed using Spring Boot. The project manages vehicles, users, and rental bookings with authentication, role-based authorization, booking conflict validation, DTO architecture, pagination, Swagger API documentation, and global exception handling.
This project was designed as a portfolio-quality backend system following layered architecture and production-style API design principles.
 
Features
Authentication & Authorization
JWT Authentication
Spring Security integration
Stateless authentication
USER and ADMIN roles
Protected APIs
Role-based access control
Custom AccessDenied handling
Secure booking ownership validation
 
Vehicle Management
Add new vehicles
Update vehicle information
Delete vehicles
Get vehicle by ID
Get all vehicles
Vehicle availability management
Pagination support
DTO-based request/response handling
Validation support
 
Booking Management
Create booking
Cancel booking
Booking history API
User-specific booking retrieval
Admin access to all bookings
Booking overlap conflict prevention
Vehicle maintenance availability checks
Booking status tracking
 
Tech Stack
Technology
Purpose
Java 17
Programming Language
Spring Boot
Backend Framework
Spring Security
Authentication & Authorization
JWT
Token-based Authentication
Spring Data JPA
Database Access
Hibernate
ORM
H2 Database
Development Database
Maven
Dependency Management
Swagger/OpenAPI
API Documentation
Postman
API Testing

 
Architecture
The project follows layered architecture:
Controller Layer
↓
Service Layer
↓
Repository Layer
↓
Database
 
Project Structure
src/main/java
│
├── controller
│ ├── AuthController
│ ├── VehicleController
│ └── BookingController
│
├── service
│ ├── VehicleService
│ ├── BookingService
│ └── impl
│ ├── VehicleServiceImpl
│ └── BookingServiceImpl
│
├── repository
│ ├── VehicleRepository
│ ├── BookingRepository
│ └── UserRepository
│
├── entity
│ ├── Vehicle
│ ├── Booking
│ └── AppUser
│
├── dto
│ ├── VehicleRequestDTO
│ ├── VehicleResponseDTO
│ ├── BookingRequestDTO
│ └── BookingResponseDTO
│
├── security
│ ├── JwtFilter
│ ├── JwtUtil
│ ├── SecurityConfig
│ └── CustomAccessDeniedHandler
│
├── exception
│ ├── GlobalExceptionHandler
│ ├── ResourceNotFoundException
│ ├── BadRequestException
│ └── BookingAlreadyCancelledException
│
└── enums
└── BookingStatus
 
Security & Roles
USER Role
A USER can:
View vehicles
Create bookings only for themselves
Cancel only their own bookings
View their own booking history
 
ADMIN Role
An ADMIN can:
Add vehicles
Update vehicles
Delete vehicles
Create bookings for any user
Cancel any booking
View all bookings
Manage vehicle availability
 
Database Entities
Vehicle Entity
Represents vehicles available for rental.
Fields
Field
Type
id
Long
name
String
type
String
available
Boolean

 
Booking Entity
Represents booking transactions.
Fields
Field
Type
id
Long
vehicleId
Long
customerId
Long
startDate
LocalDate
endDate
LocalDate
status
BookingStatus

 
AppUser Entity
Represents authenticated users.
Fields
Field
Type
id
Long
username
String
password
String
role
String

 
DTO Architecture
DTOs are used to separate API contracts from database entities.
VehicleRequestDTO
Used when creating/updating vehicles.
VehicleResponseDTO
Used when returning vehicle data.
BookingRequestDTO
Used for booking creation.
BookingResponseDTO
Used when returning booking details.
 
API Endpoints
Authentication APIs
Register User
POST /auth/register
Request Body
{
"username": "divya",
"password": "password123",
"role": "USER"
}
 
Login
POST /auth/login
Request Body
{
"username": "divya",
"password": "password123"
}
Response
{
"token": "JWT_TOKEN"
}
 
Vehicle APIs
Get All Vehicles
GET /vehicles?page=0&size=5
Features
Pagination support
DTO response
Public access
 
Get Vehicle By ID
GET /vehicles/{id}
 
Add Vehicle
POST /vehicles
ADMIN Only
Request Body
{
"id": 101,
"name": "Toyota Corolla",
"type": "Sedan",
"available": true
}
 
Update Vehicle
PUT /vehicles/{id}
ADMIN Only
 
Delete Vehicle
DELETE /vehicles/{id}
ADMIN Only
 
Booking APIs
Create Booking
POST /bookings
USER Rules
USER can book only for themselves
customerId must match logged-in user
ADMIN Rules
ADMIN can create bookings for any user
Request Body
{
"vehicleId": 1,
"customerId": 1,
"startDate": "2026-05-20",
"endDate": "2026-05-25"
}
 
Cancel Booking
PATCH /bookings/{id}/cancel
Rules
USER can cancel only own bookings
ADMIN can cancel any booking
 
Get My Bookings
GET /bookings/my
Returns only bookings of logged-in user.
 
Get All Bookings
GET /bookings
ADMIN Only
Returns all bookings in system.
 
Validation Rules
Vehicle Validation
Vehicle ID required
Vehicle name cannot be blank
Vehicle type cannot be blank
Vehicle availability required
Duplicate vehicle ID prevention
 
Booking Validation
Start date required
End date required
Start date must be before end date
Vehicle must exist
User must exist
Vehicle must be available
Booking overlap prevention
Already cancelled booking prevention
USER ownership validation
 
Pagination
Vehicle APIs support pagination.
Example:
GET /vehicles?page=0&size=5
Parameters
Parameter
Description
page
Page number
size
Number of records

 
Booking Conflict Validation
The system prevents overlapping bookings.
A vehicle cannot be booked if:
Existing booking start date overlaps
Existing booking end date overlaps
Vehicle already reserved during selected period
Implemented using custom JPQL query.
 
Exception Handling
Global exception handling implemented using:
@RestControllerAdvice
 
Custom Exceptions
Exception
Purpose
ResourceNotFoundException
Missing resources
BadRequestException
Invalid requests
BookingAlreadyCancelledException
Duplicate cancellation

 
Standard Eror Response
{
"message": "Vehicle not found",
"status": 404,
"timestamp": "2026-05-13T10:00:00"
}
 
JWT Security
Protected APIs require:
Authorization: Bearer JWT_TOKEN
JWT token contains:
username
role
expiration
 
Swagger/OpenAPI Documentation
Swagger UI available at:
http://localhost:8080/swagger-ui/index.html
Features:
Interactive API testing
JWT Authorization support
DTO schemas
Request/response examples
API documentation
 
H2 Database Console
Available at:
http://localhost:8080/h2-console
 
Running the Application
Clone Repository
git clone <repository-url>
git clone https://github.com/DivyaSriKrishnakumar/vehicle-rental-management-system.git 
 
Build Project
mvn clean install
 
Run Application
mvn spring-boot:run
 
Testing Tools
The project was tested using:
Postman
Swagger UI
H2 Console
 
 
Author
Divya Sri Krishnakumar
Backend project focused on enterprise-style Spring Boot REST API architecture and secure booking management.
