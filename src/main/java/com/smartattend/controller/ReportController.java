package com.smartattend.controller;

import com.smartattend.dto.AcademicDtoModels.AuditLogDto;
import com.smartattend.dto.ApiResponse;
import com.smartattend.dto.ReportDtoModels.*;
import com.smartattend.service.AuditService;
import com.smartattend.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports & Analytics", description = "Endpoints for student-wise, subject-wise, admin dashboard analytics, CSV export, and audit logs")
public class ReportController {

    private final ReportService reportService;
    private final AuditService auditService;

    public ReportController(ReportService reportService, AuditService auditService) {
        this.reportService = reportService;
        this.auditService = auditService;
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Generate official BEU format student-wise attendance report")
    public ResponseEntity<ApiResponse<StudentAttendanceReportDto>> getStudentReport(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.ok(reportService.generateStudentReport(studentId)));
    }

    @GetMapping("/student/{studentId}/export-csv")
    @Operation(summary = "Export student attendance report to CSV")
    public ResponseEntity<byte[]> exportStudentReportCsv(@PathVariable Long studentId) {
        byte[] csvData = reportService.exportStudentReportCsv(studentId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_attendance_report_" + studentId + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }

    @GetMapping("/subject/{subjectId}")
    @Operation(summary = "Generate subject-wise attendance report for all enrolled students")
    public ResponseEntity<ApiResponse<SubjectAttendanceReportDto>> getSubjectReport(@PathVariable Long subjectId) {
        return ResponseEntity.ok(ApiResponse.ok(reportService.generateSubjectReport(subjectId)));
    }

    @GetMapping("/admin/stats")
    @Operation(summary = "Get overall college attendance statistics and Chart.js analytics")
    public ResponseEntity<ApiResponse<AdminDashboardDto>> getAdminStats() {
        return ResponseEntity.ok(ApiResponse.ok(reportService.getAdminDashboardStats()));
    }

    @GetMapping("/audit-logs")
    @Operation(summary = "Get system audit logs with pagination")
    public ResponseEntity<ApiResponse<Page<AuditLogDto>>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.ok(auditService.getAllLogs(pageable)));
    }
}
