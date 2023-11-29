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

//    @PostMapping("/cancel")
//    public ResponseEntity cancelVolunteerApplication(@RequestParam String volunteerName) {
//        try {
//            // 봉사폼을 찾기 위해 volunteerName을 사용
//            VolunteerForm volunteerForm = (VolunteerForm) volunteerFormRepository.findByTitle(volunteerName)
//                    .orElseThrow(() -> new ApplicationException("봉사폼을 찾을 수 없습니다."));
//
//            // 취소 처리
//            cancelVolunteerApplication(volunteerForm.getId());
//            return new ResponseEntity<>(HttpStatus.OK); // 200 OK
//        } catch (ApplicationException e) {
//            // 에러 발생 시 클라이언트에게 에러 응답 전송
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
//        }
//    }
}

