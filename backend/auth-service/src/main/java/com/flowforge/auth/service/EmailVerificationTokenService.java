package com.flowforge.auth.service;

import com.flowforge.auth.entity.EmailVerificationToken;
import com.flowforge.auth.entity.User;
import com.flowforge.auth.repository.EmailVerificationTokenRepository;
import com.flowforge.common.exception.InvalidVerificationTokenException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;

@Service
public class EmailVerificationTokenService {
    private final EmailVerificationTokenRepository repository;

    private final SecureRandom secureRandom = new SecureRandom();

    private static final Duration TOKEN_EXPIRY =
            Duration.ofMinutes(30);

    public EmailVerificationTokenService(
            EmailVerificationTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String createToken(User user) {
        //Now every time we create a new verification token, previous active tokens are invalidated.
        invalidatePreviousTokens(user);

        String rawToken = generateRawToken();

        String tokenHash = hashToken(rawToken);

        EmailVerificationToken verificationToken =
                new EmailVerificationToken();

        verificationToken.setToken(tokenHash);
        verificationToken.setUser(user);
        verificationToken.setCreatedAt(Instant.now());
        verificationToken.setExpiresAt(
                Instant.now().plus(TOKEN_EXPIRY)
        );

        this.repository.save(verificationToken);

        return rawToken;
    }

    private String generateRawToken() {

        byte[] bytes = new byte[32];

        secureRandom.nextBytes(bytes);

        return HexFormat.of().formatHex(bytes);
    }

    private String hashToken(String token) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
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
    public void verifyToken(String rawToken) {

        String tokenHash = hashToken(rawToken);

        EmailVerificationToken verificationToken =
                this.repository.findByToken(tokenHash)
                        .orElseThrow(() ->
                                new InvalidVerificationTokenException(
                                        "Invalid verification token"
                                ));

        if (verificationToken.getVerifiedAt() != null) {
            throw new InvalidVerificationTokenException(
                    "Email is already verified"
            );
        }

        if (verificationToken.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new InvalidVerificationTokenException(
                    "Verification token has expired"
            );
        }

        User user = verificationToken.getUser();

        user.setEnabled(true);

        verificationToken.setVerifiedAt(
                Instant.now()
        );
    }


    // if user it not verify email for sometime and logout then need to invalidatePreviousToken
    private void invalidatePreviousTokens(User user) {

        List<EmailVerificationToken> tokens =
                this.repository
                        .findByUserAndVerifiedAtIsNullAndInvalidatedAtIsNull(
                                user
                        );

        Instant now = Instant.now();

        for (EmailVerificationToken token : tokens) {
            token.setInvalidatedAt(now);
        }
    }
}
