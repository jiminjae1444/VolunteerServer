package com.example.Volunteer.service;

import com.example.Volunteer.controller.ApplicationException;
import com.example.Volunteer.controller.VolunteerApplicationRequest;
import com.example.Volunteer.model.*;
import com.example.Volunteer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;


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
    @Autowired
    private UserRepository userRepository;

    public void updateVolunteerHoursForExpiredForms() {
        List<VolunteerForm> expiredForms = volunteerFormRepository.findExpiredForms(LocalDate.now());

        for (VolunteerForm form : expiredForms) {
            List<VolunteerApplication> applications = applicationRepository.findByVolunteerForm(form);

            for (VolunteerApplication application : applications) {
                updateVolunteerHoursAndGrade(application);
            }
        }
    }

    public void updateVolunteerGradeForAllUsers() {
        List<Info> allUsers = infoRepository.findAll();

        for (Info userInfo : allUsers) {
            updateVolunteerGrade(userInfo);
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
        VolunteerForm volunteerForm = application.getVolunteerForm();
        int recruitmentHours = volunteerForm.getRecruitment_hours();

        // 해당 봉사 신청에 참여한 모든 사용자들의 정보 가져오기
        List<VolunteerApplication> applications = applicationRepository.findByVolunteerForm(volunteerForm);

        for (VolunteerApplication app : applications) {
            Info info = app.getInfo();
            info.setTotal_hours(info.getTotal_hours() + recruitmentHours);
            infoRepository.save(info);
        }
    }

    public List<VolunteerApplication> getApplicationsByUsername(String username) {
        return applicationRepository.findByApplicantUsername(username);
    }

    public void apply(VolunteerApplicationRequest applicationRequest) {
        // 신청 처리 로직 추가
        // 예시로 시작일 전이고, 모집 인원이 넘지 않으면서 우선순위에 따라 처리하도록 함
        // (이 부분은 실제로는 데이터베이스 등에서 확인하여 로직을 작성해야 함)

        // 봉사 시작일 전인지 확인
        if (isBeforeStartDate(applicationRequest.getVolunteerFormName())) {
            // 모집 인원 확인
            if (isRecruitmentNotFull(applicationRequest.getVolunteerFormName())) {
                // 우선순위에 따라 처리
                processApplicationByPriority(applicationRequest);
            } else {
                throw new ApplicationException("봉사 모집이 마감되었습니다.");
            }
        } else {
            throw new ApplicationException("봉사 신청일이 지났습니다.");
        }
    }


    // 다른 필요한 메서드들 추가

    private boolean isBeforeStartDate(String volunteerFormName) {
        Optional<VolunteerForm> volunteerFormOptional = Optional.ofNullable(volunteerFormRepository.findByTitle(volunteerFormName));

        if (volunteerFormOptional.isPresent()) {
            VolunteerForm volunteerForm = volunteerFormOptional.get();

            // 봉사 폼의 시작일을 가져오기
            LocalDate startDate = volunteerForm.getStart_date();

            // 현재 날짜와 비교하여 시작일이 이전인지 확인
            return LocalDate.now().isBefore(startDate);
        } else {
            throw new RuntimeException("봉사 폼을 찾을 수 없습니다.");
        }
    }

    private boolean isRecruitmentNotFull(String volunteerFormName) {
        Optional<VolunteerForm> volunteerFormOptional = Optional.ofNullable(volunteerFormRepository.findByTitle(volunteerFormName));

        if (volunteerFormOptional.isPresent()) {
            VolunteerForm volunteerForm = volunteerFormOptional.get();

            // 봉사 폼의 모집 인원과 현재 신청된 인원을 가져오기
            int recruitmentSlots = volunteerForm.getSlots();
            int currentApplications = applicationRepository.countByVolunteerFormId(volunteerFormName);

            // 모집 인원이 꽉 차지 않았는지 확인
            return currentApplications <= recruitmentSlots;
        } else {
            throw new RuntimeException("봉사 폼을 찾을 수 없습니다.");
        }
    }

    private void processApplicationByPriority(VolunteerApplicationRequest applicationRequest) {
        Optional<VolunteerForm> volunteerFormOptional = Optional.ofNullable(volunteerFormRepository.findByTitle(applicationRequest.getVolunteerFormName()));

        if (volunteerFormOptional.isPresent()) {
            VolunteerForm volunteerForm = volunteerFormOptional.get();
            String priority = volunteerForm.getPriority();

            switch (priority) {
                case "선착순":
                    // 선착순 처리 로직 추가
                    processFirstComeFirstServe(applicationRequest);
                    break;
                case "경력자만":
                    // 경력자만 처리 로직 추가
                    processExperiencedOnly(applicationRequest);
                    break;
                default:
                    throw new ApplicationException("알 수 없는 봉사 우선순위입니다.");
            }
        } else {
            throw new RuntimeException("봉사 폼을 찾을 수 없습니다.");
        }
    }

    private void processFirstComeFirstServe(VolunteerApplicationRequest applicationRequest) {
        String volunteerFormName = applicationRequest.getVolunteerFormName();

        // 현재까지의 신청 건수 확인
        int currentApplications = applicationRepository.countByVolunteerFormId(volunteerFormName);

        // 봉사 폼의 모집 인원 확인
        Optional<VolunteerForm> volunteerFormOptional = Optional.ofNullable(volunteerFormRepository.findByTitle(volunteerFormName));

        if (volunteerFormOptional.isPresent()) {
            VolunteerForm volunteerForm = volunteerFormOptional.get();
            int recruitmentSlots = volunteerForm.getSlots();

            // 모집 인원이 다 차지 않았으면 선착순 처리
            if (currentApplications < recruitmentSlots) {
                // 추가적인 처리가 필요하다면 이 부분에 로직을 추가하면 됩니다.
                // 선착순으로 신청 정보 저장
                applicationRepository.save(new VolunteerApplication(applicationRequest, volunteerFormRepository, userRepository, infoRepository));
            } else {
                // 모집 인원이 다 찼으면 해당 메시지를 클라이언트에 전달하거나 예외를 던져 처리할 수 있습니다.
                throw new ApplicationException("봉사 모집인원을 초과합니다.");
            }
        } else {
            throw new RuntimeException("봉사 폼을 찾을 수 없습니다.");
        }
    }


    private void processExperiencedOnly(VolunteerApplicationRequest applicationRequest) {
        // 경력자만 처리 로직 추가
        // 실제로는 경력자 여부를 확인하고 저장하는 등의 작업이 들어감
        // 예시로는 경력자 여부를 확인하는 isExperienced 메서드를 사용
        if (isExperienced(applicationRequest.getUsername())) {
            // 경력자인 경우에만 처리
            // ...
            String volunteerFormName = applicationRequest.getVolunteerFormName();

            // 현재까지의 신청 건수 확인
            int currentApplications = applicationRepository.countByVolunteerFormId(volunteerFormName);

            // 봉사 폼의 모집 인원 확인
            Optional<VolunteerForm> volunteerFormOptional = Optional.ofNullable(volunteerFormRepository.findByTitle(volunteerFormName));

            if (volunteerFormOptional.isPresent()) {
                VolunteerForm volunteerForm = volunteerFormOptional.get();
                int recruitmentSlots = volunteerForm.getSlots();

                // 모집 인원이 다 차지 않았으면 선착순 처리
                if (currentApplications < recruitmentSlots) {
                    // 추가적인 처리가 필요하다면 이 부분에 로직을 추가하면 됩니다.

                    // 선착순으로 신청 정보 저장
                    applicationRepository.save(new VolunteerApplication(applicationRequest, volunteerFormRepository, userRepository, infoRepository));
                } else {
                    // 모집 인원이 다 찼으면 해당 메시지를 클라이언트에 전달하거나 예외를 던져 처리할 수 있습니다.
                    throw new ApplicationException("봉사 모집인원을 초과합니다.");
                }
            } else {
                throw new RuntimeException("봉사 폼을 찾을 수 없습니다.");
            }
        } else {
            throw new ApplicationException("경력자만 해당 봉사에 신청할 수 있습니다.");
        }
    }

    private boolean isExperienced(String username) {
        // 1. username으로 user 정보 찾기
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

        // 2. user 정보가 존재하는지 확인
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 3. user의 id를 통해 Info 정보 찾기
            Optional<Info> infoOptional = infoRepository.findByUserId(user.getId());

            // 4. Info 정보가 존재하고, grade가 '경력자'인지 확인
            return infoOptional.map(info -> "경력자".equals(info.getGrade())).orElse(false);
        } else {
            // user 정보가 존재하지 않으면 경력자가 아님
            return false;
        }
    }

    public List<VolunteerApplication> getVolunteerApplications(Long userId) {
        return applicationRepository.findByUserId(userId);
    }


    public void cancelVolunteerApplication(long applicationId) {
        // applicationId에 해당하는 봉사 신청을 찾아서 삭제
        Optional<VolunteerApplication> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isPresent()) {
            VolunteerApplication application = optionalApplication.get();
            applicationRepository.delete(application);
        } else {
            // 해당 ID에 해당하는 봉사 신청이 없는 경우 예외 처리
            throw new VolunteerApplicationNotFoundException("Volunteer Application not found for id: " + applicationId);
        }
    }
}

