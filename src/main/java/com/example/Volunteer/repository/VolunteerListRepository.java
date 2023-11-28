package com.example.Volunteer.repository;

import com.example.Volunteer.model.VolunteerForm;
import com.example.Volunteer.model.VolunteerList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerListRepository extends JpaRepository<VolunteerList, Long> {
    List<VolunteerList> findByVolunteerForm_Title(String title);
}
