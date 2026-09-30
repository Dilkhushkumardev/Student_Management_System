package com.smartattend.dto;

import com.smartattend.enums.AttendanceMethod;
import com.smartattend.enums.AttendanceStatus;
import com.smartattend.enums.EligibilityStatus;
import com.smartattend.enums.SessionStatus;
import com.smartattend.enums.VerificationMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AttendanceDtoModels {

    public static class AttendanceSessionCreateRequest {
        @NotNull(message = "Subject ID is required")
        private Long subjectId;

        @NotNull(message = "Section ID is required")
        private Long sectionId;

        @NotNull(message = "Session date is required")
        private LocalDate sessionDate;

        @NotNull(message = "Start time is required")
        private LocalTime startTime;

        @NotNull(message = "End time is required")
        private LocalTime endTime;

        private Integer periodNumber;
        private String roomNo;
        private VerificationMode verificationMode;
        private String notes;

        public AttendanceSessionCreateRequest() {}

        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
        public LocalDate getSessionDate() { return sessionDate; }
        public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }
        public LocalTime getStartTime() { return startTime; }
        public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
        public LocalTime getEndTime() { return endTime; }
        public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
        public Integer getPeriodNumber() { return periodNumber; }
        public void setPeriodNumber(Integer periodNumber) { this.periodNumber = periodNumber; }
        public String getRoomNo() { return roomNo; }
        public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
        public VerificationMode getVerificationMode() { return verificationMode; }
        public void setVerificationMode(VerificationMode verificationMode) { this.verificationMode = verificationMode; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class AttendanceSessionDto {
        private Long id;
        private String sessionCode;
        private Long subjectId;
        private String courseCode;
        private String subjectName;
        private Long facultyId;
        private String facultyName;
        private Long sectionId;
        private String sectionName;
        private LocalDate sessionDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer periodNumber;
        private String roomNo;
        private SessionStatus status;
        private VerificationMode verificationMode;
        private Boolean isLocked;
        private LocalDateTime lockedAt;
        private String notes;
        private Integer totalStudents;
        private Integer presentCount;
        private Integer absentCount;
        private Integer lateCount;
        private Integer excusedCount;
        private BigDecimal attendancePercentage;

        public AttendanceSessionDto() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getSessionCode() { return sessionCode; }
        public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public Long getFacultyId() { return facultyId; }
        public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
        public String getFacultyName() { return facultyName; }
        public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
        public String getSectionName() { return sectionName; }
        public void setSectionName(String sectionName) { this.sectionName = sectionName; }
        public LocalDate getSessionDate() { return sessionDate; }
        public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }
        public LocalTime getStartTime() { return startTime; }
        public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
        public LocalTime getEndTime() { return endTime; }
        public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
        public Integer getPeriodNumber() { return periodNumber; }
        public void setPeriodNumber(Integer periodNumber) { this.periodNumber = periodNumber; }
        public String getRoomNo() { return roomNo; }
        public void setRoomNo(String roomNo) { this.roomNo = roomNo; }
        public SessionStatus getStatus() { return status; }
        public void setStatus(SessionStatus status) { this.status = status; }
        public VerificationMode getVerificationMode() { return verificationMode; }
        public void setVerificationMode(VerificationMode verificationMode) { this.verificationMode = verificationMode; }
        public Boolean getIsLocked() { return isLocked; }
        public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }
        public LocalDateTime getLockedAt() { return lockedAt; }
        public void setLockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public Integer getTotalStudents() { return totalStudents; }
        public void setTotalStudents(Integer totalStudents) { this.totalStudents = totalStudents; }
        public Integer getPresentCount() { return presentCount; }
        public void setPresentCount(Integer presentCount) { this.presentCount = presentCount; }
        public Integer getAbsentCount() { return absentCount; }
        public void setAbsentCount(Integer absentCount) { this.absentCount = absentCount; }
        public Integer getLateCount() { return lateCount; }
        public void setLateCount(Integer lateCount) { this.lateCount = lateCount; }
        public Integer getExcusedCount() { return excusedCount; }
        public void setExcusedCount(Integer excusedCount) { this.excusedCount = excusedCount; }
        public BigDecimal getAttendancePercentage() { return attendancePercentage; }
        public void setAttendancePercentage(BigDecimal attendancePercentage) { this.attendancePercentage = attendancePercentage; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final AttendanceSessionDto dto = new AttendanceSessionDto();
            public Builder id(Long id) { dto.setId(id); return this; }
            public Builder sessionCode(String sessionCode) { dto.setSessionCode(sessionCode); return this; }
            public Builder subjectId(Long subjectId) { dto.setSubjectId(subjectId); return this; }
            public Builder courseCode(String courseCode) { dto.setCourseCode(courseCode); return this; }
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder facultyId(Long facultyId) { dto.setFacultyId(facultyId); return this; }
            public Builder facultyName(String facultyName) { dto.setFacultyName(facultyName); return this; }
            public Builder sectionId(Long sectionId) { dto.setSectionId(sectionId); return this; }
            public Builder sectionName(String sectionName) { dto.setSectionName(sectionName); return this; }
            public Builder sessionDate(LocalDate sessionDate) { dto.setSessionDate(sessionDate); return this; }
            public Builder startTime(LocalTime startTime) { dto.setStartTime(startTime); return this; }
            public Builder endTime(LocalTime endTime) { dto.setEndTime(endTime); return this; }
            public Builder periodNumber(Integer periodNumber) { dto.setPeriodNumber(periodNumber); return this; }
            public Builder roomNo(String roomNo) { dto.setRoomNo(roomNo); return this; }
            public Builder status(SessionStatus status) { dto.setStatus(status); return this; }
            public Builder verificationMode(VerificationMode verificationMode) { dto.setVerificationMode(verificationMode); return this; }
            public Builder isLocked(Boolean isLocked) { dto.setIsLocked(isLocked); return this; }
            public Builder lockedAt(LocalDateTime lockedAt) { dto.setLockedAt(lockedAt); return this; }
            public Builder notes(String notes) { dto.setNotes(notes); return this; }
            public Builder totalStudents(Integer totalStudents) { dto.setTotalStudents(totalStudents); return this; }
            public Builder presentCount(Integer presentCount) { dto.setPresentCount(presentCount); return this; }
            public Builder absentCount(Integer absentCount) { dto.setAbsentCount(absentCount); return this; }
            public Builder lateCount(Integer lateCount) { dto.setLateCount(lateCount); return this; }
            public Builder excusedCount(Integer excusedCount) { dto.setExcusedCount(excusedCount); return this; }
            public Builder attendancePercentage(BigDecimal attendancePercentage) { dto.setAttendancePercentage(attendancePercentage); return this; }
            public AttendanceSessionDto build() { return dto; }
        }
    }

    public static class AttendanceRecordDto {
        private Long id;
        private Long sessionId;
        private Long studentId;
        private String rollNo;
        private String studentName;
        private String universityRegNo;
        private String biometricId;
        private AttendanceStatus status;
        private AttendanceMethod method;
        private Long biometricEventId;
        private String remarks;
        private LocalDateTime markedAt;
        private Boolean isAdminOverride;
        private String overrideReason;

        public AttendanceRecordDto() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getSessionId() { return sessionId; }
        public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public String getRollNo() { return rollNo; }
        public void setRollNo(String rollNo) { this.rollNo = rollNo; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public String getUniversityRegNo() { return universityRegNo; }
        public void setUniversityRegNo(String universityRegNo) { this.universityRegNo = universityRegNo; }
        public String getBiometricId() { return biometricId; }
        public void setBiometricId(String biometricId) { this.biometricId = biometricId; }
        public AttendanceStatus getStatus() { return status; }
        public void setStatus(AttendanceStatus status) { this.status = status; }
        public AttendanceMethod getMethod() { return method; }
        public void setMethod(AttendanceMethod method) { this.method = method; }
        public Long getBiometricEventId() { return biometricEventId; }
        public void setBiometricEventId(Long biometricEventId) { this.biometricEventId = biometricEventId; }
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
        public LocalDateTime getMarkedAt() { return markedAt; }
        public void setMarkedAt(LocalDateTime markedAt) { this.markedAt = markedAt; }
        public Boolean getIsAdminOverride() { return isAdminOverride; }
        public void setIsAdminOverride(Boolean isAdminOverride) { this.isAdminOverride = isAdminOverride; }
        public String getOverrideReason() { return overrideReason; }
        public void setOverrideReason(String overrideReason) { this.overrideReason = overrideReason; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final AttendanceRecordDto dto = new AttendanceRecordDto();
            public Builder id(Long id) { dto.setId(id); return this; }
            public Builder sessionId(Long sessionId) { dto.setSessionId(sessionId); return this; }
            public Builder studentId(Long studentId) { dto.setStudentId(studentId); return this; }
            public Builder rollNo(String rollNo) { dto.setRollNo(rollNo); return this; }
            public Builder studentName(String studentName) { dto.setStudentName(studentName); return this; }
            public Builder universityRegNo(String universityRegNo) { dto.setUniversityRegNo(universityRegNo); return this; }
            public Builder biometricId(String biometricId) { dto.setBiometricId(biometricId); return this; }
            public Builder status(AttendanceStatus status) { dto.setStatus(status); return this; }
            public Builder method(AttendanceMethod method) { dto.setMethod(method); return this; }
            public Builder biometricEventId(Long biometricEventId) { dto.setBiometricEventId(biometricEventId); return this; }
            public Builder remarks(String remarks) { dto.setRemarks(remarks); return this; }
            public Builder markedAt(LocalDateTime markedAt) { dto.setMarkedAt(markedAt); return this; }
            public Builder isAdminOverride(Boolean isAdminOverride) { dto.setIsAdminOverride(isAdminOverride); return this; }
            public Builder overrideReason(String overrideReason) { dto.setOverrideReason(overrideReason); return this; }
            public AttendanceRecordDto build() { return dto; }
        }
    }

    public static class AttendanceRecordUpdateRequest {
        @NotNull(message = "Attendance status is required")
        private AttendanceStatus status;
        private String remarks;

        public AttendanceRecordUpdateRequest() {}
        public AttendanceStatus getStatus() { return status; }
        public void setStatus(AttendanceStatus status) { this.status = status; }
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }

    public static class AdminOverrideRequest {
        @NotNull(message = "Attendance status is required")
        private AttendanceStatus status;

        @NotBlank(message = "Override reason is mandatory for audit trail")
        private String overrideReason;

        private String remarks;

        public AdminOverrideRequest() {}
        public AttendanceStatus getStatus() { return status; }
        public void setStatus(AttendanceStatus status) { this.status = status; }
        public String getOverrideReason() { return overrideReason; }
        public void setOverrideReason(String overrideReason) { this.overrideReason = overrideReason; }
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }

    public static class BulkAttendanceMarkRequest {
        @NotNull(message = "Session ID is required")
        private Long sessionId;
        private List<StudentAttendanceItem> records;

        public BulkAttendanceMarkRequest() {}
        public Long getSessionId() { return sessionId; }
        public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
        public List<StudentAttendanceItem> getRecords() { return records; }
        public void setRecords(List<StudentAttendanceItem> records) { this.records = records; }
    }

    public static class StudentAttendanceItem {
        private Long studentId;
        private AttendanceStatus status;
        private AttendanceMethod method;
        private String remarks;

        public StudentAttendanceItem() {}
        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public AttendanceStatus getStatus() { return status; }
        public void setStatus(AttendanceStatus status) { this.status = status; }
        public AttendanceMethod getMethod() { return method; }
        public void setMethod(AttendanceMethod method) { this.method = method; }
        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }

    public static class AttendanceSummaryDto {
        private Long id;
        private Long studentId;
        private String rollNo;
        private String studentName;
        private Long subjectId;
        private String courseCode;
        private String subjectName;
        private Long semesterId;
        private Integer totalClasses;
        private Integer presentClasses;
        private Integer absentClasses;
        private Integer lateClasses;
        private Integer excusedClasses;
        private BigDecimal attendancePercentage;
        private Integer attendanceMarks;
        private EligibilityStatus eligibility;
        private AttendanceShortageCalculation shortageCalculation;
        private LocalDateTime lastCalculatedAt;

        public AttendanceSummaryDto() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getStudentId() { return studentId; }
        public void setStudentId(Long studentId) { this.studentId = studentId; }
        public String getRollNo() { return rollNo; }
        public void setRollNo(String rollNo) { this.rollNo = rollNo; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public Long getSemesterId() { return semesterId; }
        public void setSemesterId(Long semesterId) { this.semesterId = semesterId; }
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
        public AttendanceShortageCalculation getShortageCalculation() { return shortageCalculation; }
        public void setShortageCalculation(AttendanceShortageCalculation shortageCalculation) { this.shortageCalculation = shortageCalculation; }
        public LocalDateTime getLastCalculatedAt() { return lastCalculatedAt; }
        public void setLastCalculatedAt(LocalDateTime lastCalculatedAt) { this.lastCalculatedAt = lastCalculatedAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final AttendanceSummaryDto dto = new AttendanceSummaryDto();
            public Builder id(Long id) { dto.setId(id); return this; }
            public Builder studentId(Long studentId) { dto.setStudentId(studentId); return this; }
            public Builder rollNo(String rollNo) { dto.setRollNo(rollNo); return this; }
            public Builder studentName(String studentName) { dto.setStudentName(studentName); return this; }
            public Builder subjectId(Long subjectId) { dto.setSubjectId(subjectId); return this; }
            public Builder courseCode(String courseCode) { dto.setCourseCode(courseCode); return this; }
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder semesterId(Long semesterId) { dto.setSemesterId(semesterId); return this; }
            public Builder totalClasses(Integer totalClasses) { dto.setTotalClasses(totalClasses); return this; }
            public Builder presentClasses(Integer presentClasses) { dto.setPresentClasses(presentClasses); return this; }
            public Builder absentClasses(Integer absentClasses) { dto.setAbsentClasses(absentClasses); return this; }
            public Builder lateClasses(Integer lateClasses) { dto.setLateClasses(lateClasses); return this; }
            public Builder excusedClasses(Integer excusedClasses) { dto.setExcusedClasses(excusedClasses); return this; }
            public Builder attendancePercentage(BigDecimal attendancePercentage) { dto.setAttendancePercentage(attendancePercentage); return this; }
            public Builder attendanceMarks(Integer attendanceMarks) { dto.setAttendanceMarks(attendanceMarks); return this; }
            public Builder eligibility(EligibilityStatus eligibility) { dto.setEligibility(eligibility); return this; }
            public Builder shortageCalculation(AttendanceShortageCalculation shortageCalculation) { dto.setShortageCalculation(shortageCalculation); return this; }
            public Builder lastCalculatedAt(LocalDateTime lastCalculatedAt) { dto.setLastCalculatedAt(lastCalculatedAt); return this; }
            public AttendanceSummaryDto build() { return dto; }
        }
    }

    public static class AttendanceShortageCalculation {
        private BigDecimal currentPercentage;
        private Integer classesAttended;
        private Integer totalClasses;
        private Integer classesNeededFor75;
        private Integer maxClassesCanMissWhileAbove75;
        private Boolean isEligible;
        private Boolean isCondonable;
        private String statusBadgeColor;
        private String actionableAdvice;

        public AttendanceShortageCalculation() {}

        public BigDecimal getCurrentPercentage() { return currentPercentage; }
        public void setCurrentPercentage(BigDecimal currentPercentage) { this.currentPercentage = currentPercentage; }
        public Integer getClassesAttended() { return classesAttended; }
        public void setClassesAttended(Integer classesAttended) { this.classesAttended = classesAttended; }
        public Integer getTotalClasses() { return totalClasses; }
        public void setTotalClasses(Integer totalClasses) { this.totalClasses = totalClasses; }
        public Integer getClassesNeededFor75() { return classesNeededFor75; }
        public void setClassesNeededFor75(Integer classesNeededFor75) { this.classesNeededFor75 = classesNeededFor75; }
        public Integer getMaxClassesCanMissWhileAbove75() { return maxClassesCanMissWhileAbove75; }
        public void setMaxClassesCanMissWhileAbove75(Integer maxClassesCanMissWhileAbove75) { this.maxClassesCanMissWhileAbove75 = maxClassesCanMissWhileAbove75; }
        public Boolean getIsEligible() { return isEligible; }
        public void setIsEligible(Boolean isEligible) { this.isEligible = isEligible; }
        public Boolean getIsCondonable() { return isCondonable; }
        public void setIsCondonable(Boolean isCondonable) { this.isCondonable = isCondonable; }
        public String getStatusBadgeColor() { return statusBadgeColor; }
        public void setStatusBadgeColor(String statusBadgeColor) { this.statusBadgeColor = statusBadgeColor; }
        public String getActionableAdvice() { return actionableAdvice; }
        public void setActionableAdvice(String actionableAdvice) { this.actionableAdvice = actionableAdvice; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final AttendanceShortageCalculation c = new AttendanceShortageCalculation();
            public Builder currentPercentage(BigDecimal currentPercentage) { c.setCurrentPercentage(currentPercentage); return this; }
            public Builder classesAttended(Integer classesAttended) { c.setClassesAttended(classesAttended); return this; }
            public Builder totalClasses(Integer totalClasses) { c.setTotalClasses(totalClasses); return this; }
            public Builder classesNeededFor75(Integer classesNeededFor75) { c.setClassesNeededFor75(classesNeededFor75); return this; }
            public Builder maxClassesCanMissWhileAbove75(Integer maxClassesCanMissWhileAbove75) { c.setMaxClassesCanMissWhileAbove75(maxClassesCanMissWhileAbove75); return this; }
            public Builder isEligible(Boolean isEligible) { c.setIsEligible(isEligible); return this; }
            public Builder isCondonable(Boolean isCondonable) { c.setIsCondonable(isCondonable); return this; }
            public Builder statusBadgeColor(String statusBadgeColor) { c.setStatusBadgeColor(statusBadgeColor); return this; }
            public Builder actionableAdvice(String actionableAdvice) { c.setActionableAdvice(actionableAdvice); return this; }
            public AttendanceShortageCalculation build() { return c; }
        }
    }

    public static class AttendanceCardDto {
        private Long subjectId;
        private String courseCode;
        private String subjectName;
        private String subjectType;
        private BigDecimal attendancePercentage;
        private Integer presentClasses;
        private Integer absentClasses;
        private Integer totalClasses;
        private Integer attendanceMarks;
        private EligibilityStatus eligibility;
        private String statusBadgeColor;
        private String nextClassSchedule;
        private AttendanceShortageCalculation calculation;

        public AttendanceCardDto() {}

        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public String getSubjectType() { return subjectType; }
        public void setSubjectType(String subjectType) { this.subjectType = subjectType; }
        public BigDecimal getAttendancePercentage() { return attendancePercentage; }
        public void setAttendancePercentage(BigDecimal attendancePercentage) { this.attendancePercentage = attendancePercentage; }
        public Integer getPresentClasses() { return presentClasses; }
        public void setPresentClasses(Integer presentClasses) { this.presentClasses = presentClasses; }
        public Integer getAbsentClasses() { return absentClasses; }
        public void setAbsentClasses(Integer absentClasses) { this.absentClasses = absentClasses; }
        public Integer getTotalClasses() { return totalClasses; }
        public void setTotalClasses(Integer totalClasses) { this.totalClasses = totalClasses; }
        public Integer getAttendanceMarks() { return attendanceMarks; }
        public void setAttendanceMarks(Integer attendanceMarks) { this.attendanceMarks = attendanceMarks; }
        public EligibilityStatus getEligibility() { return eligibility; }
        public void setEligibility(EligibilityStatus eligibility) { this.eligibility = eligibility; }
        public String getStatusBadgeColor() { return statusBadgeColor; }
        public void setStatusBadgeColor(String statusBadgeColor) { this.statusBadgeColor = statusBadgeColor; }
        public String getNextClassSchedule() { return nextClassSchedule; }
        public void setNextClassSchedule(String nextClassSchedule) { this.nextClassSchedule = nextClassSchedule; }
        public AttendanceShortageCalculation getCalculation() { return calculation; }
        public void setCalculation(AttendanceShortageCalculation calculation) { this.calculation = calculation; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final AttendanceCardDto dto = new AttendanceCardDto();
            public Builder subjectId(Long subjectId) { dto.setSubjectId(subjectId); return this; }
            public Builder courseCode(String courseCode) { dto.setCourseCode(courseCode); return this; }
            public Builder subjectName(String subjectName) { dto.setSubjectName(subjectName); return this; }
            public Builder subjectType(String subjectType) { dto.setSubjectType(subjectType); return this; }
            public Builder attendancePercentage(BigDecimal attendancePercentage) { dto.setAttendancePercentage(attendancePercentage); return this; }
            public Builder presentClasses(Integer presentClasses) { dto.setPresentClasses(presentClasses); return this; }
            public Builder absentClasses(Integer absentClasses) { dto.setAbsentClasses(absentClasses); return this; }
            public Builder totalClasses(Integer totalClasses) { dto.setTotalClasses(totalClasses); return this; }
            public Builder attendanceMarks(Integer attendanceMarks) { dto.setAttendanceMarks(attendanceMarks); return this; }
            public Builder eligibility(EligibilityStatus eligibility) { dto.setEligibility(eligibility); return this; }
            public Builder statusBadgeColor(String statusBadgeColor) { dto.setStatusBadgeColor(statusBadgeColor); return this; }
            public Builder nextClassSchedule(String nextClassSchedule) { dto.setNextClassSchedule(nextClassSchedule); return this; }
            public Builder calculation(AttendanceShortageCalculation calculation) { dto.setCalculation(calculation); return this; }
            public AttendanceCardDto build() { return dto; }
        }
    }
}
