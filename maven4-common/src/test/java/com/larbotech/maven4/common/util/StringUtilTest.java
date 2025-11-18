package com.larbotech.maven4.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StringUtil Tests")
class StringUtilTest {

    @Test
    @DisplayName("Should capitalize string")
    void shouldCapitalizeString() {
        assertThat(StringUtil.capitalize("hello")).isEqualTo("Hello");
    }

    @Test
    @DisplayName("Should split and trim string")
    void shouldSplitAndTrimString() {
        List<String> result = StringUtil.splitAndTrim("a, b , c", ",");
        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    @DisplayName("Should convert to camel case")
    void shouldConvertToCamelCase() {
        assertThat(StringUtil.toCamelCase("hello_world")).isEqualTo("HelloWorld");
    }

    @Test
    @DisplayName("Should convert to snake case")
    void shouldConvertToSnakeCase() {
        assertThat(StringUtil.toSnakeCase("HelloWorld")).isEqualTo("hello_world");
    }

    @Test
    @DisplayName("Should reverse string")
    void shouldReverseString() {
        assertThat(StringUtil.reverse("hello")).isEqualTo("olleh");
    }
}
