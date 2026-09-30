package com.smartattend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "biometric_users")
public class BiometricUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", unique = true)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", unique = true)
    private Faculty faculty;

    @Column(name = "biometric_id", nullable = false, unique = true, length = 50)
    private String biometricId;

    @Column(name = "device_user_id", nullable = false, length = 50)
    private String deviceUserId;

    @Column(name = "enrollment_status", nullable = false, length = 20)
    private String enrollmentStatus = "ENROLLED";

    @Column(name = "template_type", length = 30)
    private String templateType = "FINGERPRINT";

    @CreationTimestamp
    @Column(name = "enrolled_at", updatable = false)
    private LocalDateTime enrolledAt;

    @Column(name = "last_verified_at")
    private LocalDateTime lastVerifiedAt;

    public BiometricUser() {}

    public BiometricUser(Long id, Student student, Faculty faculty, String biometricId, String deviceUserId, String enrollmentStatus, String templateType, LocalDateTime enrolledAt, LocalDateTime lastVerifiedAt) {
        this.id = id;
        this.student = student;
        this.faculty = faculty;
        this.biometricId = biometricId;
        this.deviceUserId = deviceUserId;
        this.enrollmentStatus = enrollmentStatus != null ? enrollmentStatus : "ENROLLED";
        this.templateType = templateType != null ? templateType : "FINGERPRINT";
        this.enrolledAt = enrolledAt;
        this.lastVerifiedAt = lastVerifiedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public String getBiometricId() { return biometricId; }
    public void setBiometricId(String biometricId) { this.biometricId = biometricId; }
    public String getDeviceUserId() { return deviceUserId; }
    public void setDeviceUserId(String deviceUserId) { this.deviceUserId = deviceUserId; }
    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }
    public String getTemplateType() { return templateType; }
    public void setTemplateType(String templateType) { this.templateType = templateType; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
    public LocalDateTime getLastVerifiedAt() { return lastVerifiedAt; }
    public void setLastVerifiedAt(LocalDateTime lastVerifiedAt) { this.lastVerifiedAt = lastVerifiedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Student student;
        private Faculty faculty;
        private String biometricId;
        private String deviceUserId;
        private String enrollmentStatus = "ENROLLED";
        private String templateType = "FINGERPRINT";
        private LocalDateTime enrolledAt;
        private LocalDateTime lastVerifiedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder student(Student student) { this.student = student; return this; }
        public Builder faculty(Faculty faculty) { this.faculty = faculty; return this; }
        public Builder biometricId(String biometricId) { this.biometricId = biometricId; return this; }
        public Builder deviceUserId(String deviceUserId) { this.deviceUserId = deviceUserId; return this; }
        public Builder enrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; return this; }
        public Builder templateType(String templateType) { this.templateType = templateType; return this; }
        public Builder enrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; return this; }
        public Builder lastVerifiedAt(LocalDateTime lastVerifiedAt) { this.lastVerifiedAt = lastVerifiedAt; return this; }

        public BiometricUser build() {
            return new BiometricUser(id, student, faculty, biometricId, deviceUserId, enrollmentStatus, templateType, enrolledAt, lastVerifiedAt);
        }
    }
}
