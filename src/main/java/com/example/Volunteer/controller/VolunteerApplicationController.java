package com.example.Volunteer.controller;

import com.example.Volunteer.model.*;
import com.example.Volunteer.repository.ApplicationRepository;
import com.example.Volunteer.repository.InfoRepository;
import com.example.Volunteer.repository.UserRepository;
import com.example.Volunteer.repository.VolunteerFormRepository;
import com.example.Volunteer.service.VolunteerApplicationService;
import com.example.Volunteer.service.VolunteerFormNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity applyForVolunteer(@RequestBody VolunteerApplicationRequest applicationRequest) throws VolunteerFormNotFoundException {
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
    public ResponseEntity<List<VolunteerApplicationDTO>> getVolunteerApplicationsForInfo(@PathVariable long infoId) {
        List<VolunteerApplication> applications = applicationRepository.findByInfoId(infoId);
        List<VolunteerApplicationDTO> applicationDTOs = applications.stream()
                .map(VolunteerApplicationDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicationDTOs);
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


