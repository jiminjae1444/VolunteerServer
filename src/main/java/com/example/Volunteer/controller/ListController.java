package com.example.Volunteer.controller;


import com.example.Volunteer.model.VolunteerList;
import com.example.Volunteer.repository.VolunteerListRepository;
import com.example.Volunteer.service.VolunteerListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/volunteer/list")
public class ListController {

    @Autowired
    private VolunteerListService volunteerListService;
    @Autowired
    private VolunteerListRepository volunteerListRepository;

    private List<VolunteerList> volunteerList = new ArrayList<>();
    @GetMapping("/getVolunteerList")
    public List<VolunteerList> getVolunteerList() {
        // 여기서는 간단한 더미 데이터를 반환하도록 하겠습니다.
        return volunteerListRepository.findAll(); // 모든 데이터 가져오기
    }
    // 기타 필요한 엔드포인트 및 메서드 추가 가능
}