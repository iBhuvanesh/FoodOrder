package com.foodorder.auth.service;

import com.foodorder.auth.dto.AuthResponse;
import com.foodorder.auth.dto.LoginRequest;
import com.foodorder.auth.dto.RegisterRequest;
import com.foodorder.auth.entity.User;
import com.foodorder.auth.exception.BadRequestException;
import com.foodorder.auth.exception.UnauthorizedException;
import com.foodorder.auth.repository.UserRepository;
import com.foodorder.auth.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        userRepository.save(user);
        log.info("Registered new user: {} with role {}", user.getEmail(), user.getRole());

        return new AuthResponse(user.getId(), jwtService.generateToken(user.getId(), user.getEmail(), user.getRole()), user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        log.info("User logged in: {}", user.getEmail());
        return new AuthResponse(user.getId(), jwtService.generateToken(user.getId(), user.getEmail(), user.getRole()), user.getEmail(), user.getRole().name());
    }
}
