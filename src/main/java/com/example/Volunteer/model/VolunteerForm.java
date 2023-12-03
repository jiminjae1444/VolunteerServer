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
    @OneToMany(mappedBy = "volunteerForm", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<VolunteerApplication> volunteerApplications = new ArrayList<>();

    @Override
    public String toString() {
        return "VolunteerForm{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", slots=" + slots +
                ", recruitment_hours=" + recruitment_hours +
                ", priority='" + priority + '\'' +
                '}';
    }
}
