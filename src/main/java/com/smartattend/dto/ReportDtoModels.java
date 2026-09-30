package com.smartattend.dto;

import com.smartattend.enums.EligibilityStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReportDtoModels {

    public static class StudentAttendanceReportDto {
        private String universityName;
        private String collegeName;
        private String academicSession;
        private String semester;
        private String department;
        private String section;
        private String studentName;
        private String rollNumber;
        private String registrationNumber;
        private String email;
        private List<SubjectAttendanceRow> subjectRows;
        private BigDecimal overallAttendancePercentage;
        private Integer totalSubjects;
        private Integer shortageSubjectsCount;
        private EligibilityStatus overallEligibility;
        private LocalDateTime generatedAt;

        public StudentAttendanceReportDto() {}

        public String getUniversityName() { return universityName; }
        public void setUniversityName(String universityName) { this.universityName = universityName; }
        public String getCollegeName() { return collegeName; }
        public void setCollegeName(String collegeName) { this.collegeName = collegeName; }
        public String getAcademicSession() { return academicSession; }
        public void setAcademicSession(String academicSession) { this.academicSession = academicSession; }
        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getSection() { return section; }
        public void setSection(String section) { this.section = section; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getRollNumber() { return rollNumber; }
        public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
        public String getRegistrationNumber() { return registrationNumber; }
        public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public List<SubjectAttendanceRow> getSubjectRows() { return subjectRows; }
        public void setSubjectRows(List<SubjectAttendanceRow> subjectRows) { this.subjectRows = subjectRows; }
        public BigDecimal getOverallAttendancePercentage() { return overallAttendancePercentage; }
        public void setOverallAttendancePercentage(BigDecimal overallAttendancePercentage) { this.overallAttendancePercentage = overallAttendancePercentage; }
        public Integer getTotalSubjects() { return totalSubjects; }
        public void setTotalSubjects(Integer totalSubjects) { this.totalSubjects = totalSubjects; }
        public Integer getShortageSubjectsCount() { return shortageSubjectsCount; }
        public void setShortageSubjectsCount(Integer shortageSubjectsCount) { this.shortageSubjectsCount = shortageSubjectsCount; }
        public EligibilityStatus getOverallEligibility() { return overallEligibility; }
        public void setOverallEligibility(EligibilityStatus overallEligibility) { this.overallEligibility = overallEligibility; }
        public LocalDateTime getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final StudentAttendanceReportDto dto = new StudentAttendanceReportDto();
            public Builder universityName(String universityName) { dto.setUniversityName(universityName); return this; }
            public Builder collegeName(String collegeName) { dto.setCollegeName(collegeName); return this; }
            public Builder academicSession(String academicSession) { dto.setAcademicSession(academicSession); return this; }
            public Builder semester(String semester) { dto.setSemester(semester); return this; }
            public Builder department(String department) { dto.setDepartment(department); return this; }
            public Builder section(String section) { dto.setSection(section); return this; }
            public Builder studentName(String studentName) { dto.setStudentName(studentName); return this; }
            public Builder rollNumber(String rollNumber) { dto.setRollNumber(rollNumber); return this; }
            public Builder registrationNumber(String registrationNumber) { dto.setRegistrationNumber(registrationNumber); return this; }
            public Builder email(String email) { dto.setEmail(email); return this; }
            public Builder subjectRows(List<SubjectAttendanceRow> subjectRows) { dto.setSubjectRows(subjectRows); return this; }
            public Builder overallAttendancePercentage(BigDecimal overallAttendancePercentage) { dto.setOverallAttendancePercentage(overallAttendancePercentage); return this; }
            public Builder totalSubjects(Integer totalSubjects) { dto.setTotalSubjects(totalSubjects); return this; }
            public Builder shortageSubjectsCount(Integer shortageSubjectsCount) { dto.setShortageSubjectsCount(shortageSubjectsCount); return this; }
            public Builder overallEligibility(EligibilityStatus overallEligibility) { dto.setOverallEligibility(overallEligibility); return this; }
            public Builder generatedAt(LocalDateTime generatedAt) { dto.setGeneratedAt(generatedAt); return this; }
            public StudentAttendanceReportDto build() { return dto; }
        }
    }

    public static class SubjectAttendanceRow {
        private Long subjectId;
        private String courseCode;
        private String subjectName;
        private String subjectType;
        private Integer totalClasses;
        private Integer presentClasses;
        private Integer absentClasses;
        private Integer lateClasses;
        private Integer excusedClasses;
        private BigDecimal percentage;
        private Integer attendanceMarks;
        private EligibilityStatus status;

        public SubjectAttendanceRow() {}

        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public String getSubjectType() { return subjectType; }
        public void setSubjectType(String subjectType) { this.subjectType = subjectType; }
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
        public BigDecimal getPercentage() { return percentage; }
        public void setPercentage(BigDecimal percentage) { this.percentage = percentage; }
        public Integer getAttendanceMarks() { return attendanceMarks; }
        public void setAttendanceMarks(Integer attendanceMarks) { this.attendanceMarks = attendanceMarks; }
        public EligibilityStatus getStatus() { return status; }
        public void setStatus(EligibilityStatus status) { this.status = status; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final SubjectAttendanceRow row = new SubjectAttendanceRow();
            public Builder subjectId(Long subjectId) { row.setSubjectId(subjectId); return this; }
            public Builder courseCode(String courseCode) { row.setCourseCode(courseCode); return this; }
            public Builder subjectName(String subjectName) { row.setSubjectName(subjectName); return this; }
            public Builder subjectType(String subjectType) { row.setSubjectType(subjectType); return this; }
            public Builder totalClasses(Integer totalClasses) { row.setTotalClasses(totalClasses); return this; }
            public Builder presentClasses(Integer presentClasses) { row.setPresentClasses(presentClasses); return this; }
            public Builder absentClasses(Integer absentClasses) { row.setAbsentClasses(absentClasses); return this; }
            public Builder lateClasses(Integer lateClasses) { row.setLateClasses(lateClasses); return this; }
            public Builder excusedClasses(Integer excusedClasses) { row.setExcusedClasses(excusedClasses); return this; }
            public Builder percentage(BigDecimal percentage) { row.setPercentage(percentage); return this; }
            public Builder attendanceMarks(Integer attendanceMarks) { row.setAttendanceMarks(attendanceMarks); return this; }
            public Builder status(EligibilityStatus status) { row.setStatus(status); return this; }
            public SubjectAttendanceRow build() { return row; }
        }
    }

    public static class SubjectAttendanceReportDto {
        private Long subjectId;
        private String courseCode;
        private String subjectName;
        private String departmentName;
        private String semesterName;
        private String facultyName;
        private Integer totalClassesConducted;
        private BigDecimal averageClassAttendance;
        private List<StudentSubjectRow> studentRows;

        public SubjectAttendanceReportDto() {}

        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
        public String getSemesterName() { return semesterName; }
        public void setSemesterName(String semesterName) { this.semesterName = semesterName; }
        public String getFacultyName() { return facultyName; }
        public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
        public Integer getTotalClassesConducted() { return totalClassesConducted; }
        public void setTotalClassesConducted(Integer totalClassesConducted) { this.totalClassesConducted = totalClassesConducted; }
        public BigDecimal getAverageClassAttendance() { return averageClassAttendance; }
        public void setAverageClassAttendance(BigDecimal averageClassAttendance) { this.averageClassAttendance = averageClassAttendance; }
        public List<StudentSubjectRow> getStudentRows() { return studentRows; }
        public void setStudentRows(List<StudentSubjectRow> studentRows) { this.studentRows = studentRows; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final SubjectAttendanceReportDto dto = new SubjectAttendanceReportDto();
            public Builder subjectId(Long subjectId) { dto.setSubjectId(subjectId); return this; }
            public Builder courseCode(String courseCode) { dto.setCourseCode(courseCode); return this; }
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder departmentName(String departmentName) { dto.setDepartmentName(departmentName); return this; }
            public Builder semesterName(String semesterName) { dto.setSemesterName(semesterName); return this; }
            public Builder facultyName(String facultyName) { dto.setFacultyName(facultyName); return this; }
            public Builder totalClassesConducted(Integer totalClassesConducted) { dto.setTotalClassesConducted(totalClassesConducted); return this; }
            public Builder averageClassAttendance(BigDecimal averageClassAttendance) { dto.setAverageClassAttendance(averageClassAttendance); return this; }
            public Builder studentRows(List<StudentSubjectRow> studentRows) { dto.setStudentRows(studentRows); return this; }
            public SubjectAttendanceReportDto build() { return dto; }
        }
    }

    public static class StudentSubjectRow {
        private Long studentId;
        private String rollNo;
        private String studentName;
        private String universityRegNo;
        private Integer totalClasses;
        private Integer presentClasses;
        private Integer absentClasses;
        private BigDecimal attendancePercentage;
        private Integer marks;
        private EligibilityStatus eligibility;

        public StudentSubjectRow() {}

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public String getRollNo() { return rollNo; }
        public void setRollNo(String rollNo) { this.rollNo = rollNo; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getUniversityRegNo() { return universityRegNo; }
        public void setUniversityRegNo(String universityRegNo) { this.universityRegNo = universityRegNo; }
        public Integer getTotalClasses() { return totalClasses; }
        public void setTotalClasses(Integer totalClasses) { this.totalClasses = totalClasses; }
        public Integer getPresentClasses() { return presentClasses; }
        public void setPresentClasses(Integer presentClasses) { this.presentClasses = presentClasses; }
        public Integer getAbsentClasses() { return absentClasses; }
        public void setAbsentClasses(Integer absentClasses) { this.absentClasses = absentClasses; }
        public BigDecimal getAttendancePercentage() { return attendancePercentage; }
        public void setAttendancePercentage(BigDecimal attendancePercentage) { this.attendancePercentage = attendancePercentage; }
        public Integer getMarks() { return marks; }
        public void setMarks(Integer marks) { this.marks = marks; }
        public EligibilityStatus getEligibility() { return eligibility; }
        public void setEligibility(EligibilityStatus eligibility) { this.eligibility = eligibility; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final StudentSubjectRow row = new StudentSubjectRow();
            public Builder studentId(Long studentId) { row.setStudentId(studentId); return this; }
            public Builder rollNo(String rollNo) { row.setRollNo(rollNo); return this; }
            public Builder studentName(String studentName) { row.setStudentName(studentName); return this; }
            public Builder universityRegNo(String universityRegNo) { row.setUniversityRegNo(universityRegNo); return this; }
            public Builder totalClasses(Integer totalClasses) { row.setTotalClasses(totalClasses); return this; }
            public Builder presentClasses(Integer presentClasses) { row.setPresentClasses(presentClasses); return this; }
            public Builder absentClasses(Integer absentClasses) { row.setAbsentClasses(absentClasses); return this; }
            public Builder attendancePercentage(BigDecimal attendancePercentage) { row.setAttendancePercentage(attendancePercentage); return this; }
            public Builder marks(Integer marks) { row.setMarks(marks); return this; }
            public Builder eligibility(EligibilityStatus eligibility) { row.setEligibility(eligibility); return this; }
            public StudentSubjectRow build() { return row; }
        }
    }

    public static class AdminDashboardDto {
        private Long totalStudents;
        private Long totalFaculty;
        private Long totalDepartments;
        private Long totalSubjects;
        private Long todayAttendanceSessions;
        private BigDecimal averageCollegeAttendance;
        private Long studentsBelow75;
        private Long biometricDevicesOnline;
        private Long biometricDevicesOffline;
        private Long activeSessionsCount;
        private List<SubjectAvgDto> subjectWiseAttendance;
        private List<DepartmentAvgDto> departmentWiseAttendance;
        private List<DailyTrendDto> weeklyTrend;

        public AdminDashboardDto() {}

        public Long getTotalStudents() { return totalStudents; }
        public void setTotalStudents(Long totalStudents) { this.totalStudents = totalStudents; }
        public Long getTotalFaculty() { return totalFaculty; }
        public void setTotalFaculty(Long totalFaculty) { this.totalFaculty = totalFaculty; }
        public Long getTotalDepartments() { return totalDepartments; }
        public void setTotalDepartments(Long totalDepartments) { this.totalDepartments = totalDepartments; }
        public Long getTotalSubjects() { return totalSubjects; }
        public void setTotalSubjects(Long totalSubjects) { this.totalSubjects = totalSubjects; }
        public Long getTodayAttendanceSessions() { return todayAttendanceSessions; }
        public void setTodayAttendanceSessions(Long todayAttendanceSessions) { this.todayAttendanceSessions = todayAttendanceSessions; }
        public BigDecimal getAverageCollegeAttendance() { return averageCollegeAttendance; }
        public void setAverageCollegeAttendance(BigDecimal averageCollegeAttendance) { this.averageCollegeAttendance = averageCollegeAttendance; }
        public Long getStudentsBelow75() { return studentsBelow75; }
        public void setStudentsBelow75(Long studentsBelow75) { this.studentsBelow75 = studentsBelow75; }
        public Long getBiometricDevicesOnline() { return biometricDevicesOnline; }
        public void setBiometricDevicesOnline(Long biometricDevicesOnline) { this.biometricDevicesOnline = biometricDevicesOnline; }
        public Long getBiometricDevicesOffline() { return biometricDevicesOffline; }
        public void setBiometricDevicesOffline(Long biometricDevicesOffline) { this.biometricDevicesOffline = biometricDevicesOffline; }
        public Long getActiveSessionsCount() { return activeSessionsCount; }
        public void setActiveSessionsCount(Long activeSessionsCount) { this.activeSessionsCount = activeSessionsCount; }
        public List<SubjectAvgDto> getSubjectWiseAttendance() { return subjectWiseAttendance; }
        public void setSubjectWiseAttendance(List<SubjectAvgDto> subjectWiseAttendance) { this.subjectWiseAttendance = subjectWiseAttendance; }
        public List<DepartmentAvgDto> getDepartmentWiseAttendance() { return departmentWiseAttendance; }
        public void setDepartmentWiseAttendance(List<DepartmentAvgDto> departmentWiseAttendance) { this.departmentWiseAttendance = departmentWiseAttendance; }
        public List<DailyTrendDto> getWeeklyTrend() { return weeklyTrend; }
        public void setWeeklyTrend(List<DailyTrendDto> weeklyTrend) { this.weeklyTrend = weeklyTrend; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final AdminDashboardDto dto = new AdminDashboardDto();
            public Builder totalStudents(Long totalStudents) { dto.setTotalStudents(totalStudents); return this; }
            public Builder totalFaculty(Long totalFaculty) { dto.setTotalFaculty(totalFaculty); return this; }
            public Builder totalDepartments(Long totalDepartments) { dto.setTotalDepartments(totalDepartments); return this; }
            public Builder totalSubjects(Long totalSubjects) { dto.setTotalSubjects(totalSubjects); return this; }
            public Builder todayAttendanceSessions(Long todayAttendanceSessions) { dto.setTodayAttendanceSessions(todayAttendanceSessions); return this; }
            public Builder averageCollegeAttendance(BigDecimal averageCollegeAttendance) { dto.setAverageCollegeAttendance(averageCollegeAttendance); return this; }
            public Builder studentsBelow75(Long studentsBelow75) { dto.setStudentsBelow75(studentsBelow75); return this; }
            public Builder biometricDevicesOnline(Long biometricDevicesOnline) { dto.setBiometricDevicesOnline(biometricDevicesOnline); return this; }
            public Builder biometricDevicesOffline(Long biometricDevicesOffline) { dto.setBiometricDevicesOffline(biometricDevicesOffline); return this; }
            public Builder activeSessionsCount(Long activeSessionsCount) { dto.setActiveSessionsCount(activeSessionsCount); return this; }
            public Builder subjectWiseAttendance(List<SubjectAvgDto> subjectWiseAttendance) { dto.setSubjectWiseAttendance(subjectWiseAttendance); return this; }
            public Builder departmentWiseAttendance(List<DepartmentAvgDto> departmentWiseAttendance) { dto.setDepartmentWiseAttendance(departmentWiseAttendance); return this; }
            public Builder weeklyTrend(List<DailyTrendDto> weeklyTrend) { dto.setWeeklyTrend(weeklyTrend); return this; }
            public AdminDashboardDto build() { return dto; }
        }
    }

    public static class FacultyDashboardDto {
        private Long facultyId;
        private String facultyName;
        private String departmentName;
        private List<AttendanceDtoModels.AttendanceSessionDto> todaysClasses;
        private List<AttendanceDtoModels.AttendanceSessionDto> activeSessions;
        private Integer totalAssignedSubjects;
        private Integer totalAssignedSections;
        private Integer todayPresentCount;
        private Integer todayAbsentCount;
        private BigDecimal overallSubjectAverage;
        private Integer studentsBelow75Count;
        private List<StudentDtoModels.StudentShortageDto> shortageStudents;

        public FacultyDashboardDto() {}

        public Long getFacultyId() { return facultyId; }
        public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
        public String getFacultyName() { return facultyName; }
        public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
        public List<AttendanceDtoModels.AttendanceSessionDto> getTodaysClasses() { return todaysClasses; }
        public void setTodaysClasses(List<AttendanceDtoModels.AttendanceSessionDto> todaysClasses) { this.todaysClasses = todaysClasses; }
        public List<AttendanceDtoModels.AttendanceSessionDto> getActiveSessions() { return activeSessions; }
        public void setActiveSessions(List<AttendanceDtoModels.AttendanceSessionDto> activeSessions) { this.activeSessions = activeSessions; }
        public Integer getTotalAssignedSubjects() { return totalAssignedSubjects; }
        public void setTotalAssignedSubjects(Integer totalAssignedSubjects) { this.totalAssignedSubjects = totalAssignedSubjects; }
        public Integer getTotalAssignedSections() { return totalAssignedSections; }
        public void setTotalAssignedSections(Integer totalAssignedSections) { this.totalAssignedSections = totalAssignedSections; }
        public Integer getTodayPresentCount() { return todayPresentCount; }
        public void setTodayPresentCount(Integer todayPresentCount) { this.todayPresentCount = todayPresentCount; }
        public Integer getTodayAbsentCount() { return todayAbsentCount; }
        public void setTodayAbsentCount(Integer todayAbsentCount) { this.todayAbsentCount = todayAbsentCount; }
        public BigDecimal getOverallSubjectAverage() { return overallSubjectAverage; }
        public void setOverallSubjectAverage(BigDecimal overallSubjectAverage) { this.overallSubjectAverage = overallSubjectAverage; }
        public Integer getStudentsBelow75Count() { return studentsBelow75Count; }
        public void setStudentsBelow75Count(Integer studentsBelow75Count) { this.studentsBelow75Count = studentsBelow75Count; }
        public List<StudentDtoModels.StudentShortageDto> getShortageStudents() { return shortageStudents; }
        public void setShortageStudents(List<StudentDtoModels.StudentShortageDto> shortageStudents) { this.shortageStudents = shortageStudents; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final FacultyDashboardDto dto = new FacultyDashboardDto();
            public Builder facultyId(Long facultyId) { dto.setFacultyId(facultyId); return this; }
            public Builder facultyName(String facultyName) { dto.setFacultyName(facultyName); return this; }
            public Builder departmentName(String departmentName) { dto.setDepartmentName(departmentName); return this; }
            public Builder todaysClasses(List<AttendanceDtoModels.AttendanceSessionDto> todaysClasses) { dto.setTodaysClasses(todaysClasses); return this; }
            public Builder activeSessions(List<AttendanceDtoModels.AttendanceSessionDto> activeSessions) { dto.setActiveSessions(activeSessions); return this; }
            public Builder totalAssignedSubjects(Integer totalAssignedSubjects) { dto.setTotalAssignedSubjects(totalAssignedSubjects); return this; }
            public Builder totalAssignedSections(Integer totalAssignedSections) { dto.setTotalAssignedSections(totalAssignedSections); return this; }
            public Builder todayPresentCount(Integer todayPresentCount) { dto.setTodayPresentCount(todayPresentCount); return this; }
            public Builder todayAbsentCount(Integer todayAbsentCount) { dto.setTodayAbsentCount(todayAbsentCount); return this; }
            public Builder overallSubjectAverage(BigDecimal overallSubjectAverage) { dto.setOverallSubjectAverage(overallSubjectAverage); return this; }
            public Builder studentsBelow75Count(Integer studentsBelow75Count) { dto.setStudentsBelow75Count(studentsBelow75Count); return this; }
            public Builder shortageStudents(List<StudentDtoModels.StudentShortageDto> shortageStudents) { dto.setShortageStudents(shortageStudents); return this; }
            public FacultyDashboardDto build() { return dto; }
        }
    }

    public static class StudentDashboardDto {
        private Long studentId;
        private String studentName;
        private String rollNo;
        private String universityRegNo;
        private String departmentName;
        private String sectionName;
        private String semesterName;
        private BigDecimal overallAttendancePercentage;
        private Integer totalSubjectsCount;
        private Integer shortageSubjectsCount;
        private Integer totalMarksEarned;
        private EligibilityStatus overallEligibility;
        private List<AttendanceDtoModels.AttendanceCardDto> subjectCards;
        private List<RecentAttendanceItemDto> recentAttendance;
        private List<BiometricVerificationHistoryDto> biometricHistory;

        public StudentDashboardDto() {}

        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getRollNo() { return rollNo; }
        public void setRollNo(String rollNo) { this.rollNo = rollNo; }
        public String getUniversityRegNo() { return universityRegNo; }
        public void setUniversityRegNo(String universityRegNo) { this.universityRegNo = universityRegNo; }
        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
        public String getSectionName() { return sectionName; }
        public void setSectionName(String sectionName) { this.sectionName = sectionName; }
        public String getSemesterName() { return semesterName; }
        public void setSemesterName(String semesterName) { this.semesterName = semesterName; }
        public BigDecimal getOverallAttendancePercentage() { return overallAttendancePercentage; }
        public void setOverallAttendancePercentage(BigDecimal overallAttendancePercentage) { this.overallAttendancePercentage = overallAttendancePercentage; }
        public Integer getTotalSubjectsCount() { return totalSubjectsCount; }
        public void setTotalSubjectsCount(Integer totalSubjectsCount) { this.totalSubjectsCount = totalSubjectsCount; }
        public Integer getShortageSubjectsCount() { return shortageSubjectsCount; }
        public void setShortageSubjectsCount(Integer shortageSubjectsCount) { this.shortageSubjectsCount = shortageSubjectsCount; }
        public Integer getTotalMarksEarned() { return totalMarksEarned; }
        public void setTotalMarksEarned(Integer totalMarksEarned) { this.totalMarksEarned = totalMarksEarned; }
        public EligibilityStatus getOverallEligibility() { return overallEligibility; }
        public void setOverallEligibility(EligibilityStatus overallEligibility) { this.overallEligibility = overallEligibility; }
        public List<AttendanceDtoModels.AttendanceCardDto> getSubjectCards() { return subjectCards; }
        public void setSubjectCards(List<AttendanceDtoModels.AttendanceCardDto> subjectCards) { this.subjectCards = subjectCards; }
        public List<RecentAttendanceItemDto> getRecentAttendance() { return recentAttendance; }
        public void setRecentAttendance(List<RecentAttendanceItemDto> recentAttendance) { this.recentAttendance = recentAttendance; }
        public List<BiometricVerificationHistoryDto> getBiometricHistory() { return biometricHistory; }
        public void setBiometricHistory(List<BiometricVerificationHistoryDto> biometricHistory) { this.biometricHistory = biometricHistory; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final StudentDashboardDto dto = new StudentDashboardDto();
            public Builder studentId(Long studentId) { dto.setStudentId(studentId); return this; }
            public Builder studentName(String studentName) { dto.setStudentName(studentName); return this; }
            public Builder rollNo(String rollNo) { dto.setRollNo(rollNo); return this; }
            public Builder universityRegNo(String universityRegNo) { dto.setUniversityRegNo(universityRegNo); return this; }
            public Builder departmentName(String departmentName) { dto.setDepartmentName(departmentName); return this; }
            public Builder sectionName(String sectionName) { dto.setSectionName(sectionName); return this; }
            public Builder semesterName(String semesterName) { dto.setSemesterName(semesterName); return this; }
            public Builder overallAttendancePercentage(BigDecimal overallAttendancePercentage) { dto.setOverallAttendancePercentage(overallAttendancePercentage); return this; }
            public Builder totalSubjectsCount(Integer totalSubjectsCount) { dto.setTotalSubjectsCount(totalSubjectsCount); return this; }
            public Builder shortageSubjectsCount(Integer shortageSubjectsCount) { dto.setShortageSubjectsCount(shortageSubjectsCount); return this; }
            public Builder totalMarksEarned(Integer totalMarksEarned) { dto.setTotalMarksEarned(totalMarksEarned); return this; }
            public Builder overallEligibility(EligibilityStatus overallEligibility) { dto.setOverallEligibility(overallEligibility); return this; }
            public Builder subjectCards(List<AttendanceDtoModels.AttendanceCardDto> subjectCards) { dto.setSubjectCards(subjectCards); return this; }
            public Builder recentAttendance(List<RecentAttendanceItemDto> recentAttendance) { dto.setRecentAttendance(recentAttendance); return this; }
            public Builder biometricHistory(List<BiometricVerificationHistoryDto> biometricHistory) { dto.setBiometricHistory(biometricHistory); return this; }
            public StudentDashboardDto build() { return dto; }
        }
    }

    public static class RecentAttendanceItemDto {
        private Long sessionId;
        private String subjectName;
        private String courseCode;
        private LocalDate date;
        private String status;
        private String method;
        private String time;

        public RecentAttendanceItemDto() {}

        public Long getSessionId() { return sessionId; }
        public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final RecentAttendanceItemDto dto = new RecentAttendanceItemDto();
            public Builder sessionId(Long sessionId) { dto.setSessionId(sessionId); return this; }
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder courseCode(String courseCode) { dto.setCourseCode(courseCode); return this; }
            public Builder date(LocalDate date) { dto.setDate(date); return this; }
            public Builder status(String status) { dto.setStatus(status); return this; }
            public Builder method(String method) { dto.setMethod(method); return this; }
            public Builder time(String time) { dto.setTime(time); return this; }
            public RecentAttendanceItemDto build() { return dto; }
        }
    }

    public static class BiometricVerificationHistoryDto {
        private Long eventId;
        private String deviceLocation;
        private String verificationType;
        private String verificationResult;
        private LocalDateTime timestamp;

        public BiometricVerificationHistoryDto() {}

        public Long getEventId() { return eventId; }
        public void setEventId(Long eventId) { this.eventId = eventId; }
        public String getDeviceLocation() { return deviceLocation; }
        public void setDeviceLocation(String deviceLocation) { this.deviceLocation = deviceLocation; }
        public String getVerificationType() { return verificationType; }
        public void setVerificationType(String verificationType) { this.verificationType = verificationType; }
        public String getVerificationResult() { return verificationResult; }
        public void setVerificationResult(String verificationResult) { this.verificationResult = verificationResult; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final BiometricVerificationHistoryDto dto = new BiometricVerificationHistoryDto();
            public Builder eventId(Long eventId) { dto.setEventId(eventId); return this; }
            public Builder deviceLocation(String deviceLocation) { dto.setDeviceLocation(deviceLocation); return this; }
            public Builder verificationType(String verificationType) { dto.setVerificationType(verificationType); return this; }
            public Builder verificationResult(String verificationResult) { dto.setVerificationResult(verificationResult); return this; }
            public Builder timestamp(LocalDateTime timestamp) { dto.setTimestamp(timestamp); return this; }
            public BiometricVerificationHistoryDto build() { return dto; }
        }
    }

    public static class SubjectAvgDto {
        private String subjectName;
        private BigDecimal averagePercentage;

        public SubjectAvgDto() {}
        public SubjectAvgDto(String subjectName, BigDecimal averagePercentage) {
            this.subjectName = subjectName;
            this.averagePercentage = averagePercentage;
        }

        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public BigDecimal getAveragePercentage() { return averagePercentage; }
        public void setAveragePercentage(BigDecimal averagePercentage) { this.averagePercentage = averagePercentage; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final SubjectAvgDto dto = new SubjectAvgDto();
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder averagePercentage(BigDecimal averagePercentage) { dto.setAveragePercentage(averagePercentage); return this; }
            public SubjectAvgDto build() { return dto; }
        }
    }

    public static class DepartmentAvgDto {
        private String departmentName;
        private BigDecimal averagePercentage;

        public DepartmentAvgDto() {}
        public DepartmentAvgDto(String departmentName, BigDecimal averagePercentage) {
            this.departmentName = departmentName;
            this.averagePercentage = averagePercentage;
        }

        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
        public BigDecimal getAveragePercentage() { return averagePercentage; }
        public void setAveragePercentage(BigDecimal averagePercentage) { this.averagePercentage = averagePercentage; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final DepartmentAvgDto dto = new DepartmentAvgDto();
            public Builder departmentName(String departmentName) { dto.setDepartmentName(departmentName); return this; }
            public Builder averagePercentage(BigDecimal averagePercentage) { dto.setAveragePercentage(averagePercentage); return this; }
            public DepartmentAvgDto build() { return dto; }
        }
    }

    public static class DailyTrendDto {
        private String day;
        private Integer present;
        private Integer absent;
        private BigDecimal percentage;

        public DailyTrendDto() {}
        public DailyTrendDto(String day, Integer present, Integer absent, BigDecimal percentage) {
            this.day = day;
            this.present = present;
            this.absent = absent;
            this.percentage = percentage;
        }

        public String getDay() { return day; }
        public void setDay(String day) { this.day = day; }
        public Integer getPresent() { return present; }
        public void setPresent(Integer present) { this.present = present; }
        public Integer getAbsent() { return absent; }
        public void setAbsent(Integer absent) { this.absent = absent; }
        public BigDecimal getPercentage() { return percentage; }
        public void setPercentage(BigDecimal percentage) { this.percentage = percentage; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final DailyTrendDto dto = new DailyTrendDto();
            public Builder day(String day) { dto.setDay(day); return this; }
            public Builder present(Integer present) { dto.setPresent(present); return this; }
            public Builder absent(Integer absent) { dto.setAbsent(absent); return this; }
            public Builder percentage(BigDecimal percentage) { dto.setPercentage(percentage); return this; }
            public DailyTrendDto build() { return dto; }
        }
    }
}
