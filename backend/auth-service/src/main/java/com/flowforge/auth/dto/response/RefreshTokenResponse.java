package com.flowforge.auth.dto.response;

public class RefreshTokenResponse {

    private String accessToken;
    private String tokenType;

    public RefreshTokenResponse(
            String accessToken,
            String tokenType) {

        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
