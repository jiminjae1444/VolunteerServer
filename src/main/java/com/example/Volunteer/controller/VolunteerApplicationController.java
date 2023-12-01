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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/volunteer-applications")
public class VolunteerApplicationController {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private VolunteerFormRepository volunteerFormRepository;
    @Autowired
    private InfoRepository infoRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VolunteerApplicationService volunteerApplicationService;

    @PostMapping("/apply")
    public ResponseEntity applyForVolunteer(@RequestBody VolunteerApplicationRequest applicationRequest) {
        try {
            // 신청 처리
          volunteerApplicationService.apply(applicationRequest);
            return new ResponseEntity<>(HttpStatus.CREATED); // 201 Created
        } catch (ApplicationException e) {
            // 에러 발생 시 클라이언트에게 에러 응답 전송
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
    @GetMapping("/info/{infoId}/applications")
    public ResponseEntity<List<VolunteerApplication>> getVolunteerApplicationsForInfo(@PathVariable long infoId) {
        List<VolunteerApplication> applications = applicationRepository.findByInfoId(infoId);
        return ResponseEntity.ok(applications);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> cancelVolunteerApplication(@PathVariable long applicationId) {
        try {
            // 서비스를 사용하여 봉사 신청을 취소하는 로직을 수행
            volunteerApplicationService.cancelVolunteerApplication(applicationId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // 취소 중 오류가 발생한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


