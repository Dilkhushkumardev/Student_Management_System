package com.smartattend.service;

import com.smartattend.dto.AttendanceDtoModels.AttendanceShortageCalculation;
import com.smartattend.entity.AttendanceRule;
import com.smartattend.enums.EligibilityStatus;
import com.smartattend.repository.AttendanceRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceCalculationServiceTest {

    @Mock
    private AttendanceRuleRepository attendanceRuleRepository;

    @InjectMocks
    private AttendanceCalculationService calculationService;

    private AttendanceRule defaultRule;

    @BeforeEach
    void setUp() {
        defaultRule = new AttendanceRule();
        defaultRule.setId(1L);
        defaultRule.setRuleName("BEU Standard 75% Attendance Rule");
        defaultRule.setMinPercentageRequired(new BigDecimal("75.00"));
        defaultRule.setCondonationPercentageAllowed(new BigDecimal("15.00"));
        defaultRule.setLateAsPresentWeight(BigDecimal.ONE);
        defaultRule.setIsActive(true);
    }

    @Test
    @DisplayName("Calculate percentage: 16 present out of 20 = 80.00%")
    void testCalculatePercentage_Normal() {
        BigDecimal pct = calculationService.calculatePercentage(16, 0, 0, 20, BigDecimal.ONE);
        assertEquals(new BigDecimal("80.00"), pct);
    }

    @Test
    @DisplayName("Calculate percentage with 0 total classes returns 0.00%")
    void testCalculatePercentage_ZeroTotal() {
        BigDecimal pct = calculationService.calculatePercentage(0, 0, 0, 0, BigDecimal.ONE);
        assertEquals(new BigDecimal("0.00"), pct);
    }

    @Test
    @DisplayName("Shortage Calculator: 13 present out of 19 = 68.42% requires 5 consecutive classes for 75%")
    void testCalculateShortage_Below75Percent() {
        when(attendanceRuleRepository.findByIsActiveTrue()).thenReturn(Optional.of(defaultRule));

        AttendanceShortageCalculation calc = calculationService.calculateShortage(13, 0, 19);

        assertNotNull(calc);
        assertFalse(calc.getIsEligible());
        assertTrue(calc.getIsCondonable()); // 68.42% >= 60.00% (medical condonation)
        assertEquals(new BigDecimal("68.42"), calc.getCurrentPercentage());
        assertEquals(5, calc.getClassesNeededFor75());
        assertEquals(0, calc.getMaxClassesCanMissWhileAbove75());
        assertEquals("YELLOW", calc.getStatusBadgeColor());
        assertTrue(calc.getActionableAdvice().contains("5 consecutive class(es)"));
    }

    @Test
    @DisplayName("Margin Calculator: 18 present out of 20 = 90.00% allows missing up to 4 classes")
    void testCalculateShortage_Above75Percent() {
        when(attendanceRuleRepository.findByIsActiveTrue()).thenReturn(Optional.of(defaultRule));

        AttendanceShortageCalculation calc = calculationService.calculateShortage(18, 0, 20);

        assertNotNull(calc);
        assertTrue(calc.getIsEligible());
        assertEquals(new BigDecimal("90.00"), calc.getCurrentPercentage());
        assertEquals(0, calc.getClassesNeededFor75());
        assertEquals(4, calc.getMaxClassesCanMissWhileAbove75());
        assertEquals("GREEN", calc.getStatusBadgeColor());
        assertTrue(calc.getActionableAdvice().contains("4 class(es)"));
    }

    @Test
    @DisplayName("Eligibility determination: >=75% ELIGIBLE, 60-74.99% CONDONABLE_MEDICAL, <60% SHORTAGE")
    void testDetermineEligibility() {
        assertEquals(EligibilityStatus.ELIGIBLE, calculationService.determineEligibility(new BigDecimal("85.00")));
        assertEquals(EligibilityStatus.ELIGIBLE, calculationService.determineEligibility(new BigDecimal("75.00")));
        assertEquals(EligibilityStatus.CONDONABLE_MEDICAL, calculationService.determineEligibility(new BigDecimal("68.42")));
        assertEquals(EligibilityStatus.CONDONABLE_MEDICAL, calculationService.determineEligibility(new BigDecimal("60.00")));
        assertEquals(EligibilityStatus.SHORTAGE, calculationService.determineEligibility(new BigDecimal("55.00")));
        assertEquals(EligibilityStatus.SHORTAGE, calculationService.determineEligibility(new BigDecimal("0.00")));
    }
}
