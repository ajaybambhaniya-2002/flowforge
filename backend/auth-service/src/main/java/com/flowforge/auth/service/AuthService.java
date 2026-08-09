package com.flowforge.auth.service;

import com.flowforge.auth.dto.request.LoginRequest;
import com.flowforge.auth.dto.request.RegisterRequest;
import com.flowforge.auth.dto.response.LoginResponse;
import com.flowforge.auth.dto.response.RegisterResponse;
import jakarta.validation.Valid;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest loginRequest);
}
