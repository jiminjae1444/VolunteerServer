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

//    public String updateUser(User updatedUser) {
//        // 여기에서 사용 자 정보를 업데이트하는 로직을 구현
//        // userRepository.save(updatedUser); 를 사용하여 정보를 업데이트
//        return userRepository.save(updatedUser);
//    }
}

