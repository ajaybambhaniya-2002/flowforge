package com.flowforge.common.constants;
// if we have multi language application then then based on help of code frontend can show message
// if USER_404 then user not found,, Hindi -> उपयोगकर्ता नहीं मिला
public final class ErrorCodes {
    private ErrorCodes() {
    }

    // User Module
    public static final String USER_NOT_FOUND = "USER_404";
    public static final String USER_ALREADY_EXISTS = "USER_409";

    // Auth Module
    public static final String INVALID_CREDENTIALS = "AUTH_401";
    public static final String TOKEN_EXPIRED = "AUTH_402";
    public static final String ACCESS_DENIED = "AUTH_403";

    // Validation
    public static final String VALIDATION_FAILED = "VAL_400";

    // Common
    public static final String INTERNAL_ERROR = "SYS_500";
}
