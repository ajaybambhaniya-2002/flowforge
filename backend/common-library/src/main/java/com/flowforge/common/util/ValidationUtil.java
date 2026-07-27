package com.flowforge.common.util;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static boolean isPositive(Long id) {

        return id != null && id > 0;

    }
}
