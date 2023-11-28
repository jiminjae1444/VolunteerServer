package com.example.Volunteer.controller;

import com.example.Volunteer.model.Info;
import com.example.Volunteer.model.User;
import com.example.Volunteer.model.VolunteerApplication;
import com.example.Volunteer.model.VolunteerForm;
import com.example.Volunteer.repository.ApplicationRepository;
import com.example.Volunteer.repository.InfoRepository;
import com.example.Volunteer.repository.UserRepository;
import com.example.Volunteer.repository.VolunteerFormRepository;
import com.example.Volunteer.service.VolunteerApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/volunteer-applications")
public class VolunteerApplicationController {

    @Autowired
    private VolunteerApplicationService volunteerApplicationService;

    @PostMapping("/close-registration/{volunteerFormId}")
    public ResponseEntity<String> closeVolunteerRegistration(@PathVariable Long volunteerFormId) {
        volunteerApplicationService.closeVolunteerRegistration(volunteerFormId);
        return ResponseEntity.ok("봉사 신청 마감이 완료되었습니다.");
    }
}
