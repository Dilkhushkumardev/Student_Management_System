package com.smartattend.repository;

import com.smartattend.entity.AttendanceMarkRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceMarkRuleRepository extends JpaRepository<AttendanceMarkRule, Long> {
    List<AttendanceMarkRule> findByIsActiveTrueOrderByMinPercentageAsc();

    @Query("SELECT r FROM AttendanceMarkRule r WHERE r.isActive = TRUE " +
           "AND :percentage >= r.minPercentage AND :percentage <= r.maxPercentage")
    Optional<AttendanceMarkRule> findMatchingRule(@Param("percentage") BigDecimal percentage);
}
