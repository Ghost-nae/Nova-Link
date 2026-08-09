# Nova-Link

Nova-Link is a secure banking transaction management system built with **Java and Spring Boot**. It provides REST APIs for user authentication, account management, fund transfers, transaction recording, and transaction reversals.

## Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Lombok

## Features

* User registration and authentication
* JWT-based security
* Role-based authorization
* Bank and account management
* Fund transfers
* Transaction recording
* Failed transaction recording
* Transaction reversals
* Account status validation
* Balance validation
* Transaction audit trail

## Architecture

```
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

Security is handled through Spring Security and JWT authentication.

## Main Endpoints

### Authentication

```
POST /api/auth/register
POST /api/auth/login
```

### Transactions

```
POST /api/transactions/transfer
POST /api/transactions/reversal
```

Authenticated requests require:

```
Authorization: Bearer <JWT>
```

## Getting Started

### Prerequisites

* JDK 17+
* PostgreSQL
* Git

### Clone

```bash
git clone <repository-url>
cd Nova-Link
```

### Run

Windows:

```bash
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## Transaction Flow

Nova-Link records transaction activity for auditing and quality purposes. Successful, failed, and reversed transactions are maintained rather than simply changing account balances.

## Author

**Nkosinathi Manda**

Java Developer | Spring Boot | REST APIs | SQL | Software Testing
