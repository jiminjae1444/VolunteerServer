package com.example.Volunteer.repository;

import com.example.Volunteer.model.VolunteerForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VolunteerFormRepository extends JpaRepository<VolunteerForm,Long> {
    @Query("SELECT id FROM VolunteerForm WHERE title = :title")
    Long findIdByTitle(@Param("title") String title);

    VolunteerForm findByTitle(String volunteerName);

    @Query("SELECT vf FROM VolunteerForm vf WHERE vf.end_date < :currentDate")
    List<VolunteerForm> findExpiredForms(@Param("currentDate") LocalDate currentDate);
}
