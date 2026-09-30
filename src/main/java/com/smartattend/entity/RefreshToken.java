package com.smartattend.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private Boolean revoked = false;

    public RefreshToken() {}

    public RefreshToken(Long id, User user, String token, Instant expiryDate, Boolean revoked) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
        this.revoked = revoked != null ? revoked : false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Instant getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }
    public Boolean getRevoked() { return revoked; }
    public void setRevoked(Boolean revoked) { this.revoked = revoked; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String token;
        private Instant expiryDate;
        private Boolean revoked = false;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder token(String token) { this.token = token; return this; }
        public Builder expiryDate(Instant expiryDate) { this.expiryDate = expiryDate; return this; }
        public Builder revoked(Boolean revoked) { this.revoked = revoked; return this; }

        public RefreshToken build() {
            return new RefreshToken(id, user, token, expiryDate, revoked);
        }
    }
}
