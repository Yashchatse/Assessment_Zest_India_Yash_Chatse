package com.zest.product_api.service;

import com.zest.product_api.dto.*;
import com.zest.product_api.entity.Role;
import com.zest.product_api.entity.User;
import com.zest.product_api.exception.ResourceNotFoundException;
import com.zest.product_api.exception.UnauthorizedException;
import com.zest.product_api.repository.UserRepository;
import com.zest.product_api.security.JwtTokenProvider;
import com.zest.product_api.service.AuthService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already taken: " + request.getUsername());

        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already registered: " + request.getEmail());

        Role role = request.getRole().equalsIgnoreCase("ROLE_ADMIN")
            ? Role.ROLE_ADMIN : Role.ROLE_USER;

        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(role)
            .build();

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .username(user.getUsername())
            .role(user.getRole().name())
            .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        // Refresh token rotation — save new refresh token
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .username(user.getUsername())
            .role(user.getRole().name())
            .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken))
            throw new UnauthorizedException("Invalid or expired refresh token");

        User user = userRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new UnauthorizedException("Refresh token not recognized"));

        // Rotate — generate both tokens fresh
        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return AuthResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .username(user.getUsername())
            .role(user.getRole().name())
            .build();
    }

    @Override
    public void logout(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // Invalidate refresh token on logout
        user.setRefreshToken(null);
        userRepository.save(user);
    }
}