package com.smartattend.repository;

import com.smartattend.entity.AttendanceSession;
import com.smartattend.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    Optional<AttendanceSession> findBySessionCode(String sessionCode);
    List<AttendanceSession> findByStatus(SessionStatus status);
    List<AttendanceSession> findByFacultyId(Long facultyId);
    List<AttendanceSession> findByFacultyIdAndSessionDate(Long facultyId, LocalDate sessionDate);
    List<AttendanceSession> findBySubjectIdAndSectionId(Long subjectId, Long sectionId);
    List<AttendanceSession> findBySessionDate(LocalDate sessionDate);

    @Query("SELECT s FROM AttendanceSession s WHERE s.status = 'ACTIVE' " +
           "AND (:facultyId IS NULL OR s.faculty.id = :facultyId)")
    List<AttendanceSession> findActiveSessions(@Param("facultyId") Long facultyId);

    @Query("SELECT s FROM AttendanceSession s WHERE " +
           "(:facultyId IS NULL OR s.faculty.id = :facultyId) " +
           "AND (:subjectId IS NULL OR s.subject.id = :subjectId) " +
           "AND (:sectionId IS NULL OR s.section.id = :sectionId) " +
           "AND (:startDate IS NULL OR s.sessionDate >= :startDate) " +
           "AND (:endDate IS NULL OR s.sessionDate <= :endDate) " +
           "AND (:status IS NULL OR s.status = :status)")
    Page<AttendanceSession> searchSessions(@Param("facultyId") Long facultyId,
                                           @Param("subjectId") Long subjectId,
                                           @Param("sectionId") Long sectionId,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate,
                                           @Param("status") SessionStatus status,
                                           Pageable pageable);

    @Query("SELECT COUNT(s) FROM AttendanceSession s WHERE s.status = 'ACTIVE'")
    long countActiveSessions();

    @Query("SELECT COUNT(s) FROM AttendanceSession s WHERE s.sessionDate = :date")
    long countSessionsOnDate(@Param("date") LocalDate date);
}
