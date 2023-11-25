package com.example.Volunteer.repository;

import com.example.Volunteer.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

   Optional<User> findById(Long userId);

    boolean existsByUsername(String username);
}
