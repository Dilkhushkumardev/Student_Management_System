package com.smartattend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "semesters", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"semester_number", "academic_session"})
})
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "semester_number", nullable = false)
    private Integer semesterNumber;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "academic_session", nullable = false, length = 50)
    private String academicSession;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Semester() {}

    public Semester(Long id, Integer semesterNumber, String name, String academicSession, Boolean isCurrent, Boolean isActive) {
        this.id = id;
        this.semesterNumber = semesterNumber;
        this.name = name;
        this.academicSession = academicSession;
        this.isCurrent = isCurrent != null ? isCurrent : false;
        this.isActive = isActive != null ? isActive : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getSemesterNumber() { return semesterNumber; }
    public void setSemesterNumber(Integer semesterNumber) { this.semesterNumber = semesterNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAcademicSession() { return academicSession; }
    public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
    public Boolean getIsCurrent() { return isCurrent; }
    public void setIsCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Integer semesterNumber;
        private String name;
        private String academicSession;
        private Boolean isCurrent = false;
        private Boolean isActive = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder semesterNumber(Integer semesterNumber) { this.semesterNumber = semesterNumber; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder academicSession(String academicSession) { this.academicSession = academicSession; return this; }
        public Builder isCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public Semester build() {
            return new Semester(id, semesterNumber, name, academicSession, isCurrent, isActive);
        }
    }
}
