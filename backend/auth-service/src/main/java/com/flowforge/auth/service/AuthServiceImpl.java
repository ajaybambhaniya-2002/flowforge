package com.flowforge.auth.service;

import com.flowforge.auth.dto.request.RegisterRequest;
import com.flowforge.auth.dto.response.RegisterResponse;
import com.flowforge.auth.entity.Role;
import com.flowforge.auth.entity.User;
import com.flowforge.auth.enums.RoleType;
import com.flowforge.auth.repository.RoleRepository;
import com.flowforge.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Transactional
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private  final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository ,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

       if(this.userRepository.existsByEmail(request.getEmail())){
           throw new RuntimeException("Email already exists");
       }

       if(this.userRepository.existsByUsername(request.getUsername())){
           throw  new RuntimeException("Username already exists");
       }

        Role role = this.roleRepository.findByRoleName(RoleType.USER.name())
                .orElseThrow(() ->
                        new RuntimeException("Default role not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(role);

        User saveUser = this.userRepository.save(user);

         return new RegisterResponse(saveUser.getId(), saveUser.getUsername(), saveUser.getEmail());
    }
}
