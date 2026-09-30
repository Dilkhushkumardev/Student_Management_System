package com.smartattend.controller;

import com.smartattend.dto.ApiResponse;
import com.smartattend.dto.FacultyDtoModels.*;
import com.smartattend.dto.ReportDtoModels.FacultyDashboardDto;
import com.smartattend.service.FacultyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculty")
@Tag(name = "Faculty", description = "Endpoints for faculty management, subject assignments, and faculty dashboard")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    @Operation(summary = "Search and filter faculty with pagination")
    public ResponseEntity<ApiResponse<Page<FacultyDto>>> getAllFaculty(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<FacultyDto> facultyList = facultyService.getAllFaculty(query, departmentId, status, pageable);
        return ResponseEntity.ok(ApiResponse.ok(facultyList));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get faculty by ID")
    public ResponseEntity<ApiResponse<FacultyDto>> getFacultyById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(facultyService.getFacultyById(id)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get faculty by User ID")
    public ResponseEntity<ApiResponse<FacultyDto>> getFacultyByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(facultyService.getFacultyByUserId(userId)));
    }

    @PostMapping
    @Operation(summary = "Add new faculty member")
    public ResponseEntity<ApiResponse<FacultyDto>> createFaculty(@Valid @RequestBody FacultyCreateRequest request) {
        FacultyDto created = facultyService.createFaculty(request);
        return ResponseEntity.ok(ApiResponse.created("Faculty registered successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing faculty")
    public ResponseEntity<ApiResponse<FacultyDto>> updateFaculty(@PathVariable Long id, @Valid @RequestBody FacultyUpdateRequest request) {
        FacultyDto updated = facultyService.updateFaculty(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Faculty updated successfully", updated));
    }

    @GetMapping("/{id}/dashboard")
    @Operation(summary = "Get faculty dashboard with today's classes and shortage lists")
    public ResponseEntity<ApiResponse<FacultyDashboardDto>> getFacultyDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(facultyService.getFacultyDashboard(id)));
    }
}
