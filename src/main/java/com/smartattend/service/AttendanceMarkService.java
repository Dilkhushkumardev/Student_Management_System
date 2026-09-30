package com.smartattend.service;

import com.smartattend.entity.AttendanceMarkRule;
import com.smartattend.repository.AttendanceMarkRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AttendanceMarkService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceMarkService.class);
    private final AttendanceMarkRuleRepository attendanceMarkRuleRepository;

    public AttendanceMarkService(AttendanceMarkRuleRepository attendanceMarkRuleRepository) {
        this.attendanceMarkRuleRepository = attendanceMarkRuleRepository;
    }

    public int calculateMarks(BigDecimal percentage) {
        if (percentage == null || percentage.compareTo(BigDecimal.valueOf(75.00)) < 0) {
            return 0;
        }

        List<AttendanceMarkRule> rules = attendanceMarkRuleRepository.findByIsActiveTrueOrderByMinPercentageAsc();

        if (rules.isEmpty()) {
            double p = percentage.doubleValue();
            if (p >= 96.0) return 5;
            if (p >= 91.0) return 4;
            if (p >= 86.0) return 3;
            if (p >= 81.0) return 2;
            if (p >= 75.0) return 1;
            return 0;
        }

        BigDecimal pct = percentage.setScale(2, RoundingMode.HALF_UP);

        for (AttendanceMarkRule rule : rules) {
            if (pct.compareTo(rule.getMinPercentage()) >= 0 &&
                pct.compareTo(rule.getMaxPercentage()) <= 0) {
                return rule.getMarksAwarded();
            }
        }

        // Fallback for percentages above highest slab or boundary edge-cases
        double p = pct.doubleValue();
        if (p >= 96.0) return 5;
        if (p >= 91.0) return 4;
        if (p >= 86.0) return 3;
        if (p >= 81.0) return 2;
        if (p >= 75.0) return 1;

        return 0;
    }
}
