package com.example.Volunteer.model;

import com.example.Volunteer.controller.VolunteerApplicationRequest;
import com.example.Volunteer.repository.InfoRepository;
import com.example.Volunteer.repository.UserRepository;
import com.example.Volunteer.repository.VolunteerFormRepository;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

@Data
@Entity
@Table(name = "volunteer_application")
public class VolunteerApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recruitment_id")
    private VolunteerForm volunteerForm;
    @ManyToOne
    @JoinColumn(name = "info_id")
    private Info info;
    @Column(name = "status")
    private String status;

    @Column(name = "applicationdate")
    private LocalDate applicationDate;

    public VolunteerApplication(VolunteerApplicationRequest applicationRequest) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VolunteerForm getVolunteerForm() {
        return volunteerForm;
    }

    public void setVolunteerForm(VolunteerForm volunteerForm) {
        this.volunteerForm = volunteerForm;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public VolunteerApplication() {
    }

    public VolunteerApplication(VolunteerApplicationRequest applicationRequest, VolunteerFormRepository volunteerFormRepository, UserRepository userRepository, InfoRepository infoRepository) {
        // VolunteerApplicationRequest에서 username을 통해 userid를 찾아온다고 가정
        String username = applicationRequest.getUsername();

        // UserRepository를 통해 username에 해당하는 User를 찾음
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // User 엔티티에서 userid를 얻어냄
            Long userId = user.getId();

            // userid를 이용하여 Info 등의 정보를 가져오는 로직도 추가
            Optional<Info> infoOptional = infoRepository.findByUserId(userId);

            if (infoOptional.isPresent()) {
                Info info = infoOptional.get();

                // VolunteerFormRepository를 통해 volunteerName에 해당하는 VolunteerForm을 찾음
                String volunteerName = applicationRequest.getVolunteerFormName();
                Optional<VolunteerForm> volunteerFormOptional = Optional.ofNullable(volunteerFormRepository.findByTitle(volunteerName));

                if (volunteerFormOptional.isPresent()) {
                    VolunteerForm volunteerForm = volunteerFormOptional.get();

                    // 나머지 로직은 이 정보들을 활용하여 VolunteerApplication 객체를 생성하는 부분
                    this.volunteerForm = volunteerForm;
                    this.info = info;
                    this.status = isBeforeStartDate(volunteerForm) ? "신청 대기" : "신청 완료";
                    this.applicationDate = LocalDate.now();
                } else {
                    throw new RuntimeException("해당 봉사 폼을 찾을 수 없습니다.");
                }
            } else {
                throw new RuntimeException("해당 사용자의 정보를 찾을 수 없습니다.");
            }
        } else {
            throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");
        }
    }



    // 시작일 이전 여부 확인
    private boolean isBeforeStartDate(VolunteerForm volunteerForm) {
        LocalDate startDate = volunteerForm.getStart_date();
        return LocalDate.now().isBefore(startDate);
    }
}
