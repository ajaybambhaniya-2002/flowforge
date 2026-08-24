package com.flowforge.auth.service;

import com.flowforge.auth.dto.request.*;
import com.flowforge.auth.dto.response.LoginResponse;
import com.flowforge.auth.dto.response.ProfileResponse;
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
import com.flowforge.common.response.PageResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final EmailVerificationTokenService emailVerificationTokenService;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository ,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           RefreshTokenService refreshTokenService,
                           PasswordResetTokenService passwordResetTokenService,
                           RateLimitService rateLimitService,
                           PasswordPolicy passwordPolicy,
                           EmailVerificationTokenService emailVerificationTokenService){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.rateLimitService = rateLimitService;
        this.passwordPolicy = passwordPolicy;
        this.emailVerificationTokenService = emailVerificationTokenService;
    }
    private static final Logger log =
            LoggerFactory.getLogger(AuthServiceImpl.class);
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

        // for Email we done this to check email is not verify yet
        user.setEnabled(false);
        User saveUser = this.userRepository.save(user);

        // for save use emailverificationToken is generated
        String rawToken =
                this.emailVerificationTokenService.createToken(
                        saveUser
                );

        // 6. Build verification URL
        String verificationUrl =
                "http://localhost:4200/verify-email?token="
                        + rawToken;

        // 7. Local development only
        log.info(
                "Email verification link for {}: {}",
                saveUser.getEmail(),
                verificationUrl
        );

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
        return new LoginResponse(token,"Bearer",
                    refreshToken.getToken());
    }






    //For Refresh Token Generation added
    @Override
    public RefreshTokenResponse refreshAccessToken(String refreshToken) {

        RefreshToken refreshTokenData =
                refreshTokenService
                        .getValidRefreshToken(
                                refreshToken
                        );

        User user = refreshTokenData.getUser();

        CustomUserDetails userDetails =
                new CustomUserDetails(user);

        String newAccessToken =
                jwtService.generateToken(userDetails);

        return new RefreshTokenResponse(
                newAccessToken,
                "Bearer"
        );
    }

    @Override
    public ProfileResponse profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails =
                (CustomUserDetails)
                        authentication.getPrincipal();
        User user = userDetails.getUser();
        return new ProfileResponse(user.getUsername(),user.getEmail());
    }

    //refresh token logout
    @Override
<<<<<<< Updated upstream
    public void logout(String refreshToken) {
=======
    public void logout( String refreshToken) {
>>>>>>> Stashed changes
    this.refreshTokenService.revokeToken(refreshToken);
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

    @Override
    @Transactional
    public void resendVerification(String email) {
        String normalizedEmail =
                email.trim().toLowerCase();

        Optional<User> userOptional =
                this.userRepository.findByEmail(
                        normalizedEmail
                );
        String key =
                "flowforge:rate-limit:resend-verification:"
                        + normalizedEmail;
        // we check in redis because rate-limit is applied if key came more then 5 time then it throw exception
        if (!this.rateLimitService.isAllowed(key)) {

            throw new TooManyRequestsException(
                    "Too many password reset requests"
            );
        }
        if (userOptional.isEmpty()) {
            return;
        }

        User user = userOptional.get();

        if (user.isEnabled()) {
            return;
        }

        String rawToken =
                this.emailVerificationTokenService.createToken(
                        user
                );

        String verificationUrl =
                "http://localhost:4200/verify-email?token="
                        + rawToken;

        log.info(
                "Email verification link for {}: {}",
                user.getEmail(),
                verificationUrl
        );
    }
}
