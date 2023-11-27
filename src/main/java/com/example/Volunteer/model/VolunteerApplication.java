package com.example.Volunteer.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
