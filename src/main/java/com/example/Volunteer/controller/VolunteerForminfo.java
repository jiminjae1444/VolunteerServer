package com.example.Volunteer.controller;

import java.time.LocalDate;

public class VolunteerForminfo {
    private String title;
    private String location;

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    public String getRecruitment_hours() {
        return recruitment_hours;
    }

    public void setRecruitment_hours() {
        this.recruitment_hours = recruitment_hours;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setRecruitment_hours(String recruitment_hours) {
        this.recruitment_hours = recruitment_hours;
    }

    private String description;
    private String start_date;
    private String end_date;
    private String slots;
    private String recruitment_hours;
    private String priority;
}
