package com.example.Volunteer.repository;

import com.example.Volunteer.model.Info;
import com.example.Volunteer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InfoRepository extends JpaRepository<Info,Long> {

    Info findByUser(User user);

    Optional<Info> findByUserId(long id);

    Optional<Object> findByUserUsername(String username);

}

