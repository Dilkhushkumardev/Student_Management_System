package com.smartattend.service;

import com.smartattend.biometric.*;
import com.smartattend.dto.BiometricDtoModels.BiometricEventRequest;
import com.smartattend.dto.BiometricDtoModels.BiometricEventResponse;
import com.smartattend.entity.*;
import com.smartattend.enums.AttendanceMethod;
import com.smartattend.enums.AttendanceStatus;
import com.smartattend.enums.BiometricResult;
import com.smartattend.enums.DeviceStatus;
import com.smartattend.enums.DeviceType;
import com.smartattend.enums.SessionStatus;
import com.smartattend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BiometricServiceTest {

    @Mock
    private BiometricDeviceRepository deviceRepository;
    @Mock
    private BiometricUserRepository biometricUserRepository;
    @Mock
    private BiometricEventRepository biometricEventRepository;
    @Mock
    private AttendanceSessionRepository sessionRepository;
    @Mock
    private AttendanceRecordRepository recordRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private BiometricEventPublisher eventPublisher;
    @Mock
    private AuditService auditService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private DemoBiometricProvider demoBiometricProvider;
    @Mock
    private FingerprintBiometricProvider fingerprintBiometricProvider;
    @Mock
    private FaceBiometricProvider faceBiometricProvider;
    @Mock
    private RFIDBiometricProvider rfidBiometricProvider;

    private BiometricService biometricService;

    private BiometricDevice device;
    private Student student;
    private AttendanceSession session;
    private Section section;

    @BeforeEach
    void setUp() {
        biometricService = new BiometricService(
                deviceRepository,
                biometricUserRepository,
                biometricEventRepository,
                sessionRepository,
                recordRepository,
                studentRepository,
                enrollmentRepository,
                eventPublisher,
                auditService,
                notificationService,
                demoBiometricProvider,
                fingerprintBiometricProvider,
                faceBiometricProvider,
                rfidBiometricProvider
        );

        device = new BiometricDevice();
        device.setId(1L);
        device.setDeviceCode("BIO-CSE-001");
        device.setDeviceName("Lab 1 Biometric Terminal");
        device.setDeviceType(DeviceType.FINGERPRINT);
        device.setStatus(DeviceStatus.ONLINE);

        section = new Section();
        section.setId(1L);
        section.setName("Section A");

        student = new Student();
        student.setId(101L);
        student.setRollNo("26105110001");
        student.setName("Rahul Kumar");
        student.setBiometricId("BIO-STU-001");
        student.setSection(section);

        session = new AttendanceSession();
        session.setId(501L);
        session.setSessionCode("ATT-20260929-100102-01");
        session.setStatus(SessionStatus.ACTIVE);
        session.setSection(section);
    }

    @Test
    @DisplayName("Duplicate Protection: Repeated scan within active session returns DUPLICATE result")
    void testProcessBiometricEvent_DuplicateScanPrevented() {
        BiometricEventRequest request = BiometricEventRequest.builder()
                .deviceIdentifier("BIO-CSE-001")
                .biometricUserId("BIO-STU-001")
                .verificationType("FINGERPRINT")
                .targetSessionId(501L)
                .build();

        when(deviceRepository.findByDeviceCode("BIO-CSE-001")).thenReturn(Optional.of(device));
        when(studentRepository.findByBiometricId("BIO-STU-001")).thenReturn(Optional.of(student));
        when(sessionRepository.findById(501L)).thenReturn(Optional.of(session));

        AttendanceRecord existingRecord = new AttendanceRecord();
        existingRecord.setId(901L);
        existingRecord.setStudent(student);
        existingRecord.setSession(session);
        existingRecord.setStatus(AttendanceStatus.PRESENT);

        when(recordRepository.findBySessionIdAndStudentId(501L, 101L)).thenReturn(Optional.of(existingRecord));

        BiometricEventResponse response = biometricService.processBiometricEvent(request);

        assertNotNull(response);
        assertEquals(BiometricResult.DUPLICATE, response.getResult());
        assertTrue(response.getMessage().contains("already recorded"));

        // Verify save on recordRepository was NEVER called for duplicate scan
        verify(recordRepository, never()).save(any(AttendanceRecord.class));
    }

    @Test
    @DisplayName("First-time scan records biometric attendance and marks student PRESENT")
    void testProcessBiometricEvent_NewScanRecorded() {
        BiometricEventRequest request = BiometricEventRequest.builder()
                .deviceIdentifier("BIO-CSE-001")
                .biometricUserId("BIO-STU-001")
                .verificationType("FINGERPRINT")
                .targetSessionId(501L)
                .build();

        when(deviceRepository.findByDeviceCode("BIO-CSE-001")).thenReturn(Optional.of(device));
        when(studentRepository.findByBiometricId("BIO-STU-001")).thenReturn(Optional.of(student));
        when(sessionRepository.findById(501L)).thenReturn(Optional.of(session));
        when(recordRepository.findBySessionIdAndStudentId(501L, 101L)).thenReturn(Optional.empty());

        BiometricEvent savedEvent = new BiometricEvent();
        savedEvent.setId(777L);
        savedEvent.setVerificationType("FINGERPRINT");
        when(biometricEventRepository.save(any(BiometricEvent.class))).thenReturn(savedEvent);

        when(recordRepository.save(any(AttendanceRecord.class))).thenAnswer(invocation -> {
            AttendanceRecord r = invocation.getArgument(0);
            r.setId(888L);
            return r;
        });

        BiometricEventResponse response = biometricService.processBiometricEvent(request);

        assertNotNull(response);
        assertEquals(BiometricResult.SUCCESS, response.getResult());
        assertEquals("Rahul Kumar", response.getStudentName());
        assertEquals("26105110001", response.getRollNo());

        // Verify that attendance record was saved
        verify(recordRepository, times(1)).save(any(AttendanceRecord.class));
    }
}
