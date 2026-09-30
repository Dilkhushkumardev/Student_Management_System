package com.smartattend.service;

import com.smartattend.dto.SubjectDtoModels.*;
import com.smartattend.entity.*;
import com.smartattend.exception.DuplicateResourceException;
import com.smartattend.exception.ResourceNotFoundException;
import com.smartattend.repository.DepartmentRepository;
import com.smartattend.repository.SemesterRepository;
import com.smartattend.repository.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private static final Logger log = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final SemesterRepository semesterRepository;
    private final AuditService auditService;

    public SubjectService(SubjectRepository subjectRepository,
                          DepartmentRepository departmentRepository,
                          SemesterRepository semesterRepository,
                          AuditService auditService) {
        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;
        this.semesterRepository = semesterRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<SubjectDto> getAllSubjects(String query, Long departmentId, Long semesterId, Boolean isActive, Pageable pageable) {
        return subjectRepository.searchSubjects(query, departmentId, semesterId, isActive, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<SubjectDto> getActiveSubjects() {
        return subjectRepository.findByIsActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectDto> getSubjectsBySemester(Long semesterId) {
        return subjectRepository.findBySemesterId(semesterId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubjectDto getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));
        return mapToDto(subject);
    }

    @Transactional
    public SubjectDto createSubject(SubjectCreateRequest request) {
        if (subjectRepository.existsByCourseCode(request.getCourseCode())) {
            throw new DuplicateResourceException("Subject", "Course Code", request.getCourseCode());
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found with ID: " + request.getSemesterId()));

        Subject subject = new Subject();
        subject.setCourseCode(request.getCourseCode());
        subject.setSubjectName(request.getSubjectName());
        subject.setSubjectType(request.getSubjectType());
        subject.setLectureHours(request.getLectureHours() != null ? request.getLectureHours() : 3);
        subject.setTutorialHours(request.getTutorialHours() != null ? request.getTutorialHours() : 0);
        subject.setPracticalHours(request.getPracticalHours() != null ? request.getPracticalHours() : 0);
        subject.setCredits(request.getCredits());
        subject.setMaxInternalMarks(request.getMaxInternalMarks() != null ? request.getMaxInternalMarks() : 30);
        subject.setMaxEseMarks(request.getMaxEseMarks() != null ? request.getMaxEseMarks() : 70);
        subject.setDepartment(department);
        subject.setSemester(semester);
        subject.setAcademicSession(request.getAcademicSession());
        subject.setAttendanceRequired(request.getAttendanceRequired() != null ? request.getAttendanceRequired() : true);
        subject.setIsActive(true);

        subject = subjectRepository.save(subject);

        auditService.log("CREATE_SUBJECT", "Subject", subject.getId().toString(), null, subject.getCourseCode(), null,
                "Subject created: " + subject.getSubjectName() + " (" + subject.getCourseCode() + ")");

        return mapToDto(subject);
    }

    @Transactional
    public SubjectDto updateSubject(Long id, SubjectUpdateRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));

        subject.setSubjectName(request.getSubjectName());
        if (request.getSubjectType() != null) subject.setSubjectType(request.getSubjectType());
        if (request.getLectureHours() != null) subject.setLectureHours(request.getLectureHours());
        if (request.getTutorialHours() != null) subject.setTutorialHours(request.getTutorialHours());
        if (request.getPracticalHours() != null) subject.setPracticalHours(request.getPracticalHours());
        if (request.getCredits() != null) subject.setCredits(request.getCredits());
        if (request.getMaxInternalMarks() != null) subject.setMaxInternalMarks(request.getMaxInternalMarks());
        if (request.getMaxEseMarks() != null) subject.setMaxEseMarks(request.getMaxEseMarks());
        if (request.getAttendanceRequired() != null) subject.setAttendanceRequired(request.getAttendanceRequired());
        if (request.getIsActive() != null) subject.setIsActive(request.getIsActive());

        subject = subjectRepository.save(subject);

        auditService.log("UPDATE_SUBJECT", "Subject", subject.getId().toString(), null, subject.getCourseCode(), null,
                "Subject updated: " + subject.getSubjectName());

        return mapToDto(subject);
    }

    public SubjectDto mapToDto(Subject s) {
        Long semId = s.getSemester() != null ? s.getSemester().getId() : null;
        String semName = s.getSemester() != null ? s.getSemester().getName() : "";
        Long dId = s.getDepartment() != null ? s.getDepartment().getId() : null;
        String dName = s.getDepartment() != null ? s.getDepartment().getName() : "";

        return SubjectDto.builder()
                .id(s.getId())
                .courseCode(s.getCourseCode())
                .subjectName(s.getSubjectName())
                .subjectType(s.getSubjectType())
                .lectureHours(s.getLectureHours())
                .tutorialHours(s.getTutorialHours())
                .practicalHours(s.getPracticalHours())
                .credits(s.getCredits())
                .maxInternalMarks(s.getMaxInternalMarks())
                .maxEseMarks(s.getMaxEseMarks())
                .semesterId(semId)
                .semesterName(semName)
                .departmentId(dId)
                .departmentName(dName)
                .academicSession(s.getAcademicSession())
                .attendanceRequired(s.getAttendanceRequired())
                .isActive(s.getIsActive())
                .build();
    }
}
