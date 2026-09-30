package com.smartattend.repository;

import com.smartattend.entity.AcademicCalendar;
import com.smartattend.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AcademicCalendarRepository extends JpaRepository<AcademicCalendar, Long> {
    List<AcademicCalendar> findByAcademicSessionOrderByStartDateAsc(String academicSession);
    List<AcademicCalendar> findByEventType(EventType eventType);
    List<AcademicCalendar> findByIsActiveTrueOrderByStartDateAsc();
    List<AcademicCalendar> findByStartDateBetween(LocalDate start, LocalDate end);
}
