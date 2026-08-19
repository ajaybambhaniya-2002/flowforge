package com.flowforge.auth.controller;

import com.flowforge.auth.dto.request.*;
import com.flowforge.auth.dto.response.LoginResponse;
import com.flowforge.auth.dto.response.RefreshTokenResponse;
import com.flowforge.auth.dto.response.RegisterResponse;
import com.flowforge.auth.service.AuthService;
import com.flowforge.auth.service.EmailVerificationTokenService;
import com.flowforge.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    public AuthController(AuthService authService,
             EmailVerificationTokenService emailVerificationTokenService) {
        this.authService = authService;
        this.emailVerificationTokenService = emailVerificationTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>>register(@Valid @RequestBody RegisterRequest request){
       RegisterResponse response = this.authService.register(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "User registered successfully", response));
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>>login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse response = this.authService.login(loginRequest);
       return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Login successfully Done", response));
    }

    @PostMapping("/refresh")
    public  ResponseEntity<RefreshTokenResponse>refrsh( @Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(
                this.authService.refreshAccessToken(refreshTokenRequest)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody LogoutRequest request) {

        this.authService.logout(request);

        return ResponseEntity.noContent().build();
    }

    //for change password
    @PostMapping("/change-password")
    public ResponseEntity<Void>changePassword(@Valid @RequestBody ChangePasswordRequest request){
        this.authService.changePassword(request);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        this.authService.forgotPassword(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "If the account exists, password reset instructions have been sent.",null)

        );
    }
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Password reset successfully",null)
        );
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @RequestParam String token) {

        this.emailVerificationTokenService.verifyToken(token);

        return ResponseEntity.ok(
                new ApiResponse<>(true,"Email verified successfully",null));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Void>> resendVerification(
            @Valid @RequestBody
            ResendVerificationRequest request) {
        this.authService.resendVerification(
                request.getEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>(true,"If the account exists and is not verified, "
                        + "a verification link has been sent.",null));
    }

    @GetMapping("/test")
    public String testUser(){
        return "done JWT is Working with token";
    }


}
