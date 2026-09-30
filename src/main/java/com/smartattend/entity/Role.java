package com.smartattend.entity;

import com.smartattend.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private RoleType name;

    @Column(length = 255)
    private String description;

    public Role() {}

    public Role(Long id, RoleType name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RoleType getName() { return name; }
    public void setName(RoleType name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private RoleType name;
        private String description;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(RoleType name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }

        public Role build() {
            return new Role(id, name, description);
        }
    }
}
