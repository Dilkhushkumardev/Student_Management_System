package com.smartattend.controller;

import com.smartattend.dto.ApiResponse;
import com.smartattend.dto.SubjectDtoModels.*;
import com.smartattend.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@Tag(name = "Subjects", description = "Endpoints for managing subjects, syllabus metadata, credits, and contact hours")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    @Operation(summary = "Search and filter subjects with pagination")
    public ResponseEntity<ApiResponse<Page<SubjectDto>>> getAllSubjects(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long semesterId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "courseCode") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SubjectDto> subjects = subjectService.getAllSubjects(query, departmentId, semesterId, isActive, pageable);
        return ResponseEntity.ok(ApiResponse.ok(subjects));
    }

    @GetMapping("/active")
    @Operation(summary = "List all active subjects")
    public ResponseEntity<ApiResponse<List<SubjectDto>>> getActiveSubjects() {
        return ResponseEntity.ok(ApiResponse.ok(subjectService.getActiveSubjects()));
    }

    @GetMapping("/semester/{semesterId}")
    @Operation(summary = "List subjects for a particular semester")
    public ResponseEntity<ApiResponse<List<SubjectDto>>> getSubjectsBySemester(@PathVariable Long semesterId) {
        return ResponseEntity.ok(ApiResponse.ok(subjectService.getSubjectsBySemester(semesterId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID")
    public ResponseEntity<ApiResponse<SubjectDto>> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(subjectService.getSubjectById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new subject")
    public ResponseEntity<ApiResponse<SubjectDto>> createSubject(@Valid @RequestBody SubjectCreateRequest request) {
        SubjectDto created = subjectService.createSubject(request);
        return ResponseEntity.ok(ApiResponse.created("Subject created successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing subject")
    public ResponseEntity<ApiResponse<SubjectDto>> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectUpdateRequest request) {
        SubjectDto updated = subjectService.updateSubject(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Subject updated successfully", updated));
    }
}
