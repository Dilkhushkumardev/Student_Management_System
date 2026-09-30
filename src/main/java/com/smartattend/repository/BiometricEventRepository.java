package com.smartattend.repository;

import com.smartattend.entity.BiometricEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BiometricEventRepository extends JpaRepository<BiometricEvent, Long> {
    List<BiometricEvent> findByAttendanceSessionId(Long attendanceSessionId);
    List<BiometricEvent> findByStudentId(Long studentId);
    List<BiometricEvent> findByDeviceId(Long deviceId);
    List<BiometricEvent> findByIsProcessedFalse();

    @Query("SELECT e FROM BiometricEvent e WHERE e.attendanceSession.id = :sessionId " +
           "ORDER BY e.eventTimestamp DESC")
    List<BiometricEvent> findRecentEventsForSession(@Param("sessionId") Long sessionId, Pageable pageable);

    @Query("SELECT COUNT(e) > 0 FROM BiometricEvent e WHERE e.biometricUserId = :biometricUserId " +
           "AND e.attendanceSession.id = :sessionId AND e.eventTimestamp >= :since")
    Boolean existsRecentEventForSession(@Param("biometricUserId") String biometricUserId,
                                        @Param("sessionId") Long sessionId,
                                        @Param("since") LocalDateTime since);

    Page<BiometricEvent> findAllByOrderByEventTimestampDesc(Pageable pageable);
}
