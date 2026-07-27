package com.flowforge.common.util;

public final class StringUtil {

    private StringUtil() {
    }

    public static boolean isNullOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }

    public static String capitalize(String value) {

        if (isNullOrBlank(value)) {
            return value;
        }

        return value.substring(0, 1).toUpperCase()
                + value.substring(1).toLowerCase();

    }
}
