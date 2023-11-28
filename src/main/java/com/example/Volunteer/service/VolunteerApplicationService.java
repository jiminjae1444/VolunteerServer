package com.example.Volunteer.service;

import com.example.Volunteer.model.Info;
import com.example.Volunteer.model.VolunteerApplication;
import com.example.Volunteer.model.VolunteerForm;
import com.example.Volunteer.model.VolunteerList;
import com.example.Volunteer.repository.ApplicationRepository;
import com.example.Volunteer.repository.InfoRepository;
import com.example.Volunteer.repository.VolunteerFormRepository;
import com.example.Volunteer.repository.VolunteerListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;


@Service
public class VolunteerApplicationService {
    @Autowired
    private VolunteerFormRepository volunteerFormRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private InfoRepository infoRepository;
    @Autowired
    private VolunteerListRepository volunteerListRepository;

    public void closeVolunteerRegistration(Long volunteerFormId) {
        VolunteerForm volunteerForm = volunteerFormRepository.findById(volunteerFormId)
                .orElseThrow(() -> new EntityNotFoundException("Volunteer form not found."));
        if (volunteerForm.getEnd_date().isBefore(LocalDate.now())) {
            // VolunteerApplication 가져오기
            List<VolunteerApplication> applications = applicationRepository.findByVolunteerForm(volunteerForm);

            for (VolunteerApplication application : applications) {
                // VolunteerApplication을 기반으로 봉사 시간 및 등급 업데이트
                updateVolunteerHoursAndGrade(application);
            }
        }
    }

    private void updateVolunteerGrade(Info userInfo) {
        int totalVolunteerHours = userInfo.getTotal_hours();

        if (totalVolunteerHours < 50) {
            userInfo.setGrade("초보자");
        } else if (totalVolunteerHours < 100) {
            userInfo.setGrade("중급자");
        } else {
            userInfo.setGrade("경력자");
        }
        infoRepository.save(userInfo);
    }

    private void updateVolunteerHoursAndGrade(VolunteerApplication application) {
        // 해당 사용자의 봉사 시간을 업데이트
        Info info = application.getInfo();
        info.setTotal_hours(info.getTotal_hours() + application.getVolunteerForm().getRecruitment_hours());
        infoRepository.save(info);

        // 봉사 등급 업데이트
        updateVolunteerGrade(info);
    }
}