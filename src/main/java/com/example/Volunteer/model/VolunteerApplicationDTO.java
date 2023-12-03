package com.example.Volunteer.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VolunteerApplicationDTO {
    private Long id;
    private String status;
    private LocalDate applicationDate;
    private String volunteerFormTitle;

    public VolunteerApplicationDTO(VolunteerApplication application) {
        this.id = application.getId();
        this.status = application.getStatus();
        this.applicationDate = application.getApplicationDate();
        this.volunteerFormTitle = application.getVolunteerForm() != null ? application.getVolunteerForm().getTitle() : null;
    }
}