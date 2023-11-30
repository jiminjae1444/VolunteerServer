package com.example.Volunteer.service;

import com.example.Volunteer.model.User;
import com.example.Volunteer.repository.UserRepository;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User savepassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getId();
        } else {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
    }
}


