# ODTrack Academia™ - Backend Service

A Spring Boot-based REST API service for OD (On Duty) request management in academic institutions.

## Project Overview

ODTrack Academia™ Backend provides secure, low-latency endpoints for the Flutter mobile client to:
- Authenticate both student (Reg-No/DOB) and staff (Email/Password) roles
- Manage OD lifecycle: create, approve/reject, audit
- Serve timetable & staff directory data
- Generate digitally-signed approval PDFs

## Technology Stack

- **Runtime**: Spring Boot 3.5.4 + Java 23
- **Web**: Spring Web MVC (REST), Spring Security (JWT), Spring Data JPA
- **Database**: PostgreSQL 15
- **File Storage**: Local MinIO (S3-compatible) for signed PDFs
- **Background Jobs**: Spring Batch nightly timetable import (CSV/XLSX)
- **Build**: Maven, JAR packaging, Jib for containerisation
- **Monitoring**: Spring Boot Actuator + Micrometer → Prometheus → Grafana

## Authentication & Authorization

### Login Endpoints
- `POST /auth/student` - Student login with register number and DOB
- `POST /auth/staff` - Staff login with email and password

### Security
- JWT tokens (30 min expiry) with role claims (ROLE_STUDENT, ROLE_STAFF, ROLE_ADMIN)
- bcrypt 12 rounds for staff passwords
- Students authenticate with register number + DOB (no password)

## API Endpoints (v1)

### Timetable
- `GET /api/v1/timetable?date=YYYY-MM-DD&year=1..4` - Get timetable data

### Staff Directory
- `GET /api/v1/staff/{staffId}` - Get staff member details (public fields)

### OD Requests
- `POST /api/v1/od-requests` - Create new OD request (student)
- `GET /api/v1/od-requests` - Get own OD requests list
- `PATCH /api/v1/od-requests/{id}/status` - Approve/reject OD request (staff)
- `GET /api/v1/od-requests/{id}/approval-pdf` - Download approval PDF

## Data Model

### Users Table
```sql
users(
    id, 
    role, 
    register_number, 
    dob, 
    email, 
    password_hash, 
    full_name, 
    subject, 
    year_handled, 
    phone
)
```

### Timetable Table
```sql
timetable(
    id, 
    staff_id, 
    date, 
    period, 
    year, 
    hall, 
    subject
)
```

### OD Requests Table
```sql
od_requests(
    id, 
    student_id, 
    date, 
    period, 
    reason, 
    status, 
    staff_id, 
    signed_pdf_path, 
    created_at, 
    updated_at
)
```

## Security & Compliance

- RBAC enforced via method-level @PreAuthorize
- GDPR/FERPA compliant: minimal PII retention
- PDFs auto-archive after 1 year
- Stateless design for horizontal scaling

## Non-Functional Requirements

- P95 API latency < 200ms under 500 concurrent users
- 99.5% monthly uptime (institutional SLA)
- Horizontal scale-ready: stateless pods behind Nginx

## Development Milestones

- **M1 (Week 1)** - DB schema & Docker compose scaffold ✅
- **M2 (Week 3)** - Auth endpoints + JWT issuer
- **M3 (Week 5)** - Timetable CRUD & CSV importer
- **M4 (Week 7)** - OD flow + PDF signing service
- **M5 (Week 9)** - Load test & security scan
- **M6 (Week 10)** - Tag v1.0, hand-over to DevOps

## Getting Started

### Prerequisites
- Java 23
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL 15
- MinIO

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd ODTrack-Academia-Backend
   ```

2. Start infrastructure services:
   ```bash
   docker-compose up -d postgres minio
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the API documentation:
   ```
   http://localhost:8080/swagger-ui.html
   ```

## Environment Configuration

Copy `.env.template` to `.env` and configure:
- Database connection details
- JWT secret key
- MinIO credentials
- File storage paths

## Monitoring

- Health checks: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- Prometheus endpoint: `http://localhost:8080/actuator/prometheus`

## License

Copyright © 2025 Office of Academic Affairs. All rights reserved.