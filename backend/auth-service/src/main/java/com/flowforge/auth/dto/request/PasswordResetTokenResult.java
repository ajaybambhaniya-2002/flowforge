package com.flowforge.auth.dto.request;

import com.flowforge.auth.entity.PasswordResetToken;

public record PasswordResetTokenResult(
        PasswordResetToken resetToken,
        String rawToken
) {
    @Override
    public PasswordResetToken resetToken() {
        return resetToken;
    }

    @Override
    public String rawToken() {
        return rawToken;
    }
}