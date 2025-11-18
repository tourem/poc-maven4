package com.larbotech.maven4.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Utilitaires de validation
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[1-9]\\d{1,14}$"
    );

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"
    );

    public static boolean isValidEmail(String email) {
        return StringUtils.isNotBlank(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return StringUtils.isNotBlank(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidUrl(String url) {
        return StringUtils.isNotBlank(url) && URL_PATTERN.matcher(url).matches();
    }

    public static boolean isNumeric(String str) {
        return StringUtils.isNumeric(str);
    }

    public static boolean isAlphanumeric(String str) {
        return StringUtils.isAlphanumeric(str);
    }

    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }

    public static boolean hasLength(String str, int minLength, int maxLength) {
        if (str == null) return false;
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }
}
