package com.example.Volunteer.repository;

import com.example.Volunteer.model.VolunteerApplication;
import com.example.Volunteer.model.VolunteerForm;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<VolunteerApplication,Long> {
    List<VolunteerApplication> findByVolunteerForm(VolunteerForm volunteerForm);

    List<VolunteerApplication> findByApplicantUsername(String username);

    int countByVolunteerFormId(String volunteerFormName);
}
