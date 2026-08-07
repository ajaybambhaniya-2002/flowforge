package com.flowforge.auth.service;

import com.flowforge.auth.dto.request.RegisterRequest;
import com.flowforge.auth.dto.response.RegisterResponse;
import com.flowforge.auth.repository.RoleRepository;
import com.flowforge.auth.repository.UserRepository;

public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private  final RoleRepository roleRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository ){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        return null;
    }
}
