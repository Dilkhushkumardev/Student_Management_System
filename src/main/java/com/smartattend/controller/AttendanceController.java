package com.smartattend.controller;

import com.smartattend.dto.ApiResponse;
import com.smartattend.dto.AttendanceDtoModels.*;
import com.smartattend.enums.SessionStatus;
import com.smartattend.service.AttendanceSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@Tag(name = "Attendance", description = "Endpoints for creating sessions, manual recording, session locking, and admin overrides")
public class AttendanceController {

    private final AttendanceSessionService sessionService;

    public AttendanceController(AttendanceSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/sessions")
    @Operation(summary = "Create and start a new attendance session")
    public ResponseEntity<ApiResponse<AttendanceSessionDto>> createSession(@Valid @RequestBody AttendanceSessionCreateRequest request) {
        AttendanceSessionDto created = sessionService.createSession(request);
        return ResponseEntity.ok(ApiResponse.created("Attendance session created successfully", created));
    }

    @GetMapping("/sessions")
    @Operation(summary = "Search attendance sessions with filters and pagination")
    public ResponseEntity<ApiResponse<Page<AttendanceSessionDto>>> searchSessions(
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) SessionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "sessionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AttendanceSessionDto> sessions = sessionService.searchSessions(facultyId, subjectId, sectionId, startDate, endDate, status, pageable);
        return ResponseEntity.ok(ApiResponse.ok(sessions));
    }

    @GetMapping("/sessions/active")
    @Operation(summary = "Get currently active attendance sessions for a faculty member")
    public ResponseEntity<ApiResponse<List<AttendanceSessionDto>>> getActiveSessions(@RequestParam(required = false) Long facultyId) {
        return ResponseEntity.ok(ApiResponse.ok(sessionService.getActiveSessions(facultyId)));
    }

    @GetMapping("/sessions/{id}")
    @Operation(summary = "Get attendance session details by ID")
    public ResponseEntity<ApiResponse<AttendanceSessionDto>> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(sessionService.getSessionById(id)));
    }

    @GetMapping("/sessions/{id}/records")
    @Operation(summary = "Get all student attendance records for a session")
    public ResponseEntity<ApiResponse<List<AttendanceRecordDto>>> getSessionRecords(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(sessionService.getSessionRecords(id)));
    }

    @PutMapping("/records/{id}")
    @Operation(summary = "Manually update single student attendance record before session locking")
    public ResponseEntity<ApiResponse<AttendanceRecordDto>> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceRecordUpdateRequest request) {
        AttendanceRecordDto updated = sessionService.updateRecord(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Attendance record updated", updated));
    }

    @PostMapping("/records/bulk")
    @Operation(summary = "Mark bulk manual attendance for a session")
    public ResponseEntity<ApiResponse<String>> markBulkAttendance(@Valid @RequestBody BulkAttendanceMarkRequest request) {
        sessionService.markBulkAttendance(request);
        return ResponseEntity.ok(ApiResponse.ok("Bulk attendance submitted successfully", "Session ID: " + request.getSessionId()));
    }

    @PostMapping("/sessions/{id}/lock")
    @Operation(summary = "Lock attendance session and update summary calculations for students")
    public ResponseEntity<ApiResponse<AttendanceSessionDto>> lockSession(@PathVariable Long id) {
        AttendanceSessionDto locked = sessionService.lockSession(id);
        return ResponseEntity.ok(ApiResponse.ok("Attendance session locked successfully. Summary calculations updated.", locked));
    }

    @PostMapping("/override/{recordId}")
    @Operation(summary = "Admin override for attendance modification after session has been locked")
    public ResponseEntity<ApiResponse<AttendanceRecordDto>> adminOverride(
            @PathVariable Long recordId,
            @Valid @RequestBody AdminOverrideRequest request) {
        AttendanceRecordDto overridden = sessionService.adminOverride(recordId, request);
        return ResponseEntity.ok(ApiResponse.ok("Admin override processed and logged to audit trail", overridden));
    }
}
