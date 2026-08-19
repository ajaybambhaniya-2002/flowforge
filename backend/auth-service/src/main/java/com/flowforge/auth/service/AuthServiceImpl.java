package com.flowforge.auth.service;

import com.flowforge.auth.dto.request.*;
import com.flowforge.auth.dto.response.LoginResponse;
import com.flowforge.auth.dto.response.RefreshTokenResponse;
import com.flowforge.auth.dto.response.RegisterResponse;
import com.flowforge.auth.entity.*;
import com.flowforge.auth.enums.RoleType;
import com.flowforge.auth.exception.EmailAlreadyExistsException;
import com.flowforge.auth.exception.RoleNotFoundException;
import com.flowforge.auth.security.PasswordPolicy;
import com.flowforge.common.exception.TooManyRequestsException;
import com.flowforge.auth.exception.UsernameAlreadyExistsException;
import com.flowforge.auth.repository.RoleRepository;
import com.flowforge.auth.repository.UserRepository;
import com.flowforge.auth.security.CustomUserDetails;
import com.flowforge.auth.security.JwtService;
import com.flowforge.auth.security.RateLimitService;
import com.flowforge.common.exception.BadRequestException;
import com.flowforge.common.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private  final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final RateLimitService rateLimitService;
    private final PasswordPolicy passwordPolicy;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository ,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           RefreshTokenService refreshTokenService,
                           PasswordResetTokenService passwordResetTokenService,
                           RateLimitService rateLimitService,
                           PasswordPolicy passwordPolicy){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.rateLimitService = rateLimitService;
        this.passwordPolicy = passwordPolicy;
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
        // Get authenticated user
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String token = this.jwtService.generateToken(userDetails);
        // getPrinciple() means we get CustomeUserDetail about user

        //refresh token added
        RefreshToken refreshToken =
                this.refreshTokenService.createRefreshToken(
                        userDetails.getUser()
                );
        // it return JWT Token
        return new LoginResponse(token,
                refreshToken.getToken(),"Bearer");
    }






    //For Refresh Token Generation added
    @Override
    public RefreshTokenResponse refreshAccessToken(RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService
                        .getValidRefreshToken(
                                request.getRefreshToken()
                        );

        User user = refreshToken.getUser();

        CustomUserDetails userDetails =
                new CustomUserDetails(user);

        String newAccessToken =
                jwtService.generateToken(userDetails);

        return new RefreshTokenResponse(
                newAccessToken,
                "Bearer"
        );
    }

    //refresh token logout
    @Override
    public void logout(LogoutRequest request) {
    this.refreshTokenService.revokeToken(request.getRefreshToken());
    }

    //change password
    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {

        //user is already login so from securitycotextHolder we get data

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // we get User from holder
        CustomUserDetails userDetails =
                (CustomUserDetails)
                        authentication.getPrincipal();

        User user = userDetails.getUser();

        // need to match request password and DB stored password before setting to new password
        boolean matches =
                this.passwordEncoder.matches(
                        request.getCurrentPassword(),
                        user.getPassword()
                );

        if (!matches) {
            throw new UnauthorizedException(
                    "Current password is incorrect"
            );
        }

        if (request.getCurrentPassword()
                .equals(request.getNewPassword())) {

            throw new BadRequestException(
                    "New password must be different from current password"
            );
        }
        this.passwordPolicy.validate(request.getNewPassword());
        user.setPassword(
                this.passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void forgotPassword(
            ForgotPasswordRequest request) {

        String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();

        String key =
                "flowforge:rate-limit:forgot-password:"
                        + email;
    // we check in redis because rate-limit is applied if key came more then 5 time then it throw exception
        if (!this.rateLimitService.isAllowed(key)) {

            throw new TooManyRequestsException(
                    "Too many password reset requests"
            );
        }

        Optional<User> userOptional =
                userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return;
        }

        User user = userOptional.get();

        PasswordResetTokenResult result =
                passwordResetTokenService
                        .createToken(user);

        String resetLink =
                "http://localhost:4200/reset-password?token="
                        + result.rawToken();

        System.out.println(
                "PASSWORD RESET LINK: " + resetLink
        );
    }
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = this.passwordResetTokenService.validateToken(request.getToken());
        User user = resetToken.getUser();
        // checking passowrd follow pre-defined rule of policy
        this.passwordPolicy.validate(request.getNewPassword());
        user.setPassword(this.passwordEncoder.encode(request.getNewPassword()));

        this.userRepository.save(user);

        this.passwordResetTokenService.markAsUsed(
                resetToken
        );

        this.refreshTokenService.revokeAllTokens(
                user
        );
    }
}
