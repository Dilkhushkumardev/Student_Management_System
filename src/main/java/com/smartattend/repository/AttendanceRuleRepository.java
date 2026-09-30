package com.smartattend.repository;

import com.smartattend.entity.AttendanceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRuleRepository extends JpaRepository<AttendanceRule, Long> {
    Optional<AttendanceRule> findByIsActiveTrue();
    Optional<AttendanceRule> findByRuleName(String ruleName);
}
