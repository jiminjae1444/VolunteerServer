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
    @Modifying
    @Query("UPDATE Info i SET i.total_hours = i.total_hours + :hours WHERE i.user.id = :userId")
    void updateTotalHours(@Param("userId") Long userId, @Param("hours") int hours);

    @Modifying
    @Query("UPDATE Info i SET i.grade = CASE " +
            "WHEN i.total_hours < 50 THEN '초보자' " +
            "WHEN i.total_hours < 100 THEN '중급자' " +
            "ELSE '경력자' END " +
            "WHERE i.user.id = :userId")
    void updateGrade(@Param("userId") Long userId);
    Info findByUser(User user);
}

