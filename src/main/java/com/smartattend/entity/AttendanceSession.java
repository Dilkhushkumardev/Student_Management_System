package com.smartattend.entity;

import com.smartattend.enums.SessionStatus;
import com.smartattend.enums.VerificationMode;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "attendance_sessions")
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_code", nullable = false, unique = true, length = 64)
    private String sessionCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "period_number", nullable = false)
    private Integer periodNumber = 1;

    @Column(name = "room_no", length = 50)
    private String roomNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status = SessionStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_mode", nullable = false, length = 20)
    private VerificationMode verificationMode = VerificationMode.MANUAL;

    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locked_by_id")
    private User lockedBy;

    @Column(length = 255)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public AttendanceSession() {}

    public AttendanceSession(Long id, String sessionCode, Subject subject, Faculty faculty, Section section, LocalDate sessionDate, LocalTime startTime, LocalTime endTime, Integer periodNumber, String roomNo, SessionStatus status, VerificationMode verificationMode, Boolean isLocked, LocalDateTime lockedAt, User lockedBy, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.sessionCode = sessionCode;
        this.subject = subject;
        this.faculty = faculty;
        this.section = section;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.periodNumber = periodNumber != null ? periodNumber : 1;
        this.roomNo = roomNo;
        this.status = status != null ? status : SessionStatus.ACTIVE;
        this.verificationMode = verificationMode != null ? verificationMode : VerificationMode.MANUAL;
        this.isLocked = isLocked != null ? isLocked : false;
        this.lockedAt = lockedAt;
        this.lockedBy = lockedBy;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSessionCode() { return sessionCode; }
    public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }
    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public Integer getPeriodNumber() { return periodNumber; }
    public void setPeriodNumber(Integer periodNumber) { this.periodNumber = periodNumber; }
    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
    public VerificationMode getVerificationMode() { return verificationMode; }
    public void setVerificationMode(VerificationMode verificationMode) { this.verificationMode = verificationMode; }
    public Boolean getIsLocked() { return isLocked; }
    public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }
    public LocalDateTime getLockedAt() { return lockedAt; }
    public void setLockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; }
    public User getLockedBy() { return lockedBy; }
    public void setLockedBy(User lockedBy) { this.lockedBy = lockedBy; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String sessionCode;
        private Subject subject;
        private Faculty faculty;
        private Section section;
        private LocalDate sessionDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer periodNumber = 1;
        private String roomNo;
        private SessionStatus status = SessionStatus.ACTIVE;
        private VerificationMode verificationMode = VerificationMode.MANUAL;
        private Boolean isLocked = false;
        private LocalDateTime lockedAt;
        private User lockedBy;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder sessionCode(String sessionCode) { this.sessionCode = sessionCode; return this; }
        public Builder subject(Subject subject) { this.subject = subject; return this; }
        public Builder faculty(Faculty faculty) { this.faculty = faculty; return this; }
        public Builder section(Section section) { this.section = section; return this; }
        public Builder sessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; return this; }
        public Builder startTime(LocalTime startTime) { this.startTime = startTime; return this; }
        public Builder endTime(LocalTime endTime) { this.endTime = endTime; return this; }
        public Builder periodNumber(Integer periodNumber) { this.periodNumber = periodNumber; return this; }
        public Builder roomNo(String roomNo) { this.roomNo = roomNo; return this; }
        public Builder status(SessionStatus status) { this.status = status; return this; }
        public Builder verificationMode(VerificationMode verificationMode) { this.verificationMode = verificationMode; return this; }
        public Builder isLocked(Boolean isLocked) { this.isLocked = isLocked; return this; }
        public Builder lockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; return this; }
        public Builder lockedBy(User lockedBy) { this.lockedBy = lockedBy; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public AttendanceSession build() {
            return new AttendanceSession(id, sessionCode, subject, faculty, section, sessionDate, startTime, endTime, periodNumber, roomNo, status, verificationMode, isLocked, lockedAt, lockedBy, notes, createdAt, updatedAt);
        }
    }
}
