package com.zest.product_api.service;

import com.zest.product_api.dto.AuthResponse;
import com.zest.product_api.dto.LoginRequest;
import com.zest.product_api.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String username);
}
