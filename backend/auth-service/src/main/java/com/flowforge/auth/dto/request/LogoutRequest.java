package com.flowforge.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LogoutRequest {
    @NotBlank(message = "refresh token is require")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
