package com.smartattend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "university_reg_no", nullable = false, unique = true, length = 50)
    private String universityRegNo;

    @Column(name = "roll_no", nullable = false, unique = true, length = 50)
    private String rollNo;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "father_name", length = 150)
    private String fatherName;

    @Column(name = "mother_name", length = 150)
    private String motherName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String mobile;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 10)
    private String gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "admission_year", nullable = false)
    private Integer admissionYear;

    @Column(name = "profile_photo", length = 255)
    private String profilePhoto;

    @Column(name = "biometric_id", unique = true, length = 50)
    private String biometricId;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Student() {}

    public Student(Long id, User user, String universityRegNo, String rollNo, String name, String fatherName, String motherName, String email, String mobile, LocalDate dateOfBirth, String gender, Department department, Branch branch, Batch batch, Semester semester, Section section, Integer admissionYear, String profilePhoto, String biometricId, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.universityRegNo = universityRegNo;
        this.rollNo = rollNo;
        this.name = name;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.email = email;
        this.mobile = mobile;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.department = department;
        this.branch = branch;
        this.batch = batch;
        this.semester = semester;
        this.section = section;
        this.admissionYear = admissionYear;
        this.profilePhoto = profilePhoto;
        this.biometricId = biometricId;
        this.status = status != null ? status : "ACTIVE";
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getUniversityRegNo() { return universityRegNo; }
    public void setUniversityRegNo(String universityRegNo) { this.universityRegNo = universityRegNo; }
    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }
    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }
    public Integer getAdmissionYear() { return admissionYear; }
    public void setAdmissionYear(Integer admissionYear) { this.admissionYear = admissionYear; }
    public String getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }
    public String getBiometricId() { return biometricId; }
    public void setBiometricId(String biometricId) { this.biometricId = biometricId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String universityRegNo;
        private String rollNo;
        private String name;
        private String fatherName;
        private String motherName;
        private String email;
        private String mobile;
        private LocalDate dateOfBirth;
        private String gender;
        private Department department;
        private Branch branch;
        private Batch batch;
        private Semester semester;
        private Section section;
        private Integer admissionYear;
        private String profilePhoto;
        private String biometricId;
        private String status = "ACTIVE";
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder universityRegNo(String universityRegNo) { this.universityRegNo = universityRegNo; return this; }
        public Builder rollNo(String rollNo) { this.rollNo = rollNo; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder fatherName(String fatherName) { this.fatherName = fatherName; return this; }
        public Builder motherName(String motherName) { this.motherName = motherName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder mobile(String mobile) { this.mobile = mobile; return this; }
        public Builder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder department(Department department) { this.department = department; return this; }
        public Builder branch(Branch branch) { this.branch = branch; return this; }
        public Builder batch(Batch batch) { this.batch = batch; return this; }
        public Builder semester(Semester semester) { this.semester = semester; return this; }
        public Builder section(Section section) { this.section = section; return this; }
        public Builder admissionYear(Integer admissionYear) { this.admissionYear = admissionYear; return this; }
        public Builder profilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; return this; }
        public Builder biometricId(String biometricId) { this.biometricId = biometricId; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Student build() {
            return new Student(id, user, universityRegNo, rollNo, name, fatherName, motherName, email, mobile, dateOfBirth, gender, department, branch, batch, semester, section, admissionYear, profilePhoto, biometricId, status, createdAt, updatedAt);
        }
    }
}
