package com.zest.product_api.service;

import com.zest.product_api.dto.*;
import com.zest.product_api.entity.Role;
import com.zest.product_api.entity.User;
import com.zest.product_api.exception.ResourceNotFoundException;
import com.zest.product_api.repository.UserRepository;
import com.zest.product_api.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("encoded_password");
        user.setRole(Role.ROLE_USER);

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole("ROLE_USER");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    void register_WhenNewUser_ShouldReturnAuthResponse() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(jwtTokenProvider.generateAccessToken("testuser")).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken("testuser")).thenReturn("refresh_token");
        when(userRepository.save(any(User.class))).thenReturn(user);

        AuthResponse result = authService.register(registerRequest);

        assertNotNull(result);
        assertEquals("access_token", result.getAccessToken());
        assertEquals("refresh_token", result.getRefreshToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_WhenUsernameExists_ShouldThrowException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(RuntimeException.class,
            () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_WhenValidCredentials_ShouldReturnAuthResponse() {
        when(authenticationManager.authenticate(
            any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(null);
        when(userRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateAccessToken("testuser"))
            .thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken("testuser"))
            .thenReturn("refresh_token");
        when(userRepository.save(any(User.class))).thenReturn(user);

        AuthResponse result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("access_token", result.getAccessToken());
        assertEquals("Bearer", result.getTokenType());
    }

    @Test
    void logout_WhenUserExists_ShouldClearRefreshToken() {
        when(userRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> authService.logout("testuser"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void logout_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("unknown"))
            .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> authService.logout("unknown"));
    }
}