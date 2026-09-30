# SmartAttend — Subject Wise Attendance Management System

> **Designed for Bihar Engineering University (BEU), Patna — Session 2026–2030 (B.Tech 1st Semester Group-A)**

---

## 1. Project Overview

**SmartAttend** is a production-grade, full-stack college ERP platform built for engineering colleges affiliated with **Bihar Engineering University (BEU), Patna**. It delivers granular, subject-wise attendance tracking, hardware-ready biometric verification, real-time live attendance counters via Server-Sent Events (SSE), automated 75% attendance rule validation, mathematical shortage calculators, and the official BEU 5-mark attendance assessment slab.

Unlike basic demo applications, SmartAttend operates on normalized relational schemas with full audit trails, strict transaction boundaries, automatic Flyway database migrations, and role-based access control (RBAC).

---

## 2. Technology Stack

### Backend Architecture

- **Language & Runtime:** Java 21 LTS / Java 26
- **Framework:** Spring Boot 3.3.4 (Spring MVC, Spring Data JPA, Hibernate ORM)
- **Security:** Spring Security 6 with stateless JWT token authentication (JJWT 0.12.6) & BCrypt password hashing
- **Database:** MySQL 8.x (with zero-config embedded H2 fallback for immediate prototyping)
- **Database Migrations:** Flyway Migration Engine (Automated V1 through V6 scripts)
- **Real-Time Streaming:** Server-Sent Events (SSE) & Spring WebSockets
- **CSV & Export Engine:** OpenCSV 5.9
- **API Documentation:** SpringDoc OpenAPI 2.6.0 (Swagger 3 / OpenAPI UI)
- **Build Tool:** Apache Maven 3.9+

### Frontend Architecture

- **Structure & Logic:** HTML5, Modern Vanilla JavaScript (ES6+ modular API client)
- **Styling & Theme:** Bootstrap 5.3.3, Custom Tech-oriented Blue/Indigo ERP Design System, Dark/Light mode tokens
- **Data Visualization:** Chart.js 4.4
- **Icons & Typography:** FontAwesome 6 Pro, Google Fonts (Outfit, Inter, JetBrains Mono)
- **Reporting:** Printable Official BEU Attendance Reports with CSV export

---

## 3. Academic & Curriculum Configuration (BEU 2026–2030)

Initially seeded for **B.Tech 1st Semester (Group-A)**, Department of Computer Science & Engineering (CSE):

### Seeded Theory Subjects

1. `100102` — Engineering Mathematics - I (3-1-0 | 4 Credits)
2. `100104` — Engineering Physics (3-1-0 | 4 Credits)
3. `100105` — Introduction to AI (3-0-0 | 3 Credits)
4. `100108` — Computer Fundamentals & Emerging Technologies (3-0-0 | 3 Credits)
5. `100109` — Universal Human Values (2-0-0 | 2 Credits)
6. `100110` — Essence of Indian Constitution (2-0-0 | Non-Credit)
7. `100111` — Basics of Electrical & Electronics Engineering (3-1-0 | 4 Credits)

### Seeded Practical / Laboratory Subjects

1. `100104P` — Engineering Physics Lab (0-0-3 | 1.5 Credits)
2. `100111P` — Basics of Electrical & Electronics Engineering Lab (0-0-2 | 1 Credit)
3. `100112P` — Programming for Problem Solving Lab (0-0-4 | 2 Credits)

---

## 4. Key Architectural Modules

### 4.1. Granular Subject-Wise Attendance Tracking

Attendance is **never** averaged indiscriminately. Every attendance event records:

- `Student ID` & `Roll Number`
- `Subject ID` & `Course Code`
- `Faculty ID`
- `Section ID`
- `Session Date` & `Period Number` (1 to 8)
- `Method` (`BIOMETRIC`, `MANUAL`, `ADMIN_OVERRIDE`)
- `Status` (`PRESENT`, `ABSENT`, `LATE`, `EXCUSED`)
- `Biometric Event ID`

### 4.2. BEU 75% Attendance Rule & Shortage Calculator

Under BEU regulations (Appendix-I), students must attain at least 75% attendance in each theory and practical paper to be eligible for university examinations. Condonation up to 15% on medical grounds is permissible by the Principal (60% to 74.99%).

- **Consecutive Classes Required to Reach 75%:**
  ```math
  \text{Classes Needed } (k) = \left\lceil \frac{0.75 \times \text{Total} - \text{Attended}}{1 - 0.75} \right\rceil
  ```
- **Maximum Classes That Can Be Missed While Remaining $\ge 75\%$:**
  ```math
  \text{Margin } (m) = \left\lfloor \frac{\text{Attended}}{0.75} - \text{Total} \right\rfloor
  ```

### 4.3. BEU 5-Mark Attendance Slab Rule Engine

Configurable via `attendance_mark_rules` table:

- **96.00% – 100.00%** $\rightarrow$ **5 Marks**
- **91.00% – 95.99%** $\rightarrow$ **4 Marks**
- **86.00% – 90.99%** $\rightarrow$ **3 Marks**
- **81.00% – 85.99%** $\rightarrow$ **2 Marks**
- **75.00% – 80.99%** $\rightarrow$ **1 Mark**
- **Below 75.00%** $\rightarrow$ **0 Marks**

### 4.4. Biometric Integration & Live SSE Counter

- **Hardware Architecture:** `BiometricProvider` interface supporting `FingerprintBiometricProvider`, `FaceBiometricProvider`, `RFIDBiometricProvider`, and `DemoBiometricProvider`.
- **Idempotency & Duplicate Protection:** Scans for a student already recorded in an active session return `DUPLICATE` without double-incrementing counters.
- **Live Counter Widget:** Real-time metrics for Total Students, Present, Absent, Late, and Not Verified with live percentage bars over Server-Sent Events.

---

## 5. Demo Credentials

| Role | Username | Password | Access Portal |
| :--- | :--- | :--- | :--- |
| **Super Admin** | `admin` | `Admin@123` | `/admin/index.html` |
| **Faculty** | `faculty` | `Faculty@123` | `/faculty/index.html` |
| **Student** | `student` | `Student@123` | `/student/index.html` |

> *Pre-seeded with 30 demo students (`26105110001` to `26105110030`) across CSE-A.*

---

## 6. Setup & Installation

### Option A: Running Directly with Maven (Fastest Local Development)

```bash
# 1. Clone repository and navigate to root
cd SAS

# 2. Compile and package
mvn clean package -DskipTests

# 3. Run application
java -jar target/smart-attend-1.0.0.jar
```

*Or simply double-click `run.bat` on Windows.*

The application will start at [`http://localhost:8080`](http://localhost:8080).

---

### Option B: Running with Docker Compose (Production MySQL Setup)

```bash
# Launch MySQL 8.0 and SmartAttend application containers
docker compose up --build -d

# Check logs
docker compose logs -f
```

---

## 7. API Endpoints & Swagger Documentation

Interactive OpenAPI / Swagger UI is available at:
👉 [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)

### Key REST Endpoints

- `POST /api/auth/login` — Authenticate and receive JWT token
- `GET /api/attendance/sessions` — Fetch active sessions
- `POST /api/attendance/sessions` — Start attendance session
- `POST /api/attendance/sessions/{id}/lock` — Freeze session & compute summaries
- `GET /api/biometric/live-counter/{sessionId}` — Get live counter metrics
- `GET /api/biometric/stream/{sessionId}` — SSE real-time stream
- `POST /api/biometric/simulate` — Demo simulation mode trigger
- `GET /api/reports/student/{id}` — Generate BEU student attendance sheet
- `GET /api/reports/student/{id}/export-csv` — Export report to CSV

---

## 8. Automated Database Migrations (Flyway)

On application startup, Flyway automatically applies:

- `V1__initial_schema.sql` — 25 Relational tables with indexes, foreign keys & unique constraints
- `V2__seed_roles.sql` — RBAC roles and default admin user
- `V3__seed_academic_data.sql` — BEU Patna university info, batch 2026-2030, CSE dept, Section A
- `V4__seed_subjects.sql` — 10 BEU 1st Semester Group-A subjects (7 Theory + 3 Lab)
- `V5__seed_demo_students.sql` — 30 Demo students with biometric IDs & enrollments
- `V6__seed_attendance_rules.sql` — 75% rule, 15% medical condonation, 5-mark slab rules, timetable & BEU 2026 holiday calendar

---

## 9. Running Tests

```bash
mvn test
```

All unit tests and integration tests verify:

- 75% attendance threshold & medical condonation rules
- Shortage formula & margin calculations
- BEU 5-mark slab mapping
- Biometric duplicate protection & session locking

---

## 10. License

Developed for Bihar Engineering University (BEU), Patna affiliated institutions.  
Released under the MIT License.
