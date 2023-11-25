package com.example.Volunteer.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
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

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "applicationdate")
    private LocalDateTime applicationDate;

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

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }
}
