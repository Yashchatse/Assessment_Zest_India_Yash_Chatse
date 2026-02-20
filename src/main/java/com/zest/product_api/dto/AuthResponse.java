package com.zest.product_api.dto;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private String username;
    private String role;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String refreshToken,
                        String tokenType, String username, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType != null ? tokenType : "Bearer";
        this.username = username;
        this.role = role;
    }

    // ── Getters ───────────────────────────────────────────
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getTokenType() { return tokenType; }
    public String getUsername() { return username; }
    public String getRole() { return role; }

    // ── Setters ───────────────────────────────────────────
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(String role) { this.role = role; }

    // ── Builder ───────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private String username;
        private String role;

        public Builder accessToken(String accessToken) { this.accessToken = accessToken; return this; }
        public Builder refreshToken(String refreshToken) { this.refreshToken = refreshToken; return this; }
        public Builder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder role(String role) { this.role = role; return this; }

        public AuthResponse build() {
            return new AuthResponse(accessToken, refreshToken, tokenType, username, role);
        }
    }
}