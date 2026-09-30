package com.smartattend.service;

import com.smartattend.dto.AttendanceDtoModels.AttendanceShortageCalculation;
import com.smartattend.entity.AttendanceRule;
import com.smartattend.enums.EligibilityStatus;
import com.smartattend.repository.AttendanceRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AttendanceCalculationService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceCalculationService.class);
    private final AttendanceRuleRepository attendanceRuleRepository;

    public AttendanceCalculationService(AttendanceRuleRepository attendanceRuleRepository) {
        this.attendanceRuleRepository = attendanceRuleRepository;
    }

    public BigDecimal calculatePercentage(int present, int late, int excused, int total, BigDecimal lateWeight) {
        if (total <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal weight = lateWeight != null ? lateWeight : BigDecimal.ONE;
        BigDecimal effectivePresent = BigDecimal.valueOf(present)
                .add(BigDecimal.valueOf(late).multiply(weight));

        BigDecimal percentage = effectivePresent
                .multiply(BigDecimal.valueOf(100.0))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);

        if (percentage.compareTo(BigDecimal.valueOf(100.0)) > 0) {
            percentage = BigDecimal.valueOf(100.00);
        }
        return percentage;
    }

    public AttendanceShortageCalculation calculateShortage(int presentClasses, int lateClasses, int totalClasses) {
        AttendanceRule rule = attendanceRuleRepository.findByIsActiveTrue()
                .orElseGet(() -> {
                    AttendanceRule defaultRule = new AttendanceRule();
                    defaultRule.setMinPercentageRequired(BigDecimal.valueOf(75.00));
                    defaultRule.setCondonationPercentageAllowed(BigDecimal.valueOf(15.00));
                    defaultRule.setLateAsPresentWeight(BigDecimal.ONE);
                    return defaultRule;
                });

        BigDecimal minReq = rule.getMinPercentageRequired() != null ? rule.getMinPercentageRequired() : BigDecimal.valueOf(75.00);
        BigDecimal condonation = rule.getCondonationPercentageAllowed() != null ? rule.getCondonationPercentageAllowed() : BigDecimal.valueOf(15.00);
        BigDecimal medicalThreshold = minReq.subtract(condonation);

        BigDecimal lateWeight = rule.getLateAsPresentWeight() != null ? rule.getLateAsPresentWeight() : BigDecimal.ONE;
        BigDecimal currentPercentage = calculatePercentage(presentClasses, lateClasses, 0, totalClasses, lateWeight);

        double attended = presentClasses + (lateClasses * lateWeight.doubleValue());
        double targetFrac = minReq.doubleValue() / 100.0;

        int classesNeeded = 0;
        int maxCanMiss = 0;
        String advice;
        String badgeColor;
        boolean isEligible = currentPercentage.compareTo(minReq) >= 0;
        boolean isCondonable = !isEligible && currentPercentage.compareTo(medicalThreshold) >= 0;

        if (totalClasses == 0) {
            advice = "No classes conducted yet. Maintain consistent attendance from class 1.";
            badgeColor = "GREEN";
        } else if (isEligible) {
            double m = (attended / targetFrac) - totalClasses;
            maxCanMiss = Math.max(0, (int) Math.floor(m));
            badgeColor = "GREEN";
            if (maxCanMiss > 0) {
                advice = String.format("Excellent! You can miss up to %d class(es) and remain at or above 75%%.", maxCanMiss);
            } else {
                advice = "Attendance is at/above 75%. Do not miss the next class to avoid falling into shortage.";
            }
        } else {
            double numerator = (targetFrac * totalClasses) - attended;
            double denominator = 1.0 - targetFrac;
            double k = numerator / denominator;
            classesNeeded = Math.max(1, (int) Math.ceil(k));

            if (isCondonable) {
                badgeColor = "YELLOW";
                advice = String.format("Attendance shortage! Attend the next %d consecutive class(es) without absence to reach 75%%. Eligible for Principal medical condonation.", classesNeeded);
            } else {
                badgeColor = "RED";
                advice = String.format("Critical attendance shortage (< 60%%)! Attend the next %d consecutive class(es) to reach 75%%. Risk of being debarred from exams.", classesNeeded);
            }
        }

        return AttendanceShortageCalculation.builder()
                .currentPercentage(currentPercentage)
                .classesAttended(presentClasses + lateClasses)
                .totalClasses(totalClasses)
                .classesNeededFor75(classesNeeded)
                .maxClassesCanMissWhileAbove75(maxCanMiss)
                .isEligible(isEligible)
                .isCondonable(isCondonable)
                .statusBadgeColor(badgeColor)
                .actionableAdvice(advice)
                .build();
    }

    public EligibilityStatus determineEligibility(BigDecimal percentage) {
        if (percentage == null) {
            return EligibilityStatus.SHORTAGE;
        }
        if (percentage.compareTo(BigDecimal.valueOf(75.00)) >= 0) {
            return EligibilityStatus.ELIGIBLE;
        } else if (percentage.compareTo(BigDecimal.valueOf(60.00)) >= 0) {
            return EligibilityStatus.CONDONABLE_MEDICAL;
        } else {
            return EligibilityStatus.SHORTAGE;
        }
    }
}
