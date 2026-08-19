package com.flowforge.auth.repository;

import com.flowforge.auth.entity.EmailVerificationToken;
import com.flowforge.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    Optional<EmailVerificationToken> findByUserAndVerifiedAtIsNull(User user);
    List<EmailVerificationToken> findByUserAndVerifiedAtIsNullAndInvalidatedAtIsNull(
            User user
    );
}
