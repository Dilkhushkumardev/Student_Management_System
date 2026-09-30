# 🧠 Project Memory & Context (MEMORY.md)

<div align="center">

# SmartAttend Project Memory & State
### Current Development State, Runtime Configurations & System Context

[![Project Status](https://img.shields.io/badge/System%20State-Operational%20%26%20Tested-success?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/docs/TASKS.md)
[![Last Updated](https://img.shields.io/badge/Updated-Session%202026--2030-blue?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/README.md)

</div>

---

## 1. Executive Snapshot & Current Status

SmartAttend is **100% operational** with full backend business logic, automated Flyway migrations, biometric simulation engine, role-based portals (Admin, Faculty, Student), and official BEU 75% attendance rule & 5-mark assessment engines active.

- **Primary Repository:** `c:/Users/dilkh/OneDrive/Desktop/SAS`
- **Current Active Branch:** `main`
- **Application Port:** `http://localhost:8080`
- **OpenAPI / Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **API Spec (v3):** `http://localhost:8080/v3/api-docs`

---

## 2. Seeded Accounts & Master Credentials

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Default System Logins                           │
├─────────────┬────────────────┬──────────────┬──────────────────────────┤
│ Role        │ Username       │ Password     │ Landing Portal           │
├─────────────┼────────────────┼──────────────┼──────────────────────────┤
│ Super Admin │ admin          │ Admin@123    │ /admin/index.html        │
│ Faculty     │ faculty        │ Faculty@123  │ /faculty/index.html      │
│ Student     │ student        │ Student@123  │ /student/index.html      │
└─────────────┴────────────────┴──────────────┴──────────────────────────┘
```

> **Seeded Student Roster:** 30 Demo B.Tech CSE students (`26105110001` through `26105110030`) enrolled in B.Tech 1st Semester Section A.

---

## 3. Seeded BEU Academic Syllabus (1st Semester CSE)

### Theory Subjects (4 & 3 Credits)
1. `100102` — **Engineering Mathematics - I** (3-1-0 | 4 Credits)
2. `100104` — **Engineering Physics** (3-1-0 | 4 Credits)
3. `100105` — **Introduction to AI** (3-0-0 | 3 Credits)
4. `100108` — **Computer Fundamentals & Emerging Tech** (3-0-0 | 3 Credits)
5. `100109` — **Universal Human Values (UHV)** (2-0-0 | 2 Credits)
6. `100110` — **Essence of Indian Constitution** (2-0-0 | Non-Credit)
7. `100111` — **Basics of Electrical & Electronics Engg** (3-1-0 | 4 Credits)

### Practical Laboratory Subjects (1 to 2 Credits)
1. `100104P` — **Engineering Physics Lab** (0-0-3 | 1.5 Credits)
2. `100111P` — **Basics of Electrical & Electronics Lab** (0-0-2 | 1 Credit)
3. `100112P` — **Programming for Problem Solving Lab** (0-0-4 | 2 Credits)

---

## 4. Active Runtime Environment

- **Java Runtime:** Java 21 LTS (or Java 26 compatible)
- **Spring Boot Version:** `3.3.4`
- **Default Database Profile:** `h2` (Instant in-memory setup) or `mysql` (Docker / Production)
- **JWT Secret:** Injected via `JWT_SECRET` env var (256-bit secure key in `.env.example`)
- **JWT Expiration:** 24 Hours (`86400000ms`)
- **Refresh Token Expiration:** 7 Days (`604800000ms`)

---

## 5. Architectural Invariants (Permanent Memory)

> [!NOTE]
> Never violate these constraints during future feature additions:

1. **BEU 75% Boundary Rule:** Attendance percentage is calculated strictly per subject ($k = \text{attended} / \text{total} \times 100$). It is never aggregated across subjects to avoid masking course-specific shortages.
2. **BEU 5-Mark Assessment Slab:**
   - $\ge 96\% \rightarrow 5$ Marks
   - $91\text{--}95.99\% \rightarrow 4$ Marks
   - $86\text{--}90.99\% \rightarrow 3$ Marks
   - $81\text{--}85.99\% \rightarrow 2$ Marks
   - $75\text{--}80.99\% \rightarrow 1$ Mark
   - $< 75\% \rightarrow 0$ Marks
3. **Session Immutability:** Once an `AttendanceSession` status is set to `LOCKED`, attendance records cannot be modified via standard faculty endpoints. Only Super Admin can override via an audited override transaction.
4. **Biometric Scan Idempotency:** Duplicate scans within an active period session return `DUPLICATE` without incrementing attendance counters.

---

## 6. Immediate Next Steps / Potential Enhancements

- [ ] Add WhatsApp notification webhook trigger when a student's attendance drops below 75% in any subject.
- [ ] Implement biometric device hardware health polling daemon.
- [ ] Add semester-end consolidated PDF generation with university seal watermark.
