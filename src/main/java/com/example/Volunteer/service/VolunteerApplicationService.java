package com.example.Volunteer.service;

import com.example.Volunteer.controller.ApplicationException;
import com.example.Volunteer.controller.VolunteerApplicationRequest;
import com.example.Volunteer.model.*;
import com.example.Volunteer.repository.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

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

    private Set<Long> processedInfoFormPairs = new HashSet<>();

    public void updateVolunteerHoursForExpiredForms() {
        // 현재 날짜를 가져오기
        LocalDate currentDate = LocalDate.now();

        // 현재 날짜를 기준으로 종료일(end_date)이 지난 봉사 폼을 가져오기
        List<VolunteerForm> expiredForms = volunteerFormRepository.findExpiredForms(currentDate);

        // 모든 봉사 신청 정보 가져오기
        Iterable<VolunteerApplication> applicationsAll = applicationRepository.findAll();

        // 봉사 폼과 봉사 신청 정보 비교
        for (VolunteerForm form : expiredForms) {
            for (VolunteerApplication application : applicationsAll) {
                // 현재 날짜보다 지난 봉사 폼과 매칭된 봉사 신청 정보 처리
                if (Objects.equals(application.getVolunteerForm().getId(), form.getId())) {
                    updateVolunteerHoursAndGrade(form, application);
                }
            }
        }
    }

    private void updateVolunteerHoursAndGrade(VolunteerForm form, VolunteerApplication application) {
        int recruitmentHours = form.getRecruitment_hours();
        Long infoId = application.getInfo().getId();
        Long formId = form.getId();

        // 이미 처리된 (infoId, formId) 쌍이라면 건너뛰기
        if (processedInfoFormPairs.contains(getInfoFormPairKey(infoId, formId))) {
            return;
        }

        Optional<Info> optionalInfo = infoRepository.findById(infoId);
        if (optionalInfo.isPresent()) {
            Info info = optionalInfo.get();
            info.setTotal_hours(info.getTotal_hours() + recruitmentHours);
            infoRepository.save(info);

            // 처리된 (infoId, formId) 쌍 저장
            processedInfoFormPairs.add(getInfoFormPairKey(infoId, formId));
        }
    }

    private Long getInfoFormPairKey(Long infoId, Long formId) {
        return infoId * 1000000L + formId; // 임의의 방법으로 Long으로 변환
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
    public void apply(@NotNull VolunteerApplicationRequest applicationRequest) throws VolunteerFormNotFoundException {
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
        try {
            return Optional.ofNullable(volunteerFormRepository.findByTitle(volunteerFormName))
                    .map(volunteerForm -> LocalDate.now().isBefore(volunteerForm.getStart_date()))
                    .orElse(false);
        } catch (Exception e) {
            // 예외가 발생하면 로그에 기록하고 false를 반환하거나 적절한 방식으로 처리합니다.
            e.printStackTrace(); // 실제 프로덕션 코드에서는 로깅 라이브러리를 사용하세요.
            return false;
        }
    }


    private boolean isRecruitmentNotFull(String volunteerFormName) {
        Optional<VolunteerForm> volunteerFormOptional = Optional.ofNullable(volunteerFormRepository.findByTitle(volunteerFormName));

        if (volunteerFormOptional.isPresent()) {
            VolunteerForm volunteerForm = volunteerFormOptional.get();

            // 봉사 폼의 모집 인원과 현재 신청된 인원을 가져오기
            int recruitmentSlots = volunteerForm.getSlots();
            Long currentApplications = applicationRepository.countByVolunteerFormTitle(volunteerFormName);

            // 모집 인원이 꽉 차지 않았는지 확인
            return currentApplications < recruitmentSlots;
        } else {
            throw new RuntimeException("봉사 폼을 찾을 수 없습니다.");
        }
    }

    private void processApplicationByPriority(VolunteerApplicationRequest applicationRequest) throws VolunteerFormNotFoundException {
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

    public void processFirstComeFirstServe(VolunteerApplicationRequest applicationRequest) {
        String username = applicationRequest.getApplicantUsername();

        // 클라이언트로부터 전달받은 username을 사용하여 Info를 찾아오기
        Info info = (Info) infoRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 username에 대한 Info를 찾을 수 없습니다."));

        String volunteerFormName = applicationRequest.getVolunteerFormName();

        // 현재까지의 신청 건수 확인
        Long currentApplications = applicationRepository.countByVolunteerFormTitle(volunteerFormName);

        // 봉사 폼의 모집 인원 확인
        Optional<VolunteerForm> volunteerFormOptional = Optional.ofNullable(volunteerFormRepository.findByTitle(volunteerFormName));

        if (volunteerFormOptional.isPresent()) {
            VolunteerForm volunteerForm = volunteerFormOptional.get();
            int recruitmentSlots = volunteerForm.getSlots();

            // 모집 인원이 다 차지 않았으면 선착순 처리
            if (currentApplications < recruitmentSlots) {
                // 추가적인 처리가 필요하다면 이 부분에 로직을 추가하면 됩니다.

                // 기존에 해당 info_id와 recruitment_id의 조합이 이미 존재하는지 확인
                boolean isDuplicateApplication = applicationRepository.existsByInfoAndVolunteerForm(info, volunteerForm);

                if (!isDuplicateApplication) {
                    // 선착순으로 신청 정보 저장
                    VolunteerApplication volunteerApplication = new VolunteerApplication();
                    volunteerApplication.setInfo(info);
                    volunteerApplication.setVolunteerForm(volunteerForm);
                    volunteerApplication.setStatus(isBeforeStartDateCheck(volunteerForm.getStart_date()));
                    volunteerApplication.setApplicationDate(LocalDate.now());

                    applicationRepository.save(volunteerApplication);
                } else {
                    // 이미 해당 조합이 존재한다면 에러 처리 또는 적절한 조치를 취할 수 있습니다.
                    throw new ApplicationException("이미 봉사 신청한 내역이 있습니다.");
                }
            } else {
                // 모집 인원이 다 찼으면 해당 메시지를 클라이언트에 전달하거나 예외를 던져 처리할 수 있습니다.
                throw new ApplicationException("봉사 모집인원을 초과합니다.");
            }
        } else {
            throw new RuntimeException("봉사 폼을 찾을 수 없습니다.");
        }
    }

    // 위 메서드의 수정된 부분
    public String isBeforeStartDateCheck(LocalDate startDate) {
        try {
            return LocalDate.now().isBefore(startDate) ? "신청 대기" : "신청 완료";
        } catch (Exception e) {
            // 예외가 발생하면 로그에 기록하고 적절한 방식으로 처리합니다.
            e.printStackTrace(); // 실제 프로덕션 코드에서는 로깅 라이브러리를 사용하세요.
            return "상태 확인 불가"; // 적절한 에러 상태를 반환하거나 다른 처리를 수행할 수 있습니다.
        }
    }



    private void processExperiencedOnly(VolunteerApplicationRequest applicationRequest) throws VolunteerFormNotFoundException {
        if (isExperienced(applicationRequest.getApplicantUsername())) {
            Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(applicationRequest.getApplicantUsername()));
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Optional<Info> infoOptional = infoRepository.findByUserId(user.getId());

                if (infoOptional.isPresent()) {
                    Info info = infoOptional.get();
                    String volunteerFormName = applicationRequest.getVolunteerFormName();
                    Long currentApplications = applicationRepository.countByVolunteerFormTitle(volunteerFormName);
                    Optional<VolunteerForm> volunteerFormOptional = Optional.ofNullable(volunteerFormRepository.findByTitle(volunteerFormName));

                    if (volunteerFormOptional.isPresent()) {
                        VolunteerForm volunteerForm = volunteerFormOptional.get();
                        int recruitmentSlots = volunteerForm.getSlots();

                        if (currentApplications < recruitmentSlots) {
                            boolean isDuplicateApplication = applicationRepository.existsByInfoAndVolunteerForm(info, volunteerForm);

                            if (!isDuplicateApplication) {
                                VolunteerApplication volunteerApplication = new VolunteerApplication();
                                volunteerApplication.setInfo(info);
                                volunteerApplication.setVolunteerForm(volunteerForm);
                                volunteerApplication.setStatus(isBeforeStartDateCheck(volunteerForm.getStart_date()));
                                volunteerApplication.setApplicationDate(LocalDate.now());

                                applicationRepository.save(volunteerApplication);
                            } else {
                                throw new ApplicationException("봉사 모집인원을 초과합니다.");
                            }
                        } else {
                            throw new VolunteerFormNotFoundException("봉사 폼을 찾을 수 없습니다.");
                        }
                    } else {
                        throw new RuntimeException("봉사 폼을 찾을 수 없습니다.");
                    }
                } else {
                    throw new RuntimeException("유저 정보를 찾을 수 없습니다.");
                }
            } else {
                // 사용자가 경력자가 아닌 경우 400 Bad Request를 전송
                throw new ApplicationException("경력자만 해당 봉사에 신청할 수 있습니다.");
            }
        } else {
            // 사용자가 경력자가 아닌 경우 400 Bad Request를 전송
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
    public void cancelVolunteerApplication(Long applicationId) {
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
    // 시작일이 지난 경우 신청대기인 봉사 신청 상태를 신청완료로 업데이트
    public void updateStatusForExpiredApplications() {
        // 현재 날짜를 가져오기
        LocalDate currentDate = LocalDate.now();

        // 시작일이 지난 봉사 신청을 조회
        List<VolunteerApplication> expiredApplications = applicationRepository.findByStatusAndApplicationDateBefore(currentDate);

        // 각 봉사 신청 상태를 신청완료로 업데이트
        for (VolunteerApplication application : expiredApplications) {
            if(application.getStatus().equals("신청완료")){
                continue;
            }
            application.setStatus("신청완료");
            applicationRepository.save(application);
        }
    }
}

