package com.larbotech.maven4.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ValidationUtil Tests")
class ValidationUtilTest {

    @Test
    @DisplayName("Should validate email")
    void shouldValidateEmail() {
        assertThat(ValidationUtil.isValidEmail("test@example.com")).isTrue();
        assertThat(ValidationUtil.isValidEmail("invalid-email")).isFalse();
    }

    @Test
    @DisplayName("Should validate phone")
    void shouldValidatePhone() {
        assertThat(ValidationUtil.isValidPhone("+33612345678")).isTrue();
        assertThat(ValidationUtil.isValidPhone("invalid")).isFalse();
    }

    @Test
    @DisplayName("Should validate URL")
    void shouldValidateUrl() {
        assertThat(ValidationUtil.isValidUrl("https://example.com")).isTrue();
        assertThat(ValidationUtil.isValidUrl("invalid-url")).isFalse();
    }

    @Test
    @DisplayName("Should check if numeric")
    void shouldCheckIfNumeric() {
        assertThat(ValidationUtil.isNumeric("12345")).isTrue();
        assertThat(ValidationUtil.isNumeric("abc")).isFalse();
    }

    @Test
    @DisplayName("Should check string length")
    void shouldCheckStringLength() {
        assertThat(ValidationUtil.hasLength("hello", 3, 10)).isTrue();
        assertThat(ValidationUtil.hasLength("hi", 3, 10)).isFalse();
    }
}
