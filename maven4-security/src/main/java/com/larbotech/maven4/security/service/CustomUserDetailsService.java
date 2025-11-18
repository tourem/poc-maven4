package com.larbotech.maven4.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service personnalisé pour charger les détails utilisateur
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user by username: " + username);
        
        // Simulation - dans un vrai projet, charger depuis la base de données
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6") // password
                    .authorities(new ArrayList<>())
                    .build();
        }
        
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
