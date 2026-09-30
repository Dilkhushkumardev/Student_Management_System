package com.smartattend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "faculty_subjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"faculty_id", "subject_id", "section_id", "academic_session"})
})
public class FacultySubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "academic_session", nullable = false, length = 50)
    private String academicSession;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public FacultySubject() {}

    public FacultySubject(Long id, Faculty faculty, Subject subject, Section section, String academicSession, Boolean isActive) {
        this.id = id;
        this.faculty = faculty;
        this.subject = subject;
        this.section = section;
        this.academicSession = academicSession;
        this.isActive = isActive != null ? isActive : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }
    public String getAcademicSession() { return academicSession; }
    public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Faculty faculty;
        private Subject subject;
        private Section section;
        private String academicSession;
        private Boolean isActive = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder faculty(Faculty faculty) { this.faculty = faculty; return this; }
        public Builder subject(Subject subject) { this.subject = subject; return this; }
        public Builder section(Section section) { this.section = section; return this; }
        public Builder academicSession(String academicSession) { this.academicSession = academicSession; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public FacultySubject build() {
            return new FacultySubject(id, faculty, subject, section, academicSession, isActive);
        }
    }
}
