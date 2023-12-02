package com.example.Volunteer.repository;

import com.example.Volunteer.model.Info;
import com.example.Volunteer.model.VolunteerApplication;
import com.example.Volunteer.model.VolunteerForm;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<VolunteerApplication,Long> {
    List<VolunteerApplication> findByVolunteerForm(Long volunteerFormId);


    List<VolunteerApplication> findByInfoId(Long infoId);

    Long countByVolunteerFormTitle(String volunteerFormTitle);

    boolean existsByInfoAndVolunteerForm(Info info, VolunteerForm volunteerForm);
}
