# 🧪 Comprehensive Test Plan & QA Checklist (TEST_PLAN.md)

<div align="center">

# SmartAttend Verification Suite & Quality Assurance Plan
### Test Strategy, Mathematical Boundary Cases, Security Audits & E2E Checklist

[![Test Status](https://img.shields.io/badge/Test%20Suites-Passing-brightgreen?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/docs/TASKS.md)
[![Coverage Target](https://img.shields.io/badge/Coverage-90%25%20Core%20Logic-blue?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/docs/RULES.md)

</div>

---

## 1. Quality Assurance Strategy & Test Pyramid

```
                ▲
               / \
              /   \
             / E2E \       Playwright / Browser QA & Kiosk Simulation
            /-------\
           /  Integ  \     Spring Boot @SpringBootTest / MockMvc / REST API
          /-----------\
         /    Unit     \   JUnit 5 & Mockito (Formulas, Slabs, JWT Providers)
        /---------------\
```

---

## 2. Mathematical Rule Engine Boundary Test Cases

The BEU mathematical engine must pass the following precise test vectors:

### 2.1. BEU 75% Shortage & Bunk Margin Formula Tests

$$\text{Classes Needed } (k) = \max\left(0, \, \left\lceil \frac{0.75 \times \text{Total} - \text{Attended}}{0.25} \right\rceil\right)$$
$$\text{Safe Bunk Margin } (m) = \max\left(0, \, \left\lfloor \frac{\text{Attended}}{0.75} - \text{Total} \right\rfloor\right)$$

| Case ID | Total Classes | Classes Attended | Actual % | Expected Status | Classes Needed ($k$) | Safe Margin ($m$) | Expected Assessment Marks |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| `TC-M01` | 40 | 40 | 100.00% | SAFE | 0 | 13 | **5 / 5** |
| `TC-M02` | 40 | 38 | 95.00% | SAFE | 0 | 10 | **4 / 5** |
| `TC-M03` | 40 | 35 | 87.50% | SAFE | 0 | 6 | **3 / 5** |
| `TC-M04` | 40 | 33 | 82.50% | SAFE | 0 | 4 | **2 / 5** |
| `TC-M05` | 40 | 30 | 75.00% | BORDERLINE | 0 | 0 | **1 / 5** |
| `TC-M06` | 40 | 29 | 72.50% | CONDONATION | 3 | 0 | **0 / 5** |
| `TC-M07` | 40 | 24 | 60.00% | CONDONATION | 18 | 0 | **0 / 5** |
| `TC-M08` | 40 | 23 | 57.50% | DEBARRED | 21 | 0 | **0 / 5** |
| `TC-M09` | 0 | 0 | 0.00% | NO DATA | 0 | 0 | **0 / 5** |
| `TC-M10` | 1 | 0 | 0.00% | SHORTAGE | 3 | 0 | **0 / 5** |

---

## 3. Security & RBAC Authorization Test Matrix

| Endpoint | Method | Unauthenticated | Student Role | Faculty Role | Super Admin Role |
| :--- | :---: | :---: | :---: | :---: | :---: |
| `/api/auth/login` | POST | 🟢 200 OK | 🟢 200 OK | 🟢 200 OK | 🟢 200 OK |
| `/api/student/my-attendance` | GET | 🔴 401 Unauth | 🟢 200 OK | 🟢 200 OK | 🟢 200 OK |
| `/api/attendance/session/create` | POST | 🔴 401 Unauth | 🔴 403 Forbidden | 🟢 200 OK | 🟢 200 OK |
| `/api/attendance/session/lock` | POST | 🔴 401 Unauth | 🔴 403 Forbidden | 🟢 200 OK | 🟢 200 OK |
| `/api/admin/override-attendance` | POST | 🔴 401 Unauth | 🔴 403 Forbidden | 🔴 403 Forbidden | 🟢 200 OK |
| `/api/academic/departments` | GET | 🔴 401 Unauth | 🟢 200 OK | 🟢 200 OK | 🟢 200 OK |

---

## 4. End-to-End User Flow QA Checklist

### 4.1. Authentication Flow
- [ ] Attempt login with invalid password $\rightarrow$ Displays clear red error toast without crashing.
- [ ] Successful login with `admin` $\rightarrow$ Redirects to `/admin/index.html` with Bearer token stored.
- [ ] Refresh token rotation $\rightarrow$ Calling `/api/auth/refresh` returns fresh valid JWT token.
- [ ] Logout $\rightarrow$ Clears client storage and redirects to `/login.html`.

### 4.2. Faculty Session & Live Biometric Flow
- [ ] Faculty navigates to `/faculty/index.html` and selects *Engineering Mathematics - I (Period 1, CSE-A)*.
- [ ] Clicks **Start Session** $\rightarrow$ Session status changes to `IN_PROGRESS`.
- [ ] Open Biometric Simulator at `/biometric/index.html` and simulate scan for student `26105110001`.
- [ ] Verify Faculty screen increments live counter instantly via SSE without page refresh.
- [ ] Re-simulate scan for same student in same session $\rightarrow$ Returns `DUPLICATE` status; live counter does not double-count.
- [ ] Faculty clicks **Lock Session** $\rightarrow$ Status changes to `LOCKED`.
- [ ] Attempt to manually modify attendance after locking $\rightarrow$ Returns HTTP 400 *Session is locked*.

### 4.3. Student Portal & Safety Index Flow
- [ ] Student logs in as `student` / `Student@123`.
- [ ] Dashboard displays 7 Theory + 3 Practical subjects with individual progress gauges.
- [ ] Subject with $>75\%$ displays **Safe (Margin: +X)** in Emerald color.
- [ ] Subject with $<75\%$ displays **Shortage (Need: +Y consecutive classes)** with recovery calculation in Crimson color.
- [ ] Assessment mark column shows correct BEU internal mark (0 to 5) per subject.

---

## 5. Automated Verification Commands (cURL & CLI)

```bash
# 1. Health Check Endpoint
curl -i -X GET http://localhost:8080/actuator/health

# 2. Authenticate as Super Admin
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Admin@123"}' > auth_response.json

# 3. Retrieve Student Attendance (Using JWT Token)
TOKEN=$(cat auth_response.json | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
curl -i -X GET http://localhost:8080/api/attendance/student/my-attendance \
  -H "Authorization: Bearer $TOKEN"

# 4. Stream Biometric Events (SSE)
curl -N -X GET http://localhost:8080/api/biometric/stream/1 \
  -H "Authorization: Bearer $TOKEN"
```
