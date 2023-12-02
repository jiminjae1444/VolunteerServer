package com.example.Volunteer.controller;

public class VolunteerApplicationRequest {
        private String volunteerFormName;
        private String applicantUsername;

    public String getApplicantUsername() {
        return applicantUsername;
    }

    public void setApplicantUsername(String applicantUsername) {
        this.applicantUsername = applicantUsername;
    }

    public String getVolunteerFormName() {
        return volunteerFormName;
    }

    public void setVolunteerFormName(String volunteerFormName) {
        this.volunteerFormName = volunteerFormName;
    }
}

