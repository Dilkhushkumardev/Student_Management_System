package com.smartattend.service;

import com.smartattend.dto.AttendanceDtoModels.*;
import com.smartattend.entity.*;
import com.smartattend.enums.AttendanceStatus;
import com.smartattend.enums.EligibilityStatus;
import com.smartattend.enums.SessionStatus;
import com.smartattend.exception.BadRequestException;
import com.smartattend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceSessionServiceTest {

    @Mock
    private AttendanceSessionRepository sessionRepository;
    @Mock
    private AttendanceRecordRepository recordRepository;
    @Mock
    private AttendanceSummaryRepository summaryRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private FacultyRepository facultyRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AttendanceCalculationService calculationService;
    @Mock
    private AttendanceMarkService markService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private AuditService auditService;

    private AttendanceSessionService sessionService;

    private AttendanceSession session;
    private Subject subject;
    private Faculty faculty;
    private Section section;
    private Semester semester;
    private Student student;

    @BeforeEach
    void setUp() {
        sessionService = new AttendanceSessionService(
                sessionRepository,
                recordRepository,
                summaryRepository,
                subjectRepository,
                sectionRepository,
                facultyRepository,
                studentRepository,
                enrollmentRepository,
                userRepository,
                calculationService,
                markService,
                notificationService,
                auditService
        );

        semester = new Semester();
        semester.setId(1L);
        semester.setSemesterNumber(1);

        subject = new Subject();
        subject.setId(10L);
        subject.setCourseCode("100102");
        subject.setSubjectName("Engineering Mathematics - I");
        subject.setSemester(semester);

        faculty = new Faculty();
        faculty.setId(20L);
        faculty.setName("Dr. Rajesh Kumar");

        section = new Section();
        section.setId(30L);
        section.setName("Section A");

        student = new Student();
        student.setId(100L);
        student.setName("Rahul Kumar");
        student.setRollNo("26105110001");

        session = new AttendanceSession();
        session.setId(1L);
        session.setSessionCode("ATT-20260929-100102-01");
        session.setSubject(subject);
        session.setFaculty(faculty);
        session.setSection(section);
        session.setSessionDate(LocalDate.now());
        session.setStartTime(LocalTime.of(10, 0));
        session.setEndTime(LocalTime.of(11, 0));
        session.setPeriodNumber(1);
        session.setStatus(SessionStatus.ACTIVE);
        session.setIsLocked(false);
    }

    @Test
    @DisplayName("Lock Session: Successfully locks session, generates summaries, and writes audit log")
    void testLockSession_Success() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(AttendanceSession.class))).thenReturn(session);
        when(subjectRepository.findById(10L)).thenReturn(Optional.of(subject));
        when(studentRepository.findAll()).thenReturn(Collections.singletonList(student));

        AttendanceRecord record = new AttendanceRecord();
        record.setId(500L);
        record.setSession(session);
        record.setStudent(student);
        record.setStatus(AttendanceStatus.PRESENT);

        when(recordRepository.findByStudentIdAndSubjectId(100L, 10L)).thenReturn(Collections.singletonList(record));
        when(calculationService.calculatePercentage(1, 0, 0, 1, BigDecimal.ONE)).thenReturn(new BigDecimal("100.00"));
        when(markService.calculateMarks(new BigDecimal("100.00"))).thenReturn(5);
        when(calculationService.determineEligibility(new BigDecimal("100.00"))).thenReturn(EligibilityStatus.ELIGIBLE);
        when(summaryRepository.findByStudentIdAndSubjectIdAndSemesterId(100L, 10L, 1L)).thenReturn(Optional.empty());

        AttendanceSessionDto result = sessionService.lockSession(1L);

        assertNotNull(result);
        assertEquals(SessionStatus.LOCKED, session.getStatus());
        assertTrue(session.getIsLocked());

        verify(summaryRepository, times(1)).save(any(AttendanceSummary.class));
        verify(auditService, times(1)).log(eq("LOCK_SESSION"), eq("AttendanceSession"), eq("1"), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Update Record: Throws BadRequestException if session is locked")
    void testUpdateRecord_LockedSessionThrowsException() {
        session.setIsLocked(true);
        AttendanceRecord record = new AttendanceRecord();
        record.setId(500L);
        record.setSession(session);
        record.setStatus(AttendanceStatus.ABSENT);

        when(recordRepository.findById(500L)).thenReturn(Optional.of(record));

        AttendanceRecordUpdateRequest updateRequest = new AttendanceRecordUpdateRequest();
        updateRequest.setStatus(AttendanceStatus.PRESENT);

        assertThrows(BadRequestException.class, () -> sessionService.updateRecord(500L, updateRequest));
    }
}
