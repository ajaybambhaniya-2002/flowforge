package com.flowforge.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;

import com.flowforge.auth.dto.request.PasswordResetTokenResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowforge.auth.entity.PasswordResetToken;
import com.flowforge.auth.entity.User;
import com.flowforge.auth.repository.PasswordResetTokenRepository;
import com.flowforge.common.exception.UnauthorizedException;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository
            passwordResetTokenRepository;

    private final SecureRandom secureRandom;

    public PasswordResetTokenService(
            PasswordResetTokenRepository passwordResetTokenRepository) {

        this.passwordResetTokenRepository = passwordResetTokenRepository;

        this.secureRandom = new SecureRandom();
    }

    private String generateToken() {

        byte[] randomBytes = new byte[32];

        secureRandom.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    @Transactional

    public PasswordResetTokenResult createToken(User user) {
        // need to check if multipletime reset password happpen then old token should be invalid
        invalidatePreviousTokens(user);

        String rawToken = generateToken();

        PasswordResetToken resetToken =
                new PasswordResetToken();

        // in DB we need to store hashPassword
        resetToken.setToken(
                hashToken(rawToken)
        );

        resetToken.setUser(user);

        resetToken.setCreatedAt(
                Instant.now()
        );

        resetToken.setExpiresAt(
                Instant.now()
                        .plus(15, ChronoUnit.MINUTES)
        );

        PasswordResetToken savedToken =
                passwordResetTokenRepository.save(
                        resetToken
                );

        return new PasswordResetTokenResult(
                savedToken,
                rawToken
        );
    }

    public PasswordResetToken getToken(String token) {

        return passwordResetTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new UnauthorizedException(
                                "Invalid password reset token"
                        )
                );
    }

        public PasswordResetToken validateToken(
            String rawToken) {

        String tokenHash = hashToken(rawToken);

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(tokenHash)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "Invalid password reset token"
                                )
                        );

        if (resetToken.getUsedAt() != null) {
            throw new UnauthorizedException(
                    "Password reset token has already been used"
            );
        }
        if (resetToken.getInvalidatedAt() != null) {
            throw new UnauthorizedException(
                    "Password reset token is no longer valid"
            );
        }

        if (resetToken.getExpiresAt()
                .isBefore(Instant.now())) {
            throw new UnauthorizedException(
                    "Password reset token has expired"
            );
        }

        return resetToken;
    }

    @Transactional
    public void markAsUsed(
            PasswordResetToken resetToken) {

        resetToken.setUsedAt(
                Instant.now()
        );

       this.passwordResetTokenRepository.save(
                resetToken
        );
    }

    private String hashToken(String token) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            token.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    e
            );
        }
    }


    @Transactional
    public void invalidatePreviousTokens(User user) {

        List<PasswordResetToken> tokens =
                this.passwordResetTokenRepository
                        .findByUserAndUsedAtIsNullAndInvalidatedAtIsNull(
                                user
                        );

        Instant now = Instant.now();

        for (PasswordResetToken token : tokens) {
            token.setInvalidatedAt(now);
        }

        this.passwordResetTokenRepository.saveAll(tokens);
    }
}