package com.smartattend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class FacultyDtoModels {

    public static class FacultyDto {
        private Long id;
        private Long userId;
        private String employeeId;
        private String name;
        private String email;
        private String mobile;
        private Long departmentId;
        private String departmentName;
        private String designation;
        private String status;
        private List<AssignedSubjectDto> assignedSubjects;

        public FacultyDto() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }
        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
        public String getDesignation() { return designation; }
        public void setDesignation(String designation) { this.designation = designation; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public List<AssignedSubjectDto> getAssignedSubjects() { return assignedSubjects; }
        public void setAssignedSubjects(List<AssignedSubjectDto> assignedSubjects) { this.assignedSubjects = assignedSubjects; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final FacultyDto dto = new FacultyDto();
            public Builder id(Long id) { dto.setId(id); return this; }
            public Builder userId(Long userId) { dto.setUserId(userId); return this; }
            public Builder employeeId(String employeeId) { dto.setEmployeeId(employeeId); return this; }
            public Builder name(String name) { dto.setName(name); return this; }
            public Builder email(String email) { dto.setEmail(email); return this; }
            public Builder mobile(String mobile) { dto.setMobile(mobile); return this; }
            public Builder departmentId(Long departmentId) { dto.setDepartmentId(departmentId); return this; }
            public Builder departmentName(String departmentName) { dto.setDepartmentName(departmentName); return this; }
            public Builder designation(String designation) { dto.setDesignation(designation); return this; }
            public Builder status(String status) { dto.setStatus(status); return this; }
            public Builder assignedSubjects(List<AssignedSubjectDto> assignedSubjects) { dto.setAssignedSubjects(assignedSubjects); return this; }
            public FacultyDto build() { return dto; }
        }
    }

    public static class AssignedSubjectDto {
        private Long facultySubjectId;
        private Long subjectId;
        private String courseCode;
        private String subjectName;
        private Long sectionId;
        private String sectionName;
        private String academicSession;

        public AssignedSubjectDto() {}

        public Long getFacultySubjectId() { return facultySubjectId; }
        public void setFacultySubjectId(Long facultySubjectId) { this.facultySubjectId = facultySubjectId; }
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
        public String getSectionName() { return sectionName; }
        public void setSectionName(String sectionName) { this.sectionName = sectionName; }
        public String getAcademicSession() { return academicSession; }
        public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final AssignedSubjectDto dto = new AssignedSubjectDto();
            public Builder facultySubjectId(Long facultySubjectId) { dto.setFacultySubjectId(facultySubjectId); return this; }
            public Builder subjectId(Long subjectId) { dto.setSubjectId(subjectId); return this; }
            public Builder courseCode(String courseCode) { dto.setCourseCode(courseCode); return this; }
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder sectionId(Long sectionId) { dto.setSectionId(sectionId); return this; }
            public Builder sectionName(String sectionName) { dto.setSectionName(sectionName); return this; }
            public Builder academicSession(String academicSession) { dto.setAcademicSession(academicSession); return this; }
            public AssignedSubjectDto build() { return dto; }
        }
    }

    public static class FacultyCreateRequest {
        @NotBlank(message = "Employee ID is required")
        private String employeeId;

        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Valid email is required")
        private String email;

        private String mobile;

        @NotNull(message = "Department ID is required")
        private Long departmentId;

        @NotBlank(message = "Designation is required")
        private String designation;

        private String username;
        private String password;
        private List<Long> subjectIds;
        private Long sectionId;

        public FacultyCreateRequest() {}

        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }
        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
        public String getDesignation() { return designation; }
        public void setDesignation(String designation) { this.designation = designation; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public List<Long> getSubjectIds() { return subjectIds; }
        public void setSubjectIds(List<Long> subjectIds) { this.subjectIds = subjectIds; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final FacultyCreateRequest req = new FacultyCreateRequest();
            public Builder employeeId(String employeeId) { req.setEmployeeId(employeeId); return this; }
            public Builder name(String name) { req.setName(name); return this; }
            public Builder email(String email) { req.setEmail(email); return this; }
            public Builder mobile(String mobile) { req.setMobile(mobile); return this; }
            public Builder departmentId(Long departmentId) { req.setDepartmentId(departmentId); return this; }
            public Builder designation(String designation) { req.setDesignation(designation); return this; }
            public Builder username(String username) { req.setUsername(username); return this; }
            public Builder password(String password) { req.setPassword(password); return this; }
            public Builder subjectIds(List<Long> subjectIds) { req.setSubjectIds(subjectIds); return this; }
            public Builder sectionId(Long sectionId) { req.setSectionId(sectionId); return this; }
            public FacultyCreateRequest build() { return req; }
        }
    }

    public static class FacultyUpdateRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @Email(message = "Valid email is required")
        private String email;

        private String mobile;
        private Long departmentId;
        private String designation;
        private String status;

        public FacultyUpdateRequest() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }
        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
        public String getDesignation() { return designation; }
        public void setDesignation(String designation) { this.designation = designation; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
