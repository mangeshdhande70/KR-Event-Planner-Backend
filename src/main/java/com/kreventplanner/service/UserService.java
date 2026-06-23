package com.kreventplanner.service;

import com.kreventplanner.dto.LoginRequest;
import com.kreventplanner.dto.LoginResponse;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest);
}
