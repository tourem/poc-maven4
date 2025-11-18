package com.larbotech.maven4.batch.reader;

import com.larbotech.maven4.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader pour les utilisateurs
 */
@Component
@Slf4j
public class UserItemReader implements ItemReader<User> {

    private List<User> users;
    private int nextUserIndex;

    public UserItemReader() {
        this.users = generateUsers();
        this.nextUserIndex = 0;
    }

    @Override
    public User read() {
        User nextUser = null;

        if (nextUserIndex < users.size()) {
            nextUser = users.get(nextUserIndex);
            nextUserIndex++;
        }

        return nextUser;
    }

    private List<User> generateUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            users.add(User.builder()
                .username("user" + i)
                .email("user" + i + "@example.com")
                .firstName("First" + i)
                .lastName("Last" + i)
                .status(User.UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
        }
        return users;
    }
}
