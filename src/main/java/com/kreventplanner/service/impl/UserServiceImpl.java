package com.kreventplanner.service.impl;

import com.kreventplanner.dto.LoginRequest;
import com.kreventplanner.dto.LoginResponse;
import com.kreventplanner.entity.User;
import com.kreventplanner.repository.UserRepository;
import com.kreventplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) {
            return new LoginResponse(false, "User not found", null, null);
        }

        User user = userOpt.get();
        System.out.println(user.getPassword());
        System.out.println(passwordEncoder.encode(loginRequest.getPassword()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponse(false, "Invalid credentials", null, null);
        }

        return new LoginResponse(true, "Login successful", user.getUsername(), user.getRole());
    }
}
