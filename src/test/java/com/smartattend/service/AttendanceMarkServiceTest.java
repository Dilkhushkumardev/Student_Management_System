package com.smartattend.service;

import com.smartattend.entity.AttendanceMarkRule;
import com.smartattend.repository.AttendanceMarkRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceMarkServiceTest {

    @Mock
    private AttendanceMarkRuleRepository attendanceMarkRuleRepository;

    @InjectMocks
    private AttendanceMarkService attendanceMarkService;

    private List<AttendanceMarkRule> rules;

    @BeforeEach
    void setUp() {
        AttendanceMarkRule r1 = new AttendanceMarkRule(1L, new BigDecimal("75.00"), new BigDecimal("80.99"), 1, "75-80% Slab", true);
        AttendanceMarkRule r2 = new AttendanceMarkRule(2L, new BigDecimal("81.00"), new BigDecimal("85.99"), 2, "81-85% Slab", true);
        AttendanceMarkRule r3 = new AttendanceMarkRule(3L, new BigDecimal("86.00"), new BigDecimal("90.99"), 3, "86-90% Slab", true);
        AttendanceMarkRule r4 = new AttendanceMarkRule(4L, new BigDecimal("91.00"), new BigDecimal("95.99"), 4, "91-95% Slab", true);
        AttendanceMarkRule r5 = new AttendanceMarkRule(5L, new BigDecimal("96.00"), new BigDecimal("100.00"), 5, "96-100% Slab", true);

        rules = Arrays.asList(r1, r2, r3, r4, r5);
    }

    @Test
    @DisplayName("BEU 5-Mark Slab: 96-100% awards 5 marks")
    void testCalculateMarks_FiveMarks() {
        when(attendanceMarkRuleRepository.findByIsActiveTrueOrderByMinPercentageAsc()).thenReturn(rules);

        assertEquals(5, attendanceMarkService.calculateMarks(new BigDecimal("100.00")));
        assertEquals(5, attendanceMarkService.calculateMarks(new BigDecimal("97.50")));
        assertEquals(5, attendanceMarkService.calculateMarks(new BigDecimal("96.00")));
    }

    @Test
    @DisplayName("BEU 5-Mark Slab: 91-95% awards 4 marks")
    void testCalculateMarks_FourMarks() {
        when(attendanceMarkRuleRepository.findByIsActiveTrueOrderByMinPercentageAsc()).thenReturn(rules);

        assertEquals(4, attendanceMarkService.calculateMarks(new BigDecimal("95.00")));
        assertEquals(4, attendanceMarkService.calculateMarks(new BigDecimal("92.30")));
        assertEquals(4, attendanceMarkService.calculateMarks(new BigDecimal("91.00")));
    }

    @Test
    @DisplayName("BEU 5-Mark Slab: 86-90% awards 3 marks")
    void testCalculateMarks_ThreeMarks() {
        when(attendanceMarkRuleRepository.findByIsActiveTrueOrderByMinPercentageAsc()).thenReturn(rules);

        assertEquals(3, attendanceMarkService.calculateMarks(new BigDecimal("90.00")));
        assertEquals(3, attendanceMarkService.calculateMarks(new BigDecimal("87.50")));
        assertEquals(3, attendanceMarkService.calculateMarks(new BigDecimal("86.00")));
    }

    @Test
    @DisplayName("BEU 5-Mark Slab: 81-85% awards 2 marks")
    void testCalculateMarks_TwoMarks() {
        when(attendanceMarkRuleRepository.findByIsActiveTrueOrderByMinPercentageAsc()).thenReturn(rules);

        assertEquals(2, attendanceMarkService.calculateMarks(new BigDecimal("85.00")));
        assertEquals(2, attendanceMarkService.calculateMarks(new BigDecimal("82.35")));
        assertEquals(2, attendanceMarkService.calculateMarks(new BigDecimal("81.00")));
    }

    @Test
    @DisplayName("BEU 5-Mark Slab: 75-80% awards 1 mark")
    void testCalculateMarks_OneMark() {
        when(attendanceMarkRuleRepository.findByIsActiveTrueOrderByMinPercentageAsc()).thenReturn(rules);

        assertEquals(1, attendanceMarkService.calculateMarks(new BigDecimal("80.00")));
        assertEquals(1, attendanceMarkService.calculateMarks(new BigDecimal("76.50")));
        assertEquals(1, attendanceMarkService.calculateMarks(new BigDecimal("75.00")));
    }

    @Test
    @DisplayName("BEU 5-Mark Slab: Below 75% awards 0 marks")
    void testCalculateMarks_ZeroMarks() {
        assertEquals(0, attendanceMarkService.calculateMarks(new BigDecimal("74.99")));
        assertEquals(0, attendanceMarkService.calculateMarks(new BigDecimal("68.42")));
        assertEquals(0, attendanceMarkService.calculateMarks(new BigDecimal("0.00")));
        assertEquals(0, attendanceMarkService.calculateMarks(null));
    }

    @Test
    @DisplayName("BEU 5-Mark Slab: Intermediate decimals correctly match active slabs")
    void testCalculateMarks_IntermediateDecimals() {
        when(attendanceMarkRuleRepository.findByIsActiveTrueOrderByMinPercentageAsc()).thenReturn(rules);

        assertEquals(1, attendanceMarkService.calculateMarks(new BigDecimal("80.50")));
        assertEquals(2, attendanceMarkService.calculateMarks(new BigDecimal("85.50")));
        assertEquals(3, attendanceMarkService.calculateMarks(new BigDecimal("90.50")));
        assertEquals(4, attendanceMarkService.calculateMarks(new BigDecimal("95.50")));
    }

    @Test
    @DisplayName("BEU 5-Mark Slab: Fallback calculations when database rules list is empty")
    void testCalculateMarks_EmptyRulesFallback() {
        when(attendanceMarkRuleRepository.findByIsActiveTrueOrderByMinPercentageAsc()).thenReturn(List.of());

        assertEquals(5, attendanceMarkService.calculateMarks(new BigDecimal("98.00")));
        assertEquals(4, attendanceMarkService.calculateMarks(new BigDecimal("93.00")));
        assertEquals(3, attendanceMarkService.calculateMarks(new BigDecimal("88.00")));
        assertEquals(2, attendanceMarkService.calculateMarks(new BigDecimal("83.00")));
        assertEquals(1, attendanceMarkService.calculateMarks(new BigDecimal("77.00")));
        assertEquals(0, attendanceMarkService.calculateMarks(new BigDecimal("70.00")));
    }
}
