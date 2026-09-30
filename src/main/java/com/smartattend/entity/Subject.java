package com.smartattend.entity;

import com.smartattend.enums.SubjectType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code", nullable = false, unique = true, length = 30)
    private String courseCode;

    @Column(name = "subject_name", nullable = false, length = 150)
    private String subjectName;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_type", nullable = false, length = 20)
    private SubjectType subjectType = SubjectType.THEORY;

    @Column(name = "lecture_hours", nullable = false)
    private Integer lectureHours = 3;

    @Column(name = "tutorial_hours", nullable = false)
    private Integer tutorialHours = 0;

    @Column(name = "practical_hours", nullable = false)
    private Integer practicalHours = 0;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal credits = BigDecimal.valueOf(3.0);

    @Column(name = "max_internal_marks", nullable = false)
    private Integer maxInternalMarks = 30;

    @Column(name = "max_ese_marks", nullable = false)
    private Integer maxEseMarks = 70;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "academic_session", nullable = false, length = 50)
    private String academicSession;

    @Column(name = "attendance_required", nullable = false)
    private Boolean attendanceRequired = true;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Subject() {}

    public Subject(Long id, String courseCode, String subjectName, SubjectType subjectType, Integer lectureHours, Integer tutorialHours, Integer practicalHours, BigDecimal credits, Integer maxInternalMarks, Integer maxEseMarks, Semester semester, Department department, String academicSession, Boolean attendanceRequired, Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.courseCode = courseCode;
        this.subjectName = subjectName;
        this.subjectType = subjectType != null ? subjectType : SubjectType.THEORY;
        this.lectureHours = lectureHours != null ? lectureHours : 3;
        this.tutorialHours = tutorialHours != null ? tutorialHours : 0;
        this.practicalHours = practicalHours != null ? practicalHours : 0;
        this.credits = credits != null ? credits : BigDecimal.valueOf(3.0);
        this.maxInternalMarks = maxInternalMarks != null ? maxInternalMarks : 30;
        this.maxEseMarks = maxEseMarks != null ? maxEseMarks : 70;
        this.semester = semester;
        this.department = department;
        this.academicSession = academicSession;
        this.attendanceRequired = attendanceRequired != null ? attendanceRequired : true;
        this.isActive = isActive != null ? isActive : true;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public SubjectType getSubjectType() { return subjectType; }
    public void setSubjectType(SubjectType subjectType) { this.subjectType = subjectType; }
    public Integer getLectureHours() { return lectureHours; }
    public void setLectureHours(Integer lectureHours) { this.lectureHours = lectureHours; }
    public Integer getTutorialHours() { return tutorialHours; }
    public void setTutorialHours(Integer tutorialHours) { this.tutorialHours = tutorialHours; }
    public Integer getPracticalHours() { return practicalHours; }
    public void setPracticalHours(Integer practicalHours) { this.practicalHours = practicalHours; }
    public BigDecimal getCredits() { return credits; }
    public void setCredits(BigDecimal credits) { this.credits = credits; }
    public Integer getMaxInternalMarks() { return maxInternalMarks; }
    public void setMaxInternalMarks(Integer maxInternalMarks) { this.maxInternalMarks = maxInternalMarks; }
    public Integer getMaxEseMarks() { return maxEseMarks; }
    public void setMaxEseMarks(Integer maxEseMarks) { this.maxEseMarks = maxEseMarks; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public String getAcademicSession() { return academicSession; }
    public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
    public Boolean getAttendanceRequired() { return attendanceRequired; }
    public void setAttendanceRequired(Boolean attendanceRequired) { this.attendanceRequired = attendanceRequired; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String courseCode;
        private String subjectName;
        private SubjectType subjectType = SubjectType.THEORY;
        private Integer lectureHours = 3;
        private Integer tutorialHours = 0;
        private Integer practicalHours = 0;
        private BigDecimal credits = BigDecimal.valueOf(3.0);
        private Integer maxInternalMarks = 30;
        private Integer maxEseMarks = 70;
        private Semester semester;
        private Department department;
        private String academicSession;
        private Boolean attendanceRequired = true;
        private Boolean isActive = true;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder courseCode(String courseCode) { this.courseCode = courseCode; return this; }
        public Builder subjectName(String subjectName) { this.subjectName = subjectName; return this; }
        public Builder subjectType(SubjectType subjectType) { this.subjectType = subjectType; return this; }
        public Builder lectureHours(Integer lectureHours) { this.lectureHours = lectureHours; return this; }
        public Builder tutorialHours(Integer tutorialHours) { this.tutorialHours = tutorialHours; return this; }
        public Builder practicalHours(Integer practicalHours) { this.practicalHours = practicalHours; return this; }
        public Builder credits(BigDecimal credits) { this.credits = credits; return this; }
        public Builder maxInternalMarks(Integer maxInternalMarks) { this.maxInternalMarks = maxInternalMarks; return this; }
        public Builder maxEseMarks(Integer maxEseMarks) { this.maxEseMarks = maxEseMarks; return this; }
        public Builder semester(Semester semester) { this.semester = semester; return this; }
        public Builder department(Department department) { this.department = department; return this; }
        public Builder academicSession(String academicSession) { this.academicSession = academicSession; return this; }
        public Builder attendanceRequired(Boolean attendanceRequired) { this.attendanceRequired = attendanceRequired; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Subject build() {
            return new Subject(id, courseCode, subjectName, subjectType, lectureHours, tutorialHours, practicalHours, credits, maxInternalMarks, maxEseMarks, semester, department, academicSession, attendanceRequired, isActive, createdAt);
        }
    }
}
