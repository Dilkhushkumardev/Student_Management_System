package com.smartattend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "subject_id", "semester_id"})
})
public class Enrollment {

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
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "academic_session", nullable = false, length = 50)
    private String academicSession;

    @Column(name = "enrollment_status", nullable = false, length = 20)
    private String enrollmentStatus = "ACTIVE";

    @CreationTimestamp
    @Column(name = "enrolled_at", updatable = false)
    private LocalDateTime enrolledAt;

    public Enrollment() {}

    public Enrollment(Long id, Student student, Subject subject, Section section, Semester semester, String academicSession, String enrollmentStatus, LocalDateTime enrolledAt) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.section = section;
        this.semester = semester;
        this.academicSession = academicSession;
        this.enrollmentStatus = enrollmentStatus != null ? enrollmentStatus : "ACTIVE";
        this.enrolledAt = enrolledAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public String getAcademicSession() { return academicSession; }
    public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }
    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Student student;
        private Subject subject;
        private Section section;
        private Semester semester;
        private String academicSession;
        private String enrollmentStatus = "ACTIVE";
        private LocalDateTime enrolledAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder student(Student student) { this.student = student; return this; }
        public Builder subject(Subject subject) { this.subject = subject; return this; }
        public Builder section(Section section) { this.section = section; return this; }
        public Builder semester(Semester semester) { this.semester = semester; return this; }
        public Builder academicSession(String academicSession) { this.academicSession = academicSession; return this; }
        public Builder enrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; return this; }
        public Builder enrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; return this; }

        public Enrollment build() {
            return new Enrollment(id, student, subject, section, semester, academicSession, enrollmentStatus, enrolledAt);
        }
    }
}
