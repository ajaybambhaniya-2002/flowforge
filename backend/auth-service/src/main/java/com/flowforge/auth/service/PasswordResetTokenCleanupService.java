package com.flowforge.auth.service;

import com.flowforge.auth.repository.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
@Service
public class PasswordResetTokenCleanupService {
    private final PasswordResetTokenRepository repository;

    public PasswordResetTokenCleanupService(
            PasswordResetTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public long cleanupExpiredTokens() {

        return this.repository.deleteByExpiresAtBefore(
                Instant.now()
        );
    }
}
