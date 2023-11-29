package com.example.Volunteer.controller;

public class VolunteerApplicationRequest {
        private String volunteerFormName;
        private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVolunteerFormName() {
        return volunteerFormName;
    }

    public void setVolunteerFormName(String volunteerFormName) {
        this.volunteerFormName = volunteerFormName;
    }
}

