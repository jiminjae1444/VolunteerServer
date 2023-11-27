package com.example.Volunteer.controller;

import com.example.Volunteer.model.Info;
import com.example.Volunteer.model.User;
import com.example.Volunteer.model.VolunteerApplication;
import com.example.Volunteer.model.VolunteerForm;
import com.example.Volunteer.repository.ApplicationRepository;
import com.example.Volunteer.repository.InfoRepository;
import com.example.Volunteer.repository.UserRepository;
import com.example.Volunteer.repository.VolunteerFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/volunteer-application")
public class VolunteerApplicationController {

    @Autowired
    private VolunteerFormRepository volunteerFormRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private InfoRepository infoRepository;

    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody VolunteerApplicationRequest applicationRequest) {
        VolunteerForm volunteerForm = volunteerFormRepository.findById(applicationRequest.getVolunteerFormId())
                .orElseThrow(() -> new EntityNotFoundException("VolunteerForm not found with id: " + applicationRequest.getVolunteerFormId()));

        // 프라이어리티가 선착순이고, 모집 기간이 시작되었으며, 모집 인원이 다 차지 않은 경우
        if ("선착순".equals(volunteerForm.getPriority())
                && volunteerForm.getStart_date().isAfter(LocalDate.now())
                && volunteerForm.getVolunteerLists().size() < volunteerForm.getSlots()) {
            // 선착순으로 처리하는 로직 추가
            processPriorityApplication(volunteerForm, applicationRequest);
        }
        // 프라이어리티가 경력자만이고, 사용자 등급이 경력자인 경우
        else if ("경력자만".equals(volunteerForm.getPriority())
                && isUserExperienced(applicationRequest.getUserId())) {
            // 경력자만으로 처리하는 로직 추가
            processExperiencedApplication(volunteerForm, applicationRequest);
        } else {
            // 그 외의 경우는 거부 처리
            processRejectApplication(volunteerForm, applicationRequest);
        }

        return ResponseEntity.ok("신청이 처리되었습니다.");
    }

    private void processPriorityApplication(VolunteerForm volunteerForm, VolunteerApplicationRequest applicationRequest) {
        // 여기에 선착순 처리 로직 추가
        if (volunteerForm.getVolunteerLists().size() < volunteerForm.getSlots()) {
            // 모집 인원이 다 차지 않은 경우에만 신청 처리
            processApprovalApplication(volunteerForm, applicationRequest);
        } else {
            processRejectApplication(volunteerForm, applicationRequest);
        }
    }

    private void processExperiencedApplication(VolunteerForm volunteerForm, VolunteerApplicationRequest applicationRequest) {
        // 여기에 경력자만 처리 로직 추가
        processApprovalApplication(volunteerForm, applicationRequest);
    }

    private void processApprovalApplication(VolunteerForm volunteerForm, VolunteerApplicationRequest applicationRequest) {
        VolunteerApplication application = new VolunteerApplication();
        application.setVolunteerForm(volunteerForm);
        application.setInfo(infoRepository.findById(applicationRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Info not found with id: " + applicationRequest.getUserId())));
        application.setStatus("승인대기");
        application.setApplicationDate(LocalDate.now());

        applicationRepository.save(application);

        // 시작일이 지나고 모집 인원이 넘지 않은 경우에만 업데이트
        if (volunteerForm.getStart_date().isBefore(LocalDate.now())
                && volunteerForm.getVolunteerLists().size() <= volunteerForm.getSlots()) {
            // 시작일이 지나면서 모집 인원이 다 차지 않은 경우에만 "신청완료"로 업데이트
            application.setStatus("신청완료");
            applicationRepository.save(application);
        }
    }

    private void processRejectApplication(VolunteerForm volunteerForm, VolunteerApplicationRequest applicationRequest) {
        // 여기에 거부 처리 로직 추가
        VolunteerApplication application = new VolunteerApplication();
        application.setVolunteerForm(volunteerForm);
        application.setInfo(infoRepository.findById(applicationRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Info not found with id: " + applicationRequest.getUserId())));
        application.setStatus("거부");
        application.setApplicationDate(LocalDate.now());

        applicationRepository.save(application);
    }

    private boolean isUserExperienced(Long userId) {
        // 여기에 사용자가 경력자인지 확인하는 로직 추가
        // 사용자 정보를 가져와서 등급이 경력자인지 확인
        return infoRepository.findById(userId)
                .map(Info::getGrade)
                .map("경력자"::equals)
                .orElse(false);
    }
}
