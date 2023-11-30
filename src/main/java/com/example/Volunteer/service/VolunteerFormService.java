package com.example.Volunteer.service;

import com.example.Volunteer.model.VolunteerForm;
import com.example.Volunteer.repository.VolunteerFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VolunteerFormService {
    @Autowired
    private VolunteerFormRepository volunteerFormRepository;

    public void createVolunteer(VolunteerForm volunteerForm) {

        volunteerFormRepository.save(volunteerForm);
    }
    public VolunteerForm getVolunteerById(Long volunteerId) {
        Optional<VolunteerForm> volunteerFormOptional = volunteerFormRepository.findById(volunteerId);

        if (volunteerFormOptional.isPresent()) {
            return volunteerFormOptional.get();
        } else {
            // 봉사 ID에 해당하는 봉사 정보를 찾을 수 없을 때 처리할 내용
            throw new VolunteerNotFoundException("봉사 정보를 찾을 수 없습니다. ID: " + volunteerId);
        }
    }

    public VolunteerForm getVolunteerFormById(long volunteerFormId) {
        return volunteerFormRepository.findById(volunteerFormId) .orElseThrow(() -> new ResourceNotFoundException("VolunteerForm not found with id: " + volunteerFormId));
    }
}

