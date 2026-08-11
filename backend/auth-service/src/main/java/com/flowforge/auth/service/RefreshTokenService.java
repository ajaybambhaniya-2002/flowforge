package com.flowforge.auth.service;

import com.flowforge.auth.config.JwtProperties;
import com.flowforge.auth.entity.RefreshToken;
import com.flowforge.auth.entity.User;

import com.flowforge.auth.repository.RefreshTokenRepository;
import com.flowforge.common.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    private final SecureRandom secureRandom =
            new SecureRandom();

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            JwtProperties jwtProperties) {

        this.refreshTokenRepository =
                refreshTokenRepository;

        this.jwtProperties = jwtProperties;
    }

    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken =
                new RefreshToken();

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String token =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(randomBytes);

        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(Instant.now());

        refreshToken.setExpiresAt(
                Instant.now()
                        .plusMillis(
                                this.jwtProperties
                                        .getRefreshExpiration()
                        )
        );

        refreshToken.setRevoked(false);

        return this.refreshTokenRepository.save(
                refreshToken
        );
    }

    public Optional<RefreshToken> findByToken(
            String token) {

        return this.refreshTokenRepository
                .findByToken(token);
    }

    public RefreshToken getValidRefreshToken(String token) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "Invalid refresh token"
                                )
                        );

        if (refreshToken.isRevoked()) {

            throw new UnauthorizedException(
                    "Refresh token has been revoked"
            );
        }

        if (refreshToken.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new UnauthorizedException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }

    public RefreshToken validateRefreshToken(
            RefreshToken refreshToken) {

        if (refreshToken.isRevoked()) {
            throw new RuntimeException(
                    "Refresh token has been revoked"
            );
        }

        if (refreshToken.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new RuntimeException(
                    "Refresh token expired"
            );
        }

        return refreshToken;
    }

    public void revokeToken(
            RefreshToken refreshToken) {


        refreshToken.setRevoked(true);

       this.refreshTokenRepository.save(
                refreshToken
        );
    }

    public void revokeToken(String token) {

        RefreshToken refreshToken =
               this.refreshTokenRepository.findByToken(token)
                       .orElseThrow(()->
                               new UnauthorizedException("Invalid refresh token"));

        refreshToken.setRevoked(true);

        this.refreshTokenRepository.save(refreshToken);
    }
}