package com.flowforge.auth.security;

import org.springframework.stereotype.Component;

import com.flowforge.common.exception.WeakPasswordException;

@Component
public class PasswordPolicy {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 64;

    public void validate(String password) {

        if (password == null || password.isBlank()) {
            throw new WeakPasswordException(
                    "Password is required"
            );
        }

        if (password.length() < MIN_LENGTH) {
            throw new WeakPasswordException(
                    "Password must be at least 8 characters"
            );
        }

        if (password.length() > MAX_LENGTH) {
            throw new WeakPasswordException(
                    "Password must not exceed 64 characters"
            );
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new WeakPasswordException(
                    "Password must contain an uppercase letter"
            );
        }

        if (!password.matches(".*[a-z].*")) {
            throw new WeakPasswordException(
                    "Password must contain a lowercase letter"
            );
        }

        if (!password.matches(".*\\d.*")) {
            throw new WeakPasswordException(
                    "Password must contain a digit"
            );
        }

        if (!password.matches(".*[!@#$%^&*].*")) {
            throw new WeakPasswordException(
                    "Password must contain a special character"
            );
        }
    }
}