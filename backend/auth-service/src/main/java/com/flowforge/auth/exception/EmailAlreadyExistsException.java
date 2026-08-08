package com.flowforge.auth.exception;

import com.flowforge.common.exception.BusinessException;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String email) {

        super(
                "AUTH_EMAIL_EXISTS",
                "Email already exists: " + email
        );
    }
}
