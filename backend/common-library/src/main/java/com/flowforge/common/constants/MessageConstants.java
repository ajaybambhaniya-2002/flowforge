package com.flowforge.common.constants;

public final class MessageConstants {

    private MessageConstants() {
    }

    // Success Messages
    public static final String USER_CREATED = "User created successfully";
    public static final String USER_UPDATED = "User updated successfully";
    public static final String USER_DELETED = "User deleted successfully";

    // Error Messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ROLE_NOT_FOUND = "Role not found";

    public static final String INVALID_CREDENTIALS =
            "Invalid username or password";

    public static final String ACCESS_DENIED =
            "Access denied";

    public static final String INTERNAL_SERVER_ERROR =
            "Internal server error";
}
