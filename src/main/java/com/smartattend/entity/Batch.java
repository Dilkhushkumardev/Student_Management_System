package com.smartattend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "start_year", nullable = false)
    private Integer startYear;

    @Column(name = "end_year", nullable = false)
    private Integer endYear;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Batch() {}

    public Batch(Long id, String name, Integer startYear, Integer endYear, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
        this.isActive = isActive != null ? isActive : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getStartYear() { return startYear; }
    public void setStartYear(Integer startYear) { this.startYear = startYear; }
    public Integer getEndYear() { return endYear; }
    public void setEndYear(Integer endYear) { this.endYear = endYear; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private Integer startYear;
        private Integer endYear;
        private Boolean isActive = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder startYear(Integer startYear) { this.startYear = startYear; return this; }
        public Builder endYear(Integer endYear) { this.endYear = endYear; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public Batch build() {
            return new Batch(id, name, startYear, endYear, isActive);
        }
    }
}
