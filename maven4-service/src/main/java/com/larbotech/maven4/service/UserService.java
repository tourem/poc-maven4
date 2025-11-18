package com.larbotech.maven4.service;

import com.larbotech.maven4.common.dto.UserDto;
import com.larbotech.maven4.common.entity.User;
import com.larbotech.maven4.common.exception.ResourceNotFoundException;
import com.larbotech.maven4.common.exception.ValidationException;
import com.larbotech.maven4.common.mapper.UserMapper;
import com.larbotech.maven4.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des utilisateurs
 * Démonstration des fonctionnalités Maven 4
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Créer un nouvel utilisateur
     */
    public UserDto createUser(UserDto userDto) {
        log.info("Creating user: {}", userDto.getUsername());

        // Validation métier
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ValidationException("Username already exists: " + userDto.getUsername());
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ValidationException("Email already exists: " + userDto.getEmail());
        }

        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);

        log.info("User created successfully with id: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    /**
     * Récupérer un utilisateur par ID
     */
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        log.debug("Fetching user with id: {}", id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id));

        return userMapper.toDto(user);
    }

    /**
     * Récupérer tous les utilisateurs
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.debug("Fetching all users");

        return userRepository.findAll().stream()
            .map(userMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Mettre à jour un utilisateur
     */
    public UserDto updateUser(Long id, UserDto userDto) {
        log.info("Updating user with id: {}", id);

        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id));

        // Vérifier l'unicité du username si modifié
        if (!existingUser.getUsername().equals(userDto.getUsername()) &&
            userRepository.existsByUsername(userDto.getUsername())) {
            throw new ValidationException("Username already exists: " + userDto.getUsername());
        }

        // Vérifier l'unicité de l'email si modifié
        if (!existingUser.getEmail().equals(userDto.getEmail()) &&
            userRepository.existsByEmail(userDto.getEmail())) {
            throw new ValidationException("Email already exists: " + userDto.getEmail());
        }

        userMapper.updateEntityFromDto(userDto, existingUser);
        User updatedUser = userRepository.save(existingUser);

        log.info("User updated successfully: {}", id);
        return userMapper.toDto(updatedUser);
    }

    /**
     * Supprimer un utilisateur
     */
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }

    /**
     * Rechercher un utilisateur par username
     */
    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        log.debug("Finding user by username: {}", username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));

        return userMapper.toDto(user);
    }
}
