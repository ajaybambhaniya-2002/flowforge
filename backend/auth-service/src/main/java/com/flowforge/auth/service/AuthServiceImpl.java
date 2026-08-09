package com.flowforge.auth.service;

import com.flowforge.auth.config.JwtProperties;
import com.flowforge.auth.dto.request.LoginRequest;
import com.flowforge.auth.dto.request.RegisterRequest;
import com.flowforge.auth.dto.response.LoginResponse;
import com.flowforge.auth.dto.response.RegisterResponse;
import com.flowforge.auth.entity.Role;
import com.flowforge.auth.entity.User;
import com.flowforge.auth.enums.RoleType;
import com.flowforge.auth.exception.EmailAlreadyExistsException;
import com.flowforge.auth.exception.RoleNotFoundException;
import com.flowforge.auth.exception.UsernameAlreadyExistsException;
import com.flowforge.auth.repository.RoleRepository;
import com.flowforge.auth.repository.UserRepository;
import com.flowforge.auth.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private  final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository ,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // Register API
    @Override
    public RegisterResponse register(RegisterRequest request) {

       if(this.userRepository.existsByEmail(request.getEmail())){
           throw new UsernameAlreadyExistsException(
                   request.getUsername()
           );
       }

       if(this.userRepository.existsByUsername(request.getUsername())){
           throw new EmailAlreadyExistsException(
                   request.getEmail()
           );
       }

        Role role = this.roleRepository.findByRoleName(RoleType.USER.name())
                .orElseThrow(() ->
                        new RoleNotFoundException(
                                RoleType.USER.name()
                        ));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(role);

        User saveUser = this.userRepository.save(user);

            return new RegisterResponse(saveUser.getId(), saveUser.getUsername(), saveUser.getEmail());
    }


    // login API
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String token = this.jwtService.generateToken((UserDetails) authentication.getPrincipal());
        // getPrinciple() means we get CustomeUserDetail about user

        // it return JWT Token
        return new LoginResponse(token,"Bearer");
    }
}
