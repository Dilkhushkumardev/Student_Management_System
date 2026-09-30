package com.smartattend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Branch() {}

    public Branch(Long id, String code, String name, Department department, Boolean isActive) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.department = department;
        this.isActive = isActive != null ? isActive : true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String code;
        private String name;
        private Department department;
        private Boolean isActive = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder department(Department department) { this.department = department; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public Branch build() {
            return new Branch(id, code, name, department, isActive);
        }
    }
}
