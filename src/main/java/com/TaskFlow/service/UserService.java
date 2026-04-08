package com.TaskFlow.service;

import com.TaskFlow.model.User;
import com.TaskFlow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
            .orElseGet(() -> {
                User fallback = new User(username, passwordEncoder.encode("password123"), "Usuario Recuperado", username + "@example.com");
                return userRepository.save(fallback);
            });
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(String username, String rawPassword, String displayName, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        User user = new User(username, passwordEncoder.encode(rawPassword), displayName, email);
        return userRepository.save(user);
    }
}
