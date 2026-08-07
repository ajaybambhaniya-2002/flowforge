package com.flowforge.auth.service;

import com.flowforge.auth.dto.request.RegisterRequest;
import com.flowforge.auth.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
}
