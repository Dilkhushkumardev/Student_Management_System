package com.smartattend.entity;

import com.smartattend.enums.AttendanceMethod;
import com.smartattend.enums.AttendanceStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_records", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"attendance_session_id", "student_id"})
})
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attendance_session_id", nullable = false)
    private AttendanceSession session;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status = AttendanceStatus.PRESENT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AttendanceMethod method = AttendanceMethod.MANUAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biometric_event_id")
    private BiometricEvent biometricEvent;

    @Column(length = 255)
    private String remarks;

    @CreationTimestamp
    @Column(name = "marked_at")
    private LocalDateTime markedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by_id")
    private User markedBy;

    @Column(name = "is_admin_override", nullable = false)
    private Boolean isAdminOverride = false;

    @Column(name = "override_reason", length = 255)
    private String overrideReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "override_by_id")
    private User overrideBy;

    @Column(name = "override_at")
    private LocalDateTime overrideAt;

    public AttendanceRecord() {}

    public AttendanceRecord(Long id, AttendanceSession session, Student student, AttendanceStatus status, AttendanceMethod method, BiometricEvent biometricEvent, String remarks, LocalDateTime markedAt, User markedBy, Boolean isAdminOverride, String overrideReason, User overrideBy, LocalDateTime overrideAt) {
        this.id = id;
        this.session = session;
        this.student = student;
        this.status = status != null ? status : AttendanceStatus.PRESENT;
        this.method = method != null ? method : AttendanceMethod.MANUAL;
        this.biometricEvent = biometricEvent;
        this.remarks = remarks;
        this.markedAt = markedAt;
        this.markedBy = markedBy;
        this.isAdminOverride = isAdminOverride != null ? isAdminOverride : false;
        this.overrideReason = overrideReason;
        this.overrideBy = overrideBy;
        this.overrideAt = overrideAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AttendanceSession getSession() { return session; }
    public void setSession(AttendanceSession session) { this.session = session; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
    public AttendanceMethod getMethod() { return method; }
    public void setMethod(AttendanceMethod method) { this.method = method; }
    public BiometricEvent getBiometricEvent() { return biometricEvent; }
    public void setBiometricEvent(BiometricEvent biometricEvent) { this.biometricEvent = biometricEvent; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDateTime getMarkedAt() { return markedAt; }
    public void setMarkedAt(LocalDateTime markedAt) { this.markedAt = markedAt; }
    public User getMarkedBy() { return markedBy; }
    public void setMarkedBy(User markedBy) { this.markedBy = markedBy; }
    public Boolean getIsAdminOverride() { return isAdminOverride; }
    public void setIsAdminOverride(Boolean isAdminOverride) { this.isAdminOverride = isAdminOverride; }
    public String getOverrideReason() { return overrideReason; }
    public void setOverrideReason(String overrideReason) { this.overrideReason = overrideReason; }
    public User getOverrideBy() { return overrideBy; }
    public void setOverrideBy(User overrideBy) { this.overrideBy = overrideBy; }
    public LocalDateTime getOverrideAt() { return overrideAt; }
    public void setOverrideAt(LocalDateTime overrideAt) { this.overrideAt = overrideAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private AttendanceSession session;
        private Student student;
        private AttendanceStatus status = AttendanceStatus.PRESENT;
        private AttendanceMethod method = AttendanceMethod.MANUAL;
        private BiometricEvent biometricEvent;
        private String remarks;
        private LocalDateTime markedAt;
        private User markedBy;
        private Boolean isAdminOverride = false;
        private String overrideReason;
        private User overrideBy;
        private LocalDateTime overrideAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder session(AttendanceSession session) { this.session = session; return this; }
        public Builder student(Student student) { this.student = student; return this; }
        public Builder status(AttendanceStatus status) { this.status = status; return this; }
        public Builder method(AttendanceMethod method) { this.method = method; return this; }
        public Builder biometricEvent(BiometricEvent biometricEvent) { this.biometricEvent = biometricEvent; return this; }
        public Builder remarks(String remarks) { this.remarks = remarks; return this; }
        public Builder markedAt(LocalDateTime markedAt) { this.markedAt = markedAt; return this; }
        public Builder markedBy(User markedBy) { this.markedBy = markedBy; return this; }
        public Builder isAdminOverride(Boolean isAdminOverride) { this.isAdminOverride = isAdminOverride; return this; }
        public Builder overrideReason(String overrideReason) { this.overrideReason = overrideReason; return this; }
        public Builder overrideBy(User overrideBy) { this.overrideBy = overrideBy; return this; }
        public Builder overrideAt(LocalDateTime overrideAt) { this.overrideAt = overrideAt; return this; }

        public AttendanceRecord build() {
            return new AttendanceRecord(id, session, student, status, method, biometricEvent, remarks, markedAt, markedBy, isAdminOverride, overrideReason, overrideBy, overrideAt);
        }
    }
}
