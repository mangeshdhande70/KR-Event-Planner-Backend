package com.kreventplanner.service.impl;

import com.kreventplanner.dto.LoginRequest;
import com.kreventplanner.dto.LoginResponse;
import com.kreventplanner.entity.User;
import com.kreventplanner.repository.UserRepository;
import com.kreventplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kreventplanner.security.JwtUtil;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // This delegates to the CustomUserDetailsService and PasswordEncoder we configured!
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // We fetch the user to get their role for the response and the token
            User user = userRepository.findByUsername(loginRequest.getUsername()).get();

            // If we reach here, username and password are correct
            String token = jwtUtil.generateToken(loginRequest.getUsername(), user);
            
            return new LoginResponse(true, "Login successful", user.getUsername(), user.getRole(), token);

        } catch (AuthenticationException e) {
            // Spring Security throws this if the user isn't found OR password doesn't match
            return new LoginResponse(false, "Invalid username or password", null, null, null);
        }
    }
}
