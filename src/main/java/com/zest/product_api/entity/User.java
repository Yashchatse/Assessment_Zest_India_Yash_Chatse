package com.zest.product_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    // ── Constructors ──────────────────────────────────────
    public User() {}

    public User(Long id, String username, String password,
                String email, Role role, String refreshToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    // ── Getters ───────────────────────────────────────────
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public String getRefreshToken() { return refreshToken; }

    // ── Setters ───────────────────────────────────────────
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(Role role) { this.role = role; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    // ── Builder ───────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String username;
        private String password;
        private String email;
        private Role role;
        private String refreshToken;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder refreshToken(String refreshToken) { this.refreshToken = refreshToken; return this; }

        public User build() {
            return new User(id, username, password, email, role, refreshToken);
        }
    }
}