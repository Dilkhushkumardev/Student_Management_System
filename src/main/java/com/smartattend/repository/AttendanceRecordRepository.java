package com.smartattend.repository;

import com.smartattend.entity.AttendanceRecord;
import com.smartattend.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findBySessionId(Long sessionId);
    Optional<AttendanceRecord> findBySessionIdAndStudentId(Long sessionId, Long studentId);
    Boolean existsBySessionIdAndStudentId(Long sessionId, Long studentId);

    List<AttendanceRecord> findByStudentId(Long studentId);

    @Query("SELECT r FROM AttendanceRecord r WHERE r.student.id = :studentId " +
           "AND r.session.subject.id = :subjectId ORDER BY r.session.sessionDate DESC")
    List<AttendanceRecord> findByStudentIdAndSubjectId(@Param("studentId") Long studentId,
                                                      @Param("subjectId") Long subjectId);

    @Query("SELECT r FROM AttendanceRecord r WHERE r.student.id = :studentId " +
           "AND r.session.sessionDate = :date")
    List<AttendanceRecord> findByStudentIdAndDate(@Param("studentId") Long studentId,
                                                 @Param("date") LocalDate date);

    @Query("SELECT r FROM AttendanceRecord r WHERE r.session.sessionDate = :date")
    List<AttendanceRecord> findByDate(@Param("date") LocalDate date);

    @Query("SELECT r.status, COUNT(r) FROM AttendanceRecord r WHERE r.session.id = :sessionId GROUP BY r.status")
    List<Object[]> countStatusBySessionId(@Param("sessionId") Long sessionId);

    @Query("SELECT r.status, COUNT(r) FROM AttendanceRecord r WHERE r.session.sessionDate = :date GROUP BY r.status")
    List<Object[]> countStatusByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(r) FROM AttendanceRecord r WHERE r.student.id = :studentId " +
           "AND r.session.subject.id = :subjectId AND r.status = :status")
    long countByStudentAndSubjectAndStatus(@Param("studentId") Long studentId,
                                          @Param("subjectId") Long subjectId,
                                          @Param("status") AttendanceStatus status);
}
