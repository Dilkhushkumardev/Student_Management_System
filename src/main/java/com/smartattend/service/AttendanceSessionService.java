package com.smartattend.service;

import com.smartattend.dto.AttendanceDtoModels.*;
import com.smartattend.entity.*;
import com.smartattend.enums.*;
import com.smartattend.exception.BadRequestException;
import com.smartattend.exception.ResourceNotFoundException;
import com.smartattend.repository.*;
import com.smartattend.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttendanceSessionService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceSessionService.class);

    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;
    private final AttendanceSummaryRepository summaryRepository;
    private final SubjectRepository subjectRepository;
    private final SectionRepository sectionRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final AttendanceCalculationService calculationService;
    private final AttendanceMarkService markService;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public AttendanceSessionService(AttendanceSessionRepository sessionRepository,
                                    AttendanceRecordRepository recordRepository,
                                    AttendanceSummaryRepository summaryRepository,
                                    SubjectRepository subjectRepository,
                                    SectionRepository sectionRepository,
                                    FacultyRepository facultyRepository,
                                    StudentRepository studentRepository,
                                    EnrollmentRepository enrollmentRepository,
                                    UserRepository userRepository,
                                    AttendanceCalculationService calculationService,
                                    AttendanceMarkService markService,
                                    NotificationService notificationService,
                                    AuditService auditService) {
        this.sessionRepository = sessionRepository;
        this.recordRepository = recordRepository;
        this.summaryRepository = summaryRepository;
        this.subjectRepository = subjectRepository;
        this.sectionRepository = sectionRepository;
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.calculationService = calculationService;
        this.markService = markService;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    @Transactional
    public AttendanceSessionDto createSession(AttendanceSessionCreateRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + request.getSubjectId()));

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with ID: " + request.getSectionId()));

        Long currentUserId = SecurityUtils.getCurrentUserId().orElse(1L);
        Faculty faculty = facultyRepository.findByUserId(currentUserId)
                .orElseGet(() -> facultyRepository.findById(1L).orElseThrow(() ->
                        new ResourceNotFoundException("No faculty profile found to assign attendance session")));

        String dateStr = request.getSessionDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sessionCode = "SESS-" + dateStr + "-" + subject.getCourseCode() + "-P" + (request.getPeriodNumber() != null ? request.getPeriodNumber() : 1) + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        AttendanceSession session = new AttendanceSession();
        session.setSessionCode(sessionCode);
        session.setSubject(subject);
        session.setFaculty(faculty);
        session.setSection(section);
        session.setSessionDate(request.getSessionDate());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setPeriodNumber(request.getPeriodNumber() != null ? request.getPeriodNumber() : 1);
        session.setRoomNo(request.getRoomNo() != null ? request.getRoomNo() : "Room 201");
        session.setStatus(SessionStatus.ACTIVE);
        session.setVerificationMode(request.getVerificationMode() != null ? request.getVerificationMode() : VerificationMode.MANUAL);
        session.setIsLocked(false);
        session.setNotes(request.getNotes());

        session = sessionRepository.save(session);

        List<Student> enrolledStudents = studentRepository.findBySectionIdOrderByRollNoAsc(section.getId());
        List<AttendanceRecord> initialRecords = new ArrayList<>();
        for (Student s : enrolledStudents) {
            AttendanceRecord rec = new AttendanceRecord();
            rec.setSession(session);
            rec.setStudent(s);
            rec.setStatus(AttendanceStatus.ABSENT);
            rec.setMethod(session.getVerificationMode() == VerificationMode.BIOMETRIC ? AttendanceMethod.BIOMETRIC : AttendanceMethod.MANUAL);
            rec.setMarkedAt(LocalDateTime.now());
            rec.setMarkedBy(faculty.getUser());
            rec.setRemarks("Initial session record");
            initialRecords.add(rec);
        }
        recordRepository.saveAll(initialRecords);

        auditService.log("CREATE_SESSION", "AttendanceSession", session.getId().toString(),
                null, session.getSessionCode(), null,
                "Attendance session created for " + subject.getSubjectName() + " (" + section.getName() + ")");

        return mapToDto(session);
    }

    @Transactional(readOnly = true)
    public List<AttendanceSessionDto> getActiveSessions(Long facultyId) {
        return sessionRepository.findActiveSessions(facultyId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AttendanceSessionDto getSessionById(Long id) {
        AttendanceSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with ID: " + id));
        return mapToDto(session);
    }

    @Transactional(readOnly = true)
    public List<AttendanceRecordDto> getSessionRecords(Long sessionId) {
        return recordRepository.findBySessionId(sessionId).stream()
                .map(this::mapToRecordDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AttendanceRecordDto updateRecord(Long recordId, AttendanceRecordUpdateRequest request) {
        AttendanceRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + recordId));

        if (Boolean.TRUE.equals(record.getSession().getIsLocked())) {
            throw new BadRequestException("Session is locked. Changes require Admin Override with documented reason.", "SESSION_LOCKED");
        }

        AttendanceStatus oldStatus = record.getStatus();
        record.setStatus(request.getStatus());
        record.setRemarks(request.getRemarks());
        record.setMarkedAt(LocalDateTime.now());
        record = recordRepository.save(record);

        auditService.log("UPDATE_RECORD", "AttendanceRecord", record.getId().toString(),
                oldStatus.name(), request.getStatus().name(), null,
                "Manual attendance adjusted for " + record.getStudent().getName() + " in session " + record.getSession().getSessionCode());

        return mapToRecordDto(record);
    }

    @Transactional
    public void markBulkAttendance(BulkAttendanceMarkRequest request) {
        AttendanceSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with ID: " + request.getSessionId()));

        if (Boolean.TRUE.equals(session.getIsLocked())) {
            throw new BadRequestException("Cannot update locked attendance session", "SESSION_LOCKED");
        }

        if (request.getRecords() != null) {
            for (StudentAttendanceItem item : request.getRecords()) {
                Optional<AttendanceRecord> optRecord = recordRepository.findBySessionIdAndStudentId(session.getId(), item.getStudentId());
                if (optRecord.isPresent()) {
                    AttendanceRecord r = optRecord.get();
                    r.setStatus(item.getStatus() != null ? item.getStatus() : AttendanceStatus.PRESENT);
                    r.setMethod(item.getMethod() != null ? item.getMethod() : AttendanceMethod.MANUAL);
                    r.setRemarks(item.getRemarks());
                    r.setMarkedAt(LocalDateTime.now());
                    recordRepository.save(r);
                } else {
                    studentRepository.findById(item.getStudentId()).ifPresent(s -> {
                        AttendanceRecord r = new AttendanceRecord();
                        r.setSession(session);
                        r.setStudent(s);
                        r.setStatus(item.getStatus() != null ? item.getStatus() : AttendanceStatus.PRESENT);
                        r.setMethod(item.getMethod() != null ? item.getMethod() : AttendanceMethod.MANUAL);
                        r.setRemarks(item.getRemarks());
                        r.setMarkedAt(LocalDateTime.now());
                        recordRepository.save(r);
                    });
                }
            }
        }
    }

    @Transactional
    public AttendanceSessionDto lockSession(Long sessionId) {
        AttendanceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with ID: " + sessionId));

        if (Boolean.TRUE.equals(session.getIsLocked())) {
            return mapToDto(session);
        }

        Long currentUserId = SecurityUtils.getCurrentUserId().orElse(1L);
        User currentUser = userRepository.findById(currentUserId).orElse(null);

        session.setIsLocked(true);
        session.setStatus(SessionStatus.LOCKED);
        session.setLockedAt(LocalDateTime.now());
        session.setLockedBy(currentUser);
        session = sessionRepository.save(session);

        // Transactionally calculate and update AttendanceSummary for all students in that Subject & Semester
        updateAttendanceSummariesForSubject(session.getSubject().getId(), session.getSubject().getSemester().getId());

        auditService.log("LOCK_SESSION", "AttendanceSession", session.getId().toString(),
                "ACTIVE", "LOCKED", null,
                "Attendance session " + session.getSessionCode() + " locked and summaries calculated.");

        return mapToDto(session);
    }

    @Transactional
    public AttendanceRecordDto adminOverride(Long recordId, AdminOverrideRequest request) {
        AttendanceRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + recordId));

        Long currentUserId = SecurityUtils.getCurrentUserId().orElse(1L);
        User currentUser = userRepository.findById(currentUserId).orElse(null);

        AttendanceStatus oldStatus = record.getStatus();
        record.setStatus(request.getStatus());
        record.setIsAdminOverride(true);
        record.setOverrideReason(request.getOverrideReason());
        record.setOverrideBy(currentUser);
        record.setOverrideAt(LocalDateTime.now());
        record.setRemarks(request.getRemarks() != null ? request.getRemarks() : "Admin override: " + request.getOverrideReason());
        record = recordRepository.save(record);

        updateAttendanceSummariesForSubject(record.getSession().getSubject().getId(), record.getSession().getSubject().getSemester().getId());

        auditService.log("ADMIN_OVERRIDE", "AttendanceRecord", record.getId().toString(),
                oldStatus.name(), request.getStatus().name(), null,
                "Admin override reason: " + request.getOverrideReason());

        return mapToRecordDto(record);
    }

    @Transactional
    public void updateAttendanceSummariesForSubject(Long subjectId, Long semesterId) {
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null) return;

        List<Student> students = studentRepository.findAll();

        for (Student student : students) {
            List<AttendanceRecord> records = recordRepository.findByStudentIdAndSubjectId(student.getId(), subjectId);
            if (records.isEmpty()) continue;

            int total = records.size();
            int present = 0;
            int absent = 0;
            int late = 0;
            int excused = 0;

            for (AttendanceRecord r : records) {
                if (r.getStatus() == AttendanceStatus.PRESENT) present++;
                else if (r.getStatus() == AttendanceStatus.ABSENT) absent++;
                else if (r.getStatus() == AttendanceStatus.LATE) late++;
                else if (r.getStatus() == AttendanceStatus.EXCUSED) excused++;
            }

            BigDecimal percentage = calculationService.calculatePercentage(present, late, excused, total, BigDecimal.ONE);
            int marks = markService.calculateMarks(percentage);
            EligibilityStatus eligibility = calculationService.determineEligibility(percentage);

            AttendanceSummary summary = summaryRepository.findByStudentIdAndSubjectIdAndSemesterId(student.getId(), subjectId, semesterId)
                    .orElseGet(() -> {
                        AttendanceSummary s = new AttendanceSummary();
                        s.setStudent(student);
                        s.setSubject(subject);
                        s.setSemester(subject.getSemester());
                        return s;
                    });

            summary.setTotalClasses(total);
            summary.setPresentClasses(present);
            summary.setAbsentClasses(absent);
            summary.setLateClasses(late);
            summary.setExcusedClasses(excused);
            summary.setAttendancePercentage(percentage);
            summary.setAttendanceMarks(marks);
            summary.setEligibility(eligibility);
            summary.setLastCalculatedAt(LocalDateTime.now());
            summaryRepository.save(summary);

            if (eligibility == EligibilityStatus.SHORTAGE || eligibility == EligibilityStatus.CONDONABLE_MEDICAL) {
                if (student.getUser() != null) {
                    notificationService.sendNotification(
                            student.getUser().getId(),
                            "Attendance Shortage Warning - " + subject.getCourseCode(),
                            String.format("Your attendance in %s is %s%% (%d/%d classes). Minimum 75%% is required under BEU Regulations.",
                                    subject.getSubjectName(), percentage.toPlainString(), present + late, total),
                            NotificationType.SHORTAGE_WARNING,
                            subject.getId().toString()
                    );
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<AttendanceSessionDto> searchSessions(Long facultyId, Long subjectId, Long sectionId,
                                                     LocalDate startDate, LocalDate endDate,
                                                     SessionStatus status, Pageable pageable) {
        return sessionRepository.searchSessions(facultyId, subjectId, sectionId, startDate, endDate, status, pageable)
                .map(this::mapToDto);
    }

    private AttendanceSessionDto mapToDto(AttendanceSession s) {
        List<AttendanceRecord> records = recordRepository.findBySessionId(s.getId());
        int present = 0;
        int absent = 0;
        int late = 0;
        int excused = 0;

        for (AttendanceRecord r : records) {
            if (r.getStatus() == AttendanceStatus.PRESENT) present++;
            else if (r.getStatus() == AttendanceStatus.ABSENT) absent++;
            else if (r.getStatus() == AttendanceStatus.LATE) late++;
            else if (r.getStatus() == AttendanceStatus.EXCUSED) excused++;
        }

        int total = records.size();
        BigDecimal percentage = BigDecimal.ZERO;
        if (total > 0) {
            percentage = BigDecimal.valueOf(present + late)
                    .multiply(BigDecimal.valueOf(100.0))
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        }

        String subName = s.getSubject() != null ? s.getSubject().getSubjectName() : "Course";
        String courseCode = s.getSubject() != null ? s.getSubject().getCourseCode() : "N/A";
        Long subId = s.getSubject() != null ? s.getSubject().getId() : null;

        String facName = s.getFaculty() != null ? s.getFaculty().getName() : "Faculty";
        Long facId = s.getFaculty() != null ? s.getFaculty().getId() : null;

        String secName = s.getSection() != null ? s.getSection().getName() : "Section";
        Long secId = s.getSection() != null ? s.getSection().getId() : null;

        return AttendanceSessionDto.builder()
                .id(s.getId())
                .sessionCode(s.getSessionCode())
                .subjectId(subId)
                .courseCode(courseCode)
                .subjectName(subName)
                .facultyId(facId)
                .facultyName(facName)
                .sectionId(secId)
                .sectionName(secName)
                .sessionDate(s.getSessionDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .periodNumber(s.getPeriodNumber())
                .roomNo(s.getRoomNo())
                .status(s.getStatus())
                .verificationMode(s.getVerificationMode())
                .isLocked(s.getIsLocked())
                .lockedAt(s.getLockedAt())
                .notes(s.getNotes())
                .totalStudents(total)
                .presentCount(present)
                .absentCount(absent)
                .lateCount(late)
                .excusedCount(excused)
                .attendancePercentage(percentage)
                .build();
    }

    private AttendanceRecordDto mapToRecordDto(AttendanceRecord r) {
        Long sessId = r.getSession() != null ? r.getSession().getId() : null;
        Long stId = r.getStudent() != null ? r.getStudent().getId() : null;
        String roll = r.getStudent() != null ? r.getStudent().getRollNo() : "N/A";
        String name = r.getStudent() != null ? r.getStudent().getName() : "N/A";
        String reg = r.getStudent() != null ? r.getStudent().getUniversityRegNo() : "N/A";
        String bio = r.getStudent() != null ? r.getStudent().getBiometricId() : "N/A";

        return AttendanceRecordDto.builder()
                .id(r.getId())
                .sessionId(sessId)
                .studentId(stId)
                .rollNo(roll)
                .studentName(name)
                .universityRegNo(reg)
                .biometricId(bio)
                .status(r.getStatus())
                .method(r.getMethod())
                .biometricEventId(r.getBiometricEvent() != null ? r.getBiometricEvent().getId() : null)
                .remarks(r.getRemarks())
                .markedAt(r.getMarkedAt())
                .isAdminOverride(r.getIsAdminOverride())
                .overrideReason(r.getOverrideReason())
                .build();
    }
}
