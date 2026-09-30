# 🔌 Complete REST API Reference (API_DOCUMENTATION.md)

<div align="center">

# SmartAttend REST API & Real-Time Event Specification
### OpenAPI 3.0 / Swagger Documented Endpoints

[![Swagger UI](https://img.shields.io/badge/Swagger%20UI-Interactive%20Console-85EA2D?style=for-the-badge&logo=swagger)](http://localhost:8080/swagger-ui/index.html)
[![OpenAPI Spec](https://img.shields.io/badge/OpenAPI-v3.0%20JSON-brightgreen?style=for-the-badge)](http://localhost:8080/v3/api-docs)
[![Auth](https://img.shields.io/badge/Auth-Bearer%20JWT-red?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/docs/SECURITY.md)

</div>

---

## 1. Global API Standards

- **Base URL:** `http://localhost:8080/api`
- **Content Type:** `application/json; charset=UTF-8`
- **Security Header:** `Authorization: Bearer <JWT_ACCESS_TOKEN>`

### 1.1. Standard Response Envelope (`ApiResponse<T>`)

All endpoints return a predictable JSON payload:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { ... },
  "timestamp": "2026-09-30T18:30:00Z"
}
```

### 1.2. Standard Error Response

```json
{
  "success": false,
  "message": "Session is locked and cannot be modified",
  "data": null,
  "errorCode": "SESSION_LOCKED_ERROR",
  "timestamp": "2026-09-30T18:30:00Z"
}
```

---

## 2. Authentication Endpoints (`/api/auth`)

### 2.1. User Login
- **Endpoint:** `POST /api/auth/login`
- **Access:** Public
- **Request Body:**
```json
{
  "username": "faculty",
  "password": "Faculty@123"
}
```
- **Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Authentication successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "7c9e6679-7425-40de-944b-e07fc1f90ae7",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "user": {
      "id": 2,
      "username": "faculty",
      "fullName": "Prof. R. K. Sharma",
      "email": "faculty@smartattend.beu.edu.in",
      "role": "ROLE_FACULTY"
    }
  }
}
```

### 2.2. Refresh Access Token
- **Endpoint:** `POST /api/auth/refresh`
- **Request Body:** `{ "refreshToken": "7c9e6679-7425-40de-944b-e07fc1f90ae7" }`
- **Response (200 OK):** Returns fresh `accessToken`.

---

## 3. Attendance Management Endpoints (`/api/attendance`)

### 3.1. Create / Start Period Session
- **Endpoint:** `POST /api/attendance/session/create`
- **Access:** `ROLE_FACULTY`, `ROLE_SUPER_ADMIN`
- **Request Body:**
```json
{
  "subjectId": 1,
  "sectionId": 1,
  "periodNumber": 1,
  "sessionDate": "2026-10-01",
  "roomNumber": "LH-101"
}
```
- **Response (200 OK):** Returns created `AttendanceSessionResponse` with status `IN_PROGRESS`.

### 3.2. Record Individual Student Attendance
- **Endpoint:** `POST /api/attendance/mark`
- **Access:** `ROLE_FACULTY`, `ROLE_SUPER_ADMIN`
- **Request Body:**
```json
{
  "sessionId": 105,
  "studentId": 1,
  "status": "PRESENT",
  "method": "MANUAL",
  "remarks": "Marked present by instructor"
}
```

### 3.3. Bulk Mark Attendance for Full Class
- **Endpoint:** `POST /api/attendance/bulk-mark`
- **Access:** `ROLE_FACULTY`, `ROLE_SUPER_ADMIN`
- **Request Body:**
```json
{
  "sessionId": 105,
  "records": [
    { "studentId": 1, "status": "PRESENT" },
    { "studentId": 2, "status": "ABSENT" },
    { "studentId": 3, "status": "PRESENT" }
  ]
}
```

### 3.4. Lock Session (Cryptographic Immutability)
- **Endpoint:** `POST /api/attendance/session/{sessionId}/lock`
- **Access:** `ROLE_FACULTY`, `ROLE_SUPER_ADMIN`
- **Response (200 OK):** Returns session with `status: LOCKED`, `lockedAt`, and `lockedBy`.

---

## 4. Student Attendance & BEU Calculator Endpoints (`/api/student`)

### 4.1. Get Authenticated Student's Full Attendance Summary
- **Endpoint:** `GET /api/attendance/student/my-attendance`
- **Access:** `ROLE_STUDENT`
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Student attendance loaded",
  "data": {
    "student": {
      "id": 1,
      "rollNumber": "26105110001",
      "fullName": "Aarav Kumar",
      "registrationNo": "BEU2026CSE001",
      "branch": "Computer Science & Engineering",
      "semester": "1st Semester",
      "section": "A"
    },
    "overallPercentage": 82.5,
    "totalClassesHeld": 160,
    "totalClassesAttended": 132,
    "subjectSummaries": [
      {
        "subjectCode": "100102",
        "subjectName": "Engineering Mathematics - I",
        "subjectType": "THEORY",
        "credits": 4.0,
        "classesHeld": 40,
        "classesAttended": 35,
        "percentage": 87.50,
        "status": "SAFE",
        "marginClasses": 6,
        "classesNeededFor75": 0,
        "assessmentMarks": 3,
        "maxMarks": 5
      },
      {
        "subjectCode": "100104",
        "subjectName": "Engineering Physics",
        "subjectType": "THEORY",
        "credits": 4.0,
        "classesHeld": 40,
        "classesAttended": 28,
        "percentage": 70.00,
        "status": "CONDONATION_REQUIRED",
        "marginClasses": 0,
        "classesNeededFor75": 8,
        "assessmentMarks": 0,
        "maxMarks": 5
      }
    ]
  }
}
```

---

## 5. Biometric Hardware & Live SSE Endpoints (`/api/biometric`)

### 5.1. Process Biometric Scan
- **Endpoint:** `POST /api/biometric/scan`
- **Access:** `ROLE_SUPER_ADMIN`, `ROLE_FACULTY`, `DEVICE_AGENT`
- **Request Body:**
```json
{
  "deviceId": "BIO-DEV-CSE-01",
  "biometricHash": "FINGERPRINT_HASH_HEX_26105110001",
  "sessionId": 105
}
```
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Biometric verification successful",
  "data": {
    "scanStatus": "PRESENT",
    "studentName": "Aarav Kumar",
    "rollNumber": "26105110001",
    "verifiedAt": "2026-10-01T09:12:45Z"
  }
}
```

### 5.2. Connect to Real-Time Live Counter Stream (SSE)
- **Endpoint:** `GET /api/biometric/stream/{sessionId}`
- **Headers:** `Accept: text/event-stream`
- **Stream Event Payload:**
```
event: stats-update
data: {"sessionId":105,"totalStudents":30,"presentCount":25,"absentCount":5,"lateCount":0,"attendancePercentage":83.33,"lastStudentName":"Aarav Kumar"}
```

---

## 6. Official Reports & Export Endpoints (`/api/reports`)

### 6.1. Export Subject Attendance Sheet (OpenCSV)
- **Endpoint:** `GET /api/reports/subject/{subjectId}/csv`
- **Access:** `ROLE_FACULTY`, `ROLE_SUPER_ADMIN`
- **Response:** `Content-Type: text/csv; filename="BEU_Attendance_100102.csv"`
- **CSV Headers:** `Roll No, Registration No, Student Name, Total Classes, Attended, Percentage, BEU Status, Internal 5-Mark Score`

---

## 7. HTTP Status Code Reference

| Status Code | Meaning | Example Occurrence |
| :--- | :--- | :--- |
| **`200 OK`** | Request succeeded | Data loaded or session updated |
| **`201 Created`** | Resource created | New session or attendance recorded |
| **`400 Bad Request`** | Validation failure | Missing required fields or session locked |
| **`401 Unauthorized`** | Missing/Invalid JWT | Token expired or invalid signature |
| **`403 Forbidden`** | Insufficient role | Student attempting to lock faculty session |
| **`404 Not Found`** | Resource not found | Subject or Student ID does not exist |
| **`500 Server Error`** | Unhandled internal exception | Handled gracefully via GlobalExceptionHandler |
