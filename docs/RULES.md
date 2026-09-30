# 📜 Engineering & AI Rulebook (RULES.md)

<div align="center">

# SmartAttend Engineering Standards & AI Development Rules
### Mandatory Conventions, Quality Gates & Architectural Constraints

[![Java Standard](https://img.shields.io/badge/Java-21%20LTS%20Standards-ED8B00?style=for-the-badge&logo=openjdk)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/pom.xml)
[![Architecture](https://img.shields.io/badge/Pattern-Layered%20Service%20Architecture-blue?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/docs/ARCHITECTURE.md)
[![Security](https://img.shields.io/badge/Security-Strict%20Zero%20Trust-red?style=for-the-badge)](file:///c:/Users/dilkh/OneDrive/Desktop/SAS/docs/SECURITY.md)

</div>

---

## 1. General Principles & Core Invariants

> [!IMPORTANT]
> All AI coding assistants and human developers MUST adhere unconditionally to these guidelines before writing or modifying any code.

1. **Do Not Break Working Functionality:** Always check existing contracts, endpoints, and database tables before altering signatures.
2. **Strict Layer Separation:**
   - `Controller` $\rightarrow$ Handles HTTP requests, validation, response wrapping. No business or DB logic.
   - `Service` $\rightarrow$ Executes business workflows, calculations, transaction boundaries (`@Transactional`).
   - `Repository` $\rightarrow$ Handles database queries via Spring Data JPA.
   - `Entity` $\rightarrow$ Pure JPA domain models with Hibernate annotations.
3. **No Hardcoded Secrets:** Never hardcode passwords, JWT private keys, or API tokens in Java files or client-side JavaScript. Use environment variables and `application.yml`.
4. **Clean DTO Boundaries:** Never expose raw JPA Entities directly in REST Controller response bodies. Always map to immutable or structured DTO records/classes.

---

## 2. Backend Coding Standards (Java 21 & Spring Boot 3.3.x)

```java
// ✅ CORRECT: Clean Controller with DTO, Bean Validation & Global Response
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance Management", description = "Endpoints for recording & querying attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    @PreAuthorize("hasAnyRole('FACULTY', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AttendanceRecordResponse>> markAttendance(
            @Valid @RequestBody MarkAttendanceRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {
        
        AttendanceRecordResponse response = attendanceService.recordAttendance(request, currentUser.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Attendance marked successfully", response));
    }
}
```

### 2.1. Backend Rules Checklist
- [x] Use Java 21 LTS features (Records for DTOs, Pattern Matching, Switch Expressions, `var` where clarity is preserved).
- [x] Use Lombok annotations (`@Data`, `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@Builder`) thoughtfully without breaking Hibernate proxy equality (`@EqualsAndHashCode(onlyExplicitlyIncluded = true)` on ID).
- [x] Every public REST API must return standardized `ApiResponse<T>` containing `success`, `message`, `data`, and `timestamp`.
- [x] Handle all exceptions via `@RestControllerAdvice` in `GlobalExceptionHandler.java` (no raw stack traces to the client).
- [x] Database queries involving multiple operations must be decorated with `@Transactional(rollbackFor = Exception.class)`.

---

## 3. Frontend Standards (Modular Vanilla JS & CSS)

### 3.1. JavaScript Rules
- **No Global Variable Pollution:** Wrap all page logic in modular IIFEs or ES6 modules (e.g. `const StudentPortal = (() => { ... })();`).
- **Unified API Client:** All network requests MUST go through `/js/api.js` which handles:
  - Automatic `Authorization: Bearer <token>` header injection.
  - Automatic 401 Unauthorized interception and redirect to `/login.html`.
  - JSON parsing and error modal/toast triggering.
- **XSS Prevention:** Never use `element.innerHTML = userInput` with unsanitized data. Use `element.textContent` or sanitize strings before rendering table cells.

```javascript
// ✅ CORRECT: Clean API Invocation via Centralized Client
async function loadStudentAttendance() {
  try {
    UiUtils.showLoadingSkeleton('#attendanceTableBody');
    const response = await ApiClient.get('/api/attendance/student/my-attendance');
    if (response.success) {
      renderAttendanceCards(response.data);
    }
  } catch (error) {
    UiUtils.showToast(error.message || 'Failed to load attendance records', 'danger');
  } finally {
    UiUtils.hideLoadingSkeleton('#attendanceTableBody');
  }
}
```

---

## 4. Database & Flyway Migration Conventions

- **Immutable Existing Migrations:** Never edit or delete an already committed Flyway migration script (e.g., `V1__...` through `V6__...`).
- **Sequential Versioning:** New schema changes must be added as `V7__<descriptive_name>.sql`, `V8__...`, etc.
- **All Foreign Keys Indexed:** Every `@ManyToOne` relationship or foreign key column must have a corresponding database index for high query performance.
- **Timestamp Tracking:** Every table must include `created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP` and `updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`.

---

## 5. Git Commit & Branching Conventions

Follow Conventional Commits standard:

```
feat:     Add BEU 5-mark attendance slab recalculator service
fix:      Prevent duplicate biometric scan increments in active session
refactor: Extract shortage calculation logic into dedicated rule evaluator
docs:     Update API documentation for biometric live SSE streaming
test:     Add unit tests for margin and consecutive class formulas
chore:    Bump Spring Boot dependency version to 3.3.4
```

---

## 6. AI Agent Interaction Guidelines

When instructing an AI to work on this repository:
1. **Scope Limit:** Tackle one feature or fix at a time (e.g. `TASK-003`).
2. **Context First:** Read `PRD.md`, `ARCHITECTURE.md`, `DESIGN.md`, and `RULES.md` before generating code.
3. **Validate:** Always verify compilation and tests before considering a task completed.
