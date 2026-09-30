package com.smartattend.controller;

import com.smartattend.dto.AcademicDtoModels.*;
import com.smartattend.dto.ApiResponse;
import com.smartattend.dto.TimetableDtoModels.*;
import com.smartattend.entity.*;
import com.smartattend.service.AcademicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic")
@Tag(name = "Academic & Configuration", description = "Endpoints for academic departments, branches, semesters, sections, timetable, BEU holiday calendar, and attendance rules")
public class AcademicController {

    private final AcademicService academicService;

    public AcademicController(AcademicService academicService) {
        this.academicService = academicService;
    }

    @GetMapping("/departments")
    @Operation(summary = "List all departments")
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
        return ResponseEntity.ok(ApiResponse.ok(academicService.getAllDepartments()));
    }

    @PostMapping("/departments")
    @Operation(summary = "Create a new department")
    public ResponseEntity<ApiResponse<Department>> createDepartment(@Valid @RequestBody Department department) {
        return ResponseEntity.ok(ApiResponse.created("Department created", academicService.createDepartment(department)));
    }

    @GetMapping("/branches")
    @Operation(summary = "List all branches")
    public ResponseEntity<ApiResponse<List<Branch>>> getAllBranches() {
        return ResponseEntity.ok(ApiResponse.ok(academicService.getAllBranches()));
    }

    @GetMapping("/batches")
    @Operation(summary = "List all batches")
    public ResponseEntity<ApiResponse<List<Batch>>> getAllBatches() {
        return ResponseEntity.ok(ApiResponse.ok(academicService.getAllBatches()));
    }

    @PostMapping("/batches")
    @Operation(summary = "Create a new batch")
    public ResponseEntity<ApiResponse<Batch>> createBatch(@Valid @RequestBody Batch batch) {
        return ResponseEntity.ok(ApiResponse.created("Batch created", academicService.createBatch(batch)));
    }

    @GetMapping("/semesters")
    @Operation(summary = "List all semesters")
    public ResponseEntity<ApiResponse<List<Semester>>> getAllSemesters() {
        return ResponseEntity.ok(ApiResponse.ok(academicService.getAllSemesters()));
    }

    @GetMapping("/sections")
    @Operation(summary = "List all sections")
    public ResponseEntity<ApiResponse<List<Section>>> getAllSections() {
        return ResponseEntity.ok(ApiResponse.ok(academicService.getAllSections()));
    }

    @GetMapping("/timetable/section/{sectionId}")
    @Operation(summary = "Get timetable for a section")
    public ResponseEntity<ApiResponse<List<TimetableDto>>> getTimetableBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(ApiResponse.ok(academicService.getTimetableBySection(sectionId)));
    }

    @GetMapping("/timetable/faculty/{facultyId}")
    @Operation(summary = "Get timetable for a faculty member")
    public ResponseEntity<ApiResponse<List<TimetableDto>>> getTimetableByFaculty(@PathVariable Long facultyId) {
        return ResponseEntity.ok(ApiResponse.ok(academicService.getTimetableByFaculty(facultyId)));
    }

    @PostMapping("/timetable")
    @Operation(summary = "Create a new timetable entry")
    public ResponseEntity<ApiResponse<TimetableDto>> createTimetableEntry(@Valid @RequestBody TimetableCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.created("Timetable slot created", academicService.createTimetableEntry(request)));
    }

    @GetMapping("/calendar")
    @Operation(summary = "Get official university academic calendar and holiday list")
    public ResponseEntity<ApiResponse<List<AcademicCalendarDto>>> getAcademicCalendar(@RequestParam(required = false) String session) {
        return ResponseEntity.ok(ApiResponse.ok(academicService.getCalendarEvents(session)));
    }

    @PostMapping("/calendar")
    @Operation(summary = "Add an event or holiday to the academic calendar")
    public ResponseEntity<ApiResponse<AcademicCalendarDto>> createCalendarEvent(@Valid @RequestBody AcademicCalendarDto dto) {
        return ResponseEntity.ok(ApiResponse.created("Calendar event created", academicService.createCalendarEvent(dto)));
    }

    @GetMapping("/rules/attendance")
    @Operation(summary = "Get active attendance threshold rule (75% rule & medical condonation)")
    public ResponseEntity<ApiResponse<AttendanceRuleDto>> getAttendanceRule() {
        return ResponseEntity.ok(ApiResponse.ok(academicService.getActiveAttendanceRule()));
    }

    @PutMapping("/rules/attendance")
    @Operation(summary = "Update attendance threshold rule")
    public ResponseEntity<ApiResponse<AttendanceRuleDto>> updateAttendanceRule(@Valid @RequestBody AttendanceRuleDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Attendance rule updated", academicService.updateAttendanceRule(dto)));
    }

    @GetMapping("/rules/marks")
    @Operation(summary = "Get attendance mark calculation slab rules (BEU 5-mark slab)")
    public ResponseEntity<ApiResponse<List<AttendanceMarkRuleDto>>> getAttendanceMarkRules() {
        return ResponseEntity.ok(ApiResponse.ok(academicService.getAttendanceMarkRules()));
    }
}
