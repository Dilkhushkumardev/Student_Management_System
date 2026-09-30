package com.smartattend.entity;

import com.smartattend.enums.EligibilityStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_summary", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "subject_id", "semester_id"})
})
public class AttendanceSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "total_classes", nullable = false)
    private Integer totalClasses = 0;

    @Column(name = "present_classes", nullable = false)
    private Integer presentClasses = 0;

    @Column(name = "absent_classes", nullable = false)
    private Integer absentClasses = 0;

    @Column(name = "late_classes", nullable = false)
    private Integer lateClasses = 0;

    @Column(name = "excused_classes", nullable = false)
    private Integer excusedClasses = 0;

    @Column(name = "attendance_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal attendancePercentage = BigDecimal.ZERO;

    @Column(name = "attendance_marks", nullable = false)
    private Integer attendanceMarks = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EligibilityStatus eligibility = EligibilityStatus.SHORTAGE;

    @UpdateTimestamp
    @Column(name = "last_calculated_at")
    private LocalDateTime lastCalculatedAt;

    public AttendanceSummary() {}

    public AttendanceSummary(Long id, Student student, Subject subject, Semester semester, Integer totalClasses, Integer presentClasses, Integer absentClasses, Integer lateClasses, Integer excusedClasses, BigDecimal attendancePercentage, Integer attendanceMarks, EligibilityStatus eligibility, LocalDateTime lastCalculatedAt) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.semester = semester;
        this.totalClasses = totalClasses != null ? totalClasses : 0;
        this.presentClasses = presentClasses != null ? presentClasses : 0;
        this.absentClasses = absentClasses != null ? absentClasses : 0;
        this.lateClasses = lateClasses != null ? lateClasses : 0;
        this.excusedClasses = excusedClasses != null ? excusedClasses : 0;
        this.attendancePercentage = attendancePercentage != null ? attendancePercentage : BigDecimal.ZERO;
        this.attendanceMarks = attendanceMarks != null ? attendanceMarks : 0;
        this.eligibility = eligibility != null ? eligibility : EligibilityStatus.SHORTAGE;
        this.lastCalculatedAt = lastCalculatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public Integer getTotalClasses() { return totalClasses; }
    public void setTotalClasses(Integer totalClasses) { this.totalClasses = totalClasses; }
    public Integer getPresentClasses() { return presentClasses; }
    public void setPresentClasses(Integer presentClasses) { this.presentClasses = presentClasses; }
    public Integer getAbsentClasses() { return absentClasses; }
    public void setAbsentClasses(Integer absentClasses) { this.absentClasses = absentClasses; }
    public Integer getLateClasses() { return lateClasses; }
    public void setLateClasses(Integer lateClasses) { this.lateClasses = lateClasses; }
    public Integer getExcusedClasses() { return excusedClasses; }
    public void setExcusedClasses(Integer excusedClasses) { this.excusedClasses = excusedClasses; }
    public BigDecimal getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(BigDecimal attendancePercentage) { this.attendancePercentage = attendancePercentage; }
    public Integer getAttendanceMarks() { return attendanceMarks; }
    public void setAttendanceMarks(Integer attendanceMarks) { this.attendanceMarks = attendanceMarks; }
    public EligibilityStatus getEligibility() { return eligibility; }
    public void setEligibility(EligibilityStatus eligibility) { this.eligibility = eligibility; }
    public LocalDateTime getLastCalculatedAt() { return lastCalculatedAt; }
    public void setLastCalculatedAt(LocalDateTime lastCalculatedAt) { this.lastCalculatedAt = lastCalculatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Student student;
        private Subject subject;
        private Semester semester;
        private Integer totalClasses = 0;
        private Integer presentClasses = 0;
        private Integer absentClasses = 0;
        private Integer lateClasses = 0;
        private Integer excusedClasses = 0;
        private BigDecimal attendancePercentage = BigDecimal.ZERO;
        private Integer attendanceMarks = 0;
        private EligibilityStatus eligibility = EligibilityStatus.SHORTAGE;
        private LocalDateTime lastCalculatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder student(Student student) { this.student = student; return this; }
        public Builder subject(Subject subject) { this.subject = subject; return this; }
        public Builder semester(Semester semester) { this.semester = semester; return this; }
        public Builder totalClasses(Integer totalClasses) { this.totalClasses = totalClasses; return this; }
        public Builder presentClasses(Integer presentClasses) { this.presentClasses = presentClasses; return this; }
        public Builder absentClasses(Integer absentClasses) { this.absentClasses = absentClasses; return this; }
        public Builder lateClasses(Integer lateClasses) { this.lateClasses = lateClasses; return this; }
        public Builder excusedClasses(Integer excusedClasses) { this.excusedClasses = excusedClasses; return this; }
        public Builder attendancePercentage(BigDecimal attendancePercentage) { this.attendancePercentage = attendancePercentage; return this; }
        public Builder attendanceMarks(Integer attendanceMarks) { this.attendanceMarks = attendanceMarks; return this; }
        public Builder eligibility(EligibilityStatus eligibility) { this.eligibility = eligibility; return this; }
        public Builder lastCalculatedAt(LocalDateTime lastCalculatedAt) { this.lastCalculatedAt = lastCalculatedAt; return this; }

        public AttendanceSummary build() {
            return new AttendanceSummary(id, student, subject, semester, totalClasses, presentClasses, absentClasses, lateClasses, excusedClasses, attendancePercentage, attendanceMarks, eligibility, lastCalculatedAt);
        }
    }
}
