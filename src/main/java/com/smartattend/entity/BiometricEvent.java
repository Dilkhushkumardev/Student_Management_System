package com.smartattend.entity;

import com.smartattend.enums.BiometricResult;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "biometric_events")
public class BiometricEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id", nullable = false)
    private BiometricDevice device;

    @Column(name = "biometric_user_id", nullable = false, length = 50)
    private String biometricUserId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_session_id")
    private AttendanceSession attendanceSession;

    @Column(name = "verification_type", nullable = false, length = 30)
    private String verificationType = "FINGERPRINT";

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_result", nullable = false, length = 20)
    private BiometricResult verificationResult = BiometricResult.SUCCESS;

    @CreationTimestamp
    @Column(name = "event_timestamp")
    private LocalDateTime eventTimestamp;

    @Column(name = "device_location", length = 150)
    private String deviceLocation;

    @Column(name = "raw_payload", columnDefinition = "TEXT")
    private String rawPayload;

    @Column(name = "is_processed", nullable = false)
    private Boolean isProcessed = false;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    public BiometricEvent() {}

    public BiometricEvent(Long id, BiometricDevice device, String biometricUserId, Student student, AttendanceSession attendanceSession, String verificationType, BiometricResult verificationResult, LocalDateTime eventTimestamp, String deviceLocation, String rawPayload, Boolean isProcessed, LocalDateTime processedAt) {
        this.id = id;
        this.device = device;
        this.biometricUserId = biometricUserId;
        this.student = student;
        this.attendanceSession = attendanceSession;
        this.verificationType = verificationType != null ? verificationType : "FINGERPRINT";
        this.verificationResult = verificationResult != null ? verificationResult : BiometricResult.SUCCESS;
        this.eventTimestamp = eventTimestamp;
        this.deviceLocation = deviceLocation;
        this.rawPayload = rawPayload;
        this.isProcessed = isProcessed != null ? isProcessed : false;
        this.processedAt = processedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BiometricDevice getDevice() { return device; }
    public void setDevice(BiometricDevice device) { this.device = device; }
    public String getBiometricUserId() { return biometricUserId; }
    public void setBiometricUserId(String biometricUserId) { this.biometricUserId = biometricUserId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public AttendanceSession getAttendanceSession() { return attendanceSession; }
    public void setAttendanceSession(AttendanceSession attendanceSession) { this.attendanceSession = attendanceSession; }
    public String getVerificationType() { return verificationType; }
    public void setVerificationType(String verificationType) { this.verificationType = verificationType; }
    public BiometricResult getVerificationResult() { return verificationResult; }
    public void setVerificationResult(BiometricResult verificationResult) { this.verificationResult = verificationResult; }
    public LocalDateTime getEventTimestamp() { return eventTimestamp; }
    public void setEventTimestamp(LocalDateTime eventTimestamp) { this.eventTimestamp = eventTimestamp; }
    public String getDeviceLocation() { return deviceLocation; }
    public void setDeviceLocation(String deviceLocation) { this.deviceLocation = deviceLocation; }
    public String getRawPayload() { return rawPayload; }
    public void setRawPayload(String rawPayload) { this.rawPayload = rawPayload; }
    public Boolean getIsProcessed() { return isProcessed; }
    public void setIsProcessed(Boolean isProcessed) { this.isProcessed = isProcessed; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private BiometricDevice device;
        private String biometricUserId;
        private Student student;
        private AttendanceSession attendanceSession;
        private String verificationType = "FINGERPRINT";
        private BiometricResult verificationResult = BiometricResult.SUCCESS;
        private LocalDateTime eventTimestamp;
        private String deviceLocation;
        private String rawPayload;
        private Boolean isProcessed = false;
        private LocalDateTime processedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder device(BiometricDevice device) { this.device = device; return this; }
        public Builder biometricUserId(String biometricUserId) { this.biometricUserId = biometricUserId; return this; }
        public Builder student(Student student) { this.student = student; return this; }
        public Builder attendanceSession(AttendanceSession attendanceSession) { this.attendanceSession = attendanceSession; return this; }
        public Builder verificationType(String verificationType) { this.verificationType = verificationType; return this; }
        public Builder verificationResult(BiometricResult verificationResult) { this.verificationResult = verificationResult; return this; }
        public Builder eventTimestamp(LocalDateTime eventTimestamp) { this.eventTimestamp = eventTimestamp; return this; }
        public Builder deviceLocation(String deviceLocation) { this.deviceLocation = deviceLocation; return this; }
        public Builder rawPayload(String rawPayload) { this.rawPayload = rawPayload; return this; }
        public Builder isProcessed(Boolean isProcessed) { this.isProcessed = isProcessed; return this; }
        public Builder processedAt(LocalDateTime processedAt) { this.processedAt = processedAt; return this; }

        public BiometricEvent build() {
            return new BiometricEvent(id, device, biometricUserId, student, attendanceSession, verificationType, verificationResult, eventTimestamp, deviceLocation, rawPayload, isProcessed, processedAt);
        }
    }
}
