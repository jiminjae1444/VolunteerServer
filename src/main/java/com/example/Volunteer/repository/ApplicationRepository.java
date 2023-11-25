package com.example.Volunteer.repository;

import com.example.Volunteer.model.VolunteerApplication;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<VolunteerApplication,Long> {
}
