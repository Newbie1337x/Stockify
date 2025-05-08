package org.stockify.util;

public class StringUtils {

    public static String capitalizeFirst(String str) {
        if (str == null || str.isBlank()) {
            return str;
        }
        String trimmed = str.trim().toLowerCase();
        return Character.toUpperCase(trimmed.charAt(0))
                + trimmed.substring(1);
    }
}
