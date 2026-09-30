# 🏛️ Architecture Decision Records (DECISIONS.md)

<div align="center">

# SmartAttend Architecture Decision Log
### Formal Records of Technical Decisions, Trade-offs & Rationale

[![ADR Standard](https://img.shields.io/badge/Format-MADR%202.1.2-purple?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/docs/ARCHITECTURE.md)
[![Status](https://img.shields.io/badge/Total%20Decisions-10%20Approved-success?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/docs/DECISIONS.md)

</div>

---

## 📑 Index of Architectural Decisions

- [ADR-001: Core Backend Framework (Spring Boot 3.3.4 + Java 21 LTS)](#adr-001-core-backend-framework-spring-boot-334--java-21-lts)
- [ADR-002: Stateless JWT Authentication with Refresh Tokens over Stateful Sessions](#adr-002-stateless-jwt-authentication-with-refresh-tokens-over-stateful-sessions)
- [ADR-003: Dual-Profile Database Strategy (H2 In-Memory for Dev, MySQL 8.0 for Prod)](#adr-003-dual-profile-database-strategy-h2-in-memory-for-dev-mysql-80-for-prod)
- [ADR-004: Server-Sent Events (SSE) for Real-Time Biometric Counters](#adr-004-server-sent-events-sse-for-real-time-biometric-counters)
- [ADR-005: Modular Vanilla JS & CSS Tokens over Heavy SPA Frameworks](#adr-005-modular-vanilla-js--css-tokens-over-heavy-spa-frameworks)
- [ADR-006: Version-Controlled Flyway Database Migrations](#adr-006-version-controlled-flyway-database-migrations)
- [ADR-007: Strategy Pattern for Multi-Modal Biometric Hardware Abstraction](#adr-007-strategy-pattern-for-multi-modal-biometric-hardware-abstraction)
- [ADR-008: Mathematical Floor/Ceil Algorithms for BEU 75% Attendance Compliance](#adr-008-mathematical-floorceil-algorithms-for-beu-75-attendance-compliance)
- [ADR-009: In-Memory Streaming CSV Generation via OpenCSV](#adr-009-in-memory-streaming-csv-generation-via-opencsv)
- [ADR-010: Immutable Period Session Locking & Tamper-Evident Audit Logging](#adr-010-immutable-period-session-locking--tamper-evident-audit-logging)

---

## ADR-001: Core Backend Framework (Spring Boot 3.3.4 + Java 21 LTS)

### Status: `ACCEPTED` | Date: `2026-09-15`

### Context
We needed a high-performance, enterprise-grade backend to handle concurrent biometric scans, calculate complex academic rules across thousands of students, and integrate seamlessly with college IT infrastructure.

### Decision
Adopt **Spring Boot 3.3.4** running on **Java 21 LTS**.

### Consequences
- **Positive:** Leverages Java 21 Virtual Threads (Project Loom) capability, strong type safety, robust Spring Data JPA ORM ecosystem, enterprise Spring Security 6 integration.
- **Negative:** Slightly higher JVM memory footprint during startup compared to Go or Rust, but fully mitigated by container memory allocation (512MB RAM minimum).

---

## ADR-002: Stateless JWT Authentication with Refresh Tokens over Stateful Sessions

### Status: `ACCEPTED` | Date: `2026-09-16`

### Context
Colleges operate multiple client devices (Admin desktops, faculty laptops, mobile phones, hallway biometric tablets). Storing server-side HTTP sessions creates clustering bottlenecks and memory leaks under high concurrent traffic.

### Decision
Implement **Stateless JSON Web Tokens (JWT)** using `io.jsonwebtoken (JJWT 0.12.6)` with 24-hour access tokens and 7-day database-backed refresh tokens.

### Consequences
- **Positive:** Zero server-side session memory consumption, seamless horizontal scalability, tamper-proof cryptographic signatures (HMAC-SHA256).
- **Negative:** Access tokens cannot be revoked instantly without maintaining a token blacklist. Mitigated by short 24-hour token lifespans and refresh token revocation in the database.

---

## ADR-003: Dual-Profile Database Strategy (H2 In-Memory for Dev, MySQL 8.0 for Prod)

### Status: `ACCEPTED` | Date: `2026-09-17`

### Context
Developers, professors, and evaluators need to run the application immediately with zero database installation, while production university servers require ACID-compliant relational persistence with automated backups.

### Decision
Implement dual Spring Profiles (`h2` and `mysql`). The default configuration boots seamlessly into H2 in-memory mode when MySQL is unavailable.

### Consequences
- **Positive:** Zero friction for new contributors (just execute `run.bat`), while production deployments leverage MySQL 8.x connection pooling (HikariCP) and transactional integrity.
- **Negative:** All SQL migrations in Flyway must use standard ANSI-SQL constructs compatible with both H2 and MySQL 8 dialects.

---

## ADR-004: Server-Sent Events (SSE) for Real-Time Biometric Counters

### Status: `ACCEPTED` | Date: `2026-09-18`

### Context
During lecture attendance marking, faculty screens must update immediately as students tap biometric scanners at the classroom door.

### Decision
Use **Server-Sent Events (SSE)** via Spring's `SseEmitter` instead of full-duplex WebSockets.

### Consequences
- **Positive:** SSE is built directly on standard HTTP/1.1 and HTTP/2, requires no special proxy or firewall configuration, supports automatic browser reconnection natively, and provides unidirectional streaming with minimal memory overhead.
- **Negative:** Unidirectional only (client cannot send messages back across the SSE stream), which is ideal for this use case since clients submit data via standard REST `POST` requests.

---

## ADR-005: Modular Vanilla JS & CSS Tokens over Heavy SPA Frameworks

### Status: `ACCEPTED` | Date: `2026-09-19`

### Context
Engineering college portals are often accessed over low-bandwidth campus networks or older institutional hardware. Complex node build pipelines (React/Next.js/Vue) create unnecessary compilation complexity for on-premise deployments.

### Decision
Build frontend portals using **Modern Vanilla JavaScript (ES6 Modules)**, Bootstrap 5.3.3, and custom CSS design tokens served directly from Spring Boot's static resources.

### Consequences
- **Positive:** Zero build step needed for frontend, instant page loads (<100ms), no Node.js runtime required on the server, easily customizable by college webmasters.
- **Negative:** Requires disciplined modular code organization in JavaScript to prevent spaghetti code. Handled by establishing strict patterns in `RULES.md`.

---

## ADR-006: Version-Controlled Flyway Database Migrations

### Status: `ACCEPTED` | Date: `2026-09-20`

### Context
Schema modifications (adding academic rules, subject tables, student rosters) must be tracked, reproducible, and applied automatically upon container startup.

### Decision
Adopt **Flyway Migration Engine** with automated migration scripts (`V1__initial_schema.sql` through `V6__seed_attendance_rules.sql`).

### Consequences
- **Positive:** Guaranteed consistency across all environments, automatic baseline validation, zero manual SQL execution required during deployment.
- **Negative:** Migration scripts are strictly immutable once applied. Schema alterations must be appended as new sequential version numbers.

---

## ADR-007: Strategy Pattern for Multi-Modal Biometric Hardware Abstraction

### Status: `ACCEPTED` | Date: `2026-09-21`

### Context
Colleges deploy different biometric hardware across campuses (Mantra/Morpho optical fingerprint scanners, RFID card readers, facial recognition IP cameras, or web simulators for demo purposes).

### Decision
Implement the **Strategy Design Pattern** via `BiometricProvider` interface with dedicated implementations (`FingerprintBiometricProvider`, `RFIDBiometricProvider`, `FaceBiometricProvider`, `DemoBiometricProvider`).

### Consequences
- **Positive:** High extensibility; new vendor hardware can be added simply by implementing one Java interface without touching core attendance services.
- **Negative:** Requires hardware-specific bridge drivers or local REST agents for USB-attached physical devices.

---

## ADR-008: Mathematical Floor/Ceil Algorithms for BEU 75% Attendance Compliance

### Status: `ACCEPTED` | Date: `2026-09-22`

### Context
Approximation errors or improper rounding in attendance shortage calculations lead to unlawful debarment of students or invalid admit cards.

### Decision
Use strict mathematical formulas with Ceiling function ($\lceil \dots \rceil$) for classes needed and Floor function ($\lfloor \dots \rfloor$) for safe bunk margins:
- $\text{Needed } (k) = \max\left(0, \lceil (0.75 \times \text{Total} - \text{Attended}) / 0.25 \rceil\right)$
- $\text{Margin } (m) = \max\left(0, \lfloor (\text{Attended} / 0.75) - \text{Total} \rfloor\right)$

### Consequences
- **Positive:** 100% mathematical precision with zero ambiguity; mathematically impossible for a student following the calculated margin to fall below 75.00%.
- **Negative:** None.

---

## ADR-009: In-Memory Streaming CSV Generation via OpenCSV

### Status: `ACCEPTED` | Date: `2026-09-23`

### Context
End-of-semester attendance reports must be submitted to the BEU Controller of Examinations in standardized spreadsheet formats.

### Decision
Integrate **OpenCSV 5.9** with streaming HTTP responses directly to the client browser (`Content-Disposition: attachment; filename="attendance_report.csv"`).

### Consequences
- **Positive:** No temporary files created on disk, instant download start, handles large student rosters without out-of-memory errors.
- **Negative:** CSV does not support complex cell styling; handled by providing formatted HTML print templates as well.

---

## ADR-010: Immutable Period Session Locking & Tamper-Evident Audit Logging

### Status: `ACCEPTED` | Date: `2026-09-24`

### Context
Attendance fraud often occurs when records are modified days after a class has concluded.

### Decision
Introduce a strict **Session Lock state**. Once locked by faculty at period conclusion, attendance records become immutable. Any subsequent changes require an administrative override which writes a non-deletable record to `audit_logs`.

### Consequences
- **Positive:** Complete institutional integrity, eliminates unauthorized retrospective modifications, provides full forensic auditability.
- **Negative:** Requires faculty to lock sessions diligently; handled by automatic end-of-day session closure background job.
