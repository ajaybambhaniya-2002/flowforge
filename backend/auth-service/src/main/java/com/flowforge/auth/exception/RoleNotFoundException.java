package com.flowforge.auth.exception;

import com.flowforge.common.exception.BusinessException;

public class RoleNotFoundException extends BusinessException {
    public RoleNotFoundException(String roleName) {

        super(
                "AUTH_ROLE_NOT_FOUND",
                "Required role not found: " + roleName
        );
    }
}
