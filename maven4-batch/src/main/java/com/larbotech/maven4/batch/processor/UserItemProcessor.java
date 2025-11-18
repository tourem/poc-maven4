package com.larbotech.maven4.batch.processor;

import com.larbotech.maven4.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Processor pour les utilisateurs
 */
@Component
@Slf4j
public class UserItemProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) throws Exception {
        log.debug("Processing user: {}", user.getUsername());
        
        // Transformation complexe simulée
        String transformedUsername = user.getUsername().toUpperCase();
        user.setUsername(transformedUsername);
        
        // Simulation de traitement lourd
        Thread.sleep(10);
        
        return user;
    }
}
