package com.zest.product_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpiration;

    @Value("${app.jwt.token-prefix}")
    private String tokenPrefix;

    @Value("${app.jwt.header-name}")
    private String headerName;

    public String getSecret() { return secret; }
    public long getExpiration() { return expiration; }
    public long getRefreshExpiration() { return refreshExpiration; }
    public String getTokenPrefix() { return tokenPrefix; }
    public String getHeaderName() { return headerName; }
}