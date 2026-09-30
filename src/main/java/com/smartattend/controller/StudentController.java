package com.smartattend.controller;

import com.smartattend.dto.ApiResponse;
import com.smartattend.dto.ReportDtoModels.StudentDashboardDto;
import com.smartattend.dto.StudentDtoModels.*;
import com.smartattend.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Endpoints for managing students, CSV import/export, and student dashboards")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @Operation(summary = "Search and filter students with pagination")
    public ResponseEntity<ApiResponse<Page<StudentDto>>> getAllStudents(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "rollNo") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<StudentDto> students = studentService.getAllStudents(query, departmentId, semesterId, sectionId, status, pageable);
        return ResponseEntity.ok(ApiResponse.ok(students));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<ApiResponse<StudentDto>> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getStudentById(id)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get student by User ID")
    public ResponseEntity<ApiResponse<StudentDto>> getStudentByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getStudentByUserId(userId)));
    }

    @PostMapping
    @Operation(summary = "Add a new student")
    public ResponseEntity<ApiResponse<StudentDto>> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        StudentDto created = studentService.createStudent(request);
        return ResponseEntity.ok(ApiResponse.created("Student registered successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing student")
    public ResponseEntity<ApiResponse<StudentDto>> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentUpdateRequest request) {
        StudentDto updated = studentService.updateStudent(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Student updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate student")
    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.ok("Student deactivated successfully", "ID: " + id));
    }

    @PostMapping(value = "/import-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import students in bulk via CSV file")
    public ResponseEntity<ApiResponse<Map<String, Object>>> importStudentsCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long batchId,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Long sectionId) {

        Map<String, Object> result = studentService.importStudentsFromCsv(file, departmentId, branchId, batchId, semesterId, sectionId);
        return ResponseEntity.ok(ApiResponse.ok("CSV import completed", result));
    }

    @GetMapping("/export-csv")
    @Operation(summary = "Export student list to CSV")
    public ResponseEntity<byte[]> exportStudentsCsv(@RequestParam(required = false) Long sectionId) {
        byte[] csvBytes = studentService.exportStudentsCsv(sectionId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students_export.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }

    @GetMapping("/{id}/dashboard")
    @Operation(summary = "Get full student dashboard with subject cards and shortage advice")
    public ResponseEntity<ApiResponse<StudentDashboardDto>> getStudentDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getStudentDashboard(id)));
    }

    @GetMapping("/shortage")
    @Operation(summary = "Get list of all students currently below 75% attendance")
    public ResponseEntity<ApiResponse<List<StudentShortageDto>>> getShortageStudents() {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getShortageStudents()));
    }
}
