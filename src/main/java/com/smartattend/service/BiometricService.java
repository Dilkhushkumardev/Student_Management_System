package com.smartattend.service;

import com.smartattend.biometric.BiometricEventPublisher;
import com.smartattend.biometric.DemoBiometricProvider;
import com.smartattend.biometric.FaceBiometricProvider;
import com.smartattend.biometric.FingerprintBiometricProvider;
import com.smartattend.biometric.RFIDBiometricProvider;
import com.smartattend.dto.BiometricDtoModels.*;
import com.smartattend.entity.*;
import com.smartattend.enums.*;
import com.smartattend.exception.ResourceNotFoundException;
import com.smartattend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BiometricService {

    private static final Logger log = LoggerFactory.getLogger(BiometricService.class);

    private final BiometricDeviceRepository deviceRepository;
    private final BiometricUserRepository biometricUserRepository;
    private final BiometricEventRepository biometricEventRepository;
    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final BiometricEventPublisher eventPublisher;
    private final AuditService auditService;
    private final NotificationService notificationService;

    private final DemoBiometricProvider demoBiometricProvider;
    private final FingerprintBiometricProvider fingerprintBiometricProvider;
    private final FaceBiometricProvider faceBiometricProvider;
    private final RFIDBiometricProvider rfidBiometricProvider;

    public BiometricService(BiometricDeviceRepository deviceRepository,
                            BiometricUserRepository biometricUserRepository,
                            BiometricEventRepository biometricEventRepository,
                            AttendanceSessionRepository sessionRepository,
                            AttendanceRecordRepository recordRepository,
                            StudentRepository studentRepository,
                            EnrollmentRepository enrollmentRepository,
                            BiometricEventPublisher eventPublisher,
                            AuditService auditService,
                            NotificationService notificationService,
                            DemoBiometricProvider demoBiometricProvider,
                            FingerprintBiometricProvider fingerprintBiometricProvider,
                            FaceBiometricProvider faceBiometricProvider,
                            RFIDBiometricProvider rfidBiometricProvider) {
        this.deviceRepository = deviceRepository;
        this.biometricUserRepository = biometricUserRepository;
        this.biometricEventRepository = biometricEventRepository;
        this.sessionRepository = sessionRepository;
        this.recordRepository = recordRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.eventPublisher = eventPublisher;
        this.auditService = auditService;
        this.notificationService = notificationService;
        this.demoBiometricProvider = demoBiometricProvider;
        this.fingerprintBiometricProvider = fingerprintBiometricProvider;
        this.faceBiometricProvider = faceBiometricProvider;
        this.rfidBiometricProvider = rfidBiometricProvider;
    }

    @Transactional
    public BiometricEventResponse processBiometricEvent(BiometricEventRequest request) {
        // 1. Resolve Device
        BiometricDevice device = deviceRepository.findByDeviceCode(request.getDeviceIdentifier())
                .or(() -> deviceRepository.findBySerialNumber(request.getDeviceIdentifier()))
                .orElseGet(() -> {
                    return deviceRepository.findById(1L).orElseGet(() -> {
                        BiometricDevice newDev = new BiometricDevice();
                        newDev.setDeviceCode("BIO-DEFAULT-01");
                        newDev.setDeviceName("Default Terminal");
                        newDev.setSerialNumber("SN-DEFAULT-001");
                        newDev.setStatus(DeviceStatus.ONLINE);
                        newDev.setDeviceType(DeviceType.FINGERPRINT);
                        newDev.setLocation("Main Classroom");
                        newDev.setLastHeartbeat(LocalDateTime.now());
                        return deviceRepository.save(newDev);
                    });
                });

        device.setLastHeartbeat(LocalDateTime.now());
        device.setLastSyncTime(LocalDateTime.now());
        device.setStatus(DeviceStatus.ONLINE);
        deviceRepository.save(device);

        // 2. Resolve Student
        Student student = findStudentByBiometricOrRoll(request.getBiometricUserId())
                .orElse(null);

        if (student == null) {
            BiometricEvent unknownEvent = new BiometricEvent();
            unknownEvent.setDevice(device);
            unknownEvent.setBiometricUserId(request.getBiometricUserId());
            unknownEvent.setVerificationType(request.getVerificationType() != null ? request.getVerificationType() : "FINGERPRINT");
            unknownEvent.setVerificationResult(BiometricResult.UNKNOWN_USER);
            unknownEvent.setEventTimestamp(LocalDateTime.now());
            unknownEvent.setDeviceLocation(device.getLocation());
            unknownEvent.setRawPayload(request.getRawPayload());
            unknownEvent.setIsProcessed(true);
            unknownEvent.setProcessedAt(LocalDateTime.now());
            biometricEventRepository.save(unknownEvent);

            return BiometricEventResponse.builder()
                    .success(false)
                    .result(BiometricResult.UNKNOWN_USER)
                    .message("No student found matching biometric user ID: " + request.getBiometricUserId())
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        // 3. Resolve Target Active Attendance Session
        AttendanceSession session = null;
        if (request.getTargetSessionId() != null) {
            session = sessionRepository.findById(request.getTargetSessionId()).orElse(null);
        }

        if (session == null || session.getStatus() != SessionStatus.ACTIVE) {
            List<AttendanceSession> activeSessions = sessionRepository.findActiveSessions(null);
            for (AttendanceSession s : activeSessions) {
                if (s.getSection() != null && student.getSection() != null &&
                        s.getSection().getId().equals(student.getSection().getId())) {
                    session = s;
                    break;
                }
            }
        }

        if (session == null || session.getStatus() != SessionStatus.ACTIVE) {
            BiometricEvent evt = new BiometricEvent();
            evt.setDevice(device);
            evt.setBiometricUserId(request.getBiometricUserId());
            evt.setStudent(student);
            evt.setVerificationType(request.getVerificationType() != null ? request.getVerificationType() : "FINGERPRINT");
            evt.setVerificationResult(BiometricResult.SUCCESS);
            evt.setEventTimestamp(LocalDateTime.now());
            evt.setDeviceLocation(device.getLocation());
            evt.setIsProcessed(true);
            evt.setProcessedAt(LocalDateTime.now());
            biometricEventRepository.save(evt);

            String sectionName = student.getSection() != null ? student.getSection().getName() : "General";
            return BiometricEventResponse.builder()
                    .success(true)
                    .result(BiometricResult.SUCCESS)
                    .studentId(student.getId())
                    .studentName(student.getName())
                    .rollNo(student.getRollNo())
                    .message("Biometric recognized. However, no active class session is open for " + sectionName)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        // 4. Duplicate Check & Idempotency
        Optional<AttendanceRecord> existingRecord = recordRepository.findBySessionIdAndStudentId(session.getId(), student.getId());
        if (existingRecord.isPresent()) {
            AttendanceRecord record = existingRecord.get();
            if (record.getStatus() == AttendanceStatus.PRESENT || record.getStatus() == AttendanceStatus.LATE) {
                BiometricEvent dupEvent = new BiometricEvent();
                dupEvent.setDevice(device);
                dupEvent.setBiometricUserId(request.getBiometricUserId());
                dupEvent.setStudent(student);
                dupEvent.setAttendanceSession(session);
                dupEvent.setVerificationType(request.getVerificationType() != null ? request.getVerificationType() : "FINGERPRINT");
                dupEvent.setVerificationResult(BiometricResult.DUPLICATE);
                dupEvent.setEventTimestamp(LocalDateTime.now());
                dupEvent.setDeviceLocation(device.getLocation());
                dupEvent.setIsProcessed(true);
                dupEvent.setProcessedAt(LocalDateTime.now());
                biometricEventRepository.save(dupEvent);

                BiometricLiveCounterDto currentCounter = getLiveCounter(session.getId());

                String subName = session.getSubject() != null ? session.getSubject().getSubjectName() : "Course";
                return BiometricEventResponse.builder()
                        .success(true)
                        .result(BiometricResult.DUPLICATE)
                        .eventId(dupEvent.getId())
                        .studentId(student.getId())
                        .studentName(student.getName())
                        .rollNo(student.getRollNo())
                        .sessionId(session.getId())
                        .subjectName(subName)
                        .message("Attendance already recorded for Roll: " + student.getRollNo())
                        .timestamp(LocalDateTime.now())
                        .updatedCounter(currentCounter)
                        .build();
            }
        }

        // 5. Create / Update Attendance Record to PRESENT
        BiometricEvent event = new BiometricEvent();
        event.setDevice(device);
        event.setBiometricUserId(request.getBiometricUserId());
        event.setStudent(student);
        event.setAttendanceSession(session);
        event.setVerificationType(request.getVerificationType() != null ? request.getVerificationType() : "FINGERPRINT");
        event.setVerificationResult(BiometricResult.SUCCESS);
        event.setEventTimestamp(LocalDateTime.now());
        event.setDeviceLocation(device.getLocation());
        event.setIsProcessed(true);
        event.setProcessedAt(LocalDateTime.now());
        BiometricEvent savedEvent = biometricEventRepository.save(event);

        final AttendanceSession activeSession = session;
        AttendanceRecord record = existingRecord.orElseGet(() -> {
            AttendanceRecord r = new AttendanceRecord();
            r.setSession(activeSession);
            r.setStudent(student);
            return r;
        });

        record.setStatus(AttendanceStatus.PRESENT);
        record.setMethod(AttendanceMethod.BIOMETRIC);
        record.setBiometricEvent(savedEvent);
        record.setMarkedAt(LocalDateTime.now());
        record.setRemarks("Biometric verified: " + savedEvent.getVerificationType());
        record = recordRepository.save(record);

        // Update biometric user last verified
        biometricUserRepository.findByStudentId(student.getId()).ifPresent(bu -> {
            bu.setLastVerifiedAt(LocalDateTime.now());
            biometricUserRepository.save(bu);
        });

        // 6. Real-time Live Broadcast
        BiometricLiveCounterDto updatedCounter = getLiveCounter(session.getId());
        LiveStudentFeedDto feedItem = LiveStudentFeedDto.builder()
                .recordId(record.getId() != null ? record.getId() : savedEvent.getId())
                .studentId(student.getId())
                .rollNo(student.getRollNo())
                .studentName(student.getName())
                .verificationType(savedEvent.getVerificationType())
                .verificationResult("VERIFIED")
                .timeAgo("Just now")
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publishLiveCounterUpdate(session.getId(), updatedCounter);
        eventPublisher.publishStudentScan(session.getId(), feedItem);

        auditService.log("BIOMETRIC_SCAN", "AttendanceRecord", record.getId() != null ? record.getId().toString() : "N/A",
                "NOT_RECORDED", "PRESENT", null,
                "Biometric scan success for " + student.getName() + " (" + student.getRollNo() + ") in session " + session.getSessionCode());

        String subName = session.getSubject() != null ? session.getSubject().getSubjectName() : "Course";
        return BiometricEventResponse.builder()
                .success(true)
                .result(BiometricResult.SUCCESS)
                .eventId(savedEvent.getId())
                .studentId(student.getId())
                .studentName(student.getName())
                .rollNo(student.getRollNo())
                .sessionId(session.getId())
                .subjectName(subName)
                .message("Biometric verified successfully for " + student.getName())
                .timestamp(LocalDateTime.now())
                .updatedCounter(updatedCounter)
                .build();
    }

    @Transactional
    public BiometricLiveCounterDto simulateScan(BiometricSimulationRequest request) {
        AttendanceSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with ID: " + request.getSessionId()));

        if (Boolean.TRUE.equals(session.getIsLocked())) {
            throw new IllegalStateException("Attendance session is locked. Cannot record further scans.");
        }

        if (Boolean.TRUE.equals(request.getSimulateAllRemaining())) {
            List<Student> enrolledStudents = studentRepository.findBySectionIdOrderByRollNoAsc(session.getSection().getId());
            for (Student s : enrolledStudents) {
                Optional<AttendanceRecord> rec = recordRepository.findBySessionIdAndStudentId(session.getId(), s.getId());
                if (rec.isEmpty() || rec.get().getStatus() == AttendanceStatus.ABSENT) {
                    BiometricEventRequest req = BiometricEventRequest.builder()
                            .deviceIdentifier("BIO-CSE-001")
                            .biometricUserId(s.getBiometricId() != null ? s.getBiometricId() : s.getRollNo())
                            .verificationType(request.getVerificationType() != null ? request.getVerificationType() : "SIMULATION")
                            .targetSessionId(session.getId())
                            .build();
                    processBiometricEvent(req);
                }
            }
        } else if (request.getStudentId() != null) {
            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));

            BiometricEventRequest req = BiometricEventRequest.builder()
                    .deviceIdentifier("BIO-CSE-001")
                    .biometricUserId(student.getBiometricId() != null ? student.getBiometricId() : student.getRollNo())
                    .verificationType(request.getVerificationType() != null ? request.getVerificationType() : "SIMULATION")
                    .targetSessionId(session.getId())
                    .build();
            processBiometricEvent(req);
        }

        return getLiveCounter(session.getId());
    }

    @Transactional(readOnly = true)
    public BiometricLiveCounterDto getLiveCounter(Long sessionId) {
        AttendanceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance session not found with ID: " + sessionId));

        List<Student> sectionStudents = studentRepository.findBySectionIdOrderByRollNoAsc(session.getSection().getId());
        int total = sectionStudents.size();

        List<AttendanceRecord> records = recordRepository.findBySessionId(sessionId);
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

        int notVerified = Math.max(0, total - present - late);

        BigDecimal percentage = BigDecimal.ZERO;
        if (total > 0) {
            percentage = BigDecimal.valueOf(present + late)
                    .multiply(BigDecimal.valueOf(100.0))
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        }

        // Recent Feed
        List<BiometricEvent> recentEvents = biometricEventRepository.findRecentEventsForSession(sessionId, PageRequest.of(0, 15));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        List<LiveStudentFeedDto> feedList = new ArrayList<>();
        for (BiometricEvent e : recentEvents) {
            feedList.add(LiveStudentFeedDto.builder()
                    .recordId(e.getId())
                    .studentId(e.getStudent() != null ? e.getStudent().getId() : null)
                    .rollNo(e.getStudent() != null ? e.getStudent().getRollNo() : e.getBiometricUserId())
                    .studentName(e.getStudent() != null ? e.getStudent().getName() : "Unenrolled User")
                    .verificationType(e.getVerificationType())
                    .verificationResult(e.getVerificationResult() != null ? e.getVerificationResult().name() : "SUCCESS")
                    .timeAgo(e.getEventTimestamp() != null ? e.getEventTimestamp().format(timeFormatter) : "Recently")
                    .timestamp(e.getEventTimestamp())
                    .build());
        }

        String subName = session.getSubject() != null ? session.getSubject().getSubjectName() : "Subject";
        String secName = session.getSection() != null ? session.getSection().getName() : "Section";
        String facName = session.getFaculty() != null ? session.getFaculty().getName() : "Faculty";

        return BiometricLiveCounterDto.builder()
                .sessionId(session.getId())
                .sessionCode(session.getSessionCode())
                .subjectName(subName)
                .sectionName(secName)
                .facultyName(facName)
                .totalStudents(total)
                .presentCount(present)
                .absentCount(absent)
                .lateCount(late)
                .excusedCount(excused)
                .notVerifiedCount(notVerified)
                .attendancePercentage(percentage)
                .isLocked(session.getIsLocked())
                .recentFeed(feedList)
                .lastUpdatedAt(LocalDateTime.now())
                .build();
    }

    @Transactional
    public void recordHeartbeat(String deviceCode) {
        deviceRepository.findByDeviceCode(deviceCode).ifPresent(d -> {
            d.setLastHeartbeat(LocalDateTime.now());
            d.setStatus(DeviceStatus.ONLINE);
            deviceRepository.save(d);
        });
    }

    @Transactional(readOnly = true)
    public List<BiometricDeviceDto> getAllDevices() {
        return deviceRepository.findAll().stream().map(d -> BiometricDeviceDto.builder()
                .id(d.getId())
                .deviceCode(d.getDeviceCode())
                .deviceName(d.getDeviceName())
                .serialNumber(d.getSerialNumber())
                .ipAddress(d.getIpAddress())
                .port(d.getPort())
                .location(d.getLocation())
                .deviceType(d.getDeviceType())
                .status(d.getStatus())
                .lastHeartbeat(d.getLastHeartbeat())
                .lastSyncTime(d.getLastSyncTime())
                .isActive(d.getIsActive())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    public BiometricDeviceDto createDevice(BiometricDeviceCreateRequest request) {
        BiometricDevice device = new BiometricDevice();
        device.setDeviceCode(request.getDeviceCode());
        device.setDeviceName(request.getDeviceName());
        device.setSerialNumber(request.getSerialNumber());
        device.setIpAddress(request.getIpAddress());
        device.setPort(request.getPort() != null ? request.getPort() : 80);
        device.setLocation(request.getLocation());
        device.setDeviceType(request.getDeviceType() != null ? request.getDeviceType() : DeviceType.FINGERPRINT);
        device.setStatus(DeviceStatus.ONLINE);
        device.setApiKey(request.getApiKey());
        device.setLastHeartbeat(LocalDateTime.now());
        device.setLastSyncTime(LocalDateTime.now());
        device.setIsActive(true);

        device = deviceRepository.save(device);
        return BiometricDeviceDto.builder()
                .id(device.getId())
                .deviceCode(device.getDeviceCode())
                .deviceName(device.getDeviceName())
                .serialNumber(device.getSerialNumber())
                .ipAddress(device.getIpAddress())
                .port(device.getPort())
                .location(device.getLocation())
                .deviceType(device.getDeviceType())
                .status(device.getStatus())
                .lastHeartbeat(device.getLastHeartbeat())
                .lastSyncTime(device.getLastSyncTime())
                .isActive(device.getIsActive())
                .build();
    }

    private Optional<Student> findStudentByBiometricOrRoll(String identifier) {
        if (identifier == null || identifier.isBlank()) return Optional.empty();

        return studentRepository.findByBiometricId(identifier)
                .or(() -> studentRepository.findByRollNo(identifier))
                .or(() -> studentRepository.findByUniversityRegNo(identifier))
                .or(() -> biometricUserRepository.findByDeviceUserId(identifier).map(BiometricUser::getStudent))
                .or(() -> biometricUserRepository.findByBiometricId(identifier).map(BiometricUser::getStudent));
    }
}
