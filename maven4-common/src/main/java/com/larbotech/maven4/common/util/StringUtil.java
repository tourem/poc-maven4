package com.larbotech.maven4.common.util;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utilitaires pour les chaînes de caractères
 */
public class StringUtil {

    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }

    public static String escapeHtml(String str) {
        return StringEscapeUtils.escapeHtml4(str);
    }

    public static String padStart(String str, int minLength, char padChar) {
        return Strings.padStart(str, minLength, padChar);
    }

    public static List<String> splitAndTrim(String str, String delimiter) {
        return Arrays.stream(str.split(delimiter))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public static boolean isNullOrEmpty(String str) {
        return Strings.isNullOrEmpty(str);
    }

    public static String truncate(String str, int maxLength) {
        return StringUtils.truncate(str, maxLength);
    }

    public static String reverse(String str) {
        return StringUtils.reverse(str);
    }

    public static String removeAccents(String str) {
        return StringUtils.stripAccents(str);
    }

    public static String toCamelCase(String str) {
        return Arrays.stream(str.split("_"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining());
    }

    public static String toSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
