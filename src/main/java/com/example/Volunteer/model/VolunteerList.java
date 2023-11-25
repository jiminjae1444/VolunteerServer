package com.example.Volunteer.model;

import com.example.Volunteer.controller.VolunteerEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "volunteer_list")
public class VolunteerList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "volunteer_name")
    private String volunteerName;

    @Column(name = "volunteer_hour")
    private int volunteerHour;

    @Column(name = "volunteer_persons")
    private int volunteerPersons;

    @ManyToOne
    @JoinColumn(name = "volunteer_form_id", nullable = false)
    @JsonIgnore
    private VolunteerForm volunteerForm;

    public VolunteerForm getVolunteerForm() {
        return volunteerForm;
    }

    public void setVolunteerForm(VolunteerForm volunteerForm) {
        this.volunteerForm = volunteerForm;
    }

    public VolunteerList() {
        // 기본 생성자
    }
    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public int getVolunteerHour() {
        return volunteerHour;
    }

    public void setVolunteerHour(int volunteerHour) {
        this.volunteerHour = volunteerHour;
    }

    public int getVolunteerPersons() {
        return volunteerPersons;
    }

    public void setVolunteerPersons(int volunteerPersons) {
        this.volunteerPersons = volunteerPersons;
    }
// 기타 필요한 코드 및 생성자, 게터, 세터 등을 추가할 수 있습니다.
}