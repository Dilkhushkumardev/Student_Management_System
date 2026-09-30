package com.smartattend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudentDtoModels {

    public static class StudentDto {
        private Long id;
        private Long userId;
        private String universityRegNo;
        private String rollNo;
        private String name;
        private String fatherName;
        private String motherName;
        private String email;
        private String mobile;
        private LocalDate dateOfBirth;
        private String gender;
        private Long departmentId;
        private String departmentName;
        private Long branchId;
        private String branchName;
        private Long batchId;
        private String batchName;
        private Long semesterId;
        private String semesterName;
        private Long sectionId;
        private String sectionName;
        private Integer admissionYear;
        private String profilePhoto;
        private String biometricId;
        private String status;
        private BigDecimal overallAttendancePercentage;
        private String eligibilityStatus;
        private LocalDateTime createdAt;

        public StudentDto() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
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
        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
        public Long getBranchId() { return branchId; }
        public void setBranchId(Long branchId) { this.branchId = branchId; }
        public String getBranchName() { return branchName; }
        public void setBranchName(String branchName) { this.branchName = branchName; }
        public Long getBatchId() { return batchId; }
        public void setBatchId(Long batchId) { this.batchId = batchId; }
        public String getBatchName() { return batchName; }
        public void setBatchName(String batchName) { this.batchName = batchName; }
        public Long getSemesterId() { return semesterId; }
        public void setSemesterId(Long semesterId) { this.semesterId = semesterId; }
        public String getSemesterName() { return semesterName; }
        public void setSemesterName(String semesterName) { this.semesterName = semesterName; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
        public String getSectionName() { return sectionName; }
        public void setSectionName(String sectionName) { this.sectionName = sectionName; }
        public Integer getAdmissionYear() { return admissionYear; }
        public void setAdmissionYear(Integer admissionYear) { this.admissionYear = admissionYear; }
        public String getProfilePhoto() { return profilePhoto; }
        public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }
        public String getBiometricId() { return biometricId; }
        public void setBiometricId(String biometricId) { this.biometricId = biometricId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public BigDecimal getOverallAttendancePercentage() { return overallAttendancePercentage; }
        public void setOverallAttendancePercentage(BigDecimal overallAttendancePercentage) { this.overallAttendancePercentage = overallAttendancePercentage; }
        public String getEligibilityStatus() { return eligibilityStatus; }
        public void setEligibilityStatus(String eligibilityStatus) { this.eligibilityStatus = eligibilityStatus; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final StudentDto dto = new StudentDto();
            public Builder id(Long id) { dto.setId(id); return this; }
            public Builder userId(Long userId) { dto.setUserId(userId); return this; }
            public Builder universityRegNo(String universityRegNo) { dto.setUniversityRegNo(universityRegNo); return this; }
            public Builder rollNo(String rollNo) { dto.setRollNo(rollNo); return this; }
            public Builder name(String name) { dto.setName(name); return this; }
            public Builder fatherName(String fatherName) { dto.setFatherName(fatherName); return this; }
            public Builder motherName(String motherName) { dto.setMotherName(motherName); return this; }
            public Builder email(String email) { dto.setEmail(email); return this; }
            public Builder mobile(String mobile) { dto.setMobile(mobile); return this; }
            public Builder dateOfBirth(LocalDate dateOfBirth) { dto.setDateOfBirth(dateOfBirth); return this; }
            public Builder gender(String gender) { dto.setGender(gender); return this; }
            public Builder departmentId(Long departmentId) { dto.setDepartmentId(departmentId); return this; }
            public Builder departmentName(String departmentName) { dto.setDepartmentName(departmentName); return this; }
            public Builder branchId(Long branchId) { dto.setBranchId(branchId); return this; }
            public Builder branchName(String branchName) { dto.setBranchName(branchName); return this; }
            public Builder batchId(Long batchId) { dto.setBatchId(batchId); return this; }
            public Builder batchName(String batchName) { dto.setBatchName(batchName); return this; }
            public Builder semesterId(Long semesterId) { dto.setSemesterId(semesterId); return this; }
            public Builder semesterName(String semesterName) { dto.setSemesterName(semesterName); return this; }
            public Builder sectionId(Long sectionId) { dto.setSectionId(sectionId); return this; }
            public Builder sectionName(String sectionName) { dto.setSectionName(sectionName); return this; }
            public Builder admissionYear(Integer admissionYear) { dto.setAdmissionYear(admissionYear); return this; }
            public Builder profilePhoto(String profilePhoto) { dto.setProfilePhoto(profilePhoto); return this; }
            public Builder biometricId(String biometricId) { dto.setBiometricId(biometricId); return this; }
            public Builder status(String status) { dto.setStatus(status); return this; }
            public Builder overallAttendancePercentage(BigDecimal overallAttendancePercentage) { dto.setOverallAttendancePercentage(overallAttendancePercentage); return this; }
            public Builder eligibilityStatus(String eligibilityStatus) { dto.setEligibilityStatus(eligibilityStatus); return this; }
            public Builder createdAt(LocalDateTime createdAt) { dto.setCreatedAt(createdAt); return this; }
            public StudentDto build() { return dto; }
        }
    }

    public static class StudentCreateRequest {
        @NotBlank(message = "University Registration Number is required")
        private String universityRegNo;

        @NotBlank(message = "Roll Number is required")
        private String rollNo;

        @NotBlank(message = "Student Name is required")
        private String name;

        private String fatherName;
        private String motherName;

        @NotBlank(message = "Email is required")
        @Email(message = "Valid email is required")
        private String email;

        private String mobile;
        private LocalDate dateOfBirth;
        private String gender;

        @NotNull(message = "Department ID is required")
        private Long departmentId;

        @NotNull(message = "Branch ID is required")
        private Long branchId;

        @NotNull(message = "Batch ID is required")
        private Long batchId;

        @NotNull(message = "Semester ID is required")
        private Long semesterId;

        @NotNull(message = "Section ID is required")
        private Long sectionId;

        @NotNull(message = "Admission Year is required")
        private Integer admissionYear;

        private String biometricId;
        private String password;

        public StudentCreateRequest() {}

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
        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
        public Long getBranchId() { return branchId; }
        public void setBranchId(Long branchId) { this.branchId = branchId; }
        public Long getBatchId() { return batchId; }
        public void setBatchId(Long batchId) { this.batchId = batchId; }
        public Long getSemesterId() { return semesterId; }
        public void setSemesterId(Long semesterId) { this.semesterId = semesterId; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
        public Integer getAdmissionYear() { return admissionYear; }
        public void setAdmissionYear(Integer admissionYear) { this.admissionYear = admissionYear; }
        public String getBiometricId() { return biometricId; }
        public void setBiometricId(String biometricId) { this.biometricId = biometricId; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final StudentCreateRequest req = new StudentCreateRequest();
            public Builder universityRegNo(String universityRegNo) { req.setUniversityRegNo(universityRegNo); return this; }
            public Builder rollNo(String rollNo) { req.setRollNo(rollNo); return this; }
            public Builder name(String name) { req.setName(name); return this; }
            public Builder fatherName(String fatherName) { req.setFatherName(fatherName); return this; }
            public Builder motherName(String motherName) { req.setMotherName(motherName); return this; }
            public Builder email(String email) { req.setEmail(email); return this; }
            public Builder mobile(String mobile) { req.setMobile(mobile); return this; }
            public Builder dateOfBirth(LocalDate dateOfBirth) { req.setDateOfBirth(dateOfBirth); return this; }
            public Builder gender(String gender) { req.setGender(gender); return this; }
            public Builder departmentId(Long departmentId) { req.setDepartmentId(departmentId); return this; }
            public Builder branchId(Long branchId) { req.setBranchId(branchId); return this; }
            public Builder batchId(Long batchId) { req.setBatchId(batchId); return this; }
            public Builder semesterId(Long semesterId) { req.setSemesterId(semesterId); return this; }
            public Builder sectionId(Long sectionId) { req.setSectionId(sectionId); return this; }
            public Builder admissionYear(Integer admissionYear) { req.setAdmissionYear(admissionYear); return this; }
            public Builder biometricId(String biometricId) { req.setBiometricId(biometricId); return this; }
            public Builder password(String password) { req.setPassword(password); return this; }
            public StudentCreateRequest build() { return req; }
        }
    }

    public static class StudentUpdateRequest {
        @NotBlank(message = "Student Name is required")
        private String name;
        private String fatherName;
        private String motherName;
        @Email(message = "Valid email is required")
        private String email;
        private String mobile;
        private LocalDate dateOfBirth;
        private String gender;
        private Long semesterId;
        private Long sectionId;
        private String biometricId;
        private String status;

        public StudentUpdateRequest() {}

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
        public Long getSemesterId() { return semesterId; }
        public void setSemesterId(Long semesterId) { this.semesterId = semesterId; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
        public String getBiometricId() { return biometricId; }
        public void setBiometricId(String biometricId) { this.biometricId = biometricId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class StudentShortageDto {
        private Long studentId;
        private String rollNo;
        private String name;
        private String universityRegNo;
        private String subjectCode;
        private String subjectName;
        private Integer totalClasses;
        private Integer attendedClasses;
        private BigDecimal attendancePercentage;
        private Integer classesNeededFor75;
        private String alertMessage;

        public StudentShortageDto() {}

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public String getRollNo() { return rollNo; }
        public void setRollNo(String rollNo) { this.rollNo = rollNo; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getUniversityRegNo() { return universityRegNo; }
        public void setUniversityRegNo(String universityRegNo) { this.universityRegNo = universityRegNo; }
        public String getSubjectCode() { return subjectCode; }
        public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public Integer getTotalClasses() { return totalClasses; }
        public void setTotalClasses(Integer totalClasses) { this.totalClasses = totalClasses; }
        public Integer getAttendedClasses() { return attendedClasses; }
        public void setAttendedClasses(Integer attendedClasses) { this.attendedClasses = attendedClasses; }
        public BigDecimal getAttendancePercentage() { return attendancePercentage; }
        public void setAttendancePercentage(BigDecimal attendancePercentage) { this.attendancePercentage = attendancePercentage; }
        public Integer getClassesNeededFor75() { return classesNeededFor75; }
        public void setClassesNeededFor75(Integer classesNeededFor75) { this.classesNeededFor75 = classesNeededFor75; }
        public String getAlertMessage() { return alertMessage; }
        public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final StudentShortageDto dto = new StudentShortageDto();
            public Builder studentId(Long studentId) { dto.setStudentId(studentId); return this; }
            public Builder rollNo(String rollNo) { dto.setRollNo(rollNo); return this; }
            public Builder name(String name) { dto.setName(name); return this; }
            public Builder universityRegNo(String universityRegNo) { dto.setUniversityRegNo(universityRegNo); return this; }
            public Builder subjectCode(String subjectCode) { dto.setSubjectCode(subjectCode); return this; }
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder totalClasses(Integer totalClasses) { dto.setTotalClasses(totalClasses); return this; }
            public Builder attendedClasses(Integer attendedClasses) { dto.setAttendedClasses(attendedClasses); return this; }
            public Builder attendancePercentage(BigDecimal attendancePercentage) { dto.setAttendancePercentage(attendancePercentage); return this; }
            public Builder classesNeededFor75(Integer classesNeededFor75) { dto.setClassesNeededFor75(classesNeededFor75); return this; }
            public Builder alertMessage(String alertMessage) { dto.setAlertMessage(alertMessage); return this; }
            public StudentShortageDto build() { return dto; }
        }
    }
}
