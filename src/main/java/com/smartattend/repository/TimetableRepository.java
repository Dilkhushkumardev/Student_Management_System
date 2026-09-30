package com.smartattend.repository;

import com.smartattend.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findBySectionIdAndIsActiveTrue(Long sectionId);
    List<Timetable> findByFacultyIdAndIsActiveTrue(Long facultyId);
    List<Timetable> findByDayOfWeekAndSectionIdAndIsActiveTrue(String dayOfWeek, Long sectionId);

    @Query("SELECT t FROM Timetable t WHERE t.dayOfWeek = :dayOfWeek " +
           "AND :currentTime BETWEEN t.startTime AND t.endTime " +
           "AND t.isActive = TRUE")
    List<Timetable> findActiveSlots(@Param("dayOfWeek") String dayOfWeek, @Param("currentTime") LocalTime currentTime);

    @Query("SELECT t FROM Timetable t WHERE t.dayOfWeek = :dayOfWeek " +
           "AND :currentTime BETWEEN t.startTime AND t.endTime " +
           "AND t.faculty.id = :facultyId AND t.isActive = TRUE")
    Optional<Timetable> findCurrentSlotForFaculty(@Param("dayOfWeek") String dayOfWeek,
                                                  @Param("currentTime") LocalTime currentTime,
                                                  @Param("facultyId") Long facultyId);
}
