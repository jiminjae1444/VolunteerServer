package com.example.Volunteer.service;

import com.example.Volunteer.controller.ApplicationResponse;
import com.example.Volunteer.model.ApplicationStatus;
import com.example.Volunteer.model.Info;
import com.example.Volunteer.model.VolunteerApplication;
import com.example.Volunteer.model.VolunteerForm;
import com.example.Volunteer.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.awt.SystemColor.info;

@Service
public class VolunteerApplicationService {
    @Autowired
    private static InfoService infoService;
    @Autowired
    private static ApplicationRepository applicationRepository;
    @Autowired
    private static VolunteerFormService volunteerFormService;
    public static ApplicationResponse applyForVolunteer(Long volunteerId, Long infoId) {
        // 여기에서 봉사 신청 로직을 구현합니다.
        // 필요한 봉사 정보와 사용자 정보를 확인하고 신청을 처리합니다.
        Info info = infoService.getInfoById(infoId);

        // VolunteerFormService를 사용하여 봉사 정보 가져오기
        VolunteerForm volunteerForm = volunteerFormService.getVolunteerById(volunteerId);

        if (isApplicationOpen(volunteerForm)) {
            VolunteerApplication application = new VolunteerApplication();
            application.setId(volunteerId);
            application.setInfo(info);

            if ("경력자만".equals(volunteerForm.getPriority())) {
                // Priority가 "경력자만"인 경우 사용자 등급 검사
                if (!userHasRequiredGrade(info)) {
                    application.setStatus(ApplicationStatus.valueOf("REJECTED"));
                    return new ApplicationResponse(false , "봉사 신청에 실패했습니다. 조건을 다시 확인해주세요.", null);
                }
            }
            //Priority가 "선착순"이든 사용자 등급에 관계없이 승인 처리
            application.setStatus(ApplicationStatus.valueOf("PENDDING"));
            applicationRepository.save(application);
            return new ApplicationResponse(true, "봉사 신청이 성공적으로 등록되었습니다.", application.getId());
        } else {
            VolunteerApplication application = new VolunteerApplication();
            application.setStatus(ApplicationStatus.valueOf("REJECTED"));
            return new ApplicationResponse(false , "봉사 신청에 실패했습니다. 조건을 다시 확인해주세요.", null);
        }
    }
    public static boolean userHasRequiredGrade(Info info) {
        String userGrade = String.valueOf(info.getGrade());

        // 필요한 등급을 정의합니다. 이 예시에서는 "경력자"가 필요한 등급입니다.
        String requiredGrade = "경력자";

        // 사용자의 등급과 필요한 등급을 비교하여 확인합니다.
        return userGrade.equals(requiredGrade);
    }

    public static boolean isApplicationOpen(VolunteerForm volunteerForm) {
        // 여기에서 봉사 신청 가능 여부를 확인합니다.
        // 마감일 등을 확인하여 봉사 신청 가능 여부를 결정합니다.
        LocalDate currentDateTime = LocalDate.now();

        // 봉사활동의 시작일 및 마감일을 가져옵니다.
        LocalDate startDate = volunteerForm.getStart_date();
        LocalDate endDate = volunteerForm.getEnd_date();

        // 현재 날짜와 시간이 시작일 이후이고 마감일 이전이면 봉사 신청이 열려 있습니다.
        return currentDateTime.isAfter(startDate) && currentDateTime.isBefore(endDate);
    }
}
