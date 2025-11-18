package com.larbotech.maven4.batch.processor;

import com.larbotech.maven4.common.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserItemProcessor Tests")
class UserItemProcessorTest {

    private UserItemProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new UserItemProcessor();
    }

    @Test
    @DisplayName("Should transform username to uppercase")
    void shouldTransformUsernameToUppercase() throws Exception {
        // Given
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .status(User.UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        User result = processor.process(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("TESTUSER");
    }
}
