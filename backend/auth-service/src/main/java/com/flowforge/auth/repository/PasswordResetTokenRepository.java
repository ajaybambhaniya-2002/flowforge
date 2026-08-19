package com.flowforge.auth.repository;

import com.flowforge.auth.entity.PasswordResetToken;
import com.flowforge.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    Optional<PasswordResetToken>findByToken(String token);

    List<PasswordResetToken>
    findByUserAndUsedAtIsNullAndInvalidatedAtIsNull(
            User user
    );
    long deleteByExpiresAtBefore(Instant now);
}
