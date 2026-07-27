package com.flowforge.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private DateTimeUtil() {
    }

    public static String now() {

        return LocalDateTime.now().format(FORMATTER);

    }
}
