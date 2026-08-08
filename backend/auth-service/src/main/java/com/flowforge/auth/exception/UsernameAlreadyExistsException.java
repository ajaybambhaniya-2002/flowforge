package com.flowforge.auth.exception;

import com.flowforge.common.exception.BusinessException;

public class UsernameAlreadyExistsException extends BusinessException {

    public UsernameAlreadyExistsException(String username) {

        super(
                "AUTH_USERNAME_EXISTS",
                "Username already exists: " + username
        );
    }
}
