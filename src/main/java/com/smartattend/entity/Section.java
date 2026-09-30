package com.smartattend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "academic_session", nullable = false, length = 50)
    private String academicSession;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Section() {}

    public Section(Long id, String name, Batch batch, Semester semester, Department department, String academicSession, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.batch = batch;
        this.semester = semester;
        this.department = department;
        this.academicSession = academicSession;
        this.isActive = isActive != null ? isActive : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public String getAcademicSession() { return academicSession; }
    public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private Batch batch;
        private Semester semester;
        private Department department;
        private String academicSession;
        private Boolean isActive = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder batch(Batch batch) { this.batch = batch; return this; }
        public Builder semester(Semester semester) { this.semester = semester; return this; }
        public Builder department(Department department) { this.department = department; return this; }
        public Builder academicSession(String academicSession) { this.academicSession = academicSession; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public Section build() {
            return new Section(id, name, batch, semester, department, academicSession, isActive);
        }
    }
}
