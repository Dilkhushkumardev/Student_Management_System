package com.smartattend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_rules")
public class AttendanceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_name", nullable = false, unique = true, length = 100)
    private String ruleName;

    @Column(name = "min_percentage_required", nullable = false, precision = 5, scale = 2)
    private BigDecimal minPercentageRequired = BigDecimal.valueOf(75.00);

    @Column(name = "condonation_percentage_allowed", nullable = false, precision = 5, scale = 2)
    private BigDecimal condonationPercentageAllowed = BigDecimal.valueOf(15.00);

    @Column(name = "late_as_present_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal lateAsPresentWeight = BigDecimal.valueOf(1.00);

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(length = 255)
    private String description;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public AttendanceRule() {}

    public AttendanceRule(Long id, String ruleName, BigDecimal minPercentageRequired, BigDecimal condonationPercentageAllowed, BigDecimal lateAsPresentWeight, Boolean isActive, String description, LocalDateTime updatedAt) {
        this.id = id;
        this.ruleName = ruleName;
        this.minPercentageRequired = minPercentageRequired != null ? minPercentageRequired : BigDecimal.valueOf(75.00);
        this.condonationPercentageAllowed = condonationPercentageAllowed != null ? condonationPercentageAllowed : BigDecimal.valueOf(15.00);
        this.lateAsPresentWeight = lateAsPresentWeight != null ? lateAsPresentWeight : BigDecimal.valueOf(1.00);
        this.isActive = isActive != null ? isActive : true;
        this.description = description;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public BigDecimal getMinPercentageRequired() { return minPercentageRequired; }
    public void setMinPercentageRequired(BigDecimal minPercentageRequired) { this.minPercentageRequired = minPercentageRequired; }
    public BigDecimal getCondonationPercentageAllowed() { return condonationPercentageAllowed; }
    public void setCondonationPercentageAllowed(BigDecimal condonationPercentageAllowed) { this.condonationPercentageAllowed = condonationPercentageAllowed; }
    public BigDecimal getLateAsPresentWeight() { return lateAsPresentWeight; }
    public void setLateAsPresentWeight(BigDecimal lateAsPresentWeight) { this.lateAsPresentWeight = lateAsPresentWeight; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String ruleName;
        private BigDecimal minPercentageRequired = BigDecimal.valueOf(75.00);
        private BigDecimal condonationPercentageAllowed = BigDecimal.valueOf(15.00);
        private BigDecimal lateAsPresentWeight = BigDecimal.valueOf(1.00);
        private Boolean isActive = true;
        private String description;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder ruleName(String ruleName) { this.ruleName = ruleName; return this; }
        public Builder minPercentageRequired(BigDecimal minPercentageRequired) { this.minPercentageRequired = minPercentageRequired; return this; }
        public Builder condonationPercentageAllowed(BigDecimal condonationPercentageAllowed) { this.condonationPercentageAllowed = condonationPercentageAllowed; return this; }
        public Builder lateAsPresentWeight(BigDecimal lateAsPresentWeight) { this.lateAsPresentWeight = lateAsPresentWeight; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public AttendanceRule build() {
            return new AttendanceRule(id, ruleName, minPercentageRequired, condonationPercentageAllowed, lateAsPresentWeight, isActive, description, updatedAt);
        }
    }
}
