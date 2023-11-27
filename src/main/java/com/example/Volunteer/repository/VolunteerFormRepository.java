package com.example.Volunteer.repository;

import com.example.Volunteer.model.VolunteerForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VolunteerFormRepository extends JpaRepository<VolunteerForm,Long> {
    @Query("SELECT id FROM VolunteerForm WHERE title = :title")
    Long findIdByTitle(@Param("title") String title);

}
