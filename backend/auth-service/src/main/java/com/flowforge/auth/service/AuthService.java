package com.flowforge.auth.service;

import com.flowforge.auth.dto.request.*;
import com.flowforge.auth.dto.response.LoginResponse;
import com.flowforge.auth.dto.response.ProfileResponse;
import com.flowforge.auth.dto.response.RefreshTokenResponse;
import com.flowforge.auth.dto.response.RegisterResponse;
import jakarta.validation.Valid;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest loginRequest);

    RefreshTokenResponse refreshAccessToken(
            String refreshToken);

    void logout(String refreshToken);
    ProfileResponse profile();
    void changePassword(ChangePasswordRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void resendVerification(String email);
}
