package com.smartattend.repository;

import com.smartattend.entity.AttendanceSummary;
import com.smartattend.enums.EligibilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceSummaryRepository extends JpaRepository<AttendanceSummary, Long> {
    List<AttendanceSummary> findByStudentId(Long studentId);
    List<AttendanceSummary> findBySubjectId(Long subjectId);
    List<AttendanceSummary> findBySemesterId(Long semesterId);
    Optional<AttendanceSummary> findByStudentIdAndSubjectIdAndSemesterId(Long studentId, Long subjectId, Long semesterId);

    @Query("SELECT s FROM AttendanceSummary s WHERE s.student.id = :studentId")
    List<AttendanceSummary> findAllByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT s FROM AttendanceSummary s WHERE s.subject.id = :subjectId")
    List<AttendanceSummary> findAllBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT s FROM AttendanceSummary s WHERE s.eligibility = 'SHORTAGE'")
    List<AttendanceSummary> findShortageSummaries();

    @Query("SELECT COUNT(DISTINCT s.student.id) FROM AttendanceSummary s WHERE s.attendancePercentage < 75.00")
    long countStudentsBelow75();

    @Query("SELECT AVG(s.attendancePercentage) FROM AttendanceSummary s")
    BigDecimal findAverageAttendancePercentage();

    @Query("SELECT s.subject.id, s.subject.subjectName, AVG(s.attendancePercentage) " +
           "FROM AttendanceSummary s GROUP BY s.subject.id, s.subject.subjectName")
    List<Object[]> getSubjectWiseAverageAttendance();

    @Query("SELECT s.student.department.name, AVG(s.attendancePercentage) " +
           "FROM AttendanceSummary s GROUP BY s.student.department.name")
    List<Object[]> getDepartmentWiseAverageAttendance();
}
