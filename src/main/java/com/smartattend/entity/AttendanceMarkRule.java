package com.smartattend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "attendance_mark_rules")
public class AttendanceMarkRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal minPercentage;

    @Column(name = "max_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal maxPercentage;

    @Column(name = "marks_awarded", nullable = false)
    private Integer marksAwarded;

    @Column(name = "rule_description", length = 255)
    private String ruleDescription;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public AttendanceMarkRule() {}

    public AttendanceMarkRule(Long id, BigDecimal minPercentage, BigDecimal maxPercentage, Integer marksAwarded, String ruleDescription, Boolean isActive) {
        this.id = id;
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
        this.marksAwarded = marksAwarded;
        this.ruleDescription = ruleDescription;
        this.isActive = isActive != null ? isActive : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getMinPercentage() { return minPercentage; }
    public void setMinPercentage(BigDecimal minPercentage) { this.minPercentage = minPercentage; }
    public BigDecimal getMaxPercentage() { return maxPercentage; }
    public void setMaxPercentage(BigDecimal maxPercentage) { this.maxPercentage = maxPercentage; }
    public Integer getMarksAwarded() { return marksAwarded; }
    public void setMarksAwarded(Integer marksAwarded) { this.marksAwarded = marksAwarded; }
    public String getRuleDescription() { return ruleDescription; }
    public void setRuleDescription(String ruleDescription) { this.ruleDescription = ruleDescription; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private BigDecimal minPercentage;
        private BigDecimal maxPercentage;
        private Integer marksAwarded;
        private String ruleDescription;
        private Boolean isActive = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder minPercentage(BigDecimal minPercentage) { this.minPercentage = minPercentage; return this; }
        public Builder maxPercentage(BigDecimal maxPercentage) { this.maxPercentage = maxPercentage; return this; }
        public Builder marksAwarded(Integer marksAwarded) { this.marksAwarded = marksAwarded; return this; }
        public Builder ruleDescription(String ruleDescription) { this.ruleDescription = ruleDescription; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public AttendanceMarkRule build() {
            return new AttendanceMarkRule(id, minPercentage, maxPercentage, marksAwarded, ruleDescription, isActive);
        }
    }
}
