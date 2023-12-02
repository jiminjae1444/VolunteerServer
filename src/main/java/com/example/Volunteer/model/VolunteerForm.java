package com.example.Volunteer.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "volunteer_form")
public class VolunteerForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "location")
    private String location;
    @Column(name = "description")
    private String description;
    @Column(name = "start_date")
    private LocalDate start_date;
    @Column(name = "end_date")
    private LocalDate end_date;
    @Column(name = "slots")
    private int slots;
    @Column(name = "recruitment_hours")
    private int recruitment_hours;
    @Column(name = "priority")
    private String priority;
    @OneToMany(mappedBy = "volunteerForm", cascade = CascadeType.ALL)
    private List<VolunteerList> volunteerLists = new ArrayList<>();
    @OneToMany(mappedBy = "volunteerForm", cascade = CascadeType.ALL)
    private List<VolunteerApplication> volunteerApplications = new ArrayList<>();
    @Transient
    private String start_date_str; // 안드로이드 앱에서 전송되는 start_date 문자열
    @Transient
    private String end_date_str; // 안드로이드 앱에서 전송되는 end_date 문자열

    public void addVolunteerList(VolunteerList volunteerList) {
        volunteerLists.add(volunteerList);
        volunteerList.setVolunteerForm(this);
    }
    public boolean isClosed() {
        return LocalDate.now().isAfter(end_date);
    }

}
