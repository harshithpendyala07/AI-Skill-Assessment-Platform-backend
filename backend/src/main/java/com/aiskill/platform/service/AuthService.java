package com.aiskill.platform.service;

import com.aiskill.platform.config.JwtService;
import com.aiskill.platform.dto.AuthRequest;
import com.aiskill.platform.dto.AuthResponse;
import com.aiskill.platform.dto.RegisterRequest;
import com.aiskill.platform.model.Role;
import com.aiskill.platform.model.User;
import com.aiskill.platform.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered: " + request.getEmail());
        }

        Role role = Role.ROLE_STUDENT;
        if (request.getRole() != null && request.getRole().toUpperCase().contains("ADMIN")) {
            role = Role.ROLE_ADMIN;
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                role
        );
        user.setTargetRole(request.getTargetRole() != null ? request.getTargetRole() : "Full Stack Developer");
        user.setBio("Passionate developer aiming for technical mastery.");
        user.setSkills("Java, Spring Boot, React, SQL");

        userRepository.save(user);

        var springUser = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(user.getRole().name()))
        );

        String jwt = jwtService.generateToken(springUser);

        return new AuthResponse(jwt, user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getTargetRole());
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        var springUser = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(user.getRole().name()))
        );

        String jwt = jwtService.generateToken(springUser);

        return new AuthResponse(jwt, user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getTargetRole());
    }

    public User getCurrentAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found in security context");
        }
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }
}
