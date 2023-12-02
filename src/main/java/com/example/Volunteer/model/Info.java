package com.example.Volunteer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "info")
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "total_hours", columnDefinition = "INT DEFAULT 0")
    private int total_hours;
    @Column(name = "grade" ,columnDefinition = "VARCHAR(50) DEFAULT '초보자'")
    private String grade;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "info",cascade = CascadeType.ALL) // 필요에 따라 mappedBy 속성 설정
    private List<VolunteerApplication> volunteerApplications = new ArrayList<>();
    public Info() {
    }
}
