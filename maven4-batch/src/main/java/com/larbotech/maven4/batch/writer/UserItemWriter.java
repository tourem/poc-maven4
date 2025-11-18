package com.larbotech.maven4.batch.writer;

import com.larbotech.maven4.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * Writer pour les utilisateurs
 */
@Component
@Slf4j
public class UserItemWriter implements ItemWriter<User> {

    @Override
    public void write(Chunk<? extends User> chunk) throws Exception {
        for (User user : chunk) {
            log.info("Writing user: {}", user.getUsername());
        }
    }
}
